package com.hawolt.ui.store;

import com.hawolt.client.misc.SortOrder;
import com.hawolt.client.resources.ledge.store.objects.StoreSortProperty;

public record StoreSortOption(StoreSortProperty property, SortOrder order) {

    @Override
    public String toString() {
        String suffix = switch (this.order) {
            case DESCENDING -> "↓";
            case ASCENDING -> "↑";
        };
        return String.format("%s %s", this.property, suffix);
    }
}
