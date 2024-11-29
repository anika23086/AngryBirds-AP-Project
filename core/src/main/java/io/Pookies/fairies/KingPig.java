package io.Pookies.fairies;

public class KingPig extends Pig {
    private static final String TEXTURE_PATH = "kingpig.png";

    public KingPig(float x, float y) {
        super(TEXTURE_PATH, x, y);
        this.maxHealth = 4;
        this.currentHealth = maxHealth;
    }}
