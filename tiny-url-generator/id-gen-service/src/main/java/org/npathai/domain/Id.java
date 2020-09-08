package org.npathai.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * An Id is stored in encoded form of array of integers having length = N, N being the length of id.
 * Each encoded index of Id maps to a character set used to represent valid characters in id.
 * 0 - A
 * 1 - B
 * ...
 * 25 - Z
 * 26 - a
 * 27 - b
 * ...
 * 51 - z
 *
 * This has been done to made to find next incremented Id easier.
 * For instance: Id "AAAAZ" will be encoded as 0,0,0,0,25, it is easy to increment
 * 0,0,0,0,25 -> 0,0,0,0,26 and next id becomes "AAAAa".
 *
 * Read the tests for better understanding of how the encoding and decoding logic works.
 *
 */
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

    public Id incrementAndGet() throws IdExhaustedException {
        return incrementAndGet(1);
    }

    public Id incrementAndGet(int count) throws IdExhaustedException {
        int[] next = Arrays.copyOf(decoded, decoded.length);
        int index = 0;
        int carry = count;
        while (carry > 0 && index < decoded.length) {
            int sum = next[index] + carry;
            next[index] = sum % charSet.size();
            carry = sum / charSet.size();
            index++;
        }

        if (carry > 0) {
            // We have exhausted all ids possible within id size e.g. 5
            throw new IdExhaustedException();
        }
        return new Id(next);
    }
}
