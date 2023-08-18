package com.hawolt.ui.chat.window;

import com.hawolt.util.panel.ChildUIComponent;
import com.hawolt.util.ui.PaintHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
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
        List<String> history = new ArrayList<>();
        int width = getPreferredSize().width - 10;

        // don't compute if window isn't open
        if (width > 0) {
            Graphics2D graphics2D = PaintHelper.getGraphics2D();
            FontMetrics metrics = graphics2D.getFontMetrics();
            String[] messages = this.message.split("\n");
            if (messages.length == 1 && !isTooBig(messages[0], width, metrics)) {
                history.add(messages[0]);
            } else {
                for (String message : messages) {
                    history.addAll(formatMessage(message, width, metrics));
                }
            }
        }
        return history.toArray(String[]::new);
    }

    private List<String> formatMessage(String message, int width, FontMetrics metrics) {
        List<String> formattedMessage = new ArrayList<>();
        message = message.trim();
        if (!isTooBig(message, width, metrics)) {
            // fits in window -> add
            formattedMessage.add(message);
        } else {
            StringBuilder currentLine = new StringBuilder();
            boolean hasTrailingMessagePart = false;
            // message is too big -> check each word
            for(String word: message.split(" ")) {
                if(!isTooBig(currentLine + word + " ", width, metrics)) {
                    // again if word fits on screen -> add to current line
                    currentLine.append(word).append(" ");
                } else {
                    // add current line as message
                    formattedMessage.add(currentLine.toString());
                    // flush current line
                    currentLine = new StringBuilder();
                    // word is too big (how???) -> split word
                    if (isTooBig(word, width, metrics)) {
                        formattedMessage.addAll(formatLongWord(word, width, metrics));
                        hasTrailingMessagePart = false;
                    } else {
                        currentLine.append(word).append(" ");
                        hasTrailingMessagePart = true;
                    }
                }
                if(hasTrailingMessagePart) {
                    // ensure that last inline message is added
                    formattedMessage.add(currentLine.toString());
                }
            }
        }
        return formattedMessage;
    }

    private List<String> formatLongWord(String word, int width, FontMetrics metrics){
        List<String> parts = new ArrayList<>(splitWord(word.trim()));

        while(parts.stream().anyMatch(part -> isTooBig(part, width, metrics))) {
            List<String> temp = new ArrayList<>();

            for(String part: parts) {
                if(isTooBig(part, width, metrics)) {
                    temp.addAll(splitWord(part));
                } else {
                    temp.add(part);
                }
                parts = new LinkedList<>(temp);
            }
        }
        return parts;
    }

    private List<String> splitWord(String word){
        List<String> retVal = new ArrayList<>();
        int half = word.length() / 2;

        retVal.add(word.substring(0, half));
        retVal.add(word.substring(half));

        return retVal;
    }

    private boolean isTooBig(String text, int width, FontMetrics metrics) {
        return metrics.stringWidth(text) > width;
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
