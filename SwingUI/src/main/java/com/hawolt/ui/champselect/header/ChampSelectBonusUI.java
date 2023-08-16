package com.hawolt.ui.champselect.header;

import com.hawolt.logger.Logger;
import org.json.JSONArray;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created: 06/08/2023 14:09
 * Author: Twitter @hawolt
 **/

public class ChampSelectBonusUI extends JPanel {
    private ChampSelectBanComponent[] components = new ChampSelectBanComponent[5];

    public ChampSelectBonusUI() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(300, 70));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new GridLayout(0, 5, 5, 5));
        reset();
    }

    public void reset() {
        this.removeAll();
        for (int i = 0; i < components.length; i++) {
            ChampSelectBanComponent component = new ChampSelectBanComponent();
            components[i] = component;
            component.update(-1);
            this.add(components[i]);
        }
    }

    public void rebuild(JSONArray team) {
        Logger.info("REBUILDING BAN UI");
        this.removeAll();
        this.components = new ChampSelectBanComponent[team.length()];
        for (int i = 0; i < team.length(); i++) {
            this.components[i] = new ChampSelectBanComponent();
            this.components[i].update(-1);
            this.add(components[i]);
        }
    }

    public void update(int normalizedCellId, int championId) {
        if (championId == 0) return;
        ChampSelectBanComponent component = components[normalizedCellId];
        component.update(championId);
    }
}
