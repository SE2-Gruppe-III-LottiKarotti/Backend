package at.aau.serg.websocketdemoserver.msg;

public class CensorMessage {
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
