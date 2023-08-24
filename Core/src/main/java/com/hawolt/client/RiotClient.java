package com.hawolt.client;

import com.hawolt.client.handler.RMSHandler;
import com.hawolt.client.handler.RTMPHandler;
import com.hawolt.client.handler.XMPPHandler;
import com.hawolt.client.misc.ClientConfiguration;
import com.hawolt.client.misc.captcha.ManualCaptchaSupplier;
import com.hawolt.exception.CaptchaException;
import com.hawolt.virtual.client.LoginStateConsumer;
import com.hawolt.virtual.leagueclient.client.VirtualLeagueClient;
import com.hawolt.virtual.leagueclient.exception.LeagueException;
import com.hawolt.virtual.leagueclient.instance.VirtualLeagueClientInstance;
import com.hawolt.virtual.riotclient.RiotClientException;
import com.hawolt.virtual.riotclient.client.VirtualRiotClient;
import com.hawolt.virtual.riotclient.instance.VirtualRiotClientInstance;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Created: 11/08/2023 19:20
 * Author: Twitter @hawolt
 **/

public class RiotClient implements BiConsumer<VirtualLeagueClient, Throwable> {

    private final ClientConfiguration configuration;
    private final IClientCallback callback;

    public RiotClient(ClientConfiguration configuration, IClientCallback callback, boolean regular) {
        this.configuration = configuration;
        this.callback = callback;
        if (regular) {
            this.loginAndCreate();
        } else {
            this.refreshAndCreate();
        }
    }

    public ClientConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public void accept(VirtualLeagueClient virtualLeagueClient, Throwable throwable) {
        if (throwable != null) callback.onLoginFlowException(throwable);
        else {
            LeagueClient client = new LeagueClient(virtualLeagueClient);
            try {
                finalize(client);
                callback.onClient(client);
            } catch (Exception e) {
                callback.onLoginFlowException(e);
            }
        }
    }

    private void finalize(LeagueClient client) throws IOException, URISyntaxException {
        if (configuration.getComplete()) client.setRMS(RMSHandler.build(client).connect());
        if (!configuration.getMinimal()) client.setXMPP(XMPPHandler.build(client).connect());
        if (configuration.getComplete()) client.setRTMP(RTMPHandler.build(client).connect());
    }


    private void refreshAndCreate() {
        try {
            VirtualRiotClientInstance instance = getRiotClientInstance();
            VirtualRiotClient virtualRiotClient = instance.login(configuration.getPlatform(), configuration.getRefreshToken(), null);
            VirtualLeagueClientInstance virtualLeagueClientInstance = virtualRiotClient.createVirtualLeagueClientInstance(
                    virtualRiotClient.getRiotClientSupplier(),
                    true
            );
            CompletableFuture<VirtualLeagueClient> virtualLeagueClientFuture = virtualLeagueClientInstance.login(
                    configuration.getIgnoreSummoner(),
                    configuration.getSelfRefresh(),
                    configuration.getComplete(),
                    configuration.getMinimal()
            );
            virtualLeagueClientFuture.whenComplete(this);
        } catch (IOException | LeagueException e) {
            callback.onLoginFlowException(e);
        }
    }

    private VirtualRiotClientInstance getRiotClientInstance() {
        return VirtualRiotClientInstance.create(
                configuration.getGateway(),
                configuration.getCookieSupplier(),
                new LoginStateConsumer(),
                false
        );
    }

    private void loginAndCreate() {
        try {
            login(configuration, getRiotClientInstance());
        } catch (IOException | LeagueException | RiotClientException | CaptchaException | InterruptedException e) {
            callback.onLoginFlowException(e);
        }
    }

    private void login(ClientConfiguration configuration, VirtualRiotClientInstance virtualRiotClientInstance) throws IOException, LeagueException, RiotClientException, CaptchaException, InterruptedException {
        String username = configuration.getUsername();
        String password = configuration.getPassword();
        VirtualRiotClient virtualRiotClient = virtualRiotClientInstance.login(username, password, configuration.getMultifactorSupplier(), new ManualCaptchaSupplier());
        VirtualLeagueClientInstance virtualLeagueClientInstance = virtualRiotClient.createVirtualLeagueClientInstance();
        CompletableFuture<VirtualLeagueClient> virtualLeagueClientFuture = virtualLeagueClientInstance.login(
                configuration.getIgnoreSummoner(),
                configuration.getSelfRefresh(),
                configuration.getComplete(),
                configuration.getMinimal()
        );
        virtualLeagueClientFuture.whenComplete(this);
    }
}
