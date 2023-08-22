package com.hawolt.service;

import com.hawolt.StaticConstant;
import com.hawolt.logger.Logger;
import com.hawolt.objects.LocalSettings;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class LocalSettingsService implements FileService<LocalSettings> {

    private final Path path;

    private static final LocalSettingsService INSTANCE = new LocalSettingsService();

    private LocalSettingsService() {
        this.path = StaticConstant.LOCAL_SETTINGS_DIRECTORY.resolve(StaticConstant.LOCAL_SETTINGS_FILE);
    }

    @Override
    public void writeFile(LocalSettings content) {
        Logger.debug("Storing credentials at {}", path.toFile().getAbsolutePath());
        try {
            Files.createDirectories(StaticConstant.LOCAL_SETTINGS_DIRECTORY);
            Files.writeString(
                    path,
                    content.toString(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            Logger.fatal("Error handling LocalSettings");
            Logger.error(e);
        }
    }

    @Override
    public LocalSettings readFile() {
        try {
            JSONObject file = new JSONObject(new String(Files.readAllBytes(path)));
            String username = file.getString("username");
            String password = file.getString("password");
            boolean rememberMe = file.getBoolean("rememberMe");
            return new LocalSettings(username, password, rememberMe);
        } catch (IOException e) {
            Logger.warn("Failed to read LocalSettings at {}", this.path);
        }
        return null;
    }

    @Override
    public void deleteFile() {
        if (this.path.toFile().exists()) {
            try {
                Files.delete(this.path);
            } catch (IOException e) {
                Logger.warn("Failed to delete LocalSettings");
                Logger.error(e);
            }
        }
    }

    public static LocalSettingsService get() {
        return INSTANCE;
    }

}
