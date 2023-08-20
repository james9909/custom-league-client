package com.hawolt.client.resources.ledge.loot;

import com.hawolt.client.LeagueClient;
import com.hawolt.client.resources.ledge.AbstractLedgeEndpoint;
import com.hawolt.client.resources.ledge.loot.objects.Loot;
import com.hawolt.client.resources.ledge.loot.objects.LootAction;
import com.hawolt.client.resources.ledge.loot.objects.PlayerLoot;
import com.hawolt.generic.Constant;
import com.hawolt.http.OkHttp3Client;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

/**
 * Created: 27/07/2023 22:21
 * Author: Twitter @hawolt
 **/

public class LootLedge extends AbstractLedgeEndpoint {
    public LootLedge(LeagueClient client, String base) {
        super(client, base);
    }

    //TODO hardcoded location fix
    public JSONObject interact(Loot loot, LootAction action) throws IOException {
        return interact(loot, action, 1);
    }

    public JSONObject interact(Loot loot, LootAction action, int repeat) throws IOException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(base.substring(base.lastIndexOf('/') + 1))
                .addPathSegment(name())
                .addPathSegment("v" + version())
                .addPathSegment("playerloot")
                .addPathSegment("location")
                .addPathSegment(String.format("lolriot.aws-euc1-prod.%s", platform.name().toLowerCase()))
                .addPathSegment("craftref")
                .addPathSegment("id")
                .addPathSegment(UUID.randomUUID().toString())
                .build();
        JSONObject object = new JSONObject();
        object.put("accountId", client.getVirtualLeagueClientInstance().getUserInformation().getOriginalAccountId());
        object.put("clientId", "LolClient-LEdge");
        object.put("playerId", client.getVirtualLeagueClientInstance().getUserInformation().getUserInformationLeagueAccount().getSummonerId());
        object.put("puuid", client.getVirtualLeagueClientInstance().getUserInformation().getSub());
        object.put("recipeName", String.join("_", loot.getLootName().substring(0, loot.getLootName().lastIndexOf("_")), action.name().toLowerCase()));
        object.put("repeat", repeat);
        JSONArray array = new JSONArray();
        JSONObject instance = new JSONObject();
        instance.put("lootName", loot.getLootName());
        instance.put("refId", "");
        array.put(instance);
        if (action == LootAction.UPGRADE) {
            JSONObject upgrade = new JSONObject();
            String type = loot.getLootName().contains("SKIN") ? "SKIN" : "CHAMPION";
            instance.put("lootName", String.join("_", type, action.name().toLowerCase()));
            instance.put("refId", "");
            array.put(upgrade);
        }
        object.put("lootNameRefIds", array);
        Request request = jsonRequest(url)
                .post(RequestBody.create(object.toString(), Constant.APPLICATION_JSON))
                .build();
        Call call = OkHttp3Client.perform(request, gateway);
        try (Response response = call.execute()) {
            try (ResponseBody body = response.body()) {
                String plain = body.string();
                return new JSONObject(plain);
            }
        }
    }

    public PlayerLoot get() throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(base.substring(base.lastIndexOf('/') + 1))
                .addPathSegment(name())
                .addPathSegment("v" + version())
                .addPathSegment("playerlootdefinitions")
                .addPathSegment("location")
                .addPathSegment(String.format("lolriot.aws-euc1-prod.%s", platform.name().toLowerCase()))
                .addPathSegment("playerId")
                .addPathSegment(String.valueOf(client.getVirtualLeagueClientInstance().getUserInformation().getUserInformationLeagueAccount().getSummonerId()))
                .addQueryParameter("lastLootItemUpdate", timestamp)
                .addQueryParameter("lastRecipeUpdate", timestamp)
                .addQueryParameter("lastQueryUpdate", timestamp)
                .build();
        Request request = jsonRequest(url).get().build();
        Call call = OkHttp3Client.perform(request, gateway);
        try (Response response = call.execute()) {
            try (ResponseBody body = response.body()) {
                String plain = body.string();
                JSONObject object = new JSONObject(plain);
                return new PlayerLoot(object.getJSONArray("playerLoot"));
            }
        }
    }

    @Override
    public int version() {
        return 1;
    }

    @Override
    public String name() {
        return "loot";
    }

    @Override
    public String rcp() {
        return "rcp-be-lol-loot";
    }
}
