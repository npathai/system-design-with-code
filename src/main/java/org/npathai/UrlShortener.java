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
    // in reverse. So reverse to get normal order
    private int[] current = {0, 0, 0, 0, 0};

    public String toShort(String longUrl) {
        String shortUrl = "http://localhost/" + translate(current);
        nextUrl();
        shortToLong.put(shortUrl, longUrl);
        return shortUrl;
    }

    private String translate(int[] current) {
        StringBuilder url = new StringBuilder();
        for (int i = current.length - 1; i >= 0; i--) {
            url.append(charSet.get(current[i]));
        }
        return url.toString();
    }

    private void nextUrl() {
        int index = 0;
        int carry = 1;
        while (carry > 0) {
            int sum = current[index] + carry;
            current[index] = sum % 52;
            carry = sum / 52;
            index++;
        }
    }

    public String toLong(String shortUrl) {
        return shortToLong.get(shortUrl);
    }
}
