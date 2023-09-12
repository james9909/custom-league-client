package com.hawolt.ui.chat.profile;

import com.hawolt.util.ColorPalette;
import com.hawolt.util.panel.ChildUIComponent;
import com.hawolt.util.ui.PaintHelper;
import com.hawolt.virtual.leagueclient.userinfo.UserInformation;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created: 08/08/2023 17:25
 * Author: Twitter @hawolt
 **/

public class ChatSidebarProfile extends ChildUIComponent {
    private final ChatSidebarSummoner summoner;
    private final ChatSidebarProfileIcon icon;

    public ChatSidebarProfile(UserInformation information, LayoutManager layout) {
        super(layout);
        this.setBackground(ColorPalette.accentColor);
        this.setBorder(new EmptyBorder(0, 5, 5, 0));
        this.setPreferredSize(new Dimension(300, 90));
        //had to make a container 'cause to put the header buttons in the corner i had to change the border here, and then the icon wasn't in the proper
        //position, not even by setting a border on it, like this it looks like it did before
        ChildUIComponent iconContainer = new ChildUIComponent(new BorderLayout());
        iconContainer.setBackground(ColorPalette.accentColor);
        iconContainer.setBorder(new EmptyBorder(5, 0, 0, 0));
        iconContainer.add(icon = new ChatSidebarProfileIcon(information, new BorderLayout()), BorderLayout.CENTER);
        this.add(iconContainer, BorderLayout.WEST);
        this.add(summoner = new ChatSidebarSummoner(new GridLayout(3, 0, 0, 5)), BorderLayout.CENTER);
    }

    public ChatSidebarSummoner getSummoner() {
        return summoner;
    }

    public ChatSidebarProfileIcon getIcon() {
        return icon;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //Drawing top left rounding
        int width = getWidth();
        int height = getHeight();
        g2d.setColor(ColorPalette.accentColor);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
    }
}
