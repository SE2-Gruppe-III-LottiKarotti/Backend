package at.aau.serg.websocketdemoserver.logic;

public record CensoredWordsDB(String [] censoredWords) {
    public static final CensoredWordsDB INSTANCE = new CensoredWordsDB(new String[]
            {"Fuck", "Arschloch", "4rschloch", "4rschl0ch", "Kurva"});
}
