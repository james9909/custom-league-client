package com.hawolt.client.resources.ledge.store.objects;

public enum SubInventoryType {
    CHAMPION_BUNDLE(false),
    SKIN_BUNDLE(false),
    SKIN_VARIANT_BUNDLE(false),
    RECOLOR(false),
    CHROMA_BUNDLE(false),
    ICON_BUNDLE(false),
    HEXTECH_BUNDLE(false),
    RUNE_PAGE_BUNDLE(false),
    LOL_EVENT_PASS(false),
    TFT_PASS(false),
    LOL_CLASH_TICKETS(true),
    LOL_CLASH_PREMIUM_TICKETS(true),
    TFT_STAR_FRAGMENTS(true),
    CHEST(false),
    MATERIAL(false);
    final boolean isLowerCase;

    SubInventoryType(boolean isLowerCase) {
        this.isLowerCase = isLowerCase;
    }

    public boolean isLowerCase() {
        return isLowerCase;
    }

    @Override
    public String toString() {
        return !isLowerCase ? name() : name().toLowerCase();
    }
}
