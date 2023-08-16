package com.hawolt.client.resources.ledge.summoner.objects;

import org.json.JSONArray;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created: 28/07/2023 00:07
 * Author: Twitter @hawolt
 **/

public class SummonerValidation {
    private final JSONArray array;

    public SummonerValidation(JSONArray array) {
        this.array = array;
    }

    public boolean isValid() {
        return array.isEmpty();
    }

    public List<String> getInvalidationReasoning() {
        return array.toList().stream().map(Object::toString).collect(Collectors.toList());
    }
}
