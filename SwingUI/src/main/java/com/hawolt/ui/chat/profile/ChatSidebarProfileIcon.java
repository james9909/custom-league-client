package com.hawolt.ui.chat.profile;

import com.hawolt.async.loader.ResourceConsumer;
import com.hawolt.async.loader.ResourceLoader;
import com.hawolt.logger.Logger;
import com.hawolt.util.panel.ChildUIComponent;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

/**
 * Created: 08/08/2023 17:28
 * Author: Twitter @hawolt
 **/

public class ChatSidebarProfileIcon extends ChildUIComponent implements ResourceConsumer<BufferedImage, byte[]> {
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
        ResourceLoader.loadResource(String.format(ICON_BASE_URL, iconId), this);
    }

    @Override
    public void onException(Object o, Exception e) {
        Logger.fatal("Failed to load resource {}", o);
        Logger.error(e);
    }

    @Override
    public void consume(Object o, BufferedImage image) {
        this.icon = Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, ICON_SIZE, ICON_SIZE);
        this.repaint();
    }

    @Override
    public BufferedImage transform(byte[] bytes) throws Exception {
        return ImageIO.read(new ByteArrayInputStream(bytes));
    }
}
