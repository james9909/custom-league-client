package com.hawolt.objects;

import org.json.JSONObject;

/**
 * Created: 06/08/2023 23:24
 * Author: Twitter @hawolt
 **/

public class Spell {
    public final static String BASE = "https://raw.communitydragon.org/latest/game/data/spells/icons2d/";
    private final long id;
    private final String name, iconName;
    private String iconPath;

    public Spell(JSONObject o) {
        this.id = o.getLong("id");
        this.iconPath = o.getString("iconPath");
        this.iconName = iconPath.substring(iconPath.lastIndexOf("/") + 1).toLowerCase();
        this.iconPath = String.join("/", BASE, iconName);
        this.name = o.getString("name");
    }

    public long getId() {
        return id;
    }

    public String getIconPath() {
        return iconPath;
    }

    public String getName() {
        return name;
    }

    public String getIconName() {
        return iconName;
    }

    @Override
    public String toString() {
        return name;
    }
}
