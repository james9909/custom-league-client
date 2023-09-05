package com.hawolt.ui.chat;

import com.hawolt.LeagueClientUI;
import com.hawolt.ui.chat.friendlist.ChatSidebarEssentials;
import com.hawolt.ui.chat.friendlist.ChatSidebarFooter;
import com.hawolt.ui.chat.friendlist.ChatSidebarFriendlist;
import com.hawolt.util.ColorPalette;
import com.hawolt.util.panel.ChildUIComponent;
import com.hawolt.util.ui.LScrollPane;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Created: 08/08/2023 17:08
 * Author: Twitter @hawolt
 **/

public class ChatSidebar extends ChildUIComponent {
    private final ChatSidebarEssentials essentials;
    private final ChatSidebarFooter footer;
    private final ChatSidebarFriendlist list;

    public ChatSidebar(LeagueClientUI leagueClientUI) {
        super(new BorderLayout());
        this.setPreferredSize(new Dimension(300, 0));
        this.setBackground(Color.RED);
        ChildUIComponent component = new ChildUIComponent(new BorderLayout());
        this.setBorder(new MatteBorder(0, 2, 0, 0, Color.DARK_GRAY));
        component.setBackground(ColorPalette.BACKGROUND_COLOR);
        list = new ChatSidebarFriendlist(leagueClientUI.getChatUI(), leagueClientUI);
        component.add(list, BorderLayout.NORTH);
        LScrollPane scrollPane = new LScrollPane(component);
        scrollPane.getVerticalScrollBar().setUnitIncrement(15);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        ChildUIComponent bundle = new ChildUIComponent(new BorderLayout());
        bundle.add(essentials = new ChatSidebarEssentials(leagueClientUI, list), BorderLayout.NORTH);
        bundle.add(scrollPane, BorderLayout.CENTER);
        bundle.add(footer = new ChatSidebarFooter(leagueClientUI.getSettingsUI()), BorderLayout.SOUTH);
        this.add(bundle, BorderLayout.CENTER);
    }

    public ChatSidebarFriendlist getChatSidebarFriendlist() {
        return list;
    }

    public ChatSidebarEssentials getEssentials() {
        return essentials;
    }
}
