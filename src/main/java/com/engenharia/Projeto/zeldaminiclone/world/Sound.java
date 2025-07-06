package com.engenharia.Projeto.zeldaminiclone.world;

import javax.sound.sampled.*;

public class Sound {

    private Clip clip;

    public Sound(String path) {
        try {
            var resourceUrl = getClass().getResource(path);
            if (resourceUrl == null) {
                System.err.println("Arquivo de áudio não encontrado: " + path);
                return;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(resourceUrl);
            clip = AudioSystem.getClip();
            clip.open(ais);
            setVolume(0.5f); // Define o volume inicial (0.0 a 1.0)

        } catch (Exception e) {
            System.err.println("Erro ao carregar áudio: " + path);
            e.printStackTrace();
        }
    }

    public void setVolume(float volume) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float)
                    (Math.log10(Math.max(volume, 0.0001)) * 20); // converte para decibéis
            gainControl.setValue(dB);
        }
    }

    public void play() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
}
