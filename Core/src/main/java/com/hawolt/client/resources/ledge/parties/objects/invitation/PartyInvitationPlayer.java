package com.hawolt.client.resources.ledge.parties.objects.invitation;

import org.json.JSONObject;

/**
 * Created: 19/01/2023 17:47
 * Author: Twitter @hawolt
 **/

public class PartyInvitationPlayer {
    private final long partyVersion, accountId, summonerId;
    private final String role, puuid, platformId, partyId;
    private final Object canInvite, ready, invitedBySummonerId, inviteTimestamp;

    public PartyInvitationPlayer(JSONObject o) {
        this.partyVersion = o.getLong("partyVersion");
        this.accountId = o.getLong("accountId");
        this.role = o.getString("role");
        this.canInvite = getSafe(o, "canInvite");
        this.ready = getSafe(o, "ready");
        this.puuid = o.getString("puuid");
        this.summonerId = o.getLong("summonerId");
        this.platformId = o.getString("platformId");
        this.partyId = o.getString("partyId");
        this.invitedBySummonerId = getSafe(o, "invitedBySummonerId");
        this.inviteTimestamp = getSafe(o, "inviteTimestamp");
    }

    private Object getSafe(JSONObject o, String name) {
        return o.has(name) ? o.get(name) : null;
    }

    public long getPartyVersion() {
        return partyVersion;
    }

    public long getAccountId() {
        return accountId;
    }

    public long getSummonerId() {
        return summonerId;
    }

    public String getRole() {
        return role;
    }

    public String getPUUID() {
        return puuid;
    }

    public String getPlatformId() {
        return platformId;
    }

    public String getPartyId() {
        return partyId;
    }

    public Object getCanInvite() {
        return canInvite;
    }

    public Object getReady() {
        return ready;
    }

    public Object getInvitedBySummonerId() {
        return invitedBySummonerId;
    }

    public Object getInviteTimestamp() {
        return inviteTimestamp;
    }

    @Override
    public String toString() {
        return "PartyInvitationPlayer{" +
                "partyVersion=" + partyVersion +
                ", accountId=" + accountId +
                ", summonerId=" + summonerId +
                ", role='" + role + '\'' +
                ", puuid='" + puuid + '\'' +
                ", platformId='" + platformId + '\'' +
                ", partyId='" + partyId + '\'' +
                ", canInvite=" + canInvite +
                ", ready=" + ready +
                ", invitedBySummonerId=" + invitedBySummonerId +
                ", inviteTimestamp=" + inviteTimestamp +
                '}';
    }
}
