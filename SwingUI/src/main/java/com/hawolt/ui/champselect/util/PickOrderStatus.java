package com.hawolt.ui.champselect.util;

import org.json.JSONObject;

/**
 * Created: 08/09/2023 19:32
 * Author: Twitter @hawolt
 **/

public class PickOrderStatus {
    private final int id, cellId;
    private final String state;

    public PickOrderStatus(JSONObject object) {
        this.id = object.getInt("id");
        this.cellId = object.getInt("cellId");
        this.state = object.getString("state");
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
}
