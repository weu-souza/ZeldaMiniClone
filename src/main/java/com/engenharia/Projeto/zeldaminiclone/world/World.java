package com.engenharia.Projeto.zeldaminiclone.world;

import com.engenharia.Projeto.zeldaminiclone.Game;
import com.engenharia.Projeto.zeldaminiclone.creatures.Enemies;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class World {
    public static ArrayList<Blocks> blocks = new ArrayList<>();
    public static int WIDTH, HEIGHT;


    public World(String path) {

        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(path);
            if (is == null) {
                throw new IllegalArgumentException("Mapa não encontrado: map.png");
            }
            BufferedImage map = ImageIO.read(is);
            int width = map.getWidth();
            int height = map.getHeight();
            WIDTH = map.getWidth();
            HEIGHT = map.getHeight();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int pixelColor = map.getRGB(x, y);
                    int red = (pixelColor >> 16) & 0xff;
                    int green = (pixelColor >> 8) & 0xff;
                    int blue = pixelColor & 0xff;
                    if (red == 0 && green == 0 && blue == 0) {
                        blocks.add(new Blocks(x * 16, y * 16, 0));
                    } else if (red == 224 && green == 243 && blue == 4) {
                        blocks.add(new Blocks(x * 16, y * 16, 1));
                    } else if (red == 4 && green == 16 && blue == 243) {
                        Game.player.x = x * 16;
                        Game.player.y = y * 16;
                        blocks.add(new Blocks(x * 16, y * 16, 1));
                    } else if (red == 245 && green == 9 && blue == 9) {
                        Game.enemies.add(new Enemies(x * 16, y * 16));
                        blocks.add(new Blocks(x * 16, y * 16, 1));
                    } else if (red == 42 && green == 47 && blue == 140) {
                        Game.npc.x = x * 16;
                        Game.npc.y = y * 16;
                        blocks.add(new Blocks(x * 16, y * 16, 1));
                    } else if (red == 255 && green == 255 && blue == 255) {
                        blocks.add(new Blocks(x * 16, y * 16, 2));
                    } else if (red == 4 && green == 243 && blue == 5) {
                        blocks.add(new Blocks(x * 16, y * 16, 3));
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

    public static boolean isFree(int xnext, int ynext) {
        Rectangle futurePlayer = new Rectangle(xnext, ynext, 16, 16);

        for (int i = 0; i < blocks.size(); i++) {
            Blocks bloco = blocks.get(i);
            if (bloco.type == 0 && futurePlayer.intersects(bloco)) { // verifica apenas colisão com paredes
                return false;
            } else if (bloco.type == 3 && futurePlayer.intersects(bloco)) { // verifica apenas colisão com paredes
                return false;
            }
        }
        return true;
    }
}
