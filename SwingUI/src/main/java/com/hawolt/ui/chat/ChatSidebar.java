package com.hawolt.ui.chat;

import com.hawolt.LeagueClientUI;
import com.hawolt.ui.chat.friendlist.ChatSidebarEssentials;
import com.hawolt.ui.chat.friendlist.ChatSidebarFriendlist;
import com.hawolt.ui.chat.profile.ChatSidebarProfile;
import com.hawolt.util.panel.ChildUIComponent;
import com.hawolt.virtual.leagueclient.userinfo.UserInformation;

import javax.swing.*;
import java.awt.*;

/**
 * Created: 08/08/2023 17:08
 * Author: Twitter @hawolt
 **/

public class ChatSidebar extends ChildUIComponent {
    private final ChatSidebarEssentials essentials;
    private final ChatSidebarProfile profile;
    private final ChatSidebarFriendlist list;

    public ChatSidebar(UserInformation information, LeagueClientUI leagueClientUI) {
        super(new BorderLayout());
        this.setPreferredSize(new Dimension(300, 0));
        this.setBackground(Color.RED);
        this.add(profile = new ChatSidebarProfile(information, new BorderLayout()), BorderLayout.NORTH);
        ChildUIComponent component = new ChildUIComponent(new BorderLayout());
        list = new ChatSidebarFriendlist(leagueClientUI.getChatUI());
        component.add(list, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.getVerticalScrollBar().setUnitIncrement(15);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        ChildUIComponent bundle = new ChildUIComponent(new BorderLayout());
        bundle.add(essentials = new ChatSidebarEssentials(leagueClientUI, list), BorderLayout.NORTH);
        bundle.add(scrollPane, BorderLayout.CENTER);
        this.add(bundle, BorderLayout.CENTER);
    }

    public void configure(UserInformation userInformation) {
        if (userInformation.isLeagueAccountAssociated()) {
            String name = userInformation.getUserInformationLeagueAccount().getSummonerName();
            getProfile().getSummoner().getChatSidebarName().setSummonerName(name);
            long iconId = userInformation.getUserInformationLeagueAccount().getProfileIcon();
            getProfile().getIcon().setIconId(iconId);
        } else {
            getProfile().getSummoner().getChatSidebarName().setSummonerName("");
            getProfile().getIcon().setIconId(29);
        }
    }

    public ChatSidebarProfile getProfile() {
        return profile;
    }

    public ChatSidebarFriendlist getChatSidebarFriendlist() {
        return list;
    }

    public ChatSidebarEssentials getEssentials() {
        return essentials;
    }
}
