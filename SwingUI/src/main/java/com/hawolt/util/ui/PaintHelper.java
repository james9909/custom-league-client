package com.hawolt.util.ui;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created: 15/08/2023 17:12
 * Author: Twitter @hawolt
 **/

public class PaintHelper {
    private static Font font;

    public static Graphics2D getGraphics2D() {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        if (font == null) font = graphics2D.getFont();
        return graphics2D;
    }

    public static Font getFont() {
        if (font == null) getGraphics2D().dispose();
        return font;
    }

    public static void drawText(Graphics g, String text, int x, int y, Color textColor) {
        g.setColor(textColor);
        g.drawString(text, x, y);
    }

    public static void drawShadowText(Graphics g, String text, int x, int y, Color textColor) {
        g.setColor(Color.BLACK);
        g.drawString(text, x + 1, y + 1);
        g.setColor(textColor);
        g.drawString(text, x, y);
    }

    public static int getFontHeight(FontMetrics metrics)
    {
        return metrics.getAscent() - metrics.getDescent() - metrics.getLeading();
    }
}
