package io.Pookies.fairies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BubblePig extends Pig {
    private static final String TEXTURE_PATH = "bubblepig.png";

    public BubblePig(float x, float y) {
        super(TEXTURE_PATH, x, y);
        this.maxHealth = 1;
        this.currentHealth = maxHealth;
    }
}
