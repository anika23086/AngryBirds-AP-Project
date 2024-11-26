package io.Pookies.fairies;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.Body;


public class Bird {
    protected Texture texture;
    protected Body body;
    protected boolean launched;
    protected float size;

    public Bird(String texturePath, float x, float y, World world){
        texture = new Texture(texturePath);
        size = texture.getWidth(); // Assuming square texture

        // Create physics body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Main.toMeters(x), Main.toMeters(y));

        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(Main.toMeters(size / 2));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.4f;

        body.createFixture(fixtureDef);
        shape.dispose();

        launched = false;
    }

    public Body getBody() {
        return body;
    }

    public void render(SpriteBatch batch){
        Vector2 position = body.getPosition();
        float angle = (float) Math.toDegrees(body.getAngle());
        batch.draw(texture,
            Main.toPixels(position.x) - size/2,
            Main.toPixels(position.y) - size/2,
            size/2, size/2,
            size, size,
            1, 1, angle,
            0, 0,
            texture.getWidth(), texture.getHeight(),
            false, false);
    }

    public void dispose(){
        texture.dispose();
    }

    public void setPosition(float x, float y){
        body.setTransform(Main.toMeters(x), Main.toMeters(y), body.getAngle());
    }

    public Vector2 getPosition(){
        Vector2 pos = body.getPosition();
        return new Vector2(Main.toPixels(pos.x), Main.toPixels(pos.y));
    }

    public void launch(Vector2 force){
        body.setType(BodyDef.BodyType.DynamicBody);
        body.applyLinearImpulse(force, body.getWorldCenter(), true);
        launched = true;
    }

    public boolean isLaunched(){
        return launched;
    }
}

