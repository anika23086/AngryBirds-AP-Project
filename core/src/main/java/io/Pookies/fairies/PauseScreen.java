package io.Pookies.fairies;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class PauseScreen implements Screen {
    private final Game game;
    private Stage stage;
    private SpriteBatch batch;
    private Texture pauseBackground;
    private Texture resumeButtonTexture;
    private Texture menuButtonTexture;
    private Texture retryButtonTexture;
    private Texture exitButtonTexture;
    private Screen previousScreen;

    private ImageButton resumeButton;
    private ImageButton menuButton;
    private ImageButton retryButton;
    private ImageButton exitButton;

    public PauseScreen(Game game, Screen previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen;
    }
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        pauseBackground = new Texture(Gdx.files.internal("pauseScreen.png"));

        resumeButtonTexture = new Texture(Gdx.files.internal("resume_button.png"));
        menuButtonTexture = new Texture(Gdx.files.internal("menu_button.png"));
        retryButtonTexture = new Texture(Gdx.files.internal("retry_button.png"));
        exitButtonTexture = new Texture(Gdx.files.internal("exit_button.png"));

        resumeButton = new ImageButton(new TextureRegionDrawable(resumeButtonTexture));
        menuButton = new ImageButton(new TextureRegionDrawable(menuButtonTexture));
        retryButton = new ImageButton(new TextureRegionDrawable(retryButtonTexture));
        exitButton = new ImageButton(new TextureRegionDrawable(exitButtonTexture));

        float buttonWidth = resumeButton.getWidth();
        float buttonHeight = resumeButton.getHeight();

        resumeButton.setPosition(Gdx.graphics.getWidth() / 2f - buttonWidth * 1.5f-40f, Gdx.graphics.getHeight() / 2f-120f);
        menuButton.setPosition(Gdx.graphics.getWidth() / 2f - buttonWidth / 2f-40f, Gdx.graphics.getHeight() / 2f-120f);
        retryButton.setPosition(Gdx.graphics.getWidth() / 2f + buttonWidth * 0.5f-40f, Gdx.graphics.getHeight() / 2f-120f);
        exitButton.setPosition(Gdx.graphics.getWidth() / 2f - exitButton.getWidth() / 2f-40f, Gdx.graphics.getHeight() / 2f - 225f); // Position below menu button

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Main) game).clickSound.play(((Main) game).clickSoundVolume);
                if (previousScreen != null) {
                    dispose(); // Only dispose PauseScreen resources
                    game.setScreen(previousScreen); // Return to previous screen
                }
            }
        });

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Main) game).clickSound.play(((Main) game).clickSoundVolume);
                if (previousScreen != null) {
                    previousScreen.dispose(); // Dispose previous screen
                }
                dispose(); // Dispose pause screen
                game.setScreen(new LevelScreen(game));
            }
        });

        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Main) game).clickSound.play(((Main) game).clickSoundVolume);

                Screen currentLevel = ((Main) game).getCurrentLevel();

                if (previousScreen != null) {
                    previousScreen.dispose(); // Dispose the previous screen when retrying
                    // Create a new instance of the same level type
                    if (previousScreen instanceof LevelScreen_1) {
                        game.setScreen(new LevelScreen_1(game));
                    } else if (previousScreen instanceof LevelScreen_2) {
                        game.setScreen(new LevelScreen_2(game));
                    } else if (previousScreen instanceof LevelScreen_3) {
                        game.setScreen(new LevelScreen_3(game));
                    } else {
                        game.setScreen(new LevelScreen_1(game));
                    }
                }
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Main) game).clickSound.play(((Main) game).clickSoundVolume);
                if (previousScreen != null) {
                    previousScreen.dispose();
                }
                Gdx.app.exit();            }
        });

        stage.addActor(resumeButton);
        stage.addActor(menuButton);
        stage.addActor(retryButton);
        stage.addActor(exitButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(pauseBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
            stage = null;
        }
        if (batch != null) {
            batch.dispose();
            batch = null;
        }
        if (pauseBackground != null) {
            pauseBackground.dispose();
            pauseBackground = null;
        }
        if (resumeButtonTexture != null) {
            resumeButtonTexture.dispose();
            resumeButtonTexture = null;
        }
        if (menuButtonTexture != null) {
            menuButtonTexture.dispose();
            menuButtonTexture = null;
        }
        if (retryButtonTexture != null) {
            retryButtonTexture.dispose();
            retryButtonTexture = null;
        }
        if (exitButtonTexture != null) {
            exitButtonTexture.dispose();
            exitButtonTexture = null;
        }
    }
}
