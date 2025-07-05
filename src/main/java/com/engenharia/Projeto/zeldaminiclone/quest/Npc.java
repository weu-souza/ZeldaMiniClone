package com.engenharia.Projeto.zeldaminiclone.quest;

import com.engenharia.Projeto.zeldaminiclone.player.Camera;
import com.engenharia.Projeto.zeldaminiclone.player.Player;
import com.engenharia.Projeto.zeldaminiclone.player.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.engenharia.Projeto.zeldaminiclone.player.SpriteSheet.resize;

public class Npc {
    public int x, y, width, height;
    private BufferedImage sprite;
    private Quest quest;

    public Npc(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 16;
        this.height = 16;
        SpriteSheet sheet = new SpriteSheet("Npc/Npc.png");
        this.sprite = resize(sheet.getSprite(0, 0, 700, 700), 16, 16);
        this.quest = new Quest();
    }

    public Quest getQuest() {
        return quest;
    }

    public void tick(Player player, int enemiesAlive, boolean ePressed) {
        // Verifica proximidade com o player
        int dx = Math.abs(player.x - this.x);
        int dy = Math.abs(player.y - this.y);

        if (dx < 20 && dy < 20 && ePressed && !quest.isActive()) {
            quest.start(); // Inicia a quest
        }


        if (quest.isActive() && !quest.isCompleted()) {
            quest.update(enemiesAlive);
        }
    }

    public void render(Graphics g) {
        g.drawImage(sprite, x - Camera.x, y - Camera.y, null);

        // Exibir texto quando perto
        if (quest.isActive() && !quest.isCompleted()) {
            g.setColor(Color.WHITE);
            g.drawString("Derrote todos os inimigos!", x - Camera.x, y - Camera.y - 10);
        } else if (quest.isCompleted()) {
            g.setColor(Color.YELLOW);
            g.drawString("Obrigado, herói!", x - Camera.x, y - Camera.y - 10);
        }
    }
}

