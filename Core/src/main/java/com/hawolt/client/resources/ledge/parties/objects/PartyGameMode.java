package com.hawolt.client.resources.ledge.parties.objects;

import org.json.JSONObject;

/**
 * Created: 20/08/2023 15:20
 * Author: Twitter @hawolt
 **/

public class PartyGameMode {
    private final String gameType;
    private final int queueId, maxPartySize, maxTeamSize;

    public PartyGameMode(JSONObject o) {
        this.gameType = o.getString("gameType");
        this.queueId = o.getInt("queueId");
        this.maxPartySize = o.getInt("maxPartySize");
        this.maxTeamSize = o.getInt("maxTeamSize");
    }

    public String getGameType() {
        return gameType;
    }

    public int getQueueId() {
        return queueId;
    }

    public int getMaxPartySize() {
        return maxPartySize;
    }

    public int getMaxTeamSize() {
        return maxTeamSize;
    }

    @Override
    public String toString() {
        return "PartyGameMode{" +
                "gameType='" + gameType + '\'' +
                ", queueId=" + queueId +
                ", maxPartySize=" + maxPartySize +
                ", maxTeamSize=" + maxTeamSize +
                '}';
    }
}
