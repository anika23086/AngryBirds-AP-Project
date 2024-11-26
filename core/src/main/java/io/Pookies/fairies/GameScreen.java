package io.Pookies.fairies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;

public class GameScreen implements Screen {
    private final Main game;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Bird currentBird;
    private Slingshot slingshot;
    private Array<Pig> pigs;
    private Array<Structure> structures;
    private Vector2 dragStart;
    private boolean isDragging;
    private boolean birdLaunched;
    private float maxDragDistance = 3f; // in meters

    private static final float MIN_LAUNCH_FORCE = 5f;
    private static final float MAX_LAUNCH_FORCE = 25f;

    public GameScreen() {
        this.game = (Main) Gdx.app.getApplicationListener();
        this.pigs = new Array<>();
        this.structures = new Array<>();
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / Main.PPM, Gdx.graphics.getHeight() / Main.PPM);
        batch = game.batch;

        // Initialize Box2D World
        world = new World(new Vector2(0, -9.81f), true);
        debugRenderer = new Box2DDebugRenderer();

        // Create game objects
        createGround();
        createSlingshot();
        createInitialBird();
        createLevel(); // Add pigs and structures

        // Setup collision detection
        setupContactListener();

        // Input processing
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
                if (!birdLaunched && isBirdTouched(worldCoords.x, worldCoords.y)) {
                    isDragging = true;
                    dragStart = new Vector2(worldCoords.x, worldCoords.y);
                }
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (isDragging && !birdLaunched) {
                    Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
                    Vector2 dragCurrent = new Vector2(worldCoords.x, worldCoords.y);
                    Vector2 dragVector = dragCurrent.sub(slingshot.getPosition());

                    // Limit drag distance
                    if (dragVector.len() > maxDragDistance) {
                        dragVector.nor().scl(maxDragDistance);
                    }

                    // Update bird position
                    currentBird.setPosition(
                        slingshot.getPosition().x + dragVector.x,
                        slingshot.getPosition().y + dragVector.y
                    );
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (isDragging && !birdLaunched) {
                    Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
                    Vector2 releasePos = new Vector2(worldCoords.x, worldCoords.y);
                    Vector2 launchVector = releasePos.sub(slingshot.getPosition());

                    // Calculate launch force
                    float force = MathUtils.clamp(
                        launchVector.len() * 10f,
                        MIN_LAUNCH_FORCE,
                        MAX_LAUNCH_FORCE
                    );

                    // Launch bird
                    currentBird.launch(launchVector.nor().scl(force));
                    birdLaunched = true;
                    isDragging = false;
                }
                return true;
            }
        });
    }

    private void createGround() {
        BodyDef groundDef = new BodyDef();
        groundDef.type = BodyDef.BodyType.StaticBody;
        groundDef.position.set(camera.viewportWidth / 2, 0.5f);

        Body groundBody = world.createBody(groundDef);
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(camera.viewportWidth / 2, 0.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundShape;
        fixtureDef.friction = 0.5f;
        groundBody.createFixture(fixtureDef);
        groundShape.dispose();
    }

    private void createSlingshot() {
        slingshot = new Slingshot(2f, 2f);
        BodyDef slingshotDef = new BodyDef();
        slingshotDef.type = BodyDef.BodyType.StaticBody;
        slingshotDef.position.set(2f, 2f);

        Body slingshotBody = world.createBody(slingshotDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        slingshotBody.createFixture(fixtureDef);
        shape.dispose();
    }

    private void createInitialBird() {
        currentBird = new Bird("pinkbird.png",
            Main.toPixels(slingshot.getPosition().x),
            Main.toPixels(slingshot.getPosition().y),
            world
        );
    }

    private void createLevel() {
        // Create pigs
        createPig(6f, 2f);
        createPig(8f, 2f);

        // Create structures
        createStructure(7f, 2f, "wood1.png");
        createStructure(5f, 2f, "wood2.png");
    }

    private void createPig(float x, float y) {
        Pig pig = new Pig("pig.png",
            Main.toPixels(x),
            Main.toPixels(y),
            world
        );
        pigs.add(pig);
    }

    private void createStructure(float x, float y, String texturePath) {
        Structure structure = new Structure(texturePath,
            Main.toPixels(x),
            Main.toPixels(y),
            world
        );
        structures.add(structure);
    }

    private void setupContactListener() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                // Handle collisions between birds, pigs, and structures
                // Add scoring logic here
            }

            @Override
            public void endContact(Contact contact) {}

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                float[] impulses = impulse.getNormalImpulses();
                float maxImpulse = 0;
                for (float imp : impulses) {
                    maxImpulse = Math.max(maxImpulse, imp);
                }

                if (maxImpulse > 10f) {
                    // Handle destruction of objects
                    // Update score
                }
            }
        });
    }

    private boolean isBirdTouched(float x, float y) {
        Vector2 birdPos = currentBird.getPosition();
        return birdPos.dst(x, y) < 0.5f; // Touch radius in meters
    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0.8f, 0.8f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update physics
        world.step(delta, 6, 2);

        // Update camera
        camera.update();

        // Begin sprite batch
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Draw slingshot
        // Note: You'll need to implement slingshot rendering
        slingshot.render(batch);

        // Draw current bird
        currentBird.render(batch);

        // Draw pigs
        for (Pig pig : pigs) {
            pig.render(batch);
        }

        // Draw structures
        for (Structure structure : structures) {
            structure.render(batch);
        }

        batch.end();

        // Debug rendering
        debugRenderer.render(world, camera.combined);

        // Check game state
        checkGameState();
    }

    private void checkGameState() {
        if (currentBird.isLaunched()) {
            // Check if bird is off screen or stopped
            Vector2 birdPos = currentBird.getPosition();
            Vector2 birdVel = currentBird.getBody().getLinearVelocity();

            if (birdPos.x > camera.viewportWidth ||
                birdPos.x < 0 ||
                (birdVel.len() < 0.1f && birdPos.y < 1f)) {
                resetBird();
            }
        }

        // Check win condition
        boolean allPigsDestroyed = true;
        for (Pig pig : pigs) {
            if (!pig.isDestroyed()) {
                allPigsDestroyed = false;
                break;
            }
        }

        if (allPigsDestroyed) {
            // Level completed
            // You might want to add a way to transition to the next screen
            LevelScreen.level1Completed = true;
            game.setScreen(new LevelScreen(game));
        }
    }

    private void resetBird() {
        if (currentBird != null) {
            currentBird.dispose();
        }
        createInitialBird();
        birdLaunched = false;
        isDragging = false;
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / Main.PPM, height / Main.PPM);
    }

    @Override
    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }
}
