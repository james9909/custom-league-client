package com.hawolt.ui.github;

import com.hawolt.generic.util.Network;
import com.hawolt.http.NativeHttpClient;
import com.hawolt.http.layer.IResponse;
import com.hawolt.logger.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;

public class Github {

    private static final String BASE_URL = "https://api.github.com/repos/hawolt/custom-league-client";

    private static final String BUG_URL = "https://github.com/hawolt/custom-league-client/issues/new?assignees=&labels=bug&projects=&template=bug_report.md&title=%5BBUG%5D";

    private static JSONObject latestRelease;
    private static JSONArray releases;
    private static JSONArray contributorsList;
    private static JSONArray issues;

    private Github() {
    }

    public static JSONObject getLatestRelease() {
        String url = BASE_URL + "/releases/latest";

        if (latestRelease == null) {
            latestRelease = doObjectRequest(url);
        }

        return latestRelease;
    }

    public static JSONArray getReleases() {
        String url = BASE_URL + "/releases";

        if (releases == null) {
            releases = doArrayRequest(url);
        }

        return releases;
    }

    public static JSONArray getContributorsList() {
        String url = BASE_URL + "/contributors";

        if (contributorsList == null) {
            contributorsList = doArrayRequest(url);
        }

        return contributorsList;
    }

    public static JSONArray getIssues() {
        String url = BASE_URL + "/issues";

        if (issues == null) {
            issues = doArrayRequest(url);
        }

        return issues;
    }

    private static JSONObject doObjectRequest(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        try {
            IResponse response = NativeHttpClient.execute(request);
            return new JSONObject(response.asString());
        } catch (IOException | InterruptedException e) {
            Logger.error("request failed to " + url);
            return new JSONObject();
        }
    }

    private static JSONArray doArrayRequest(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        try {
            IResponse response = NativeHttpClient.execute(request);
            return new JSONArray(response.asString());
        } catch (IOException | InterruptedException e) {
            Logger.error("request failed to " + url);
            return new JSONArray();
        }
    }

    private static JSONObject getVersion() {
        JSONObject currentVersion = new JSONObject();
        JSONArray versions = getReleases();
        Properties props = new Properties();
        try {
            props.load(Github.class.getClassLoader().getResourceAsStream("project.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String version = props.getProperty("version");
        version = version.replace("-", "-release-");
        System.out.println(version);
        for (int i = 0; i < versions.length(); i++) {
            if (version.equals(versions.getJSONObject(i).get("name")))
                currentVersion = versions.getJSONObject(i);
        }
        return currentVersion;
    }

    public static String getChangelog() {
        return getVersion().get("body").toString();
    }

    public static String getCurrentVersion() {
        return getVersion().get("name").toString();
    }

    public static String getLatestVersion() {
        return getLatestRelease().get("name").toString();
    }

    public static String getCurrentReleaseDate() {
        return date(getVersion().get("published_at").toString());
    }

    public static String getLatestReleaseDate() {
        return date(getLatestRelease().get("published_at").toString());
    }

    public static void submitBug() {
        try {
            Network.browse(BUG_URL);
        } catch (IOException e) {
            Logger.error("failed to request bug submit");
        }
    }

    private static String date(String date) {
        Date release = Date.from(Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(date)));
        return release.toString();
    }
}
