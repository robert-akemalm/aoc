package aoc2017;

import java.util.HashMap;
import java.util.Map;

public class Day13 {
    public static void main(String[] args) throws Exception {
        part1();
        part2();
    }

    private static void part1() throws Exception {
        Map<Integer, Integer> scanners = new HashMap<>();
        int max = 0;
        for (String line : DATA.split("\n")) {
            int depth = Integer.parseInt(line.substring(0, line.indexOf(":")));
            int range = Integer.parseInt(line.substring(line.indexOf(":") + 2));
            scanners.put(depth, range);
            max = Math.max(max, depth);
        }

        int penalty = 0;
        for (int depth = 0; depth <= max; depth++) {
            Integer range = scanners.get(depth);
            if (range != null) {
                int lapLength = (range - 1) * 2;
                if (depth % lapLength == 0) {
                    penalty += range * depth;
                }
            }
        }
        System.out.println(penalty);
    }

    private static void part2() throws Exception {
        Map<Integer, Integer> scanners = new HashMap<>();
        int max = 0;
        for (String line : DATA.split("\n")) {
            int depth = Integer.parseInt(line.substring(0, line.indexOf(":")));
            int range = Integer.parseInt(line.substring(line.indexOf(":") + 2));
            scanners.put(depth, range);
            max = Math.max(max, depth);
        }

        for (int delay = 0; delay < Integer.MAX_VALUE; delay++) {
            int penalty = 0;
            for (int depth = 0; depth <= max; depth++) {
                Integer range = scanners.get(depth);
                if (range != null) {
                    int lapLength = (range - 1) * 2;
                    if ((delay + depth) % lapLength == 0) {
                        penalty++;
                    }
                }
            }
            if (penalty == 0) {
                System.out.println(delay);
                return;
            }
        }
    }

    private static final String DATA2 = "0: 3\n"
                                        + "1: 2\n"
                                        + "4: 4\n"
                                        + "6: 4";

    private static final String DATA = "0: 5\n"
                                       + "1: 2\n"
                                       + "2: 3\n"
                                       + "4: 4\n"
                                       + "6: 8\n"
                                       + "8: 4\n"
                                       + "10: 6\n"
                                       + "12: 6\n"
                                       + "14: 8\n"
                                       + "16: 6\n"
                                       + "18: 6\n"
                                       + "20: 12\n"
                                       + "22: 14\n"
                                       + "24: 8\n"
                                       + "26: 8\n"
                                       + "28: 9\n"
                                       + "30: 8\n"
                                       + "32: 8\n"
                                       + "34: 12\n"
                                       + "36: 10\n"
                                       + "38: 12\n"
                                       + "40: 12\n"
                                       + "44: 14\n"
                                       + "46: 12\n"
                                       + "48: 10\n"
                                       + "50: 12\n"
                                       + "52: 12\n"
                                       + "54: 12\n"
                                       + "56: 14\n"
                                       + "58: 12\n"
                                       + "60: 14\n"
                                       + "62: 14\n"
                                       + "64: 14\n"
                                       + "66: 14\n"
                                       + "68: 17\n"
                                       + "70: 12\n"
                                       + "72: 14\n"
                                       + "76: 14\n"
                                       + "78: 14\n"
                                       + "80: 14\n"
                                       + "82: 18\n"
                                       + "84: 14\n"
                                       + "88: 20";
}
