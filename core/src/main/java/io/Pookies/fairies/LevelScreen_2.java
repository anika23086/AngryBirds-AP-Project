package io.Pookies.fairies;

import com.badlogic.gdx.*;
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

public class LevelScreen_2 implements Screen, InputProcessor {
    private final Game game;
    private Stage stage;
    private SpriteBatch batch;
    private Texture levelBackground;
    private Bird purpleBird;
    private Bird pinkBird1, pinkBird2;
    private UnclePig unclePig1;
    private BubblePig bubblePig1;
    private WoodStructure woodStructure1;
    private IceStructure icestructure1, icestructure2;
    private Slingshot slingshot;
    private float clickSoundVolume;
    private boolean gameStarted;
    private Vector2 slingshotOrigin;
    private Vector2 dragStart;
    private float maxDragDistance = 600f;
    private float launchPower;
    private float launchAngle;
    private ShapeRenderer shapeRenderer;
    private boolean showTrajectory;
    private boolean pigDestroyed1 = false;
    private boolean pigDestroyed2 = false;
    private boolean woodStructureDestroyed = false;
    private boolean iceStructureDestroyed1 = false;
    private boolean iceStructureDestroyed2 = false;
    private boolean birdDestroyed = false;
    private Rectangle birdRectangle;
    private Rectangle pigRectangle1, pigRectangle2;
    private Rectangle woodStructureRectangle;
    private Rectangle iceStructureRectangle1, iceStructureRectangle2;
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
    private Texture sparkleTexture;
    private Vector2 sparklePosition;
    private float sparkleTimer;
    private static final float SPARKLE_DURATION = 1.0f;
    private float sparkleAlpha;
    private ImageButton pauseButton;
    private Texture pauseButtonTexture;
    private ShapeRenderer debugRenderer = new ShapeRenderer();
    private Texture simplePauseTexture;
    private Rectangle pauseButtonBounds;
    private static final int PAUSE_BUTTON_SIZE = 50;
    private static final int PADDING = 10;
    private boolean allBirdsExhausted = false;
    private boolean currentBirdUsed = false;

    private Bird currentBird;
    private Bird[] birds;
    private int currentBirdIndex = 0;

    public LevelScreen_2(Game game) {
        this.game = game;
        ((Main) game).setCurrentLevel(this);
        batch = new SpriteBatch();
        purpleBird = new PurpleBird(150, 240);
        pinkBird1 = new PinkBird(150, 240);
        pinkBird2 = new PinkBird(150, 240);
        unclePig1 = new UnclePig(920, 650);
        bubblePig1 = new BubblePig(1025, 375);
        woodStructure1 = new WoodStructure(900, 380);
        icestructure1 = new IceStructure(1018, 150);
        icestructure2 = new IceStructure(900, 150);
        slingshot = new Slingshot(220, 130);
        clickSoundVolume = ((Main) game).clickSoundVolume;
        gameStarted = false;
        slingshotOrigin = new Vector2(220, 130);
        shapeRenderer = new ShapeRenderer();
        showTrajectory = false;
        birdRectangle = new Rectangle(30, 80, 50, 50);
        pigRectangle1 = new Rectangle(920, 650, 50, 50);
        pigRectangle2 = new Rectangle(1025, 375, 50, 50);
        woodStructureRectangle = new Rectangle(900, 380, 100, 200);
        iceStructureRectangle1 = new Rectangle(1018, 150, 100, 200);
        iceStructureRectangle2 = new Rectangle(900, 150, 100, 200);
        scoreFont = new BitmapFont();
        scoreFont.setColor(Color.YELLOW);
        scoreFont.getData().setScale(3.0f);
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

        birds = new Bird[]{purpleBird, pinkBird1, pinkBird2};
        currentBirdIndex = 0;
        currentBird = birds[currentBirdIndex];
        currentBird.setPosition(slingshotOrigin.x,slingshotOrigin.y);
    }

