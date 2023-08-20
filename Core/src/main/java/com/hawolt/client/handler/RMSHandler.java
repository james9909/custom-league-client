package com.hawolt.client.handler;

import com.hawolt.client.LeagueClient;
import com.hawolt.logger.Logger;
import com.hawolt.rms.IRiotMessageServiceConnectionCallback;
import com.hawolt.rms.VirtualRiotMessageClient;
import com.hawolt.rms.data.GenericRiotMessageEvent;
import com.hawolt.version.local.LocalRiotFileVersion;
import com.hawolt.virtual.clientconfig.impl.PlayerClientConfig;
import com.hawolt.virtual.clientconfig.impl.rms.RMSAffinity;
import com.hawolt.virtual.clientconfig.impl.rms.RiotMessageServiceConfig;
import com.hawolt.virtual.leagueclient.authentication.RMS;
import com.hawolt.virtual.leagueclient.client.Authentication;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created: 11/08/2023 18:22
 * Author: Twitter @hawolt
 **/

public class RMSHandler implements IRiotMessageServiceConnectionCallback {
    private final VirtualRiotMessageClient virtualRiotMessageClient;
    private final LeagueClient client;
    private final URI url;

    public static RMSHandler build(LeagueClient client) throws URISyntaxException, MalformedURLException {
        RMS rms = (RMS) client.getVirtualLeagueClient().get(Authentication.RMS);
        String affinity = rms.get("rms_token");
        JSONObject content = new JSONObject(new String(Base64.getDecoder().decode(affinity.split("\\.")[1])));
        PlayerClientConfig playerClientConfig = client.getVirtualLeagueClientInstance().getPlayerClientConfig();
        RiotMessageServiceConfig riotMessageServiceConfig = playerClientConfig.getRiotMessageServiceConfig();
        String url = String.format(
                "%s:443/rms/v1/session?token=%s&id=%s&token_type=access&product_id=league_of_legends&platform=windows&device=desktop",
                riotMessageServiceConfig.getRiotMessageServiceAffinity(RMSAffinity.AFFINITIES, content.getString("affinity")),
                client.getVirtualLeagueClient().getVirtualLeagueClientInstance().getLeagueClientSupplier().get("lol.access_token", true),
                UUID.randomUUID()
        );
        Map<String, String> headers = new HashMap<>();
        LocalRiotFileVersion versionSupplier = client.getVirtualRiotClientInstance().getLocalRiotFileVersion();
        headers.put("User-Agent", String.format("RiotClient/%s entitlements (;;;)", versionSupplier.getVersionValue("RiotGamesApi.dll")));
        headers.put("X-Riot-Affinity", affinity);
        return new RMSHandler(client, headers, new URI(url));
    }

    public RMSHandler(LeagueClient client, Map<String, String> headers, URI url) {
        this.virtualRiotMessageClient = new VirtualRiotMessageClient(url, headers, this);
        this.client = client;
        this.url = url;
    }

    public RMSHandler connect() {
        this.virtualRiotMessageClient.connect();
        return this;
    }

    public VirtualRiotMessageClient getVirtualRiotMessageClient() {
        return virtualRiotMessageClient;
    }

    public URI getUrl() {
        return url;
    }

    @Override
    public void onRiotMessageEvent(GenericRiotMessageEvent genericRiotMessageEvent) {
        Logger.info(genericRiotMessageEvent);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        Logger.info("RMS connection closed: {}, {}, {}", i, s, b);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        Logger.info("Connected to RMS: {}", serverHandshake.getHttpStatusMessage());
    }

    @Override
    public void onError(Exception e) {
        Logger.error(e);
    }
}
