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
        // Set up global error handler first thing
        Thread.setDefaultUncaughtExceptionHandler(new NativeCrashHandler(this));

        try {
            // Initialize game resources
            batch = new SpriteBatch();
            clickSound = Gdx.audio.newSound(Gdx.files.internal("click.mp3"));

            // Initialize and configure background music
            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("backgroundmusic.mp3"));
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(0.1f);
            backgroundMusic.play();

            // Set initial screen
            setScreen(new OpeningPage(this));

        } catch (Exception e) {
            Gdx.app.error("Main", "Error during game initialization", e);
            safeCleanup();
            try {
                setScreen(new FailureScreen(this));
            } catch (Exception screenError) {
                Gdx.app.error("Main", "Failed to show failure screen", screenError);
                Gdx.app.exit();
            }
        }
    }

    private void safeCleanup() {
        try {
            if (batch != null) batch.dispose();
            if (backgroundMusic != null) {
                backgroundMusic.stop();
                backgroundMusic.dispose();
            }
            if (clickSound != null) clickSound.dispose();
        } catch (Exception e) {
            Gdx.app.error("Main", "Error during cleanup", e);
        }
    }

    @Override
    public void dispose() {
        try {
            super.dispose();
            safeCleanup();
        } catch (Exception e) {
            Gdx.app.error("Main", "Error during disposal", e);
        }
    }

    // Helper method to safely pause/resume background music
    public void pauseBackgroundMusic() {
        try {
            if (backgroundMusic != null && backgroundMusic.isPlaying()) {
                backgroundMusic.pause();
            }
        } catch (Exception e) {
            Gdx.app.error("Main", "Error pausing background music", e);
        }
    }

    public void resumeBackgroundMusic() {
        try {
            if (backgroundMusic != null && !backgroundMusic.isPlaying()) {
                backgroundMusic.play();
            }
        } catch (Exception e) {
            Gdx.app.error("Main", "Error resuming background music", e);
        }
    }
}
