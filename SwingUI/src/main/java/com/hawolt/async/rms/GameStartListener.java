package com.hawolt.async.rms;

import com.hawolt.generic.data.Platform;
import com.hawolt.rms.data.subject.service.IServiceMessageListener;
import com.hawolt.rms.data.subject.service.RiotMessageServiceMessage;
import com.hawolt.util.Launcher;
import org.json.JSONObject;

/**
 * Created: 11/08/2023 18:01
 * Author: Twitter @hawolt
 **/

public class GameStartListener implements IServiceMessageListener<RiotMessageServiceMessage> {
    private final Platform platform;

    public GameStartListener(Platform platform) {
        this.platform = platform;
    }

    @Override
    public void onMessage(RiotMessageServiceMessage riotMessageServiceMessage) {
        JSONObject object = riotMessageServiceMessage.getPayload().getPayload();
        String ip = object.getString("serverIp");
        String port = String.valueOf(object.getInt("serverPort"));
        String encryptionKey = object.getString("encryptionKey");
        String gameId = String.valueOf(object.getLong("gameId"));
        String summonerId = String.valueOf(object.getLong("summonerId"));
        Launcher.launch(ip, port, encryptionKey, summonerId, gameId, platform);
    }
}
