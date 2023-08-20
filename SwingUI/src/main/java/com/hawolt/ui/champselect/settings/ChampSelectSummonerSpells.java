package com.hawolt.ui.champselect.settings;

import com.hawolt.async.loader.ResourceConsumer;
import com.hawolt.async.loader.ResourceLoader;
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
    private JComboBox<Spell> spell1, spell2;

    public ChampSelectSummonerSpells(ISpellChangedListener spellChangedListener) {
        super(new GridLayout(0, 2, 5, 0));
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.spellChangedListener = spellChangedListener;
        ResourceLoader.load("https://raw.communitydragon.org/pbe/plugins/rcp-be-lol-game-data/global/default/v1/summoner-spells.json", this);
    }

    public Spell getSelectedSpell1() {
        return spell1.getItemAt(spell1.getSelectedIndex());
    }

    public Spell getSelectedSpell2() {
        return spell2.getItemAt(spell2.getSelectedIndex());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        spellChangedListener.onSpellSelection((int) getSelectedSpell1().getId(), (int) getSelectedSpell2().getId());
    }

    @Override
    public void onException(Object o, Exception e) {

    }

    private List<Long> temporaryWhiteList = Arrays.asList(1L, 3L, 4L, 6L, 7L, 11L, 12L, 13L, 14L, 21L);

    @Override
    public void consume(Object o, JSONArray array) {
        List<Spell> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject reference = array.getJSONObject(i);
            Spell spell = new Spell(reference);
            if (!temporaryWhiteList.contains(spell.getId())) continue;
            list.add(spell);
        }
        Spell[] spells = list.toArray(Spell[]::new);
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
