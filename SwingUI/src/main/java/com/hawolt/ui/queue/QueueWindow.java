package com.hawolt.ui.queue;

import com.hawolt.LeagueClientUI;
import com.hawolt.client.LeagueClient;
import com.hawolt.client.resources.ledge.parties.PartiesLedge;
import com.hawolt.client.resources.ledge.parties.objects.PartiesRegistration;
import com.hawolt.client.resources.ledge.parties.objects.data.PartyRole;
import com.hawolt.client.resources.ledge.parties.objects.data.PartyType;
import com.hawolt.client.resources.ledge.parties.objects.data.PositionPreference;
import com.hawolt.client.resources.ledge.teambuilder.objects.MatchContext;
import com.hawolt.logger.Logger;
import com.hawolt.rms.data.subject.service.IServiceMessageListener;
import com.hawolt.rms.data.subject.service.MessageService;
import com.hawolt.rms.data.subject.service.RiotMessageServiceMessage;
import com.hawolt.rtmp.amf.TypedObject;
import com.hawolt.rtmp.io.RtmpPacket;
import com.hawolt.rtmp.utility.Base64GZIP;
import com.hawolt.rtmp.utility.PacketCallback;
import com.hawolt.ui.chat.friendlist.ChatSidebarEssentials;
import com.hawolt.ui.queue.pop.QueueDialog;
import com.hawolt.util.AudioEngine;
import com.hawolt.util.panel.ChildUIComponent;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created: 10/08/2023 15:52
 * Author: Twitter @hawolt
 **/

public class QueueWindow extends ChildUIComponent implements ActionListener, Runnable, PacketCallback, IServiceMessageListener<RiotMessageServiceMessage> {
    private final CardLayout layout = new CardLayout();
    private final LeagueClientUI leagueClientUI;
    private final ChildUIComponent parent;
    private final QueueLobby lobby;

    public QueueWindow(LeagueClientUI leagueClientUI) {
        super(new BorderLayout());
        this.leagueClientUI = leagueClientUI;
        this.add(parent = new ChildUIComponent(layout), BorderLayout.CENTER);
        this.parent.add("lobby", lobby = new QueueLobby(leagueClientUI, parent, layout));
        LeagueClientUI.service.execute(this);
    }

    public void showClientComponent(String name) {
        layout.show(parent, name);
    }

    public QueueLobby getLobby() {
        return lobby;
    }

