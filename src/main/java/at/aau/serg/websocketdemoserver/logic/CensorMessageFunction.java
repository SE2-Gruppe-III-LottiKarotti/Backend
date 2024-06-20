package at.aau.serg.websocketdemoserver.logic;


import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CensorMessageFunction {
    public static String censorText(String text, Set<String> censoredWordsDB) {
        if (text == null || censoredWordsDB == null) {
            return text;
        }

        for (String word : censoredWordsDB) {
            //regex pattern
            //matcht groß und kleinschreibung --> einstellbar bei CASE_INSENSITIVE --> auch andere Parameter möglich
            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(word) + "\\b", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);
            String sign = "*";
            String replacement = sign.repeat(word.length());
            text = matcher.replaceAll(replacement);
        }
        return text;
    }
}
