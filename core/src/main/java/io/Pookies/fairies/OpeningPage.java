package io.Pookies.fairies;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class OpeningPage implements Screen {
    private final Main game;
    private Stage stage;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Texture playButtonTexture;
    private Texture playButtonPressedTexture;
    private ImageButton playButton;

    public OpeningPage(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        backgroundTexture = new Texture(Gdx.files.internal("homepage.png"));
        batch = new SpriteBatch();

        playButtonTexture = new Texture(Gdx.files.internal("play_button.png"));
        playButtonPressedTexture = new Texture(Gdx.files.internal("play_button_pressed.png"));

        TextureRegionDrawable playButtonDrawable = new TextureRegionDrawable(new TextureRegion(playButtonTexture));
        TextureRegionDrawable playButtonPressedDrawable = new TextureRegionDrawable(new TextureRegion(playButtonPressedTexture));

        playButton = new ImageButton(playButtonDrawable, playButtonPressedDrawable);
        playButton.setPosition(
            Gdx.graphics.getWidth() / 2f - playButton.getWidth() / 2f,
            Gdx.graphics.getHeight() / 2f - playButton.getHeight() / 2f - 350f
        );

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.clickSound.play(game.clickSoundVolume);
                game.setScreen(new LevelScreen(game));
            }
        });

        stage.addActor(playButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (playButtonTexture != null) playButtonTexture.dispose();
        if (playButtonPressedTexture != null) playButtonPressedTexture.dispose();
    }
}
