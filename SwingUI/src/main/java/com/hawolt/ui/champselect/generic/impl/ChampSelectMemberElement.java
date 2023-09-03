package com.hawolt.ui.champselect.generic.impl;

import com.hawolt.async.loader.ResourceManager;
import com.hawolt.client.LeagueClient;
import com.hawolt.client.resources.communitydragon.DataTypeConverter;
import com.hawolt.client.resources.communitydragon.champion.ChampionIndex;
import com.hawolt.client.resources.communitydragon.champion.ChampionSource;
import com.hawolt.client.resources.communitydragon.skin.SkinIndex;
import com.hawolt.client.resources.communitydragon.skin.SkinSource;
import com.hawolt.client.resources.communitydragon.spell.SpellIndex;
import com.hawolt.client.resources.communitydragon.spell.SpellSource;
import com.hawolt.client.resources.ledge.summoner.SummonerLedge;
import com.hawolt.logger.Logger;
import com.hawolt.ui.champselect.ChampSelectContext;
import com.hawolt.ui.champselect.data.ChampSelectTeam;
import com.hawolt.ui.champselect.data.ChampSelectTeamType;
import com.hawolt.ui.champselect.generic.ChampSelectUIComponent;
import com.hawolt.ui.champselect.util.ChampSelectMember;
import com.hawolt.ui.champselect.util.ChampSelectTeamMember;
import com.hawolt.ui.impl.Debouncer;
import com.hawolt.util.ColorPalette;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Created: 30/08/2023 17:03
 * Author: Twitter @hawolt
 **/

public class ChampSelectMemberElement extends ChampSelectUIComponent implements Runnable, DataTypeConverter<byte[], BufferedImage> {
    private static final String SPRITE_PATH = "https://raw.communitydragon.org/pbe/plugins/rcp-be-lol-game-data/global/default/v1/champion-splashes/%s/%s.jpg";
    private static final Dimension SUMMONER_SPELL_DIMENSION = new Dimension(28, 28);
    private final ResourceManager<BufferedImage> manager = new ResourceManager<>(this);
    private final ChampSelectTeamType teamType;
    private final ChampSelectTeam team;

    private int championId, indicatorId, skinId, spell1Id, spell2Id;
    private BufferedImage sprite, spellOne, spellTwo, clearChampion;
    private ChampSelectMember member;
    private String hero, name;

    @Override
    public void run() {
        if (!(member instanceof ChampSelectTeamMember teamMember)) return;
        LeagueClient client = context.getLeagueClient();
        if (client == null) return;
        SummonerLedge summonerLedge = client.getLedge().getSummoner();
        this.name = String.valueOf(teamMember.getSummonerId());
        try {
            this.name = summonerLedge.resolveSummonerByPUUD(teamMember.getPUUID()).getName();
        } catch (IOException e) {
            Logger.error("Failed to retrieve name for {}", teamMember.getPUUID());
        }
        switch (teamMember.getNameVisibilityType()) {
            case "UNHIDDEN" -> context.cache(teamMember.getPUUID(), String.format("%s (%s)", name, getHiddenName()));
            case "HIDDEN" -> context.cache(teamMember.getPUUID(), getHiddenName());
            case "VISIBLE" -> context.cache(teamMember.getPUUID(), name);
        }
        this.repaint();
    }

    private class ChampSelectSelectMemberResizeAdapter extends ComponentAdapter {
        private static final Debouncer debouncer = new Debouncer();

        @Override
        public void componentResized(ComponentEvent e) {
            if (sprite == null) return;
            debouncer.debounce(
                    String.valueOf(member.getCellId()),
                    ChampSelectMemberElement.this::adjust,
                    200, TimeUnit.MILLISECONDS
            );
        }
    }

    private void adjust() {
        Dimension dimension = getSize();
        clearChampion = Scalr.resize(sprite, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, dimension.width);
        repaint();
    }

