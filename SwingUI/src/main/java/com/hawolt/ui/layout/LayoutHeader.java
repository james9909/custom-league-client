package com.hawolt.ui.layout;

import com.hawolt.LeagueClientUI;
import com.hawolt.async.LazyLoadedImageComponent;
import com.hawolt.async.loader.ResourceLoader;
import com.hawolt.client.LeagueClient;
import com.hawolt.ui.chat.profile.ChatSidebarProfile;
import com.hawolt.ui.layout.wallet.HeaderWallet;
import com.hawolt.util.ColorPalette;
import com.hawolt.util.audio.AudioEngine;
import com.hawolt.util.audio.Sound;
import com.hawolt.util.panel.ChildUIComponent;
import com.hawolt.util.ui.LFlatButton;
import com.hawolt.util.ui.LHighlightType;
import com.hawolt.util.ui.LTextAlign;
import com.hawolt.virtual.leagueclient.userinfo.UserInformation;

import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.function.Consumer;

/**
 * Created: 09/08/2023 15:52
 * Author: Twitter @hawolt
 **/

public class LayoutHeader extends ChildUIComponent {
    private final ChatSidebarProfile profile;
    private final HeaderWallet wallet;

    private Point initialClick;

    public LayoutHeader(ILayoutManager manager, LeagueClient client) {
        super(new BorderLayout());
        this.setBackground(ColorPalette.ACCENT_COLOR);
        this.setPreferredSize(new Dimension(0, 90));

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                LayoutHeader.this.initialClick = e.getPoint();
            }
        });
        Frame source = Frame.getFrames()[0];
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(final MouseEvent e) {
                final int thisX = source.getLocation().x;
                final int thisY = source.getLocation().y;
                final int xMoved = e.getX() - LayoutHeader.this.initialClick.x;
                final int yMoved = e.getY() - LayoutHeader.this.initialClick.y;
                final int X = thisX + xMoved;
                final int Y = thisY + yMoved;
                source.setLocation(X, Y);
            }
        });

        ChildUIComponent main = new ChildUIComponent(new BorderLayout());
        main.setBorder(new EmptyBorder(5, 5, 5, 5));
        main.setBackground(ColorPalette.ACCENT_COLOR);
        add(main, BorderLayout.CENTER);

        LazyLoadedImageComponent component = new LazyLoadedImageComponent(new Dimension(90, 90));
        component.setBackground(ColorPalette.ACCENT_COLOR);
        ResourceLoader.loadLocalResource("fullsize-logo.png", component);
        add(component, BorderLayout.WEST);

        ChildUIComponent buttons = new ChildUIComponent(new GridLayout(0, 5, 3, 0));
        buttons.setBackground(ColorPalette.ACCENT_COLOR);
        main.add(buttons, BorderLayout.WEST);

        LFlatButton button = new LFlatButton("STORE", LTextAlign.CENTER, LHighlightType.BOTTOM);
        LFlatButton button1 = new LFlatButton("PLAY", LTextAlign.CENTER, LHighlightType.BOTTOM);
        LFlatButton button2 = new LFlatButton("CHAMPSELECT", LTextAlign.CENTER, LHighlightType.BOTTOM);


        final Consumer<LFlatButton> selectButton = (b) -> {
            button.setSelected(false);
            button1.setSelected(false);
            button2.setSelected(false);
            b.setSelected(true);
        };

        button.addActionListener(o -> {
            selectButton.accept(button);
            AudioEngine.play(Sound.OPEN_STORE);
            manager.showComponent("store");
        });
        buttons.add(button);
        button1.addActionListener(o -> {
            selectButton.accept(button1);
            manager.showComponent("play");
        });
        buttons.add(button1);
        button2.addActionListener(o -> {
            selectButton.accept(button2);
            manager.showComponent("select");
        });
        buttons.add(button2);

        main.add(wallet = new HeaderWallet(client), BorderLayout.EAST);

        UserInformation userInformation = client.getVirtualLeagueClient()
                .getVirtualLeagueClientInstance()
                .getUserInformation();
        add(profile = new ChatSidebarProfile(userInformation, new BorderLayout()), BorderLayout.EAST);
        configure(userInformation);
    }

    public void configure(UserInformation userInformation) {
        if (userInformation.isLeagueAccountAssociated()) {
            String name = userInformation.getUserInformationLeagueAccount().getSummonerName();
            getProfile().getSummoner().getChatSidebarName().setSummonerName(name);
            long iconId = userInformation.getUserInformationLeagueAccount().getProfileIcon();
            getProfile().getIcon().setIconId(iconId);
        } else {
            getProfile().getSummoner().getChatSidebarName().setSummonerName("");
            getProfile().getIcon().setIconId(29);
        }
    }

    public ChatSidebarProfile getProfile() {
        return profile;
    }

    public HeaderWallet getWallet() {
        return wallet;
    }
}
