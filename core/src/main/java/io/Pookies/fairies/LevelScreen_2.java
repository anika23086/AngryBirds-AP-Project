package io.Pookies.fairies;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.InputMultiplexer;

public class LevelScreen_2 implements Screen, InputProcessor {
    private final Game game;
    private SpriteBatch batch;
    private Texture level2game;
    private PurpleBird purpleBird;
    private boolean levelComplete = false;
    private Stage stage;
    private Texture pauseButtonTexture;
    private ImageButton pauseButton;
    private UnclePig unclePig1;
    private BubblePig bubblePig1;
    private WoodStructure woodStructure1;
    private IceStructure icestructure1, icestructure2;
    private Slingshot slingshot;

    // Game mechanics variables
    private Rectangle birdRectangle;
    private Rectangle pigRectangle1, pigRectangle2;
    private Rectangle woodStructureRectangle;
    private Rectangle iceStructureRectangle1, iceStructureRectangle2;
    private BitmapFont scoreFont;
    private int currentScore;
    private static final int PIG_POINTS = 200;
    private static final int STRUCTURE_POINTS = 100;

    private boolean gameStarted = false;
    private Vector2 dragStart;
    private float maxDragDistance = 600f;
    private float launchPower;
    private float launchAngle;

    private boolean pigDestroyed1 = false;
    private boolean pigDestroyed2 = false;
    private boolean woodStructureDestroyed = false;
    private boolean iceStructureDestroyed1 = false;
    private boolean iceStructureDestroyed2 = false;
    private boolean birdDestroyed = false;

    public LevelScreen_2(Game game) {
        this.game = game;
        ((Main) game).setCurrentLevel(this);
        batch = new SpriteBatch();

        // Initialize game objects
        purpleBird = new PurpleBird(30, 80);
        unclePig1 = new UnclePig(920, 650);
        bubblePig1 = new BubblePig(1025, 375);
        woodStructure1 = new WoodStructure(900, 380);
        icestructure1 = new IceStructure(1018, 150);
        icestructure2 = new IceStructure(900, 150);
        slingshot = new Slingshot(220, 130);

        // Initialize rectangles for collision detection
        birdRectangle = new Rectangle(30, 80, 50, 50);
        pigRectangle1 = new Rectangle(920, 650, 50, 50);
        pigRectangle2 = new Rectangle(1025, 375, 50, 50);
        woodStructureRectangle = new Rectangle(900, 380, 100, 200);
        iceStructureRectangle1 = new Rectangle(1018, 150, 100, 200);
        iceStructureRectangle2 = new Rectangle(900, 150, 100, 200);

        // Initialize score font
        scoreFont = new BitmapFont();
        scoreFont.setColor(Color.YELLOW);
        scoreFont.getData().setScale(3.0f);
        currentScore = 0;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        level2game = new Texture(Gdx.files.internal("gameLevel_2.png"));
        stage = new Stage(new ScreenViewport());

        // Pause button setup
        pauseButtonTexture = new Texture(Gdx.files.internal("pause_button.png"));
        TextureRegionDrawable pauseDrawable = new TextureRegionDrawable(pauseButtonTexture);
        pauseButton = new ImageButton(pauseDrawable);
        pauseButton.setPosition(Gdx.graphics.getWidth() - pauseButton.getWidth() - 20,
            Gdx.graphics.getHeight() - pauseButton.getHeight() - 20);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PauseScreen(game));
            }
        });
        stage.addActor(pauseButton);

        // Input processing
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update game state
        if (gameStarted && !birdDestroyed) {
            purpleBird.update(delta);
            birdRectangle.setPosition(purpleBird.getPosition().x, purpleBird.getPosition().y);
            checkCollisions();
        }

        // Render game
        batch.begin();
        batch.draw(level2game, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Render game objects based on their state
        if (!birdDestroyed) purpleBird.render(batch);
        if (!pigDestroyed1) unclePig1.render(batch);
        if (!pigDestroyed2) bubblePig1.render(batch);
        if (!woodStructureDestroyed) woodStructure1.render(batch);
        if (!iceStructureDestroyed1) icestructure1.render(batch);
        if (!iceStructureDestroyed2) icestructure2.render(batch);

        slingshot.render(batch);

        // Render score
        scoreFont.draw(batch, "Score: " + currentScore, 1700, Gdx.graphics.getHeight() - 20);

        batch.end();

        // Check for level completion
        checkLevelCompletion();

        // Render stage
        stage.act(delta);
        stage.draw();
    }


    private void checkCollisions() {
        // Check pig collisions
        if (!pigDestroyed1 && birdRectangle.overlaps(pigRectangle1)) {
            handlePigCollision(1);
        }
        if (!pigDestroyed2 && birdRectangle.overlaps(pigRectangle2)) {
            handlePigCollision(2);
        }

        // Check structure collisions
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
            currentScore += PIG_POINTS;
        } else if (pigNumber == 2 && !pigDestroyed2) {
            pigDestroyed2 = true;
            currentScore += PIG_POINTS;
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
    }

    private void checkLevelCompletion() {
        if (pigDestroyed1 && pigDestroyed2 &&
            woodStructureDestroyed &&
            iceStructureDestroyed1 &&
            iceStructureDestroyed2) {
            levelComplete = true;
            game.setScreen(new SuccessScreen(game));
        }
    }

    private void checkBirdDestructionConditions(float delta) {
        Vector2 birdPos = purpleBird.getPosition();
        Vector2 birdVelocity = purpleBird.getVelocity();
        boolean isOffScreen = birdPos.x < -100 ||
            birdPos.x > Gdx.graphics.getWidth() + 100 ||
            birdPos.y < -100 ||
            birdPos.y > Gdx.graphics.getHeight() + 100;

        boolean hasStopped = birdVelocity.len() < 1.0f;

        if (isOffScreen || hasStopped) {
            birdDestroyed = true;
        }
    }

    // Input processing methods for launching bird
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        stage.getCamera().unproject(touchPos);

        if (purpleBird.contains(touchPos.x, touchPos.y)) {
            dragStart = new Vector2(touchPos.x, touchPos.y);
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

            purpleBird.drag(touchPos.x, touchPos.y);
            launchPower = Math.min(dragDistance / maxDragDistance, 1f);
            launchAngle = (float) Math.atan2(dragVector.y, dragVector.x);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (dragStart != null) {
            Vector2 touchPos = new Vector2(screenX, screenY);
            Vector2 dragVector = new Vector2(dragStart.x - touchPos.x, dragStart.y - touchPos.y);
            float dragDistance = dragVector.len();
            float velocityMultiplier = 2000f;

            float launchSpeedX = (float) (launchPower * Math.cos(launchAngle) * velocityMultiplier);
            float launchSpeedY = (float) (launchPower * Math.sin(launchAngle) * velocityMultiplier);

            purpleBird.launch(launchSpeedX, launchSpeedY);
            gameStarted = true;
            dragStart = null;
        }
        return true;
    }

    // Other required InputProcessor method stubs
    @Override public boolean keyDown(int keycode) { return false; }
    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override public boolean scrolled(float amountX, float amountY) { return false; }
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }

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
        unclePig1.dispose();
        bubblePig1.dispose();
        woodStructure1.dispose();
        icestructure1.dispose();
        icestructure2.dispose();
        scoreFont.dispose();
    }
}
