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
    private final String platformId, puuid;
    private final long accountId, summonerId, eligibilityHash, createdAt, serverUtcMillis;
    private final int version;

    protected final List<Party> parties = new ArrayList<>();
    protected final CurrentParty currentParty;

    public PartiesRegistration(JSONObject o) {
        Logger.debug("Registration: {}", o);
        this.serverUtcMillis = o.getLong("serverUtcMillis");
        this.eligibilityHash = o.getLong("eligibilityHash");
        this.platformId = o.getString("platformId");
        this.summonerId = o.getLong("summonerId");
        this.accountId = o.getLong("accountId");
        this.createdAt = o.getLong("createdAt");
        this.version = o.getInt("version");
        this.puuid = o.getString("puuid");
        this.currentParty = new CurrentParty(o.getJSONObject("currentParty"));
        JSONArray parties = o.getJSONArray("parties");
        for (int i = 0; i < parties.length(); i++) {
            JSONObject party = parties.getJSONObject(i);
            if ("LEADER".equals(party.getString("role"))) {
                this.parties.add(new OwnedParty(party));
            } else {
                this.parties.add(new AvailableParty(party));
            }
        }
    }

    public String getPlatformId() {
        return platformId;
    }

    public String getPuuid() {
        return puuid;
    }

    public long getAccountId() {
        return accountId;
    }

    public long getSummonerId() {
        return summonerId;
    }

    public long getEligibilityHash() {
        return eligibilityHash;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getServerUtcMillis() {
        return serverUtcMillis;
    }

    public int getVersion() {
        return version;
    }

    public List<Party> getParties() {
        return parties;
    }

    public CurrentParty getCurrentParty() {
        return currentParty;
    }

    @Override
    public String toString() {
        return "PartiesRegistration{" +
                "platformId='" + platformId + '\'' +
                ", puuid='" + puuid + '\'' +
                ", accountId=" + accountId +
                ", summonerId=" + summonerId +
                ", eligibilityHash=" + eligibilityHash +
                ", createdAt=" + createdAt +
                ", serverUtcMillis=" + serverUtcMillis +
                ", version=" + version +
                ", parties=" + parties +
                ", currentParty=" + currentParty +
                '}';
    }
}
