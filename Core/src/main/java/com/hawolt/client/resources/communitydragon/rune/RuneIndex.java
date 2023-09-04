package com.hawolt.client.resources.communitydragon.rune;

import com.hawolt.http.layer.IResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created: 29/08/2023 21:29
 * Author: Twitter @hawolt
 **/

public class RuneIndex {

    private final LinkedList<RuneType> main = new LinkedList<>();
    private RuneType extra;

    public RuneIndex() {

    }

    public RuneIndex(IResponse response, JSONArray local) {
        this.extra = new RuneType(local.getJSONObject(0));
        JSONArray array = new JSONArray(response.asString());
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            main.add(new RuneType(object));
        }
    }

    public LinkedList<RuneType> getMain() {
        return main;
    }

    public RuneType getAdditional() {
        return extra;
    }
}
