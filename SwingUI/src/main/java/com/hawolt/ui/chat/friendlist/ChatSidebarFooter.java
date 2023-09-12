package com.hawolt.ui.chat.friendlist;

import com.hawolt.ui.github.Github;
import com.hawolt.ui.settings.SettingsUI;
import com.hawolt.util.ColorPalette;
import com.hawolt.util.panel.ChildUIComponent;
import com.hawolt.util.ui.LFlatButton;
import com.hawolt.util.ui.LHighlightType;
import com.hawolt.util.ui.LLabel;
import com.hawolt.util.ui.LTextAlign;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class ChatSidebarFooter extends ChildUIComponent {
    private static final Font font = new Font("", Font.BOLD, 20);
    private static final int HEIGHT = 30;

    public ChatSidebarFooter(SettingsUI settingsWindow) {
        super(new BorderLayout());
        this.setPreferredSize(new Dimension(0, HEIGHT));
        this.setBackground(ColorPalette.BACKGROUND_COLOR);
        this.setBorder(new MatteBorder(1, 0, 0, 0, Color.DARK_GRAY));
        LFlatButton settingsButton = new LFlatButton("⚙", LTextAlign.CENTER, LHighlightType.COMPONENT);
        settingsButton.setBorder(BorderFactory.createEmptyBorder());
        settingsButton.setFont(font);
        settingsButton.setPreferredSize(new Dimension(HEIGHT, HEIGHT));
        settingsButton.addActionListener(listener -> {
            if (settingsWindow.isVisible()) {
                settingsWindow.close();
            } else {
                settingsWindow.setVisible(true);
            }
        });
        add(settingsButton, BorderLayout.EAST);
        LLabel version = new LLabel(Github.getCurrentVersion(), LTextAlign.CENTER, true);
        add(version, BorderLayout.CENTER);
    }
}