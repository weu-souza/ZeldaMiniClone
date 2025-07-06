package com.engenharia.Projeto.zeldaminiclone.quest;

import com.engenharia.Projeto.zeldaminiclone.Game;
import com.engenharia.Projeto.zeldaminiclone.player.Camera;
import com.engenharia.Projeto.zeldaminiclone.player.Player;
import com.engenharia.Projeto.zeldaminiclone.player.SpriteSheet;
import com.engenharia.Projeto.zeldaminiclone.quest.CoinQuest;
import com.engenharia.Projeto.zeldaminiclone.quest.Quest;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.engenharia.Projeto.zeldaminiclone.player.SpriteSheet.resize;

public class Npc {
    public int x, y, width, height;
    private BufferedImage sprite;
    private Quest killEnemiesQuest;
    private CoinQuest collectCoinsQuest;

    public Npc(int x, int y, CoinQuest collectCoinsQuest) {
        this.x = x;
        this.y = y;
        this.width = 16;
        this.height = 16;
        SpriteSheet sheet = new SpriteSheet("Npc/Npc.png");
        this.sprite = resize(sheet.getSprite(0, 0, 700, 700), 16, 16);

        this.killEnemiesQuest = new Quest();
        this.collectCoinsQuest = collectCoinsQuest;
    }

    public Quest getKillEnemiesQuest() {
        return killEnemiesQuest;
    }

    public CoinQuest getCollectCoinsQuest() {
        return collectCoinsQuest;
    }

    public void tick(Player player, int enemiesAlive, boolean ePressed) {
        int dx = Math.abs(player.x - this.x);
        int dy = Math.abs(player.y - this.y);

        // MAPA 1: iniciar quest de matar inimigos
        if (Game.currentMap == 1 && dx < 20 && dy < 20 && ePressed && !killEnemiesQuest.isActive()) {
            killEnemiesQuest.start();
        }

        // Atualiza a quest de matar inimigos
        if (Game.currentMap == 1 && killEnemiesQuest.isActive() && !killEnemiesQuest.isCompleted()) {
            killEnemiesQuest.update(enemiesAlive);
        }

        // MAPA 2: iniciar quest de coletar moedas apenas ao pressionar E
        if (Game.currentMap == 2 && dx < 20 && dy < 20 && ePressed && !collectCoinsQuest.isActive()) {
            collectCoinsQuest.start();
        }

        // Atualiza a quest de moedas
        if (Game.currentMap == 2 && collectCoinsQuest.isActive() && !collectCoinsQuest.isCompleted()) {
            collectCoinsQuest.update();
        }
    }

    public void render(Graphics g) {
        g.drawImage(sprite, x - Camera.x, y - Camera.y, null);

        if (Game.currentMap == 1) {
            if (!killEnemiesQuest.isActive() && !killEnemiesQuest.isCompleted()) {
                g.setColor(Color.GRAY);
                g.drawString("Aperte [E] para começar!", x - Camera.x, y - Camera.y - 10);
            }
            else if (killEnemiesQuest.isActive() && !killEnemiesQuest.isCompleted()) {
                g.setColor(Color.WHITE);
                g.drawString("Derrote todos os inimigos!", x - Camera.x, y - Camera.y - 10);
            } else if (killEnemiesQuest.isCompleted()) {
                g.setColor(Color.YELLOW);
                g.drawString("Missão concluída!", x - Camera.x, y - Camera.y - 10);
            }
        }
        else if (Game.currentMap == 2) {
            if(!collectCoinsQuest.isActive() && !collectCoinsQuest.isCompleted()){
                g.setColor(Color.GRAY);
                g.drawString("Aperte [E] para começar!", x - Camera.x, y - Camera.y - 10);
            }
           else if (collectCoinsQuest.isActive() && !collectCoinsQuest.isCompleted()) {
                g.setColor(Color.WHITE);
                g.drawString("Colete todas as moedas!", x - Camera.x, y - Camera.y - 10);
            } else if (collectCoinsQuest.isCompleted()) {
                g.setColor(Color.YELLOW);
                g.drawString("Obrigado por coletar!", x - Camera.x, y - Camera.y - 10);
            }
        }
    }
}
