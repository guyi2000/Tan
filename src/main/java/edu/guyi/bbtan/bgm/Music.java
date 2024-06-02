package edu.guyi.bbtan.bgm;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.util.Objects;

public class Music {
    public void playMusic() {
        try {
            BufferedInputStream audio = new BufferedInputStream(
                    Objects.requireNonNull(Music.class.getResourceAsStream("/bgm/bgm.wav"))
            );
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audio);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
