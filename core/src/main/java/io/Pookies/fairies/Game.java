package io.Pookies.fairies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private int score;
    private int highScore;
    private int remainingBirds;
    private World world;
    private List<Bird> birds;
    private List<Pig> pigs;
    private List<Structure> structures;
    private SpriteBatch batch; // Add SpriteBatch instance

    public Game(SpriteBatch batch) {
        this.score = 0;
        this.highScore = 0;
        this.remainingBirds = 3;
        this.world = new World(new Vector2(0, -10), true);
        this.birds = new ArrayList<>();
        this.pigs = new ArrayList<>();
        this.structures = new ArrayList<>();
        this.batch = batch; // Initialize SpriteBatch instance

        // Set up contact listener for collision handling
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                handleCollision(contact);
            }

            @Override
            public void endContact(Contact contact) {}

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        });
    }

    public void startGame() {
        System.out.println("Game started.");
        resetGameplay();
    }

    public void endGame() {
        System.out.println("Game ended.");
        if (score > highScore) {
            highScore = score;
            System.out.println("New high score achieved!");
        }
    }

    public void updateScore(int points) {
        score += points;
        System.out.println("Score updated! Current score: " + score + ".");
    }

    public boolean checkForVictory() {
        // Placeholder for victory condition
        boolean victory = score >= 100;  // Example condition
        if (victory) {
            System.out.println("Victory achieved!");
        }
        return victory;
    }

    public void launchBird(float velocity, float angle) {
        if (remainingBirds > 0) {
            remainingBirds--;
            Bird bird = new Bird(world, "bird.png", 0, 0); // Create a new bird
            birds.add(bird);
            bird.launch(velocity, angle);
            System.out.println("Bird launched!");
        } else {
            System.out.println("No birds left to launch!");
        }
    }

    public void resetGameplay() {
        score = 0;
        remainingBirds = 3;
        birds.clear();
        pigs.clear();
        structures.clear();
        System.out.println("Gameplay reset. Good luck!");
    }

    public void update() {
        // Update the physics world
        world.step(1/60f, 6, 2);

        // Update game objects
        for (Bird bird : birds) {
            bird.render(batch); // Use SpriteBatch instance to render
        }
        for (Pig pig : pigs) {
            pig.render(batch); // Use SpriteBatch instance to render
        }
        for (Structure structure : structures) {
            structure.render(batch); // Use SpriteBatch instance to render
        }
    }

    private void handleCollision(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        // Check if the collision involves a bird
        if (isBird(bodyA) || isBird(bodyB)) {
            Bird bird = isBird(bodyA) ? (Bird) bodyA.getUserData() : (Bird) bodyB.getUserData();
            Body otherBody = isBird(bodyA) ? bodyB : bodyA;

            // Check if the other body is a pig
            if (isPig(otherBody)) {
                Pig pig = (Pig) otherBody.getUserData();
                pig.takeDamage(10); // Example damage value
                if (pig.isDestroyed()) {
                    pigs.remove(pig);
                    world.destroyBody(pig.body);
                    updateScore(50); // Example score for destroying a pig
                }
            }
            // Check if the other body is a structure
            else if (isStructure(otherBody)) {
                Structure structure = (Structure) otherBody.getUserData();
                structure.takeDamage(10); // Example damage value
                if (structure.isDestroyed()) {
                    structures.remove(structure);
                    world.destroyBody(structure.body);
                    updateScore(20); // Example score for destroying a structure
                }
            }
        }
    }

    private boolean isBird(Body body) {
        return body.getUserData() instanceof Bird;
    }

    private boolean isPig(Body body) {
        return body.getUserData() instanceof Pig;
    }

    private boolean isStructure(Body body) {
        return body.getUserData() instanceof Structure;
    }

    // Getters and setters if needed
    public int getScore() {
        return score;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getRemainingBirds() {
        return remainingBirds;
    }
}

