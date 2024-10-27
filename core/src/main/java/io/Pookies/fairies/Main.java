package io.Pookies.fairies;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
    public SpriteBatch batch;
    private Music backgroundMusic;
    public Sound clickSound;
    public float clickSoundVolume = 1.0f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        clickSound = Gdx.audio.newSound(Gdx.files.internal("click.mp3"));
        setScreen(new OpeningPage(this));

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("backgroundmusic.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.1f);
        backgroundMusic.play();
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundMusic.dispose();
        clickSound.dispose();
    }
}
