package com.hawolt.ui.layout;

import com.hawolt.client.LeagueClient;
import com.hawolt.ui.layout.wallet.HeaderWallet;
import com.hawolt.util.AudioEngine;
import com.hawolt.util.panel.ChildUIComponent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created: 09/08/2023 15:52
 * Author: Twitter @hawolt
 **/

public class LayoutHeader extends ChildUIComponent {
    private final HeaderWallet wallet;

    public LayoutHeader(ILayoutManager manager, LeagueClient client) {
        super(new GridLayout(0, 5));
        this.setBackground(Color.GRAY);
        this.setPreferredSize(new Dimension(0, 90));
        this.setBorder(new EmptyBorder(5, 5, 5, 5));

        JButton button = new JButton("STORE");
        button.addActionListener(o -> {
            AudioEngine.play("openstore.wav");
            manager.showComponent("store");
        });
        add(button);
        JButton button1 = new JButton("PLAY");
        button1.addActionListener(o -> {
            manager.showComponent("play");
        });
        add(button1);
        JButton button2 = new JButton("CHAMPSELECT");
        button2.addActionListener(o -> {
            manager.showComponent("select");
        });
        add(button2);
        JButton button3 = new JButton("RUNES");
        button3.addActionListener(o -> {
            manager.showComponent("runes");
        });
        add(button3);
        add(wallet = new HeaderWallet(client));
    }

    public HeaderWallet getWallet() {
        return wallet;
    }
}
