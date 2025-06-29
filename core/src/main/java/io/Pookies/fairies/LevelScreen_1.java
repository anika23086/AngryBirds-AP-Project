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
    public PinkBird pinkBird;
    public BubblePig bubblePig;
    private StoneStructure stoneStructure1, stoneStructure2;
    private Slingshot slingshot;
    private float clickSoundVolume;
    private boolean gameStarted;
    private Vector2 slingshotOrigin;
    private Vector2 dragStart;
    private float maxDragDistance = 600f;
    private float launchPower;
    private float launchAngle;
    private ShapeRenderer shapeRenderer;
    public boolean pigDestroyed = false;
    public boolean stoneStructure1Destroyed = false;
    public boolean stoneStructure2Destroyed = false;
    public boolean birdDestroyed = false;
    private Rectangle birdRectangle;
    private Rectangle pigRectangle;
    private BitmapFont scoreFont;
    private int currentScore;
    private static final int PIG_POINTS = 200;
    private static final int STRUCTURE_POINTS = 100;
    public boolean levelComplete = false;
    private float levelCompleteTimer = 0;
    private static final float LEVEL_COMPLETE_DELAY = 1.0f;
    private boolean checkingForFailure = false;
    private float failureCheckTimer = 0;
    static final float FAILURE_CHECK_DELAY = 2.0f;
    private static final float VELOCITY_THRESHOLD = 1.0f;
    private static final float SCREEN_BOUNDS_MARGIN = 100f;
    private boolean isErrorHandlerSet = false;
    private Texture sparkleTexture;        // Sparkle texture
    private Vector2 sparklePosition;       // Position of the sparkles
    private float sparkleTimer;            // Timer for controlling sparkles
    private static final float SPARKLE_DURATION = 1.0f; // Sparkle visibility duration in seconds
    private float sparkleAlpha;            // Transparency of sparkles
    private ImageButton pauseButton;
    private Texture pauseButtonTexture;
    private ShapeRenderer debugRenderer = new ShapeRenderer();
    private Texture simplePauseTexture;
    private Rectangle pauseButtonBounds;
    private static final int PAUSE_BUTTON_SIZE = 50;
    private static final int PADDING = 10;
    public boolean failureTriggered = false;
    private static final int MAX_BIRDS = 1; // Adjust based on the number of birds
    public int birdsUsed = 0;              // Track how many birds have been used


    public LevelScreen_1(Game game) {
        this.game = game;
        ((Main) game).setCurrentLevel(this);
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
        birdRectangle = new Rectangle(120, 200, 50, 50); // Use approximate sizes
        pigRectangle = new Rectangle(1090, 560, 50, 50);
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
        simplePauseTexture = new Texture(Gdx.files.internal("pause_button.png"));
        pauseButtonBounds = new Rectangle(
            Gdx.graphics.getWidth() - PAUSE_BUTTON_SIZE - PADDING,
            Gdx.graphics.getHeight() - PAUSE_BUTTON_SIZE - PADDING,
            PAUSE_BUTTON_SIZE,
            PAUSE_BUTTON_SIZE
        );
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

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);

        // Add pause button
        pauseButtonTexture = new Texture(Gdx.files.internal("pause_button.png"));
        TextureRegionDrawable pauseDrawable = new TextureRegionDrawable(pauseButtonTexture);
        pauseButton = new ImageButton(pauseDrawable);

        // Position the button in the top-right corner with proper padding
        pauseButton.setPosition(
            Gdx.graphics.getWidth() - pauseButton.getWidth(),
            Gdx.graphics.getHeight() - pauseButton.getHeight()
        );

        // Add click listener
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Main) game).clickSound.play(((Main) game).clickSoundVolume);
                // Don't dispose the current screen when pausing
                game.setScreen(new PauseScreen(game, (Screen)LevelScreen_1.this));
            }
        });

        // Debug statement to verify button addition
        Gdx.app.log("LevelScreen_1", "Pause button added to stage");
        Gdx.app.log("LevelScreen_1", "Pause button position: (" + pauseButton.getX() + ", " + pauseButton.getY() + "), size: (" + pauseButton.getWidth() + ", " + pauseButton.getHeight() + ")");
    }

    @Override
    public void render(float delta) {
        try {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();
            batch.draw(levelBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            // Check for level completion
            if (levelComplete) {
                levelCompleteTimer += delta;
                if (levelCompleteTimer >= LEVEL_COMPLETE_DELAY) {
                    if (!LevelScreen.level1Completed) {
                        LevelScreen.level1Completed = true;
                    }
                    game.setScreen(new SuccessScreen(game,1));
                    return;
                }
            }

            if (gameStarted && !levelComplete) {
                if (!checkingForFailure) {
                    checkingForFailure = true;
                }
                checkForFailureConditions(delta);
            }

            // Render pig and bird updates
            if (gameStarted && !pigDestroyed) {
                bubblePig.update(delta);
                pigRectangle.setPosition(bubblePig.getPosition().x, bubblePig.getPosition().y);

                // Check if pig falls below the screen
                if (bubblePig.getPosition().y < -100) {
                    pigDestroyed = true;
                }
            }

            if (gameStarted && !birdDestroyed) {
                pinkBird.update(delta);
                birdRectangle.setPosition(pinkBird.getPosition().x, pinkBird.getPosition().y);
                checkCollisions();
            }

            // Draw other game elements
            if (!birdDestroyed) {
                pinkBird.draw(batch,1);
            }
            if (!pigDestroyed) {
                bubblePig.render(batch);
            }

            // Render sparkles
            if (sparkleTimer > 0) {
                sparkleTimer -= delta;
                sparkleAlpha = Math.max(0, sparkleTimer / SPARKLE_DURATION);
                batch.setColor(1, 1, 1, sparkleAlpha);
                batch.draw(sparkleTexture, sparklePosition.x, sparklePosition.y, 100, 100);
                batch.setColor(1, 1, 1, 1); // Reset color
            }

            if (!stoneStructure1Destroyed) {
                stoneStructure1.render(batch);
            }
            if (!stoneStructure2Destroyed) {
                stoneStructure2.render(batch);
            }

            slingshot.render(batch);

            // Render score counter
            scoreFont.draw(batch, "Score: " + currentScore, 40, Gdx.graphics.getHeight() - 115);

            batch.end();

            stage.act(delta);
            stage.draw();
        } catch (Throwable t) {
            handleCrash(t);
        }
    }

    private void triggerFailure() {
        if (!failureTriggered) {
            failureTriggered = true;
            Gdx.app.log("LevelScreen_1", "Triggering Failure Screen");

            Gdx.app.postRunnable(() -> {
                try {
                    game.setScreen(new FailureScreen(game, 1));
                } catch (Exception e) {
                    Gdx.app.error("LevelScreen_1", "Error switching to failure screen", e);
                }
            });
        }
    }

    private void checkLevelCompletion() {
        if (!LevelScreen.level1Completed) {
            LevelScreen.level1Completed = true; // Mark level as completed
        }
        game.setScreen(new SuccessScreen(game,1)); // Transition to success screen
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

    private void handleCrash(Throwable throwable) {
        Gdx.app.error("LevelScreen_1", "Crash detected", throwable);
        try {
            // Ensure we're on the main thread when switching screens
            Gdx.app.postRunnable(() -> {
                try {
                    dispose(); // Clean up resources
                    game.setScreen(new FailureScreen(game, 1));
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

    public void checkForFailureConditions(float delta) {
        failureCheckTimer += delta;

        if (failureCheckTimer >= FAILURE_CHECK_DELAY) {
            // Reset timer for periodic checking
            failureCheckTimer = 0;

            if (gameStarted && !levelComplete) {
                Vector2 birdPos = pinkBird.getPosition();
                Vector2 birdVelocity = pinkBird.getVelocity();

                boolean isOffScreen = birdPos.x < -SCREEN_BOUNDS_MARGIN ||
                    birdPos.x > Gdx.graphics.getWidth() + SCREEN_BOUNDS_MARGIN ||
                    birdPos.y < -SCREEN_BOUNDS_MARGIN ||
                    birdPos.y > Gdx.graphics.getHeight() + SCREEN_BOUNDS_MARGIN;

                boolean hasStopped = birdVelocity.len() < VELOCITY_THRESHOLD;

                // If bird is off screen or stopped, consider it used
                if ((isOffScreen || hasStopped) && !birdDestroyed) {
                    birdDestroyed = true; // Mark the bird as used
                    birdsUsed++;          // Increment bird counter
                    Gdx.app.log("LevelScreen_1", "Bird destroyed. Birds used: " + birdsUsed);
                }

                // Check failure condition: all birds used and pig still alive
                if (birdsUsed >= MAX_BIRDS && !pigDestroyed) {
                    triggerFailure();
                }
            }
        }
    }

    private void checkCollisions() {
        // First, check pig collision (prioritize pig over structures)
        if (!pigDestroyed && isColliding(pinkBird, bubblePig)) {
            handlePigCollision();
            return; // Exit early if pig is hit to prevent structure collision
        }

        // Then check structure collisions
        if (!stoneStructure1Destroyed && isColliding(pinkBird, stoneStructure1)) {
            handleStructureCollision(1);
        }

        if (!stoneStructure2Destroyed && isColliding(pinkBird, stoneStructure2)) {
            handleStructureCollision(2);
        }
    }

    private boolean isColliding(Object obj1, Object obj2) {
        if (obj1 instanceof Bird && obj2 instanceof BubblePig) {
            Bird bird = (Bird) obj1;
            BubblePig pig = (BubblePig) obj2;

            // Get the current positions
            Vector2 birdPos = bird.getPosition();
            Vector2 pigPos = pig.getPosition();

            // Get the textures
            Texture birdTexture = bird.getCurrentTexture();
            Texture pigTexture = pig.getCurrentTexture();

            // Create rectangles for more precise collision detection
            Rectangle birdRect = new Rectangle(
                birdPos.x,
                birdPos.y,
                birdTexture.getWidth(),
                birdTexture.getHeight()
            );

            Rectangle pigRect = new Rectangle(
                pigPos.x,
                pigPos.y,
                pigTexture.getWidth(),
                pigTexture.getHeight()
            );

            return birdRect.overlaps(pigRect);
        }

        if (obj1 instanceof Bird && obj2 instanceof StoneStructure) {
            Bird bird = (Bird) obj1;
            StoneStructure structure = (StoneStructure) obj2;

            // Get the current positions
            Vector2 birdPos = bird.getPosition();
            Vector2 structurePos = structure.getPosition();

            // Get the textures
            Texture birdTexture = bird.getCurrentTexture();
            Texture structureTexture = structure.getCurrentTexture();

            // More precise collision detection using rectangles
            Rectangle birdRect = new Rectangle(
                birdPos.x,
                birdPos.y,
                birdTexture.getWidth(),
                birdTexture.getHeight()
            );

            Rectangle structureRect = new Rectangle(
                structurePos.x,
                structurePos.y,
                structureTexture.getWidth(),
                structureTexture.getHeight()
            );

            return birdRect.overlaps(structureRect);
        }

        return false;
    }

    public void handlePigCollision() {
        bubblePig.takeHit(pinkBird);
        if (bubblePig.getCurrentHealth() <= 0) {
            if (!pigDestroyed) {
                pigDestroyed = true;
                birdDestroyed = true;
                currentScore += PIG_POINTS;
                Gdx.app.log("LevelScreen_1", "Pig hit! Score +" + PIG_POINTS);

                sparklePosition.set(bubblePig.getPosition());
                sparkleTimer = SPARKLE_DURATION;
                levelComplete = true;
                LevelScreen.level1Completed = true;
            }
        } else {
            birdDestroyed = true; // Mark bird as used even if the pig isn't destroyed
            birdsUsed++;
        }
    }

    public void handleStructureCollision(int structureNumber) {
        if (structureNumber == 1 && !stoneStructure1Destroyed) {
            stoneStructure1.takeHit(pinkBird);
            if (stoneStructure1.getCurrentDurability() <= 0) {
                stoneStructure1Destroyed = true;
                currentScore += STRUCTURE_POINTS;
                Gdx.app.log("LevelScreen_1", "Structure 1 destroyed! Score +" + STRUCTURE_POINTS);
            }
            birdDestroyed = true; // Mark bird as used
            birdsUsed++;
        } else if (structureNumber == 2 && !stoneStructure2Destroyed) {
            stoneStructure2.takeHit(pinkBird);
            if (stoneStructure2.getCurrentDurability() <= 0) {
                stoneStructure2Destroyed = true;
                currentScore += STRUCTURE_POINTS;
                Gdx.app.log("LevelScreen_1", "Structure 2 destroyed! Score +" + STRUCTURE_POINTS);
            }
            birdDestroyed = true; // Mark bird as used
            birdsUsed++;
        }
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        stage.getCamera().unproject(touchPos);

        if (pauseButtonBounds.contains(touchPos.x, touchPos.y)) {
            ((Main) game).clickSound.play(((Main) game).clickSoundVolume);
            // Don't dispose the current screen when pausing
            game.setScreen(new PauseScreen(game, (Screen)this));
            return true;
        }

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
            float maxDragDistance = 200f;
            float launchPower = Math.min(dragDistance / maxDragDistance, 1f);

            // Adjust velocity multiplier
            float velocityMultiplier = 2000f;

            // Calculate launch speed components
            float launchSpeedX = (float) (launchPower * Math.cos(launchAngle) * velocityMultiplier);
            float launchSpeedY = (float) (launchPower * Math.sin(launchAngle) * velocityMultiplier);

            // Launch the bird with calculated speed
            pinkBird.launch(launchSpeedX, launchSpeedY);
            gameStarted = true;

            // Reset failure-related flags
            failureTriggered = false;

            dragStart = null;
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
    }

    @Override
    public void dispose() {
        try {
            if (stage != null) {
                stage.dispose();
                stage = null;
            }
            if (batch != null) {
                batch.dispose();
                batch = null;
            }
            if (levelBackground != null) {
                levelBackground.dispose();
                levelBackground = null;
            }
            if (pinkBird != null) {
                pinkBird.dispose();
                pinkBird = null;
            }
            if (bubblePig != null) {
                bubblePig.dispose();
                bubblePig = null;
            }
            if (stoneStructure1 != null) {
                stoneStructure1.dispose();
                stoneStructure1 = null;
            }
            if (stoneStructure2 != null) {
                stoneStructure2.dispose();
                stoneStructure2 = null;
            }
            if (shapeRenderer != null) {
                shapeRenderer.dispose();
                shapeRenderer = null;
            }
            if (sparkleTexture != null) {
                sparkleTexture.dispose();
                sparkleTexture = null;
            }
            if (scoreFont != null) {
                scoreFont.dispose();
                scoreFont = null;
            }
            if (pauseButtonTexture != null) {
                pauseButtonTexture.dispose();
                pauseButtonTexture = null;
            }
            if (simplePauseTexture != null) {
                simplePauseTexture.dispose();
                simplePauseTexture = null;
            }
        } catch (Exception e) {
            Gdx.app.error("LevelScreen_1", "Error during disposal", e);
        }
    }
}
