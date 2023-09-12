package com.hawolt.ui.queue;

import com.hawolt.LeagueClientUI;
import com.hawolt.client.resources.ledge.parties.PartiesLedge;
import com.hawolt.client.resources.ledge.parties.objects.data.PositionPreference;
import com.hawolt.logger.Logger;
import com.hawolt.util.panel.ChildUIComponent;
import com.hawolt.util.ui.LComboBox;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * Created: 11/08/2023 23:00
 * Author: Twitter @hawolt
 **/

public class DraftQueueLobby extends QueueLobby {
    JComboBox<PositionPreference> main, other;

    public DraftQueueLobby(LeagueClientUI leagueClientUI, Container parent, CardLayout layout) {
        super(leagueClientUI, parent, layout);

    }


    @Override
    protected void createSpecificComponents(ChildUIComponent component) {
        ChildUIComponent roles = new ChildUIComponent(new GridLayout(0, 2, 5, 0));
        roles.setBorder(new EmptyBorder(5, 5, 5, 5));
        main = new LComboBox<>(PositionPreference.values());
        main.addActionListener(this);
        roles.add(main);
        other = new LComboBox<>(PositionPreference.values());
        other.addActionListener(this);
        roles.add(other);
        component.add(roles, BorderLayout.SOUTH);
    }

    @Override
    protected void createGrid(ChildUIComponent component) {
        grid = new ChildUIComponent(new GridLayout(1, 5));
        for (int i = 0; i < 5; i++) grid.add(new DraftSummonerComponent());
        grid.setBackground(Color.YELLOW);
        component.add(grid, BorderLayout.CENTER);
    }


    @Override
    public DraftSummonerComponent getSummonerComponentAt(int id) {
        int index;
        if (id == 0) index = 2;
        else if (id == 1) index = 1;
        else if (id == 2) index = 3;
        else if (id == 3) index = 0;
        else index = 4;
        return (DraftSummonerComponent) grid.getComponent(index);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e != null) {
            try {
                PositionPreference primary = main.getItemAt(main.getSelectedIndex());
                PositionPreference secondary = other.getItemAt(other.getSelectedIndex());
                PartiesLedge partiesLedge = leagueClientUI.getLeagueClient().getLedge().getParties();
                partiesLedge.metadata(primary, secondary);
            } catch (IOException ex) {
                Logger.error(ex);
            }
        }
    }


}
