package com.hawolt.client.resources.platform.history.object;

import org.json.JSONObject;

/**
 * Created: 14/01/2023 21:09
 * Author: Twitter @hawolt
 **/

public class MatchOutcomeSummary {
    private final MatchOutcomeMetadata metadata;
    private final MatchOutcomeJSON json;

    public MatchOutcomeSummary(JSONObject game) {
        this.metadata = new MatchOutcomeMetadata(game.getJSONObject("metadata"));
        JSONObject json = game.getJSONObject("json");
        long queueId = json.getLong("queueId");
        //TODO add actual implementation
        if (queueId != 420 && queueId != 440) throw new RuntimeException("Unsupported Summary");
        this.json = new MatchOutcomeJSON(json);
    }

    public MatchOutcomeMetadata getMetadata() {
        return metadata;
    }

    public MatchOutcomeJSON getJson() {
        return json;
    }

    @Override
    public String toString() {
        return "MatchOutcomeSummary{" +
                "metadata=" + metadata +
                ", json=" + json +
                '}';
    }
}
