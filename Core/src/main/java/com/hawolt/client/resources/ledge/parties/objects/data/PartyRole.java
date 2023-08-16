package com.hawolt.client.resources.ledge.parties.objects.data;

/**
 * Created: 19/01/2023 17:23
 * Author: Twitter @hawolt
 **/

public enum PartyRole {
    DECLINED;

    @Override
    public String toString() {
        return "\"" + name() + "\"";
    }
}
