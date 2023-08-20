package com.hawolt.client.resources.ledge.parties.objects;

import org.json.JSONObject;

/**
 * Created: 20/08/2023 15:31
 * Author: Twitter @hawolt
 **/

public class PartyParticipant {
    protected final String puuid, platformId, partyId, role;
    protected final long accountId, summonerId;
    protected final int partyVersion;

    public PartyParticipant(JSONObject o) {
        this.puuid = o.getString("puuid");
        this.platformId = o.getString("platformId");
        this.accountId = o.getLong("accountId");
        this.summonerId = o.getLong("summonerId");
        this.partyId = o.getString("partyId");
        this.partyVersion = o.getInt("partyVersion");
        this.role = o.getString("role");
    }

    public String getPuuid() {
        return puuid;
    }

    public String getPlatformId() {
        return platformId;
    }

    public String getPartyId() {
        return partyId;
    }

    public String getRole() {
        return role;
    }

    public long getAccountId() {
        return accountId;
    }

    public long getSummonerId() {
        return summonerId;
    }


    public int getPartyVersion() {
        return partyVersion;
    }

    @Override
    public String toString() {
        return "PartyParticipant{" +
                "puuid='" + puuid + '\'' +
                ", platformId='" + platformId + '\'' +
                ", partyId='" + partyId + '\'' +
                ", role='" + role + '\'' +
                ", accountId=" + accountId +
                ", summonerId=" + summonerId +
                ", partyVersion=" + partyVersion +
                '}';
    }
}
