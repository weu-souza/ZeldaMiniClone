package com.engenharia.Projeto.zeldaminiclone.world;

import com.engenharia.Projeto.zeldaminiclone.player.SpriteSheet;

import java.awt.image.BufferedImage;

public class Portal {
    private int x;
    private int y;
    private String destination;
    private BufferedImage[] sprites;

    public Portal(int x, int y, String destination) {
        this.x = x;
        this.y = y;
        this.destination = destination;
    }

    private void loadSprites() {
        SpriteSheet sheet = new SpriteSheet("world/portal.png");
        sprites[0] = sheet.getSprite(0, 0, 16, 16); // Ajuste as coordenadas conforme sua imagem
        sprites[1] = sheet.getSprite(16, 0, 16, 16); // Ajuste as coordenadas conforme sua imagem
        sprites[2] = sheet.getSprite(32, 0, 16, 16); // Ajuste as coordenadas conforme sua imagem
        sprites[3] = sheet.getSprite(0, 16, 16, 16); // Ajuste as coordenadas conforme sua imagem
        sprites[4] = sheet.getSprite(16, 16, 16, 16); // Ajuste as coordenadas conforme sua imagem
        sprites[5] = sheet.getSprite(32, 16, 16, 16); // Ajuste as coordenadas conforme sua imagem
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getDestination() {
        return destination;
    }

}
