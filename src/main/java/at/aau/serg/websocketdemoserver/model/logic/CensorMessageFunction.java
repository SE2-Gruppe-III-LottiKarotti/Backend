package at.aau.serg.websocketdemoserver.model.logic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CensorMessageFunction {
    public static String censorText(String text, String[] censoredWords) {
        if (text == null || censoredWords == null) {
            return text;
        }

        for (String word : censoredWords) {
            String replacement = "*".repeat(word.length());
            text = text.replaceAll("(?i)" + word, replacement);
        }

        return text;
    }
}
