package io.Pookies.fairies;

import com.badlogic.gdx.graphics.Texture;
import io.Pookies.fairies.Structure;

public class IceStructure extends Structure {
    private static final String TEXTURE_PATH = "icestructure.png";
    private static final int DURABILITY = 3;

    public IceStructure(float x, float y) {
        super(x, y, DURABILITY);
        this.texture = new Texture(TEXTURE_PATH);
    }

    @Override
    public boolean takeHit(Bird bird) {
        currentDurability -= bird.getPower();
        return isDestroyed();
    }}
