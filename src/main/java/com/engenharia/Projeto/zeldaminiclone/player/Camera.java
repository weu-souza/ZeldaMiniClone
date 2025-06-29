package com.engenharia.Projeto.zeldaminiclone.player;

public class Camera {
    public static int x;
    public static int y;
    public static int clamp(int atual, int minimo, int maximo) {
        if (atual < minimo) return minimo;
        if (atual > maximo) return maximo;
        return atual;
    }

}
