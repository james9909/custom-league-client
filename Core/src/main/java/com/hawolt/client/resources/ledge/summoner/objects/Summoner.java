package com.hawolt.client.resources.ledge.summoner.objects;

import org.json.JSONObject;

/**
 * Created: 19/01/2023 17:28
 * Author: Twitter @hawolt
 **/

public class Summoner {
    private final int level, expPoints, levelAndXpVersion, revisionId, expToNextLevel, profileIconId;
    private final long summonerId, accountId, revisionDate, lastGameDate;
    private final boolean nameChangeFlag, unnamed;
    private final String puuid, name, privacy;

    public Summoner(JSONObject o) {
        this.name = o.getString("name");
        this.profileIconId = o.getInt("profileIconId");
        this.level = o.getInt("level");
        this.expPoints = o.getInt("expPoints");
        this.levelAndXpVersion = o.getInt("levelAndXpVersion");
        this.revisionId = o.getInt("revisionId");
        this.revisionDate = o.getLong("revisionDate");
        this.lastGameDate = o.getLong("lastGameDate");
        this.nameChangeFlag = o.getBoolean("nameChangeFlag");
        this.unnamed = o.getBoolean("unnamed");
        this.privacy = o.getString("privacy");
        this.expToNextLevel = o.getInt("expToNextLevel");
        this.puuid = o.getString("puuid");
        this.summonerId = o.getLong("id");
        this.accountId = o.getLong("accountId");
    }

    public long getAccountId() {
        return accountId;
    }

    public long getSummonerId() {
        return summonerId;
    }

    public String getPUUID() {
        return puuid;
    }

    public int getLevel() {
        return level;
    }

    public int getExpPoints() {
        return expPoints;
    }

    public int getLevelAndXpVersion() {
        return levelAndXpVersion;
    }

    public int getRevisionId() {
        return revisionId;
    }

    public int getExpToNextLevel() {
        return expToNextLevel;
    }

    public int getProfileIconId() {
        return profileIconId;
    }

    public long getRevisionDate() {
        return revisionDate;
    }

    public long getLastGameDate() {
        return lastGameDate;
    }

    public boolean isNameChangeFlag() {
        return nameChangeFlag;
    }

    public boolean isUnnamed() {
        return unnamed;
    }

    public String getPuuid() {
        return puuid;
    }

    public String getName() {
        return name;
    }

    public String getPrivacy() {
        return privacy;
    }

    @Override
    public String toString() {
        return "Summoner{" +
                "level=" + level +
                ", expPoints=" + expPoints +
                ", levelAndXpVersion=" + levelAndXpVersion +
                ", revisionId=" + revisionId +
                ", expToNextLevel=" + expToNextLevel +
                ", profileIconId=" + profileIconId +
                ", summonerId=" + summonerId +
                ", accountId=" + accountId +
                ", revisionDate=" + revisionDate +
                ", lastGameDate=" + lastGameDate +
                ", nameChangeFlag=" + nameChangeFlag +
                ", unnamed=" + unnamed +
                ", puuid='" + puuid + '\'' +
                ", name='" + name + '\'' +
                ", privacy='" + privacy + '\'' +
                '}';
    }
}
