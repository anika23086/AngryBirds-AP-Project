package io.Pookies.fairies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Bird {
    protected Texture texture;
    protected Body body;
    protected float x, y; // Position coordinates

    public Bird(World world, String texturePath, float x, float y) {
        this.texture = new Texture(texturePath);
        this.x = x;
        this.y = y;

        // Initialize the bird's physics body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);

        // Define the shape and fixture
        CircleShape shape = new CircleShape();
        shape.setRadius(0.5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.5f;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void render(SpriteBatch batch) {
        Vector2 position = body.getPosition();
        batch.draw(texture, position.x - 0.5f, position.y - 0.5f);
    }

    public void launch(float velocity, float angle) {
        float vx = velocity * (float) Math.cos(angle);
        float vy = velocity * (float) Math.sin(angle);
        body.setLinearVelocity(vx, vy);
    }

    public void dispose() {
        texture.dispose();
    }
}
