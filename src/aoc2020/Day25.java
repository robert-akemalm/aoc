package aoc2020;

public class Day25 {
    public static void main(String[] args) {
        a(TEST_INPUT);
        a(INPUT);
        b(TEST_INPUT);
        b(INPUT);
    }

    private static void b(String input) {
    }

    private static void a(String input) {
        long[] publicKeys = input.lines().mapToLong(Long::parseLong).toArray();
        long loopSize1 = -1;
        long loopSize2 = -1;
        long val = 1;
        for (int i = 1; loopSize1 == -1 || loopSize2 == -1; i++) {
            val *= 7;
            val %= 20201227;
            if (val == publicKeys[0]) {
                loopSize1 = i;
                System.out.println("loopSize1: " + loopSize1);
            }
            if (val == publicKeys[1]) {
                loopSize2 = i;
                System.out.println("loopSize2: " + loopSize2);
            }
        }
        System.out.println(transform(publicKeys[0], loopSize2));
        System.out.println(transform(publicKeys[1], loopSize1));
        System.out.println();
    }

    static long transform(long subjectNumber, long loopSize) {
        long value = 1;
        for (int i = 0; i < loopSize; i++) {
            value *= subjectNumber;
            value %= 20201227;
        }
        return value;
    }

    private static final String TEST_INPUT = "5764801\n"
                                             + "17807724";

    private static final String INPUT = "11349501\n"
                                        + "5107328";
}
