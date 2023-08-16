package com.hawolt.ui.runes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created: 15/08/2023 20:50
 * Author: Twitter @hawolt
 **/

public class DDRuneType extends DDBasicRune {
    private final LinkedList<LinkedList<DDRune>> alignment = new LinkedList<>();

    public DDRuneType(JSONObject object) {
        super(object);
        JSONArray slots = object.getJSONArray("slots");
        for (int i = 0; i < slots.length(); i++) {
            JSONObject nested = slots.getJSONObject(i);
            JSONArray runes = nested.getJSONArray("runes");
            LinkedList<DDRune> list = new LinkedList<>();
            for (int j = 0; j < runes.length(); j++) {
                list.add(new DDRune(runes.getJSONObject(j)));
            }
            alignment.add(list);
        }
    }

    public LinkedList<LinkedList<DDRune>> getAlignment() {
        return alignment;
    }

    @Override
    public String toString() {
        return "DDRuneType{" +
                "alignment=" + alignment +
                '}';
    }
}
