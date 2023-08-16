package com.hawolt.client.resources.ledge.store.objects;

import org.json.JSONObject;

/**
 * Created: 28/07/2023 01:59
 * Author: Twitter @hawolt
 **/

public class Price {
    private final String currency;
    private final int cost;

    public Price(JSONObject object) {
        this.currency = object.getString("currency");
        this.cost = object.getInt("cost");
    }

    public String getCurrency() {
        return currency;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "Price{" +
                "currency='" + currency + '\'' +
                ", cost=" + cost +
                '}';
    }
}
