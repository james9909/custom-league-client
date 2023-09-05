package com.hawolt.util.os;

import com.hawolt.io.Core;
import com.hawolt.logger.Logger;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created: 05/09/2023 15:42
 * Author: Twitter @hawolt
 **/

public class WMIC {
    private static String wmic() throws IOException {
        ProcessBuilder builder = new ProcessBuilder("WMIC", "path", "win32_process", "get", "Caption,Processid,Commandline");
        builder.redirectErrorStream(true);
        Process process = builder.start();
        try (InputStream stream = process.getInputStream()) {
            return Core.read(stream).toString();
        }
    }

    public static boolean isProcessRunning(String name) {
        try {
            String[] list = wmic().split("\n");
            for (String process : list) {
                if (process.trim().startsWith(name)) return true;
            }
        } catch (IOException e) {
            Logger.error("Failed to retrieve process list using wmic");
        }
        return false;
    }
}
