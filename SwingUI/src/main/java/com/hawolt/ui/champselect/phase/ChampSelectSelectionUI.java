package com.hawolt.ui.champselect.phase;

import com.hawolt.LeagueClientUI;
import com.hawolt.ui.champselect.ChampSelectPhase;
import com.hawolt.ui.champselect.IChampSelection;
import com.hawolt.util.panel.ChildUIComponent;
import org.json.JSONArray;

import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;

/**
 * Created: 06/08/2023 19:13
 * Author: Twitter @hawolt
 **/

public class ChampSelectSelectionUI extends ChildUIComponent implements ChampSelectSelectionCallback {
    private ChampSelectSelectionComponent[] components;
    private final IChampSelection selection;
    private int selectedComponentId = -1;
    private final ChampSelectPhase phase;
    private long selectedChampionId;

    public ChampSelectSelectionUI(ChampSelectPhase phase, IChampSelection selection) {
        super(new GridLayout(0, 8, 5, 5));
        this.setBorder(new EmptyBorder(5, 5, 0, 5));
        this.setBackground(Color.BLACK);
        this.selection = selection;
        this.phase = phase;
    }

    public void update(IChampSelection selection, JSONArray array) {
        Integer[] arr = new Integer[array.length()];
        for (int i = 0; i < array.length(); i++) {
            arr[i] = array.getInt(i);
        }
        update(selection, arr);
    }

    public void update(IChampSelection selection, Integer[] ids) {
        LeagueClientUI.service.execute(() -> {
            this.components = new ChampSelectSelectionComponent[ids.length];
            Arrays.sort(ids, (id1, id2) -> {
                String name1 = selection.getChampionCache().get(id1).getName();
                String name2 = selection.getChampionCache().get(id2).getName();
                return name1.compareTo(name2);
            });
            this.removeAll();
            for (int i = 0; i < components.length; i++) {
                ChampSelectSelectionComponent component = new ChampSelectSelectionComponent(this, selection, i);
                this.components[i] = component;
                component.update(ids[i]);
                this.add(component);
            }
            this.revalidate();
        });
    }

    @Override
    public void onSelection(int componentId, long championId) {
        int previousSelectedId = selectedComponentId;
        this.selectedChampionId = championId;
        this.selectedComponentId = componentId;
        if (previousSelectedId == -1) return;
        this.components[previousSelectedId].unselect();
        this.selection.onSelect(phase, championId);
        this.revalidate();
    }

    public long getSelectedChampionId() {
        return selectedChampionId;
    }
}
