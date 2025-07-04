package com.engenharia.Projeto.zeldaminiclone.world;

import com.engenharia.Projeto.zeldaminiclone.player.Camera;
import com.engenharia.Projeto.zeldaminiclone.player.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Blocks3d extends Rectangle{
    private BufferedImage texture;
    public int type;

    public Blocks3d(int x, int y, int type) {
        super(x, y, 16, 16);
        this.type = type;

        SpriteSheet caminhoForaCasa = new SpriteSheet("world/terra_grama.png");

        // Criar imagem com suporte a transparência
        BufferedImage tempTexture = null;
        switch (type) {
            case 0:
                texture = caminhoForaCasa.getSprite(0, 32, 16, 16); //arbusto
                break;
            case 1:
                texture = caminhoForaCasa.getSprite(0, 64, 16, 16); //arvore
                break;
            case 2:
                texture = caminhoForaCasa.getSprite(0, 112, 16, 16); //vaso
                break;
            case 3:
                texture = caminhoForaCasa.getSprite(16, 112, 16, 16); //vaso quebrado
                break;
        }

    }


    public void render(Graphics g) {
        g.drawImage(texture, x - Camera.x, y - Camera.y, null);

    }
}
