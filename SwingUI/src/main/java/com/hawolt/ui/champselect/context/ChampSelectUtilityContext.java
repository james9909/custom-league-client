package com.hawolt.ui.champselect.context;

import com.hawolt.ui.champselect.data.ActionObject;
import com.hawolt.ui.champselect.data.ChampSelectMember;
import com.hawolt.ui.champselect.data.ChampSelectTeamMember;

import java.util.List;
import java.util.Optional;

/**
 * Created: 10/09/2023 03:11
 * Author: Twitter @hawolt
 **/

public interface ChampSelectUtilityContext {

    List<ActionObject> getCurrent();

    boolean isTeamMember(ChampSelectMember member);

    boolean isLockedIn(ChampSelectMember member);

    boolean isPicking(ChampSelectMember member);

    boolean isSelf(ChampSelectMember member);

    boolean isFinalizing();

    Optional<ActionObject> getOwnPickPhase();

    Optional<ActionObject> getOwnBanPhase();

    ChampSelectTeamMember getSelf();

    void quitChampSelect();
}
