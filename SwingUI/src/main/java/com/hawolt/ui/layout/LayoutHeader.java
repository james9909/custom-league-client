package com.hawolt.ui.layout;

import com.hawolt.client.LeagueClient;
import com.hawolt.ui.layout.wallet.HeaderWallet;
import com.hawolt.util.AudioEngine;
import com.hawolt.util.ColorPalette;
import com.hawolt.util.panel.ChildUIComponent;
import com.hawolt.util.ui.LFlatButton;
import com.hawolt.util.ui.LHighlightType;
import com.hawolt.util.ui.LTextAlign;

import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Created: 09/08/2023 15:52
 * Author: Twitter @hawolt
 **/

public class LayoutHeader extends ChildUIComponent {
    private final HeaderWallet wallet;

    public LayoutHeader(ILayoutManager manager, LeagueClient client) {
        super(new GridLayout(0, 5, 5, 0));
        this.setBackground(ColorPalette.ACCENT_COLOR);
        this.setPreferredSize(new Dimension(0, 90));
        this.setBorder(new EmptyBorder(5, 5, 5, 5));

        LFlatButton button = new LFlatButton("STORE", LTextAlign.CENTER, LHighlightType.BOTTOM);
        LFlatButton button1 = new LFlatButton("PLAY", LTextAlign.CENTER, LHighlightType.BOTTOM);
        LFlatButton button2 = new LFlatButton("CHAMPSELECT", LTextAlign.CENTER, LHighlightType.BOTTOM);
        LFlatButton button3 = new LFlatButton("RUNES", LTextAlign.CENTER, LHighlightType.BOTTOM);

        final Consumer<LFlatButton> selectButton = (b) -> {
            button.setSelected(false);
            button1.setSelected(false);
            button2.setSelected(false);
            button3.setSelected(false);
            b.setSelected(true);
        };

        button.addActionListener(o -> {
            selectButton.accept(button);
            AudioEngine.play("openstore.wav");
            manager.showComponent("store");
        });
        add(button);
        button1.addActionListener(o -> {
            selectButton.accept(button1);
            manager.showComponent("play");
        });
        add(button1);
        button2.addActionListener(o -> {
            selectButton.accept(button2);
            manager.showComponent("select");
        });
        add(button2);
        button3.addActionListener(o -> {
            selectButton.accept(button3);
            manager.showComponent("runes");
        });

        add(button3);
        add(wallet = new HeaderWallet(client));
    }

    public HeaderWallet getWallet() {
        return wallet;
    }
}
