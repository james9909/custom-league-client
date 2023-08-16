package com.hawolt.client.resources.ledge.parties.objects.data;

/**
 * Created: 19/01/2023 18:19
 * Author: Twitter @hawolt
 **/

public enum PartyType {
    OPEN, CLOSED;

    @Override
    public String toString() {
        return "\"" + name().toLowerCase() + "\"";
    }
}
