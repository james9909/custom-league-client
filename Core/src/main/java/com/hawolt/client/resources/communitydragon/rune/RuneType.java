package com.hawolt.client.resources.communitydragon.rune;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created: 03/09/2023 01:36
 * Author: Twitter @hawolt
 **/

public class RuneType extends BasicRune {
    private final LinkedList<LinkedList<BasicRune>> alignment = new LinkedList<>();

    public RuneType(JSONObject object) {
        super(object);
        JSONArray slots = object.getJSONArray("slots");
        for (int i = 0; i < slots.length(); i++) {
            JSONObject nested = slots.getJSONObject(i);
            JSONArray runes = nested.getJSONArray("runes");
            LinkedList<BasicRune> list = new LinkedList<>();
            for (int j = 0; j < runes.length(); j++) {
                list.add(new BasicRune(runes.getJSONObject(j)));
            }
            alignment.add(list);
        }
    }

    public LinkedList<LinkedList<BasicRune>> getAlignment() {
        return alignment;
    }
}
