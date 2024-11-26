package io.Pookies.fairies;

import com.badlogic.gdx.physics.box2d.World;

public class BubblePig extends Pig {
    public BubblePig(float x, float y, World world) {
        super("bubblepig.png", x, y, world);
        // Add specific physics properties for bubble pig
        body.getFixtureList().get(0).setRestitution(0.8f);
        body.getFixtureList().get(0).setDensity(0.8f);
        body.resetMassData();
    }
}
