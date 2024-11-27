package io.Pookies.fairies;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class PinkBird extends Bird {
    public PinkBird(float x, float y) {
        super("pinkbird.png", x, y);
    }

    public Vector2 getVelocity() {
        return new Vector2(0, 0);
    }
}
