package com.hawolt.ui;

import com.hawolt.logger.Logger;
import com.hawolt.util.panel.MainUIComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Created: 06/08/2023 13:07
 * Author: Twitter @hawolt
 **/

public class MainUI extends MainUIComponent implements ComponentListener {

    private final Dimension dimension = new Dimension(400, 300);
    private final JLayeredPane layeredPane;
    private JComponent main, chat;

    public MainUI(JFrame frame) {
        super(frame);
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(1600, 900));
        this.addComponentListener(this);
        this.container.add(this);
        this.add(layeredPane = new JLayeredPane(), BorderLayout.CENTER);
        this.init();
    }

    public void setMainComponent(JComponent main) {
        if (this.main != null) this.remove(this.main);
        this.main = main;
        Dimension dimension = getPreferredSize();
        main.setBounds(0, 0, dimension.width, dimension.height);
        this.layeredPane.add(main, JLayeredPane.DEFAULT_LAYER);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (main == null) return;
        adjust();
    }

    public void adjust() {
        Dimension dimension = getSize();
        Logger.error(dimension);
        main.setBounds(0, 0, dimension.width, dimension.height);
        setChatPosition();
        revalidate();
    }

    private void setChatPosition() {
        if (chat == null) return;
        Dimension bounds = getSize();
        chat.setBounds(bounds.width - 300 - dimension.width, bounds.height - dimension.height, dimension.width, dimension.height);
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    public void addChatComponent(JComponent chat) {
        this.chat = chat;
        this.layeredPane.add(chat, JLayeredPane.POPUP_LAYER);
        this.setChatPosition();
    }
}
