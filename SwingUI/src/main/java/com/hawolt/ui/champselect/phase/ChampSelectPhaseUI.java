package com.hawolt.ui.champselect.phase;

import com.hawolt.client.LeagueClient;
import com.hawolt.ui.champselect.ChampSelect;
import com.hawolt.ui.champselect.IChampSelection;
import com.hawolt.ui.champselect.chat.ChampSelectChatUI;
import com.hawolt.ui.champselect.phase.ban.ChampSelectBanPhaseUI;
import com.hawolt.ui.champselect.phase.pick.ChampSelectPickPhaseUI;
import com.hawolt.ui.champselect.settings.ChampSelectSetting;
import com.hawolt.ui.chat.window.ChatUI;
import com.hawolt.util.panel.ChildUIComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created: 06/08/2023 18:58
 * Author: Twitter @hawolt
 **/

public class ChampSelectPhaseUI extends ChildUIComponent {
    private final CardLayout layout = new CardLayout();
    private final JPanel card = new JPanel();
    private final ChampSelectPickPhaseUI pickPhaseUI;
    private final ChampSelectBanPhaseUI banPhaseUI;
    private final ChampSelectSetting setting;
    private final ChampSelectChatUI csChatUI;
    private final JButton dodge;
    private String current;

    public ChampSelectPhaseUI(ChampSelect champSelect, IChampSelection selection, LeagueClient leagueClient, ChampSelectSetting settings, ChatUI chatUI) {
        super(new BorderLayout());
        this.add(card, BorderLayout.CENTER);
        this.card.setLayout(layout);
        this.card.add("pick", pickPhaseUI = new ChampSelectPickPhaseUI(selection));
        this.card.add("ban", banPhaseUI = new ChampSelectBanPhaseUI(selection));

        ChildUIComponent component = new ChildUIComponent(new BorderLayout());
        setting = settings;
        component.add(setting, BorderLayout.NORTH);
        component.add(csChatUI = new ChampSelectChatUI(champSelect, chatUI), BorderLayout.CENTER);

        ChildUIComponent buttons = new ChildUIComponent(new GridLayout(0, 1, 0, 0));
        component.add(buttons, BorderLayout.SOUTH);
        dodge = new JButton("Dodge");
        dodge.setActionCommand("DODGE");
        buttons.add(dodge);
        this.add(component, BorderLayout.SOUTH);
    }

    public ChampSelectSetting getSetting() {
        return setting;
    }

    public String getCurrent() {
        return current;
    }

    public void show(String name) {
        this.current = name;
        this.layout.show(card, name);
    }

    public ChampSelectPickPhaseUI getPickPhaseUI() {
        return pickPhaseUI;
    }

    public ChampSelectBanPhaseUI getBanPhaseUI() {
        return banPhaseUI;
    }

    public ChampSelectChatUI getChatUI() {
        return csChatUI;
    }

    public void configure(ActionListener listener) {
        this.dodge.addActionListener(listener);
    }
}
