package com.hawolt.client.resources.ledge.leagues.objects;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: 05/09/2023 20:34
 * Author: Twitter @hawolt
 **/

public class LeagueLedgeNotifications {
    private final List<LeagueNotification> leagueNotifications = new ArrayList<>();

    public LeagueLedgeNotifications(JSONObject object) {
        JSONArray leagueNotifications = object.getJSONArray("leagueNotifications");
        for (int i = 0; i < leagueNotifications.length(); i++) {
            leagueNotifications.put(new LeagueNotification(leagueNotifications.getJSONObject(i)));
        }
    }

    public List<LeagueNotification> getLeagueNotifications() {
        return leagueNotifications;
    }
}
