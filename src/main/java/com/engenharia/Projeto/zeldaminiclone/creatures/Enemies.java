package com.engenharia.Projeto.zeldaminiclone.creatures;

import com.engenharia.Projeto.zeldaminiclone.player.Camera;
import com.engenharia.Projeto.zeldaminiclone.player.HealthBar;
import com.engenharia.Projeto.zeldaminiclone.player.Player;
import com.engenharia.Projeto.zeldaminiclone.player.SpriteSheet;
import com.engenharia.Projeto.zeldaminiclone.world.Sound;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.engenharia.Projeto.zeldaminiclone.player.SpriteSheet.resize;

public class Enemies {
    public int x, y;
    public final int startX;
    public final int startY;
    public int width, height, speed;
    private BufferedImage[][] sprites;
    public int maxLife = 3; // Máximo de vida do player
    public int life = maxLife;
    private HealthBar healthBar = new HealthBar();
    private int currentDirection = 0; // 0 = baixo, 1 = direita, 2 = cima, 3 = esquerda
    private int currentAnimationFrame = 0;
    private int animationDelay = 10;
    private int animationCount = 0;
    private BufferedImage[] attackSprites = new BufferedImage[3];
    private boolean isAttacking = false;
    private int attackFrame = 0;
    private int attackFrameDelay = 0;
    public boolean attackHitRegistered = false;
    private long lastAttackTime = 0;    // hora do último ataque em milissegundos
    private final long attackCooldown = 500;
    private final Sound walkingSound = new Sound("/sounds/walking.wav");
    private boolean walkingSoundPlaying = false;
    private final Sound attackSound = new Sound("/sounds/sword-sound.wav");
    private boolean attackSoundPlayed = false;


    public Enemies(int x, int y) {
        this.x = x;
        this.y = y;
        startX = x;
        startY = y;
        this.width = 16;
        this.height = 16;
        this.speed = 1;
        loadSprites();
        healthBar.setLife(life); // Inicializa a barra de vida
    }

    public void takeDamage(int amount) {
        life -= amount;
        if (life < 0) {
            life = 0;
        }
        healthBar.setLife(life);
    }

    public boolean isDead() {
        return life == 0;
    }

    private void loadSprites() {
        SpriteSheet sheet = new SpriteSheet("monsters/Moblin.png");
        sprites = new BufferedImage[4][2]; // 4 direções, 2 frames cada
        SpriteSheet AttackX = new SpriteSheet("player/slash_x.png");
        SpriteSheet AttackY = new SpriteSheet("player/slash_y.png");

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

    public void startAttack() {
        if (!isAttacking) {
            isAttacking = true;
            attackFrame = 0;
            attackFrameDelay = 0;
            attackHitRegistered = false;
            playAttackSound();
            stopWalkingSound();
        }
    }

    private void playWalkingSound() {
        if (!walkingSoundPlaying) {
            walkingSound.loop();
            walkingSoundPlaying = true;
        }
    }

    private void stopWalkingSound() {
        if (walkingSoundPlaying) {
            walkingSound.stop();
            walkingSoundPlaying = false;
        }
    }

    private void playAttackSound() {
        if (!attackSoundPlayed) {
            attackSound.play();
            attackSoundPlayed = true;
        }
    }

    public Rectangle getAttackBounds() {
        int attackRange = 16;

        switch (currentDirection) {
            case 0:
                return new Rectangle(x, y + 16, 16, attackRange); // baixo
            case 1:
                return new Rectangle(x + 16, y, attackRange, 16); // direita
            case 2:
                return new Rectangle(x, y - attackRange, 16, attackRange); // cima
            case 3:
                return new Rectangle(x - attackRange, y, attackRange, 16); // esquerda
        }

        return new Rectangle(x, y, 16, 16);
    }

    private void moveToPlayerWithLogic(int dx, int dy, double distance, Player player, long currentTime) {
        double stopDistance = 16;

        if (distance > stopDistance) {
            double moveX = 0, moveY = 0;
            playWalkingSound();

            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > 0) {
                    moveX = speed;
                    currentDirection = 1;
                } else {
                    moveX = -speed;
                    currentDirection = 3;
                }
            } else {
                if (dy > 0) {
                    moveY = speed;
                    currentDirection = 0;
                } else {
                    moveY = -speed;
                    currentDirection = 2;
                }
            }

            double newDx = player.x - (this.x + moveX);
            double newDy = player.y - (this.y + moveY);
            double newDistance = Math.sqrt(newDx * newDx + newDy * newDy);

            if (newDistance >= stopDistance) {
                this.x += moveX;
                this.y += moveY;
            }
        } else {
            stopWalkingSound();
        }

