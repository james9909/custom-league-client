package com.hawolt.ui.champselect.sidebar;

import java.awt.image.BufferedImage;
import java.util.function.Function;

/**
 * Created: 18/08/2023 18:17
 * Author: Twitter @hawolt
 **/

public class ChampSelectMemberSprite {
    private final Function<BufferedImage, BufferedImage> function;
    private final String identifier;
    private BufferedImage image;
    private String resource;

    public ChampSelectMemberSprite(String identifier, Function<BufferedImage, BufferedImage> function) {
        this.identifier = identifier;
        this.function = function;
    }

    public Function<BufferedImage, BufferedImage> getFunction() {
        return function;
    }

    public String getIdentifier() {
        return identifier;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
