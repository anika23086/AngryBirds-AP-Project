package io.Pookies.fairies;

import com.badlogic.gdx.math.Vector2;
import io.Pookies.fairies.Bird;

public class PinkBird extends Bird {
    public PinkBird(float x, float y) {
        super("pinkbird.png", x, y, 1f, 2000f);
    }

    @Override
    public int getStrengthAgainst(String material) {
        return 1;
    }

    @Override
    public Vector2 getVelocity() {
        return velocity;
    }

}