        if (distance <= stopDistance && currentTime - lastAttackTime >= attackCooldown) {
            startAttack();
            lastAttackTime = currentTime;
        }
    }

    private void moveBackToStart() {
        int dx = startX - this.x;
        int dy = startY - this.y;

        if (dx == 0 && dy == 0) {
            stopWalkingSound();
            return;
        }

        playWalkingSound();

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                this.x += speed;
                currentDirection = 1;
            } else {
                this.x -= speed;
                currentDirection = 3;
            }
        } else {
            if (dy > 0) {
                this.y += speed;
                currentDirection = 0;
            } else {
                this.y -= speed;
                currentDirection = 2;
            }
        }
    }


    public void tick(Player player) {
        if (isDead()) return;

        animationCount++;
        if (animationCount > animationDelay) {
            animationCount = 0;
            currentAnimationFrame = (currentAnimationFrame + 1) % 2;
        }

        int dx = player.x - this.x;
        int dy = player.y - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        long currentTime = System.currentTimeMillis();

        int attackRange = 16; // distância para atacar
        int chaseRange = 48;

// Player dentro do alcance
        if (distance <= chaseRange && !isAttacking) {
            moveToPlayerWithLogic(dx, dy, distance, player, currentTime);
        }
// Player longe, volta pra posição inicial
        else if (!isAttacking) {
            moveBackToStart();
        }


        if (isAttacking) {
            attackFrameDelay++;

            int attackDelayThreshold = 3;

            if (attackFrameDelay >= attackDelayThreshold) {
                attackFrame++;
                attackFrameDelay = 0;

                if (!attackHitRegistered && getAttackBounds().intersects(new Rectangle(player.x, player.y, 16, 16))) {
                    player.takeDamage(1);
                    attackHitRegistered = true;
                }

                if (attackFrame >= attackSprites.length) {
                    attackFrame = 0;
                    isAttacking = false;
                    attackSoundPlayed = false;
                }
            }

        }
    }


    public void render(Graphics g) {
        if (isDead()) return;

        int drawX = x - Camera.x;
        int drawY = y - Camera.y;

        // Desenha o inimigo normal
        BufferedImage currentSprite = sprites[currentDirection][currentAnimationFrame];
        g.drawImage(currentSprite, drawX, drawY, null);

        // Barra de vida
        healthBar.render(g, x, y, true);

        // Se estiver atacando, desenha o slash
        if (isAttacking) {
            int slashX = drawX;
            int slashY = drawY;

            switch (currentDirection) {
                case 0:
                    slashY += 16;
                    break;
                case 1:
                    slashX += 16;
                    break;
                case 2:
                    slashY -= 16;
                    break;
                case 3:
                    slashX -= 16;
                    break;
            }

            BufferedImage slash = (currentDirection == 0 || currentDirection == 2) ? attackSprites[1] : attackSprites[0];
            int w = slash.getWidth();
            int h = slash.getHeight();

            switch (currentDirection) {
                case 0:
                case 1:
                    g.drawImage(slash, slashX, slashY, null);
                    break;
                case 2:
                    g.drawImage(slash, slashX, slashY + h, w, -h, null);
                    break;
                case 3:
                    g.drawImage(slash, slashX + w, slashY, -w, h, null);
                    break;
            }
        }
    }

}
