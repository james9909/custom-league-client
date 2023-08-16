package com.hawolt.client.resources.ledge.parties.objects;

/**
 * Created: 19/01/2023 17:40
 * Author: Twitter @hawolt
 **/

public class PartyException extends Exception {
    public PartyException() {
        super("NO_PARTY_AVAILABLE");
    }
}
