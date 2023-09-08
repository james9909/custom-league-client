package com.hawolt.ui.queue;

import com.hawolt.LeagueClientUI;
import com.hawolt.client.LeagueClient;
import com.hawolt.client.resources.ledge.parties.PartiesLedge;
import com.hawolt.client.resources.ledge.parties.objects.PartiesRegistration;
import com.hawolt.client.resources.ledge.parties.objects.data.PartyRole;
import com.hawolt.client.resources.ledge.parties.objects.data.PartyType;
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
import com.hawolt.util.ColorPalette;
import com.hawolt.util.audio.AudioEngine;
import com.hawolt.util.audio.Sound;
import com.hawolt.util.panel.ChildUIComponent;
import com.hawolt.util.ui.LFlatButton;
import com.hawolt.util.ui.LHighlightType;
import com.hawolt.util.ui.LLabel;
import com.hawolt.util.ui.LTextAlign;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created: 10/08/2023 15:52
 * Author: Twitter @hawolt
 **/

public class QueueWindow extends ChildUIComponent implements Runnable, PacketCallback, IServiceMessageListener<RiotMessageServiceMessage> {
    private final CardLayout layout = new CardLayout();
    private final LeagueClientUI leagueClientUI;
    private final ChildUIComponent parent;
    private final DraftQueueLobby lobby;
    private final TFTQueueLobby tftLobby;
    Boolean init = false;

    ChildUIComponent main = new ChildUIComponent(new BorderLayout());

    public QueueWindow(LeagueClientUI leagueClientUI) {
        super(new BorderLayout());
        this.leagueClientUI = leagueClientUI;
        this.add(parent = new ChildUIComponent(layout), BorderLayout.CENTER);
        lobby = new DraftQueueLobby(leagueClientUI, parent, layout);
        tftLobby = new TFTQueueLobby(leagueClientUI, parent, layout);
        LeagueClientUI.service.execute(this);
    }


    public DraftQueueLobby getDraftLobby() {
        Component[] components = this.parent.getComponents();
        boolean alreadyShown = false;
        for (int i = 0; i < components.length; i++) {
            if (!alreadyShown && components[i].getClass().getCanonicalName().equals("com.hawolt.ui.queue.DraftQueueLobby"))
                alreadyShown = true;
            if (components[i].getClass().getCanonicalName().equals("com.hawolt.ui.queue.TFTQueueLobby"))
                this.parent.remove(i);
        }
        if (!alreadyShown) {
            this.parent.add("lobby", lobby);
        }
        return lobby;
    }


    public TFTQueueLobby getTftLobby() {
        Component[] components = this.parent.getComponents();
        boolean alreadyShown = false;
        for (int i = 0; i < components.length; i++) {
            if (!alreadyShown && components[i].getClass().getCanonicalName().equals("com.hawolt.ui.queue.TFTQueueLobby"))
                alreadyShown = true;
            if (components[i].getClass().getCanonicalName().equals("com.hawolt.ui.queue.DraftQueueLobby"))
                this.parent.remove(i);
        }
        if (!alreadyShown) {
            this.parent.add("lobby", tftLobby);
        }
        return tftLobby;
    }

