package com.hawolt.ui.runes;

import com.hawolt.LeagueClientUI;
import com.hawolt.async.loader.ResourceConsumer;
import com.hawolt.async.loader.ResourceLoader;
import com.hawolt.client.LeagueClient;
import com.hawolt.logger.Logger;
import com.hawolt.util.panel.ChildUIComponent;
import com.hawolt.util.ui.LFlatButton;
import com.hawolt.util.ui.LHighlightType;
import com.hawolt.util.ui.LTabbedPane;
import com.hawolt.util.ui.LTextAlign;
import com.hawolt.version.local.LocalLeagueFileVersion;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created: 15/08/2023 20:14
 * Author: Twitter @hawolt
 **/

public class RuneSelectionPanel extends ChildUIComponent implements ActionListener, ResourceConsumer<JSONArray, byte[]> {
    private final String DD_RUNES = "http://ddragon.leagueoflegends.com/cdn/%s/data/en_US/runesReforged.json";
    private final LTabbedPane main, secondary;
    private final ChildUIComponent right;
    private final LeagueClient client;
    private final LFlatButton button;
    private int selected = -1;
    private RunePanel extra;

    public RuneSelectionPanel(LeagueClientUI leagueClientUI) {
        super(new BorderLayout());
        ChildUIComponent panel = new ChildUIComponent(new GridLayout(0, 2, 5, 0));
        this.add(panel, BorderLayout.CENTER);
        this.main = new LTabbedPane();
        panel.add(main);
        this.secondary = new LTabbedPane();
        this.right = new ChildUIComponent(new BorderLayout());
        this.right.add(secondary, BorderLayout.CENTER);
        panel.add(right);
        this.client = leagueClientUI.getLeagueClient();
        LocalLeagueFileVersion leagueFileVersion = client.getVirtualLeagueClientInstance().getLocalLeagueFileVersion();
        String value = leagueFileVersion.getVersionValue(client.getPlayerPlatform(), "LeagueClientUxRender.exe");
        String[] versions = value.split("\\.");
        String patch = String.format("%s.%s.1", versions[0], versions[1]);
        this.button = new LFlatButton("Set Runes", LTextAlign.CENTER, LHighlightType.COMPONENT);
        this.button.addActionListener(this);
        this.add(button, BorderLayout.SOUTH);
        ResourceLoader.loadResource(String.format(DD_RUNES, patch), this);
        ResourceLoader.loadLocalResource("runes.json", this);
    }

    public JSONObject getSelectedRunes() throws IncompleteRunePageException {
        JSONObject runes = new JSONObject();
        JSONArray perkIds = new JSONArray();
        RunePanel primaryStyle = getSelectedRuneType(main);
        RunePanel subStyle = getSelectedRuneType(secondary);
        for (DDRune rune : getAllRunesOrdered(primaryStyle, subStyle, extra)) {
            perkIds.put(rune.getId());
        }
        runes.put("perkIds", perkIds);
        runes.put("perkStyle", primaryStyle.getType().getId());
        runes.put("perkSubStyle", subStyle.getType().getId());
        return runes;
    }

    private void disableTabAndSelectNextAvailable(JTabbedPane pane, int index) {
        if (selected != -1) pane.setEnabledAt(selected, true);
        pane.setEnabledAt(selected = index, false);
        if (pane.getSelectedIndex() != index) return;
        pane.setSelectedIndex((++index) % pane.getTabCount());
    }

    private RunePanel getSelectedRuneType(JTabbedPane pane) {
        return ((RunePanel) pane.getComponentAt(pane.getSelectedIndex()));
    }

    private DDRune[] getAllRunesOrdered(RunePanel... panels) throws IncompleteRunePageException {
        List<DDRune> runes = new ArrayList<>();
        for (RunePanel panel : panels) {
            runes.addAll(Arrays.asList(panel.getSelectedRunes()));
        }
        return runes.toArray(DDRune[]::new);
    }

    @Override
    public void onException(Object o, Exception e) {
        Logger.fatal("Failed to load a resource at {}", o);
        Logger.error(e);
    }

    @Override
    public void consume(Object o, JSONArray array) {
        if (array.length() == 1) {
            extra = new RunePanel(new DDRuneType(array.getJSONObject(0)), false, false);
            extra.setPreferredSize(new Dimension(0, 300));
            right.add(extra, BorderLayout.SOUTH);
        } else {
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                DDRuneType type = new DDRuneType(object);
                main.addTab(type.getName(), new RunePanel(type, false));
                secondary.addTab(type.getName(), new RunePanel(type, true));
            }
            disableTabAndSelectNextAvailable(secondary, 0);
            main.addChangeListener(listener -> disableTabAndSelectNextAvailable(secondary, main.getSelectedIndex()));
        }
        revalidate();
    }

    @Override
    public JSONArray transform(byte[] bytes) {
        return new JSONArray(new String(bytes));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (client == null) return;
        try {
            JSONObject runes = getSelectedRunes();
            client.getLedge().getPerks().setRunesForCurrentRegistration(runes);
        } catch (IncompleteRunePageException | IOException ex) {
            Logger.error(ex);
        }
    }
}
