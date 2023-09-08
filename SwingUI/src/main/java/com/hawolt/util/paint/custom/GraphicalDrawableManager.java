package com.hawolt.util.paint.custom;

import com.hawolt.generic.data.Unsafe;
import com.hawolt.util.paint.custom.impl.AbstractGraphicalDrawable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created: 08/09/2023 13:20
 * Author: Twitter @hawolt
 **/

public class GraphicalDrawableManager extends MouseAdapter implements Drawable, Iterable<AbstractGraphicalDrawable> {

    private final Map<String, AbstractGraphicalDrawable> drawables = new ConcurrentHashMap<>();
    private final Object lock = new Object();
    private final JComponent source;

    public GraphicalDrawableManager(JComponent source) {
        this.source = source;
    }

    public <T> T getGraphicalComponent(String name) {
        return Unsafe.cast(drawables.get(name));
    }

    public void register(String name, AbstractGraphicalDrawable drawable) {
        synchronized (lock) {
            this.drawables.put(name, drawable);
        }
    }

    @NotNull
    @Override
    public Iterator<AbstractGraphicalDrawable> iterator() {
        synchronized (lock) {
            return drawables.values().iterator();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        boolean repaint = false;
        for (AbstractGraphicalDrawable drawable : this) {
            if (!drawable.isConfigured() || !drawable.isHover()) continue;
            drawable.onHover(false);
            repaint = true;
        }
        if (repaint) this.source.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        boolean repaint = false;
        for (AbstractGraphicalDrawable drawable : this) {
            if (!drawable.isConfigured()) continue;
            if (drawable.getContentArea().contains(e.getPoint())) {
                if (!drawable.isHover()) {
                    drawable.onHover(true);
                    repaint = true;
                }
            } else {
                if (drawable.isHover()) {
                    drawable.onHover(false);
                    repaint = true;
                }
            }
        }
        if (repaint) this.source.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (AbstractGraphicalDrawable drawable : this) {
            if (!drawable.isConfigured()) continue;
            if (drawable.getContentArea().contains(e.getPoint())) {
                drawable.onMouseDown();
            }
        }
        this.source.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (AbstractGraphicalDrawable drawable : this) {
            if (!drawable.isConfigured()) continue;
            if (drawable.getContentArea().contains(e.getPoint())) {
                drawable.onClick(e);
            }
        }
        this.source.repaint();
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        for (AbstractGraphicalDrawable drawable : this) drawable.draw(graphics2D);
    }

    @Override
    public void draw(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        draw(graphics2D);
    }
}
