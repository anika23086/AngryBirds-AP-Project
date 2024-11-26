package io.Pookies.fairies;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
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
    private ImageButton pauseButton;
    private PinkBird pinkBird;
    private BubblePig bubblePig;
    private StoneStructure stoneStructure;
    private Slingshot slingshot;
    private float clickSoundVolume;
    private World world;

    public LevelScreen_1(Game game) {
        world = new World(new Vector2(0, -9.8f), true);
        this.game = game;
        batch = new SpriteBatch();
        pinkBird = new PinkBird(30, 80, world);
        bubblePig = new BubblePig(1090, 340, world);
        stoneStructure = new StoneStructure(1100, 100, world);
        slingshot = new Slingshot(30,80);
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

        stage.addActor(pauseButton);
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
        if (pinkBird != null) pinkBird.dispose();
        if (bubblePig != null) bubblePig.dispose();
        if (stoneStructure != null) stoneStructure.dispose();
    }
}
