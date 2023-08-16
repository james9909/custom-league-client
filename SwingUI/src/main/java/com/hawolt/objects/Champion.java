package com.hawolt.objects;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: 19/04/2023 16:37
 * Author: Twitter @hawolt
 **/

public class Champion {
    private final List<String> roles = new ArrayList<>();
    private final String name, alias;
    private final long id;

    public Champion(JSONObject o) {
        this.name = o.getString("name");
        this.alias = o.getString("alias");
        this.id = o.getLong("id");
        JSONArray roles = o.getJSONArray("roles");
        for (int i = 0; i < roles.length(); i++) {
            this.roles.add(roles.getString(i));
        }
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public long getId() {
        return id;
    }
}
