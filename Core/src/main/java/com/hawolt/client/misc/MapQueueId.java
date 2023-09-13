package com.hawolt.client.misc;

public class MapQueueId {
    public static String getGameMode(int queueId) {
        return switch (queueId) {
            case 400, 430, 420, 440, 830 -> "CLASSIC";
            case 1090, 1100, 1130, 1160 -> "TFT";
            case 450 -> "ARAM";
            default -> "UNKNOWN";
        };
    }

    public static String getGameQueueType(int queueId) {
        return switch (queueId) {
            case 400, 430 -> "NORMAL";
            case 420 -> "RANKED_SOLO_5x5";
            case 440 -> "RANKED_FLEX_SR";
            case 450 -> "ARAM_UNRANKED_5x5";
            case 830, 840, 850 -> "BOT";
            case 1090 -> "NORMAL_TFT";
            case 1100 -> "RANKED_TFT";
            case 1130 -> "RANKED_TFT_TURBO";
            case 1160 -> "RANKED_TFT_DOUBLE_UP";
            default -> "UNKNOWN";
        };
    }

    public static String getMapId(int queueId) {
        return switch (queueId) {
            case 400, 430, 420, 440, 830, 840, 850 -> "11";
            case 450 -> "12";
            case 1090, 1100, 1130, 1160 -> "22";
            default -> "-1";
        };
    }

}
