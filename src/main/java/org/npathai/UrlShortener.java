package org.npathai;

import java.util.HashMap;
import java.util.Map;

public class UrlShortener {

    private static final Map<Integer, Character> charSet = initDictionary();

    private static Map<Integer, Character> initDictionary() {
        Map<Integer, Character> charSet = new HashMap<>();
        int i = 0;
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            charSet.put(i++, ch);
        }

        for (char ch = 'a'; ch <= 'z'; ch++) {
            charSet.put(i++, ch);
        }
        return charSet;
    }

    private Map<String, String> shortToLong = new HashMap<>();
    private int[] current = {0, 0, 0, 0, 0};

    public String toShort(String longUrl) {
        String shortUrl = "http://localhost/" + String.valueOf(current);
        nextUrl();
        shortToLong.put(shortUrl, longUrl);
        return shortUrl;
    }

    private void nextUrl() {
        for (int idx = 0; idx < current.length; idx++) {

        }
    }

    public String toLong(String shortUrl) {
        return shortToLong.get(shortUrl);
    }
}
