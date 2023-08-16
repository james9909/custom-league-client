package com.hawolt.client.resources.ledge.parties.objects.invitation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: 19/01/2023 17:38
 * Author: Twitter @hawolt
 **/

public class PartyInvitation {
    private final List<PartyInvitationPlayer> players = new ArrayList<>();

    public PartyInvitation(JSONObject o) {
        JSONObject currentParty = o.getJSONObject("currentParty");
        JSONArray players = currentParty.getJSONArray("players");
        for (int i = 0; i < players.length(); i++) {
            this.players.add(new PartyInvitationPlayer(players.getJSONObject(i)));
        }
    }

    public List<PartyInvitationPlayer> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        return "PartyInvitation{" +
                "players=" + players +
                '}';
    }
}
