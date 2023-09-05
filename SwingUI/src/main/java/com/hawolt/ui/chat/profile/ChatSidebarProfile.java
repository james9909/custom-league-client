package com.hawolt.ui.chat.profile;

import com.hawolt.util.ColorPalette;
import com.hawolt.util.panel.ChildUIComponent;
import com.hawolt.virtual.leagueclient.userinfo.UserInformation;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Created: 08/08/2023 17:25
 * Author: Twitter @hawolt
 **/

public class ChatSidebarProfile extends ChildUIComponent {
    private final ChatSidebarSummoner summoner;
    private final ChatSidebarProfileIcon icon;

    public ChatSidebarProfile(UserInformation information, LayoutManager layout) {
        super(layout);
        this.setBackground(ColorPalette.ACCENT_COLOR);
        this.setBorder(
                new CompoundBorder(
                        new MatteBorder(0, 2, 0, 0, Color.DARK_GRAY),
                        new EmptyBorder(5, 5, 5, 5)
                )
        );
        this.setPreferredSize(new Dimension(300, 90));
        this.add(icon = new ChatSidebarProfileIcon(information, new BorderLayout()), BorderLayout.WEST);
        this.add(summoner = new ChatSidebarSummoner(new GridLayout(3, 0, 0, 5)), BorderLayout.CENTER);
    }

    public ChatSidebarSummoner getSummoner() {
        return summoner;
    }

    public ChatSidebarProfileIcon getIcon() {
        return icon;
    }
}
