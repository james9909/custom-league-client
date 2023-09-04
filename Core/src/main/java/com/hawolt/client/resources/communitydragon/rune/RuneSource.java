package com.hawolt.client.resources.communitydragon.rune;

import com.hawolt.client.resources.communitydragon.CommunityDragonSource;
import com.hawolt.http.OkHttp3Client;
import com.hawolt.http.layer.IResponse;
import com.hawolt.io.Core;
import com.hawolt.io.RunLevel;
import com.hawolt.logger.Logger;
import okhttp3.Request;
import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created: 29/08/2023 20:11
 * Author: Twitter @hawolt
 **/

public class RuneSource implements CommunityDragonSource<RuneIndex> {

    public static final RuneSource INSTANCE = new RuneSource();

    private RuneIndex cache;

    @Override
    public String getSource(String... args) {
        String source = "http://ddragon.leagueoflegends.com/cdn/%s/data/en_US/runesReforged.json";
        for (String arg : args) {
            source = String.format(source, arg);
        }
        return source;
    }

    @Override
    public RuneIndex get(String... args) {
        if (cache != null) return cache;
        Request request = new Request.Builder()
                .url(getSource(args))
                .header("User-Agent", "hawolt-custom-client-core")
                .get()
                .build();
        try (InputStream stream = RunLevel.get("runes.json")) {
            String local = Core.read(stream).toString();
            IResponse response = OkHttp3Client.execute(request);
            cache = new RuneIndex(response, new JSONArray(local));
        } catch (IOException e) {
            Logger.warn("failed to load {}", getClass().getSimpleName());
        }
        return cache != null ? cache : new RuneIndex();
    }
}
