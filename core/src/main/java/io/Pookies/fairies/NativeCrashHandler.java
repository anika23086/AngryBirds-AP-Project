package io.Pookies.fairies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;

public class NativeCrashHandler implements Thread.UncaughtExceptionHandler {
    private final Game game;

    public NativeCrashHandler(Game game) {
        this.game = game;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        // Log the error
        Gdx.app.error("NativeCrashHandler", "Native crash detected", throwable);

        // Post the screen transition to the main thread
        Gdx.app.postRunnable(() -> {
            try {
                game.setScreen(new FailureScreen(game));
            } catch (Exception e) {
                Gdx.app.error("NativeCrashHandler", "Failed to show failure screen", e);
                // Force exit if we can't show the failure screen
                Gdx.app.exit();
            }
        });
    }
}
