package com.hawolt.ui.chat.window;

import com.hawolt.util.panel.ChildUIComponent;
import com.hawolt.util.ui.PaintHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created: 09/08/2023 10:38
 * Author: Twitter @hawolt
 **/

public class ChatMessage extends ChildUIComponent {
    private final ChatPerspective perspective;
    private String[] computed;
    private String message;

    public ChatMessage(ChatPerspective perspective) {
        super(null);
        this.setBackground(perspective == ChatPerspective.SELF ? Color.LIGHT_GRAY.brighter() : Color.LIGHT_GRAY);
        this.perspective = perspective;
        ChildUIComponent test = new ChildUIComponent(null);
        test.setBackground(Color.GREEN);
        add(test, BorderLayout.CENTER);
    }

    public ChatPerspective getPerspective() {
        return perspective;
    }

    public void append(String text) {
        if (this.message == null) {
            this.message = text;
        } else {
            this.message = this.message + text;
        }
        this.computed = compute();
        Graphics2D graphics2D = PaintHelper.getGraphics2D();
        FontMetrics metrics = graphics2D.getFontMetrics();
        this.setPreferredSize(new Dimension(getPreferredSize().width, 10 + (computed.length * (metrics.getAscent() + 2))));
        this.setSize(getPreferredSize());
        this.revalidate();
        this.repaint();
    }


    private String[] compute() {
        int width = getPreferredSize().width - 10;
        Graphics2D graphics2D = PaintHelper.getGraphics2D();
        FontMetrics metrics = graphics2D.getFontMetrics();
        List<String> list = new ArrayList<>();
        String[] messages = message.split("\n");
        if (messages.length == 1 && metrics.stringWidth(messages[0]) < width) {
            list.add(messages[0]);
        } else {
            for (String message : messages) {
                message = message.trim();
                if (metrics.stringWidth(message) < width) {
                    list.add(message);
                } else {
                    String[] words = message.split(" ");
                    String currentLine = words[0];
                    for (int i = 1; i < words.length; i++) {
                        String tmp = String.join(" ", currentLine, words[i]);
                        if (metrics.stringWidth(currentLine) < width) {
                            currentLine = tmp;
                            if (i == words.length - 1) {
                                list.add(currentLine);
                            }
                        } else {
                            list.add(currentLine);
                            currentLine = words[i];
                        }
                    }
                }
            }
        }
        return list.toArray(String[]::new);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (computed == null) return;
        Dimension dimension = getPreferredSize();
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        FontMetrics metrics = graphics2D.getFontMetrics();
        for (int i = 0; i < computed.length; i++) {
            String message = computed[i];
            int x = perspective == ChatPerspective.OTHER ? 5 : dimension.width - 5 - metrics.stringWidth(message);
            int y = 3 + ((i + 1) * metrics.getAscent()) + ((i + 1) * 2);
            graphics2D.drawString(message, x, y);
        }
    }
}
