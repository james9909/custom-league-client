package com.hawolt.client.resources.ledge.parties.objects;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: 20/08/2023 15:53
 * Author: Twitter @hawolt
 **/

public class PartyRestriction {
    private final List<GatekeeperRestriction> list = new ArrayList<>();

    public PartyRestriction(JSONObject restrictions) {
        JSONArray array = restrictions.getJSONArray("gatekeeperRestrictions");
        for (int i = 0; i < array.length(); i++) {
            list.add(new GatekeeperRestriction(array.getJSONObject(i)));
        }
    }

    public List<GatekeeperRestriction> getRestrictionList() {
        return list;
    }

    @Override
    public String toString() {
        return "PartyRestriction{" +
                "list=" + list +
                '}';
    }
}
