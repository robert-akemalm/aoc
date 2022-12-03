package aoc2017;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day10 {
    public static void main(String[] args) throws Exception {
        part1();
        System.out.println(part2(DATA));
    }

    private static void part1() throws Exception {
        int[] numbers = IntStream.range(0, 256).toArray();
        int[] lengths = Stream.of(DATA.split(",")).mapToInt(Integer::parseInt).toArray();
        int currentPos = 0;
        int skipSize = 0;

        for (int length : lengths) {
            int[] reversedSubList = new int[length];
            for (int i = 0; i < length ; i++) {
                reversedSubList[length - i - 1] = numbers[(currentPos + i) % numbers.length];
            }

            for (int i = 0; i < length ; i++) {
                numbers[(currentPos + i) % numbers.length] = reversedSubList[i];
            }

            currentPos += length + skipSize;
            skipSize++;
        }

        System.out.println(numbers[0] * numbers[1]);
    }

    public static String part2(String data) throws Exception {
        int[] numbers = IntStream.range(0, 256).toArray();
        int[] lengths = IntStream.concat(data.chars(), IntStream.of(17, 31, 73, 47, 23)).toArray();
        int currentPos = 0;
        int skipSize = 0;

        for (int round = 0; round < 64; round++) {
            for (int length : lengths) {
                int[] reversedSubList = new int[length];
                for (int i = 0; i < length; i++) {
                    reversedSubList[length - i - 1] = numbers[(currentPos + i) % numbers.length];
                }

                for (int i = 0; i < length; i++) {
                    numbers[(currentPos + i) % numbers.length] = reversedSubList[i];
                }

                currentPos += length + skipSize;
                skipSize++;
            }
        }

        String[] denseHash = new String[16];
        for (int i = 0; i < 16; i++) {
            int denseValue = numbers[i * 16];
            for (int k = 1; k < 16; k++) {
                denseValue ^= numbers[i * 16 + k];
            }
            String hash = Integer.toString(denseValue, 16);
            if (hash.length() == 1) {
                hash = "0" + hash;
            }

            denseHash[i] = hash;
        }
        return Stream.of(denseHash).reduce((s, s2) -> s + s2).get();
    }

    private static final String DATA = "230,1,2,221,97,252,168,169,57,99,0,254,181,255,235,167";
}
