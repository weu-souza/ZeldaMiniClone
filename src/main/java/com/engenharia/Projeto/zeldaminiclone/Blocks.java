package com.engenharia.Projeto.zeldaminiclone;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Blocks extends Rectangle{
    public Blocks(int x, int y) {
        super(x, y, 16, 16);
    }
    public void render(Graphics g) {
        g.setColor(Color.darkGray);
        g.fillRect(x, y, width, height);
    }

}
