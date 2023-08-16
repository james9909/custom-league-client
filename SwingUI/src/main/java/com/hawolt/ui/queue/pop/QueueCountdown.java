package com.hawolt.ui.queue.pop;

import com.hawolt.async.ExecutorManager;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created: 11/08/2023 21:11
 * Author: Twitter @hawolt
 **/

public class QueueCountdown extends JPanel implements Runnable {
    private final ScheduledExecutorService service = ExecutorManager.getScheduledService("queue-countdown");
    private final long timestamp = System.currentTimeMillis();
    private final ScheduledFuture<?> future;
    private final long maxAfkMillis;
    private final JDialog dialog;

    public QueueCountdown(JDialog dialog, long maxAfkMillis) {
        this.future = service.scheduleAtFixedRate(this::repaint, 0, 20, TimeUnit.MILLISECONDS);
        this.service.schedule(this, maxAfkMillis, TimeUnit.MILLISECONDS);
        this.maxAfkMillis = maxAfkMillis;
        this.dialog = dialog;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        long elapsed = System.currentTimeMillis() - timestamp;
        double percentage = (double) elapsed / (double) maxAfkMillis;
        Dimension dimension = getSize();
        int width = (int) Math.floor(dimension.getWidth() * percentage);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, dimension.width - width, dimension.height);
    }

    @Override
    public void run() {
        future.cancel(true);
        service.shutdown();
        dialog.dispose();
    }
}
