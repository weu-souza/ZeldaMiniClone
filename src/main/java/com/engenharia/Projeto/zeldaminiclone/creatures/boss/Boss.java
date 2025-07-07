package com.engenharia.Projeto.zeldaminiclone.creatures.boss;

import com.engenharia.Projeto.zeldaminiclone.Game;
import com.engenharia.Projeto.zeldaminiclone.creatures.Enemies;
import com.engenharia.Projeto.zeldaminiclone.player.Camera;
import com.engenharia.Projeto.zeldaminiclone.player.HealthBar;
import com.engenharia.Projeto.zeldaminiclone.player.SpriteSheet;
import com.engenharia.Projeto.zeldaminiclone.utils.TelaFinalizacao;

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
        for (int i = 0; i < bossSprites.length; i++) {
            bossSprites[i] = resize(sheet.getSprite(i * 16, 0, 16, 16), 32, 32);
        }
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
    public Rectangle getHitbox() {
        return new Rectangle(x, y, 32, 32);
    }

    @Override
    public void render(Graphics g) {
        int drawX = x - Camera.x;
        int drawY = y - Camera.y;

        g.drawImage(bossSprites[currentAnimationFrame], drawX, drawY, null);

        healthBar.render(g, drawX, drawY - 10, false);
    }

    // Removido método podeLevarDano() e lógica de invencibilidade
}
