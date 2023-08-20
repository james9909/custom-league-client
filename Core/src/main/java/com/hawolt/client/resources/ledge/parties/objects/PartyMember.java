package com.hawolt.client.resources.ledge.parties.objects;

import org.json.JSONObject;

/**
 * Created: 20/08/2023 15:36
 * Author: Twitter @hawolt
 **/

public class PartyMember extends PartyParticipant {

    private final boolean ready, canInvite;
    private PartyParticipantMetadata participantMetadata;

    public PartyMember(JSONObject o) {
        super(o);
        if (o.has("metadata")) {
            this.participantMetadata = new PartyParticipantMetadata(o.getJSONObject("metadata"));
        }
        this.canInvite = o.getBoolean("canInvite");
        this.ready = o.getBoolean("ready");
    }

    public PartyParticipantMetadata getParticipantMetadata() {
        return participantMetadata;
    }

    public boolean isReady() {
        return ready;
    }

    public boolean isCanInvite() {
        return canInvite;
    }

    @Override
    public String toString() {
        return "PartyMember{" +
                "participantMetadata=" + participantMetadata +
                ", ready=" + ready +
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
