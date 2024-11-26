package io.Pookies.fairies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Structure {
    protected Texture texture;
    protected Body body;
    protected float health;

    public Structure(World world, String texturePath, float x, float y) {
        texture = new Texture(texturePath);

        // Initialize the structure's physics body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);

        // Define the shape and fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.5f;
        body.createFixture(fixtureDef);
        shape.dispose();

        health = 100; // Initial health
    }

    public void render(SpriteBatch batch) {
        Vector2 position = body.getPosition();
        batch.draw(texture, position.x - 0.5f, position.y - 0.5f);
    }

    public void takeDamage(float damage) {
        health -= damage;
        if (health <= 0) {
            isDestroyed();
        }
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    public void dispose() {
        texture.dispose();
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }
}
