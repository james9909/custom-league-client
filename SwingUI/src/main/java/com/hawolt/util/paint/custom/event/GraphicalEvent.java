package com.hawolt.util.paint.custom.event;

/**
 * Created: 08/09/2023 13:33
 * Author: Twitter @hawolt
 **/

public class GraphicalEvent {
    private final long timestamp = System.currentTimeMillis();
    private final Object source, initiator;

    public GraphicalEvent(Object source, Object initiator) {
        this.initiator = initiator;
        this.source = source;
    }

    public Object getInitiator() {
        return initiator;
    }

    public Object getSource() {
        return source;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
