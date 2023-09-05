package com.hawolt.ui.chat.friendlist;

import com.hawolt.LeagueClientUI;
import com.hawolt.client.LeagueClient;
import com.hawolt.client.resources.ledge.LedgeEndpoint;
import com.hawolt.client.resources.ledge.parties.PartiesLedge;
import com.hawolt.client.resources.ledge.parties.objects.PartyException;
import com.hawolt.client.resources.ledge.parties.objects.data.PartyRole;
import com.hawolt.client.resources.ledge.summoner.SummonerLedge;
import com.hawolt.client.resources.ledge.summoner.objects.Summoner;
import com.hawolt.logger.Logger;
import com.hawolt.util.ColorPalette;
import com.hawolt.util.ui.LFlatButton;
import com.hawolt.util.ui.LHighlightType;
import com.hawolt.util.ui.LTextAlign;
import com.hawolt.util.ui.PaintHelper;
import com.hawolt.xmpp.core.VirtualRiotXMPPClient;
import com.hawolt.xmpp.event.objects.friends.GenericFriend;
import com.hawolt.xmpp.event.objects.friends.impl.OnlineFriend;
import com.hawolt.xmpp.event.objects.presence.AbstractPresence;
import com.hawolt.xmpp.event.objects.presence.ConnectionStatus;
import com.hawolt.xmpp.event.objects.presence.impl.GamePresence;
import com.hawolt.xmpp.event.objects.presence.impl.PresenceData;
import com.hawolt.xmpp.event.objects.presence.impl.data.LeagueOfLegends;
import com.hawolt.xmpp.event.objects.presence.impl.data.Valorant;
import com.hawolt.xmpp.event.objects.presence.impl.data.lol.LeagueGameType;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.util.List;

/**
 * Created: 08/08/2023 18:15
 * Author: Twitter @hawolt
 **/

public class ChatSidebarFriend extends LFlatButton {
    private final VirtualRiotXMPPClient xmppClient;
    private final LeagueClientUI leagueClientUI;
    private final GenericFriend friend;
    private AbstractPresence lastKnownPresence;
    private String providedUsername;
    private Runnable runnable;
    private int counter;


