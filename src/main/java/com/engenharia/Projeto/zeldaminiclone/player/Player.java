package com.engenharia.Projeto.zeldaminiclone.player;

import com.engenharia.Projeto.zeldaminiclone.Game;
import com.engenharia.Projeto.zeldaminiclone.world.World;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Player {
    public int x, y;
    public int speed = 2;
    public boolean right, left, up, down;
    private BufferedImage[][] sprites;
    private int currentAnimationFrame = 0;
    private int animationFrames = 2; // 2 frames por direção
    private int animationDelay = 10; // Controle de velocidade da animação
    private int animationCount = 0;
    private int currentDirection = 0; // 0=baixo, 1=direita, 2=cima, 3=esquerda

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        loadSprites();
    }

    private void loadSprites() {
        SpriteSheet sheet = new SpriteSheet("player/Link_walk.png");
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
        boolean isMoving = false;

        if (right && World.isFree(x + speed, y)) {
            x += speed;
            currentDirection = 1;
            isMoving = true;
        } else if (left && World.isFree(x - speed, y)) {
            x -= speed;
            currentDirection = 3;
            isMoving = true;
        }

        if (up && World.isFree(x, y - speed)) {
            y -= speed;
            currentDirection = 2;
            isMoving = true;
        } else if (down && World.isFree(x, y + speed)) {
            y += speed;
            currentDirection = 0;
            isMoving = true;
        }

        // Atualiza a animação apenas se estiver se movendo
        if (isMoving) {
            animationCount++;
            if (animationCount > animationDelay) {
                animationCount = 0;
                currentAnimationFrame = (currentAnimationFrame + 1) % animationFrames;
            }
        } else {
            currentAnimationFrame = 0; // Volta para o primeiro frame quando parado
        }

        Camera.x = Camera.clamp(x - (Game.WIDTH / 2), 0, (World.WIDTH * 16) -
                Game.WIDTH);
        Camera.y = Camera.clamp(y - (Game.HEIGHT / 2), 0, (World.HEIGHT * 16) -
                Game.HEIGHT);

    }


    public void render(Graphics g) {
        BufferedImage currentSprite = sprites[currentDirection][currentAnimationFrame];

        int drawX = x - Camera.x;
        int drawY = y - Camera.y;

        if (currentDirection == 3) { // Esquerda — precisa espelhar horizontalmente
            g.drawImage(currentSprite, drawX + 16, drawY, -16, 16, null);
        } else {
            g.drawImage(currentSprite, drawX, drawY, null);
        }
    }

}