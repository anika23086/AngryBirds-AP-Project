package io.Pookies.fairies;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class LevelScreen_2 implements Screen {
    private final Game game;
    private SpriteBatch batch;
    private Texture level2game;
    private PurpleBird purplebird;
    private boolean levelCompleted = false;
    private Stage stage;
    private Texture pauseButtonTexture;
    private ImageButton pauseButton;
    private BubblePig bubblePig1, bubblePig2;
    private StoneStructure stoneStructure1, stoneStructure2, stoneStructure3, stoneStructure4;
    private Slingshot slingshot;
    private World world;
    private Body body;



    public LevelScreen_2(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        purplebird = new PurpleBird(30, 80, world);
        bubblePig1 = new BubblePig(world, "bubblepig.png",885, 380);
        bubblePig2 = new BubblePig(world, "bubblepig.png",1130, 275);
        stoneStructure1 = new StoneStructure(1015, 380, world);
        stoneStructure2 = new StoneStructure(1130, 150, world);
        stoneStructure3 = new StoneStructure(1015, 150, world);
        stoneStructure4 = new StoneStructure(900, 150, world);
        slingshot = new Slingshot(world,30, 80);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        level2game = new Texture(Gdx.files.internal("gameLevel_2.png"));
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        pauseButtonTexture = new Texture(Gdx.files.internal("pause_button.png"));
        TextureRegionDrawable pauseDrawable = new TextureRegionDrawable(pauseButtonTexture);
        pauseButton = new ImageButton(pauseDrawable);
        pauseButton.setPosition(Gdx.graphics.getWidth() - pauseButton.getWidth() - 20, Gdx.graphics.getHeight() - pauseButton.getHeight() - 20);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PauseScreen(game));
            }
        });
        stage.addActor(pauseButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(level2game, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        purplebird.render(batch);
        bubblePig1.render(batch);
        bubblePig2.render(batch);
        stoneStructure1.render(batch);
        stoneStructure2.render(batch);
        stoneStructure3.render(batch);
        stoneStructure4.render(batch);
        slingshot.render(batch);
        batch.end();
        if (Gdx.input.justTouched() && !levelCompleted) {
            completeLevel();
        }
        stage.act(delta);
        stage.draw();
    }

    private void completeLevel() {
        levelCompleted = true;
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
        batch.dispose();
        level2game.dispose();
        if (stage != null) stage.dispose();
        if (pauseButtonTexture != null) pauseButtonTexture.dispose();
        purplebird.dispose();
        bubblePig1.dispose();
        bubblePig2.dispose();
        stoneStructure1.dispose();
        stoneStructure2.dispose();
        stoneStructure3.dispose();
        stoneStructure4.dispose();
    }
}
