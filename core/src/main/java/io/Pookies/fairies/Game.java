package io.Pookies.fairies;

class Level {
    private int levelNumber;

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }
}

public class Game {
    private int score;
    private int highScore;
    private int remainingBirds;
    private Level currentLevel;

    public Game() {
        this.score = 0;
        this.highScore = 0;
        this.remainingBirds = 3;
        this.currentLevel = new Level(1);
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

    public void restartGame() {
        System.out.println("Game restarted.");
        score = 0;
        currentLevel = new Level(1);
        remainingBirds = 3;
        startGame();
    }

    public void nextLevel() {
        System.out.println("Moving to the next level.");
        currentLevel.setLevelNumber(currentLevel.getLevelNumber() + 1);
        remainingBirds = 3;
        System.out.println("Welcome to Level " + currentLevel.getLevelNumber() + ".");
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

    public void launchBird() {
        if (remainingBirds > 0) {
            remainingBirds--;
            System.out.println("Bird launched!");
        } else {
            System.out.println("No birds left to launch!");
        }
    }

    public void resetGameplay() {
        score = 0;
        remainingBirds = 3;
        System.out.println("Gameplay reset. Good luck!");
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

    public Level getCurrentLevel() {
        return currentLevel;
    }
}

