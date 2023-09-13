package com.hawolt.client.resources.ledge.preferences.objects;

/**
 * Created: 11/09/2023 16:47
 * Author: Twitter @hawolt
 **/

public enum PreferenceType {
    LCU_SOCIAL_PREFERENCES("LcuSocialPreferences"),
    LCU_PREFERENCES("LCUPreferences");
    final String name;

    PreferenceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
