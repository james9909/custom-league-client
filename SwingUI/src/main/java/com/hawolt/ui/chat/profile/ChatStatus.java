package com.hawolt.ui.chat.profile;

/**
 * Created: 08/08/2023 19:16
 * Author: Twitter @hawolt
 **/

public enum ChatStatus {
    DEFAULT("default"),
    ONLINE("chat"),
    DO_NOT_DISTURB("away"),
    INGAME("dnd"),
    MOBILE("mobile"),
    OFFLINE("offline");

    private final String status;

    ChatStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return super.toString().replaceAll("_", " ");
    }
}
