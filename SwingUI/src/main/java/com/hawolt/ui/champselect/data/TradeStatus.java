package com.hawolt.ui.champselect.data;

import org.json.JSONObject;

/**
 * Created: 08/09/2023 18:03
 * Author: Twitter @hawolt
 **/

public class TradeStatus {
    private final int id, cellId;
    private final String state;

    public TradeStatus(JSONObject object) {
        this.state = object.getString("state");
        this.cellId = object.getInt("cellId");
        this.id = object.getInt("id");
    }

    public int getId() {
        return id;
    }

    public int getCellId() {
        return cellId;
    }

    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return "TradeStatus{" +
                "id=" + id +
                ", cellId=" + cellId +
                ", state='" + state + '\'' +
                '}';
    }
}
