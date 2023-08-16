package com.hawolt.ui.champselect.settings;

import com.hawolt.objects.Spell;
import com.hawolt.async.loader.impl.SpellLoader;
import com.hawolt.ui.champselect.ISpellChangedListener;
import com.hawolt.util.panel.ChildUIComponent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created: 15/08/2023 19:18
 * Author: Twitter @hawolt
 **/

public class ChampSelectSummonerSpells extends ChildUIComponent implements ActionListener {
    private final static Spell[] spells = SpellLoader.instance.getCache().values().toArray(new Spell[0]);
    private final ISpellChangedListener spellChangedListener;
    private final JComboBox<Spell> spell1, spell2;

    public ChampSelectSummonerSpells(ISpellChangedListener spellChangedListener) {
        super(new GridLayout(0, 2, 5, 0));
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.spellChangedListener = spellChangedListener;
        this.add(spell1 = new JComboBox<>(spells));
        this.spell1.addActionListener(this);
        this.add(spell2 = new JComboBox<>(spells));
        this.spell2.addActionListener(this);
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
}
