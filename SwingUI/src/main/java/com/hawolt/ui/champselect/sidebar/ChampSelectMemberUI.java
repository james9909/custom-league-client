package com.hawolt.ui.champselect.sidebar;

import com.hawolt.async.loader.impl.ImageLoader;
import com.hawolt.async.loader.impl.SpellLoader;
import com.hawolt.logger.Logger;
import com.hawolt.ui.champselect.AlliedMember;
import com.hawolt.util.Image;
import com.hawolt.util.jhlab.GaussianFilter;
import org.imgscalr.Scalr;
import org.json.JSONObject;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * Created: 06/08/2023 14:14
 * Author: Twitter @hawolt
 **/

public class ChampSelectMemberUI extends ChampSelectBlankMemberUI {
    protected static final Font font = new Font("Arial", Font.PLAIN, 16);
    private final static String splash = "https://raw.communitydragon.org/pbe/plugins/rcp-be-lol-game-data/global/default/v1/champion-splashes/%s/%s.jpg";
    private final static String preset = "https://raw.communitydragon.org/pbe/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/%s.png";

    private final static Color opaque = new Color(255, 255, 255, 50);
    private final static GaussianFilter filter = new GaussianFilter(5);

    private BufferedImage image, skin, decoration, summoner1, summoner2;
    private int championId, teamId, summonerId, cellId;
    private final String nameVisibilityType;
    protected final JSONObject object;
    private boolean completed;

    private AlliedMember member;

    public ChampSelectMemberUI(JSONObject object) {
        super();
        this.nameVisibilityType = object.getString("nameVisibilityType");
        this.championId = object.getInt("championId");
        this.summonerId = object.getInt("summonerId");
        this.teamId = object.getInt("teamId");
        this.cellId = object.getInt("cellId");
        this.object = object;
    }

    public AlliedMember getMember() {
        return member;
    }

    public int getChampionId() {
        return championId;
    }

    public int getTeamId() {
        return teamId;
    }

    public int getSummonerId() {
        return summonerId;
    }

    public int getCellId() {
        return cellId;
    }

    public int getCellIdNormalized() {
        return cellId % 5;
    }

    public String getNameVisibilityType() {
        return nameVisibilityType;
    }

    public void updateAlliedMember(AlliedMember member) {
        this.member = member;
        if (member.getChampionPickIntent() != 0) {
            ImageLoader.instance.load(String.format(preset, member.getChampionPickIntent())).whenComplete((image, e) -> {
                if (e != null) Logger.error(e);
                else {
                    this.image = Image.circleize(Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, 64, 64));
                    this.repaint();
                }
            });
        }
        if (championId != 0) {
            int fallbackId = championId * 1000;
            int skinId = member.getSkinId() == 0 ? fallbackId : member.getSkinId();
            ImageLoader.instance.load(String.format(splash, championId, skinId)).whenComplete((image, e) -> {
                if (e != null) {
                    ImageLoader.instance.load(String.format(splash, championId, fallbackId)).whenComplete((tmp, e1) -> {
                        if (tmp != null) {
                            this.skin = Scalr.crop(tmp, 300, 100, 680, 400);
                            this.prepare(this.skin);
                            this.repaint();
                        }
                    });
                } else {
                    this.skin = Scalr.crop(image, 300, 100, 680, 400);
                    this.prepare(this.skin);
                    this.repaint();
                }
            });
        }
        if (member.getSpell1Id() != 0) {
            ImageLoader.instance.load(SpellLoader.instance.getCache().get((long) member.getSpell1Id()).getIconPath()).whenComplete((image, e) -> {
                if (e != null) Logger.error(e);
                else {
                    this.summoner1 = Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, 28, 28);
                    this.repaint();
                }
            });
        }
        if (member.getSpell2Id() != 0) {
            ImageLoader.instance.load(SpellLoader.instance.getCache().get((long) member.getSpell2Id()).getIconPath()).whenComplete((image, e) -> {
                if (e != null) Logger.error(e);
                else {
                    this.summoner2 = Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, 28, 28);
                    this.repaint();
                }
            });
        }
    }

    private void prepare(BufferedImage skin) {
        Dimension dimension = getSize();
        this.decoration = filter.filter(Scalr.resize(skin, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, dimension.width), null);
        this.repaint();
    }

    public void update(int championId, boolean completed) {
        this.championId = championId;
        this.completed = completed;
        if (championId == 0) return;
        ImageLoader.instance.load(String.format(preset, this.championId)).whenComplete((image, e) -> {
            if (e != null) Logger.error(e);
            else {
                this.image = Image.circleize(Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, 64, 64));
                this.repaint();
            }
        });
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {

            Dimension dimension = getSize();
            int bar = 30;
            g.setColor(Color.DARK_GRAY.darker());
            g.fillRect(0, dimension.height - bar, dimension.width, bar);

            if (skin != null) g.drawImage(decoration, 0, 0, null);

            Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (member != null) paintMember(dimension, graphics2D, member.getHiddenName(), member.getPosition());

            int baseOffset = 10;
            int x = teamId == 1 ? baseOffset : dimension.width - baseOffset - 64;
            int y = (dimension.height >> 1) - (64 >> 1) - 15;

            int spellX = teamId == 1 ? x + baseOffset + 64 : x - baseOffset - 28;

            g.setColor(Color.BLACK);
            if (summoner1 != null) {
                g.fillRect(spellX - 1, y, summoner1.getWidth() + 2, summoner1.getHeight() + 2);
                g.drawImage(summoner1, spellX, 1 + y, null);
            }
            if (summoner2 != null) {
                g.fillRect(spellX - 1, -2 + y + 64 - 28, summoner2.getWidth() + 2, summoner2.getHeight() + 2);
                g.drawImage(summoner2, spellX, -1 + y + 64 - 28, null);
            }

            if (image == null) return;


            graphics2D.setColor(Color.BLACK);
            graphics2D.fill(new RoundRectangle2D.Float(x - 2, y - 2, image.getWidth() + 4, image.getHeight() + 4, 360, 360));

            g.drawImage(image, x, y, null);

            if (completed) return;

            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(opaque);
            graphics2D.fill(new RoundRectangle2D.Float(x - 2, y - 2, image.getWidth() + 4, image.getHeight() + 4, 360, 360));

        } catch (Exception e) {
            Logger.error(e);
        }
    }

    protected void paintMember(Dimension dimension, Graphics2D graphics2D, String name, String position) {
        graphics2D.setFont(font);
        graphics2D.setColor(Color.WHITE);
        Rectangle rectangle = new Rectangle(0, dimension.height - 30, dimension.width, 30);
        FontMetrics metrics = graphics2D.getFontMetrics();
        int y = (rectangle.y) + ((rectangle.height >> 1) + (metrics.getAscent() >> 1));
        graphics2D.drawString(name, 5, y);
        int width = metrics.stringWidth(position);
        graphics2D.drawString(position, rectangle.width - 5 - width, y);
    }
}
