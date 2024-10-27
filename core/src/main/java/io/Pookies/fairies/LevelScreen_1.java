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

public class LevelScreen_1 implements Screen {
    private final Game game;
    private Stage stage;
    private SpriteBatch batch;
    private Texture levelBackground;
    private Texture pauseButtonTexture;
    private Texture successButtonTexture;
    private Texture failureButtonTexture;
    private ImageButton pauseButton;
    private ImageButton successButton;
    private ImageButton failureButton;
    private PinkBird pinkBird;
    private BubblePig bubblePig;
    private StoneStructure stoneStructure;
    private Slingshot slingshot;
    private float clickSoundVolume;

    public LevelScreen_1(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        pinkBird = new PinkBird(30, 80);
        bubblePig = new BubblePig(1090, 340);
        stoneStructure = new StoneStructure(1100, 100);
        slingshot = new Slingshot(220, 130);
        clickSoundVolume = ((Main) game).clickSoundVolume;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        levelBackground = new Texture(Gdx.files.internal("gameLevel_1.png"));

        pauseButtonTexture = new Texture(Gdx.files.internal("pause_button.png"));
        TextureRegionDrawable pauseDrawable = new TextureRegionDrawable(pauseButtonTexture);
        pauseButton = new ImageButton(pauseDrawable);
        pauseButton.setPosition(Gdx.graphics.getWidth() - pauseButton.getWidth() - 20, Gdx.graphics.getHeight() - pauseButton.getHeight() - 20);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Main) game).clickSound.play(clickSoundVolume);
                game.setScreen(new PauseScreen(game));
            }
        });

        successButtonTexture = new Texture(Gdx.files.internal("success_button.png"));
        TextureRegionDrawable successDrawable = new TextureRegionDrawable(successButtonTexture);
        successButton = new ImageButton(successDrawable);
        successButton.setPosition(Gdx.graphics.getWidth() / 2f - successButton.getWidth() / 2f, Gdx.graphics.getHeight() / 2f + 50f);
        successButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Main) game).clickSound.play(clickSoundVolume);
                LevelScreen.level1Completed = true;
                game.setScreen(new SuccessScreen(game));
            }
        });

        failureButtonTexture = new Texture(Gdx.files.internal("failure_button.png"));
        TextureRegionDrawable failureDrawable = new TextureRegionDrawable(failureButtonTexture);
        failureButton = new ImageButton(failureDrawable);
        failureButton.setPosition(Gdx.graphics.getWidth() / 2f - failureButton.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - 50f);
        failureButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Main) game).clickSound.play(clickSoundVolume);
                game.setScreen(new FailureScreen(game));
            }
        });

        stage.addActor(pauseButton);
        stage.addActor(successButton);
        stage.addActor(failureButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(levelBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        pinkBird.render(batch);
        bubblePig.render(batch);
        stoneStructure.render(batch);
        slingshot.render(batch);
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
        if (levelBackground != null) levelBackground.dispose();
        if (pauseButtonTexture != null) pauseButtonTexture.dispose();
        if (successButtonTexture != null) successButtonTexture.dispose();
        if (failureButtonTexture != null) failureButtonTexture.dispose();
        if (pinkBird != null) pinkBird.dispose();
        if (bubblePig != null) bubblePig.dispose();
        if (stoneStructure != null) stoneStructure.dispose();
    }
}