    @Override
    public void onPacket(RtmpPacket rtmpPacket, TypedObject typedObject) throws Exception {
        TypedObject data = typedObject.getTypedObject("data");
        TypedObject message = data.getTypedObject("flex.messaging.messages.AcknowledgeMessage");
        String body = Base64GZIP.unzipBase64(message.getString("body"));
        JSONArray array = new JSONArray(body);
        Map<String, List<JSONObject>> map = new HashMap<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String state = object.getString("queueState");
            String shortName = object.getString("shortName");
            if (!shortName.contains("DRAFT") && !shortName.contains("RANKED-FLEX") && !shortName.contains("RANKED-SOLO") && !shortName.contains("TFT"))
                continue;
            if ("OFF".equals(state)) continue;
            String gameMode = object.getString("gameMode");
            if (!map.containsKey(gameMode)) map.put(gameMode, new ArrayList<>());
            map.get(gameMode).add(object);
        }
        ChildUIComponent modes = new ChildUIComponent(new GridLayout(0, (int) map.keySet().stream().filter(o -> !o.contains("TUTORIAL")).count(), 5, 0));
        modes.setBorder(new EmptyBorder(5, 5, 5, 5));
        main.setBackground(ColorPalette.BACKGROUND_COLOR);
        modes.setBackground(ColorPalette.BACKGROUND_COLOR);
        for (String key : map.keySet()) {
            if (key.contains("TUTORIAL")) continue;
            ChildUIComponent parent = new ChildUIComponent(new BorderLayout());
            ChildUIComponent grid = new ChildUIComponent(new GridLayout(0, 1, 0, 5));
            parent.setBackground(ColorPalette.BACKGROUND_COLOR);
            grid.setBackground(ColorPalette.BACKGROUND_COLOR);

            //Mode label
            LLabel label = new LLabel(key, LTextAlign.CENTER, true);

            grid.add(label);

            for (JSONObject object : map.get(key)) {
                String name = object.getString("shortName");
                if (name.contains("TUTORIAL")) {
                    continue;
                }

                LFlatButton button = new LFlatButton(name, LTextAlign.LEFT, LHighlightType.COMPONENT);

                button.setPreferredSize(new Dimension(grid.getWidth() / 4, 30));

                button.setActionCommand(object.toString());
                if (key.contains("CLASSIC")) {
                    button.addActionListener(e -> goToLobby(e, "CLASSIC"));
                } else if (key.contains("TFT")) {
                    button.addActionListener(e -> goToLobby(e, "TFT"));
                }
                grid.add(button);
            }
            parent.add(grid, BorderLayout.NORTH);
            modes.add(parent);
        }
        main.add(modes, BorderLayout.CENTER);

        this.parent.add("modes", main);
        layout.show(parent, "modes");
        revalidate();
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
            /*
             * other existing backwardsTransitionReason
             * 1.   AFK_CHECK_FAILED
             */
            switch (info.getString("backwardsTransitionReason")) {
                case "PLAYER_TIMED_OUT_ON_REQUIRED_ACTION", "PLAYER_LEFT_CHAMPION_SELECT", "PLAYER_LEFT_MATCHMAKING" -> {
                    leagueClientUI.getLayoutManager().getChampSelect().showBlankPanel();
                }
            }
            leagueClientUI.getChatSidebar().getEssentials().disableQueueState();
            try {
                leagueClientUI.getLeagueClient().getLedge().getParties().ready();
            } catch (IOException e) {
                Logger.error(e);
            }
        } else if (payload.has("phaseName")) {
            String phaseName = payload.getString("phaseName");
            ChatSidebarEssentials essentials = leagueClientUI.getChatSidebar().getEssentials();
            if (phaseName.equals("MATCHMAKING")) {
                JSONObject matchmakingState = payload.getJSONObject("matchmakingState");
                long estimatedMatchmakingTimeMillis = matchmakingState.getLong("estimatedMatchmakingTimeMillis");
                if (payload.getInt("counter") != 0) return;
                essentials.toggleQueueState(System.currentTimeMillis(), estimatedMatchmakingTimeMillis);
                revalidate();
            } else if (phaseName.equals("AFK_CHECK")) {
                AudioEngine.play(Sound.QUEUE_POP);
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

    public void goToLobby(ActionEvent e, String mode) {
        Logger.error("goTo Lobby mode: " + mode);
        if (mode.equals("CLASSIC")) {
            this.parent.add("lobby", lobby);
            layout.show(parent, "lobby");
        } else if (mode.equals("TFT")) {
            this.parent.add("lobby", tftLobby);
            layout.show(parent, "lobby");
        }
        if (!init) {
            LFlatButton button = new LFlatButton("Show Lobby", LTextAlign.CENTER);
            button.setPreferredSize(new Dimension(getWidth() / 5, 30));
            button.setHorizontalAlignment(SwingConstants.CENTER);
            button.setVerticalAlignment(SwingConstants.CENTER);
            button.addActionListener(listener -> layout.show(parent, "lobby"));
            main.add(button, BorderLayout.SOUTH);
            init = true;
        }
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
            //TODO revisit
            /*JSONObject partiesPositionPreferences = PlayerPreferencesService.get().getSettings().getPartiesPositionPreferences();
            JSONObject data = partiesPositionPreferences.getJSONObject("data");
            PositionPreference primary = PositionPreference.valueOf(data.getString("firstPreference"));
            PositionPreference secondary = PositionPreference.valueOf(data.getString("secondPreference"));
            partiesLedge.metadata(primary, secondary);*/
        } catch (IOException ex) {
            Logger.error(ex);
        }
    }
}
