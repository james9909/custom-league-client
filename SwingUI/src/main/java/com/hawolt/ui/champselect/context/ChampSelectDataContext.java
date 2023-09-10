package com.hawolt.ui.champselect.context;

import com.hawolt.client.LeagueClient;
import com.hawolt.rtmp.utility.PacketCallback;
import com.hawolt.xmpp.event.handler.message.IMessageListener;
import com.hawolt.xmpp.event.objects.presence.impl.JoinMucPresence;

import java.util.Map;

/**
 * Created: 10/09/2023 03:18
 * Author: Twitter @hawolt
 **/

public interface ChampSelectDataContext {
    IMessageListener getMessageListener();

    PacketCallback getPacketCallback();

    void cache(String puuid, String name);

    void push(JoinMucPresence presence);

    Map<String, String> getPUUIDResolver();

    LeagueClient getLeagueClient();
}
