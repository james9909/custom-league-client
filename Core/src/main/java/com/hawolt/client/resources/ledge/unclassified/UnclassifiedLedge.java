package com.hawolt.client.resources.ledge.unclassified;

import com.hawolt.client.LeagueClient;
import com.hawolt.client.resources.ledge.AbstractLedgeEndpoint;
import com.hawolt.http.OkHttp3Client;
import com.hawolt.http.layer.IResponse;
import okhttp3.Request;

import java.io.IOException;

/**
 * Created: 19/01/2023 16:38
 * Author: Twitter @hawolt
 **/

public class UnclassifiedLedge extends AbstractLedgeEndpoint {
    public UnclassifiedLedge(LeagueClient client) {
        super(client);
    }

    public IResponse getEndOfGame(long gameId) throws IOException {
        String uri = String.format("%s/stats/endOfGame/region/%s/gameId/%s/puuid/%s",
                base,
                client.getPlayerPlatform().name(),
                gameId,
                userInformation.getSub()
        );
        String agent = String.format("LeagueOfLegendsClient/%s (%s)",
                leagueVersionSupplier.getVersionValue(platform, "LeagueClientUxRender.exe"),
                "rcp-be-lol-end-of-game"
        );
        Request request = new Request.Builder()
                .url(uri)
                .addHeader("Authorization", auth())
                .addHeader("User-Agent", agent)
                .addHeader("Accept", "application/json")
                .build();
        return OkHttp3Client.execute(request, gateway);
    }


    @Override
    public int version() {
        throw new RuntimeException("Unclassified Ledge requires manual request construction");
    }

    @Override
    public String name() {
        throw new RuntimeException("Unclassified Ledge requires manual request construction");
    }

    @Override
    public String rcp() {
        throw new RuntimeException("Unclassified Ledge requires manual request construction");
    }
}
