package com.hawolt.util.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;

public class LLabel extends JPanel {
    private String text;
    private LTextAlign textAlign;
    private boolean useShadow;

    public LLabel(String text, LTextAlign align) {
        this.text = text;
        textAlign = align;
        init();
    }

    public LLabel(String text, LTextAlign align, boolean shadow) {
        this.text = text;
        textAlign = align;
        useShadow = shadow;
        init();
    }

    private void init() {
        setBackground(new Color(0, 0, 0, 0));
        setFont(new Font("Dialog", Font.BOLD, 18));
        setForeground(Color.WHITE);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFontSize(int size) {
        setFont(new Font(getFont().getFontName(), getFont().getStyle(), size));
    }

    public void useShadow(boolean use) {
        useShadow = use;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        customDraw(graphics);
    }

    private void customDraw(Graphics graphics) {
        //super.paintComponent(graphics);
        Graphics2D g2d = (Graphics2D) graphics.create();
        g2d.setFont(getFont());
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        FontMetrics metrics = getFontMetrics(getFont());
        int x = getBounds().x, y = getBounds().y;

        //Draw Label Background so that it's visible if colored
        Area backgroundArea = new Area(new Rectangle(x, y, getBounds().width, getBounds().height));
        g2d.setColor(getBackground());
        g2d.fill(backgroundArea);

        switch (textAlign) {
            case CENTER:
                x = getBounds().x + getBounds().width / 2 - metrics.stringWidth(text) / 2;
                break;
            case RIGHT:
                x = getBounds().x + getBounds().width - metrics.stringWidth(text);
                break;
        }
        y = getBounds().y + getBounds().height / 2 + PaintHelper.getFontHeight(metrics) / 2;

        if (useShadow)
            PaintHelper.drawShadowText(g2d, text, x, y, getForeground());
        else
            PaintHelper.drawText(g2d, text, x, y, getForeground());

        g2d.dispose();
    }

    public void drawTextStandalone(Graphics g) {
        customDraw(g);
    }
}
