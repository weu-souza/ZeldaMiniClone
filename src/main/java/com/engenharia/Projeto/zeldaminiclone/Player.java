package com.engenharia.Projeto.zeldaminiclone;
import java.awt.Color;
import java.awt.Graphics;

public class Player {
    public int x, y;
    public int speed = 2;
    public boolean right, left, up, down;
    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void tick() {
        if (right) x += speed;
        else if (left) x -= speed;
        if (up) y -= speed;
        else if (down) y += speed;
    }
    public void render(Graphics g) {
        g.setColor(Color.blue);
        g.fillRect(x, y, 16, 16);
    }

}
