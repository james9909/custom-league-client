package com.hawolt.client.resources.platform.history.object;

import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created: 14/01/2023 21:23
 * Author: Twitter @hawolt
 **/

public class MatchOutcomeMetadata {
    private final String product, timestamp, dataVersion, infoType, matchId;
    private final List<String> participants, tags;
    private final boolean hidden;

    public MatchOutcomeMetadata(JSONObject o) {
        this.participants = o.getJSONArray("participants").toList().stream().map(Object::toString).collect(Collectors.toList());
        this.tags = o.getJSONArray("tags").toList().stream().map(Object::toString).collect(Collectors.toList());
        this.dataVersion = o.getString("data_version");
        this.timestamp = o.getString("timestamp");
        this.infoType = o.getString("info_type");
        this.matchId = o.getString("match_id");
        this.product = o.getString("product");
        this.hidden = o.getBoolean("private");

    }

    public List<String> getParticipants() {
        return participants;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getProduct() {
        return product;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getDataVersion() {
        return dataVersion;
    }

    public String getInfoType() {
        return infoType;
    }

    public String getMatchId() {
        return matchId;
    }

    public boolean isPrivate() {
        return hidden;
    }

    @Override
    public String toString() {
        return "MatchOutcomeMetadata{" +
                "product='" + product + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", dataVersion='" + dataVersion + '\'' +
                ", infoType='" + infoType + '\'' +
                ", matchId='" + matchId + '\'' +
                ", participants=" + participants +
                ", tags=" + tags +
                ", hidden=" + hidden +
                '}';
    }
}
