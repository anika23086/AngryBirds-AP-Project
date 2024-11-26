package io.Pookies.fairies;

import com.badlogic.gdx.physics.box2d.World;

public class PurpleBird extends Bird {
    public PurpleBird(float x, float y, World world) {
        super(world, "purplebird.png", x, y); // Load a specific texture for BlueBird
    }
}
