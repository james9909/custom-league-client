package com.hawolt.ui.champselect.sidebar;

import com.hawolt.ui.champselect.AlliedMember;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created: 06/08/2023 14:03
 * Author: Twitter @hawolt
 **/

public class ChampSelectSidebarUI extends JPanel {

    private ChampSelectBlankMemberUI[] memberUIs;
    private int teamId, localPlayerCellId;
    private boolean self;

    public ChampSelectSidebarUI() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(300, 0));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new GridLayout(5, 0, 5, 5));
        reset();
    }

    public void reset() {
        this.removeAll();
        this.memberUIs = new ChampSelectBlankMemberUI[5];
        for (int i = 0; i < memberUIs.length; i++) {
            this.memberUIs[i] = new ChampSelectBlankMemberUI();
            this.add(memberUIs[i]);
        }
    }

    public void rebuild(JSONArray team, int localPlayerCellId) {
        this.removeAll();
        this.localPlayerCellId = localPlayerCellId;
        this.memberUIs = new ChampSelectMemberUI[team.length()];
        for (int i = 0; i < team.length(); i++) {
            JSONObject member = team.getJSONObject(i);
            this.memberUIs[i] = new ChampSelectMemberUI(member);
            this.add(memberUIs[i]);
            if (i == 0) {
                this.teamId = member.getInt("teamId");
                this.self = member.has("puuid");
            }
        }
    }

    public void update(int currentActionSetIndex, int index, JSONArray phase) {
        if (memberUIs == null) return;
        for (int i = 0; i < phase.length(); i++) {
            JSONObject member = phase.getJSONObject(i);
            boolean completed = member.getBoolean("completed");
            int championId = member.getInt("championId");
            int actorCellId = member.getInt("actorCellId");
            int normalizedActorCellId = actorCellId % 5;
            ChampSelectBlankMemberUI blankMemberUI = memberUIs[normalizedActorCellId];
            if (blankMemberUI instanceof ChampSelectMemberUI) {
                ChampSelectMemberUI memberUI = (ChampSelectMemberUI) blankMemberUI;
                if (memberUI.getCellId() == actorCellId) memberUI.update(championId, completed);
                boolean teamOne = (currentActionSetIndex % 2) != 0;
                boolean selfUserTeamOne = localPlayerCellId < 5;
                boolean selfUser = localPlayerCellId == actorCellId;
                boolean selfDisplay = selfUser && ((teamId == 1 && selfUserTeamOne) || (teamId == 2 && !selfUserTeamOne));
                if (selfDisplay) memberUI.setBackground(Color.DARK_GRAY.brighter());
                if (currentActionSetIndex <= 0) break;
                if (teamOne && actorCellId >= 5 || !teamOne && actorCellId < 5) {
                    if ((index > currentActionSetIndex)) continue;
                    memberUI.setBackground(selfDisplay ? Color.DARK_GRAY.brighter() : Color.DARK_GRAY);
                }
                if (teamId == 1 && teamOne || teamId == 2 && !teamOne) {
                    if (currentActionSetIndex == index) {
                        memberUI.setBackground(selfDisplay ? Color.LIGHT_GRAY.darker() : Color.LIGHT_GRAY);
                    } else if (currentActionSetIndex < index) {
                        memberUI.setBackground(selfDisplay ? Color.DARK_GRAY.brighter() : Color.DARK_GRAY.darker());
                    } else {
                        memberUI.setBackground(selfDisplay ? Color.DARK_GRAY.brighter() : Color.DARK_GRAY);
                    }

                }
            }
        }
        this.revalidate();
    }

    public void update(AlliedMember member) {
        ((ChampSelectMemberUI) memberUIs[member.getCellId() % 5]).updateAlliedMember(member);
    }

    public String find(String puuid) {
        if (memberUIs == null) return null;
        for (ChampSelectBlankMemberUI blankMemberUI : memberUIs) {
            if (!(blankMemberUI instanceof ChampSelectMemberUI memberUI)) continue;
            AlliedMember member = memberUI.getMember();
            if (member == null) continue;
            if (puuid.equals(member.getPUUID())) return member.getHiddenName();
        }
        return null;
    }
}
