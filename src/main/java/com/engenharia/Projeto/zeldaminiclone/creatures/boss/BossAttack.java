package com.engenharia.Projeto.zeldaminiclone.creatures.boss;

import com.engenharia.Projeto.zeldaminiclone.Game;
import com.engenharia.Projeto.zeldaminiclone.player.Camera;
import com.engenharia.Projeto.zeldaminiclone.player.SpriteSheet;
import com.engenharia.Projeto.zeldaminiclone.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.engenharia.Projeto.zeldaminiclone.player.SpriteSheet.resize;

public class BossAttack {
    private double x, y;
    private double dx, dy;
    private double speed = 2;

    private final int width = 16;
    private final int height = 16;

    private BufferedImage attackSprite;

    public BossAttack(double x, double y, double dirX, double dirY) {
        this.x = x;
        this.y = y;

        double length = Math.sqrt(dirX * dirX + dirY * dirY);
        this.dx = (dirX / length) * speed;
        this.dy = (dirY / length) * speed;

        loadSprite();
    }

    private void loadSprite() {
        SpriteSheet sheet = new SpriteSheet("monsters/projectile.png");
        attackSprite = resize(sheet.getSprite(0, 0, 10, 10), width, height);
    }

    public void tick() {
        x += dx;
        y += dy;

        if (colideComPlayer()) {
            Game.player.takeDamage(1);
            Game.projectiles.remove(this);
        }

        if (x < 0 || x > World.WIDTH * 16 || y < 0 || y > World.HEIGHT * 16) {
            Game.projectiles.remove(this);
        }
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        double angle = Math.atan2(dy, dx);

        int drawX = (int) x - Camera.x;
        int drawY = (int) y - Camera.y;

        g2d.rotate(angle, drawX + width / 2, drawY + height / 2);
        g2d.drawImage(attackSprite, drawX, drawY, null);
        g2d.rotate(-angle, drawX + width / 2, drawY + height / 2);
    }

    private boolean colideComPlayer() {
        Rectangle proj = new Rectangle((int) x, (int) y, width, height);
        Rectangle player = new Rectangle(Game.player.x, Game.player.y, 16, 16); // ajuste se player tiver hitbox diferente
        return proj.intersects(player);
    }
}
