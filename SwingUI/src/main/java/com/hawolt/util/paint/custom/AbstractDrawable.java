package com.hawolt.util.paint.custom;

import java.awt.*;

/**
 * Created: 08/09/2023 12:28
 * Author: Twitter @hawolt
 **/

public abstract class AbstractDrawable implements DrawableGraphic {
    protected int x, y, width, height;
    protected Dimension dimension;
    protected Rectangle area;

    public AbstractDrawable(int x, int y, int width, int heigth) {
        this(new Rectangle(x, y, width, heigth));
    }

    public AbstractDrawable() {

    }

    public AbstractDrawable(Rectangle area) {
        this.update(area);
    }

    @Override
    public void update(Rectangle area) {
        this.dimension = new Dimension(area.width, area.height);
        this.height = area.height;
        this.width = area.width;
        this.area = area;
        this.x = area.x;
        this.y = area.y;
    }

    @Override
    public void draw(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        draw(graphics2D);
    }

    public boolean isConfigured() {
        return area != null;
    }

    @Override
    public Rectangle getContentArea() {
        return area;
    }

    @Override
    public Dimension getDimension() {
        return dimension;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}
