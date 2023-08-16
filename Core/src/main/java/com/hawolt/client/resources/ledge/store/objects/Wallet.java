package com.hawolt.client.resources.ledge.store.objects;

import org.json.JSONObject;

/**
 * Created: 28/07/2023 00:21
 * Author: Twitter @hawolt
 **/

public class Wallet {
    private final int ip, rp;

    public Wallet(JSONObject object) {
        this.ip = object.getInt("ip");
        this.rp = object.getInt("rp");
    }

    public int getBlueEssence() {
        return ip;
    }

    public int getRiotPoints() {
        return rp;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "ip=" + ip +
                ", rp=" + rp +
                '}';
    }
}
