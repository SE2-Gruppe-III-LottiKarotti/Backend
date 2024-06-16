package at.aau.serg.websocketdemoserver.model.game;

import at.aau.serg.websocketdemoserver.logic.CensorMessageFunction;
import at.aau.serg.websocketdemoserver.logic.CensoredWordsDB;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ChatCensorTest {

    @Test
    public void testCensorTextWithCensoredWords() {
        String text = "You are a Fuck and an Arschloch!";
        String[] censoredWords = CensoredWordsDB.censoredWords;
        String expected = "You are a **** and an *********!";
        assertEquals(expected, CensorMessageFunction.censorText(text, censoredWords));
    }

    @Test
    public void testCensorTextWithMixedCase() {
        String text = "You are a fUcK and an ArSchLoCh!";
        String[] censoredWords = CensoredWordsDB.censoredWords;
        String expected = "You are a **** and an *********!";
        assertEquals(expected, CensorMessageFunction.censorText(text, censoredWords));
    }

    @Test
    public void testCensorTextWithNoCensoredWords() {
        String text = "Hello, how are you today?";
        String[] censoredWords = CensoredWordsDB.censoredWords;
        String expected = "Hello, how are you today?";
        assertEquals(expected, CensorMessageFunction.censorText(text, censoredWords));
    }

    @Test
    public void testCensorTextWithPartialMatch() {
        String text = "You are a kurva and an Arschloch!";
        String[] censoredWords = CensoredWordsDB.censoredWords;
        String expected = "You are a ***** and an *********!";
        assertEquals(expected, CensorMessageFunction.censorText(text, censoredWords));
    }

    @Test
    public void testCensorTextWithDifferentCensoredWords() {
        String text = "You are a 4rschloch and an 4rschl0ch!";
        String[] censoredWords = CensoredWordsDB.censoredWords;
        String expected = "You are a ********* and an *********!";
        assertEquals(expected, CensorMessageFunction.censorText(text, censoredWords));
    }
}

