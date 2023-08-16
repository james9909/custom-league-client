package com.hawolt.ui.champselect.header;

import com.hawolt.async.ExecutorManager;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created: 06/08/2023 14:06
 * Author: Twitter @hawolt
 **/

public class ChampSelectTimerUI extends JPanel {
    private final static Color outline = new Color(122, 138, 153);
    private String text;
    private int state;

    public ChampSelectTimerUI() {
        this.setBackground(Color.BLACK);
        this.setBorder(BorderFactory.createLineBorder(outline));
        ScheduledExecutorService scheduler = ExecutorManager.getScheduledService("champ-select-timer-ui");
        scheduler.scheduleAtFixedRate(this::repaint, 0, 20, TimeUnit.MILLISECONDS);
    }

    private double currentTotalTimeMillis, currentTimeRemainingMillis, timestamp;

    public void update(long currentTotalTimeMillis, long currentTimeRemainingMillis) {
        this.currentTimeRemainingMillis = currentTimeRemainingMillis;
        this.currentTotalTimeMillis = currentTotalTimeMillis;
        this.timestamp = System.currentTimeMillis();
    }

    public void update(int state, String subphase) {
        this.state = state;
        switch (state) {
            case -2:
                text = "";
                break;
            case -1:
                text = "HOVER YOUR CHAMPION";
                break;
            case 0:
                text = "BAN PHASE";
                break;
            case 7:
                text = "PREPARE YOUR LOADOUT";
                break;
            default:
                text = "PICK PHASE " + state;
                break;

        }
    }

    private final static Font font = new Font("Arial", Font.BOLD, 30);

    @Override
    protected void paintComponent(Graphics g) {
        if (!isVisible()) return;
        super.paintComponent(g);
        double difference = currentTotalTimeMillis - currentTimeRemainingMillis + (System.currentTimeMillis() - timestamp);
        double percentage = difference / currentTotalTimeMillis;
        if (percentage < 0) percentage = 0;
        Dimension dimension = getSize();
        int extend = (int) Math.ceil(dimension.width * percentage);
        g.setColor(Color.WHITE);
        g.fillRect(0, dimension.height - 5, extend, 5);
        if (text == null) return;
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setFont(font);
        FontMetrics metrics = graphics2D.getFontMetrics();
        int width = metrics.stringWidth(text);
        int remainingHeight = dimension.height - 5;
        int x = (dimension.width >> 1) - (width >> 1);
        int y = (remainingHeight >> 1) + (metrics.getAscent() >> 1);
        graphics2D.drawString(text, x, y);
    }
}
