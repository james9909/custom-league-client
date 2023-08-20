package com.hawolt.ui.chat.friendlist;

import com.hawolt.ui.queue.QueueState;
import com.hawolt.util.panel.ChildUIComponent;
import com.hawolt.xmpp.core.VirtualRiotXMPPClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Created: 09/08/2023 01:43
 * Author: Twitter @hawolt
 **/

public class ChatSidebarEssentials extends ChildUIComponent {

    private final ChatSidebarFriendEssentials essentials;
    private final QueueState state = new QueueState();
    private boolean toggled;

    public ChatSidebarEssentials(VirtualRiotXMPPClient xmppClient, IFriendListComponent component) {
        super(new GridLayout(0, 1, 5, 5));
        this.setBorder(
                BorderFactory.createCompoundBorder(
                        new MatteBorder(2, 0, 2, 0, Color.DARK_GRAY),
                        new EmptyBorder(5, 5, 5, 5)
                )
        );
        this.add(essentials = new ChatSidebarFriendEssentials(xmppClient, component));
        this.setBackground(Color.GRAY);
        component.registerNotificationBar(this);
    }

    public ChatSidebarFriendEssentials getEssentials() {
        return essentials;
    }

    public void toggleQueueState(long currentTimeMillis, long estimatedMatchmakingTimeMillis) {
        toggleQueueState(currentTimeMillis, estimatedMatchmakingTimeMillis, false);
    }

    public void toggleQueueState(long currentTimeMillis, long estimatedMatchmakingTimeMillis, boolean lpq) {
        if (toggled) {
            if (state.isLPQ()) state.updateLPQ(estimatedMatchmakingTimeMillis);
            return;
        }
        state.setTimer(currentTimeMillis, estimatedMatchmakingTimeMillis, lpq);
        add(state, 0);
        toggled = true;
        revalidate();
    }

    public void disableQueueState() {
        toggled = false;
        remove(state);
        revalidate();
    }

}
