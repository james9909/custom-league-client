package com.hawolt.client.resources.ledge.store.objects;

/**
 * Created: 21/08/2023 09:06
 * Author: Twitter @hawolt
 **/

public enum StoreSortProperty {
    BLUE_ESSENCE("Blue Essence"),
    RIOT_POINT("RP"),
    NAME("Name"),

    RELEASE_DATE("Release Date");

    private final String name;

    StoreSortProperty(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

