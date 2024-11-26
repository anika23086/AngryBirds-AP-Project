package io.Pookies.fairies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class PinkBird {
    public float getY;
    protected Texture texture;
    protected Body body;
    protected boolean isLaunched = false;
    protected Vector2 initialPosition;

    public PinkBird(World world, String texturePath, float x, float y) {
        this.texture = new Texture(texturePath);
        this.initialPosition = new Vector2(x, y);

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

        body.setUserData(this);
    }

    public void render(SpriteBatch batch) {
        Vector2 position = body.getPosition();
        batch.draw(texture, position.x - 0.5f, position.y - 0.5f);
    }

    public void launch(float velocityX, float velocityY) {
        isLaunched = true;
        body.applyLinearImpulse(new Vector2(velocityX, velocityY), body.getWorldCenter(), true);
    }

    public void dispose() {
        texture.dispose();
    }

    public boolean isLaunched() {
        return isLaunched;
    }

    public void setPosition(float x, float y) {
        body.setTransform(x, y, body.getAngle());
    }

    public void resetPosition() {
        body.setTransform(initialPosition.x, initialPosition.y, body.getAngle());
        body.setLinearVelocity(0, 0);
        isLaunched = false;
    }

    public float getX() {
        return body.getPosition().x;
    }

    public float getY() {
        return body.getPosition().y;
    }
}
