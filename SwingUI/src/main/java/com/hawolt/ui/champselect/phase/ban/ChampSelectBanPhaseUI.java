package com.hawolt.ui.champselect.phase.ban;

import com.hawolt.ui.champselect.ChampSelectPhase;
import com.hawolt.ui.champselect.IChampSelection;
import com.hawolt.ui.champselect.phase.ChampSelectSelectionUI;
import com.hawolt.util.panel.ChildUIComponent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * Created: 06/08/2023 18:59
 * Author: Twitter @hawolt
 **/

public class ChampSelectBanPhaseUI extends ChildUIComponent {

    private final ChampSelectSelectionUI selectionUI;
    private final JScrollPane scrollPane;
    private final JButton button;

    public ChampSelectBanPhaseUI(IChampSelection selection) {
        super(new BorderLayout(0, 5));
        this.setBorder(new EmptyBorder(0, 0, 5, 0));
        this.add(scrollPane = new JScrollPane(), BorderLayout.CENTER);
        this.add(button = new JButton("BAN"), BorderLayout.SOUTH);
        this.button.setActionCommand("BAN");
        this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.scrollPane.getViewport().add(selectionUI = new ChampSelectSelectionUI(ChampSelectPhase.BAN, selection));
        this.scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.DARK_GRAY;
            }
        });
        this.scrollPane.getVerticalScrollBar().setBackground(Color.BLACK);
        this.scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        this.scrollPane.setBorder(null);
        this.setBackground(Color.BLACK);
    }

    public JButton getButton() {
        return button;
    }

    public ChampSelectSelectionUI getSelectionUI() {
        return selectionUI;
    }
}
