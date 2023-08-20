package com.hawolt.client.resources.ledge.parties.objects;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created: 20/08/2023 15:33
 * Author: Twitter @hawolt
 **/

public class PartyParticipantMetadata {
    private final JSONArray positionPref;

    public PartyParticipantMetadata(JSONObject o) {
        this.positionPref = o.getJSONArray("positionPref");
    }

    public String getPrimaryPreference() {
        return positionPref.getString(0);
    }

    public String getSecondaryPreference() {
        return positionPref.getString(1);
    }

    @Override
    public String toString() {
        return "PartyParticipantMetadata{" +
                "primary=" + getPrimaryPreference() +
                ", secondary=" + getSecondaryPreference() +
                '}';
    }
}