    public ChampSelectMemberElement(ChampSelectTeamType teamType, ChampSelectTeam team, ChampSelectMember member) {
        this.addComponentListener(new ChampSelectSelectMemberResizeAdapter());
        this.setBackground(ColorPalette.BACKGROUND_COLOR);
        this.teamType = teamType;
        this.member = member;
        this.team = team;
    }

    private void setClearChampion(int championId) {
        ChampionIndex championIndex = ChampionSource.CHAMPION_SOURCE_INSTANCE.get();
        this.hero = championIndex.getChampion(championId).getName();
    }

    private void setChampionId(int championId) {
        this.championId = championId;
        this.setClearChampion(championId);
        this.updateSprite(championId, championId * 1000);
    }

    private void updateChamp(int championId) {
        if (championId == 0) return;
        setChampionId(championId);
    }

    private void updateChamp(ChampSelectMember member) {
        if (this.championId == member.getChampionId()) return;
        if (member.getChampionId() == 0) return;
        setChampionId(member.getChampionId());
    }

    private void updateChampIndicator(ChampSelectTeamMember member) {
        if (this.indicatorId == member.getChampionPickIntent()) return;
        this.indicatorId = member.getChampionPickIntent();
        if (indicatorId != 0) {
            this.setClearChampion(indicatorId);
            this.updateSprite(indicatorId, indicatorId * 1000);
        } else if (member.getChampionId() == 0 && !isPicking()) {
            this.clearChampion = null;
            this.sprite = null;
        }
    }

    //
    private void updateSkin(ChampSelectTeamMember member) {
        if (this.skinId == member.getSkinId()) return;
        if (member.getSkinId() != 0) {
            this.skinId = member.getSkinId();
            SkinIndex skinIndex = SkinSource.SKIN_SOURCE_INSTANCE.get();
            this.updateSprite(member.getChampionId(), skinIndex.getSkin(member.getSkinId()).getId());
        }
    }

    private void updateSprite(int championId, int skinId) {
        manager.load(
                String.format(
                        SPRITE_PATH,
                        championId,
                        skinId
                ),
                bufferedImage -> {
                    this.sprite = Scalr.crop(bufferedImage, 300, 50, 640, 400);
                    this.adjust();
                }
        );
    }

    private void updateSpellOne(ChampSelectTeamMember member) {
        if (this.spell1Id == member.getSpell1Id()) return;
        if (member.getSpell1Id() != 0) {
            this.spell1Id = member.getSpell1Id();
            SpellIndex spellIndex = SpellSource.SPELL_SOURCE_INSTANCE.get();
            manager.load(
                    spellIndex.getSpell(spell1Id).getIconPath(),
                    bufferedImage -> {
                        ChampSelectMemberElement.this.spellOne = Scalr.resize(
                                bufferedImage,
                                Scalr.Method.ULTRA_QUALITY,
                                Scalr.Mode.FIT_TO_HEIGHT,
                                SUMMONER_SPELL_DIMENSION.width,
                                SUMMONER_SPELL_DIMENSION.height
                        );
                    }
            );
        }
    }

    private void updateSpellTwo(ChampSelectTeamMember member) {
        if (this.spell2Id == member.getSpell2Id()) return;
        if (member.getSpell2Id() != 0) {
            this.spell2Id = member.getSpell2Id();
            SpellIndex spellIndex = SpellSource.SPELL_SOURCE_INSTANCE.get();
            manager.load(
                    spellIndex.getSpell(spell2Id).getIconPath(),
                    bufferedImage -> {
                        ChampSelectMemberElement.this.spellTwo = Scalr.resize(
                                bufferedImage,
                                Scalr.Method.ULTRA_QUALITY,
                                Scalr.Mode.FIT_TO_HEIGHT,
                                SUMMONER_SPELL_DIMENSION.width,
                                SUMMONER_SPELL_DIMENSION.height
                        );
                    }
            );
        }
    }

