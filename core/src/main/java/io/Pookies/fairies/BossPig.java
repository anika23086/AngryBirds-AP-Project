package io.Pookies.fairies;

public class BossPig extends Pig {
    private static final String TEXTURE_PATH = "bosspig.png";

    public BossPig(float x, float y) {
        super(TEXTURE_PATH, x, y);
        this.maxHealth = 4;
        this.currentHealth = maxHealth;
    }}
