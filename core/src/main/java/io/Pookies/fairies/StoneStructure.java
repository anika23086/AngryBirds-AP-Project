package io.Pookies.fairies;

import com.badlogic.gdx.graphics.Texture;
import io.Pookies.fairies.Structure;

public class StoneStructure extends Structure {
    private static final String TEXTURE_PATH = "stonestructure.png";
    private static final int DURABILITY = 1;

    public StoneStructure(float x, float y) {
        super(x, y, DURABILITY);
        this.texture = new Texture(TEXTURE_PATH);
    }
}
