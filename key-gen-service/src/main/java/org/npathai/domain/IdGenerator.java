package org.npathai.domain;

import java.util.HashMap;
import java.util.Map;

public class IdGenerator {
    private static final Map<Integer, Character> charSet = initDictionary();

    // in reverse. So reverse to get normal order
    private int[] current;

    public IdGenerator() {
        current = new int[] {0, 0, 0, 0, 0};
    }

    private static Map<Integer, Character> initDictionary() {
        Map<Integer, Character> charSet = new HashMap<Integer, Character>();
        int i = 0;
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            charSet.put(i++, ch);
        }

        for (char ch = 'a'; ch <= 'z'; ch++) {
            charSet.put(i++, ch);
        }
        return charSet;
    }

    public synchronized String generate() {
        return getAndIncrementCurrentId();
    }

    private String getAndIncrementCurrentId() {
        String id = encode();
        nextId();
        return id;
    }

    private String encode() {
        StringBuilder url = new StringBuilder();
        for (int i = current.length - 1; i >= 0; i--) {
            url.append(charSet.get(current[i]));
        }
        return url.toString();
    }

    // FIXME will fail at runtime with Index Out of Bounds if we exhaust all short urls
    void nextId() {
        int index = 0;
        int carry = 1;
        while (carry > 0) {
            int sum = current[index] + carry;
            current[index] = sum % 52;
            carry = sum / 52;
            index++;
        }
    }
}