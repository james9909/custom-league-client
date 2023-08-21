package com.hawolt.client.resources.ledge.store.objects;

import java.util.Comparator;
import java.util.function.Function;

/**
 * Created: 21/08/2023 09:05
 * Author: Twitter @hawolt
 **/

public class StoreItemComparator implements Comparator<StoreItem> {
    private StoreSortProperty property;

    public StoreItemComparator(StoreSortProperty kind) {
        this.property = kind;
    }

    public void setProperty(StoreSortProperty property) {
        this.property = property;
    }

    @Override
    public int compare(StoreItem o1, StoreItem o2) {
        if (this.property == null) return -1;
        return switch (this.property) {
            case RIOT_POINT -> compareValues(o1, o2, StoreItem::getRiotPointCost);
            case BLUE_ESSENCE -> compareValues(o1, o2, StoreItem::getBlueEssenceCost);
            case NAME -> compareValues(o1, o2, StoreItem::getName);
        };
    }

    public <R extends Comparable<R>> int compareValues(StoreItem o1, StoreItem o2, Function<StoreItem, R> getValue) {
        R val1 = getValue.apply(o1);
        R val2 = getValue.apply(o2);
        return val1.compareTo(val2);
    }
}