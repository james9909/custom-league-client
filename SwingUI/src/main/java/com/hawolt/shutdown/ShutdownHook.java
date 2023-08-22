package com.hawolt.shutdown;

import com.hawolt.client.LeagueClient;
import com.hawolt.generic.runnable.ExceptionalRunnable;
import com.hawolt.generic.runnable.IExceptionCallback;
import com.hawolt.logger.Logger;
import com.hawolt.shutdown.hooks.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Created: 11/08/2023 19:53
 * Author: Twitter @hawolt
 **/

public class ShutdownHook implements Runnable, IExceptionCallback {
    private final List<ExceptionalRunnable> list = new LinkedList<>();


    public ShutdownHook(LeagueClient client) {
        list.add(new ShutdownLeaveParty(client));
        list.add(new ShutdownPartyRegistration(client));
        list.add(new ShutdownRTMP(client));
        list.add(new ShutdownXMPP(client));
        list.add(new ShutdownRMS(client));
        list.add(new ShutdownServices(client));
    }

    @Override
    public void run() {
        for (int i = 0; i < list.size(); i++) {
            ExceptionalRunnable runnable = list.get(i);
            Logger.info("Executing ShutdownTask {}", i);
            runnable.run();
        }
    }

    @Override
    public void onException(Exception e) {
        Logger.error(e);
    }
}
