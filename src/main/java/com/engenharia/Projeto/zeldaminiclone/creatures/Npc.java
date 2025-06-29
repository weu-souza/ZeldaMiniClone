package com.engenharia.Projeto.zeldaminiclone.creatures;

import com.engenharia.Projeto.zeldaminiclone.player.Camera;
import com.engenharia.Projeto.zeldaminiclone.player.SpriteSheet;

import java.awt.image.BufferedImage;

public class Npc {
    // Exemplo básico de inimigo baseado na estrutura do Player
    public int x, y, width, height, speed;
    private BufferedImage sprite;

    public Npc(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 16;
        this.height = 16;
        this.speed = 1;
        SpriteSheet sheet = new SpriteSheet("Npc/penguin_cap.png");
        this.sprite = sheet.getSprite(0, 0, 16, 16);
    }

    public void tick() {
//        x += speed;
    }

    public void render(java.awt.Graphics g) {
        g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
    }
}

