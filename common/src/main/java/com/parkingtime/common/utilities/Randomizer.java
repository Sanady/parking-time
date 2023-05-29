package com.parkingtime.common.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Randomizer {
    private static final Random random = new Random();
    private static StringBuilder sb;


    public static int numberGenerator(int size) {
        sb = new StringBuilder(String.valueOf(1 + random.nextInt(9)));
        for(int i = 0; i < size - 1; ++i) {
            sb.append(random.nextInt(10));
        }
        return Integer.parseInt(sb.toString());
    }

    public static String numericGenerator(int size) {
        sb = new StringBuilder(String.valueOf(1 + random.nextInt(9)));
        for(int i = 0; i < size - 1; ++i) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static String alphabeticGenerator(int size) {
        String stringArray = "abcdefghijklmnopqrstuvxyz";
        sb = new StringBuilder(size);

        for(int i = 0; i < size; ++i) {
            int index = (int)(stringArray.length() * Math.random());
            sb.append(stringArray.charAt(index));
        }
        return sb.toString();
    }

    public static String alphaNumericGenerator(int size) {
        String stringArray = "abcdefghijklmnopqrstuvxyz8123456789";
        sb = new StringBuilder(size);
        for(int i = 0; i < size; ++i) {
            int index = (int)(stringArray.length() * Math.random());
            sb.append(stringArray.charAt(index));
        }
        return sb.toString();
    }
}
