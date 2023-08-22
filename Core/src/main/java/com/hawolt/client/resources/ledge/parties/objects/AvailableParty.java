package com.hawolt.client.resources.ledge.parties.objects;

import org.json.JSONObject;

/**
 * Created: 20/08/2023 15:19
 * Author: Twitter @hawolt
 **/

public class AvailableParty extends Party {
    private final long invitedBySummonerId, inviteTimestamp;
    private final String invitedByPuuid;
    private PartyGameMode partyGameMode;

    public AvailableParty(JSONObject o) {
        super(o);
        this.invitedBySummonerId = o.getLong("invitedBySummonerId");
        this.inviteTimestamp = o.getLong("inviteTimestamp");
        this.invitedByPuuid = o.getString("invitedByPuuid");
        if (o.has("gameMode") && !o.isNull("gameMode")) {
            this.partyGameMode = new PartyGameMode(o.getJSONObject("gameMode"));
        }
    }

    public long getInvitedBySummonerId() {
        return invitedBySummonerId;
    }

    public long getInviteTimestamp() {
        return inviteTimestamp;
    }

    public String getInvitedByPUUID() {
        return invitedByPuuid;
    }

    public PartyGameMode getPartyGameMode() {
        return partyGameMode;
    }

    @Override
    public String toString() {
        return "AvailableParty{" +
                "partyGameMode=" + partyGameMode +
                ", invitedBySummonerId=" + invitedBySummonerId +
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
