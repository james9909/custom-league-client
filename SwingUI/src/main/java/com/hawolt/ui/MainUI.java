package com.hawolt.ui;

import com.hawolt.ui.chat.window.ChatWindow;
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
    private JComponent main, chat;

    public MainUI(JFrame frame) {
        super(frame);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(1600, 900));
        this.addComponentListener(this);
        this.container.add(this);
        this.init();
    }

    public void setMainComponent(JComponent main) {
        if (this.main != null) this.remove(this.main);
        Dimension dimension = getPreferredSize();
        this.main = main;
        this.main.setBounds(0, 0, dimension.width, dimension.height);
        this.add(main);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        System.err.println("COMPONENT RESIZED");
        if (main == null) return;
        adjust();
    }

    public void adjust() {
        Dimension dimension = getSize();
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

    public void addChatComponent(ChatWindow chat) {
        this.chat = chat;
        this.add(chat);
        this.setChatPosition();
    }
}
