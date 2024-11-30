package io.Pookies.fairies;

import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;
    private int currentLevel;
    private List<Integer> completedLevels; // To track completed levels
    private float birdPositionX, birdPositionY; // Bird's current position
    private List<Pig> pigs; // List of remaining pigs
    private List<Structure> structures; // List of structures and their positions

    // Add other attributes like score, remaining attempts, etc.
    private int score;
    private int remainingAttempts;

    // Getters and setters for all fields
    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public List<Integer> getCompletedLevels() {
        return completedLevels;
    }

    public void setCompletedLevels(List<Integer> completedLevels) {
        this.completedLevels = completedLevels;
    }
}
