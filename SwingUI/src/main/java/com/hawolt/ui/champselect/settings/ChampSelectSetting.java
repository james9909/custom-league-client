package com.hawolt.ui.champselect.settings;

import com.hawolt.ui.champselect.ChampSelect;
import com.hawolt.util.panel.ChildUIComponent;

import java.awt.*;

/**
 * Created: 15/08/2023 19:22
 * Author: Twitter @hawolt
 **/

public class ChampSelectSetting extends ChildUIComponent {
    private final ChampSelectSummonerSpells summonerSpells;

    public ChampSelectSetting(ChampSelect champSelect) {
        super(new BorderLayout());
        this.add(summonerSpells = new ChampSelectSummonerSpells(champSelect), BorderLayout.CENTER);
    }

    public ChampSelectSummonerSpells getSummonerSpells() {
        return summonerSpells;
    }
}