    public void update(ChampSelectMember member) {
        this.member = member;
        this.updateChamp(member);
        if (teamType == ChampSelectTeamType.ALLIED) {
            ChampSelectTeamMember teamMember = (ChampSelectTeamMember) member;
            this.updateChampIndicator(teamMember);
            this.updateSpellOne(teamMember);
            this.updateSpellTwo(teamMember);
            this.updateSkin(teamMember);
            if (context != null && isPicking()) {
                context.getCurrent()
                        .stream()
                        .filter(actionObject -> actionObject.getActorCellId() == member.getCellId())
                        .findAny()
                        .ifPresent(actionObject -> {
                            if (context.getCurrentActionSetIndex() > 0) {
                                updateChamp(actionObject.getChampionId());
                            }
                        });
            }
        }
        this.repaint();
    }

    private static final Color HIGHLIGHTER = new Color(0xFF, 0xFF, 0xFF, 0x4F);

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (context == null) return;

        Dimension dimension = getSize();

        Color color = isPicking() ? Color.PINK : Color.ORANGE;
        g.setColor(isSelf() ? color.brighter() : color);

        //DRAW CHAMPION/SKIN IMAGE
        if (clearChampion != null) {
            int imageX = (dimension.width >> 1) - (clearChampion.getWidth() >> 1);
            int imageY = (dimension.height >> 1) - (clearChampion.getHeight() >> 1);
            g.drawImage(clearChampion, imageX, imageY, null);
        }

        //INDICATE STATUS NOT LOCKED IN
        if (!isLockedIn() || context.getCurrentActionSetIndex() <= 0) {
            g.setColor(HIGHLIGHTER);
            g.fillRect(0, 0, dimension.width, dimension.height);
        }

        //INDICATOR TO SHOW WHO WE ARE
        if (isSelf()) {
            int baseX = switch (team) {
                case BLUE -> dimension.width - (SUMMONER_SPELL_DIMENSION.width * 3) - (5 * 3);
                case PURPLE -> 5 + (SUMMONER_SPELL_DIMENSION.width << 1) + (5 << 1);
            };
            g.setColor(Color.BLACK);
            g.fillRect(baseX - 1, 5 - 1, SUMMONER_SPELL_DIMENSION.width + 2, SUMMONER_SPELL_DIMENSION.height + 2);
            g.setColor(!isPicking() ? Color.RED : Color.GREEN);
            g.fillRect(baseX, 5, SUMMONER_SPELL_DIMENSION.width, SUMMONER_SPELL_DIMENSION.height);
        }

        //DRAW SUMMONER SPELLS
        if (spellOne != null && spellTwo != null) paintSummonerSpells(g);

        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setFont(new Font("Dialog", Font.BOLD, 20));
        graphics2D.setColor(Color.BLACK);
        FontMetrics metrics = graphics2D.getFontMetrics();

        //DRAW ROLES
        if (teamType == ChampSelectTeamType.ALLIED) {
            ChampSelectTeamMember teamMember = (ChampSelectTeamMember) member;

            String position = !"UTILITY".equals(teamMember.getAssignedPosition()) ? teamMember.getAssignedPosition() : "SUPPORT";
            int positionWidth = metrics.stringWidth(position);
            int positionX = switch (team) {
                case BLUE -> 5;
                case PURPLE -> dimension.width - 5 - positionWidth;
            };
            drawTextWithShadow(graphics2D, position, positionX, 5 + metrics.getAscent());

            int guidelineY = dimension.height - 7;
            int baseY = switch (member.getNameVisibilityType()) {
                case "UNHIDDEN" -> guidelineY - metrics.getAscent() - 7;
                default -> guidelineY;
            };
            String name = switch (member.getNameVisibilityType()) {
                case "HIDDEN", "UNHIDDEN" -> getHiddenName();
                default -> String.valueOf(this.name != null ? this.name : getHiddenName());
            };
            int hiddenNameX = switch (team) {
                case BLUE -> dimension.width - 5 - metrics.stringWidth(name);
                case PURPLE -> 5;
            };
            drawTextWithShadow(graphics2D, name, hiddenNameX, baseY);
            if (baseY != guidelineY && this.name != null) {
                int visibleNameX = switch (team) {
                    case BLUE -> dimension.width - 5 - metrics.stringWidth(this.name);
                    case PURPLE -> 5;
                };
                drawTextWithShadow(graphics2D, this.name, visibleNameX, guidelineY);
            }
        }

