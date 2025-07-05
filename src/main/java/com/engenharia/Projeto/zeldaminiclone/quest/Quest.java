package com.engenharia.Projeto.zeldaminiclone.quest;

public class Quest {
    private boolean active = false;
    private boolean completed = false;

    public void start() {
        active = true;
    }

    public void update(int enemiesAlive) {
        if (active && enemiesAlive == 0) {
            completed = true;
        }
    }

    public boolean isActive() {
        return active;
    }

    public boolean isCompleted() {
        return completed;
    }
}
