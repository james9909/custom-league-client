package com.hawolt.client.resources.communitydragon.rune;

import org.json.JSONObject;

/**
 * Created: 30/08/2023 19:27
 * Author: Twitter @hawolt
 **/

public class BasicRune {
    private static final String base = "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1";
    public static BasicRune DUMMY = new BasicRune(
            new JSONObject()
                    .put("name", "dummy")
                    .put("icon", "")
                    .put("key", "")
                    .put("id", 0L)
    );

    private final String key, icon, name;
    private final long id;

    public BasicRune(JSONObject o) {
        this.name = o.getString("name");
        this.icon = o.getString("icon");
        this.key = o.getString("key");
        this.id = o.getLong("id");
    }

    public String getKey() {
        return key;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getIconPath() {
        return String.join("/", base, icon.toLowerCase());
    }

}
