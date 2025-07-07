package com.engenharia.Projeto.zeldaminiclone;

import com.engenharia.Projeto.zeldaminiclone.colectables.Collectables;
import com.engenharia.Projeto.zeldaminiclone.colectables.Inventory;
import com.engenharia.Projeto.zeldaminiclone.creatures.Enemies;
import com.engenharia.Projeto.zeldaminiclone.quest.CoinQuest;
import com.engenharia.Projeto.zeldaminiclone.quest.Npc;
import com.engenharia.Projeto.zeldaminiclone.player.Camera;
import com.engenharia.Projeto.zeldaminiclone.player.HealthBar;
import com.engenharia.Projeto.zeldaminiclone.player.Player;
import com.engenharia.Projeto.zeldaminiclone.utils.TelaFinalizacao;
import com.engenharia.Projeto.zeldaminiclone.world.Portal;
import com.engenharia.Projeto.zeldaminiclone.world.Sound;
import com.engenharia.Projeto.zeldaminiclone.world.World;

import java.awt.*;
import java.awt.image.BufferStrategy;
import javax.swing.*;
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
    public static List<Collectables> coin = new ArrayList<>();
    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    public static int currentMap = 1;
    boolean ePressed = false; /* detectar se E foi pressionado */
    CoinQuest coinQuest;
    private boolean jogoFinalizado = false;
    public static boolean portalSoundPlayed = false;
    public Sound portalSound = new Sound("/sounds/portal.wav");
    private Sound bgAUdio = new Sound("/sounds/background-sound.wav");


    public Game() {
        setPreferredSize(new java.awt.Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initFrame();
        addKeyListener(this);
        player = new Player(0, 0); // A posição será atualizada pelo World
        coinQuest = new CoinQuest(player.getInventory());
        npc = new Npc(0, 0, coinQuest); // A posição será atualizada pelo World
        portal = new Portal(0, 0); // Inicializa o portal, a posição será atualizada pelo World
        world = new World("maps/map.png");
        portalSound.setVolume(0.3f);
        bgAUdio.setVolume(0.05f);
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
        bgAUdio.loop();
        thread.start();
    }


    private void trocarMapa() {
        switch (currentMap) {
            case 1:
                currentMap = 2;
                break;
            case 2:
                currentMap = 3;
                break;
            case 3:
                currentMap = 4;
                break;
        }
        portalSoundPlayed = false;
        portalSound.stop();
        player.audioStop();
        World.clearWorld();

        // carrega o novo mapa, que precisa popular blocos novamente
        switch (currentMap) {
            case 2:
                world = new World("maps/map_2.png");
                break;
            case 3:
                world = new World("maps/map_3.png");
                break;
            case 4:
                jogoFinalizado = true;
                portalSound.stop();
                player.audioStop();
                bgAUdio.stop();
                TelaFinalizacao.mostrarTelaFinal();
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

    private void gameTick() {
        player.tick();

        for (Enemies enemy : enemies) {
            enemy.tick(player);
        }
        if (npc != null) {
            npc.tick(player, enemies.size(), ePressed);
            ;
        }
    }

    private void questTick() {
        if (coinQuest.isActive()) {
            coinQuest.update();
        }
        boolean podeAbrirPortal = false;

        if (currentMap == 1 && npc.getKillEnemiesQuest().isCompleted()) {
            if (portal == null) {
                portal = new Portal(0, 0); // insira a posição certa
            }
            podeAbrirPortal = true;
        }

        if (currentMap == 2 && npc.getCollectCoinsQuest().isCompleted()) {
            if (portal == null) {
                portal = new Portal(0, 0); // insira a posição certa
            }
            portal.tick();
            podeAbrirPortal = true;
        }
        if (currentMap == 3) {
            if (portal == null) {
                portal = new Portal(0, 0); // insira a posição certa
            }
            portal.tick();
            podeAbrirPortal = true;
        }

        if (podeAbrirPortal && portal != null) {
            if (!portalSoundPlayed) {

                portalSound.play();
                portalSoundPlayed = true;
            }
            portal.tick();

            if (portal.playerCollides(player.x, player.y)) {
                trocarMapa();
            }
        }


        for (Collectables c : coin) {
            c.tick();
            if (c.playerCollides(player.x, player.y)) {
                player.addCoin(1);
                coin.remove(c);
                break;
            }
        }
        coinQuest.update();
    }


    private void atackPlayerTick() {
        if (player.isAttacking() && !player.attackHitRegistered) {
            Rectangle playerAttack = player.getAttackBounds();

            for (Enemies enemy : enemies) {
                Rectangle enemyBounds = new Rectangle(enemy.x, enemy.y, enemy.width, enemy.height);

                if (playerAttack.intersects(enemyBounds)) {
                    enemy.takeDamage(1);
                    player.attackHitRegistered = true;
                    break;
                }
            }
        }
    }

    private void attackMobTick() {
        Iterator<Enemies> it = enemies.iterator();
        while (it.hasNext()) {
            Enemies e = it.next();
            e.tick(player);
            if (e.isDead()) {
                it.remove();
            }
        }
    }


    public void tick() {
        gameTick();
        questTick();
        atackPlayerTick();
        attackMobTick();
    }


    private void renderElements(Graphics g) {
        world.render(g);
        player.render(g);
        if (Game.npc != null) {
            Game.npc.render(g);
        }
        for (Enemies enemy : enemies) {
            enemy.render(g);
        }
        for (Collectables c : coin) {
            c.render(g);
        }
    }

    private void renderPortalAccordingToMap(Graphics g) {
        boolean podeRenderizarPortal = false;
        if (currentMap == 1 && npc.getKillEnemiesQuest().isCompleted()) {
            podeRenderizarPortal = true;
        }
        if (currentMap == 2 && npc.getCollectCoinsQuest().isCompleted()) {
            podeRenderizarPortal = true;
        }
        if (currentMap == 3) {
            podeRenderizarPortal = true;
        }

        if (podeRenderizarPortal && portal != null) {
            portal.render(g);
        }
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        Graphics g = image.getGraphics();

        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }


        // Desenhar no buffer (tela base)
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Renderizar todos os elementos no buffer SEM escala
        renderElements(g);

        renderPortalAccordingToMap(g);

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
        if (e.getKeyCode() == KeyEvent.VK_E) {
            ePressed = true;
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
        } else if (e.getKeyCode() == KeyEvent.VK_E) {
            ePressed = false;
        }
    }


}
