package com.hawolt.ui.chat.friendlist;

import com.hawolt.LeagueClientUI;
import com.hawolt.xmpp.core.VirtualRiotXMPPClient;
import com.hawolt.xmpp.event.objects.friends.GenericFriend;
import com.hawolt.xmpp.event.objects.friends.impl.OnlineFriend;
import com.hawolt.xmpp.event.objects.presence.AbstractPresence;
import com.hawolt.xmpp.event.objects.presence.ConnectionStatus;
import com.hawolt.xmpp.event.objects.presence.impl.MobilePresence;
import com.hawolt.xmpp.event.objects.presence.impl.OfflinePresence;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * Created: 08/08/2023 18:15
 * Author: Twitter @hawolt
 **/

public class ChatSidebarFriend extends JPanel implements MouseListener, MouseMotionListener {
    private static final Font font = new Font("Arial", Font.BOLD, 18);
    private final Color border = new Color(122, 138, 153);
    private final VirtualRiotXMPPClient xmppClient;
    private final GenericFriend friend;

    private AbstractPresence lastKnownPresence;
    private ConnectionStatus connectionStatus;
    private Runnable runnable;
    private int counter;

    public ChatSidebarFriend(VirtualRiotXMPPClient xmppClient, GenericFriend friend) {
        this.setPreferredSize(new Dimension(0, 50));
        this.addMouseMotionListener(this);
        this.setBackground(Color.GRAY);
        this.addMouseListener(this);
        this.xmppClient = xmppClient;
        this.friend = friend;
    }

    public GenericFriend getFriend() {
        return friend;
    }

    public void setLastKnownPresence(AbstractPresence lastKnownPresence) {
        ConnectionStatus previous = this.connectionStatus;
        this.connectionStatus = lastKnownPresence instanceof OfflinePresence ?
                ConnectionStatus.OFFLINE : lastKnownPresence instanceof MobilePresence ?
                ConnectionStatus.MOBILE : ConnectionStatus.ONLINE;
        this.lastKnownPresence = lastKnownPresence;
        /*
            if (previous != null && previous != this.connectionStatus) {
                switch (connectionStatus) {
                    case OFFLINE -> AudioEngine.play("friend_logout.wav");
                    case ONLINE -> AudioEngine.play("friend_login.wav");
                }
            }
         */
        this.repaint();
    }

    public void executeOnClick(Runnable runnable) {
        this.runnable = runnable;
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus == null ? ConnectionStatus.OFFLINE : connectionStatus;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        FontMetrics metrics = graphics2D.getFontMetrics();

        Dimension dimension = getSize();
        int height = dimension.height >> 1;

        Font current = graphics2D.getFont();

        graphics2D.setFont(font);
        graphics2D.setColor(Color.WHITE);

        String name = friend instanceof OnlineFriend ?
                ((OnlineFriend) friend).getLOLName() :
                String.join(
                        "#",
                        friend.getName().toString(),
                        friend.getTagline().toString()
                );
        graphics2D.drawString(name, 20, (height >> 1) + (metrics.getAscent() >> 1));


        if (lastKnownPresence != null) {
            JSONObject raw = lastKnownPresence.getRaw();
            if (raw.has("status")) {
                graphics2D.setFont(current);
                metrics = graphics2D.getFontMetrics();
                String status = raw.getString("status");
                graphics2D.drawString(status, 5, 18 + (31 >> 1) + (metrics.getAscent() >> 1));
            }
        }

        g.setColor(border);
        g.drawLine(0, dimension.height - 1, dimension.width, dimension.height - 1);

        Color color;
        if (connectionStatus == null) color = Color.RED;
        else {
            switch (connectionStatus) {
                case OFFLINE, UNKNOWN -> color = Color.RED;
                default -> color = Color.GREEN;
            }
        }
        graphics2D.setColor(color);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.fill(new RoundRectangle2D.Float(5, (height >> 1) - (10 >> 1), 10, 10, 360, 360));

        if (counter == 0) return;

        graphics2D.setFont(font);
        metrics = graphics2D.getFontMetrics();

        int notificationWidth = metrics.stringWidth(String.valueOf(counter));
        int widthMargin = 8, heightMargin = 2;
        int computedWidth = notificationWidth + (widthMargin << 1);
        int computedHeight = metrics.getAscent() + (heightMargin << 1);
        int computedX = dimension.width - widthMargin - computedWidth;
        int computedY = (height >> 1) - (computedHeight >> 1);

        graphics2D.setColor(Color.BLACK);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.fill(new RoundRectangle2D.Float(computedX + 1, computedY + 1, computedWidth, computedHeight, 5, 5));

        graphics2D.setColor(new Color(255, 175, 79));
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.fill(new RoundRectangle2D.Float(computedX, computedY, computedWidth, computedHeight, 5, 5));

        drawHighlightedText(graphics2D, new Rectangle(computedX, computedY, computedWidth, computedHeight), String.valueOf(counter));
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

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            LeagueClientUI.service.execute(runnable);
            this.setBackground(Color.GRAY);
            this.repaint();
        } else if (SwingUtilities.isRightMouseButton(e)) {
            JPopupMenu menu = new JPopupMenu();
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
            menu.add(remove);
            menu.add(block);
            menu.add(close);
            menu.show(this, e.getX(), e.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.setBackground(Color.LIGHT_GRAY);
        this.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.setBackground(Color.GRAY);
        this.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
