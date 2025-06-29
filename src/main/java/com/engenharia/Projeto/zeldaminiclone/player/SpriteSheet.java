package com.engenharia.Projeto.zeldaminiclone.player;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import javax.imageio.ImageIO;

public class SpriteSheet {
    private BufferedImage spritesheet;
    public SpriteSheet(String path) {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(path);
            if (is == null) {
                throw new IllegalArgumentException("spritesheet não encontrado: " + path);
            }
            spritesheet = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public BufferedImage getSprite(int x, int y, int width, int height) {
        return spritesheet.getSubimage(x, y, width, height);
    }

}
