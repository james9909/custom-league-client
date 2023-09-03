package com.hawolt.ui.champselect;

import com.hawolt.LeagueClientUI;
import com.hawolt.client.LeagueClient;
import com.hawolt.logger.Logger;
import com.hawolt.rtmp.LeagueRtmpClient;
import com.hawolt.ui.champselect.data.ChampSelectType;
import com.hawolt.ui.champselect.data.GameType;
import com.hawolt.ui.champselect.generic.ChampSelectRuneSelection;
import com.hawolt.ui.champselect.generic.ChampSelectUIComponent;
import com.hawolt.ui.champselect.generic.impl.ChampSelectChoice;
import com.hawolt.ui.champselect.generic.impl.ChampSelectSelectionElement;
import com.hawolt.util.panel.ChildUIComponent;
import com.hawolt.xmpp.event.objects.conversation.history.impl.IncomingMessage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created: 29/08/2023 18:04
 * Author: Twitter @hawolt
 **/

public abstract class AbstractRenderInstance extends ChampSelectUIComponent implements ChampSelectRenderer, ChampSelectChoice {

    protected final ChildUIComponent component = new ChildUIComponent(new BorderLayout());

    public AbstractRenderInstance() {
        this.setLayout(new BorderLayout());
        this.add(component, BorderLayout.CENTER);
    }

    private final Map<ChampSelectType, ChampSelectSelectionElement> map = new ConcurrentHashMap<>();


    protected abstract void push(IncomingMessage incomingMessage);

    protected abstract void stopChampSelectTimer();

    public abstract void invokeChampionFilter(String champion);

    public abstract void setGlobalRunePanel(ChampSelectRuneSelection selection);

    @Override
    public void init() {
        if (context == null) return;
        for (ChampSelectType type : map.keySet()) {
            ChampSelectSelectionElement element = map.get(type);
            element.setSelected(false);
            element.repaint();
        }
        map.clear();
    }

    @Override
    public void onChoice(ChampSelectSelectionElement element) {
        if (context == null || element == null) return;
        if (map.containsKey(element.getType())) {
            ChampSelectSelectionElement champSelectSelectionElement = map.get(element.getType());
            if (champSelectSelectionElement == null) return;
            champSelectSelectionElement.setSelected(false);
            champSelectSelectionElement.repaint();
        }
        map.put(element.getType(), element);
    }

    public void dodge(GameType type) {
        if (context == null) return;
        LeagueClientUI.service.execute(() -> {
            LeagueClient client = context.getLeagueClient();
            LeagueRtmpClient rtmpClient = client.getRTMPClient();
            LeagueClientUI leagueClientUI = context.getLeagueClientUI();
            try {
                switch (type) {
                    case CLASSIC ->
                            rtmpClient.getTeamBuilderService().quitGameV2Asynchronous(context.getPacketCallback());
                    case CUSTOM -> {
                        Logger.debug("currently not supported");
                    }
                }
                leagueClientUI.getChatSidebar().getEssentials().disableQueueState();
                context.quitChampSelect();
                revalidate();
            } catch (IOException e) {
                Logger.error("failed to quit game");
            }
        });
    }

    public void delegate(ChampSelectContext context) {
        this.assign(this, context);
        this.configure(context);
        this.update(this);
    }

    private void assign(JComponent parent, ChampSelectContext context) {
        Component[] components = parent.getComponents();
        for (Component component : components) {
            if (component == null) continue;
            if ((component instanceof ChampSelectUIComponent champSelectUIComponent)) {
                champSelectUIComponent.configure(context);
            }
            if (component instanceof JComponent child) assign(child, context);
        }
    }

    private void update(JComponent parent) {
        Component[] components = parent.getComponents();
        for (Component component : components) {
            if (component == null) continue;
            if (component instanceof JComponent child) update(child);
            if ((component instanceof ChampSelectUIComponent champSelectUIComponent)) {
                champSelectUIComponent.execute();
            }
        }
    }
}
