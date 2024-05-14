package at.aau.serg.websocketdemoserver.model.game;

import at.aau.serg.websocketdemoserver.msg.ChatMessage;
import org.testng.annotations.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Before;

public class GameboardTest {
        private Gameboard gameboard;

        @Before
        public void setup() {
            gameboard = new Gameboard(2);
        }

        @Test
        public void testGameboardInitialization() {
            // Check that the gameboard has been initialized with 26 fields
            assertEquals(26, gameboard.getFelder().length);
        }

        @Test
        public void testMolehillsInitialization() {
            // Check that the correct number of molehills have been initialized
            int maxMolehills = 6;
            int molehillCount = 0;
            for (Feld feld : gameboard.getFelder()) {
                if (feld != null && feld.isIstEsEinMaulwurfLoch()) {
                    molehillCount++;
                }
            }
            assertEquals(maxMolehills, molehillCount);
        }

    }
