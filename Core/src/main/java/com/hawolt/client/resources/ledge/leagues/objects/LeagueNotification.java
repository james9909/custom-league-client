package com.hawolt.client.resources.ledge.leagues.objects;

import org.json.JSONObject;

/**
 * Created: 05/09/2023 20:36
 * Author: Twitter @hawolt
 **/

public class LeagueNotification {
    private final int provisionalGamesRemaining, leaguePoints, leaguePointsDelta, splitPoints, ratedRating, ratedRatingDelta, consolationLpUsed, afkLpPenaltyAmount, afkLpPenaltyLevel, winStreak, wins, losses;
    private final boolean eligibleForPromoHelper, promoSeriesForRanksEnabled, wasAfkOrLeaver, canDemoteFromTier;
    private final String notifyReason, changeReason, queueType, tier, rank, ratedTier;
    private final long gameId;

    public LeagueNotification(JSONObject o) {
        this.gameId = o.getLong("gameId");
        this.provisionalGamesRemaining = o.getInt("provisionalGamesRemaining");
        this.leaguePoints = o.getInt("leaguePoints");
        this.leaguePointsDelta = o.getInt("leaguePointsDelta");
        this.splitPoints = o.getInt("splitPoints");
        this.ratedRating = o.getInt("ratedRating");
        this.ratedRatingDelta = o.getInt("ratedRatingDelta");
        this.consolationLpUsed = o.getInt("consolationLpUsed");
        this.afkLpPenaltyAmount = o.getInt("afkLpPenaltyAmount");
        this.afkLpPenaltyLevel = o.getInt("afkLpPenaltyLevel");
        this.winStreak = o.getInt("winStreak");
        this.wins = o.getInt("wins");
        this.losses = o.getInt("losses");
        this.notifyReason = o.getString("notifyReason");
        this.changeReason = o.getString("changeReason");
        this.queueType = o.getString("queueType");
        this.tier = o.getString("tier");
        this.rank = o.getString("rank");
        this.ratedTier = o.getString("ratedTier");
        this.eligibleForPromoHelper = o.getBoolean("eligibleForPromoHelper");
        this.promoSeriesForRanksEnabled = o.getBoolean("promoSeriesForRanksEnabled");
        this.wasAfkOrLeaver = o.getBoolean("wasAfkOrLeaver");
        this.canDemoteFromTier = o.getBoolean("canDemoteFromTier");
    }

    public int getProvisionalGamesRemaining() {
        return provisionalGamesRemaining;
    }

    public int getLeaguePoints() {
        return leaguePoints;
    }

    public int getLeaguePointsDelta() {
        return leaguePointsDelta;
    }

    public int getSplitPoints() {
        return splitPoints;
    }

    public int getRatedRating() {
        return ratedRating;
    }

    public int getRatedRatingDelta() {
        return ratedRatingDelta;
    }

    public int getConsolationLpUsed() {
        return consolationLpUsed;
    }

    public int getAfkLpPenaltyAmount() {
        return afkLpPenaltyAmount;
    }

    public int getAfkLpPenaltyLevel() {
        return afkLpPenaltyLevel;
    }

    public int getWinStreak() {
        return winStreak;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public boolean isEligibleForPromoHelper() {
        return eligibleForPromoHelper;
    }

    public boolean isPromoSeriesForRanksEnabled() {
        return promoSeriesForRanksEnabled;
    }

    public boolean isWasAfkOrLeaver() {
        return wasAfkOrLeaver;
    }

    public boolean isCanDemoteFromTier() {
        return canDemoteFromTier;
    }

    public String getNotifyReason() {
        return notifyReason;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public String getQueueType() {
        return queueType;
    }

    public String getTier() {
        return tier;
    }

    public String getRank() {
        return rank;
    }

    public String getRatedTier() {
        return ratedTier;
    }

    public long getGameId() {
        return gameId;
    }
}
