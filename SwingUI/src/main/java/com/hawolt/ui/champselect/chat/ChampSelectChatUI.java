package com.hawolt.ui.champselect.chat;

import com.hawolt.LeagueClientUI;
import com.hawolt.client.LeagueClient;
import com.hawolt.client.cache.CacheType;
import com.hawolt.client.resources.ledge.teambuilder.objects.MatchContext;
import com.hawolt.ui.champselect.ChampSelect;
import com.hawolt.ui.impl.JHintTextField;
import com.hawolt.util.panel.ChildUIComponent;
import com.hawolt.util.ui.SmartScroller;
import com.hawolt.xmpp.event.EventType;
import com.hawolt.xmpp.event.handler.message.IMessageListener;
import com.hawolt.xmpp.event.objects.conversation.history.impl.FailedMessage;
import com.hawolt.xmpp.event.objects.conversation.history.impl.IncomingMessage;
import com.hawolt.xmpp.event.objects.conversation.history.impl.OutgoingMessage;

import javax.swing.*;
import java.awt.*;

/**
 * Created: 14/08/2023 20:04
 * Author: Twitter @hawolt
 **/

public class ChampSelectChatUI extends ChildUIComponent implements IMessageListener {

    private final ChampSelect champSelect;
    private final JHintTextField input;
    private final JTextArea area;
    private MatchContext context;
    private LeagueClient client;


    public ChampSelectChatUI(ChampSelect champSelect) {
        super(new BorderLayout());
        this.champSelect = champSelect;
        if (champSelect != null) {
            this.client = champSelect.getLeagueClient();
            this.client.getXMPPClient().addHandler(EventType.ON_READY, baseObject -> client.getXMPPClient().addMessageListener(this));
        }
        this.setPreferredSize(new Dimension(0, 225));
        JScrollPane scrollPane = new JScrollPane(area = new JTextArea());
        SmartScroller.configure(scrollPane);
        this.area.setFont(new Font("Arial", Font.PLAIN, 22));
        this.area.setEditable(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(input = new JHintTextField("Send message"), BorderLayout.SOUTH);
        this.input.addActionListener(listener -> {
            if (context == null) {
                this.input.setText("");
            } else {
                String domain = String.format("champ-select.%s.pvp.net", context.getPayload().getTargetRegion());
                String jid = String.format("%s@%s", context.getPayload().getChatRoomName(), domain);
                client.getXMPPClient().sendGroupMessage(jid, input.getText(), null);
                this.input.setText("");
            }
        });
    }

    public void reset() {
        this.area.setText("");
    }

    public void build() {
        reset();
        //JOIN CHAT
        LeagueClientUI.service.execute(() -> {
            if (client == null) return;
            this.context = client.getCachedValue(CacheType.MATCH_CONTEXT);
            client.getXMPPClient().joinUnprotectedMuc(context.getPayload().getChatRoomName(), context.getPayload().getTargetRegion());
        });
    }

    @Override
    public void onMessageReceived(IncomingMessage incomingMessage) {
        String source = incomingMessage.getFrom().split("@")[0];
        if (context == null || !context.getPayload().getChatRoomName().equals(source)) return;
        String puuid = incomingMessage.getRC();
        area.append(champSelect.getHiddenName(puuid) + ": " + incomingMessage.getBody() + System.lineSeparator());
        revalidate();
    }

    @Override
    public void onMessageSent(OutgoingMessage outgoingMessage) {

    }

    @Override
    public void onFailedMessage(FailedMessage failedMessage) {

    }
}
