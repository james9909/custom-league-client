package com.hawolt.ui.champselect.phase;

import com.hawolt.async.loader.ResourceConsumer;
import com.hawolt.async.loader.ResourceLoader;
import com.hawolt.logger.Logger;
import com.hawolt.ui.champselect.IChampSelection;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

/**
 * Created: 06/08/2023 15:19
 * Author: Twitter @hawolt
 **/

public class ChampSelectSelectionComponent extends JPanel implements MouseListener, ResourceConsumer<BufferedImage, byte[]> {
    private final static String preset = "https://raw.communitydragon.org/pbe/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/%s.png";
    private final static Color opaque = new Color(255, 255, 255, 100);
    private ChampSelectSelectionCallback callback;
    private IChampSelection champSelection;
    private Rectangle rectangle;
    private BufferedImage image;
    private boolean selected;
    private int componentId;
    private int championId;

    public ChampSelectSelectionComponent(ChampSelectSelectionCallback callback, IChampSelection champSelection, int componentId) {
        this.champSelection = champSelection;
        this.componentId = componentId;
        this.callback = callback;
        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(70, 90));
        this.addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (championId == 0 || image == null) return;
        try {

            Dimension dimension = getSize();
            int x = (dimension.width >> 1) - (image.getWidth() >> 1);
            int y = 5;
            this.rectangle = new Rectangle(x, y, 64, 64);
            g.drawImage(image, x, y, null);
            g.drawString(champSelection.getChampionCache().get(championId).getName(), x, y + 80);
            if (!selected) return;
            Graphics2D graphics2D = (Graphics2D) g;

            graphics2D.setColor(opaque);
            graphics2D.fillRect(x, y, 64, 64);

            graphics2D.setColor(Color.WHITE);
            graphics2D.setStroke(new BasicStroke(3));
            graphics2D.drawRect(x + 1, y + 1, 64 - 3, 64 - 3);
        } catch (Exception e) {
            Logger.error(e);
        }

    }

    public void update(int championId) {
        if (this.championId != 0) return;
        this.championId = championId;
        ResourceLoader.loadResource(String.format(preset, this.championId), this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (rectangle.contains(e.getPoint())) {
            this.callback.onSelection(componentId, championId);
            this.selected = true;
            this.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void unselect() {
        this.selected = false;
        this.repaint();
    }


    @Override
    public void onException(Object o, Exception e) {
        Logger.fatal("Failed to load {}", o);
        Logger.error(e);
    }

    @Override
    public void consume(Object o, BufferedImage bufferedImage) {
        this.image = Scalr.resize(bufferedImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, 64, 64);
        this.repaint();
    }

    @Override
    public BufferedImage transform(byte[] bytes) throws Exception {
        return ImageIO.read(new ByteArrayInputStream(bytes));
    }
}
