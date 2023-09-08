package com.hawolt.client.resources.ledge.challenges;

import com.hawolt.client.LeagueClient;
import com.hawolt.client.resources.ledge.AbstractLedgeEndpoint;
import com.hawolt.generic.Constant;
import com.hawolt.http.OkHttp3Client;
import okhttp3.Request;
import okhttp3.RequestBody;

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
