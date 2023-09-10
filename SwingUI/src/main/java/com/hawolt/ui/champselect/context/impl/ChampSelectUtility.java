package com.hawolt.ui.champselect.context.impl;

import com.hawolt.ui.champselect.ChampSelectUI;
import com.hawolt.ui.champselect.context.ChampSelectContext;
import com.hawolt.ui.champselect.context.ChampSelectContextProvider;
import com.hawolt.ui.champselect.context.ChampSelectSettingsContext;
import com.hawolt.ui.champselect.context.ChampSelectUtilityContext;
import com.hawolt.ui.champselect.data.*;

import java.util.*;

/**
 * Created: 10/09/2023 03:29
 * Author: Twitter @hawolt
 **/

public class ChampSelectUtility extends ChampSelectContextProvider implements ChampSelectUtilityContext {
    public ChampSelectUtility(ChampSelectUI champSelectUI, ChampSelectContext context) {
        super(champSelectUI, context);
    }

    private Map<Integer, List<ActionObject>> getActionSetMapping() {
        return context.getChampSelectSettingsContext().getActionSetMapping();
    }

    private int getCurrentActionSetIndex() {
        return context.getChampSelectSettingsContext().getCurrentActionSetIndex();
    }

    private int getLocalPlayerCellId() {
        return context.getChampSelectSettingsContext().getLocalPlayerCellId();
    }

    @Override
    public boolean isFinalizing() {
        return context.getChampSelectSettingsContext().getCurrentActionSetIndex() >= getActionSetMapping().size();
    }

    @Override
    public boolean isSelf(ChampSelectMember member) {
        return member.getCellId() == context.getChampSelectSettingsContext().getLocalPlayerCellId();
    }

    @Override
    public boolean isTeamMember(ChampSelectMember member) {
        return getSelf().getTeamId() == member.getTeamId();
    }

    @Override
    public boolean isLockedIn(ChampSelectMember member) {
        if (context.getChampSelectSettingsContext().getCurrentActionSetIndex() < 0) return false;
        if (isFinalizing()) return true;
        return getActionSetMapping().values()
                .stream()
                .flatMap(Collection::stream)
                .anyMatch(actionObject -> "PICK".equals(actionObject.getType()) && actionObject.getActorCellId() == member.getCellId() && actionObject.isCompleted());
    }

    @Override
    public boolean isPicking(ChampSelectMember member) {
        if (isFinalizing() || getCurrentActionSetIndex() < 0) return false;
        return getActionSetMapping().get(getCurrentActionSetIndex())
                .stream()
                .anyMatch(actionObject -> actionObject.getActorCellId() == member.getCellId() && !actionObject.isCompleted());
    }

    @Override
    public List<ActionObject> getCurrent() {
        if (getCurrentActionSetIndex() < 0) return new ArrayList<>();
        return getActionSetMapping().get(getCurrentActionSetIndex());
    }

    @Override
    public Optional<ActionObject> getOwnBanPhase() {
        return getActionSetMapping().get(0)
                .stream()
                .filter(object -> "BAN".equals(object.getType()) && object.getActorCellId() == getLocalPlayerCellId())
                .findFirst();
    }

    @Override
    public Optional<ActionObject> getOwnPickPhase() {
        return getActionSetMapping().values()
                .stream()
                .flatMap(Collection::stream)
                .filter(object -> "PICK".equals(object.getType()) && object.getActorCellId() == getLocalPlayerCellId())
                .findFirst();
    }

    @Override
    public ChampSelectTeamMember getSelf() {
        ChampSelectSettingsContext settingsContext = context.getChampSelectSettingsContext();
        ChampSelectMember[] members = settingsContext.getCells(ChampSelectTeamType.ALLIED, TeamMemberFunction.INSTANCE);
        for (ChampSelectMember member : members) {
            if (member.getCellId() == getLocalPlayerCellId()) {
                return (ChampSelectTeamMember) member;
            }
        }
        return null;
    }

    @Override
    public void quitChampSelect() {
        this.champSelectUI.showBlankPanel();
    }

}
