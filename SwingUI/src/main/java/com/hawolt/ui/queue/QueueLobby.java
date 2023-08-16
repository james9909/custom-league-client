package com.hawolt.ui.queue;

import com.hawolt.LeagueClientUI;
import com.hawolt.client.resources.ledge.LedgeEndpoint;
import com.hawolt.client.resources.ledge.parties.PartiesLedge;
import com.hawolt.client.resources.ledge.parties.objects.PartiesRegistration;
import com.hawolt.client.resources.ledge.parties.objects.PartyException;
import com.hawolt.client.resources.ledge.parties.objects.data.PartyAction;
import com.hawolt.client.resources.ledge.parties.objects.data.PositionPreference;
import com.hawolt.client.resources.ledge.parties.objects.invitation.PartyInvitation;
import com.hawolt.client.resources.ledge.summoner.SummonerLedge;
import com.hawolt.client.resources.ledge.summoner.objects.Summoner;
import com.hawolt.logger.Logger;
import com.hawolt.util.panel.ChildUIComponent;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created: 11/08/2023 23:00
 * Author: Twitter @hawolt
 **/

public class QueueLobby extends ChildUIComponent {

    public QueueLobby(LeagueClientUI leagueClientUI, Container parent, CardLayout layout) {
        super(new BorderLayout());
        JButton close = new JButton("Return to previous Component");
        close.addActionListener(listener -> layout.show(parent, "modes"));
        add(close, BorderLayout.NORTH);

        ChildUIComponent component = new ChildUIComponent(new BorderLayout());
        JButton invite = new JButton("Invite another Summoner");
        invite.addActionListener(listener -> {
            String name = (String) JOptionPane.showInputDialog(
                    this,
                    "Summonername",
                    "Invite",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "");
            if (name == null) return;
            LedgeEndpoint ledges = leagueClientUI.getLeagueClient().getLedge();
            SummonerLedge summonerLedge = ledges.getSummoner();
            PartiesLedge partiesLedge = ledges.getParties();
            PartiesRegistration registration = partiesLedge.getCurrentRegistration();
            try {
                Summoner summoner = summonerLedge.resolveSummonerByName(name);
                PartyInvitation invitation = partiesLedge.invite(registration.getFirstPartyId(), summoner.getPUUID());
                Logger.error(invitation);
            } catch (IOException | PartyException e) {
                Logger.error(e);
            }
        });
        component.add(invite, BorderLayout.NORTH);

        ChildUIComponent roles = new ChildUIComponent(new GridLayout(0, 2, 5, 0));
        JComboBox<PositionPreference> main = new JComboBox<>(PositionPreference.values());
        roles.add(main);
        JComboBox<PositionPreference> other = new JComboBox<>(PositionPreference.values());
        roles.add(other);
        component.add(roles, BorderLayout.SOUTH);

        add(component, BorderLayout.CENTER);

        JButton start = new JButton("START QUEUE");
        start.addActionListener(listener -> {
            PartiesLedge partiesLedge = leagueClientUI.getLeagueClient().getLedge().getParties();
            PartiesRegistration registration = partiesLedge.getCurrentRegistration();
            try {
                if (registration == null) registration = partiesLedge.register();
                PositionPreference primary = main.getItemAt(main.getSelectedIndex());
                PositionPreference secondary = other.getItemAt(other.getSelectedIndex());
                PartiesRegistration last = partiesLedge.metadata(registration.getFirstPartyId(), primary, secondary);
                partiesLedge.setQueueAction(last.getFirstPartyId(), PartyAction.START);
            } catch (IOException e) {
                Logger.error(e);
            }
        });
        add(start, BorderLayout.SOUTH);
    }
}
