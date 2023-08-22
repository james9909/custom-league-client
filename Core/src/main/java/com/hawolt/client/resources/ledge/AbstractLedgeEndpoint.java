package com.hawolt.client.resources.ledge;

import com.hawolt.client.LeagueClient;
import com.hawolt.client.resources.UndocumentedEndpoint;
import com.hawolt.generic.data.Platform;
import com.hawolt.generic.token.impl.StringTokenSupplier;
import com.hawolt.version.IVersionSupplier;
import com.hawolt.virtual.leagueclient.authentication.Session;
import com.hawolt.virtual.leagueclient.client.Authentication;
import com.hawolt.virtual.leagueclient.userinfo.UserInformation;
import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Created: 19/01/2023 16:44
 * Author: Twitter @hawolt
 **/

public abstract class AbstractLedgeEndpoint extends UndocumentedEndpoint {
    protected final StringTokenSupplier tokenSupplier;
    protected final IVersionSupplier leagueVersionSupplier, gameVersionSupplier;
    protected final UserInformation userInformation;
    protected final Platform platform;

    public AbstractLedgeEndpoint(LeagueClient client, String base) {
        super(client, base);
        this.tokenSupplier = (Session) virtualLeagueClient.get(Authentication.SESSION);
        this.platform = client.getVirtualLeagueClientInstance().getPlatform();
        this.userInformation = client.getVirtualLeagueClientInstance().getUserInformation();
        this.leagueVersionSupplier = client.getVirtualLeagueClientInstance().getLocalLeagueFileVersion();
        this.gameVersionSupplier = client.getVirtualLeagueClientInstance().getLocalGameFileVersion();
        //TODO why does LAN & LAS have a custom ledge url
        if (client.getVirtualLeagueClientInstance().getPlatform() == Platform.LA1) {
            this.base = "https://lan-red.lol.sgp.pvp.net";
        } else if (client.getVirtualLeagueClientInstance().getPlatform() == Platform.LA2) {
            this.base = "https://las-red.lol.sgp.pvp.net";
        } else {
            this.base = base;
        }
    }

    public String auth() {
        return String.format("Bearer %s", tokenSupplier.get("session.session_token", true));
    }

    public String agent() {
        return String.format("LeagueOfLegendsClient/%s (%s)",
                leagueVersionSupplier.getVersionValue(platform, "LeagueClientUxRender.exe"),
                rcp()
        );
    }

    public Request.Builder jsonRequest(HttpUrl url) {
        return internalJsonRequest(new Request.Builder()
                .url(url));
    }
    public Request.Builder jsonRequest(String uri) {
        return internalJsonRequest(new Request.Builder()
                .url(uri));
    }

    private Request.Builder internalJsonRequest(Request.Builder builder) {
        return builder
                .addHeader("Authorization", auth())
                .addHeader("User-Agent", agent())
                .addHeader("Accept", "application/json");
    }
}
