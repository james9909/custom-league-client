package com.hawolt.ui.chat.window;

import com.hawolt.util.AudioEngine;
import com.hawolt.util.panel.ChildUIComponent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created: 08/08/2023 20:51
 * Author: Twitter @hawolt
 **/

public class ChatWindowHeader extends ChildUIComponent {

    private final JLabel target;

    public ChatWindowHeader(LayoutManager layout) {
        super(layout);
        this.setBackground(Color.GRAY);
        this.setForeground(Color.WHITE);
        this.setBorder(new EmptyBorder(0, 5, 0, 0));
        this.add(target = new JLabel("", SwingConstants.LEFT), BorderLayout.CENTER);
        JButton close = new JButton("×");
        this.add(close, BorderLayout.EAST);
        close.addActionListener(listener -> {
            AudioEngine.play("leave_chat.wav");
            this.getParent().setVisible(false);
        });
    }

    public void setTarget(String name) {
        target.setText(name);
    }

}
