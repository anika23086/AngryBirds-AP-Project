package io.Pookies.fairies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public abstract class Bird extends Image implements Disposable {
    protected Vector2 velocity;
    protected Vector2 originalPosition;
    protected Vector2 slingshotPosition;
    protected boolean isDragging;
    protected boolean isLaunched;
    protected static final float GRAVITY = 980f;
    protected static final float LAUNCH_SPEED_MULTIPLIER = 8f;
    protected static final float MAX_PULL_DISTANCE = 150f;
    protected float launchAngle;
    protected float pullDistance;
    protected float power;
    protected float birdVelocity;
    protected Texture birdTexture;

    public Bird(String texturePath, float x, float y, float power, float birdVelocity) {
        this.birdTexture = new Texture(Gdx.files.internal(texturePath));
        super.setDrawable(new TextureRegionDrawable(new TextureRegion(birdTexture)));

        this.velocity = new Vector2(0, 0);
        this.originalPosition = new Vector2(x-20, y);
        this.slingshotPosition = new Vector2(x, y);
        this.isDragging = false;
        this.isLaunched = false;
        this.launchAngle = 0;
        this.pullDistance = 0;
        this.power = power;
        this.birdVelocity = birdVelocity;

        setPosition(x, y);
        setSize(birdTexture.getWidth(), birdTexture.getHeight());
    }

    public Texture getTexture() {
        return birdTexture;
    }

    public void startDragging() {
        isDragging = true;
        velocity.set(0, 0);
        isLaunched = false;
    }

    public void drag(float x, float y) {
        if (isDragging) {
            setPosition(x, y);
        }
    }

    public void launch(float velocityX, float velocityY) {
        isDragging = false;
        isLaunched = true;
        velocity.set(velocityX, velocityY);
    }

    public void reset() {
        setPosition(originalPosition.x, originalPosition.y);
        velocity.set(0, 0);
        isLaunched = false;
        isDragging = false;
        launchAngle = 0;
        pullDistance = 0;
    }

    public void update(float delta) {
        if (isLaunched) {
            velocity.y -= GRAVITY * delta;
            setPosition(getX() + velocity.x * delta, getY() + velocity.y * delta);
        }
    }

    public boolean contains(float x, float y) {
        return x >= getX() && x <= getX() + getWidth() &&
            y >= getY() && y <= getY() + getHeight();
    }

    @Override
    public void dispose() {
        if (birdTexture != null) {
            birdTexture.dispose();
        }
    }

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setPosition(Vector2 position) {
        super.setPosition(position.x, position.y);
    }

    public float getPower() {
        return power;
    }

    public float getBirdVelocity() {
        return birdVelocity;
    }

    public Texture getCurrentTexture() {
        return birdTexture;
    }

    public abstract int getStrengthAgainst(String material);
}
