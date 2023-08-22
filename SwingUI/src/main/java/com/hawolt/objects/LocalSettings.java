package com.hawolt.objects;

import org.json.JSONObject;

public class LocalSettings extends JSONObject {

    public LocalSettings() {
    }

    public LocalSettings(String username, String password, boolean rememberMe) {
        put("username", username);
        put("password", password);
        put("rememberMe", rememberMe);
    }

    public String getUsername() {
        return getString("username");
    }

    public void setUsername(String username) {
        put("username", username);
    }

    public String getPassword() {
        return getString("password");
    }

    public void setPassword(String password) {
        put("password", password);
    }

    public boolean isRememberMe() {
        return getBoolean("rememberMe");
    }

    public void setRememberMe(boolean rememberMe) {
        put("rememberMe", rememberMe);
    }
}
