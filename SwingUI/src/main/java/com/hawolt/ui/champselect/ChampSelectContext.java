package com.hawolt.ui.champselect;

import com.hawolt.LeagueClientUI;
import com.hawolt.client.LeagueClient;
import com.hawolt.rtmp.utility.PacketCallback;
import com.hawolt.ui.champselect.data.ChampSelectTeamType;
import com.hawolt.ui.champselect.generic.ChampSelectRuneSelection;
import com.hawolt.ui.champselect.util.ActionObject;
import com.hawolt.ui.champselect.util.ChampSelectTeamMember;
import org.json.JSONArray;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Created: 29/08/2023 17:31
 * Author: Twitter @hawolt
 **/

public interface ChampSelectContext {
    long getGameId();

    int getQueueId();

    int getRecoveryCounter();

    String getContextId();

    int getCounter();

    String getPhaseName();

    boolean isAllowDuplicatePicks();

    boolean isSkipChampionSelect();

    boolean isAllowSkinSelection();

    boolean isAllowOptingOutOfBanning();

    long getCurrentTotalTimeMillis();

    long getCurrentTimeRemainingMillis();

    int getLocalPlayerCellId();

    int getCurrentActionSetIndex();

    String getTeamId();

    String getSubphase();

    String getTeamChatRoomId();

    Map<Integer, List<ActionObject>> getActionSetMapping();

    Optional<ActionObject> getOwnBanPhase();

    Optional<ActionObject> getOwnPickPhase();

    List<ActionObject> getBanSelection(ChampSelectTeamType type);

    int[] getChampionsAvailableForBan();

    int[] getChampionsAvailableForPick();

    ChampSelectTeamMember getSelf();

    Set<String> getCells();

    LeagueClient getLeagueClient();

    LeagueClientUI getLeagueClientUI();

    JSONArray getCells(ChampSelectTeamType type);

    void cache(String puuid, String name);

    Map<String, String> getPUUIDResolver();

    boolean isFinalizing();

    List<ActionObject> getCurrent();

    <T> T getCells(ChampSelectTeamType type, Function<JSONArray, T> function);

    void filterChampion(String champion);

    PacketCallback getPacketCallback();

    void quitChampSelect();

    ChampSelectRuneSelection getRuneSelectionPanel();
}