    public ChatSidebarFriend(VirtualRiotXMPPClient xmppClient, GenericFriend friend, LeagueClientUI leagueClientUI) {
        super();
        setHighlightType(LHighlightType.COMPONENT);
        this.setPreferredSize(new Dimension(0, 50));
        this.friend = friend;
        this.xmppClient = xmppClient;
        this.leagueClientUI = leagueClientUI;
        this.providedUsername = getProvidedUsername();
        this.setFont(new Font("Dialog", Font.BOLD, 18));
        this.setText(providedUsername);
        this.color = getBaseColor();
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                clickEvent(evt);
            }
        });
    }

    public String getProvidedUsername() {
        return friend instanceof OnlineFriend ?
                ((OnlineFriend) friend).getLOLName() :
                String.join(
                        "#",
                        friend.getName().toString(),
                        friend.getTagline().toString()
                );
    }

    public GenericFriend getFriend() {
        return friend;
    }

    private ConnectionStatus connectionStatus;
    private String status;
    private Color color;

    private Color getBaseColor() {
        return switch (getConnectionStatus()) {
            case ONLINE, MOBILE -> ColorPalette.FRIEND_ONLINE;
            default -> ColorPalette.FRIEND_OFFLINE;
        };
    }

    public void setLastKnownPresence(AbstractPresence lastKnownPresence) {
        this.lastKnownPresence = lastKnownPresence;
        this.connectionStatus = getConnectionStatus();
        this.color = getBaseColor();
        if (lastKnownPresence instanceof GamePresence presence) {
            for (PresenceData data : presence.getPresenceData()) {
                if (data instanceof LeagueOfLegends league) {
                    if ("away".equals(presence.getShow())) {
                        this.color = ColorPalette.FRIEND_DND;
                        this.status = "Do not Disturb";
                    } else {
                        switch (league.getLeagueGameStatus()) {
                            case CHAMP_SELECT, QUEUE, IN_GAME -> this.color = ColorPalette.FRIEND_IN_GAME;
                        }
                        String gameStatus = switch (league.getLeagueGameStatus()) {
                            case ONLINE -> "Online";
                            case IN_LOBBY -> "Lobby";
                            case OPEN_LOBBY -> "Lobby (Open)";
                            case CLOSED_LOBBY -> "Lobby (Closed)";
                            case FULL_LOBBY -> "Lobby (Full)";
                            case CHAMP_SELECT -> "Champselect";
                            case QUEUE -> "In Queue";
                            case IN_GAME -> "Playing";
                            case UNKNOWN -> "Idle";
                        };
                        String type = translate(league.getLeagueQueueType().name());
                        String gameType = switch (league.getLeagueQueueType()) {
                            case TFT, TFT_RANKED, DOUBLE_UP, HYPER_ROLL -> String.format("TFT %s", type);
                            case PRACTICE_TOOL, NORMAL, FLEX, SOLO_DUO, ARAM -> type;
                            case UNKNOWN -> null;
                        };
                        if (gameType != null) {
                            this.status = String.join(" ", gameStatus, gameType);
                        } else {
                            this.status = gameStatus;
                        }
                    }
                } else if (data instanceof Valorant valorant) {
                    this.color = ColorPalette.FRIEND_IN_OTHER_GAME;
                    this.status = switch (valorant.getValorantActivityType()) {
                        case IN_GAME -> "Playing Valorant";
                        case UNKNOWN -> "Valorant";
                        case IDLE -> null;
                    };
                }
            }
        }
    }

    private String translate(String name) {
        StringBuilder base = new StringBuilder(name.replace("TFT_", "").toLowerCase());
        base.setCharAt(0, Character.toUpperCase(base.charAt(0)));
        for (int i = base.length() - 1; i >= 1; i--) {
            if (base.charAt(i) == '_') {
                if (i + 1 <= base.length() - 1) base.setCharAt(i + 1, Character.toUpperCase(base.charAt(i + 1)));
                base.setCharAt(i, ' ');
            }
        }
        return base.toString();
    }

    public AbstractPresence getLastKnownPresence() {
        return lastKnownPresence;
    }

    public ConnectionStatus getConnectionStatus() {
        return lastKnownPresence != null ? lastKnownPresence.getConnectionStatus() : ConnectionStatus.OFFLINE;
    }

    public void executeOnClick(Runnable runnable) {
        this.runnable = runnable;
    }

    private final Font statusFont = new Font("Dialog", Font.BOLD, 12);

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics2D = (Graphics2D) g;
        Dimension dimension = getSize();

        int width = getHeight() / 3;
        int height = getHeight() / 3;
        int computedX = getWidth() - width - getWidth() / 20;
        int computedY = getHeight() / 2 - height / 2;

        int consumedSpace = dimension.width - computedX;
        if (status != null) PaintHelper.drawShadowText(
                graphics2D,
                statusFont,
                status,
                new Rectangle(3, 0, dimension.width - consumedSpace, getHeight() / 6),
                LTextAlign.LEFT,
                color
        );

        graphics2D.setColor(color);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.fill(new RoundRectangle2D.Float(computedX, computedY, width, height, 360, 360));

        if (counter == 0) return;

        FontMetrics metrics = graphics2D.getFontMetrics();
        int notificationWidth = metrics.stringWidth(String.valueOf(counter));
        int widthMargin = 8, heightMargin = 2, notificationOffset = 30;
        int computedWidth = notificationWidth + (widthMargin << 1);
        int computedHeight = metrics.getAscent() + (heightMargin << 1);
        int rectangleX = dimension.width - widthMargin - computedWidth - notificationOffset;
        int rectangleY = (dimension.height >> 1) - (computedHeight >> 1);

        graphics2D.setColor(Color.BLACK);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.fill(new RoundRectangle2D.Float(rectangleX + 1, rectangleY + 1, computedWidth, computedHeight, 5, 5));

        graphics2D.setColor(new Color(255, 175, 79));
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.fill(new RoundRectangle2D.Float(rectangleX, rectangleY, computedWidth, computedHeight, 5, 5));

        drawHighlightedText(graphics2D, new Rectangle(rectangleX, rectangleY, computedWidth, computedHeight), String.valueOf(counter));
    }

    private void drawHighlightedText(Graphics2D g, Rectangle rectangle, String text) {
        FontMetrics metrics = g.getFontMetrics();
        int width = metrics.stringWidth(text);
        int x = rectangle.x + (rectangle.width >> 1) - (width >> 1);
        int y = rectangle.y + (rectangle.height >> 1) + (metrics.getAscent() >> 1) - 1;
        g.setColor(Color.BLACK);
        g.drawString(text, x + 1, y + 1);
        g.setColor(Color.WHITE);
        g.drawString(text, x, y);
    }

    public void increment() {
        this.counter += 1;
        this.repaint();
    }

    public void opened() {
        this.counter = 0;
        this.repaint();
    }

    public void clickEvent(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            LeagueClientUI.service.execute(runnable);
            this.setBackground(Color.GRAY);
            this.repaint();
        } else if (SwingUtilities.isRightMouseButton(e)) {
            JPopupMenu menu = new JPopupMenu();
            if (lastKnownPresence != null && (lastKnownPresence instanceof GamePresence presence)) {
                List<LeagueOfLegends> list = presence.getPresenceData()
                        .stream()
                        .filter(o -> o instanceof LeagueOfLegends)
                        .map(o -> (LeagueOfLegends) o)
                        .toList();
                if (!list.isEmpty()) {
                    LeagueOfLegends league = list.get(0);
                    JSONObject pty = league.getPTY();
                    if (pty.has("partyId") && !pty.isNull("partyId")) {
                        String partyId = pty.getString("partyId");
                        JMenuItem join = new JMenuItem(new AbstractAction("Join Lobby") {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                LeagueClient client = leagueClientUI.getLeagueClient();
                                try {
                                    client.getLedge().getParties().role(partyId, PartyRole.MEMBER);
                                    leagueClientUI.getLayoutManager().showClientComponent("play");
                                    LeagueGameType gameType = league.getLeagueQueueType().getGameType();
                                    if (gameType == LeagueGameType.TFT) {
                                        leagueClientUI.getLayoutManager().getQueue().getTftLobby().actionPerformed(null);
                                        leagueClientUI.getLayoutManager().getQueue().showClientComponent("tftLobby");
                                    } else {
                                        leagueClientUI.getLayoutManager().getQueue().getDraftLobby().actionPerformed(null);
                                        leagueClientUI.getLayoutManager().getQueue().showClientComponent("lobby");
                                    }
                                } catch (IOException ex) {
                                    Logger.error(ex);
                                }
                            }
                        });
                        menu.add(join);
                    }
                }
            }
            JMenuItem invite = new JMenuItem(new AbstractAction("Invite Friend") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LedgeEndpoint ledges = leagueClientUI.getLeagueClient().getLedge();
                    SummonerLedge summonerLedge = ledges.getSummoner();
                    PartiesLedge partiesLedge = ledges.getParties();
                    try {
                        Summoner summoner = summonerLedge.resolveSummonerByPUUD(friend.getPUUID());
                        partiesLedge.invite(summoner.getPUUID());
                    } catch (IOException | PartyException ex) {
                        Logger.error(ex);
                    }
                }
            });
            JMenuItem remove = new JMenuItem(new AbstractAction("Remove Friend") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    xmppClient.removeFriend(friend.getJID());
                }
            });
            JMenuItem block = new JMenuItem(new AbstractAction("Block User") {
                @Override
                public void actionPerformed(ActionEvent e) {

                    xmppClient.blockUser(friend.getJID());
                    xmppClient.removeFriend(friend.getJID());
                }
            });
            JMenuItem close = new JMenuItem(new AbstractAction("Close Menu") {
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            });
            menu.add(invite);
            menu.add(remove);
            menu.add(block);
            menu.add(close);
            menu.show(this, e.getX(), e.getY());
        }
    }
}
