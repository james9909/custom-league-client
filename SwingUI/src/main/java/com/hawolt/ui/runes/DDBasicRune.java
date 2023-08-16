package com.hawolt.ui.runes;

import org.json.JSONObject;

/**
 * Created: 15/08/2023 21:00
 * Author: Twitter @hawolt
 **/

public class DDBasicRune {

    private final static String base = "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1";
    private final String key, icon, name;
    private final long id;

    public DDBasicRune(JSONObject object) {
        this.name = object.getString("name");
        this.icon = object.getString("icon");
        this.key = object.getString("key");
        this.id = object.getLong("id");
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

    @Override
    public String toString() {
        return "DDBasicRune{" +
                "key='" + key + '\'' +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
