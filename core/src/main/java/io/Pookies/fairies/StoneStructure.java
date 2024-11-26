package io.Pookies.fairies;

import com.badlogic.gdx.physics.box2d.World;

public class StoneStructure extends Structure {
    public StoneStructure(float x, float y, World world) {
        super(world, "stonestructure.png", x, y);
        // Adjust physics properties specific to stone
        body.getFixtureList().get(0).setDensity(2.0f);
        body.getFixtureList().get(0).setFriction(0.6f);
        body.resetMassData();
    }
}
