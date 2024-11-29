package io.Pookies.fairies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


public class Pig {
    protected Texture texture;
    private Vector2 velocity;
    protected Vector2 position;
    private static final float GRAVITY = 500f; // Gravity constant.
    private boolean isFalling = false; // Tracks if the pig is falling.

    public Pig(String texturePath, float x, float y) {
        texture = new Texture(texturePath);
        position = new Vector2(x, y);
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
            velocity.y += GRAVITY * delta; // Apply gravity to the vertical velocity.
            position.y += velocity.y * delta; // Update the position based on velocity.

            // Prevent the pig from falling below the ground.
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
}
