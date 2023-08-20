package com.hawolt.client.resources.ledge.parties.objects;

import org.json.JSONObject;

/**
 * Created: 20/08/2023 15:19
 * Author: Twitter @hawolt
 **/

public class AvailableParty extends Party {
    private final PartyGameMode partyGameMode;
    private final long invitedBySummonerId, inviteTimestamp;
    private final String invitedByPuuid;

    public AvailableParty(JSONObject o) {
        super(o);
        this.partyGameMode = new PartyGameMode(o.getJSONObject("gameMode"));
        this.invitedBySummonerId = o.getLong("invitedBySummonerId");
        this.inviteTimestamp = o.getLong("inviteTimestamp");
        this.invitedByPuuid = o.getString("invitedByPuuid");
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
