package com.hawolt.ui.champselect;

import com.hawolt.LeagueClientUI;
import com.hawolt.client.LeagueClient;
import com.hawolt.client.cache.CacheType;
import com.hawolt.client.resources.ledge.teambuilder.objects.MatchContext;
import com.hawolt.logger.Logger;
import com.hawolt.ui.champselect.context.ChampSelectContext;
import com.hawolt.ui.champselect.context.ChampSelectSettingsContext;
import com.hawolt.ui.champselect.context.impl.ChampSelect;
import com.hawolt.ui.champselect.impl.aram.ARAMChampSelectUI;
import com.hawolt.ui.champselect.impl.blank.BlankChampSelectUI;
import com.hawolt.ui.champselect.impl.blind.BlindChampSelectUI;
import com.hawolt.ui.champselect.impl.draft.DraftChampSelectUI;
import com.hawolt.util.panel.ChildUIComponent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created: 29/08/2023 16:59
 * Author: Twitter @hawolt
 **/

public class ChampSelectUI extends ChildUIComponent {
    private final Map<Integer, String> QUEUE_RENDERER_MAPPING = new HashMap<>();
    private final List<AbstractRenderInstance> instances = new ArrayList<>();
    private final CardLayout layout = new CardLayout();
    private final JComponent main = new ChildUIComponent(layout);
    private final ChampSelect champSelect;
    private LeagueClientUI leagueClientUI;
    private LeagueClient leagueClient;

    public ChampSelectUI(LeagueClientUI leagueClientUI) {
        super(new BorderLayout());
        this.add(main, BorderLayout.CENTER);
        if (leagueClientUI != null) {
            this.leagueClientUI = leagueClientUI;
            this.leagueClient = leagueClientUI.getLeagueClient();
            this.champSelect = new ChampSelect(this);
            this.leagueClient.getRTMPClient().addDefaultCallback(champSelect.getChampSelectDataContext().getPacketCallback());
        } else {
            this.champSelect = new ChampSelect(this);
        }
        this.addRenderInstance(BlankChampSelectUI.INSTANCE);
        this.addRenderInstance(DraftChampSelectUI.INSTANCE);
        this.addRenderInstance(BlindChampSelectUI.INSTANCE);
        this.addRenderInstance(ARAMChampSelectUI.INSTANCE);
        this.showBlankPanel();
    }

    public void showBlankPanel() {
        this.layout.show(main, "blank");
    }

    public ChampSelectUI() {
        this(null);
    }


    private void addRenderInstance(AbstractRenderInstance instance) {
        instance.setGlobalRunePanel(champSelect.getChampSelectInterfaceContext().getRuneSelectionPanel());
        int[] queueIds = instance.getSupportedQueueIds();
        for (int id : queueIds) {
            Logger.info("[champ-select] register queueId:{} as '{}'", id, instance.getCardName());
            QUEUE_RENDERER_MAPPING.put(id, instance.getCardName());
        }
        this.instances.add(instance);
        this.main.add(instance.getCardName(), instance);
    }

    public void update(ChampSelectContext context) {
        int initialCounter;
        if (leagueClient != null) {
            MatchContext matchContext = leagueClient.getCachedValue(CacheType.MATCH_CONTEXT);
            initialCounter = matchContext.getPayload().getCounter() + 1;
        } else {
            initialCounter = 5;
        }
        ChampSelectSettingsContext settingsContext = context.getChampSelectSettingsContext();
        if (settingsContext.getCounter() == initialCounter) {
            String card = QUEUE_RENDERER_MAPPING.getOrDefault(settingsContext.getQueueId(), "blank");
            Logger.info("[champ-select] switch to card {}", card);
            this.layout.show(main, card);
            if (leagueClientUI != null) leagueClientUI.getLayoutManager().showClientComponent("select");
        }
        for (AbstractRenderInstance instance : instances) {
            instance.delegate(context, initialCounter);
        }
        this.repaint();
    }

    public ChampSelect getChampSelect() {
        return champSelect;
    }

    public LeagueClient getLeagueClient() {
        return leagueClient;
    }

    public LeagueClientUI getLeagueClientUI() {
        return leagueClientUI;
    }

    public List<AbstractRenderInstance> getInstances() {
        return instances;
    }
}
