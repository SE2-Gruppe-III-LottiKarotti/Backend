package at.aau.serg.websocketdemoserver.logic;



public class CensorMessageFunction {
    public static String censorText(String text, CensoredWordsDB censoredWordsDB) {
        if (text == null || censoredWordsDB == null) {
            return text;
        }

        for (String word : censoredWordsDB.censoredWords()) {
            String replacement = "*".repeat(word.length());
            text = text.replaceAll("(?i)" + word, replacement);
        }

        return text;
    }
}
