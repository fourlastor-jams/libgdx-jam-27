package io.github.fourlastor.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import io.github.fourlastor.perceptual.Perceptual;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SoundController {
    private static final float SOUND_VOLUME = 0.45f;
    private static final float MUSIC_VOLUME = 0.7f;

    @Inject
    public SoundController() {}

    public void play(Music music, float volume, boolean repeat) {
        music.setVolume(Perceptual.perceptualToAmplitude(volume * MUSIC_VOLUME));
        if (repeat) music.setLooping(true);
        music.play();
    }

    public void play(Music music) {
        play(music, 1f, false);
    }

    public void play(Sound sound, float volume, float pitch) {
        sound.play(volume * SOUND_VOLUME, pitch, 0);
    }

    public void play(Sound sound, float volume) {
        sound.play(volume * SOUND_VOLUME);
    }

    public void play(Sound sound) {
        play(sound, 1f);
    }
}
