package aoc2017;

public class Day15 {
    public static void main(String[] args) throws Exception {
        part1();
        long t = System.currentTimeMillis();
        part2();
        System.out.println(System.currentTimeMillis() - t);
    }

    private static void part1() throws Exception {
        Generator a = new Generator(679, 16807, 1);
        Generator b = new Generator(771, 48271, 1);
        int matches = 0;
        for (int i = 0; i < 40_000_000; i++) {
            if (a.next() == b.next()) {
                matches++;
            }
        }
        System.out.println(matches);
    }

    private static void part2() throws Exception {
        Generator a = new Generator(679, 16807, 4);
        Generator b = new Generator(771, 48271, 8);
        int matches = 0;
        for (int i = 0; i < 5_000_000; i++) {
            if (a.next() == b.next()) {
                matches++;
            }
        }
        System.out.println(matches);
    }

    private static class Generator {
        private final int multiplier;
        private final int divider;
        int prevValue;

        private Generator(int prevValue, int multiplier, int divider) {
            this.prevValue = prevValue;
            this.multiplier = multiplier;
            this.divider = divider;
        }

        int next() {
            while (true) {
                long val = ((long)prevValue) * multiplier;
                prevValue = (int) (val % Integer.MAX_VALUE);
                if ((prevValue % divider) == 0) {
                    return prevValue & 0xFFFF;
                }
            }
        }
    }

    private static final String DATA2 = "flqrgnkx";

    private static final String DATA = "hxtvlmkl";
}
