package io.Pookies.fairies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Slingshot extends Structure {
    private static final String TEXTURE_PATH = "slingshot.png";

    public Slingshot(float x, float y) {
        super(x, y, 0);
        this.texture = new Texture(TEXTURE_PATH);
    }
}
