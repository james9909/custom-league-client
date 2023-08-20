package com.hawolt.client.resources.ledge.parties.objects;

import org.json.JSONObject;

/**
 * Created: 20/08/2023 15:18
 * Author: Twitter @hawolt
 **/

public class OwnedParty extends Party {
    private final boolean ready, canInvite;

    public OwnedParty(JSONObject object) {
        super(object);
        this.ready = object.getBoolean("ready");
        this.canInvite = object.getBoolean("canInvite");
    }

    public boolean isReady() {
        return ready;
    }

    public boolean isCanInvite() {
        return canInvite;
    }

    @Override
    public String toString() {
        return "OwnedParty{" +
                "ready=" + ready +
                ", canInvite=" + canInvite +
                ", puuid='" + puuid + '\'' +
                ", platformId='" + platformId + '\'' +
                ", partyId='" + partyId + '\'' +
                ", role='" + role + '\'' +
                ", accountId=" + accountId +
                ", summonerId=" + summonerId +
                ", partyVersion=" + partyVersion +
                "} " + super.toString();
    }
}
