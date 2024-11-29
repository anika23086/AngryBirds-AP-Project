package io.Pookies.fairies;

import com.badlogic.gdx.graphics.Texture;
import io.Pookies.fairies.Structure;

public class WoodStructure extends Structure {
    private static final String TEXTURE_PATH = "woodstructure.png";
    private static final int DURABILITY = 2;

    public WoodStructure(float x, float y) {
        super(x, y, DURABILITY);
        this.texture = new Texture(TEXTURE_PATH);
    }
}
