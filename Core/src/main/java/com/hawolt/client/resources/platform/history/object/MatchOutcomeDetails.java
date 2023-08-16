package com.hawolt.client.resources.platform.history.object;

import org.json.JSONObject;

/**
 * Created: 14/01/2023 21:09
 * Author: Twitter @hawolt
 **/

public class MatchOutcomeDetails {
    private final MatchOutcomeMetadata metadata;

    public MatchOutcomeDetails(JSONObject game) {
        this.metadata = new MatchOutcomeMetadata(game.getJSONObject("metadata"));
    }

    public MatchOutcomeMetadata getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return "MatchOutcomeDetails{" +
                "metadata=" + metadata +
                '}';
    }
}
