package com.hawolt.client.resources.ledge.summoner.objects;

import org.json.JSONObject;

/**
 * Created: 19/01/2023 17:28
 * Author: Twitter @hawolt
 **/

public class Summoner {
    private final String puuid;
    private final long summonerId, accountId;

    public Summoner(JSONObject o) {
        this.puuid = o.getString("puuid");
        this.summonerId = o.getLong("id");
        this.accountId = o.getLong("accountId");
    }

    public long getAccountId() {
        return accountId;
    }

    public long getSummonerId() {
        return summonerId;
    }

    public String getPUUID() {
        return puuid;
    }

    @Override
    public String toString() {
        return "Summoner{" +
                "puuid='" + puuid + '\'' +
                ", summonerId=" + summonerId +
                '}';
    }
}
