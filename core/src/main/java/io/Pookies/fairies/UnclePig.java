package io.Pookies.fairies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UnclePig extends Pig {
    private static final String TEXTURE_PATH = "unclepig.png";

    public UnclePig(float x, float y) {
        super(TEXTURE_PATH, x, y);
        this.maxHealth = 2;
        this.currentHealth = maxHealth;
    }
}
