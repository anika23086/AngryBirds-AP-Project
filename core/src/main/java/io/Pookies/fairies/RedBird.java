package io.Pookies.fairies;

import com.badlogic.gdx.math.Vector2;

public class RedBird extends Bird {
    public RedBird(float x, float y) {
        super("redbird.png", x, y, 3f, 3000f);
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
