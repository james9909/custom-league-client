package com.hawolt.ui.champselect.postgame;

import com.hawolt.LeagueClientUI;
import com.hawolt.client.resources.ledge.leagues.objects.LeagueNotification;
import com.hawolt.http.layer.IResponse;
import com.hawolt.ui.layout.LayoutComponent;
import com.hawolt.util.panel.ChildUIComponent;
import com.hawolt.util.ui.LFlatButton;
import com.hawolt.util.ui.LHighlightType;
import com.hawolt.util.ui.LTextAlign;
import org.json.JSONObject;

import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created: 11/09/2023 20:58
 * Author: Twitter @hawolt
 **/

public class PostGameUI extends ChildUIComponent implements ActionListener {
    private final LeagueClientUI leagueClientUI;
    private final LFlatButton close;

    public PostGameUI(LeagueClientUI leagueClientUI) {
        super(new BorderLayout());
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.add(close = new LFlatButton("Play Again", LTextAlign.CENTER, LHighlightType.COMPONENT), BorderLayout.SOUTH);
        this.close.addActionListener(this);
        this.leagueClientUI = leagueClientUI;
    }

    public void build(IResponse response, List<LeagueNotification> notifications) {
        build(response.asString(), notifications);
    }

    public void build(String response, List<LeagueNotification> notifications) {
        JSONObject data = new JSONObject(response);
        add(new PostGameHeader(data, notifications), BorderLayout.NORTH);
        add(new PostGameScoreboard(data), BorderLayout.CENTER);
        /*JOIN POST GAME CHAT
        String xmppRoomName = data.getString("roomName");
        String xmppRoomPassword = data.getString("roomPassword");
        PartyMucJwtDto mucJwtDto = new PartyMucJwtDto(data.getJSONObject("mucJwtDto"));
        client.getXMPPClient().joinProtectedMuc(xmppRoomName, mucJwtDto.getDomain(), xmppRoomPassword);
        */
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.leagueClientUI.getHeader().selectAndShowComponent(LayoutComponent.PLAY);
    }
}
