package at.aau.serg.websocketdemoserver.logic;

import java.util.HashSet;
import java.util.Set;

public class CensoredWordsDB {

    public static Set<String> censoredWords = new HashSet<>();

    static {
        censoredWords.add("Fuck");
        censoredWords.add("Arschloch");
        censoredWords.add("4rschloch");
        censoredWords.add("4rschl0ch");
        censoredWords.add("Kurva");

    }
    public static Set<String> getCensoredWords() {
        return censoredWords;
    }


}
