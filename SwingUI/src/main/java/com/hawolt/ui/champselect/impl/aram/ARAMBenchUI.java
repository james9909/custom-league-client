package com.hawolt.ui.champselect.impl.aram;

import com.hawolt.LeagueClientUI;
import com.hawolt.logger.Logger;
import com.hawolt.rtmp.LeagueRtmpClient;
import com.hawolt.rtmp.amf.TypedObject;
import com.hawolt.rtmp.io.RtmpPacket;
import com.hawolt.rtmp.service.impl.TeamBuilderService;
import com.hawolt.rtmp.utility.PacketCallback;
import com.hawolt.ui.champselect.generic.ChampSelectUIComponent;
import com.hawolt.ui.champselect.generic.impl.ChampSelectBenchElement;
import com.hawolt.ui.champselect.generic.impl.ChampSelectChoice;
import com.hawolt.util.ColorPalette;
import com.hawolt.util.panel.ChildUIComponent;
import com.hawolt.util.ui.LFlatButton;
import com.hawolt.util.ui.LHighlightType;
import com.hawolt.util.ui.LTextAlign;
import org.json.JSONArray;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

/**
 * Created: 10/09/2023 17:32
 * Author: Twitter @hawolt
 **/

public class ARAMBenchUI extends ChampSelectUIComponent {
    private final ChampSelectBenchElement[] elements = new ChampSelectBenchElement[10];

    public ARAMBenchUI(ChampSelectChoice callback) {
        this.setLayout(new BorderLayout());
        this.setBackground(ColorPalette.BACKGROUND_COLOR);
        this.setPreferredSize(new Dimension(0, 80));
        this.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK),
                        new EmptyBorder(5, 5, 5, 5)
                )
        );
        ChildUIComponent grid = new ChildUIComponent(new GridLayout(0, 10, 5, 0));
        for (int i = 0; i < elements.length; i++) {
            ChampSelectBenchElement element = new ChampSelectBenchElement(callback);
            element.setChampionId(-1);
            elements[i] = element;
            grid.add(element);
        }
        add(grid, BorderLayout.CENTER);

        LFlatButton button = new LFlatButton("⟳", LTextAlign.CENTER, LHighlightType.COMPONENT);
        button.addActionListener(listener -> LeagueClientUI.service.execute(() -> {
            LeagueRtmpClient client = context.getChampSelectDataContext().getLeagueClient().getRTMPClient();
            TeamBuilderService teamBuilderService = client.getTeamBuilderService();
            try {
                teamBuilderService.rerollV1Asynchronous(new PacketCallback() {
                    @Override
                    public void onPacket(RtmpPacket rtmpPacket, TypedObject typedObject) throws Exception {
                        Logger.info(typedObject);
                    }
                });
            } catch (IOException e) {
                Logger.error("Unable to reroll");
                Logger.error(e);
            }
        }));
        button.setFont(new Font(Font.DIALOG, Font.PLAIN, 50));
        add(button, BorderLayout.EAST);
    }

    @Override
    public void init() {
        for (ChampSelectBenchElement element : elements) {
            element.setChampionId(-1);
        }
    }

    @Override
    public void update() {
        JSONArray bench = context.getChampSelectSettingsContext().getChampionBench();
        for (int i = 0; i < bench.length(); i++) {
            elements[i].setChampionId(bench.getInt(i));
        }
    }
}
