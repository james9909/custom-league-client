package com.hawolt.ui.chat.friendlist;

import com.hawolt.ui.impl.JHintTextField;
import com.hawolt.util.panel.ChildUIComponent;
import com.hawolt.xmpp.core.VirtualRiotXMPPClient;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * Created: 09/08/2023 01:59
 * Author: Twitter @hawolt
 **/

public class ChatSidebarFriendEssentials extends ChildUIComponent implements DocumentListener {
    private final IFriendListComponent component;
    private final JHintTextField input;

    public ChatSidebarFriendEssentials(VirtualRiotXMPPClient xmppClient, IFriendListComponent component) {
        super(new BorderLayout(5, 0));
        this.setBackground(Color.GRAY);
        this.component = component;
        input = new JHintTextField("Name");
        add(input, BorderLayout.CENTER);
        input.getDocument().addDocumentListener(this);
        JButton add = new JButton("ADD");
        add.setFocusPainted(false);
        add.addActionListener(listener -> {
            String name = input.getText();
            if (name.contains("#")) {
                String[] data = name.split("#");
                xmppClient.addFriendByTag(data[0], data[1]);
            } else {
                xmppClient.addFriendByName(name);
            }
            input.setText("");
        });
        add(add, BorderLayout.EAST);
    }

    private void handle() {
        component.search(input.getText());
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        handle();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        handle();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        handle();
    }
}
