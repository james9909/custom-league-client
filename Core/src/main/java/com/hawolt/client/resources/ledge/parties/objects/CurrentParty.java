package com.hawolt.client.resources.ledge.parties.objects;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: 20/08/2023 15:23
 * Author: Twitter @hawolt
 **/

public class CurrentParty {
    private final List<PartyParticipant> players = new ArrayList<>();
    private final String partyId, platformId, partyType;
    private final int version, maxPartySize;
    private final boolean activityLocked;
    private final long eligibilityHash;
    private final PartyChat partyChat;
    private PartyRestriction partyRestriction;
    private PartyGameMode partyGameMode;

    public CurrentParty(JSONObject o) {
        this.partyId = o.getString("partyId");
        this.platformId = o.getString("platformId");
        this.version = o.getInt("version");
        this.partyType = o.getString("partyType");
        this.activityLocked = o.getBoolean("activityLocked");
        this.maxPartySize = o.getInt("maxPartySize");
        this.eligibilityHash = o.getLong("eligibilityHash");
        this.partyChat = new PartyChat(o.getJSONObject("chat"));
        JSONArray players = o.getJSONArray("players");
        for (int i = 0; i < players.length(); i++) {
            JSONObject player = players.getJSONObject(i);
            if ("INVITED".equals(player.getString("role"))) {
                this.players.add(new PendingPartyMember(player));
            } else {
                this.players.add(new PartyMember(player));
            }
        }
        if (o.has("gameMode")) {
            this.partyGameMode = new PartyGameMode(o.getJSONObject("gameMode"));
        }
        if (o.has("activeRestrictions")) {
            partyRestriction = new PartyRestriction(o.getJSONObject("activeRestrictions"));
        }
    }

    public PartyRestriction getPartyRestriction() {
        return partyRestriction;
    }

    public List<PartyParticipant> getPlayers() {
        return players;
    }

    public String getPartyId() {
        return partyId;
    }

    public String getPlatformId() {
        return platformId;
    }

    public String getPartyType() {
        return partyType;
    }

    public PartyGameMode getPartyGameMode() {
        return partyGameMode;
    }

    public int getVersion() {
        return version;
    }

    public int getMaxPartySize() {
        return maxPartySize;
    }

    public boolean isActivityLocked() {
        return activityLocked;
    }

    public long getEligibilityHash() {
        return eligibilityHash;
    }

    public PartyChat getPartyChat() {
        return partyChat;
    }

    @Override
    public String toString() {
        return "CurrentParty{" +
                "players=" + players +
                ", partyId='" + partyId + '\'' +
                ", platformId='" + platformId + '\'' +
                ", partyType='" + partyType + '\'' +
                ", partyGameMode=" + partyGameMode +
                ", version=" + version +
                ", maxPartySize=" + maxPartySize +
                ", activityLocked=" + activityLocked +
                ", eligibilityHash=" + eligibilityHash +
                ", partyChat=" + partyChat +
                '}';
    }
}
