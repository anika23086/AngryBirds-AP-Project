package io.Pookies.fairies;

public class PurpleBird extends Bird {
    public PurpleBird(float x, float y) {
        super("purplebird.png", x, y); // Load a specific texture for BlueBird
    }

    @Override
    public int getStrengthAgainst(String material) {
        return 2;
    }
}
