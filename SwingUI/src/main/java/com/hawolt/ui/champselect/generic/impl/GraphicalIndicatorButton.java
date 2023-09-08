package com.hawolt.ui.champselect.generic.impl;

import com.hawolt.async.loader.ResourceConsumer;
import com.hawolt.logger.Logger;
import com.hawolt.util.paint.custom.impl.objects.AbstractGraphicalButton;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

/**
 * Created: 08/09/2023 14:02
 * Author: Twitter @hawolt
 **/

public class GraphicalIndicatorButton extends AbstractGraphicalButton implements ResourceConsumer<BufferedImage, byte[]> {
    private BufferedImage image;
    private boolean highlight;

    public GraphicalIndicatorButton() {

    }

    public GraphicalIndicatorButton(Rectangle area) {
        super(area);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        if (area == null || image == null || !visible) return;
        if (enabled) {
            if (highlight) {
                graphics2D.setColor(hover ? Color.LIGHT_GRAY : Color.GREEN);
            } else if (hold) {
                graphics2D.setColor(Color.DARK_GRAY);
            } else {
                graphics2D.setColor(hover ? Color.LIGHT_GRAY : Color.GRAY);
            }
        }
        graphics2D.fillRect(x, y, width, height);
        graphics2D.drawImage(image, x, y, null);
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    @Override
    public void onException(Object o, Exception e) {
        Logger.error("Failed to load resource {}", o);
    }

    @Override
    public void consume(Object o, BufferedImage bufferedImage) {
        this.image = bufferedImage;
    }

    @Override
    public BufferedImage transform(byte[] bytes) throws Exception {
        return ImageIO.read(new ByteArrayInputStream(bytes));
    }
}
