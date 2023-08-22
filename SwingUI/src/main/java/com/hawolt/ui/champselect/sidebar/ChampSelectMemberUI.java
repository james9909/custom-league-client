package com.hawolt.ui.champselect.sidebar;

import com.hawolt.async.loader.ResourceConsumer;
import com.hawolt.async.loader.ResourceLoader;
import com.hawolt.io.Core;
import com.hawolt.logger.Logger;
import com.hawolt.objects.Champion;
import com.hawolt.objects.Spell;
import com.hawolt.ui.champselect.AlliedMember;
import com.hawolt.ui.champselect.IChampSelection;
import com.hawolt.util.Image;
import com.hawolt.util.jhlab.GaussianFilter;
import org.imgscalr.Scalr;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
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
    private IChampSelection selection;

    public ChampSelectMemberUI(IChampSelection selection, JSONObject object) {
        super();
        this.selection = selection;
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

    private static Map<Long, Spell> cache = new HashMap<>();

    static {
        try {
            String resource = "https://raw.communitydragon.org/pbe/plugins/rcp-be-lol-game-data/global/default/v1/summoner-spells.json";
            HttpsURLConnection connection = (HttpsURLConnection) new URL(resource).openConnection();
            connection.setRequestProperty("User-Agent", "Sentinel");
            try (InputStream stream = connection.getInputStream()) {
                JSONArray array = new JSONArray(Core.read(stream).toString());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject reference = array.getJSONObject(i);
                    Spell spell = new Spell(reference);
                    cache.put(spell.getId(), spell);
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        }
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
            ResourceLoader.loadResource(sprite.getResource(), this);
        }
        if (member.getChampionPickIntent() != 0) {
            ChampSelectMemberSprite sprite = new ChampSelectMemberSprite(
                    "image",
                    image -> Image.circleize(Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, 64, 64))
            );
            sprite.setResource(String.format(preset, member.getChampionPickIntent()));
            sprites.put(sprite.getResource(), sprite);
            ResourceLoader.loadResource(sprite.getResource(), this);
            ChampSelectMemberSprite skin = new ChampSelectMemberSprite(
                    "skin",
                    image -> Scalr.crop(image, 300, 100, 680, 400)
            );
            skin.setResource(String.format(splash, member.getChampionPickIntent(), member.getChampionPickIntent() * 1000));
            sprites.put(skin.getResource(), skin);
            ResourceLoader.loadResource(skin.getResource(), this);
        }
        if (member.getSpell1Id() != 0) {
            ChampSelectMemberSprite sprite = new ChampSelectMemberSprite(
                    "summoner1",
                    image -> Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, 28, 28)
            );
            sprite.setResource(cache.get((long) member.getSpell1Id()).getIconPath());
            sprites.put(sprite.getResource(), sprite);
            ResourceLoader.loadResource(sprite.getResource(), this);
        }
        if (member.getSpell2Id() != 0) {
            ChampSelectMemberSprite sprite = new ChampSelectMemberSprite(
                    "summoner2",
                    image -> Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, 28, 28)
            );
            sprite.setResource(cache.get((long) member.getSpell2Id()).getIconPath());
            sprites.put(sprite.getResource(), sprite);
            ResourceLoader.loadResource(sprite.getResource(), this);
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
        ResourceLoader.loadResource(sprite.getResource(), this);
        ChampSelectMemberSprite skin = new ChampSelectMemberSprite(
                "skin",
                image -> Scalr.crop(image, 300, 100, 680, 400)
        );
        skin.setResource(String.format(splash, championId, championId * 1000));
        sprites.put(skin.getResource(), skin);
        ResourceLoader.loadResource(skin.getResource(), this);
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

            g.setColor(Color.BLACK);
            BufferedImage summoner1 = getSprite("summoner1");
            if (summoner1 != null) {
                paintSummonerSpell(graphics2D, summoner1, 0);
            }
            BufferedImage summoner2 = getSprite("summoner2");
            if (summoner2 != null) {
                paintSummonerSpell(graphics2D, summoner2, 1);
            }
            if (decoration == null || completed) return;
            graphics2D.setColor(opaque);
            graphics2D.fillRect(0, 0, dimension.width, dimension.height);
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    protected void paintSummonerSpell(Graphics2D graphics2D, BufferedImage summoner, int index) {
        Dimension dimension = getSize();
        int baseOffset = 5;
        int factor = (teamId == 1 ? 28 + baseOffset : 0) + (index == 0 ? 0 : ((baseOffset + 28) * (teamId == 1 ? -1 : 1)));
        int x = teamId == 1 ? dimension.width - baseOffset - 28 - factor : baseOffset + factor;
        int y = baseOffset;
        graphics2D.fillRect(x - 1, y - 1, summoner.getWidth() + 2, summoner.getHeight() + 2);
        graphics2D.drawImage(summoner, x, y, null);
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
        Champion champion = selection.getChampionCache().get(championId);
        if (champion == null) return;
        Dimension size = getSize();
        graphics2D.drawString(champion.getName(), teamId == 1 ? 5 : size.width - 5 - metrics.stringWidth(champion.getName()), 5 + metrics.getAscent());
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
