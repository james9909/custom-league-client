package com.hawolt.ui.chat.friendlist;

import javax.swing.*;

/**
 * Created: 09/08/2023 01:55
 * Author: Twitter @hawolt
 **/

public interface IFriendListComponent {
    void registerNotificationBar(JComponent component);

    void onMessage(String jid);

    void search(String name);

    void onOpen(String jid);
}
