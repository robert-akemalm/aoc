package aoc2018;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Day12 {
    public static void main(String[] args) {
        String input = INPUT;
        a(input);
        b(input);
    }

    private static void b(String input) {
        String[] lines = Util.parseStrings(input);
        String initialString = lines[0].substring(lines[0].indexOf(':')+2);
        int zeroPos = initialString.length()*5;
        boolean[] state = new boolean[initialString.length() * 10];
        for (int i = 0; i < initialString.length(); i++) {
            state[zeroPos + i] = initialString.charAt(i) == '#';
        }

        boolean[] notes = new boolean[32];
        for (int n = 2; n < lines.length; n++) {
            boolean newState = lines[n].charAt(lines[n].length() - 1) == '#';
            notes[parseNote(lines[n])] = newState;
        }


        for (long i = 0; i < 50_000_000_000L; i++) {
            int firstSetInCurrent = 0;
            int lastSetInCurrent = 0;
            boolean[] current = state;
            for (int j = 0; j < state.length; j++) {
                if (state[j]) {
                    if (firstSetInCurrent == 0) {
                        firstSetInCurrent = j;
                    }
                    lastSetInCurrent = j;
                }
            }
            state = nextGeneration(state, notes);
            int firstSetInNext = 0;
            int lastSetInNext = 0;
            for (int j = 0; j < state.length; j++) {
                if (state[j]) {
                    if (firstSetInNext == 0) {
                        firstSetInNext = j;
                    }
                    lastSetInNext = j;
                }
            }
            current = Arrays.copyOfRange(current, firstSetInCurrent, lastSetInCurrent + 1);
            boolean[] next = Arrays.copyOfRange(state, firstSetInNext, lastSetInNext + 1);
            if (Arrays.equals(current, next)) {
                System.out.println("Repeated testRegister " + i + " iterations");
                int diff = firstSetInNext - firstSetInCurrent;
                long generationsLeft =  50_000_000_000L - i;
                long firstSet = firstSetInCurrent + diff * generationsLeft;
                firstSet -= zeroPos;
                sum(current, -firstSet);
                System.out.println(sum(current, -firstSet));
                break;
            }
        }

    }

    private static void a(String input) {
        String[] lines = Util.parseStrings(input);
        String initialString = lines[0].substring(lines[0].indexOf(':')+2);
        boolean[] state = new boolean[initialString.length() * 3];
        for (int i = 0; i < initialString.length(); i++) {
            state[initialString.length() + i] = initialString.charAt(i) == '#';
        }

        boolean[] notes = new boolean[32];
        for (int n = 2; n < lines.length; n++) {
            boolean newState = lines[n].charAt(lines[n].length() - 1) == '#';
            notes[parseNote(lines[n])] = newState;
        }


        for (int i = 0; i < 20; i++) {
            state = nextGeneration(state, notes);
        }
        System.out.println(sum(state, initialString.length()));
    }

    static String toString(boolean[] state) {
        StringBuilder sb = new StringBuilder(state.length);
        for (int i = 0; i < state.length; i++) {
            sb.append(state[i] ? '#' : '.');
        }
        return sb.toString();
    }
    static long sum(boolean[] state, long zeroPos) {
        long sum = 0;
        for (int i = 0; i < state.length; i++) {
            if (state[i]) {
                sum += i - zeroPos;
            }
        }
        return sum;
    }
    static boolean[] nextGeneration(boolean[] current, boolean[] notes) {
        for (int i = 0; i < 3; i++) {
            if (current[i]) {
                throw new RuntimeException();
            }
        }
        for (int i = 0; i < 3; i++) {
            if (current[current.length - i - 1]) {
                System.out.println(toString(current));
                throw new RuntimeException();
            }
        }
        boolean[] next = new boolean[current.length];
        for (int i = 0; i < current.length - 4; i++) {
            int noteIx = toNoteIx(current[i], current[i+1], current[i+2], current[i+3], current[i+4]);
            boolean isPlant = notes[noteIx];
            next[i + 2] = isPlant;
        }
        return next;
    }

    private static int toNoteIx(boolean... plants) {
        int ix = 0;
        for (int i = 0; i < plants.length; i++) {
            if (plants[i]) {
                ix += Math.pow(2, i);
            }
        }
        return ix;

    }

    private static int parseNote(String input) {
        int ix = 0;
        for (int i = 0; i < 5; i++) {
            if (input.charAt(i) == '#') {
                ix += Math.pow(2, i);
            }
        }
        return ix;
    }

    private static final String TEST_INPUT = "initial state: #..#.#..##......###...###\n"
                                             + "\n"
                                             + "...## => #\n"
                                             + "..#.. => #\n"
                                             + ".#... => #\n"
                                             + ".#.#. => #\n"
                                             + ".#.## => #\n"
                                             + ".##.. => #\n"
                                             + ".#### => #\n"
                                             + "#.#.# => #\n"
                                             + "#.### => #\n"
                                             + "##.#. => #\n"
                                             + "##.## => #\n"
                                             + "###.. => #\n"
                                             + "###.# => #\n"
                                             + "####. => #";

    private static final String INPUT = "initial state: ######....##.###.#..#####...#.#.....#..#.#.##......###.#..##..#..##..#.##..#####.#.......#.....##..\n"
                                        + "\n"
                                        + "...## => #\n"
                                        + "###.. => .\n"
                                        + "#.#.# => .\n"
                                        + "##### => .\n"
                                        + "....# => .\n"
                                        + "##.## => .\n"
                                        + "##.#. => #\n"
                                        + "##... => #\n"
                                        + "#..#. => #\n"
                                        + "#.#.. => .\n"
                                        + "#.##. => .\n"
                                        + "..... => .\n"
                                        + "##..# => .\n"
                                        + "#..## => .\n"
                                        + ".##.# => #\n"
                                        + "..### => #\n"
                                        + "..#.# => #\n"
                                        + ".#### => #\n"
                                        + ".##.. => .\n"
                                        + ".#..# => #\n"
                                        + "..##. => .\n"
                                        + "#.... => .\n"
                                        + "#...# => .\n"
                                        + ".###. => .\n"
                                        + "..#.. => .\n"
                                        + "####. => #\n"
                                        + ".#.## => .\n"
                                        + "###.# => .\n"
                                        + "#.### => #\n"
                                        + ".#... => #\n"
                                        + ".#.#. => .\n"
                                        + "...#. => .";
}
