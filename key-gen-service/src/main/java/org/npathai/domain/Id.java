package org.npathai.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Id {
    private static Map<Integer, Character> charSet = new HashMap<>();
    private static Map<Character, Integer> reverseCharSet = new HashMap<>();

    static {
        int i = 0;
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            charSet.put(i, ch);
            reverseCharSet.put(ch, i);
            i++;
        }

        for (char ch = 'a'; ch <= 'z'; ch++) {
            charSet.put(i, ch);
            reverseCharSet.put(ch, i);
            i++;
        }
    }

    // in reverse because of addition logic. So reverse to get normal order
    private int[] decoded;

    private Id(int[] decoded) {
        this.decoded = decoded;
    }

    public static Id fromDecoded(int[] decoded) {
        return new Id(decoded);
    }

    public static Id first() {
        return new Id(new int[] {0, 0, 0, 0, 0});
    }

    public static Id fromEncoded(String encoded) {
        return new Id(decode(encoded));
    }

    private static int[] decode(String encoded) {
        int[] decoded = new int[] {0, 0, 0, 0, 0};
        for (int i = 0; i < encoded.length(); i++) {
            decoded[encoded.length() - i - 1] = reverseCharSet.get(encoded.charAt(i));
        }
        return decoded;
    }

    public String encode() {
        StringBuilder url = new StringBuilder();
        for (int i = decoded.length - 1; i >= 0; i--) {
            url.append(charSet.get(decoded[i]));
        }
        return url.toString();
    }

    // FIXME will fail at runtime with Index Out of Bounds if we exhaust all short urls
    public Id next() {
        int[] next = Arrays.copyOf(decoded, decoded.length);
        int index = 0;
        int carry = 1;
        while (carry > 0) {
            int sum = next[index] + carry;
            next[index] = sum % charSet.size();
            carry = sum / charSet.size();
            index++;
        }
        return new Id(next);
    }
}
