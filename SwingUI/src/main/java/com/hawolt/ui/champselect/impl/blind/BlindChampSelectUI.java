package com.hawolt.ui.champselect.impl.blind;

import com.hawolt.client.resources.communitydragon.spell.Spell;
import com.hawolt.ui.champselect.AbstractRenderInstance;
import com.hawolt.ui.champselect.data.ChampSelectTeam;
import com.hawolt.ui.champselect.data.ChampSelectType;
import com.hawolt.ui.champselect.generic.ChampSelectRuneSelection;
import com.hawolt.ui.champselect.generic.impl.ChampSelectSidebarUI;
import com.hawolt.ui.champselect.impl.draft.DraftSelectSidebarUI;
import com.hawolt.xmpp.event.objects.conversation.history.impl.IncomingMessage;

import java.awt.*;

/**
 * Created: 03/09/2023 14:16
 * Author: Twitter @hawolt
 **/

public class BlindChampSelectUI extends AbstractRenderInstance {
    public static BlindChampSelectUI INSTANCE = new BlindChampSelectUI();


    public BlindChampSelectUI() {
        setLayout(new BorderLayout());

        this.add(new ChampSelectSidebarUI(ChampSelectTeam.PURPLE), BorderLayout.EAST);
        this.add(new ChampSelectSidebarUI(ChampSelectTeam.BLUE), BorderLayout.WEST);
    }

    @Override
    protected void push(IncomingMessage incomingMessage) {

    }

    @Override
    protected void stopChampSelectTimer() {

    }

    @Override
    public void invokeChampionFilter(String champion) {

    }

    @Override
    public void setGlobalRunePanel(ChampSelectRuneSelection selection) {

    }

    @Override
    public int[] getSupportedQueueIds() {
        return new int[]{430, 830, 840, 850};
    }

    @Override
    public String getCardName() {
        return "blind";
    }

    @Override
    public void onSummonerSubmission(Spell selectedSpellOne, Spell selectedSpellTwo) {

    }

    @Override
    public void onChoiceSubmission(ChampSelectType type, int championId, boolean completed) {

    }
}
