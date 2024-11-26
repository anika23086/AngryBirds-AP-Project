package io.Pookies.fairies;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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
    private boolean birdLaunched = false;
    private boolean isDragging = false;
    private Vector2 dragStart;
    private Vector2 originalBirdPosition;
    private Body groundBody;
    private static final float MAX_DRAG_DISTANCE = 200f;
    private static final float LAUNCH_MULTIPLIER = 10f;

    public LevelScreen_1(Game game) {
        world = new World(new Vector2(0, -9.8f), true);
        this.game = game;
        batch = new SpriteBatch();
        pinkBird = new PinkBird(world, "pinkbird.png", 30, 80);
        bubblePig = new BubblePig(world, "bubblepig.png", 1090, 340);
        stoneStructure = new StoneStructure(1100, 100,world);
        slingshot = new Slingshot(world, 30, 80);
        clickSoundVolume = ((Main) game).clickSoundVolume;

        // Create the ground
        createGround();
        originalBirdPosition = new Vector2(pinkBird.getX(), pinkBird.getY);
    }

    private void createGround() {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(0, 0);
        groundBody = world.createBody(groundBodyDef);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(Gdx.graphics.getWidth(), 1.0f);
        groundBody.createFixture(groundBox, 0.0f);
        groundBox.dispose();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
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

        world.step(1/60f, 6, 2);

        batch.begin();
        batch.draw(levelBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        pinkBird.render(batch);
        bubblePig.render(batch);
        stoneStructure.render(batch);
        slingshot.render(batch);
        batch.end();

        if (!birdLaunched) {
            handleDragging();
        }

        stage.act(delta);
        stage.draw();

        checkForVictory();
    }

    private void handleDragging() {
        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());

        if (Gdx.input.justTouched()) {
            // Check if the touch is near the bird
            if (isNearBird(mousePos)) {
                isDragging = true;
                dragStart = mousePos.cpy();
            }
        }

        if (isDragging && Gdx.input.isTouched()) {
            // Calculate drag vector
            Vector2 dragVector = mousePos.sub(dragStart);

            // Limit drag distance
            if (dragVector.len() > MAX_DRAG_DISTANCE) {
                dragVector.setLength(MAX_DRAG_DISTANCE);
            }

            // Position bird relative to original position
            Vector2 newBirdPos = originalBirdPosition.cpy().add(dragVector.scl(-1));
            pinkBird.setPosition(newBirdPos.x, newBirdPos.y);
        }

        // Launch bird when touch is released
        if (isDragging && !Gdx.input.isTouched()) {
            launchBird(mousePos);
            isDragging = false;
        }
    }

    private boolean isNearBird(Vector2 mousePos){
        float dragRadius = 50f;
        return mousePos.dst(pinkBird.getX(), pinkBird.getY()) < dragRadius;
    }

    private void launchBird(Vector2 releasePos) {
        // Calculate launch vector based on drag
        Vector2 launchVector = dragStart.sub(releasePos);

        // Calculate launch angle and magnitude
        float angle = (float) Math.atan2(launchVector.y, launchVector.x);
        float magnitude = Math.min(launchVector.len(), MAX_DRAG_DISTANCE);

        // Apply launch velocity
        float velocityX = (float) (magnitude * LAUNCH_MULTIPLIER * Math.cos(angle));
        float velocityY = (float) (magnitude * LAUNCH_MULTIPLIER * Math.sin(angle));

        // Launch the bird
        pinkBird.launch(velocityX, velocityY);
        birdLaunched = true;
    }

    private void checkForVictory() {
        if (bubblePig.isDestroyed() && stoneStructure.isDestroyed()) {
            System.out.println("Victory achieved!");
            game.setScreen(new SuccessScreen(game));
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
        if (levelBackground != null) levelBackground.dispose();
        if (pauseButtonTexture != null) pauseButtonTexture.dispose();
        if (pinkBird != null) pinkBird.dispose();
        if (bubblePig != null) bubblePig.dispose();
        if (stoneStructure != null) stoneStructure.dispose();
        if (world != null) world.dispose();
    }
}
