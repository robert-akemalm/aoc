package aoc2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day2 {
    public static void main(String[] args) {
        String input = INPUT;
        a(input);
        b(input);
    }

    private static void b(String input) {
        int expected = 19690720;
        for (int x = 0; x < 1024; x++) {
            yloop:
            for (int y = 0; y < 1024; y++) {
                try {
                    long[] ints = Arrays.stream(input.split(",")).mapToLong(Long::parseLong).toArray();
                    ints[1] = x;
                    ints[2] = y;
                    int startIx = 0;
                    while (true) {
                        long operation = ints[startIx++];
                        if (operation == 1) {
                            long a = ints[(int) ints[startIx++]];
                            long b = ints[(int) ints[startIx++]];
                            ints[(int) ints[startIx++]] = a + b;
                        } else if (operation == 2) {
                            long a = ints[(int) ints[startIx++]];
                            long b = ints[(int) ints[startIx++]];
                            ints[(int) ints[startIx++]] = a * b;
                        } else if (operation == 99) {
                            if (ints[0] == expected) {
                                System.out.println(100 * x + y);
                                return;
                            } else {
                                continue yloop;
                            }
                        } else {
                            throw new RuntimeException("" + operation);
                        }
                    }
                } catch (Exception e) {

                }
            }

        }
    }

    private static void a(String input) {
        long[] ints = Arrays.stream(input.split(",")).mapToLong(Long::parseLong).toArray();
        ints[1] = 12;
        ints[2] = 2;
        int startIx = 0;
        while (true) {
            long operation = ints[startIx++];
            if (operation == 1) {
                long a = ints[(int) ints[startIx++]];
                long b = ints[(int) ints[startIx++]];
                ints[(int) ints[startIx++]] = a + b;
            } else if (operation == 2) {
                long a = ints[(int) ints[startIx++]];
                long b = ints[(int) ints[startIx++]];
                ints[(int) ints[startIx++]] = a * b;
            } else if (operation == 99) {
                System.out.println(ints[0]);
                return;
            } else {
                throw new RuntimeException("" + operation);
            }
        }
    }

    private static final String TEST_INPUT = "1,9,10,3,2,3,11,0,99,30,40,50";

    private static final String INPUT =
            "1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,1,6,19,1,5,19,23,1,23,6,27,1,5,27,31,1,31,6,35,1,9,35,39,2,10,39,43,1,43,6,47,2,6,47,51,1,5,51,55,1,55,13,59,1,59,10,63,2,10,63,67,1,9,67,71,2,6,71,75,1,5,75,79,2,79,13,83,1,83,5,87,1,87,9,91,1,5,91,95,1,5,95,99,1,99,13,103,1,10,103,107,1,107,9,111,1,6,111,115,2,115,13,119,1,10,119,123,2,123,6,127,1,5,127,131,1,5,131,135,1,135,6,139,2,139,10,143,2,143,9,147,1,147,6,151,1,151,13,155,2,155,9,159,1,6,159,163,1,5,163,167,1,5,167,171,1,10,171,175,1,13,175,179,1,179,2,183,1,9,183,0,99,2,14,0,0";
}
