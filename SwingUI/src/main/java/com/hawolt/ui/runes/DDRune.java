package com.hawolt.ui.runes;

import org.json.JSONObject;

/**
 * Created: 15/08/2023 20:53
 * Author: Twitter @hawolt
 **/

public class DDRune extends DDBasicRune {
    private final String shortDesc, longDesc;

    public DDRune(JSONObject object) {
        super(object);
        this.shortDesc = object.getString("shortDesc");
        this.longDesc = object.getString("longDesc");
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    @Override
    public String toString() {
        return "DDRune{" +
                "shortDesc='" + shortDesc + '\'' +
                ", longDesc='" + longDesc + '\'' +
                '}';
    }
}
