package com.hawolt.util.paint.custom.impl.objects;

import com.hawolt.util.paint.custom.event.GraphicalEvent;
import com.hawolt.util.paint.custom.event.GraphicalExecutionListener;
import com.hawolt.util.paint.custom.impl.AbstractGraphicalDrawable;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

/**
 * Created: 08/09/2023 13:27
 * Author: Twitter @hawolt
 **/

public abstract class AbstractGraphicalButton extends AbstractGraphicalDrawable {
    private final List<GraphicalExecutionListener> list = new LinkedList<>();
    protected boolean enabled = true, visible = true;

    public AbstractGraphicalButton() {

    }

    public AbstractGraphicalButton(Rectangle area) {
        super(area);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void addExecutionListener(GraphicalExecutionListener listener) {
        this.list.add(listener);
    }

    @Override
    public void onClick(MouseEvent e) {
        this.hold = false;
        if (!enabled || !visible) return;
        GraphicalEvent event = new GraphicalEvent(this, e);
        for (GraphicalExecutionListener listener : list) {
            listener.onEvent(event);
        }
    }
}
