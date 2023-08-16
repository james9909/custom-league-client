package com.hawolt.async;

import com.hawolt.async.loader.impl.ImageLoader;
import com.hawolt.logger.Logger;
import org.imgscalr.Scalr;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.BiConsumer;

/**
 * Created: 15/08/2023 21:25
 * Author: Twitter @hawolt
 **/

public class LazyLoadedImageComponent extends JPanel implements BiConsumer<BufferedImage, Throwable> {
    protected final Dimension dimension;
    private final String uri;

    protected BufferedImage image;

    public LazyLoadedImageComponent(String uri, Dimension dimension) {
        this.dimension = dimension;
        this.uri = uri;
        ImageLoader.instance.load(uri).whenComplete(this);
    }

    @Override
    public void accept(BufferedImage bufferedImage, Throwable throwable) {
        if (throwable != null) Logger.error("Failed to load resource {}", uri);
        else {
            this.image = Scalr.resize(bufferedImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, dimension.width, dimension.height);
            this.repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) return;
        Dimension bounds = getSize();
        int x = (bounds.width >> 1) - (image.getWidth() >> 1);
        int y = (bounds.height >> 1) - (image.getHeight() >> 1);
        g.drawImage(image, x, y, null);
    }
}
