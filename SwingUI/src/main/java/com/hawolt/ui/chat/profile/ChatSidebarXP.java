package com.hawolt.ui.chat.profile;

import com.hawolt.virtual.leagueclient.userinfo.UserInformation;

import javax.swing.*;
import java.awt.*;

/**
 * Created: 08/08/2023 17:45
 * Author: Twitter @hawolt
 **/

public class ChatSidebarXP extends JComponent {

    private final Color UNOBTAINED = new Color(231, 97, 97);
    private final Color GAINED = new Color(93, 156, 89);

    private final Font font = new Font("Arial", Font.BOLD, 16);

    private int current, total, level;

    public ChatSidebarXP(UserInformation information) {
        if (information.isLeagueAccountAssociated()) {
            this.current = 419;
            this.total = 2193;
            this.level = (int) information.getUserInformationLeagueAccount().getSummonerLevel();
        } else {

        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension dimension = getSize();
        g.setColor(UNOBTAINED);
        g.fillRect(0, 0, dimension.width, getHeight());
        double progress = ((double) current / (double) total);
        int width = (int) Math.floor(progress * (dimension.width - 1));
        g.setColor(GAINED);
        g.fillRect(0, 0, width, getHeight());
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, dimension.width - 1, dimension.height - 1);

        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setFont(font);
        FontMetrics metrics = g.getFontMetrics();

        String experience = String.format("%s / %s", current, total);
        int y = (dimension.height >> 1) + (metrics.getAscent() >> 1);
        drawHighlightedText(g, dimension, experience, 6, y);

        int levelStringWidth = metrics.stringWidth(String.valueOf(level));
        drawHighlightedText(g, dimension, String.valueOf(level), dimension.width - 7 - levelStringWidth, y);
    }

    private void drawHighlightedText(Graphics g, Dimension d, String text, int x, int y) {
        g.setColor(Color.BLACK);
        g.drawString(text, x + 1, y + 1);
        g.setColor(Color.WHITE);
        g.drawString(text, x, y);
    }
}
