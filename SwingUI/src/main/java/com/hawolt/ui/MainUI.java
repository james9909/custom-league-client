package com.hawolt.ui;

import com.hawolt.util.panel.MainUIComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Created: 06/08/2023 13:07
 * Author: Twitter @hawolt
 **/

public class MainUI extends MainUIComponent {
    private final Dimension CHAT_TARGET_DIMENSION = new Dimension(400, 300);
    private final JLayeredPane layeredPane;
    private JComponent main;
    private JComponent chat;

    public MainUI(JFrame frame) {
        super(frame);
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(1600, 900));
        this.layeredPane = new JLayeredPane();
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (main == null) return;
                adjust();
            }
        });
        this.container.add(this);
        this.add(layeredPane, BorderLayout.CENTER);
        this.init();
    }

    public void setMainComponent(JComponent main) {
        if (this.main != null) this.remove(this.main);
        this.main = main;
        Dimension bounds = getPreferredSize();
        main.setBounds(0, 0, bounds.width, bounds.height);
        this.layeredPane.add(main, JLayeredPane.DEFAULT_LAYER);
    }

    public void adjust() {
        Dimension currentDimension = getSize();
        main.setBounds(0, 0, currentDimension.width, currentDimension.height);
        setChatPosition();
        revalidate();
    }

    private void setChatPosition() {
        if (chat == null) return;
        Dimension currentDimension = getSize();
        chat.setBounds(
                currentDimension.width - 300 - CHAT_TARGET_DIMENSION.width,
                currentDimension.height - CHAT_TARGET_DIMENSION.height,
                CHAT_TARGET_DIMENSION.width,
                CHAT_TARGET_DIMENSION.height
        );
    }

    public void addChatComponent(JComponent chat) {
        this.chat = chat;
        this.layeredPane.add(chat, JLayeredPane.POPUP_LAYER);
        this.setChatPosition();
    }
}
