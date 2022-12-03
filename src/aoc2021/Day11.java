package aoc2021;

public class Day11 {
    public static void main(String[] args) {
        a(TEST_INPUT);
        a(INPUT);
        b(TEST_INPUT);
        b(INPUT);
    }

    private static void b(String input) {
        int[][] values = input.lines().map(l -> l.chars().map(c -> c - '0').toArray()).toArray(int[][]::new);
        int expected = values.length * values[0].length;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            for (int y = 0; y < values.length; y++) {
                for (int x = 0; x < values[y].length; x++) {
                    values[y][x]++;
                }
            }

            int flashes = 0;
            for (int y = 0; y < values.length; y++) {
                for (int x = 0; x < values[y].length; x++) {
                    if (values[y][x] > 9) {
                        flashes += flash(values, y, x);
                    }
                }
            }
            if (flashes == expected) {
                System.out.println((i + 1) + ": " + flashes);
                break;
            }
        }
    }

    private static void a(String input) {
        int[][] values = input.lines().map(l -> l.chars().map(c -> c - '0').toArray()).toArray(int[][]::new);
        int flashes = 0;
        for (int i = 0; i < 100; i++) {
            for (int y = 0; y < values.length; y++) {
                for (int x = 0; x < values[y].length; x++) {
                    values[y][x]++;
                }
            }

            for (int y = 0; y < values.length; y++) {
                for (int x = 0; x < values[y].length; x++) {
                    if (values[y][x] > 9) {
                        flashes += flash(values, y, x);
                    }
                }
            }
            System.out.println(i + ": " + flashes);
        }
        System.out.println(flashes);
    }

    static int flash(int[][] values, int y, int x) {
        int flashes = 1;
        values[y][x] = 0;
        for (int y1 = y - 1; y1 <= y + 1; y1++) {
            if (y1 < 0 || y1 >= values.length) {
                continue;
            }
            for (int x1 = x - 1; x1 <= x + 1; x1++) {
                if (x1 < 0 || x1 >= values[y1].length) {
                    continue;
                }
                if (values[y1][x1] != 0) {
                    values[y1][x1]++;
                    if (values[y1][x1] > 9) {
                        flashes += flash(values, y1, x1);
                    }
                }
            }
        }
        return flashes;
    }

    private static final String TEST_INPUT = "5483143223\n"
                                             + "2745854711\n"
                                             + "5264556173\n"
                                             + "6141336146\n"
                                             + "6357385478\n"
                                             + "4167524645\n"
                                             + "2176841721\n"
                                             + "6882881134\n"
                                             + "4846848554\n"
                                             + "5283751526";

    private static final String INPUT = "2138862165\n"
                                        + "2726378448\n"
                                        + "3235172758\n"
                                        + "6281242643\n"
                                        + "4256223158\n"
                                        + "1112268142\n"
                                        + "1162836182\n"
                                        + "1543525861\n"
                                        + "1882656326\n"
                                        + "8844263151";
}
