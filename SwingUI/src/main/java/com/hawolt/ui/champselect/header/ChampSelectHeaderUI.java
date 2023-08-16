package com.hawolt.ui.champselect.header;

import com.hawolt.logger.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

/**
 * Created: 06/08/2023 14:10
 * Author: Twitter @hawolt
 **/

public class ChampSelectHeaderUI extends JPanel {

    private final ChampSelectBonusUI teamOneUI, teamTwoUI;
    private final ChampSelectTimerUI timerUI;

    public ChampSelectHeaderUI() {
        this.setLayout(new BorderLayout());
        this.add(teamTwoUI = new ChampSelectBonusUI(), BorderLayout.EAST);
        this.add(teamOneUI = new ChampSelectBonusUI(), BorderLayout.WEST);
        this.add(timerUI = new ChampSelectTimerUI(), BorderLayout.CENTER);
    }

    public void reset() {
        teamOneUI.reset();
        teamTwoUI.reset();
        timerUI.update(-2, "");
    }

    public ChampSelectBonusUI getTeamTwoUI() {
        return teamOneUI;
    }

    public ChampSelectBonusUI getTeamOneUI() {
        return teamTwoUI;
    }

    public ChampSelectTimerUI getTimerUI() {
        return timerUI;
    }

    public void update(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            JSONObject action = array.getJSONObject(i);
            int actorCellId = action.getInt("actorCellId");
            int championId = action.getInt("championId");
            boolean teamOne = actorCellId < 5;
            int normalizedCellId = actorCellId % 5;
            ChampSelectBonusUI bonusUI = teamOne ? teamOneUI : teamTwoUI;
            bonusUI.update(normalizedCellId, championId);
        }

    }
}
