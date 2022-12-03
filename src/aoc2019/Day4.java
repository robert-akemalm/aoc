package aoc2019;

import java.util.Arrays;

public class Day4 {
    public static void main(String[] args) {
        String input = INPUT;
        a(input);
        b(input);
    }

    private static void b(String input) {
        int[] parts = Arrays.stream(input.split("-")).mapToInt(Integer::parseInt).toArray();
        int count = 0;
        for (int i = parts[0]; i < parts[1]; i++) {
            int[] digits = String.valueOf(i).chars().map(c -> c - '0').toArray();
            if (hasStandAloneDouble(digits) && areIncreasing(digits)) {
                count++;
            }
        }
        System.out.println(count);
    }

    private static void a(String input) {
        int[] parts = Arrays.stream(input.split("-")).mapToInt(Integer::parseInt).toArray();
        int count = 0;
        for (int i = parts[0]; i < parts[1]; i++) {
            int[] digits = String.valueOf(i).chars().map(c -> c - '0').toArray();
            if (hasDouble(digits) && areIncreasing(digits)) {
                count++;
            }
        }
        System.out.println(count);
    }

    private static boolean areIncreasing(int[] digits) {
        for (int i = 0; i < digits.length - 1; i++) {
            if (digits[i] > digits[i + 1]) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasDouble(int[] digits) {
        for (int i = 0; i < digits.length - 1; i++) {
            if (digits[i] == digits[i + 1]) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasStandAloneDouble(int[] digits) {
        int currentDouble = -1;
        for (int i = 0; i < digits.length - 1; i++) {
            if (digits[i] != currentDouble) {
                currentDouble = digits[i];
                if (digits[i + 1] == digits[i]) {
                    if (i + 2 == digits.length || digits[i + 2] != digits[i]) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private static final String TEST_INPUT = "";

    private static final String INPUT = "264360-746325";
}
