package com.hawolt.shutdown.hooks;

import com.hawolt.async.ExecutorManager;
import com.hawolt.client.LeagueClient;
import com.hawolt.shutdown.ShutdownTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created: 16/08/2023 20:05
 * Author: Twitter @hawolt
 **/

public class ShutdownServices extends ShutdownTask {

    public ShutdownServices(LeagueClient client) {
        super(client);
    }

    @Override
    protected void execute() throws Exception {
        List<ExecutorService> services = new ArrayList<>(ExecutorManager.get());
        for (ExecutorService service : services) {
            service.shutdownNow();
        }
    }
}
