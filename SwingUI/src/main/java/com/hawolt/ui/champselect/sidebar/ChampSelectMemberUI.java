package com.hawolt.ui.champselect.sidebar;

import com.hawolt.async.loader.ResourceConsumer;
import com.hawolt.async.loader.ResourceLoader;
import com.hawolt.async.loader.impl.SpellLoader;
import com.hawolt.logger.Logger;
import com.hawolt.ui.champselect.AlliedMember;
import com.hawolt.util.Image;
import com.hawolt.util.jhlab.GaussianFilter;
import org.imgscalr.Scalr;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created: 06/08/2023 14:14
 * Author: Twitter @hawolt
 **/

public class ChampSelectMemberUI extends ChampSelectBlankMemberUI implements ResourceConsumer<BufferedImage, byte[]> {
    protected static final Font font = new Font("Arial", Font.PLAIN, 16);
    private final static String splash = "https://raw.communitydragon.org/pbe/plugins/rcp-be-lol-game-data/global/default/v1/champion-splashes/%s/%s.jpg";
    private final static String preset = "https://raw.communitydragon.org/pbe/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/%s.png";

    private final static Color opaque = new Color(255, 255, 255, 50);
    private final static GaussianFilter filter = new GaussianFilter(5);

    private final Map<String, ChampSelectMemberSprite> sprites = new HashMap<>();
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
            ChampSelectMemberSprite sprite = new ChampSelectMemberSprite(
                    "image",
                    image -> Image.circleize(Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, 64, 64))
            );
            sprite.setResource(String.format(preset, member.getChampionPickIntent()));
            sprites.put(sprite.getResource(), sprite);
            ResourceLoader.load(sprite.getResource(), this);
        }
        if (championId != 0) {
            int fallbackId = championId * 1000;
            int skinId = member.getSkinId() == 0 ? fallbackId : member.getSkinId();
            ChampSelectMemberSprite sprite = new ChampSelectMemberSprite(
                    "skin",
                    image -> {
                        if (image != null) return Scalr.crop(image, 300, 100, 680, 400);
                        else {
                            ChampSelectMemberSprite failure = sprites.get(String.format(splash, championId, skinId));
                            failure.setResource(String.format(splash, championId, fallbackId));
                            sprites.put(failure.getResource(), failure);
                            ResourceLoader.load(failure.getResource(), this);
                        }
                        return null;
                    }
            );
            sprite.setResource(String.format(splash, championId, skinId));
            sprites.put(sprite.getResource(), sprite);
            ResourceLoader.load(sprite.getResource(), this);
        }
        if (member.getSpell1Id() != 0) {
            ChampSelectMemberSprite sprite = new ChampSelectMemberSprite(
                    "summoner1",
                    image -> Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, 28, 28)
            );
            sprite.setResource(SpellLoader.instance.getCache().get((long) member.getSpell1Id()).getIconPath());
            sprites.put(sprite.getResource(), sprite);
            ResourceLoader.load(sprite.getResource(), this);
        }
        if (member.getSpell2Id() != 0) {
            ChampSelectMemberSprite sprite = new ChampSelectMemberSprite(
                    "summoner2",
                    image -> Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, 28, 28)
            );
            sprite.setResource(SpellLoader.instance.getCache().get((long) member.getSpell2Id()).getIconPath());
            sprites.put(sprite.getResource(), sprite);
            ResourceLoader.load(sprite.getResource(), this);
        }
    }

    public void updateChampSelection(int championId) {
        if (this.championId == championId) return;
        this.championId = championId;
        if (championId == 0) return;
        ChampSelectMemberSprite sprite = new ChampSelectMemberSprite(
                "image",
                image -> Image.circleize(Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, 64, 64))
        );
        sprite.setResource(String.format(preset, this.championId));
        sprites.put(sprite.getResource(), sprite);
        ResourceLoader.load(sprite.getResource(), this);
        ChampSelectMemberSprite skin = new ChampSelectMemberSprite(
                "skin",
                image -> Scalr.crop(image, 300, 100, 680, 400)
        );
        skin.setResource(String.format(splash, championId, championId * 1000));
        sprites.put(skin.getResource(), skin);
        ResourceLoader.load(skin.getResource(), this);
    }

    public void update(int championId, boolean completed) {
        this.updateChampSelection(championId);
        this.completed = completed;
    }

    public BufferedImage getSprite(String name) {
        ChampSelectMemberSprite sprite = sprites.get(name);
        return sprite != null ? sprite.getImage() : null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {

            Dimension dimension = getSize();
            int bar = 30;
            g.setColor(Color.DARK_GRAY.darker());
            g.fillRect(0, dimension.height - bar, dimension.width, bar);

            BufferedImage decoration = getSprite("decoration");
            if (decoration != null) g.drawImage(decoration, 0, 0, null);

            Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (member != null) paintMember(dimension, graphics2D, member.getHiddenName(), member.getPosition());

            int baseOffset = 10;
            int x = teamId == 1 ? baseOffset : dimension.width - baseOffset - 64;
            int y = (dimension.height >> 1) - (64 >> 1) - 15;

            int spellX = teamId == 1 ? x + baseOffset + 64 : x - baseOffset - 28;

            g.setColor(Color.BLACK);
            BufferedImage summoner1 = getSprite("summoner1");
            if (summoner1 != null) {
                g.fillRect(spellX - 1, y, summoner1.getWidth() + 2, summoner1.getHeight() + 2);
                g.drawImage(summoner1, spellX, 1 + y, null);
            }

            BufferedImage summoner2 = getSprite("summoner2");
            if (summoner2 != null) {
                g.fillRect(spellX - 1, -2 + y + 64 - 28, summoner2.getWidth() + 2, summoner2.getHeight() + 2);
                g.drawImage(summoner2, spellX, -1 + y + 64 - 28, null);
            }

            BufferedImage image = getSprite("image");
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

    @Override
    public void onException(Object o, Exception e) {
        Logger.fatal("Failed to load resource {}", o);
        Logger.error(e);
    }

    @Override
    public void consume(Object o, BufferedImage bufferedImage) {
        ChampSelectMemberSprite sprite = sprites.get(o.toString());
        BufferedImage image = sprite.getFunction().apply(bufferedImage);
        sprite.setImage(image);
        if ("skin".equalsIgnoreCase(sprite.getIdentifier())) {
            Logger.info("{} {} {}", sprite.getIdentifier(), o, bufferedImage == null);
            Dimension dimension = getSize();
            ChampSelectMemberSprite decoration = new ChampSelectMemberSprite(
                    "decoration",
                    img -> filter.filter(Scalr.resize(img, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, dimension.width), null)
            );
            decoration.setImage(decoration.getFunction().apply(image));
            sprites.put("decoration", decoration);
        } else {
            sprites.put(sprite.getIdentifier(), sprite);
        }
        repaint();
    }

    @Override
    public BufferedImage transform(byte[] bytes) throws Exception {
        return ImageIO.read(new ByteArrayInputStream(bytes));
    }
}
