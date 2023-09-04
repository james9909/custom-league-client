package com.hawolt.client.resources.communitydragon.rune;

import org.json.JSONObject;

/**
 * Created: 03/09/2023 01:37
 * Author: Twitter @hawolt
 **/

public class Rune extends BasicRune {
    private final String shortDesc, longDesc;

    public Rune(JSONObject object) {
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

}