    private void setupErrorHandler() {
        if (!isErrorHandlerSet) {
            Thread.setDefaultUncaughtExceptionHandler(new NativeCrashHandler(game));
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
            Gdx.app.error("LevelScreen_2", "Setting up error handler");
            isErrorHandlerSet = true;
        }
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        levelBackground = new Texture(Gdx.files.internal("gameLevel_2.png"));

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);

        pauseButtonTexture = new Texture(Gdx.files.internal("pause_button.png"));
        TextureRegionDrawable pauseDrawable = new TextureRegionDrawable(pauseButtonTexture);
        pauseButton = new ImageButton(pauseDrawable);

        pauseButton.setPosition(
            Gdx.graphics.getWidth() - pauseButton.getWidth(),
            Gdx.graphics.getHeight() - pauseButton.getHeight()
        );

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Main) game).clickSound.play(((Main) game).clickSoundVolume);
                game.setScreen(new PauseScreen(game, (Screen)LevelScreen_2.this));
            }
        });

        Gdx.app.log("LevelScreen_2", "Pause button added to stage");
        Gdx.app.log("LevelScreen_2", "Pause button position: (" + pauseButton.getX() + ", " + pauseButton.getY() + "), size: (" + pauseButton.getWidth() + ", " + pauseButton.getHeight() + ")");
    }

    @Override
    public void render(float delta) {
        try {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();
            batch.draw(levelBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            // Update bird position if launched
            if (gameStarted && currentBirdUsed) {
                currentBird.update(delta);
                birdRectangle.setPosition(currentBird.getPosition().x, currentBird.getPosition().y);
                checkCollisions();

                // Check if current bird should be replaced
                Vector2 birdPos = currentBird.getPosition();
                Vector2 birdVel = currentBird.getVelocity();
                boolean isOffScreen = birdPos.x < -SCREEN_BOUNDS_MARGIN ||
                    birdPos.x > Gdx.graphics.getWidth() + SCREEN_BOUNDS_MARGIN ||
                    birdPos.y < -SCREEN_BOUNDS_MARGIN ||
                    birdPos.y > Gdx.graphics.getHeight() + SCREEN_BOUNDS_MARGIN;
                boolean hasStopped = birdVel.len() < VELOCITY_THRESHOLD;

                if (isOffScreen || hasStopped) {
                    moveToNextBird();
                }
            }

            if (allBirdsExhausted && !(pigDestroyed1 && pigDestroyed2)) {
                game.setScreen(new FailureScreen(game, 2));
                return;
            }

            // Update and render pigs
            if (gameStarted && !(pigDestroyed1 && pigDestroyed2)) {
                if (!pigDestroyed1) {
                    unclePig1.update(delta);
                    pigRectangle1.setPosition(unclePig1.getPosition().x, unclePig1.getPosition().y);
                }
                if (!pigDestroyed2) {
                    bubblePig1.update(delta);
                    pigRectangle2.setPosition(bubblePig1.getPosition().x, bubblePig1.getPosition().y);
                }
            }

            // Check for level completion
            if (pigDestroyed1 && pigDestroyed2) {
                levelCompleteTimer += delta;
                if (levelCompleteTimer >= LEVEL_COMPLETE_DELAY) {
                    LevelScreen.level2Completed = true;
                    game.setScreen(new SuccessScreen(game));
                    return;
                }
            } else if (currentBirdIndex >= birds.length && !currentBirdUsed) {
                // Only show failure screen if all birds are used AND pigs remain
                game.setScreen(new FailureScreen(game, 2));
                return;
            }

            // Update current bird position if game has started
            if (gameStarted && !birdDestroyed) {
                currentBird.update(delta);
                birdRectangle.setPosition(currentBird.getPosition().x, currentBird.getPosition().y);
                checkCollisions();

                // Check if current bird is off screen or stopped
                Vector2 birdPos = currentBird.getPosition();
                Vector2 birdVel = currentBird.getVelocity();
                boolean isOffScreen = birdPos.x < -SCREEN_BOUNDS_MARGIN ||
                    birdPos.x > Gdx.graphics.getWidth() + SCREEN_BOUNDS_MARGIN ||
                    birdPos.y < -SCREEN_BOUNDS_MARGIN ||
                    birdPos.y > Gdx.graphics.getHeight() + SCREEN_BOUNDS_MARGIN;
                boolean hasStopped = birdVel.len() < VELOCITY_THRESHOLD;

                if ((isOffScreen || hasStopped) && !currentBirdUsed) {
                    currentBirdUsed = true;
                    prepareBirdTransition();
                }
            }

            if (levelComplete) {
                levelCompleteTimer += delta;
                if (levelCompleteTimer >= LEVEL_COMPLETE_DELAY) {
                    if (!LevelScreen.level2Completed) {
                        LevelScreen.level2Completed = true;
                    }
                    game.setScreen(new SuccessScreen(game));
                    return;
                }
            }

            if (sparkleTimer > 0) {
                sparkleTimer -= delta;
                sparkleAlpha = Math.max(0, sparkleTimer / SPARKLE_DURATION);
            }

            if (pigDestroyed1 && pigDestroyed2) {
                levelCompleteTimer += delta;
                if (levelCompleteTimer >= LEVEL_COMPLETE_DELAY) {
                    checkLevelCompletion();
                }
            }

            if (gameStarted && !levelComplete && !checkingForFailure) {
                checkingForFailure = true;
            }

            if (checkingForFailure && !(pigDestroyed1 && pigDestroyed2)) {
                checkForFailureConditions(delta);
            }

            if (gameStarted && !(pigDestroyed1 && pigDestroyed2)) {
                unclePig1.update(delta);
                pigRectangle1.setPosition(unclePig1.getPosition().x, unclePig1.getPosition().y);

                bubblePig1.update(delta);
                pigRectangle2.setPosition(bubblePig1.getPosition().x, bubblePig1.getPosition().y);

                if (unclePig1.getPosition().y < -100 || bubblePig1.getPosition().y < -100) {
                    pigDestroyed1 = true;
                    pigDestroyed2 = true;
                }
            }

            if (gameStarted && !birdDestroyed) {
                currentBird.update(delta);
                birdRectangle.setPosition(currentBird.getPosition().x, currentBird.getPosition().y);
                checkCollisions();
            }

            batch.draw(simplePauseTexture,
                pauseButtonBounds.x,
                pauseButtonBounds.y,
                pauseButtonBounds.width,
                pauseButtonBounds.height);

            stage.act(delta);
            stage.draw();

            if (!birdDestroyed) {
                currentBird.draw(batch, 1);
            }
            if (!pigDestroyed1) {
                unclePig1.render(batch);
            }
            if (!pigDestroyed2) {
                bubblePig1.render(batch);
            }

            if (sparkleTimer > 0) {
                batch.setColor(1, 1, 1, sparkleAlpha);
                batch.draw(sparkleTexture, sparklePosition.x, sparklePosition.y, 100, 100);
                batch.setColor(1, 1, 1, 1);
            }

            if (!woodStructureDestroyed) {
                woodStructure1.render(batch);
            }
            if (!iceStructureDestroyed1) {
                icestructure1.render(batch);
            }
            if (!iceStructureDestroyed2) {
                icestructure2.render(batch);
            }
            slingshot.render(batch);

            scoreFont.draw(batch, "Score: " + currentScore, 40, Gdx.graphics.getHeight() - 115);
            renderGameElements();
            batch.end();

        } catch (Throwable t) {
            handleCrash(t);
        }
    }

    private void moveToNextBird() {
        currentBirdUsed = false;
        currentBirdIndex++;

        if (currentBirdIndex < birds.length) {
            currentBird = birds[currentBirdIndex];
            currentBird.setPosition(slingshotOrigin.x, slingshotOrigin.y);
            gameStarted = false;
        }
    }

    private void prepareBirdTransition() {
        currentBirdIndex++;

        if (currentBirdIndex >= birds.length) {
            allBirdsExhausted = true;
            if (!(pigDestroyed1 && pigDestroyed2)) {
                game.setScreen(new FailureScreen(game, 2));
            }
            return;
        }

        // Set up next bird
        currentBird = birds[currentBirdIndex];
        currentBird.setPosition(slingshotOrigin.x, slingshotOrigin.y);
        birdDestroyed = false;
        currentBirdUsed = false;
        gameStarted = false;
    }

    private void checkLevelCompletion() {
        if (pigDestroyed1 && pigDestroyed2 && !LevelScreen.level2Completed) {
            LevelScreen.level2Completed = true;
            game.setScreen(new SuccessScreen(game));
        }
    }

    private void updateBirdPosition(float delta) {
        try {
            currentBird.update(delta);
            birdRectangle.setPosition(currentBird.getPosition().x, currentBird.getPosition().y);
            checkCollisions();
        } catch (Throwable t) {
            handleCrash(t);
        }
    }

    private void renderGameElements() {
        // Draw current bird if it exists and hasn't been launched yet
        if (currentBirdIndex < birds.length) {
            currentBird.draw(batch, 1);
        }

        if (!pigDestroyed1) {
            unclePig1.render(batch);
        }
        if (!pigDestroyed2) {
            bubblePig1.render(batch);
        }
        if (!woodStructureDestroyed) {
            woodStructure1.render(batch);
        }
        if (!iceStructureDestroyed1) {
            icestructure1.render(batch);
        }
        if (!iceStructureDestroyed2) {
            icestructure2.render(batch);
        }
        slingshot.render(batch);

        if (sparkleTimer > 0) {
            batch.setColor(1, 1, 1, sparkleAlpha);
            batch.draw(sparkleTexture, sparklePosition.x, sparklePosition.y, 100, 100);
            batch.setColor(1, 1, 1, 1);
        }

        scoreFont.draw(batch, "Score: " + currentScore, 40, Gdx.graphics.getHeight() - 115);

        // Show remaining birds
        int remainingBirds = birds.length - currentBirdIndex;
        if (currentBirdUsed) remainingBirds--;
        scoreFont.draw(batch, "Birds: " + remainingBirds, 40, Gdx.graphics.getHeight() - 155);
    }

    private void handleCrash(Throwable throwable) {
        Gdx.app.error("LevelScreen_2", "Crash detected", throwable);
        try {
            Gdx.app.postRunnable(() -> {
                try {
                    dispose();
                    game.setScreen(new FailureScreen(game, 2));
                } catch (Exception e) {
                    Gdx.app.error("LevelScreen_2", "Failed to show failure screen", e);
                    Gdx.app.exit();
                }
            });
        } catch (Exception e) {
            Gdx.app.error("LevelScreen_2", "Failed to handle crash", e);
            Gdx.app.exit();
        }
    }

    private void checkForFailureConditions(float delta) {
        failureCheckTimer += delta;

        if (failureCheckTimer >= FAILURE_CHECK_DELAY) {
            Vector2 birdPos = currentBird.getPosition();
            boolean isOffScreen = birdPos.x < -SCREEN_BOUNDS_MARGIN ||
                birdPos.x > Gdx.graphics.getWidth() + SCREEN_BOUNDS_MARGIN ||
                birdPos.y < -SCREEN_BOUNDS_MARGIN ||
                birdPos.y > Gdx.graphics.getHeight() + SCREEN_BOUNDS_MARGIN;

            Vector2 birdVelocity = currentBird.getVelocity();
            boolean hasStopped = birdVelocity.len() < VELOCITY_THRESHOLD;

            // Only show failure screen if all birds are exhausted and pigs remain
            if ((isOffScreen || hasStopped) && currentBirdIndex >= birds.length - 1 && currentBirdUsed && !(pigDestroyed1 && pigDestroyed2)) {
                game.setScreen(new FailureScreen(game, 2));
            } else if ((isOffScreen || hasStopped) && !currentBirdUsed) {
                // If current bird is off screen or stopped, prepare for next bird
                currentBirdUsed = true;
                prepareBirdTransition();
            }

            failureCheckTimer = 0; // Reset the timer
        }
    }

    private void checkCollisions() {
        if (!pigDestroyed1 && birdRectangle.overlaps(pigRectangle1)) {
            handlePigCollision(1);
        }
        if (!pigDestroyed2 && birdRectangle.overlaps(pigRectangle2)) {
            handlePigCollision(2);
        }

        if (!woodStructureDestroyed && birdRectangle.overlaps(woodStructureRectangle)) {
            handleStructureCollision(1);
        }
        if (!iceStructureDestroyed1 && birdRectangle.overlaps(iceStructureRectangle1)) {
            handleStructureCollision(2);
        }
        if (!iceStructureDestroyed2 && birdRectangle.overlaps(iceStructureRectangle2)) {
            handleStructureCollision(3);
        }
    }

    private void handlePigCollision(int pigNumber) {
        if (pigNumber == 1 && !pigDestroyed1) {
            pigDestroyed1 = true;
            birdDestroyed = true;
            currentScore += PIG_POINTS;
            System.out.println("Pig 1 hit! Score +" + PIG_POINTS + " (Total: " + currentScore + ")");

            sparklePosition.set(unclePig1.getPosition());
            sparkleTimer = SPARKLE_DURATION;
            sparkleAlpha = 1.0f;

            // Do not set levelComplete to true here
        } else if (pigNumber == 2 && !pigDestroyed2) {
            pigDestroyed2 = true;
            birdDestroyed = true;
            currentScore += PIG_POINTS;
            System.out.println("Pig 2 hit! Score +" + PIG_POINTS + " (Total: " + currentScore + ")");

            sparklePosition.set(bubblePig1.getPosition());
            sparkleTimer = SPARKLE_DURATION;
            sparkleAlpha = 1.0f;

            // Do not set levelComplete to true here
        }

        // Check if both pigs are destroyed
        if (pigDestroyed1 && pigDestroyed2) {
            levelComplete = true;
            LevelScreen.level2Completed = true;
        }
    }

    private void handleStructureCollision(int structureNumber) {
        switch (structureNumber) {
            case 1:
                woodStructureDestroyed = true;
                break;
            case 2:
                iceStructureDestroyed1 = true;
                break;
            case 3:
                iceStructureDestroyed2 = true;
                break;
        }
        currentScore += STRUCTURE_POINTS;
        System.out.println("Structure hit! Score +" + STRUCTURE_POINTS + " (Total: " + currentScore + ")");
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        stage.getCamera().unproject(touchPos);

        if (pauseButtonBounds.contains(touchPos.x, touchPos.y)) {
            ((Main) game).clickSound.play(((Main) game).clickSoundVolume);
            game.setScreen(new PauseScreen(game, (Screen)this));
            return true;
        }

        // Only allow bird interaction if the current bird hasn't been used and we haven't exhausted all birds
        if (!currentBirdUsed && currentBirdIndex < birds.length &&
            currentBird.contains(touchPos.x, touchPos.y)) {
            dragStart = new Vector2(touchPos.x, touchPos.y);
            currentBird.startDragging();
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        stage.getCamera().unproject(touchPos);

        if (dragStart != null) {
            Vector2 dragVector = new Vector2(dragStart.x - touchPos.x, dragStart.y - touchPos.y);
            float dragDistance = dragVector.len();

            if (dragDistance > maxDragDistance) {
                dragVector.nor().scl(maxDragDistance);
            }

            currentBird.setPosition(dragStart.x - dragVector.x, dragStart.y - dragVector.y);

            launchPower = Math.min(dragDistance / maxDragDistance, 1f);
            launchAngle = (float) Math.atan2(dragVector.y, dragVector.x);

            showTrajectory = true;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (dragStart != null && !currentBirdUsed) {
            Vector3 touchPos = new Vector3(screenX, screenY, 0);
            stage.getCamera().unproject(touchPos);
            Vector2 dragVector = new Vector2(dragStart.x - touchPos.x, dragStart.y - touchPos.y);
            float dragDistance = dragVector.len();
            float maxDragDistance = 200f;
            float launchPower = Math.min(dragDistance / maxDragDistance, 1f);

            float velocityMultiplier = 2000f;

            float launchSpeedX = (float) (launchPower * Math.cos(launchAngle) * velocityMultiplier);
            float launchSpeedY = (float) (launchPower * Math.sin(launchAngle) * velocityMultiplier);

            currentBird.launch(launchSpeedX, launchSpeedY);
            currentBirdUsed = true;
            gameStarted = true;
            dragStart = null;
            showTrajectory = false;
        }
        return true;
    }

    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    private void checkPigsOnDestroyedStructures() {
        if (woodStructureDestroyed) {
            Rectangle structureRect1 = new Rectangle(900, 380, 100, 200);

            if (!pigDestroyed1 && isPigDirectlyAboveStructure(structureRect1)) {
                handlePigFall(1);
            }
            if (!pigDestroyed2 && isPigDirectlyAboveStructure(structureRect1)) {
                handlePigFall(2);
            }
        }

        if (iceStructureDestroyed1) {
            Rectangle structureRect2 = new Rectangle(1018, 150, 100, 200);

            if (!pigDestroyed1 && isPigDirectlyAboveStructure(structureRect2)) {
                handlePigFall(1);
            }
            if (!pigDestroyed2 && isPigDirectlyAboveStructure(structureRect2)) {
                handlePigFall(2);
            }
        }

        if (iceStructureDestroyed2) {
            Rectangle structureRect3 = new Rectangle(900, 150, 100, 200);

            if (!pigDestroyed1 && isPigDirectlyAboveStructure(structureRect3)) {
                handlePigFall(1);
            }
            if (!pigDestroyed2 && isPigDirectlyAboveStructure(structureRect3)) {
                handlePigFall(2);
            }
        }
    }

    private boolean isOverlapping(Rectangle pigRect, Rectangle structureRect) {
        return pigRect.x >= structureRect.x &&
            pigRect.x <= structureRect.x + structureRect.width &&
            pigRect.y >= structureRect.y &&
            pigRect.y <= structureRect.y + structureRect.height;
    }

    private void handlePigFall(int pigNumber) {
        if (pigNumber == 1 && !pigDestroyed1) {
            unclePig1.startFalling();
            pigDestroyed1 = true;
            currentScore += PIG_POINTS;
            System.out.println("Pig 1 fell! Score +" + PIG_POINTS + " (Total: " + currentScore + ")");
        } else if (pigNumber == 2 && !pigDestroyed2) {
            bubblePig1.startFalling();
            pigDestroyed2 = true;
            currentScore += PIG_POINTS;
            System.out.println("Pig 2 fell! Score +" + PIG_POINTS + " (Total: " + currentScore + ")");
        }
    }

    private boolean isPigDirectlyAboveStructure(Rectangle structureRect) {
        boolean withinStructureWidth =
            pigRectangle1.x >= structureRect.x &&
                pigRectangle1.x <= structureRect.x + structureRect.width;

        boolean directlyAbove =
            pigRectangle1.y >= structureRect.y + structureRect.height &&
                pigRectangle1.y <= structureRect.y + structureRect.height + 100;

        return withinStructureWidth && directlyAbove;
    }

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
            if (purpleBird != null) {
                purpleBird.dispose();
                purpleBird = null;
            }
            if (pinkBird1 != null) {
                pinkBird1.dispose();
                pinkBird1 = null;
            }
            if (pinkBird2 != null) {
                pinkBird2.dispose();
                pinkBird2 = null;
            }
            if (unclePig1 != null) {
                unclePig1.dispose();
                unclePig1 = null;
            }
            if (bubblePig1 != null) {
                bubblePig1.dispose();
                bubblePig1 = null;
            }
            if (woodStructure1 != null) {
                woodStructure1.dispose();
                woodStructure1 = null;
            }
            if (icestructure1 != null) {
                icestructure1.dispose();
                icestructure1 = null;
            }
            if (icestructure2 != null) {
                icestructure2.dispose();
                icestructure2 = null;
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
            Gdx.app.error("LevelScreen_2", "Error during disposal", e);
        }
    }
}
