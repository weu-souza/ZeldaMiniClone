package com.engenharia.Projeto.zeldaminiclone.colectables;

import com.engenharia.Projeto.zeldaminiclone.player.Camera;
import com.engenharia.Projeto.zeldaminiclone.player.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.engenharia.Projeto.zeldaminiclone.player.SpriteSheet.resize;

public class Inventory {
    private BufferedImage coinImage;
    private int maxCoins = 8;
    private int coins = 0;

    public Inventory() {
        SpriteSheet heart = new SpriteSheet("colectables/coin.png");
        this.coinImage = resize(heart.getSprite(0, 0, 16, 16), 16, 16);
    }

    public void addCoins(int amount) {
        coins += amount;
    }

    public int getCoins() {
        return coins;
    }

    public void reset() {
        coins = 0;
    }

    public void render(Graphics g, int x, int y, boolean useCamera) {
        int baseX = useCamera ? x - Camera.x : x;
        int baseY = useCamera ? y - Camera.y - 20 : y;

        // Desenha a imagem da moeda
        g.drawImage(coinImage, baseX, baseY, 16, 16, null);

        // Desenha o número de moedas ao lado
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("x" + coins, baseX + 20, baseY + 12); // Ajusta a posição conforme preferir
    }
}
