package com.hawolt.ui.layout;

import com.hawolt.LeagueClientUI;
import com.hawolt.client.LeagueClient;
import com.hawolt.logger.Logger;
import com.hawolt.ui.champselect.ChampSelectUI;
import com.hawolt.ui.github.ReleaseWindow;
import com.hawolt.ui.queue.QueueWindow;
import com.hawolt.ui.store.StoreWindow;
import com.hawolt.util.panel.ChildUIComponent;
import org.json.JSONObject;

import javax.swing.border.MatteBorder;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created: 09/08/2023 15:48
 * Author: Twitter @hawolt
 **/

public class LayoutManager extends ChildUIComponent implements ILayoutManager {

    private final CardLayout layout = new CardLayout();
    private final ChildUIComponent center;
    private final QueueWindow queue;
    private final StoreWindow store;
    private final ChampSelectUI champSelect;

    public LayoutManager(LeagueClientUI leagueClientUI) {
        super(new BorderLayout());

        LeagueClient client = leagueClientUI.getLeagueClient();
        this.add(center = new ChildUIComponent(layout), BorderLayout.CENTER);
        //this.center.setBorder(new MatteBorder(2, 0, 0, 0, Color.DARK_GRAY));

        this.center.add("home", new ReleaseWindow());
        this.center.add("store", store = new StoreWindow(client));
        this.center.add("play", queue = new QueueWindow(leagueClientUI));
        this.center.add("champion_select", champSelect = new ChampSelectUI(leagueClientUI));
        layout.show(center, "github");
    }

    public void showClientComponent(String name) {
        layout.show(center, name);
    }

    public ChampSelectUI getChampSelectUI() {
        return champSelect;
    }

    public QueueWindow getQueue() {
        return queue;
    }

    public StoreWindow getStore() {
        return store;
    }

    @Override
    public void showComponent(String panel) {
        this.layout.show(center, panel);
    }
}
