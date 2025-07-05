package com.engenharia.Projeto.zeldaminiclone.world;

import com.engenharia.Projeto.zeldaminiclone.player.Camera;
import com.engenharia.Projeto.zeldaminiclone.player.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Portal {
    public int x;
    public int y;
    private BufferedImage[] sprites;
    private int currentFrame = 0;
    private int maxFrames = 2;
    private int frameDelay = 10; // quantos ticks cada frame fica
    private int frameDelayCounter = 0;

    public Portal(int x, int y) {
        this.x = x;
        this.y = y;
        sprites = new BufferedImage[maxFrames]; // Inicializa o array!
        loadSprites();
    }

    private void loadSprites() {
        SpriteSheet sheet = new SpriteSheet("world/portal.png");
        sprites[0] = sheet.getSprite(16, 0, 16, 48);
        sprites[1] = sheet.getSprite(80, 0, 16, 48);
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
        Rectangle portalRect = new Rectangle(x, y, 16, 48);
        return playerRect.intersects(portalRect);
    }


    public void render(java.awt.Graphics g) {
        g.drawImage(sprites[currentFrame], x - Camera.x, y - Camera.y, null);
    }
}
