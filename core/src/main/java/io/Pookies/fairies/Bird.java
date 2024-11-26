package io.Pookies.fairies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Bird {
    protected Texture texture;
    protected Vector2 position;
    protected Vector2 velocity;
    protected Vector2 originalPosition;
    protected Vector2 slingshotPosition;
    protected boolean isDragging;
    protected boolean isLaunched;
    protected static final float GRAVITY = -9.8f * 60; // Gravity scaled for game units
    protected static final float LAUNCH_SPEED_MULTIPLIER = 8f;
    protected static final float MAX_PULL_DISTANCE = 150f;
    protected float launchAngle;
    protected float pullDistance;

    public Bird(String texturePath, float x, float y) {
        this.texture = new Texture(texturePath);
        this.position = new Vector2(x, y);
        this.originalPosition = new Vector2(x-20, y);
        this.velocity = new Vector2(0, 0);
        this.slingshotPosition = new Vector2(x, y);
        this.isDragging = false;
        this.isLaunched = false;
        this.launchAngle = 0;
        this.pullDistance = 0;
    }

    public void update(float delta) {
        if (isLaunched) {
            // Apply gravity and update position
            velocity.y += GRAVITY * delta;
            position.x += velocity.x * delta;
            position.y += velocity.y * delta;

            // Optional: Reset bird if it goes off screen
            if (position.y < 0 || position.x > Gdx.graphics.getWidth()) {
                reset();
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void startDragging() {
        if (!isLaunched) {
            isDragging = true;
        }
    }

    public void drag(float x, float y) {
        if (isDragging && !isLaunched) {
            // Calculate angle between slingshot center and touch position
            float dx = x - slingshotPosition.x - x;
            float dy = y - slingshotPosition.y - y;
            launchAngle = MathUtils.atan2(dy, dx);

            // Calculate pull distance
            pullDistance = Math.min(
                new Vector2(dx, dy).len(),
                MAX_PULL_DISTANCE
            );

            // Calculate new position based on angle and pull distance
            float newX = slingshotPosition.x - pullDistance * MathUtils.cos(launchAngle);
            float newY = slingshotPosition.y - pullDistance * MathUtils.sin(launchAngle);

            position.set(newX, newY);
        }
    }

    public void launch() {
        if (isDragging && !isLaunched) {
            float launchSpeed = pullDistance * LAUNCH_SPEED_MULTIPLIER;
            float launchVelocityX = -launchSpeed * MathUtils.cos(launchAngle);
            float launchVelocityY = -launchSpeed * MathUtils.sin(launchAngle);

            velocity.set(launchVelocityX, launchVelocityY);

            isDragging = false;
            isLaunched = true;
        }
    }

    public void reset() {
        position.set(originalPosition);
        velocity.set(0, 0);
        isLaunched = false;
        isDragging = false;
        launchAngle = 0;
        pullDistance = 0;
    }

    public boolean contains(float x, float y) {
        float birdCenterX = position.x + texture.getWidth() / 2;
        float birdCenterY = position.y + texture.getHeight() / 2;
        float touchRadius = Math.max(texture.getWidth(), texture.getHeight()) / 2;

        return new Vector2(x - birdCenterX, y - birdCenterY).len() <= touchRadius;
    }

    public void dispose() {
        texture.dispose();
    }
}
