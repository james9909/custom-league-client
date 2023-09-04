package com.hawolt.client.resources.ledge.store.objects;

import java.util.Comparator;
import java.util.function.Function;

/**
 * Created: 21/08/2023 09:05
 * Author: Twitter @hawolt
 **/

public class StoreItemComparator implements Comparator<StoreItem> {
    private StoreSortProperty property;
    private StoreSortOrder order;

    public StoreItemComparator(StoreSortProperty kind, StoreSortOrder order) {
        this.property = kind;
        this.order = order;
    }

    public void setProperty(StoreSortProperty property) {
        this.property = property;
    }

    public void setOrder(StoreSortOrder order) {
        this.order = order;
    }

    @Override
    public int compare(StoreItem o1, StoreItem o2) {
        if (this.property == null) return -1;
        if (o1.hasDiscount() || o2.hasDiscount()) {
            return switch (this.property) {
                case RELEASE_DATE -> compareValues(o1, o2, StoreItem::getReleaseDate);
                case RIOT_POINT -> compareValues(o1, o2, StoreItem::getDiscountedCost);
                case BLUE_ESSENCE -> compareValues(o1, o2, StoreItem::getBlueEssenceCost);
                case NAME -> compareValues(o1, o2, StoreItem::getName);
            };
        } else {
            return switch (this.property) {
                case RELEASE_DATE -> compareValues(o1, o2, StoreItem::getReleaseDate);
                case RIOT_POINT -> compareValues(o1, o2, StoreItem::getRiotPointCost);
                case BLUE_ESSENCE -> compareValues(o1, o2, StoreItem::getBlueEssenceCost);
                case NAME -> compareValues(o1, o2, StoreItem::getName);
            };
        }
    }

    public <R extends Comparable<R>> int compareValues(StoreItem o1, StoreItem o2, Function<StoreItem, R> getValue) {
        R val1 = getValue.apply(o1);
        R val2 = getValue.apply(o2);
        return switch (this.order) {
            case DESCENDING -> val2.compareTo(val1);
            case ASCENDING -> val1.compareTo(val2);
        };
    }
}