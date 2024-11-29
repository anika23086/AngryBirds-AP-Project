package io.Pookies.fairies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public abstract class Bird {
    protected Texture texture;
    protected Vector2 position;
    protected Vector2 velocity;
    protected Vector2 originalPosition;
    protected Vector2 slingshotPosition;
    protected boolean isDragging;
    protected boolean isLaunched;
    protected static final float GRAVITY = 980f; // Gravity scaled for game units
    protected static final float LAUNCH_SPEED_MULTIPLIER = 8f;
    protected static final float MAX_PULL_DISTANCE = 150f;
    protected float launchAngle;
    protected float pullDistance;

    // New attributes for power and damage
    protected float power;       // Damage strength of the bird
    protected float birdVelocity; // Initial launch velocity

    public Bird(String texturePath, float x, float y, float power, float birdVelocity) {
        this.texture = new Texture(texturePath);
        this.position = new Vector2(x, y);
        this.originalPosition = new Vector2(x-20, y);
        this.velocity = new Vector2(0, 0);
        this.slingshotPosition = new Vector2(x, y);
        this.isDragging = false;
        this.isLaunched = false;
        this.launchAngle = 0;
        this.pullDistance = 0;

        // Set power and velocity
        this.power = power;
        this.birdVelocity = birdVelocity;
    }


    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public Texture getCurrentTexture() {
        return texture;
    }

    public void startDragging() {
        isDragging = true;
        velocity.set(0, 0);
        isLaunched = false;
    }

    public void drag(float x, float y) {
        if (isDragging) {
            position.set(x, y);
        }
    }

    public void launch(float velocityX, float velocityY) {
        isDragging = false;
        isLaunched = true;
        velocity.set(velocityX, velocityY);
    }


    public void reset() {
        position.set(originalPosition);
        velocity.set(0, 0);
        isLaunched = false;
        isDragging = false;
        launchAngle = 0;
        pullDistance = 0;
    }
    public void update(float delta) {
        if (isLaunched) {
            // Apply gravity
            velocity.y -= GRAVITY * delta;

            // Update position
            position.x += velocity.x * delta;
            position.y += velocity.y * delta;
        }
    }

    public boolean contains(float x, float y) {
        return x >= position.x && x <= position.x + texture.getWidth() &&
            y >= position.y && y <= position.y + texture.getHeight();
    }

    public void dispose() {
        if (texture != null) texture.dispose();
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }


    public void setPosition(float lerp, float lerp1) {
        this.position.set(lerp, lerp1);
    }

    // Method to get bird's damage power
    public float getPower() {
        return power;
    }

    // Method to get bird's initial velocity
    public float getBirdVelocity() {
        return birdVelocity;
    }

    public abstract int getStrengthAgainst(String material);
}
