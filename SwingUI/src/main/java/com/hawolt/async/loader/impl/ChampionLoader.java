package com.hawolt.async.loader.impl;

import com.hawolt.io.Core;
import com.hawolt.async.loader.AbstractLoader;
import com.hawolt.objects.Champion;
import com.hawolt.logger.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created: 20/04/2023 16:26
 * Author: Twitter @hawolt
 **/

public class ChampionLoader extends AbstractLoader<Integer, Champion> {

    public static ChampionLoader instance = new ChampionLoader();

    public ChampionLoader() {
        Logger.debug("loaded {} entries for {}", cache.size(), getClass().getSimpleName());
    }

    @Override
    public void init() throws URISyntaxException, IOException {
        HttpsURLConnection connection = (HttpsURLConnection) getResource().toURL().openConnection();
        connection.setRequestProperty("User-Agent", "Sentinel");
        try (InputStream stream = connection.getInputStream()) {
            JSONArray array = new JSONArray(Core.read(stream).toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject reference = array.getJSONObject(i);
                Champion champion = new Champion(reference);
                cache.put(champion.getId(), champion);
            }
        }
    }

    @Override
    public URI getResource() throws URISyntaxException {
        return new URI("https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/champion-summary.json");
    }
}
