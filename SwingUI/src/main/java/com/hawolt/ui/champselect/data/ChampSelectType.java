package com.hawolt.ui.champselect.data;

/**
 * Created: 29/08/2023 20:04
 * Author: Twitter @hawolt
 **/

public enum ChampSelectType {
    PICK("select"), BAN("ban");
    final String name;

    ChampSelectType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
