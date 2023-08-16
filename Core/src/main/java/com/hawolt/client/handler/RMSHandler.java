package com.hawolt.client.handler;

import com.hawolt.client.LeagueClient;
import com.hawolt.logger.Logger;
import com.hawolt.rms.IRiotMessageServiceConnectionCallback;
import com.hawolt.rms.VirtualRiotMessageClient;
import com.hawolt.rms.data.GenericRiotMessageEvent;
import org.java_websocket.handshake.ServerHandshake;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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
        String url = String.format(
                "wss://eu.edge.rms.si.riotgames.com:443/rms/v1/session?token=%s&id=%s&token_type=access&product_id=riot_client&platform=windows&device=desk",
                client.getVirtualLeagueClient().getVirtualLeagueClientInstance().getLeagueClientSupplier().get("lol.access_token", true),
                UUID.randomUUID()
        );
        return new RMSHandler(client, new URI(url));
    }

    public RMSHandler(LeagueClient client, URI url) {
        this.virtualRiotMessageClient = new VirtualRiotMessageClient(url, this);
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
