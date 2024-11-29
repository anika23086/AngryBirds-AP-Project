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

public class LevelScreen implements Screen {
    private final Game game;
    private Stage stage;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Texture level1ButtonTexture;
    private Texture level2ButtonTexture;
    private Texture level3ButtonTexture;
    private Texture exitButtonTexture;
    private Texture incompleteButtonTexture;
    private ImageButton level1Button, level2Button, level3Button, exitButton;

    public static boolean level1Completed = false;
    public static boolean level2Completed = false;
    public static boolean level3Completed = false;

    public LevelScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        batch = new SpriteBatch();

        backgroundTexture = new Texture(Gdx.files.internal("levels_background.png"));
        level1ButtonTexture = new Texture(Gdx.files.internal("level1button.png"));
        level2ButtonTexture = new Texture(Gdx.files.internal("level2button.png"));
        level3ButtonTexture = new Texture(Gdx.files.internal("level3button.png"));
        exitButtonTexture = new Texture(Gdx.files.internal("exit_button.png"));
        incompleteButtonTexture = new Texture(Gdx.files.internal("incompleteButton.png"));

        level1Button = new ImageButton(new TextureRegionDrawable(new TextureRegion(level1ButtonTexture)));

        TextureRegion level2Region = new TextureRegion(
            level1Completed ? level2ButtonTexture : incompleteButtonTexture
        );
        level2Button = new ImageButton(new TextureRegionDrawable(level2Region));

        TextureRegion level3Region = new TextureRegion(
            level2Completed ? level3ButtonTexture : incompleteButtonTexture
        );
        level3Button = new ImageButton(new TextureRegionDrawable(level3Region));

        exitButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(exitButtonTexture)));

        level1Button.setPosition(
            Gdx.graphics.getWidth() / 3f - level1Button.getWidth() / 3f - 350,
            Gdx.graphics.getHeight() / 2f - 175
        );

        level2Button.setPosition(
            Gdx.graphics.getWidth() / 3f - level2Button.getWidth() / 3f+175,
            Gdx.graphics.getHeight() / 2f - 175
        );

        level3Button.setPosition(
            Gdx.graphics.getWidth() / 3f - level3Button.getWidth() / 3f + 650,
            Gdx.graphics.getHeight() / 2f - 175
        );

        exitButton.setPosition(
            Gdx.graphics.getWidth() - exitButton.getWidth() - 20,
            Gdx.graphics.getHeight() - exitButton.getHeight() - 20
        );

        level1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Main) game).clickSound.play(((Main) game).clickSoundVolume);
                game.setScreen(new LevelScreen_1(game));
            }
        });

        level2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Main) game).clickSound.play(((Main) game).clickSoundVolume);
                if (level1Completed) {
                    game.setScreen(new LevelScreen_2(game));
                } else {
                    System.out.println("Complete Level 1 first!");
                }
            }
        });

        level3Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Main) game).clickSound.play(((Main) game).clickSoundVolume);
                if (level2Completed) {
                    game.setScreen(new LevelScreen_3(game));
                } else {
                    System.out.println("Complete Level 2 first!");
                }
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Main) game).clickSound.play(((Main) game).clickSoundVolume);
                Gdx.app.exit();
            }
        });

        stage.addActor(level1Button);
        stage.addActor(level2Button);
        stage.addActor(level3Button);
        stage.addActor(exitButton);
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
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        backgroundTexture.dispose();
        level1ButtonTexture.dispose();
        level2ButtonTexture.dispose();
        level3ButtonTexture.dispose();
        exitButtonTexture.dispose();
        incompleteButtonTexture.dispose();
    }
}
