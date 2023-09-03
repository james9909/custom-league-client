package com.hawolt.ui.champselect.impl.blank;

import com.hawolt.client.resources.communitydragon.spell.Spell;
import com.hawolt.ui.champselect.AbstractRenderInstance;
import com.hawolt.ui.champselect.data.ChampSelectType;
import com.hawolt.ui.champselect.generic.ChampSelectRuneSelection;
import com.hawolt.xmpp.event.objects.conversation.history.impl.IncomingMessage;

/**
 * Created: 02/09/2023 16:53
 * Author: Twitter @hawolt
 **/

public class BlankChampSelectUI extends AbstractRenderInstance {
    public static final BlankChampSelectUI INSTANCE = new BlankChampSelectUI();

    @Override
    public String getCardName() {
        return "blank";
    }

    @Override
    public int[] getSupportedQueueIds() {
        return new int[0];
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
    public void onSummonerSubmission(Spell selectedSpellOne, Spell selectedSpellTwo) {

    }

    @Override
    public void onChoiceSubmission(ChampSelectType type, int championId, boolean completed) {

    }
}
