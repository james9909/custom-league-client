package com.hawolt.client.resources.ledge.parties.objects;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: 20/08/2023 17:04
 * Author: Twitter @hawolt
 **/

public class PartyGatekeeper {
    private final List<GatekeeperRestriction> list = new ArrayList<>();
    private final String errorCode;
    private final int httpStatus;

    public PartyGatekeeper(JSONObject o) {
        this.httpStatus = o.getInt("httpStatus");
        this.errorCode = o.getString("errorCode");
        JSONObject payload = o.getJSONObject("payload");
        JSONArray array = payload.getJSONArray("gatekeeperRestrictions");
        for (int i = 0; i < array.length(); i++) {
            list.add(new GatekeeperRestriction(array.getJSONObject(i)));
        }
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public List<GatekeeperRestriction> getRestrictionList() {
        return list;
    }

    @Override
    public String toString() {
        return "PartyGatekeeper{" +
                "list=" + list +
                ", errorCode='" + errorCode + '\'' +
                ", httpStatus=" + httpStatus +
                '}';
    }
}
