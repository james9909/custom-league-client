package com.hawolt.ui.chat.profile;

import com.hawolt.util.ColorPalette;
import com.hawolt.util.ui.LComboBox;
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
        LComboBox<ChatStatus> box = new LComboBox<>(ChatStatus.values());
        box.setBackground(ColorPalette.ACCENT_COLOR);
        box.setOptionBackground(ColorPalette.BACKGROUND_COLOR);
        box.setSelectedItem(ChatStatus.OFFLINE);
        box.addItemListener(listener -> {
            if (xmppClient == null) return;
            xmppClient.setPresence(box.getItemAt(box.getSelectedIndex()).getStatus(), "", 501);
        });
        this.add(box, BorderLayout.CENTER);
    }

    public void setXMPPClient(VirtualRiotXMPPClient xmppClient) {
        this.xmppClient = xmppClient;
    }
}
