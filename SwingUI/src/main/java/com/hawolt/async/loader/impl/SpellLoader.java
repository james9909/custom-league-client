package com.hawolt.async.loader.impl;

import com.hawolt.async.loader.AbstractLoader;
import com.hawolt.io.Core;
import com.hawolt.logger.Logger;
import com.hawolt.objects.Spell;
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

public class SpellLoader extends AbstractLoader<Long, Spell> {

    public static SpellLoader instance = new SpellLoader();

    public SpellLoader() {
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
                Spell spell = new Spell(reference);
                cache.put(spell.getId(), spell);
            }
        }
    }

    @Override
    public URI getResource() throws URISyntaxException {
        return new URI("https://raw.communitydragon.org/pbe/plugins/rcp-be-lol-game-data/global/default/v1/summoner-spells.json");
    }
}
