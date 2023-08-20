package com.hawolt.client.resources.ledge.inventory;

import com.hawolt.client.LeagueClient;
import com.hawolt.client.resources.ledge.AbstractLedgeEndpoint;
import com.hawolt.http.OkHttp3Client;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created: 19/01/2023 16:38
 * Author: Twitter @hawolt
 **/

public class InventoryServiceLedge extends AbstractLedgeEndpoint {
    public InventoryServiceLedge(LeagueClient client, String base) {
        super(client, base);
    }

    public String getInventoryToken() throws IOException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(base.substring(base.lastIndexOf('/') + 1))
                .addPathSegment(name())
                .addPathSegment("v" + version())
                .addPathSegment("inventories")
                .addPathSegment("simple")
                .addQueryParameter("puuid", userInformation.getSub())
                .addQueryParameter("location", String.format("lolriot.ams1.%s", platform.name().toLowerCase()))
                .addQueryParameter("inventoryTypes", "CHAMPION")
                .addQueryParameter("inventoryTypes", "CHAMPION_SKIN")
                .addQueryParameter("accountId", String.valueOf(client.getVirtualRiotClient().getRiotClientUser().getDataUserId()))
                .build();
        Request request = jsonRequest(url).get().build();
        Call call = OkHttp3Client.perform(request, gateway);
        try (Response response = call.execute()) {
            try (ResponseBody body = response.body()) {
                String plain = body.string();
                JSONObject object = new JSONObject(plain);
                JSONObject data = object.getJSONObject("data");
                return data.getString("itemsJwt");
            }
        }
    }

    public JSONObject getBalances() throws IOException {
        String uri = String.format("%s/%s/v%s/walletsbalances",
                base,
                name(),
                version()
        );
        Request request = jsonRequest(uri).get().build();
        Call call = OkHttp3Client.perform(request, gateway);
        try (Response response = call.execute()) {
            try (ResponseBody body = response.body()) {
                String plain = body.string();
                return new JSONObject(plain);
            }
        }
    }

    @Override
    public int version() {
        return 1;
    }

    @Override
    public String name() {
        return "lolinventoryservice-ledge";
    }

    @Override
    public String rcp() {
        return "rcp-be-lol-inventory";
    }
}