    @Override
    public void onPacket(RtmpPacket rtmpPacket, TypedObject typedObject) {
        try {
            TypedObject data = typedObject.getTypedObject("data");
            TypedObject message = data.getTypedObject("flex.messaging.messages.AcknowledgeMessage");
            String body = Base64GZIP.unzipBase64(message.getString("body"));
            JSONArray array = new JSONArray(body);
            Map<String, List<JSONObject>> map = new HashMap<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String state = object.getString("queueState");
                String shortName = object.getString("shortName");
                if (!shortName.contains("DRAFT") && !shortName.contains("RANKED-FLEX") && !shortName.contains("RANKED-SOLO"))
                    continue;
                if ("OFF".equals(state)) continue;
                String gameMode = object.getString("gameMode");
                if (!map.containsKey(gameMode)) map.put(gameMode, new ArrayList<>());
                map.get(gameMode).add(object);
            }
            ChildUIComponent main = new ChildUIComponent(new BorderLayout());
            ChildUIComponent modes = new ChildUIComponent(new GridLayout(0, (int) map.keySet().stream().filter(o -> !o.contains("TUTORIAL")).count(), 5, 0));
            modes.setBorder(new EmptyBorder(5, 5, 5, 5));
            for (String key : map.keySet()) {
                if (key.contains("TUTORIAL")) continue;
                ChildUIComponent parent = new ChildUIComponent(new BorderLayout());
                ChildUIComponent grid = new ChildUIComponent(new GridLayout(0, 1, 0, 5));
                for (JSONObject object : map.get(key)) {
                    System.out.println(object.toString());
                    String name = object.getString("shortName");
                    JButton button = new JButton(name);
                    button.setActionCommand(object.toString());
                    button.addActionListener(this);
                    grid.add(button);
                }
                parent.add(grid, BorderLayout.NORTH);
                modes.add(parent);
            }
            main.add(modes, BorderLayout.CENTER);
            JButton button = new JButton("Show Lobby");
            button.addActionListener(listener -> layout.show(parent, "lobby"));
            main.add(button, BorderLayout.SOUTH);
            this.parent.add("modes", main);
            layout.show(parent, "modes");
            revalidate();
        } catch (IOException e) {
            Logger.error(e);
        }
    }

    @Override
    public void run() {
        try {
            LeagueClient client = leagueClientUI.getLeagueClient();
            client.getRMSClient().getHandler().addMessageServiceListener(MessageService.TEAMBUILDER, this);
            client.getRTMPClient().getMatchMakerService().getAllQueuesCompressedAsynchronous(this);
        } catch (IOException e) {
            Logger.error(e);
        }
    }

    @Override
    public void onMessage(RiotMessageServiceMessage riotMessageServiceMessage) {
        Logger.debug(riotMessageServiceMessage);
        JSONObject payload = riotMessageServiceMessage.getPayload().getPayload();
        if (payload.has("backwardsTransitionInfo")) {
            JSONObject info = payload.getJSONObject("backwardsTransitionInfo");
            if (!info.has("backwardsTransitionReason")) return;
            switch (info.getString("backwardsTransitionReason")) {
                case "PLAYER_LEFT_MATCHMAKING", "AFK_CHECK_FAILED" -> {
                    leagueClientUI.getChatSidebar().getEssentials().disableQueueState();
                    try {
                        leagueClientUI.getLeagueClient().getLedge().getParties().ready();
                    } catch (IOException e) {
                        Logger.error(e);
                    }
                }
            }
        } else if (payload.has("phaseName")) {
            String phaseName = payload.getString("phaseName");
            ChatSidebarEssentials essentials = leagueClientUI.getChatSidebar().getEssentials();
            if (phaseName.equals("MATCHMAKING")) {
                JSONObject matchmakingState = payload.getJSONObject("matchmakingState");
                long estimatedMatchmakingTimeMillis = matchmakingState.getLong("estimatedMatchmakingTimeMillis");
                essentials.toggleQueueState(System.currentTimeMillis(), estimatedMatchmakingTimeMillis);
                revalidate();
            } else if (phaseName.equals("AFK_CHECK")) {
                AudioEngine.play("matchmakingqueued.wav");
                JSONObject afkCheckState = payload.getJSONObject("afkCheckState");
                long maxAfkMillis = afkCheckState.getLong("maxAfkMillis");
                QueueDialog dialog = new QueueDialog(Frame.getFrames()[0], "Queue Notification", maxAfkMillis);
                if (dialog.showQueueDialog().getSelection() != 1) {
                    essentials.disableQueueState();
                } else {
                    try {
                        MatchContext context = leagueClientUI.getLeagueClient().getLedge().getTeamBuilder().indicateAfkReadiness();
                        Logger.info("Queue Accept: {}", context.getStatus());
                    } catch (IOException e) {
                        Logger.error(e);
                    }
                }
            } else {
                Logger.info("Ignored RMS Packet {}", riotMessageServiceMessage);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JSONObject json = new JSONObject(e.getActionCommand());
        long queueId = json.getLong("id");
        long maximumParticipantListSize = json.getLong("maximumParticipantListSize");
        PartiesLedge partiesLedge = leagueClientUI.getLeagueClient().getLedge().getParties();
        try {
            PartiesRegistration registration = partiesLedge.getCurrentRegistration();
            if (registration == null) partiesLedge.register();
            partiesLedge.role(PartyRole.DECLINED);
            partiesLedge.gamemode(
                    partiesLedge.getCurrentPartyId(),
                    maximumParticipantListSize,
                    0,
                    queueId
            );
            partiesLedge.partytype(PartyType.OPEN);
            partiesLedge.metadata(PositionPreference.FILL, PositionPreference.UNSELECTED);
            layout.show(parent, "lobby");
        } catch (IOException ex) {
            Logger.error(ex);
        }
    }
}
