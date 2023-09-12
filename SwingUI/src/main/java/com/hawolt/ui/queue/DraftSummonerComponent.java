package com.hawolt.ui.queue;

import com.hawolt.client.resources.ledge.parties.objects.PartyMember;
import com.hawolt.client.resources.ledge.parties.objects.PartyParticipantMetadata;

import java.awt.*;

/**
 * Created: 21/08/2023 18:34
 * Author: Twitter @hawolt
 **/

public class DraftSummonerComponent extends SummonerComponent {
    private static final Font ROLE_FONT = new Font("Arial", Font.BOLD, 16);


    public DraftSummonerComponent() {
        super();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (summoner == null || participant == null) return;
        String role = participant.getRole();
        if (!role.equals("MEMBER") && !role.equals("LEADER")) return;
        Dimension dimension = getSize();
        g.setColor(Color.GRAY);
        g.fillRect(3, 3, dimension.width - 7, dimension.height - 7);
        g.setColor(Color.BLACK);
        g.drawRect(3, 3, dimension.width - 7, dimension.height - 7);
        int centeredX = dimension.width >> 1;
        int centeredY = dimension.height >> 1;
        if (image != null) {
            int imageX = centeredX - (image.getWidth() >> 1);
            int imageY = (dimension.height >> 1) - (image.getHeight() >> 1);
            g.setColor(Color.BLACK);
            int imageSpacing = 3;
            g.fillRect(
                    imageX - imageSpacing,
                    imageY - imageSpacing,
                    image.getWidth() + (imageSpacing << 1),
                    image.getHeight() + (imageSpacing << 1)
            );
            g.drawImage(image, imageX, imageY, null);
        }
        FontMetrics metrics;
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setFont(NAME_FONT);
        metrics = graphics2D.getFontMetrics();
        String name = summoner.getName().trim();
        int width = metrics.stringWidth(name);
        int nameX = centeredX - (width >> 1);
        g.drawString(name, nameX, centeredY - (IMAGE_DIMENSION.height >> 1) - 20);

        PartyMember member = (PartyMember) participant;
        if (member == null) return;
        PartyParticipantMetadata metadata = member.getParticipantMetadata();
        if (metadata == null) return;
        graphics2D.setFont(ROLE_FONT);
        int positionY = centeredY + (IMAGE_DIMENSION.height >> 1) + 40;
        paintRoleSelection(graphics2D, centeredX, positionY, metadata.getPrimaryPreference(), 0);
        paintRoleSelection(graphics2D, centeredX, positionY, metadata.getSecondaryPreference(), 1);
    }

    private void paintRoleSelection(Graphics2D graphics2D, int centeredX, int positionY, String role, int index) {
        FontMetrics metrics = graphics2D.getFontMetrics();
        graphics2D.setColor(Color.DARK_GRAY);
        int additionalSpacing = 5, gapSpacing = 6;
        int x = gapSpacing + (index * centeredX);
        graphics2D.fillRoundRect(x, positionY - metrics.getAscent() - additionalSpacing, centeredX - (gapSpacing << 1), 18 + (additionalSpacing << 1), 10, 10);
        int primaryX = (centeredX - (index == 0 ? (centeredX >> 1) : (-1 * (centeredX >> 1)))) - (metrics.stringWidth(role) >> 1);
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(role, primaryX, positionY);
    }

}
