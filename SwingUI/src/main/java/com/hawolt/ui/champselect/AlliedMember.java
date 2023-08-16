package com.hawolt.ui.champselect;

import org.json.JSONObject;

/**
 * Created: 06/08/2023 22:15
 * Author: Twitter @hawolt
 **/

public class AlliedMember {
    private final int skinId, championPickIntent, spell1Id, spell2Id, cellId;
    private final String position, puuid, nameVisibilityType;

    public AlliedMember(JSONObject member) {
        nameVisibilityType = member.getString("nameVisibilityType");
        position = member.getString("assignedPosition");
        puuid = member.getString("puuid");
        championPickIntent = member.getInt("championPickIntent");
        skinId = member.getInt("skinId");
        spell1Id = member.getInt("spell1Id");
        spell2Id = member.getInt("spell2Id");
        cellId = member.getInt("cellId");
    }

    public int getSkinId() {
        return skinId;
    }

    public String getHiddenName() {
        switch (cellId % 5) {
            case 0:
                return "Raptor";
            case 1:
                return "Krug";
            case 2:
                return "Murk Wolf";
            case 3:
                return "Gromp";
            default:
                return "Scuttle Crab";
        }
    }

    public int getChampionPickIntent() {
        return championPickIntent;
    }

    public int getSpell1Id() {
        return spell1Id;
    }

    public int getSpell2Id() {
        return spell2Id;
    }

    public int getCellId() {
        return cellId;
    }

    public String getPosition() {
        return position;
    }

    public String getPUUID() {
        return puuid;
    }

    public String getNameVisibilityType() {
        return nameVisibilityType;
    }
}
