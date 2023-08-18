package com.hawolt.client.resources.ledge.parties.objects;

import com.hawolt.logger.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: 19/01/2023 17:12
 * Author: Twitter @hawolt
 **/

public class PartiesRegistration {

    private final List<PartiesParty> parties = new ArrayList<>();
    private final JSONObject source;

    public PartiesRegistration(JSONObject o) {
        this.source = o;
        if (!o.has("parties")) return;
        JSONArray parties = o.getJSONArray("parties");
        for (int i = 0; i < parties.length(); i++) {
            this.parties.add(new PartiesParty(parties.getJSONObject(i)));
        }
    }

    public JSONObject getSource() {
        return source;
    }

    public boolean isPartyAvailable() {
        return !parties.isEmpty();
    }

    public String getFirstPartyId() {
        return parties.get(0).getPartyId();
    }

    public List<PartiesParty> getParties() {
        return parties;
    }

    @Override
    public String toString() {
        return "PartiesRegistration{" +
                "parties=" + parties +
                '}';
    }
}
