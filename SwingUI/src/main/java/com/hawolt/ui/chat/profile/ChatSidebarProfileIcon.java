package com.hawolt.ui.chat.profile;

import com.hawolt.async.loader.impl.ImageLoader;
import com.hawolt.logger.Logger;
import com.hawolt.util.panel.ChildUIComponent;
import org.imgscalr.Scalr;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created: 08/08/2023 17:28
 * Author: Twitter @hawolt
 **/

public class ChatSidebarProfileIcon extends ChildUIComponent {
    private final String ICON_BASE_URL = "https://raw.communitydragon.org/latest/game/assets/ux/summonericons/profileicon%s.png";
    private final int ICON_SIZE = 80;
    private BufferedImage icon;

    public ChatSidebarProfileIcon(LayoutManager layout) {
        super(layout);
        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(ICON_SIZE, ICON_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (icon == null) return;
        g.drawImage(icon, 0, 0, null);
    }

    public void setIconId(long iconId) {
        ImageLoader.instance.load(String.format(ICON_BASE_URL, iconId)).whenComplete((image, e) -> {
            if (e != null) Logger.error(e);
            else {
                this.icon = Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, ICON_SIZE, ICON_SIZE);
                this.repaint();
            }
        });
    }
}
