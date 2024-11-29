package io.Pookies.fairies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


public abstract class Pig {
    protected Texture texture;
    protected Vector2 velocity;
    protected Vector2 position;
    private static final float GRAVITY = 500f;
    protected boolean isFalling = false;

    // Health-related attributes
    protected int currentHealth;
    protected int maxHealth;

    public Pig(String texturePath, float x, float y) {
        texture = new Texture(texturePath);
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
    }

    // Take damage from a bird
    public boolean takeHit(Bird bird) {
        currentHealth -= bird.getPower();
        return isDestroyed();
    }

    public void startFalling() {
        isFalling = true;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void dispose() {
        texture.dispose();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void update(float delta) {
        if (isFalling) {
            velocity.y += GRAVITY * delta;
            position.y += velocity.y * delta;

            if (position.y < 0) {
                position.y = 0;
                velocity.y = 0;
                isFalling = false;
            }
        }
    }

    public boolean isFalling() {
        return isFalling;
    }

    public Texture getCurrentTexture() {
        return texture;
    }

    public boolean isDestroyed() {
        return currentHealth <= 0;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }
}
