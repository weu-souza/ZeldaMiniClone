package com.engenharia.Projeto.zeldaminiclone.quest;

import com.engenharia.Projeto.zeldaminiclone.colectables.Inventory;

public class CoinQuest {
    private boolean active = false;
    private boolean completed = false;
    private Inventory inventory;

    public CoinQuest(Inventory inventory) {
        this.inventory = inventory;
    }

    public void start() {
        active = true;
    }

    public void update() {
        if (active && !completed && inventory.getCoins() >= 8) {
            completed = true;
        }
    }

    public boolean isActive() { return active; }
    public boolean isCompleted() { return completed; }
}
