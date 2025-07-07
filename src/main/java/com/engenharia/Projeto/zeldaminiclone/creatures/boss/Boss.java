package com.engenharia.Projeto.zeldaminiclone.creatures.boss;

import com.engenharia.Projeto.zeldaminiclone.Game;
import com.engenharia.Projeto.zeldaminiclone.creatures.Enemies;
import com.engenharia.Projeto.zeldaminiclone.player.Camera;
import com.engenharia.Projeto.zeldaminiclone.player.HealthBar;
import com.engenharia.Projeto.zeldaminiclone.player.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.engenharia.Projeto.zeldaminiclone.player.SpriteSheet.resize;

public class Boss extends Enemies {
    private int shootCooldown = 0;
    private int attackPattern = 0;

    private final int animationFrames = 2;
    private int animationCount = 0;
    private int animationDelay = 10;

    private final BufferedImage[] bossSprites = new BufferedImage[4];
    private int currentDirection = 0;
    private int currentAnimationFrame = 0;
    private BufferedImage[] downSprites = new BufferedImage[2];
    private BufferedImage[] leftSprites = new BufferedImage[2];

    private HealthBar healthBar;

    public Boss(int x, int y) {
        super(x, y);
        this.life = 10;
        this.speed = 0;
        loadSprites();

        healthBar = new HealthBar();
        healthBar.setLife(this.life);
        healthBar.setLife(life);  // Ajusta o max para a vida do boss
    }

    public void loadSprites() {
        SpriteSheet sheet = new SpriteSheet("monsters/boss.png");
        downSprites[0] = resize(sheet.getSprite(0, 0, 16, 16), 32, 32);
        downSprites[1] = resize(sheet.getSprite(16, 0, 16, 16), 32, 32);
        leftSprites[0] = resize(sheet.getSprite(32, 0, 16, 16), 32, 32);
        leftSprites[1] = resize(sheet.getSprite(48, 0, 16, 16), 32, 32);
    }

    public void tick() {
        shootCooldown++;

        if (shootCooldown >= 60) {
            realizarAtaque();
            shootCooldown = 0;
            attackPattern = (attackPattern + 1) % 3;
        }

        updateDirection();

        animationCount++;
        if (animationCount >= animationDelay) {
            animationCount = 0;
            currentAnimationFrame = (currentAnimationFrame + 1) % animationFrames;
        }

        healthBar.setLife(this.life);
    }

    private void updateDirection() {
        int dx = Game.player.x - x;
        int dy = Game.player.y - y;

        if (Math.abs(dx) > Math.abs(dy)) {
            currentDirection = dx > 0 ? 1 : 3; // direita : esquerda
        } else {
            currentDirection = dy > 0 ? 0 : 2; // baixo : cima
        }
    }

    public void takeDamage(int damage) {
        this.life -= damage;
        if (life <= 0) {
            life = 0;
            Game.jogoFinalizado = true;
        }
        healthBar.setLife(life);
    }

    private void realizarAtaque() {
        double dx = Game.player.x - x;
        double dy = Game.player.y - y;
        double length = Math.sqrt(dx * dx + dy * dy);
        dx /= length;
        dy /= length;

        Game.projectiles.add(new BossAttack(x + 16, y + 16, dx, dy));
    }

    private void animation(int drawX, int drawY, Graphics g) {
        BufferedImage sprite;

        switch (currentDirection) {
            case 0: // baixo
                sprite = downSprites[currentAnimationFrame];
                g.drawImage(sprite, drawX, drawY, null);
                break;

            case 2: // cima (espelha verticalmente o sprite de baixo)
                sprite = downSprites[currentAnimationFrame];
                g.drawImage(sprite, drawX, drawY + sprite.getHeight(), sprite.getWidth(), -sprite.getHeight(), null);
                break;

            case 3: // esquerda
                sprite = leftSprites[currentAnimationFrame];
                g.drawImage(sprite, drawX, drawY, null);
                break;

            case 1: // direita (espelha horizontalmente o sprite de esquerda)
                sprite = leftSprites[currentAnimationFrame];
                g.drawImage(sprite, drawX + sprite.getWidth(), drawY, -sprite.getWidth(), sprite.getHeight(), null);
                break;
        }
    }

    @Override
    public void render(Graphics g) {
        if (isDead()) return;
        int drawX = x - Camera.x;
        int drawY = y - Camera.y;

        animation(drawX, drawY, g);

        healthBar.render(g, drawX, drawY - 10, false);
    }
}
