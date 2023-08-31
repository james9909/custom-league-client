package com.hawolt.ui.champselect.chat;

import com.hawolt.LeagueClientUI;
import com.hawolt.client.LeagueClient;
import com.hawolt.client.cache.CacheType;
import com.hawolt.client.resources.ledge.teambuilder.objects.MatchContext;
import com.hawolt.ui.champselect.ChampSelect;
import com.hawolt.ui.chat.window.ChatUI;
import com.hawolt.ui.impl.JHintTextField;
import com.hawolt.util.panel.ChildUIComponent;
import com.hawolt.util.ui.SmartScroller;
import com.hawolt.xmpp.event.objects.conversation.history.impl.IncomingMessage;

import javax.swing.*;
import java.awt.*;

/**
 * Created: 14/08/2023 20:04
 * Author: Twitter @hawolt
 **/

public class ChampSelectChatUI extends ChildUIComponent {

    private final ChampSelect champSelect;
    private final JHintTextField input;
    private final JTextArea area;
    private MatchContext context;
    private LeagueClient client;
    private ChatUI chatUI;


    public ChampSelectChatUI(ChampSelect champSelect, ChatUI chat) {
        super(new BorderLayout());
        this.chatUI = chat;
        this.champSelect = champSelect;
        if (champSelect != null) {
            this.client = champSelect.getLeagueClient();
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
                this.client.getXMPPClient().sendGroupMessage(jid, input.getText(), null);
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
            if (this.client == null) {
                return;
            }
            this.context = this.client.getCachedValue(CacheType.MATCH_CONTEXT);
            this.client.getXMPPClient().joinUnprotectedMuc(context.getPayload().getChatRoomName(), context.getPayload().getTargetRegion());
            this.chatUI.setCSChatUI(this);
        });
    }

    public void onMessageReceived(IncomingMessage incomingMessage) {
        String source = incomingMessage.getFrom().split("@")[0];
        if (context == null || !context.getPayload().getChatRoomName().equals(source)) {
            return;
        }
        String puuid = incomingMessage.getRC();
        area.append(champSelect.getHiddenName(puuid) + ": " + incomingMessage.getBody() + System.lineSeparator());
        revalidate();
    }

}
