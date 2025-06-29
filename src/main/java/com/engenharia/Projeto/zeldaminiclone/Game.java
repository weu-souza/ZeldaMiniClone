package com.engenharia.Projeto.zeldaminiclone;

import com.engenharia.Projeto.zeldaminiclone.creatures.Enemies;
import com.engenharia.Projeto.zeldaminiclone.creatures.Npc;
import com.engenharia.Projeto.zeldaminiclone.player.Camera;
import com.engenharia.Projeto.zeldaminiclone.player.Player;
import com.engenharia.Projeto.zeldaminiclone.world.World;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Game extends Canvas implements Runnable, KeyListener {
    private JFrame frame;
    public static final int WIDTH = 340;
    public static final int HEIGHT = 220;

    public static final int SCALE = 3;
    private Thread thread;
    private boolean isRunning = true;
    private Camera camera;
    private World world;
    public static Player player;
    public static Npc npc;
    public static List<Enemies> enemies = new ArrayList<>();

    public Game() {
        setPreferredSize(new java.awt.Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initFrame();
        addKeyListener(this);
        player = new Player(0, 0); // A posição será atualizada pelo World
//        enemies = new Enemies(0, 0); // A posição será atualizada pelo World
        npc = new Npc(0, 0); // A posição será atualizada pelo World
        world = new World("map.png"); // nome da imagem na pasta resources
        start();
        Camera.x = 0;
        Camera.y = 0;
    }

    public void initFrame() {
        frame = new JFrame("Zelda Mini Clone");
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void tick() {
        player.tick();
        for (Enemies enemy : enemies) {
            enemy.tick();
        }
        npc.tick();
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
        // Aqui desenharemos os objetos no futuro
        world.render(g);
        player.render(g);
        npc.render(g);
        for (Enemies enemy : enemies) {
            enemy.render(g);
        }
        g.dispose();
        bs.show();
    }

    public void run() {
        while (isRunning) {
            tick();
            render();
            try {
                Thread.sleep(1000 / 60); // 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stop();
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D) {
            player.right = true;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            player.left = true;
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            player.up = true;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            player.down = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D) {
            player.right = false;
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            player.left = false;
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            player.up = false;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            player.down = false;
        }
    }


}
