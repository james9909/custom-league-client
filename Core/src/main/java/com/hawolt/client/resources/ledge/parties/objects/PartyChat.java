package com.hawolt.client.resources.ledge.parties.objects;

import org.json.JSONObject;

/**
 * Created: 20/08/2023 15:27
 * Author: Twitter @hawolt
 **/

public class PartyChat {
    private final PartyMucJwtDto partyMucJwtDto;
    private final String jid;

    public PartyChat(JSONObject o) {
        this.partyMucJwtDto = new PartyMucJwtDto(o.getJSONObject("mucJwtDto"));
        this.jid = o.getString("jid");
    }

    public PartyMucJwtDto getPartyMucJwtDto() {
        return partyMucJwtDto;
    }

    public String getJid() {
        return jid;
    }

    @Override
    public String toString() {
        return "PartyChat{" +
                "partyMucJwtDto=" + partyMucJwtDto +
                ", jid='" + jid + '\'' +
                '}';
    }
}
