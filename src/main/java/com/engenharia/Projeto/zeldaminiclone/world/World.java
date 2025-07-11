package com.engenharia.Projeto.zeldaminiclone.world;

import com.engenharia.Projeto.zeldaminiclone.Game;
import com.engenharia.Projeto.zeldaminiclone.colectables.Collectables;
import com.engenharia.Projeto.zeldaminiclone.creatures.Enemies;
import com.engenharia.Projeto.zeldaminiclone.creatures.boss.Boss;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import static com.engenharia.Projeto.zeldaminiclone.Game.*;

public class World {
    public static ArrayList<Blocks> blocks = new ArrayList<>();
    public static ArrayList<Blocks3d> blocks3d = new ArrayList<>();
    public static int WIDTH, HEIGHT;
    public static ArrayList<Collectables> coin = new ArrayList<>();

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
                    } else if (red == 243 && green == 4 && blue == 231) {
                        if (portal != null) {
                            Game.portal.x = x * 16;
                            Game.portal.y = y * 16;
                            if (Game.currentMap == 1 || Game.currentMap == 2) {
                                blocks.add(new Blocks(x * 16, y * 16, 1));
                            } else {
                                blocks.add(new Blocks(x * 16, y * 16, 2));
                            }
                        }
                    } else if (red == 245 && green == 9 && blue == 9) {
                        enemies.add(new Enemies(x * 16, y * 16));
                        blocks.add(new Blocks(x * 16, y * 16, 1));
                    } else if (red == 42 && green == 47 && blue == 140) {
                        if (Game.npc != null) {
                            Game.npc.x = x * 16;
                            Game.npc.y = y * 16;
                            if (Game.currentMap == 1 || Game.currentMap == 2) {
                                blocks.add(new Blocks(x * 16, y * 16, 1));
                            } else if (Game.currentMap == 3) {
                                blocks.add(new Blocks(x * 16, y * 16, 2));
                            }
                        }

                    } else if (red == 4 && green == 16 && blue == 243) {
                        Game.player.x = x * 16;
                        Game.player.y = y * 16;
                        Game.player.spawnX = Game.player.x;
                        Game.player.spawnY = Game.player.y;

                        if (Game.currentMap == 1 || Game.currentMap == 2) {
                            blocks.add(new Blocks(x * 16, y * 16, 1));
                        } else {
                            blocks.add(new Blocks(x * 16, y * 16, 2));
                        }

                    } else if (red == 196 && green == 22 && blue == 22) {
                        Boss newBoss = new Boss(x * 16, y * 16);
                        Game.boss = newBoss;
                        Game.enemies.add(newBoss);

                        if (Game.currentMap == 1 || Game.currentMap == 2) {
                            blocks.add(new Blocks(x * 16, y * 16, 1));
                        } else {
                            blocks.add(new Blocks(x * 16, y * 16, 2));
                        }

                    } else if (red == 255 && green == 255 && blue == 255) {
                        blocks.add(new Blocks(x * 16, y * 16, 2));
                    } else if (red == 4 && green == 243 && blue == 5) {
                        blocks3d.add(new Blocks3d(x * 16, y * 16, 0));
                        blocks.add(new Blocks(x * 16, y * 16, 2));
                    } else if (red == 121 && green == 112 && blue == 122) {
                        blocks3d.add(new Blocks3d(x * 16, y * 16, 1));
                        blocks.add(new Blocks(x * 16, y * 16, 2));
                    } else if (red == 219 && green == 4 && blue == 243) {
                        Game.coin.add(new Collectables(x * 16, y * 16));
                        blocks.add(new Blocks(x * 16, y * 16, 1));
                    } else if (red == 22 && green == 207 && blue == 21) {
                        blocks.add(new Blocks(x * 16, y * 16, 4));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearWorld() {
        blocks.clear();
        enemies.clear();
        blocks3d.clear();
        coin.clear();
        if (currentMap != 1 && currentMap != 2) {
            Game.npc = null;
        }
    }

    public void render(Graphics g) {
        for (int i = 0; i < blocks.size(); i++) {
            blocks.get(i).render(g);
        }
        for (int i = 0; i < blocks3d.size(); i++) {
            blocks3d.get(i).render(g);
        }
    }

    public static boolean isFree(int xnext, int ynext) {
        Rectangle futurePlayer = new Rectangle(xnext, ynext, 16, 16);

        // Verifica colisão com blocos (paredes e grama)
        for (Blocks bloco : blocks) {
            if ((bloco.type == 0) && futurePlayer.intersects(bloco)) {
                return false;
            }
        }

        for (Blocks3d b3d : blocks3d) {
            if ((b3d.type == 0 || b3d.type == 1 || b3d.type == 2) && futurePlayer.intersects(b3d)) {
                return false;
            }
        }

        if (Game.boss != null) {
            Rectangle bossBounds = new Rectangle(Game.boss.x, Game.boss.y, 16, 16);
            if (futurePlayer.intersects(bossBounds)) {
                return false;
            }
        }
        // Verifica colisão com NPC
        if (Game.npc != null) {
            Rectangle npcBounds = new Rectangle(Game.npc.x, Game.npc.y, 16, 16);
            if (futurePlayer.intersects(npcBounds)) {
                return false;
            }
        }

        // Verifica colisão com inimigos
        for (Enemies enemy : enemies) {
            Rectangle enemyBounds = new Rectangle(enemy.x, enemy.y, 16, 16);
            if (futurePlayer.intersects(enemyBounds)) {
                return true;
            }
        }

        return true; // Nenhuma colisão detectada
    }


}
