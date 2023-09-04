package com.hawolt.client.resources.ledge.store.objects;

/**
 * Created: 21/08/2023 09:06
 * Author: Twitter @hawolt
 **/

public enum StoreSortProperty {
    RELEASE_DATE("Release Date"),
    RIOT_POINT("RP"),
    BLUE_ESSENCE("Blue Essence"),
    NAME("Name");

    private final String name;

    StoreSortProperty(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

