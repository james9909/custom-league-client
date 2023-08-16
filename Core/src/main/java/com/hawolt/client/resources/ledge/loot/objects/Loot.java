package com.hawolt.client.resources.ledge.loot.objects;

import org.json.JSONObject;

/**
 * Created: 27/07/2023 22:52
 * Author: Twitter @hawolt
 **/

public class Loot {
    private final String lootName, refId;
    private final int count;

    public Loot(JSONObject o) {
        this.lootName = o.getString("lootName");
        this.refId = o.getString("refId");
        this.count = o.getInt("count");
    }

    public String getLootName() {
        return lootName;
    }

    public String getRefId() {
        return refId;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "Loot{" +
                "lootName='" + lootName + '\'' +
                ", refId='" + refId + '\'' +
                ", count=" + count +
                '}';
    }
}
