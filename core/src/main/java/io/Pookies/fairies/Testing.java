//package io.Pookies.fairies;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import com.badlogic.gdx.Game;
//
//public class Testing {
//
//    private LevelScreen_1 levelScreen;
//
//    @BeforeEach
//    public void setUp() {
//        Game mockedGame = Mockito.mock(Game.class);
//        levelScreen = new LevelScreen_1(mockedGame);
//        levelScreen.birdsUsed = 0;
//        levelScreen.pigDestroyed = false;
//        levelScreen.birdDestroyed = false;
//        levelScreen.failureTriggered = false;
//        levelScreen.levelComplete = false;
//        levelScreen.stoneStructure1Destroyed = false;
//        levelScreen.stoneStructure2Destroyed = false;
//    }
//
//    @Test
//    public void testBirdMissesPigAndHitsNothing() {
//        levelScreen.birdsUsed = 1;
//        levelScreen.pigDestroyed = false;
//        levelScreen.checkForFailureConditions(1.0f);
//        assertTrue(levelScreen.failureTriggered);
//        assertFalse(levelScreen.levelComplete);
//        assertEquals(1, levelScreen.birdsUsed);
//    }
//
//    @Test
//    public void testBirdDestroysPig() {
//        levelScreen.bubblePig.takeHit(Mockito.mock(PinkBird.class));
//        levelScreen.pigDestroyed = true;
//        levelScreen.birdsUsed = 1;
//        levelScreen.handlePigCollision();
//        assertTrue(levelScreen.levelComplete);
//        assertTrue(levelScreen.pigDestroyed);
//        assertEquals(1, levelScreen.birdsUsed);
//    }
//
//    @Test
//    public void testBirdHitsBlock() {
//        levelScreen.handleStructureCollision(1);
//        assertTrue(levelScreen.stoneStructure1Destroyed);
//        assertEquals(1, levelScreen.birdsUsed);
//        assertFalse(levelScreen.pigDestroyed);
//    }
//
//    @Test
//    public void testBirdLeavesScreen() {
//        levelScreen.birdsUsed = 0;
//        levelScreen.pinkBird.setPosition(-10, -10);
//        levelScreen.checkForFailureConditions(1.0f);
//        assertEquals(1, levelScreen.birdsUsed);
//        assertTrue(levelScreen.birdDestroyed);
//    }
//
//    @Test
//    public void testAllBirdsUsedAndPigNotDestroyed() {
//        levelScreen.birdsUsed = 1;
//        levelScreen.pigDestroyed = false;
//        levelScreen.checkForFailureConditions(1.0f);
//        assertTrue(levelScreen.failureTriggered);
//        assertFalse(levelScreen.levelComplete);
//    }
//
//    @Test
//    public void testStoneStructure2Hits() {
//        levelScreen.handleStructureCollision(2);
//        assertTrue(levelScreen.stoneStructure2Destroyed);
//        assertEquals(1, levelScreen.birdsUsed);
//        assertFalse(levelScreen.pigDestroyed);
//    }
//
//    @Test
//    public void testLevelCompletion() {
//        levelScreen.pigDestroyed = true;
//        levelScreen.birdsUsed = 1;
//        levelScreen.handlePigCollision();
//        assertTrue(levelScreen.levelComplete);
//        assertTrue(levelScreen.pigDestroyed);
//        assertEquals(1, levelScreen.birdsUsed);
//    }
//}
