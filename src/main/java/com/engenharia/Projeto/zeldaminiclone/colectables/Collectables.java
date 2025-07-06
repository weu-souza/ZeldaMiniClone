package com.engenharia.Projeto.zeldaminiclone.colectables;

import com.engenharia.Projeto.zeldaminiclone.player.Camera;
import com.engenharia.Projeto.zeldaminiclone.player.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Collectables {

    public int x;
    public int y;
    private BufferedImage[] sprites;
    private int currentFrame = 0;
    private int maxFrames = 14;
    private int frameDelay = 10; // quantos ticks cada frame fica
    private int frameDelayCounter = 0;

    public Collectables(int x, int y) {
        this.x = x;
        this.y = y;
        sprites = new BufferedImage[maxFrames]; // Inicializa o array!
        loadSprites();
    }

    private void loadSprites() {
        SpriteSheet sheet = new SpriteSheet("colectables/coin.png");

        for (int i = 0; i < maxFrames; i++) {
            sprites[i] = sheet.getSprite(i * 16, 0, 16, 16);
        }
    }

    public void tick() {
        frameDelayCounter++;
        if (frameDelayCounter >= frameDelay) {
            currentFrame++;
            if (currentFrame >= maxFrames) {
                currentFrame = 0;
            }
            frameDelayCounter = 0;
        }
    }

    public boolean playerCollides(int playerX, int playerY) {
        Rectangle playerRect = new Rectangle(playerX, playerY, 16, 16);
        Rectangle portalRect = new Rectangle(x, y, 16, 16);
        return playerRect.intersects(portalRect);
    }


    public void render(java.awt.Graphics g) {
        g.drawImage(sprites[currentFrame], x - Camera.x, y - Camera.y, null);
    }
}
