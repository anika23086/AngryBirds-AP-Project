package io.Pookies.fairies;

public class RedBird extends Bird {
    public RedBird(float x, float y) {
        super("redbird.png", x, y); // Load a specific texture for BlueBird
    }

    @Override
    public int getStrengthAgainst(String material) {
        return 3;
    }
}
