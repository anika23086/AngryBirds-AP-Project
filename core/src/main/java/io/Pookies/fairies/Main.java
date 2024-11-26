package io.Pookies.fairies;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2D;

public class Main extends Game {
    public SpriteBatch batch;
    private Music backgroundMusic;
    public Sound clickSound;
    public float clickSoundVolume = 1.0f;
    public static final float PPM = 100f;

    @Override
    public void create(){
        Box2D.init();
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
        getScreen().dispose();
    }

    public static float toMeters(float pixels){
        return pixels / PPM;
    }

    public static float toPixels(float meters){
        return meters * PPM;
    }
}
