package com.hawolt.ui.champselect;

import com.hawolt.objects.Champion;

import java.util.Map;

/**
 * Created: 10/08/2023 16:39
 * Author: Twitter @hawolt
 **/

public interface IChampSelection {
    void onSelect(ChampSelectPhase phase, long championId);

    Map<Integer, Champion> getChampionCache();
}
