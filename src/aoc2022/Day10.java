package aoc2022;

import java.util.List;
import java.util.stream.IntStream;

public class Day10 {
    public static void main(String[] args) {
        a(TEST_INPUT);
        a(INPUT);
        b(TEST_INPUT);
        b(INPUT);
    }

    private static void b(String input) {
        List<Op> ops = parse(input);
        Cpu cpu = new Cpu();
        for (Op op : ops) {
            cpu.run(op);
        }
        Util.print(cpu.screen, '\u2588', ' ');
//        Util.print(cpu.screen, '#', '.');
    }

    private static void a(String input) {
        List<Op> ops = parse(input);
        Cpu cpu = new Cpu();
        for (Op op : ops) {
            cpu.run(op);
        }
        long sum = IntStream.of(20, 60, 100, 140, 180, 220).mapToLong(cycle -> cpu.cycleToSignalStrength[cycle]).sum();
        System.out.println(sum);
    }

    static List<Op> parse(String input) {
        return input.lines()
                    .map(line -> (Op) (line.startsWith("addx ") ? new AddX(Integer.parseInt(line.substring(5))) : new Noop()))
                    .toList();
    }

    private interface Op {
        int numCycles();
    }

    record AddX(int value) implements Op {
        @Override
        public int numCycles() {
            return 2;
        }
    }

    record Noop() implements Op {
        @Override
        public int numCycles() {
            return 1;
        }
    }

    private static class Cpu {
        int cpuClock = 0;
        int crtClock = 0;
        long register = 1;
        long[] cycleToSignalStrength = new long[300];
        boolean[][] screen = IntStream.range(0, 6).mapToObj(i -> new boolean[40]).toArray(boolean[][]::new);

        void run(Op op) {
            for (int i = 0; i < op.numCycles(); i++) {
                cpuClock++;
                int hPos = crtClock % 40;
                if (register - 1 <= hPos && hPos <= register + 1) {
                    screen[crtClock / 40][hPos] = true;
                }
                cycleToSignalStrength[cpuClock] = register * cpuClock;
                crtClock++;
            }
            if (op instanceof AddX) {
                register += ((AddX) op).value;
            }
        }
    }

    private static final String TEST_INPUT = "addx 15\n"
                                             + "addx -11\n"
                                             + "addx 6\n"
                                             + "addx -3\n"
                                             + "addx 5\n"
                                             + "addx -1\n"
                                             + "addx -8\n"
                                             + "addx 13\n"
                                             + "addx 4\n"
                                             + "noop\n"
                                             + "addx -1\n"
                                             + "addx 5\n"
                                             + "addx -1\n"
                                             + "addx 5\n"
                                             + "addx -1\n"
                                             + "addx 5\n"
                                             + "addx -1\n"
                                             + "addx 5\n"
                                             + "addx -1\n"
                                             + "addx -35\n"
                                             + "addx 1\n"
                                             + "addx 24\n"
                                             + "addx -19\n"
                                             + "addx 1\n"
                                             + "addx 16\n"
                                             + "addx -11\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "addx 21\n"
                                             + "addx -15\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "addx -3\n"
                                             + "addx 9\n"
                                             + "addx 1\n"
                                             + "addx -3\n"
                                             + "addx 8\n"
                                             + "addx 1\n"
                                             + "addx 5\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "addx -36\n"
                                             + "noop\n"
                                             + "addx 1\n"
                                             + "addx 7\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "addx 2\n"
                                             + "addx 6\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "addx 1\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "addx 7\n"
                                             + "addx 1\n"
                                             + "noop\n"
                                             + "addx -13\n"
                                             + "addx 13\n"
                                             + "addx 7\n"
                                             + "noop\n"
                                             + "addx 1\n"
                                             + "addx -33\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "addx 2\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "addx 8\n"
                                             + "noop\n"
                                             + "addx -1\n"
                                             + "addx 2\n"
                                             + "addx 1\n"
                                             + "noop\n"
                                             + "addx 17\n"
                                             + "addx -9\n"
                                             + "addx 1\n"
                                             + "addx 1\n"
                                             + "addx -3\n"
                                             + "addx 11\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "addx 1\n"
                                             + "noop\n"
                                             + "addx 1\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "addx -13\n"
                                             + "addx -19\n"
                                             + "addx 1\n"
                                             + "addx 3\n"
                                             + "addx 26\n"
                                             + "addx -30\n"
                                             + "addx 12\n"
                                             + "addx -1\n"
                                             + "addx 3\n"
                                             + "addx 1\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "addx -9\n"
                                             + "addx 18\n"
                                             + "addx 1\n"
                                             + "addx 2\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "addx 9\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "addx -1\n"
                                             + "addx 2\n"
                                             + "addx -37\n"
                                             + "addx 1\n"
                                             + "addx 3\n"
                                             + "noop\n"
                                             + "addx 15\n"
                                             + "addx -21\n"
                                             + "addx 22\n"
                                             + "addx -6\n"
                                             + "addx 1\n"
                                             + "noop\n"
                                             + "addx 2\n"
                                             + "addx 1\n"
                                             + "noop\n"
                                             + "addx -10\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "addx 20\n"
                                             + "addx 1\n"
                                             + "addx 2\n"
                                             + "addx 2\n"
                                             + "addx -6\n"
                                             + "addx -11\n"
                                             + "noop\n"
                                             + "noop\n"
                                             + "noop";

