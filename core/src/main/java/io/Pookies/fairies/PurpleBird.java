package io.Pookies.fairies;

import com.badlogic.gdx.math.Vector2;

public class PurpleBird extends Bird {
    public PurpleBird(float x, float y) {
        super("purplebird.png", x, y, 2f, 2000f);
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
