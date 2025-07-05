package com.engenharia.Projeto.zeldaminiclone;

import com.engenharia.Projeto.zeldaminiclone.creatures.Enemies;
import com.engenharia.Projeto.zeldaminiclone.quest.Npc;
import com.engenharia.Projeto.zeldaminiclone.player.Camera;
import com.engenharia.Projeto.zeldaminiclone.player.HealthBar;
import com.engenharia.Projeto.zeldaminiclone.player.Player;
import com.engenharia.Projeto.zeldaminiclone.world.Portal;
import com.engenharia.Projeto.zeldaminiclone.world.World;

import java.awt.*;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
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
    public static Portal portal;
    public static Player player;
    public static Npc npc;
    public static List<Enemies> enemies = new ArrayList<>();
    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    public static int currentMap = 1;

    public Game() {
        setPreferredSize(new java.awt.Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initFrame();
        addKeyListener(this);
        player = new Player(0, 0); // A posição será atualizada pelo World
//        enemies = new Enemies(0, 0); // A posição será atualizada pelo World
        npc = new Npc(0, 0); // A posição será atualizada pelo World
        portal = new Portal(0, 0); // Inicializa o portal, a posição será atualizada pelo World

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



    private void trocarMapa() {
        World.clearWorld();
        switch (currentMap) {
            case 1:
                world = new World("maps/map.png");
                currentMap = 2;
                break;
            case 2:
                world = new World("maps/map_2.png");
                currentMap = 3;
                break;
            case 3:
                world = new World("maps/map_3.png"); // volta pro início
                currentMap = 1;
                break;
            default:
                System.out.println("Mapa desconhecido, seu desastre.");
                break;
        }

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
            enemy.tick(player);
        }
        npc.tick();
        if (portal.playerCollides(player.x, player.y)) {
            trocarMapa();
        }
        portal.tick();
        // atack player
        if (player.isAttacking() && !player.attackHitRegistered) {
            Rectangle playerAttack = player.getAttackBounds();

            for (Enemies enemy : enemies) {
                Rectangle enemyBounds = new Rectangle(enemy.x, enemy.y, enemy.width, enemy.height);

                if (playerAttack.intersects(enemyBounds)) {
                    enemy.takeDamage(1);
                    player.attackHitRegistered = true; // MARCA o hit, só um por ataque
                    break; // se quiser atacar só um inimigo por ataque
                }
            }
        }

        Iterator<Enemies> it = enemies.iterator();
        while (it.hasNext()) {
            Enemies e = it.next();
            e.tick(player);
            if (e.isDead()) {
                it.remove();
            }
        }


    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = image.getGraphics();

        // Desenhar no buffer (tela base)
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Renderizar todos os elementos no buffer SEM escala
        world.render(g);
        player.render(g);
        npc.render(g);
        portal.render(g);
        for (Enemies enemy : enemies) {
            enemy.render(g);
        }
        g.dispose();

        // Agora desenhar o buffer com escala na tela real
        Graphics gFinal = bs.getDrawGraphics();
        gFinal.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        gFinal.dispose();
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
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!player.isAttacking()) { // Crie esse método no player para acessar o atributo privado
                player.startAttack(); // Também crie esse método para iniciar o ataque
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_R) {
            player.heal(1); // Método para curar o player
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
