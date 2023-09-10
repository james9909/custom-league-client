package com.hawolt.ui.champselect.impl.draft;

import com.hawolt.async.ExecutorManager;
import com.hawolt.ui.champselect.context.ChampSelectSettingsContext;
import com.hawolt.ui.champselect.context.ChampSelectUtilityContext;
import com.hawolt.ui.champselect.data.ActionObject;
import com.hawolt.ui.champselect.generic.ChampSelectUIComponent;
import com.hawolt.ui.champselect.data.ChampSelectPhase;
import com.hawolt.ui.champselect.generic.impl.ChampSelectHeaderUI;
import com.hawolt.util.ColorPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created: 31/08/2023 17:17
 * Author: Twitter @hawolt
 **/

public class DraftChampSelectHeaderUI extends ChampSelectHeaderUI {

    public DraftChampSelectHeaderUI() {
        super();
    }

    @Override
    protected String getPhaseDescription(ChampSelectPhase phase) {
        return switch (phase) {
            case PLAN -> "DECLARE YOUR CHAMPION";
            case IDLE -> "WAITING FOR OTHERS";
            case BAN -> "BAN A CHAMPION";
            case PICK -> "SELECT YOUR CHAMPION";
            case FINALIZE -> "PREPARE FOR BATTLE";
        };
    }

    @Override
    protected ChampSelectPhase getChampSelectPhase() {
        ChampSelectUtilityContext utilityContext = context.getChampSelectUtilityContext();
        if (utilityContext.isFinalizing()) {
            return ChampSelectPhase.FINALIZE;
        } else {
            return switch (settingsContext.getCurrentActionSetIndex()) {
                case -1 -> ChampSelectPhase.PLAN;
                case 0 -> {
                    Optional<ActionObject> optional = utilityContext.getOwnBanPhase();
                    if (optional.isPresent()) {
                        ActionObject object = optional.get();
                        yield object.isCompleted() ? ChampSelectPhase.PLAN : ChampSelectPhase.BAN;
                    } else {
                        yield ChampSelectPhase.PICK;
                    }
                }
                default -> {
                    boolean pick = settingsContext.getActionSetMapping().get(settingsContext.getCurrentActionSetIndex())
                            .stream()
                            .anyMatch(o -> o.getActorCellId() == settingsContext.getLocalPlayerCellId() && !o.isCompleted());
                    yield pick ? ChampSelectPhase.PICK : ChampSelectPhase.IDLE;
                }
            };
        }
    }
}
