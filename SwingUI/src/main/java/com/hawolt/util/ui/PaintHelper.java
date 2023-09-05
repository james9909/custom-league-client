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

    public static void drawText(Graphics g, Font font, String text, int x, int y, Color textColor) {
        Color currentColor = g.getColor();
        Font currentFont = g.getFont();
        g.setFont(font);
        g.setColor(textColor);
        g.drawString(text, x, y);
        g.setFont(currentFont);
        g.setColor(currentColor);
    }

    public static void drawText(Graphics g, String text, int x, int y, Color textColor) {
        drawText(g, g.getFont(), text, x, y, textColor);
    }

    public static void drawShadowText(Graphics g, String text, int x, int y, Color textColor) {
        drawText(g, text, x + 1, y + 1, Color.BLACK);
        drawText(g, text, x, y, textColor);
    }

    public static void drawText(Graphics g, Font font, String text, Rectangle rectangle, LTextAlign alignment, Color textColor) {
        FontMetrics metrics = g.getFontMetrics();
        int y = rectangle.y + (rectangle.height >> 1) + (metrics.getAscent() >> 1);
        int x = switch (alignment) {
            case LEFT -> rectangle.x + (rectangle.width / 20);
            case CENTER -> rectangle.x - (rectangle.width >> 1) - (metrics.stringWidth(text) >> 1);
            case RIGHT -> rectangle.x + (rectangle.width - metrics.stringWidth(text) - (rectangle.width / 20));
        };
        drawText(g, font, text, x, y, textColor);
    }

    public static void drawShadowText(Graphics g, Font font, String text, Rectangle rectangle, LTextAlign alignment, Color textColor) {
        drawText(g, font, text, new Rectangle(rectangle.x + 1, rectangle.y + 1, rectangle.width, rectangle.height), alignment, Color.BLACK);
        drawText(g, font, text, rectangle, alignment, textColor);
    }

    public static void drawShadowText(Graphics g, String text, Rectangle rectangle, LTextAlign alignment, Color textColor) {
        drawShadowText(g, g.getFont(), text, rectangle, alignment, textColor);
    }

    public static int getFontHeight(FontMetrics metrics) {
        return metrics.getAscent() - metrics.getDescent() - metrics.getLeading();
    }
}
