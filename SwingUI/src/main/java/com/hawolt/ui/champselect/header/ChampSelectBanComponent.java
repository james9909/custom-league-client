package com.hawolt.ui.champselect.header;

import com.hawolt.async.loader.ResourceConsumer;
import com.hawolt.async.loader.ResourceLoader;
import com.hawolt.logger.Logger;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

/**
 * Created: 06/08/2023 15:19
 * Author: Twitter @hawolt
 **/

public class ChampSelectBanComponent extends JPanel implements ResourceConsumer<BufferedImage, byte[]> {
    private final static String preset = "https://raw.communitydragon.org/pbe/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/%s.png";
    private BufferedImage image;
    private int championId;

    public ChampSelectBanComponent() {
        setBackground(Color.BLACK);
    }

    private final static Color outline = new Color(122, 138, 153);

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension dimension = getSize();
        int x = (dimension.width >> 1) - (48 >> 1);
        int y = (dimension.height >> 1) - (48 >> 1);
        g.setColor(outline);
        g.drawRect(x, y, 48, 48);
        if (championId == 0 || image == null) return;
        g.drawImage(image, x, y, null);
        g.setColor(outline);
        g.drawRect(x, y, 48, 48);
    }

    public void update(int championId) {
        if (this.championId == championId) return;
        this.championId = championId;
        ResourceLoader.load(String.format(preset, this.championId), this);
    }

    @Override
    public void onException(Object o, Exception e) {
        Logger.fatal("Failed to load {}", o);
        Logger.error(e);
    }

    @Override
    public void consume(Object o, BufferedImage bufferedImage) {
        this.image = Scalr.resize(bufferedImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, 48, 48);
        this.repaint();
    }

    @Override
    public BufferedImage transform(byte[] bytes) throws Exception {
        return ImageIO.read(new ByteArrayInputStream(bytes));
    }
}
