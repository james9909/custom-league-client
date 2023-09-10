package com.hawolt.ui.champselect.impl.draft;

import com.hawolt.LeagueClientUI;
import com.hawolt.client.LeagueClient;
import com.hawolt.client.cache.CacheType;
import com.hawolt.client.resources.communitydragon.spell.Spell;
import com.hawolt.client.resources.ledge.teambuilder.objects.MatchContext;
import com.hawolt.logger.Logger;
import com.hawolt.rtmp.LeagueRtmpClient;
import com.hawolt.rtmp.amf.TypedObject;
import com.hawolt.rtmp.io.RtmpPacket;
import com.hawolt.rtmp.service.impl.TeamBuilderService;
import com.hawolt.rtmp.utility.PacketCallback;
import com.hawolt.ui.champselect.AbstractRenderInstance;
import com.hawolt.ui.champselect.data.ChampSelectTeam;
import com.hawolt.ui.champselect.data.ChampSelectType;
import com.hawolt.ui.champselect.data.GameType;
import com.hawolt.ui.champselect.generic.ChampSelectRuneSelection;
import com.hawolt.ui.champselect.generic.impl.*;
import com.hawolt.ui.champselect.data.ActionObject;
import com.hawolt.ui.champselect.impl.MatchmadeRenderInstance;
import com.hawolt.ui.champselect.impl.blind.BlindChampSelectUI;
import com.hawolt.util.panel.ChildUIComponent;
import com.hawolt.xmpp.core.VirtualRiotXMPPClient;
import com.hawolt.xmpp.event.objects.conversation.history.impl.IncomingMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

/**
 * Created: 29/08/2023 17:04
 * Author: Twitter @hawolt
 **/

public class DraftChampSelectUI extends MatchmadeRenderInstance implements ActionListener {

    public static DraftChampSelectUI INSTANCE = new DraftChampSelectUI(ChampSelectType.values());
    private final DraftChampSelectHeaderUI headerUI;

    public DraftChampSelectUI(ChampSelectType... supportedTypes) {
        super(supportedTypes);
        this.centerUI.getMain().add(headerUI = new DraftChampSelectHeaderUI(), BorderLayout.NORTH);
    }

    @Override
    protected ChampSelectCenterUI getCenterUI(AbstractRenderInstance instance, ChampSelectType... supportedTypes) {
        return new DraftCenterUI(instance, supportedTypes);
    }

    @Override
    protected ChampSelectSidebarUI getSidebarUI(ChampSelectTeam team) {
        return new DraftSelectSidebarUI(team);
    }

    @Override
    protected Integer[] getAllowedSummonerSpells() {
        return new Integer[]{1, 3, 4, 6, 7, 11, 12, 13, 14, 21};
    }

    @Override
    protected void stopChampSelect() {
        this.headerUI.reset();
    }

    @Override
    public int[] getSupportedQueueIds() {
        return new int[]{400, 420, 440};
    }

    @Override
    public String getCardName() {
        return "draft";
    }
}
