package io.Pookies.fairies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Structure {
    protected Texture texture;
    protected Vector2 position;
    protected int currentDurability;
    protected int maxDurability;

    public Structure(float x, float y, int durability) {
        this.position = new Vector2(x, y);
        this.maxDurability = durability;
        this.currentDurability = durability;
    }

    // Take damage from a bird
    public boolean takeHit(Bird bird) {
        // Subclasses will override this if they need specific damage calculations
        currentDurability -= bird.getPower();
        return isDestroyed();
    }

    public boolean isDestroyed() {
        return currentDurability <= 0;
    }

    public int getCurrentDurability() {
        return currentDurability;
    }

    public int getMaxDurability() {
        return maxDurability;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void dispose() {
        if (texture != null) texture.dispose();
    }

    public Vector2 getPosition() {
        return position;
    }

    public Texture getCurrentTexture() {
        return texture;
    }
}
