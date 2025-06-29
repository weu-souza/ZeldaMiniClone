package com.engenharia.Projeto.zeldaminiclone.creatures;

import com.engenharia.Projeto.zeldaminiclone.player.Camera;
import com.engenharia.Projeto.zeldaminiclone.player.SpriteSheet;

import java.awt.image.BufferedImage;

public class Enemies {
    // Exemplo básico de inimigo baseado na estrutura do Player
    public int x, y, width, height, speed;
    private BufferedImage[][] sprites;
    public Enemies(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 16;
        this.height = 16;
        this.speed = 1;
        loadSprites();
    }

    private void loadSprites() {
        SpriteSheet sheet = new SpriteSheet("monsters/Moblin.png");
        sprites = new BufferedImage[4][2]; // 4 direções, 2 frames cada

        // Carrega os sprites (ajuste as coordenadas conforme sua imagem)
        // Sprites para baixo (frames 0 e 1)
        sprites[0][0] = sheet.getSprite(0, 0, 16, 16);
        sprites[0][1] = sheet.getSprite(16, 0, 16, 16);

        // Sprites para direita (frames 2 e 3)
        sprites[1][0] = sheet.getSprite(32, 0, 16, 16);
        sprites[1][1] = sheet.getSprite(48, 0, 16, 16);

        // Sprites para cima (frames 4 e 5)
        sprites[2][0] = sheet.getSprite(64, 0, 16, 16);
        sprites[2][1] = sheet.getSprite(80, 0, 16, 16);

        // Sprites para esquerda (pode usar os da direita espelhados ou adicionar novos)
        sprites[3][0] = sheet.getSprite(32, 0, 16, 16); // Espelhado seria melhor
        sprites[3][1] = sheet.getSprite(48, 0, 16, 16);
    }

    public void tick() {
//        x += speed;
    }

    public void render(java.awt.Graphics g) {
        g.drawImage(sprites[0][0], x - Camera.x, y - Camera.y, null);
    }
}
