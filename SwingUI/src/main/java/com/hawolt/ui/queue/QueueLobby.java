package com.hawolt.ui.queue;

import com.hawolt.LeagueClientUI;
import com.hawolt.async.ExecutorManager;
import com.hawolt.client.resources.ledge.LedgeEndpoint;
import com.hawolt.client.resources.ledge.parties.PartiesLedge;
import com.hawolt.client.resources.ledge.parties.objects.*;
import com.hawolt.client.resources.ledge.parties.objects.data.PartyAction;
import com.hawolt.client.resources.ledge.parties.objects.data.PositionPreference;
import com.hawolt.client.resources.ledge.summoner.SummonerLedge;
import com.hawolt.client.resources.ledge.summoner.objects.Summoner;
import com.hawolt.logger.Logger;
import com.hawolt.util.panel.ChildUIComponent;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created: 11/08/2023 23:00
 * Author: Twitter @hawolt
 **/

public class QueueLobby extends ChildUIComponent {
    private final ScheduledExecutorService scheduler = ExecutorManager.getScheduledService("queue-resumer");
    private ScheduledFuture<?> future;

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
            try {
                Summoner summoner = summonerLedge.resolveSummonerByName(name);
                partiesLedge.invite(summoner.getPUUID());
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

        ChildUIComponent bottom = new ChildUIComponent(new GridLayout(0, 1, 0, 0));
        JButton start = new JButton("START QUEUE");
        start.addActionListener(listener -> {
            PartiesLedge partiesLedge = leagueClientUI.getLeagueClient().getLedge().getParties();
            PartiesRegistration registration = partiesLedge.getCurrentRegistration();
            try {
                if (registration == null) partiesLedge.register();
                partiesLedge.ready();
                PositionPreference primary = main.getItemAt(main.getSelectedIndex());
                PositionPreference secondary = other.getItemAt(other.getSelectedIndex());
                partiesLedge.metadata(primary, secondary);
                JSONObject response = partiesLedge.setQueueAction(PartyAction.START);
                List<GatekeeperRestriction> direct = response.has("errorCode") &&
                        "GATEKEEPER_RESTRICTED".equals(response.getString("errorCode")) ?
                        new PartyGatekeeper(response).getRestrictionList() : new ArrayList<>();
                CurrentParty party = partiesLedge.getOwnPlayer().getCurrentParty();
                PartyRestriction restriction = party.getPartyRestriction();
                List<GatekeeperRestriction> indirect = restriction != null ?
                        restriction.getRestrictionList() : new ArrayList<>();
                if (direct.isEmpty() && indirect.isEmpty()) return;
                List<GatekeeperRestriction> sorted = Stream.of(direct, indirect)
                        .flatMap(Collection::stream)
                        .sorted(((o1, o2) -> Long.compare(o2.getRemainingMillis(), o1.getRemainingMillis())))
                        .toList();
                GatekeeperRestriction gatekeeperRestriction = sorted.get(0);
                Logger.debug("Restriction: {}", gatekeeperRestriction);
                leagueClientUI.getChatSidebar().getEssentials().toggleQueueState(
                        System.currentTimeMillis(),
                        gatekeeperRestriction.getRemainingMillis(),
                        true
                );
                future = scheduler.schedule(() -> {
                    try {
                        partiesLedge.resume();
                    } catch (IOException e) {
                        Logger.error(e);
                    }
                }, gatekeeperRestriction.getRemainingMillis(), TimeUnit.MILLISECONDS);
            } catch (IOException e) {
                Logger.error(e);
            }
        });
        bottom.add(start);
        JButton stop = new JButton("STOP QUEUE");
        stop.addActionListener(listener -> {
            if (future != null) future.cancel(true);
            PartiesLedge partiesLedge = leagueClientUI.getLeagueClient().getLedge().getParties();
            PartiesRegistration registration = partiesLedge.getCurrentRegistration();
            try {
                if (registration == null) return;
                partiesLedge.setQueueAction(PartyAction.STOP);
                leagueClientUI.getChatSidebar().getEssentials().disableQueueState();
            } catch (IOException e) {
                Logger.error(e);
            }
        });
        bottom.add(stop);
        add(bottom, BorderLayout.SOUTH);
    }
}
