package com.hawolt.util.paint.custom.impl;

import com.hawolt.util.paint.custom.AbstractDrawable;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created: 08/09/2023 12:21
 * Author: Twitter @hawolt
 **/

public abstract class AbstractGraphicalDrawable extends AbstractDrawable {
    protected boolean hover, hold;

    public AbstractGraphicalDrawable() {

    }

    public AbstractGraphicalDrawable(int x, int y, int width, int heigth) {
        super(x, y, width, heigth);
    }

    public AbstractGraphicalDrawable(Rectangle area) {
        super(area);
    }

    public void onHover(boolean status) {
        this.hover = status;
        if (status) return;
        this.hold = false;
    }

    public void onMouseDown() {
        this.hold = true;
    }

    public abstract void onClick(MouseEvent e);

    public boolean isHover() {
        return hover;
    }

    public boolean isHold() {
        return hold;
    }
}
