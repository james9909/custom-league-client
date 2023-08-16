package com.hawolt.client.resources.platform.history.object;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: 14/01/2023 23:04
 * Author: Twitter @hawolt
 **/

public class MatchOutcomeJSON {
    private final long gameCreation, gameDuration, gameEndTimestamp, gameId, gameStartTimestamp;
    private final String gameMode, gameName, gameType, gameVersion, platformId, tournamentCode;
    private final List<MatchOutcomeParticipant> participants = new ArrayList<>();
    private final int mapId, queueId, seasonId;


    public MatchOutcomeJSON(JSONObject o) {
        this.gameCreation = o.getLong("gameCreation");
        this.gameDuration = o.getLong("gameDuration");
        this.gameEndTimestamp = o.getLong("gameEndTimestamp");
        this.gameId = o.getLong("gameId");
        this.gameMode = o.getString("gameMode");
        this.gameName = o.getString("gameName");
        this.gameStartTimestamp = o.getLong("gameStartTimestamp");
        this.gameType = o.getString("gameType");
        this.gameVersion = o.getString("gameVersion");
        this.mapId = o.getInt("mapId");
        this.platformId = o.getString("platformId");
        this.queueId = o.getInt("queueId");
        this.seasonId = o.getInt("seasonId");
        this.tournamentCode = o.getString("tournamentCode");
        JSONArray participants = o.getJSONArray("participants");
        for (int i = 0; i < participants.length(); i++) {
            JSONObject object = participants.getJSONObject(i);
            MatchOutcomeParticipant participant = new MatchOutcomeParticipant(object);
            this.participants.add(participant);
        }
    }

    public List<MatchOutcomeParticipant> getParticipants() {
        return participants;
    }

    public long getGameCreation() {
        return gameCreation;
    }

    public long getGameDuration() {
        return gameDuration;
    }

    public long getGameEndTimestamp() {
        return gameEndTimestamp;
    }

    public long getGameId() {
        return gameId;
    }

    public long getGameStartTimestamp() {
        return gameStartTimestamp;
    }

    public String getGameMode() {
        return gameMode;
    }

    public String getGameName() {
        return gameName;
    }

    public String getGameType() {
        return gameType;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public String getPlatformId() {
        return platformId;
    }

    public String getTournamentCode() {
        return tournamentCode;
    }

    public int getMapId() {
        return mapId;
    }

    public int getQueueId() {
        return queueId;
    }

    public int getSeasonId() {
        return seasonId;
    }

    @Override
    public String toString() {
        return "MatchOutcomeJSON{" +
                "gameCreation=" + gameCreation +
                ", gameDuration=" + gameDuration +
                ", gameEndTimestamp=" + gameEndTimestamp +
                ", gameId=" + gameId +
                ", gameStartTimestamp=" + gameStartTimestamp +
                ", gameMode='" + gameMode + '\'' +
                ", gameName='" + gameName + '\'' +
                ", gameType='" + gameType + '\'' +
                ", gameVersion='" + gameVersion + '\'' +
                ", platformId='" + platformId + '\'' +
                ", tournamentCode='" + tournamentCode + '\'' +
                ", participants=" + participants +
                ", mapId=" + mapId +
                ", queueId=" + queueId +
                ", seasonId=" + seasonId +
                '}';
    }
}
