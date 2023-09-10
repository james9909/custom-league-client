package com.hawolt.ui.champselect.impl.aram;

import com.hawolt.client.resources.communitydragon.spell.Spell;
import com.hawolt.ui.champselect.AbstractRenderInstance;
import com.hawolt.ui.champselect.data.ChampSelectType;
import com.hawolt.ui.champselect.generic.ChampSelectRuneSelection;
import com.hawolt.xmpp.event.objects.conversation.history.impl.IncomingMessage;
import com.hawolt.xmpp.event.objects.presence.impl.JoinMucPresence;

/**
 * Created: 03/09/2023 14:16
 * Author: Twitter @hawolt
 **/

public class ARAMChampSelectUI extends AbstractRenderInstance {
    @Override
    public void push(IncomingMessage incomingMessage) {

    }

    @Override
    public void push(JoinMucPresence presence) {

    }

    @Override
    protected void stopChampSelect() {

    }

    @Override
    public void invokeChampionFilter(String champion) {

    }

    @Override
    public void setGlobalRunePanel(ChampSelectRuneSelection selection) {

    }

    @Override
    public int[] getSupportedQueueIds() {
        return new int[]{450};
    }

    @Override
    public String getCardName() {
        return "aram";
    }

    @Override
    public void onSummonerSubmission(Spell selectedSpellOne, Spell selectedSpellTwo) {

    }

    @Override
    public void onChoiceSubmission(ChampSelectType type, int championId, boolean completed) {

    }
}
