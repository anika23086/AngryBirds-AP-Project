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

public class FailureScreen implements Screen {
    private final Game game;
    private Stage stage;
    private SpriteBatch batch;
    private Texture failureBackground;
    private ImageButton exitButton;
    private ImageButton menuButton;
    private ImageButton retryButton;

    public FailureScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        failureBackground = new Texture(Gdx.files.internal("failureScreen.png"));

        createButtons();
        stage.addActor(exitButton);
        stage.addActor(menuButton);
        stage.addActor(retryButton);
    }

    private void createButtons() {
        TextureRegionDrawable exitDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("exit_button.png")));
        TextureRegionDrawable menuDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("menu_button.png")));
        TextureRegionDrawable retryDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("retry_button.png")));

        exitButton = new ImageButton(exitDrawable);
        menuButton = new ImageButton(menuDrawable);
        retryButton = new ImageButton(retryDrawable);

        float buttonSpacing = 10f; // Space between buttons
        float centerY = Gdx.graphics.getHeight() / 2f - 150f; // Move down by 150 pixels

        float totalWidth = exitButton.getWidth() + menuButton.getWidth() + retryButton.getWidth() + 2 * buttonSpacing;
        float startX = (Gdx.graphics.getWidth() - totalWidth) / 2f - 45f; // Center buttons horizontally

        exitButton.setPosition(startX, centerY);
        menuButton.setPosition(startX + exitButton.getWidth() + buttonSpacing, centerY);
        retryButton.setPosition(startX + exitButton.getWidth() + menuButton.getWidth() + 2 * buttonSpacing, centerY);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Main) game).clickSound.play(((Main) game).clickSoundVolume); // Play click sound
                Gdx.app.exit(); // Exit the game
            }
        });

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Main) game).clickSound.play(((Main) game).clickSoundVolume); // Play click sound
                System.out.println("Menu button clicked!");
                game.setScreen(new LevelScreen(game)); // Change to LevelScreen
            }
        });

        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Main) game).clickSound.play(((Main) game).clickSoundVolume); // Play click sound
                System.out.println("Retry button clicked!");
                game.setScreen(new LevelScreen_1(game)); // Change to LevelScreen_1 to retry the level
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(failureBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        if (stage != null) stage.dispose();
        if (batch != null) batch.dispose();
        if (failureBackground != null) failureBackground.dispose();
    }
}
