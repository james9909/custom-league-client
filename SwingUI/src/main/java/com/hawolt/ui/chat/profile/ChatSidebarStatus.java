package com.hawolt.ui.chat.profile;

import com.hawolt.util.AudioEngine;
import com.hawolt.xmpp.core.VirtualRiotXMPPClient;

import javax.swing.*;
import java.awt.*;

/**
 * Created: 08/08/2023 17:45
 * Author: Twitter @hawolt
 **/

public class ChatSidebarStatus extends JComponent {
    private VirtualRiotXMPPClient xmppClient;

    public ChatSidebarStatus() {
        this.setLayout(new BorderLayout());
        JComboBox<ChatStatus> box = new JComboBox<>(ChatStatus.values());
        box.setSelectedItem(ChatStatus.OFFLINE);
        box.addItemListener(listener -> {
            if (xmppClient == null) return;
            AudioEngine.play("air_button_press_1.wav");
            xmppClient.setPresence(box.getItemAt(box.getSelectedIndex()).getStatus(), "", 501);
        });
        this.add(box, BorderLayout.CENTER);
    }

    public void setXMPPClient(VirtualRiotXMPPClient xmppClient) {
        this.xmppClient = xmppClient;
    }
}
