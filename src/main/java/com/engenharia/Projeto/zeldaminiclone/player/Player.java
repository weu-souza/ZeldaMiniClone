package com.engenharia.Projeto.zeldaminiclone.player;

import com.engenharia.Projeto.zeldaminiclone.Game;
import com.engenharia.Projeto.zeldaminiclone.world.World;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import static com.engenharia.Projeto.zeldaminiclone.player.SpriteSheet.resize;

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
    private BufferedImage[] attackSprites = new BufferedImage[3];
    private boolean isAttacking = false;
    private int attackFrame = 0;
    private int attackFrameDelay = 0;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        loadSprites();
    }

    private void loadSprites() {
        SpriteSheet sheet = new SpriteSheet("player/Link_walk.png");
        SpriteSheet AttackX = new SpriteSheet("player/Slash_x.png");
        SpriteSheet AttackY = new SpriteSheet("player/slash_y.png");


        sprites = new BufferedImage[4][2]; // 4 direções, 2 frames cada
        attackSprites[0] = resize(AttackX.getSprite(0, 0, 550, 400), 16, 16);
        attackSprites[1] = resize(AttackY.getSprite(0, 0, 400, 550), 16, 16);
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

    public boolean isAttacking() {
        return isAttacking;
    }

    public void startAttack() {
        if (!isAttacking) {
            isAttacking = true;
            attackFrame = 0;
            attackFrameDelay = 0;
        }
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

        // Animação de movimento
        if (isMoving && !isAttacking) {  // Não anima movimento se estiver atacando
            animationCount++;
            if (animationCount > animationDelay) {
                animationCount = 0;
                currentAnimationFrame = (currentAnimationFrame + 1) % animationFrames;
            }
        } else if (!isAttacking) {
            currentAnimationFrame = 0;
        }

        // Animação de ataque
        if (isAttacking) {
            attackFrameDelay++;
            if (attackFrameDelay >= 5) {
                attackFrame++;
                attackFrameDelay = 0;
                if (attackFrame >= attackSprites.length) {
                    attackFrame = 0;
                    isAttacking = false;
                }
            }
        }

        Camera.x = Camera.clamp(x - (Game.WIDTH / 2), 0, (World.WIDTH * 16) - Game.WIDTH);
        Camera.y = Camera.clamp(y - (Game.HEIGHT / 2), 0, (World.HEIGHT * 16) - Game.HEIGHT);
    }


    public void render(Graphics g) {
        int drawX = x - Camera.x;
        int drawY = y - Camera.y;

        // Primeiro desenha o player
        BufferedImage currentSprite = sprites[currentDirection][currentAnimationFrame];
        if (currentDirection == 3) { // esquerda espelhada
            g.drawImage(currentSprite, drawX + 16, drawY, -16, 16, null);
        } else {
            g.drawImage(currentSprite, drawX, drawY, null);
        }

        // Agora desenha o slash (ataque) se estiver atacando
        if (isAttacking) {
            int slashX = drawX;
            int slashY = drawY;

            // Deslocamento para dar sensação de ataque em direção
            switch (currentDirection) {
                case 0: slashY += 16; break; // baixo
                case 1: slashX += 16; break; // direita
                case 2: slashY -= 16; break; // cima
                case 3: slashX -= 16; break; // esquerda
            }

            BufferedImage slash = (currentDirection == 0 || currentDirection == 2) ? attackSprites[1] : attackSprites[0];
            int w = slash.getWidth();
            int h = slash.getHeight();

            switch (currentDirection) {
                case 0: // baixo
                case 1: // direita
                    g.drawImage(slash, slashX, slashY, null);
                    break;
                case 2: // cima (espelho vertical)
                    g.drawImage(slash, slashX, slashY + h, w, -h, null);
                    break;
                case 3: // esquerda (espelho horizontal)
                    g.drawImage(slash, slashX + w, slashY, -w, h, null);
                    break;
            }
        }
    }



}