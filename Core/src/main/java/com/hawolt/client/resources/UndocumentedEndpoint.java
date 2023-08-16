package com.hawolt.client.resources;

import com.hawolt.client.LeagueClient;
import com.hawolt.http.Gateway;
import com.hawolt.virtual.leagueclient.client.VirtualLeagueClient;

/**
 * Created: 14/01/2023 01:33
 * Author: Twitter @hawolt
 **/

public abstract class UndocumentedEndpoint implements IUndocumentedEndpoint {
    protected final VirtualLeagueClient virtualLeagueClient;
    protected final LeagueClient client;
    protected final Gateway gateway;
    protected String base;

    public UndocumentedEndpoint(LeagueClient client, String base) {
        this.gateway = client.getVirtualLeagueClient().getVirtualRiotClientInstance().getGateway();
        this.virtualLeagueClient = client.getVirtualLeagueClient();
        this.client = client;
        this.base = base;
    }

    @Override
    public String base() {
        return base;
    }
}
