package com.hawolt.ui.champselect.settings;

import com.hawolt.async.loader.ResourceConsumer;
import com.hawolt.async.loader.ResourceLoader;
import com.hawolt.client.LeagueClient;
import com.hawolt.logger.Logger;
import com.hawolt.objects.Spell;
import com.hawolt.ui.champselect.ISpellChangedListener;
import com.hawolt.util.panel.ChildUIComponent;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created: 15/08/2023 19:18
 * Author: Twitter @hawolt
 **/

public class ChampSelectSummonerSpells extends ChildUIComponent implements ActionListener, ResourceConsumer<JSONArray, byte[]> {
    private final ISpellChangedListener spellChangedListener;
    Integer queueId;
    LeagueClient leagueClient;
    Spell[] spells;
    private JComboBox<Spell> spell1, spell2;
    private List<Long> temporaryWhiteList = Arrays.asList(1L, 3L, 4L, 6L, 7L, 11L, 12L, 13L, 14L, 21L);

    public ChampSelectSummonerSpells(ISpellChangedListener spellChangedListener, LeagueClient client) {
        super(new GridLayout(0, 2, 5, 0));
        this.leagueClient = client;
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.spellChangedListener = spellChangedListener;
        ResourceLoader.loadResource("https://raw.communitydragon.org/pbe/plugins/rcp-be-lol-game-data/global/default/v1/summoner-spells.json", this);
    }

    public Spell getSelectedSpell1() {
        return spell1.getItemAt(spell1.getSelectedIndex());
    }

    public Spell getSelectedSpell2() {
        return spell2.getItemAt(spell2.getSelectedIndex());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            //TODO revisit
           /* PlayerPreferencesLedge playerPreferencesLedge = leagueClient.getLedge().getPlayerPreferences();
            JSONObject playerPrefs = playerPreferencesLedge.getPlayerPreferences();
            JSONObject champSelect = playerPrefs.getJSONObject("champ-select");
            JSONObject data = champSelect.getJSONObject("data");
            JSONObject spells = data.getJSONObject("spells");
            JSONArray queue;
            try {
                queue = spells.getJSONArray(this.queueId.toString());
                queue.remove(0);
                queue.remove(0);
            } catch (Exception e2) {
                queue = new JSONArray();
                spells.put(this.queueId.toString(), queue);
                Logger.error(e2);
            }
            queue.put(getSelectedSpell1().getId());
            queue.put(getSelectedSpell2().getId());
            playerPreferencesLedge.setPlayerPreferences(playerPrefs.toString());
            PlayerPreferencesService.get().getSettings().setChampSelect(champSelect);
            try {
                PlayerPreferencesService.get().writeSettingsFile();
            } catch (IOException i) {
                Logger.error(i);
            }*/
            spellChangedListener.onSpellSelection((int) getSelectedSpell1().getId(), (int) getSelectedSpell2().getId());
        } catch (Exception ex) {
            Logger.error(ex);
        }
    }

    /*
    public void joinCS(int id) {
        this.queueId = id;
        Logger.error(this.queueId);
        try {
            JSONObject champSelect = PlayerPreferencesService.get().getSettings().getChampSelect();
            JSONObject data = champSelect.getJSONObject("data");
            JSONObject jsonSpells = data.getJSONObject("spells");
            try {
                jsonSpells.getJSONArray(this.queueId.toString());
            } catch (Exception e) {
                JSONArray queueArray = new JSONArray();
                queueArray.put(6);
                queueArray.put(7);
                jsonSpells.put(this.queueId.toString(), queueArray);
            } finally {

                JSONArray queueSpells = jsonSpells.getJSONArray(this.queueId.toString());

                int primary = queueSpells.getInt(0);
                int secondary = queueSpells.getInt(1);
                Spell primarySpell = spells[0];
                Spell secondarySpell = spells[1];
                for (int i = 0; i < spells.length; i++) {
                    if (spells[i].getId() == primary) {
                        primarySpell = spells[i];
                    } else if (spells[i].getId() == secondary) {
                        secondarySpell = spells[i];
                    }
                }

                spell1.setSelectedItem(primarySpell);
                spell2.setSelectedItem(secondarySpell);
            }
        } catch (Exception e) {
            Logger.error(e.toString());
        }
    }*/

    @Override
    public void onException(Object o, Exception e) {

    }

    @Override
    public void consume(Object o, JSONArray array) {
        List<Spell> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject reference = array.getJSONObject(i);
            Spell spell = new Spell(reference);
            if (!temporaryWhiteList.contains(spell.getId())) continue;
            list.add(spell);
        }
        spells = list.toArray(Spell[]::new);
        this.add(spell1 = new JComboBox<>(spells));
        this.spell1.addActionListener(this);
        this.add(spell2 = new JComboBox<>(spells));
        this.spell2.addActionListener(this);
    }

    @Override
    public JSONArray transform(byte[] bytes) throws Exception {
        return new JSONArray(new String(bytes));
    }
}
