package com.engenharia.Projeto.zeldaminiclone.player;

import com.engenharia.Projeto.zeldaminiclone.player.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

import static com.engenharia.Projeto.zeldaminiclone.player.SpriteSheet.resize;


public class HealthBar {
    private BufferedImage heart;
    private int maxHearts = 10;
    private int currentLife;

    public HealthBar() {
        SpriteSheet heart = new SpriteSheet("monsters/healthMonster.png");
        this.heart = resize(heart.getSprite(0, 0, 204, 192), 16, 16);
        currentLife = maxHearts;
    }

    public void setLife(int life) {
        currentLife = Math.max(0, Math.min(maxHearts, life));
    }

    public void render(Graphics g, int x, int y, boolean useCamera) {
        int baseX = useCamera ? x - Camera.x : x;
        int baseY = useCamera ? y - Camera.y - 20 : y; // Só aplica offset se for mundo

        for (int i = 0; i < currentLife; i++) {
            g.drawImage(heart, baseX + (i * 18), baseY, 10, 10, null);
        }
    }


}
