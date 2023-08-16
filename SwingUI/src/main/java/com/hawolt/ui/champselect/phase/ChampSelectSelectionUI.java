package com.hawolt.ui.champselect.phase;

import com.hawolt.ui.champselect.ChampSelectPhase;
import com.hawolt.ui.champselect.IChampSelection;
import com.hawolt.util.panel.ChildUIComponent;
import org.json.JSONArray;

import javax.swing.border.EmptyBorder;
import java.awt.*;

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

    public void update(JSONArray array) {
        Long[] arr = new Long[array.length()];
        for (int i = 0; i < array.length(); i++) {
            arr[i] = array.getLong(i);
        }
        update(arr);
    }

    public void update(Long[] ids) {
        this.removeAll();
        this.components = new ChampSelectSelectionComponent[ids.length];
        for (int i = 0; i < components.length; i++) {
            ChampSelectSelectionComponent component = new ChampSelectSelectionComponent(this, i);
            this.components[i] = component;
            component.update(ids[i]);
            this.add(component);
        }
        this.revalidate();
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