        //DRAW CHAMPION NAME
        if (hero != null) {
            int heroX = switch (team) {
                case BLUE -> 5;
                case PURPLE -> dimension.width - 5 - metrics.stringWidth(hero);
            };
            drawTextWithShadow(graphics2D, hero, heroX, dimension.height - 7);
        }


        g.setColor(Color.BLACK);
        g.drawRect(0, 0, dimension.width - 1, dimension.height - 1);
    }

    public String getHiddenName() {
        return switch (member.getCellId() % 5) {
            case 0 -> "Raptor";
            case 1 -> "Krug";
            case 2 -> "Murk Wolf";
            case 3 -> "Gromp";
            default -> "Scuttle Crab";
        };
    }

    private void drawTextWithShadow(Graphics2D graphics2D, String text, int x, int y) {
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawString(text, x + 1, y + 1);
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(text, x, y);
    }

    private void paintSummonerSpells(Graphics g) {
        Dimension dimension = getSize();
        g.setColor(Color.BLACK);
        switch (team) {
            case BLUE -> {
                int baseX = dimension.width - 5 - SUMMONER_SPELL_DIMENSION.width;
                g.drawImage(spellTwo, baseX, 5, null);
                g.drawRect(baseX, 5, SUMMONER_SPELL_DIMENSION.width, SUMMONER_SPELL_DIMENSION.height);
                g.drawImage(spellOne, baseX - 5 - SUMMONER_SPELL_DIMENSION.width, 5, null);
                g.drawRect(baseX - 5 - SUMMONER_SPELL_DIMENSION.width, 5, SUMMONER_SPELL_DIMENSION.width, SUMMONER_SPELL_DIMENSION.height);
            }
            case PURPLE -> {
                int baseX = 5;
                g.drawImage(spellOne, baseX, 5, null);
                g.drawRect(baseX, 5, SUMMONER_SPELL_DIMENSION.width, SUMMONER_SPELL_DIMENSION.height);
                g.drawImage(spellTwo, baseX + SUMMONER_SPELL_DIMENSION.width + 5, 5, null);
                g.drawRect(baseX + SUMMONER_SPELL_DIMENSION.width + 5, 5, SUMMONER_SPELL_DIMENSION.width, SUMMONER_SPELL_DIMENSION.height);
            }
        }
    }


    private boolean isSelf() {
        return member.getCellId() == context.getLocalPlayerCellId();
    }

    private boolean isLockedIn() {
        if (context.isFinalizing()) return true;
        if (context == null || context.getCurrentActionSetIndex() < 0) return false;
        return context.getActionSetMapping().values()
                .stream()
                .skip(1)
                .flatMap(Collection::stream)
                .anyMatch(actionObject -> actionObject.getActorCellId() == member.getCellId() && actionObject.isCompleted());
    }

    private boolean isPicking() {
        if (context.isFinalizing()) return false;
        if (context == null || context.getCurrentActionSetIndex() < 0) return false;
        return context.getActionSetMapping().get(context.getCurrentActionSetIndex())
                .stream()
                .anyMatch(actionObject -> actionObject.getActorCellId() == member.getCellId() && !actionObject.isCompleted());
    }

    public void setIndex(ChampSelectContext index) {
        this.context = index;
    }

    @Override
    public BufferedImage apply(byte[] b) throws Exception {
        return ImageIO.read(new ByteArrayInputStream(b));
    }
}
