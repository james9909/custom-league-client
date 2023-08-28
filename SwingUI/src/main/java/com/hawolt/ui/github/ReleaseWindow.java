package com.hawolt.ui.github;

import com.hawolt.generic.util.Network;
import com.hawolt.logger.Logger;
import com.hawolt.util.panel.ChildUIComponent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;

public class ReleaseWindow extends ChildUIComponent {
    private final JTextPane changelog;

    public ReleaseWindow() {
        super(new BorderLayout());

        JScrollPane pane = new JScrollPane(changelog = new JTextPane());

        changelog.setEditable(false);
        changelog.setContentType("text/html");
        changelog.setText(createTemplate());
        changelog.addHyperlinkListener(this::handleLink);

        add(pane);
        setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    public String createTemplate() {
        HtmlBuilder builder = new HtmlBuilder();

        for (String line : Github.getChangelog().split("\r")) {
            for (String word : line.split(" ")) {
                builder.add(word);
            }
            builder.endLine();
        }

        // add discord link for good measures
        builder.endLine().addLink("https://discord.gg/UcGhC9dcHk", "[Click] Join the Discord for more stuff");

        return builder.build();
    }

    private void handleLink(HyperlinkEvent e) {
        if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
            try {
                Network.browse(e.getURL().toString());
            } catch (Exception ex) {
                Logger.error(ex);
            }
        }
    }
}
