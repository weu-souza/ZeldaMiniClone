package com.engenharia.Projeto.zeldaminiclone.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaFinalizacao extends JFrame {

    public TelaFinalizacao() {
        setTitle("Jogo Finalizado");
        setSize(400, 250);
        setLocationRelativeTo(null); // Centraliza a janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Painel principal
        JPanel painel = new JPanel();
        painel.setBackground(Color.BLACK);
        painel.setLayout(new BorderLayout());

        // Texto de parabéns
        JLabel mensagem = new JLabel("Parabéns! Você finalizou o jogo!", SwingConstants.CENTER);
        mensagem.setFont(new Font("Serif", Font.BOLD, 20));
        mensagem.setForeground(Color.WHITE);
        painel.add(mensagem, BorderLayout.CENTER);

        // Botão de sair
        JButton botaoSair = new JButton("Sair do Jogo");
        botaoSair.setFont(new Font("SansSerif", Font.PLAIN, 16));
        botaoSair.setBackground(Color.DARK_GRAY);
        botaoSair.setForeground(Color.WHITE);
        botaoSair.setFocusPainted(false);
        painel.add(botaoSair, BorderLayout.SOUTH);

        botaoSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Sai do jogo
            }
        });

        add(painel);
        setVisible(true);
    }

    // Método que você chama quando o jogo termina
    public static void mostrarTelaFinal() {
        SwingUtilities.invokeLater(() -> {
            new TelaFinalizacao();
        });
    }
}

