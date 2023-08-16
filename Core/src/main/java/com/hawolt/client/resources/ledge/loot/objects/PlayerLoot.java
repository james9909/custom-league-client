package com.hawolt.client.resources.ledge.loot.objects;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created: 27/07/2023 22:52
 * Author: Twitter @hawolt
 **/

public class PlayerLoot implements Iterable<Loot> {
    private final List<Loot> list = new ArrayList<>();

    public PlayerLoot(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            list.add(new Loot(array.getJSONObject(i)));
        }
    }

    public List<Loot> getChampionList() {
        return list.stream()
                .filter(loot -> loot.getLootName().startsWith("CHAMPION_"))
                .filter(loot -> !loot.getLootName().startsWith("CHAMPION_SKIN_"))
                .collect(Collectors.toList());
    }

    public int getBlueEssence() {
        return list.stream().filter(loot -> loot.getLootName().equals("CURRENCY_champion")).mapToInt(Loot::getCount).sum();
    }

    public int getRiotPoints() {
        return list.stream().filter(loot -> loot.getLootName().equals("CURRENCY_RP")).mapToInt(Loot::getCount).sum();
    }

    public int getOrangeEssence() {
        return list.stream().filter(loot -> loot.getLootName().equals("CURRENCY_cosmetic")).mapToInt(Loot::getCount).sum();
    }

    public int getMythicEssence() {
        return list.stream().filter(loot -> loot.getLootName().equals("CURRENCY_mythic")).mapToInt(Loot::getCount).sum();
    }

    public List<Loot> getSkinList() {
        return list.stream().filter(loot -> loot.getLootName().startsWith("CHAMPION_SKIN_RENTAL")).collect(Collectors.toList());
    }

    public List<Loot> getList() {
        return list;
    }

    @NotNull
    @Override
    public Iterator<Loot> iterator() {
        return list.iterator();
    }
}
