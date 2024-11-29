package io.Pookies.fairies;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LevelScreen_3 implements Screen {
    private final Game game;
    private SpriteBatch batch;
    private Texture level2game;
    private Bird purpleBird;
    private boolean levelCompleted = false;
    private Stage stage;
    private Texture pauseButtonTexture;
    private ImageButton pauseButton;
    private BubblePig bubblePig1, bubblePig2;
    private StoneStructure stoneStructure1, stoneStructure2, stoneStructure3, stoneStructure4;
    private Slingshot slingshot;

    public LevelScreen_3(Game game) {
        this.game = game;
        ((Main) game).setCurrentLevel(this);
        batch = new SpriteBatch();
        purpleBird = new PurpleBird(30, 80) {
            @Override
            public int getStrengthAgainst(String material) {
                return 0;
            }
        };
        bubblePig1 = new BubblePig(885, 380);
        bubblePig2 = new BubblePig(1130, 275);
        stoneStructure1 = new StoneStructure(1015, 380);
        stoneStructure2 = new StoneStructure(1130, 150);
        stoneStructure3 = new StoneStructure(1015, 150);
        stoneStructure4 = new StoneStructure(900, 150);
        slingshot = new Slingshot(220, 130);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        level2game = new Texture(Gdx.files.internal("level3_background.png"));
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        pauseButtonTexture = new Texture(Gdx.files.internal("pause_button.png"));
        TextureRegionDrawable pauseDrawable = new TextureRegionDrawable(pauseButtonTexture);
        pauseButton = new ImageButton(pauseDrawable);
        pauseButton.setPosition(Gdx.graphics.getWidth() - pauseButton.getWidth() - 20, Gdx.graphics.getHeight() - pauseButton.getHeight() - 20);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Main) game).clickSound.play(((Main) game).clickSoundVolume);
                game.setScreen(new PauseScreen(game, (Screen)LevelScreen_3.this));
            }
        });
        stage.addActor(pauseButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(level2game, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        purpleBird.draw(batch,3);
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
        purpleBird.dispose();
        bubblePig1.dispose();
        bubblePig2.dispose();
        stoneStructure1.dispose();
        stoneStructure2.dispose();
        stoneStructure3.dispose();
        stoneStructure4.dispose();
    }
}
