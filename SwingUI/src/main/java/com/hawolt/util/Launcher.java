package com.hawolt.util;

import com.hawolt.LeagueClientUI;
import com.hawolt.generic.data.Platform;
import com.hawolt.logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Created: 10/08/2023 21:11
 * Author: Twitter @hawolt
 **/

public class Launcher {
    public static void launch(String ip, String port, String encryptionKey, String playerId, String gameId, Platform platform, String gameMode) {
        LeagueClientUI.service.execute(() -> {
            try {
                ProcessBuilder builder = new ProcessBuilder(
                        "C:\\Riot Games\\League of Legends\\Game\\League of Legends.exe",
                        String.format("%s %s %s %s", ip, port, encryptionKey, playerId),
                        "-Product=" + ("TFT".equals(gameMode) ? gameMode : "LoL"),
                        "-PlayerID=" + playerId,
                        "-GameID=" + gameId,
                        "-PlayerNameMode=SUMMONER",
                        "-GameBaseDir=C:\\Riot Games\\League of Legends",
                        "-Region=" + platform.getFriendlyName(),
                        "-PlatformID=" + platform.name(),
                        "-Locale=en_US",
                        "-SkipBuild",
                        "-EnableCrashpad=true",
                        "-EnableLNP",
                        "-UseDX11=1:1",
                        "-UseMetal=0:1",
                        "-UseNewX3D",
                        "-UseNewX3DFramebuffers",
                        "-RiotClientPort=42069",
                        "-RiotClientAuthToken=SwiftRiftOrNoRiftAtAll"
                );
                builder.directory(new File("C:\\Riot Games\\League of Legends\\Game"));
                builder.redirectErrorStream(true);
                Process process = builder.start();
                try (FileWriter writer = new FileWriter("log", false)) {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        String line;
                        while ((line = in.readLine()) != null) {
                            System.out.println(line);
                            writer.write(line + System.lineSeparator());
                        }
                    }
                }
                process.waitFor();
            } catch (Exception e) {
                Logger.error(e);
            }
        });
    }
}
