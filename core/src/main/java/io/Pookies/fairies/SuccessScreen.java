package io.Pookies.fairies;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import java.util.Random;

public class SuccessScreen implements Screen {
    private final Game game;
    private Stage stage;
    private SpriteBatch batch;
    private Texture successBackground;
    private Texture pixelTexture; // For confetti particles
    private ImageButton exitButton;
    private ImageButton menuButton;
    private ImageButton nextLevelButton;
    private Array<ConfettiParticle> confettiParticles;
    private Random random;

    public SuccessScreen(Game game) {
        this.game = game;
        confettiParticles = new Array<>();
        random = new Random();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        successBackground = new Texture(Gdx.files.internal("successScreen.png"));

        // Create a 1x1 white pixel texture for confetti
        pixelTexture = createPixelTexture();

        createButtons();
        stage.addActor(exitButton);
        stage.addActor(menuButton);
        stage.addActor(nextLevelButton);

        // Spawn initial confetti
        spawnConfetti(50);
    }

    private Texture createPixelTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private void createButtons() {
        TextureRegionDrawable exitDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("exit_button.png")));
        TextureRegionDrawable menuDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("menu_button.png")));
        TextureRegionDrawable nextLevelDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("next_level_button.png")));

        exitButton = new ImageButton(exitDrawable);
        menuButton = new ImageButton(menuDrawable);
        nextLevelButton = new ImageButton(nextLevelDrawable);

        float buttonSpacing = 5f;
        float centerY = Gdx.graphics.getHeight() / 2f - 150f;

        float totalWidth = exitButton.getWidth() + menuButton.getWidth() + nextLevelButton.getWidth() + 2 * buttonSpacing;
        float startX = (Gdx.graphics.getWidth() - totalWidth) / 2f - 50f;

        exitButton.setPosition(startX, centerY);
        menuButton.setPosition(startX + exitButton.getWidth() + buttonSpacing, centerY);
        nextLevelButton.setPosition(startX + exitButton.getWidth() + menuButton.getWidth() + 2 * buttonSpacing, centerY);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Main) game).clickSound.play(((Main) game).clickSoundVolume);
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

        nextLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Main) game).clickSound.play(((Main) game).clickSoundVolume);
                System.out.println("Next Level button clicked!");
                game.setScreen(new LevelScreen_2(game));
            }
        });
    }

    private void spawnConfetti(int count) {
        for (int i = 0; i < count; i++) {
            float x = random.nextFloat() * Gdx.graphics.getWidth();
            float y = Gdx.graphics.getHeight();
            Color color = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1);
            float velocityX = MathUtils.random(-100, 100);
            float velocityY = MathUtils.random(-300, -100);
            confettiParticles.add(new ConfettiParticle(x, y, velocityX, velocityY, color));
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(successBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Update and render confetti
        for (int i = confettiParticles.size - 1; i >= 0; i--) {
            ConfettiParticle particle = confettiParticles.get(i);
            particle.update(delta);
            particle.render(batch, pixelTexture);
            if (particle.isOffScreen()) {
                confettiParticles.removeIndex(i);
            }
        }

        batch.end();

        stage.act(delta);
        stage.draw();

        // Periodically spawn new confetti
        if (MathUtils.random() < 0.1f) {
            spawnConfetti(5);
        }
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
        if (successBackground != null) successBackground.dispose();
        if (pixelTexture != null) pixelTexture.dispose();
    }

    private class ConfettiParticle {
        private float x, y;
        private float velocityX, velocityY;
        private Color color;

        public ConfettiParticle(float x, float y, float velocityX, float velocityY, Color color) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            this.color = color;
        }

        public void update(float delta) {
            x += velocityX * delta;
            y += velocityY * delta;
        }

        public void render(SpriteBatch batch, Texture texture) {
            batch.setColor(color);
            batch.draw(texture, x, y, 10, 10);
            batch.setColor(Color.WHITE);
        }

        public boolean isOffScreen() {
            return y < -10;
        }
    }
}
