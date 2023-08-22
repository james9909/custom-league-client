package com.hawolt.client.resources.ledge.parties.objects;

import org.json.JSONObject;

/**
 * Created: 20/08/2023 15:36
 * Author: Twitter @hawolt
 **/

public class PendingPartyMember extends PartyParticipant {
    private final long invitedBySummonerId, inviteTimestamp;
    private final String invitedByPuuid;

    public PendingPartyMember(JSONObject o) {
        super(o);
        this.invitedBySummonerId = o.getLong("invitedBySummonerId");
        this.inviteTimestamp = o.getLong("inviteTimestamp");
        this.invitedByPuuid = o.getString("invitedByPuuid");
    }

    public long getInvitedBySummonerId() {
        return invitedBySummonerId;
    }

    public long getInviteTimestamp() {
        return inviteTimestamp;
    }

    public String getInvitedByPuuid() {
        return invitedByPuuid;
    }

    @Override
    public String toString() {
        return "PendingPartyMember{" +
                "invitedBySummonerId=" + invitedBySummonerId +
                ", inviteTimestamp=" + inviteTimestamp +
                ", invitedByPuuid='" + invitedByPuuid + '\'' +
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
