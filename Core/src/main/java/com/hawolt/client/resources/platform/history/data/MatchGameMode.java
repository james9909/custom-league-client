package com.hawolt.client.resources.platform.history.data;

/**
 * Created: 14/01/2023 01:28
 * Author: Twitter @hawolt
 **/

public enum MatchGameMode {
    LOL, TFT;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
