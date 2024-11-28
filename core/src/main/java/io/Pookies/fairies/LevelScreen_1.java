package io.Pookies.fairies;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
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
    private StoneStructure stoneStructure1, stoneStructure2;
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
    private boolean pigDestroyed = false;
    private boolean structureDestroyed = false;
    private boolean birdDestroyed = false;
    private Rectangle birdRectangle;
    private Rectangle pigRectangle;
    private Rectangle structureRectangle;
    private BitmapFont scoreFont;
    private int currentScore;
    private static final int PIG_POINTS = 200;
    private static final int STRUCTURE_POINTS = 100;
    private boolean levelComplete = false;
    private float levelCompleteTimer = 0;
    private static final float LEVEL_COMPLETE_DELAY = 1.0f;
    private boolean checkingForFailure = false;
    private float failureCheckTimer = 0;
    private static final float FAILURE_CHECK_DELAY = 2.0f;
    private static final float VELOCITY_THRESHOLD = 1.0f;
    private static final float SCREEN_BOUNDS_MARGIN = 100f;
    private boolean isErrorHandlerSet = false;
    private Texture sparkleTexture;        // Sparkle texture
    private Vector2 sparklePosition;       // Position of the sparkles
    private float sparkleTimer;            // Timer for controlling sparkles
    private static final float SPARKLE_DURATION = 1.0f; // Sparkle visibility duration in seconds
    private float sparkleAlpha;            // Transparency of sparkles

    public LevelScreen_1(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        pinkBird = new PinkBird(120, 200); //COMMIT NO WORKKK
        bubblePig = new BubblePig(1090, 560);
        stoneStructure1 = new StoneStructure(1100, 100);
        stoneStructure2= new StoneStructure(1100, 320);
        slingshot = new Slingshot(220, 130);
        clickSoundVolume = ((Main) game).clickSoundVolume;
        gameStarted = false;
        slingshotOrigin = new Vector2(220, 130);
        shapeRenderer = new ShapeRenderer();
        showTrajectory = false;
        birdRectangle = new Rectangle(120, 200, 50, 50); // Use approximate sizes
        pigRectangle = new Rectangle(1090, 560, 50, 50);
        structureRectangle = new Rectangle(1100, 100, 100, 200);
        scoreFont = new BitmapFont();
        scoreFont.setColor(Color.YELLOW);
        scoreFont.getData().setScale(3.0f); // Make the font larger
        currentScore = 0;
        checkingForFailure = false;
        failureCheckTimer = 0;
        setupErrorHandler();
        sparkleTexture = new Texture(Gdx.files.internal("sparkles.png"));
        sparklePosition = new Vector2();
        sparkleTimer = 0;
        sparkleAlpha = 1.0f;
    }

    private void setupErrorHandler() {
        if (!isErrorHandlerSet) {
            Thread.setDefaultUncaughtExceptionHandler(new NativeCrashHandler(game));

            Gdx.app.setLogLevel(Application.LOG_DEBUG);
            Gdx.app.error("LevelScreen_1", "Setting up error handler");

            isErrorHandlerSet = true;
        }
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
        try {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            if (levelComplete) {
                levelCompleteTimer += delta;
                if (levelCompleteTimer >= LEVEL_COMPLETE_DELAY) {
                    if (!LevelScreen.level1Completed) {
                        LevelScreen.level1Completed = true;
                    }
                    game.setScreen(new SuccessScreen(game));
                    return;
                }
            }

            // Sparkle timer and alpha management
            if (sparkleTimer > 0) {
                sparkleTimer -= delta;
                sparkleAlpha = Math.max(0, sparkleTimer / SPARKLE_DURATION); // Fade out effect
            }


            if (pigDestroyed && structureDestroyed) {
                levelCompleteTimer += delta;
                if (levelCompleteTimer >= LEVEL_COMPLETE_DELAY) {
                    checkLevelCompletion(); // New method to check level completion
                }
            }

            if (gameStarted && !levelComplete && !checkingForFailure) {
                checkingForFailure = true;
            }

            if (checkingForFailure && !pigDestroyed) {
                checkForFailureConditions(delta);
            }

            if (gameStarted && !pigDestroyed) {
                bubblePig.update(delta);
                pigRectangle.setPosition(bubblePig.getPosition().x, bubblePig.getPosition().y);

                // Check if pig falls below screen
                if (bubblePig.getPosition().y < -100) {
                    pigDestroyed = true;
                }
            }

            if (gameStarted && !birdDestroyed) {
                pinkBird.update(delta);
                birdRectangle.setPosition(pinkBird.getPosition().x, pinkBird.getPosition().y);
                checkCollisions();
            }

            stage.act(delta);
            stage.draw();

            batch.begin();
            batch.draw(levelBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            if (!birdDestroyed) {
                pinkBird.render(batch);
            }
            if (!pigDestroyed) {
                bubblePig.render(batch);
            }

            // Render sparkles when timer is active
            if (sparkleTimer > 0) {
                batch.setColor(1, 1, 1, sparkleAlpha); // Adjust transparency
                batch.draw(sparkleTexture, sparklePosition.x, sparklePosition.y, 100, 100);
                batch.setColor(1, 1, 1, 1); // Reset color
            }

            if (!structureDestroyed) {
                stoneStructure1.render(batch);
                stoneStructure2.render(batch);
            }
            slingshot.render(batch);
            scoreFont.draw(batch, "Score: " + currentScore, 1700, Gdx.graphics.getHeight() - 20);

            if (showTrajectory && dragStart != null) {
                renderTrajectoryPreview();
            }

            batch.end();

        } catch (Throwable t) {
            handleCrash(t);
        }
    }

    private void checkLevelCompletion() {
        if (!LevelScreen.level1Completed) {
            LevelScreen.level1Completed = true; // Mark level as completed
        }
        game.setScreen(new SuccessScreen(game)); // Transition to success screen
    }

    private void updateBirdPosition(float delta) {
        try {
            pinkBird.update(delta);
            birdRectangle.setPosition(pinkBird.getPosition().x, pinkBird.getPosition().y);
            checkCollisions();
        } catch (Throwable t) {
            handleCrash(t);
        }
    }

    private void renderGameElements() {
        try {
            batch.begin();
            batch.draw(levelBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            if (!birdDestroyed) {
                pinkBird.render(batch);
            }
            if (!pigDestroyed) {
                bubblePig.render(batch);
            }
            if (!structureDestroyed) {
                stoneStructure1.render(batch);
                stoneStructure2.render(batch);
            }
            slingshot.render(batch);
            scoreFont.draw(batch, "Score: " + currentScore, 1700, Gdx.graphics.getHeight() - 20);

            batch.end();
        } catch (Throwable t) {
            if (batch != null && batch.isDrawing()) {
                batch.end();
            }
            handleCrash(t);
        }
    }

    private void handleCrash(Throwable throwable) {
        Gdx.app.error("LevelScreen_1", "Crash detected", throwable);
        try {
            // Ensure we're on the main thread when switching screens
            Gdx.app.postRunnable(() -> {
                try {
                    dispose(); // Clean up resources
                    game.setScreen(new FailureScreen(game));
                } catch (Exception e) {
                    Gdx.app.error("LevelScreen_1", "Failed to show failure screen", e);
                    Gdx.app.exit();
                }
            });
        } catch (Exception e) {
            Gdx.app.error("LevelScreen_1", "Failed to handle crash", e);
            Gdx.app.exit();
        }
    }

    private void checkForFailureConditions(float delta) {
        failureCheckTimer += delta;

        if (failureCheckTimer >= FAILURE_CHECK_DELAY) {
            // Check if bird is off screen
            Vector2 birdPos = pinkBird.getPosition();
            boolean isOffScreen = birdPos.x < -SCREEN_BOUNDS_MARGIN ||
                birdPos.x > Gdx.graphics.getWidth() + SCREEN_BOUNDS_MARGIN ||
                birdPos.y < -SCREEN_BOUNDS_MARGIN ||
                birdPos.y > Gdx.graphics.getHeight() + SCREEN_BOUNDS_MARGIN;

            // Check if bird has stopped moving (get velocity from your PinkBird class)
            Vector2 birdVelocity = pinkBird.getVelocity();
            boolean hasStopped = birdVelocity.len() < VELOCITY_THRESHOLD;

            // If bird is off screen or has stopped without hitting the pig, transition to failure screen
            if ((isOffScreen || hasStopped) && !pigDestroyed) {
                game.setScreen(new FailureScreen(game));
            }
        }
    }

    private void checkCollisions() {
        if (!pigDestroyed && birdRectangle.overlaps(pigRectangle)) {
            handlePigCollision();
        }

        if (!structureDestroyed && birdRectangle.overlaps(structureRectangle)) {
            handleStructureCollision();
        }
    }

    private void handlePigCollision() {
        if (!pigDestroyed) {  // Check to prevent multiple score additions
            pigDestroyed = true;
            birdDestroyed = true;
            currentScore += PIG_POINTS;
            System.out.println("Pig hit! Score +" + PIG_POINTS + " (Total: " + currentScore + ")");

            // Set sparkle position to pig's current position
            sparklePosition.set(bubblePig.getPosition());
            sparkleTimer = SPARKLE_DURATION;
            sparkleAlpha = 1.0f;  // Reset alpha for full opacity

            levelComplete = true;
            LevelScreen.level1Completed = true;
        }
    }

    private void handleStructureCollision() {
        if (!structureDestroyed) {  // Check to prevent multiple score additions
            structureDestroyed = true;
            birdDestroyed = true;
            currentScore += STRUCTURE_POINTS;
            System.out.println("Structure hit! Score +" + STRUCTURE_POINTS + " (Total: " + currentScore + ")");
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

    private void checkPigsOnDestroyedStructures() {
        if (structureDestroyed) {
            // Check if the pig is on or very close to the structure
            Rectangle structureRect1 = new Rectangle(1100, 100, 100, 200);
            Rectangle structureRect2 = new Rectangle(1100, 320, 100, 200);

            // Check for Pig on Structure 1
            if (!pigDestroyed && isPigDirectlyAboveStructure(structureRect1)) {
                handlePigFall();
            }

            if (!pigDestroyed && isPigDirectlyAboveStructure(structureRect2)) {
                handlePigFall();}
        }
    }

    private boolean isOverlapping(Rectangle pigRect, Rectangle structureRect) {
        // Check if pig is sitting on top of or within the structure
        return pigRect.x >= structureRect.x &&
            pigRect.x <= structureRect.x + structureRect.width &&
            pigRect.y >= structureRect.y &&
            pigRect.y <= structureRect.y + structureRect.height;
    }

    private void handlePigFall() {
        if (!pigDestroyed) {
            // Add falling mechanism
            bubblePig.startFalling(); // Assuming you'll add this method to BubblePig class

            // Award points for pig falling
            pigDestroyed = true;
            currentScore += PIG_POINTS;
            System.out.println("Pig fell! Score +" + PIG_POINTS + " (Total: " + currentScore + ")");
        }
    }

    private boolean isPigDirectlyAboveStructure(Rectangle structureRect) {
        // Check if pig's x-coordinate is within the width of the structure
        boolean withinStructureWidth =
            pigRectangle.x >= structureRect.x &&
                pigRectangle.x <= structureRect.x + structureRect.width;

        // Check if pig is directly above the structure
        // (y position of pig is just above the top of the structure)
        boolean directlyAbove =
            pigRectangle.y >= structureRect.y + structureRect.height &&
                pigRectangle.y <= structureRect.y + structureRect.height + 100; // Allow some vertical tolerance

        return withinStructureWidth && directlyAbove;
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
        try {
            if (stage != null) stage.dispose();
            if (batch != null) batch.dispose();
            if (levelBackground != null) levelBackground.dispose();
            if (pauseButtonTexture != null) pauseButtonTexture.dispose();
            if (pinkBird != null) pinkBird.dispose();
            if (bubblePig != null) bubblePig.dispose();
            if (stoneStructure1 != null) stoneStructure1.dispose();
            if (stoneStructure2 != null) stoneStructure2.dispose();
            if (shapeRenderer != null) shapeRenderer.dispose();
            if (sparkleTexture != null) sparkleTexture.dispose();
            if (scoreFont != null) scoreFont.dispose();
        } catch (Exception e) {
            Gdx.app.error("LevelScreen_1", "Error during disposal", e);
        }
    }
}