    private static final String INPUT = "noop\n"
                                        + "addx 7\n"
                                        + "addx -1\n"
                                        + "addx -1\n"
                                        + "addx 5\n"
                                        + "noop\n"
                                        + "noop\n"
                                        + "addx 1\n"
                                        + "addx 3\n"
                                        + "addx 2\n"
                                        + "noop\n"
                                        + "addx 2\n"
                                        + "addx 5\n"
                                        + "addx 2\n"
                                        + "addx 10\n"
                                        + "addx -9\n"
                                        + "addx 4\n"
                                        + "noop\n"
                                        + "noop\n"
                                        + "noop\n"
                                        + "addx 3\n"
                                        + "addx 5\n"
                                        + "addx -40\n"
                                        + "addx 26\n"
                                        + "addx -23\n"
                                        + "addx 2\n"
                                        + "addx 5\n"
                                        + "addx 26\n"
                                        + "addx -35\n"
                                        + "addx 12\n"
                                        + "addx 2\n"
                                        + "addx 17\n"
                                        + "addx -10\n"
                                        + "addx 3\n"
                                        + "noop\n"
                                        + "addx 2\n"
                                        + "addx 3\n"
                                        + "noop\n"
                                        + "addx 2\n"
                                        + "addx 3\n"
                                        + "noop\n"
                                        + "addx 2\n"
                                        + "addx 2\n"
                                        + "addx -39\n"
                                        + "noop\n"
                                        + "addx 15\n"
                                        + "addx -12\n"
                                        + "addx 2\n"
                                        + "addx 10\n"
                                        + "noop\n"
                                        + "addx -1\n"
                                        + "addx -2\n"
                                        + "noop\n"
                                        + "addx 5\n"
                                        + "noop\n"
                                        + "addx 5\n"
                                        + "noop\n"
                                        + "noop\n"
                                        + "addx 1\n"
                                        + "addx 4\n"
                                        + "addx -25\n"
                                        + "addx 26\n"
                                        + "addx 2\n"
                                        + "addx 5\n"
                                        + "addx 2\n"
                                        + "noop\n"
                                        + "addx -3\n"
                                        + "addx -32\n"
                                        + "addx 1\n"
                                        + "addx 4\n"
                                        + "addx -2\n"
                                        + "addx 3\n"
                                        + "noop\n"
                                        + "noop\n"
                                        + "addx 3\n"
                                        + "noop\n"
                                        + "addx 6\n"
                                        + "addx -17\n"
                                        + "addx 27\n"
                                        + "addx -7\n"
                                        + "addx 5\n"
                                        + "addx 2\n"
                                        + "addx 3\n"
                                        + "addx -2\n"
                                        + "addx 4\n"
                                        + "noop\n"
                                        + "noop\n"
                                        + "addx 5\n"
                                        + "addx 2\n"
                                        + "addx -39\n"
                                        + "noop\n"
                                        + "noop\n"
                                        + "addx 2\n"
                                        + "addx 5\n"
                                        + "addx 3\n"
                                        + "addx -2\n"
                                        + "addx 2\n"
                                        + "addx 11\n"
                                        + "addx -4\n"
                                        + "addx -5\n"
                                        + "noop\n"
                                        + "addx 10\n"
                                        + "addx -18\n"
                                        + "addx 19\n"
                                        + "addx 2\n"
                                        + "addx 5\n"
                                        + "addx 2\n"
                                        + "addx 2\n"
                                        + "addx 3\n"
                                        + "addx -2\n"
                                        + "addx 2\n"
                                        + "addx -37\n"
                                        + "noop\n"
                                        + "addx 5\n"
                                        + "addx 4\n"
                                        + "addx -1\n"
                                        + "noop\n"
                                        + "addx 4\n"
                                        + "noop\n"
                                        + "noop\n"
                                        + "addx 1\n"
                                        + "addx 4\n"
                                        + "noop\n"
                                        + "addx 1\n"
                                        + "addx 2\n"
                                        + "noop\n"
                                        + "addx 3\n"
                                        + "addx 5\n"
                                        + "noop\n"
                                        + "addx -3\n"
                                        + "addx 5\n"
                                        + "addx 5\n"
                                        + "addx 2\n"
                                        + "addx 3\n"
                                        + "noop\n"
                                        + "addx -32\n"
                                        + "noop";
}
