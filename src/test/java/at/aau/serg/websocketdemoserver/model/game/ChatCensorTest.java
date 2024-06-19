package at.aau.serg.websocketdemoserver.model.game;

import at.aau.serg.websocketdemoserver.logic.CensorMessageFunction;
import at.aau.serg.websocketdemoserver.logic.CensoredWordsDB;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ChatCensorTest {

    private CensoredWordsDB censoredWordsDB;

    @BeforeEach
    public void setup() {
        censoredWordsDB = CensoredWordsDB.INSTANCE;
    }

    @Test
    public void testCensorTextWithCensoredWords() {
        String text = "You are a Fuck and an Arschloch!";
        String expected = "You are a **** and an *********!";
        assertEquals(expected, CensorMessageFunction.censorText(text, censoredWordsDB));
    }

    @Test
    public void testCensorTextWithMixedCase() {
        String text = "You are a fUcK and an ArSchLoCh!";
        String expected = "You are a **** and an *********!";
        assertEquals(expected, CensorMessageFunction.censorText(text, censoredWordsDB));
    }

    @Test
    public void testCensorTextWithNoCensoredWords() {
        String text = "Hello, how are you today?";
        String expected = "Hello, how are you today?";
        assertEquals(expected, CensorMessageFunction.censorText(text, censoredWordsDB));
    }

    @Test
    public void testCensorTextWithPartialMatch() {
        String text = "You are a kurva and an Arschloch!";
        String expected = "You are a ***** and an *********!";
        assertEquals(expected, CensorMessageFunction.censorText(text, censoredWordsDB));
    }

    @Test
    public void testCensorTextWithDifferentCensoredWords() {
        String text = "You are a 4rschloch and an 4rschl0ch!";
        String expected = "You are a ********* and an *********!";
        assertEquals(expected, CensorMessageFunction.censorText(text, censoredWordsDB));
    }

    @AfterEach
    public void tearDown() {
        censoredWordsDB = null;
    }
}

