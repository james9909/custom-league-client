package com.hawolt.client.resources.ledge.challenges;

import com.hawolt.client.LeagueClient;
import com.hawolt.client.resources.ledge.AbstractLedgeEndpoint;
import com.hawolt.generic.Constant;
import com.hawolt.http.OkHttp3Client;
import com.hawolt.http.layer.IResponse;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created: 19/01/2023 16:38
 * Author: Twitter @hawolt
 **/

public class ChallengeLedge extends AbstractLedgeEndpoint {
    public ChallengeLedge(LeagueClient client) {
        super(client);
    }

    public boolean notify(long gameId) throws IOException {
        String uri = String.format("%s/%s/v%s/process-game/%s",
                base,
                name(),
                version(),
                gameId
        );
        Request request = jsonRequest(uri)
                .post(RequestBody.create(new byte[0], Constant.APPLICATION_JSON))
                .build();
        return OkHttp3Client.execute(request, gateway).code() == 204;
    }

    public JSONObject getChallengePoints() throws IOException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(base.substring(base.lastIndexOf('/') + 1))
                .addPathSegment(name())
                .addPathSegment("v" + version())
                .addPathSegment("summary-player-data")
                .addQueryParameter("puuid", userInformation.getSub())
                .addQueryParameter("includeSelected", "true")
                .build();
        Request request = jsonRequest(url)
                .post(RequestBody.create(new JSONArray().toString(), Constant.APPLICATION_JSON))
                .build();
        IResponse response = OkHttp3Client.execute(request, gateway);
        JSONObject object = new JSONObject(response.asString());
        return object.getJSONObject("totalPoints");
    }


    @Override
    public int version() {
        return 2;
    }

    @Override
    public String name() {
        return "challenges-client";
    }

    @Override
    public String rcp() {
        return "rcp-be-lol-challenges";
    }
}
