package com.engenharia.Projeto.zeldaminiclone;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
import javax.imageio.ImageIO;
public class World {
    public static ArrayList<Blocks> blocks = new ArrayList<>();


    public World(String path) {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(path);
            if (is == null) {
                throw new IllegalArgumentException("Mapa não encontrado: map.png");
            }
            BufferedImage map = ImageIO.read(is);
            int width = map.getWidth();
            int height = map.getHeight();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int pixelColor = map.getRGB(x, y);
                    int red = (pixelColor >> 16) & 0xff;
                    int green = (pixelColor >> 8) & 0xff;
                    int blue = pixelColor & 0xff;
                    if (red == 0 && green == 0 && blue == 0) {
                        blocks.add(new Blocks(x * 16, y * 16));
                    } else if (red == 0 && green == 0 && blue == 255) {
                        Game.player.x = x * 16;
                        Game.player.y = y * 16;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void render(Graphics g) {
        for (int i = 0; i < blocks.size(); i++) {
            blocks.get(i).render(g);
        }
    }
}
