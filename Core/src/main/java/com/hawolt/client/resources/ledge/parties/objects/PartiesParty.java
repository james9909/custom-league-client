package com.hawolt.client.resources.ledge.parties.objects;

import org.json.JSONObject;

/**
 * Created: 19/01/2023 17:17
 * Author: Twitter @hawolt
 **/

public class PartiesParty {
    private final String partyId;

    public PartiesParty(JSONObject o) {
        this.partyId = o.getString("partyId");
    }

    public String getPartyId() {
        return partyId;
    }
}
