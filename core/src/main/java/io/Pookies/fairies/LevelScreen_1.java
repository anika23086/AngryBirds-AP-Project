package io.Pookies.fairies;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class LevelScreen_1 implements Screen, InputProcessor {
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
    private boolean gameStarted;
    private Vector2 slingshotOrigin;
    private Vector2 dragStart;
    private float maxDragDistance = 600f; // Maximum distance bird can be pulled back
    private float launchPower;
    private float launchAngle;
    private ShapeRenderer shapeRenderer;
    private boolean showTrajectory;

    public LevelScreen_1(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        pinkBird = new PinkBird(120, 200);
        bubblePig = new BubblePig(1090, 340);
        stoneStructure = new StoneStructure(1100, 100);
        slingshot = new Slingshot(220, 130);
        clickSoundVolume = ((Main) game).clickSoundVolume;
        gameStarted = false;
        slingshotOrigin = new Vector2(220, 130);
        shapeRenderer = new ShapeRenderer();
        showTrajectory = false;

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

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();


        if (gameStarted) {
            pinkBird.update(delta);
        }

        batch.begin();
        batch.draw(levelBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        pinkBird.render(batch);
        bubblePig.render(batch);
        stoneStructure.render(batch);
        slingshot.render(batch);
        batch.end();


        // Draw trajectory preview
        if (showTrajectory && dragStart != null) {
            renderTrajectoryPreview();
        }


    }

    private void renderTrajectoryPreview() {
        // Prepare ShapeRenderer for drawing
        shapeRenderer.setProjectionMatrix(stage.getCamera().combined); // Use the stage's camera
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.ORANGE); // Dotted path color

        // Initialize trajectory variables
        float velocityMultiplier = 2000f; // Adjust this multiplier for scaling
        float launchSpeedX = (float) (launchPower * Math.cos(launchAngle) * velocityMultiplier);
        float launchSpeedY = (float) (launchPower * Math.sin(launchAngle) * velocityMultiplier);
        float x = pinkBird.getPosition().x;
        float y = pinkBird.getPosition().y;
        float vx = launchSpeedX / 1000f;  // Convert velocity to a smaller scale
        float vy = launchSpeedY / 1000f;
        float gravity = 9.8f; // Gravitational constant

        // Time intervals for trajectory points
        for (float t = 0; t < 3; t += 0.1f) {
            // Calculate position at time t
            float nextX = x + vx * t;
            float nextY = y + vy * t - 0.5f * gravity * t * t;

            // Draw a small circle to represent the dot
            shapeRenderer.circle(nextX, nextY, 5f); // Radius of the circle is 5
        }

        shapeRenderer.end();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        stage.getCamera().unproject(touchPos);

        // Check if touch is near the bird
        if (pinkBird.contains(touchPos.x, touchPos.y)) {
            // Store the initial drag start position
            dragStart = new Vector2(touchPos.x, touchPos.y);
            pinkBird.startDragging();
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        stage.getCamera().unproject(touchPos);

        if (dragStart != null) {
            // Calculate drag vector
            Vector2 dragVector = new Vector2(dragStart.x - touchPos.x, dragStart.y - touchPos.y);

            // Limit drag distance
            float dragDistance = dragVector.len();
            if (dragDistance > maxDragDistance) {
                dragVector.nor().scl(maxDragDistance);
            }

            // Update bird position based on drag
            pinkBird.drag(touchPos.x, touchPos.y);

            // Calculate launch power and angle
            launchPower = Math.min(dragDistance / maxDragDistance, 1f);
            launchAngle = (float) Math.atan2(dragVector.y, dragVector.x);

            // Enable trajectory preview
            showTrajectory = true;

        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (dragStart != null) {
            // Create a Vector2 for the touch position
            Vector2 touchPos = new Vector2(screenX, screenY);

            // Calculate the drag vector
            Vector2 dragVector = new Vector2(dragStart.x - touchPos.x, dragStart.y - touchPos.y);

            // Calculate drag distance and launch power
            float dragDistance = dragVector.len();
            float maxDragDistance = 200f; // Define your maximum allowable drag distance
            float launchPower = Math.min(dragDistance / maxDragDistance, 1f);

            // Adjust velocity multiplier
            float velocityMultiplier = 2000f; // Scales the launch velocity

            // Calculate launch speed components
            float launchSpeedX = (float) (launchPower * Math.cos(launchAngle) * velocityMultiplier);
            float launchSpeedY = (float) (launchPower * Math.sin(launchAngle) * velocityMultiplier);

            // Launch the bird with calculated speed
            pinkBird.launch(launchSpeedX, launchSpeedY);
            gameStarted = true;
            dragStart = null;
            showTrajectory = false;
            // Reset drag start


        }
        return true;
    }

    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    // Implement other InputProcessor methods (return false)
    @Override public boolean keyDown(int keycode) { return false; }
    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override public boolean scrolled(float amountX, float amountY) { return false; }

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
        if (shapeRenderer != null) shapeRenderer.dispose();
    }
}
