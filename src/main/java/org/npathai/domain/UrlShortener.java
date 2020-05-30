package org.npathai.domain;

import org.npathai.dao.UrlDao;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UrlShortener {

    private static final Map<Integer, Character> charSet = initDictionary();
    private UrlDao dao;

    public UrlShortener(UrlDao dao) {
        this.dao = dao;
    }

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

    // in reverse. So reverse to get normal order
    private int[] current = {0, 0, 0, 0, 0};

    public String shorten(String longUrl) {
        String id;
        synchronized (this) {
            id = translate(current);
            nextUrl();
        }
        dao.save(id, longUrl);
        return id;
    }

    private String translate(int[] current) {
        StringBuilder url = new StringBuilder();
        for (int i = current.length - 1; i >= 0; i--) {
            url.append(charSet.get(current[i]));
        }
        return url.toString();
    }

    // FIXME will fail at runtime with Index Out of Bounds if we exhaust all short urls
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

    public String toLong(String id) {
        return dao.get(id);
    }

}
