package com.hawolt.ui.chat.window;

import com.hawolt.ui.chat.friendlist.IFriendListComponent;
import com.hawolt.xmpp.core.VirtualRiotXMPPClient;
import com.hawolt.xmpp.event.objects.friends.GenericFriend;

/**
 * Created: 09/08/2023 01:14
 * Author: Twitter @hawolt
 **/

public interface IChatWindow {
    VirtualRiotXMPPClient getXMPPClient();

    void showChat(GenericFriend friend);

    void setIFriendListComponent(IFriendListComponent component);

    boolean isChatOpened(String jid);
}
