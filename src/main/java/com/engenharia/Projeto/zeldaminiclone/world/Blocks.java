package com.engenharia.Projeto.zeldaminiclone.world;

import com.engenharia.Projeto.zeldaminiclone.player.Camera;
import com.engenharia.Projeto.zeldaminiclone.player.SpriteSheet;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Blocks extends Rectangle {
    private BufferedImage texture;
    public int type;

    public Blocks(int x, int y, int type) {
        super(x, y, 16, 16);
        this.type = type;
        SpriteSheet parede = new SpriteSheet("world/tileset.png");
        SpriteSheet chaoCasa = new SpriteSheet("world/floor_tiles.png");
        SpriteSheet caminhoForaCasa = new SpriteSheet("world/terra_grama.png");
        SpriteSheet arbusto = new SpriteSheet("world/terra_grama.png");

        // Criar imagem com suporte a transparência
        BufferedImage tempTexture = null;
        switch (type) {
            case 0:
                texture = parede.getSprite(0, 16, 16, 16); //parede
                break;
            case 1:
                texture = chaoCasa.getSprite(0, 0, 16, 16);  //chão casa
                break;
            case 2:
                texture = caminhoForaCasa.getSprite(32, 0, 16, 16); //areia
                break;
            case 3:
                texture = arbusto.getSprite(16, 0, 16, 16); //arbusto
                break;
        }
    }


    public void render(Graphics g) {
        g.drawImage(texture, x - Camera.x, y - Camera.y, null);

    }


}
