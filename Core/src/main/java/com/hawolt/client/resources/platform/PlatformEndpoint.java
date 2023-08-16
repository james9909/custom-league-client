package com.hawolt.client.resources.platform;

import com.hawolt.client.LeagueClient;
import com.hawolt.client.resources.platform.history.MatchHistoryQuery;
import com.hawolt.yaml.ConfigValue;
import com.hawolt.yaml.YamlWrapper;

/**
 * Created: 14/01/2023 04:57
 * Author: Twitter @hawolt
 **/

public class PlatformEndpoint {

    private final MatchHistoryQuery matchHistoryQuery;

    public PlatformEndpoint(LeagueClient client) {
        YamlWrapper wrapper = client.getVirtualLeagueClient().getYamlWrapper();
        this.matchHistoryQuery = new MatchHistoryQuery(client, wrapper.get(ConfigValue.PLATFORM));
    }

    public MatchHistoryQuery getMatchHistoryQuery() {
        return matchHistoryQuery;
    }
}
