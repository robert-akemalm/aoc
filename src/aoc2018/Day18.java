package aoc2018;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Day18 {
    public static void main(String[] args) {
        String input = INPUT;
        a(input);
        b(input);
    }

    private static void b(String input) {
        String[] lines = Util.parseStrings(input);
        State[][] states = new State[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            states[i] = lines[i].chars().mapToObj(State::parse).toArray(State[]::new);
        }

        Map<String, Integer> stateCache = new HashMap<>();
        StateCounter stateCounter = new StateCounter();
        for (int round = 0; round < 1_000_000_000; round++) {
            String hash = toString(states);
            Integer previousRound = stateCache.get(hash);
            if (previousRound != null) {
                int diff = round - previousRound;
                while (round < 1_000_000_000) {
                    round += diff;
                }
                round -= diff;
            }
            stateCache.put(hash, round);
            states = run(states, stateCounter);
        }

        stateCounter.reset();
        for (int i = 0; i < states.length; i++) {
            for (int j = 0; j < states[i].length; j++) {
                stateCounter.add(states[i][j]);
            }
        }
        int sum = stateCounter.get(State.LUMBERYARD) * stateCounter.get(State.TREE);
        System.out.println(sum);

    }

    private static void a(String input) {
        String[] lines = Util.parseStrings(input);
        State[][] states = new State[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            states[i] = lines[i].chars().mapToObj(State::parse).toArray(State[]::new);
        }

        StateCounter stateCounter = new StateCounter();
        for (int round = 0; round < 10; round++) {
            states = run(states, stateCounter);
        }

        stateCounter.reset();
        for (int i = 0; i < states.length; i++) {
            for (int j = 0; j < states[i].length; j++) {
                stateCounter.add(states[i][j]);
            }
        }
        int sum = stateCounter.get(State.LUMBERYARD) * stateCounter.get(State.TREE);
        System.out.println(sum);
    }

    static State[][] run(State[][] states, StateCounter stateCounter) {
        State[][] newStates = new State[states.length][];
        for (int i = 0; i < states.length; i++) {
            newStates[i] = new State[states[i].length];
            for (int j = 0; j < states[i].length; j++) {
                stateCounter.reset();
                stateCounter.count(states, i, j);
                int trees = stateCounter.get(State.TREE);
                int grounds = stateCounter.get(State.GROUND);
                int lumberyards = stateCounter.get(State.LUMBERYARD);
                State nextState = states[i][j];
                switch (states[i][j]) {
                case GROUND:
                    if (trees >=3) {
                        nextState = State.TREE;
                    }
                    break;
                case TREE:
                    if (lumberyards >=3) {
                        nextState = State.LUMBERYARD;
                    }
                    break;
                case LUMBERYARD:
                    if (trees == 0 || lumberyards == 0) {
                        nextState = State.GROUND;
                    }
                    break;
                }
                newStates[i][j] = nextState;
            }
        }
        return newStates;
    }

    static String toString(State[][] states) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < states.length; i++) {
            for (int j = 0; j < states[i].length; j++) {
                sb.append(states[i][j].c);
            }
        }
        return sb.toString();
    }
    static class StateCounter {
        final int[] counts = new int[State.values().length];
        void reset() {
            Arrays.fill(counts, 0);
        }

        void add(State state) {
            counts[state.ordinal()]++;
        }

        int get(State state) {
            return counts[state.ordinal()];
        }

        void count(State[][] states, int x, int y) {
            for (int i = x - 1; i <= x + 1 ; i++) {
                if (i < 0 || i >= states.length) {
                    continue;
                }
                for (int j = y - 1; j <= y + 1 ; j++) {
                    if (j < 0 || j >= states[i].length || (i == x && j == y) ) {
                        continue;
                    }
                    add(states[i][j]);
                }
            }
        }
    }
    enum State {
        GROUND('.'), TREE('|'), LUMBERYARD('#');

        private final char c;

        State(char c) {
            this.c = c;
        }

        static State parse(int i) {
            char c = (char) i;
            for (State state : values()) {
                if (c == state.c) {
                    return state;
                }
            }
            throw new RuntimeException();
        }
    }

    private static final String TEST_INPUT = ".#.#...|#.\n"
                                             + ".....#|##|\n"
                                             + ".|..|...#.\n"
                                             + "..|#.....#\n"
                                             + "#.#|||#|#|\n"
                                             + "...#.||...\n"
                                             + ".|....|...\n"
                                             + "||...#|.#|\n"
                                             + "|.||||..|.\n"
                                             + "...#.|..|.";

    private static final String INPUT = "#|#..||.||....|.#..|#.|#......|.#....#..|...|#.#.#\n"
                                        + "#|...#..#.|..#..#...|..##.|....#|....#|.|..#......\n"
                                        + "......|.#.|.##.|.|.#..#.|...#...##..||...|..##|.#.\n"
                                        + ".###..#.#|..#.#.|...#|||.#|..#..|.|..#....#...#..|\n"
                                        + ".#|.|...|.#.##|.##....#..|...|...|.....#..#|...|.#\n"
                                        + "....||.#|.#|#..#.#|.....|.#..#.||#||.#....||#.....\n"
                                        + "#.#.#.|.|.|.|#.#.##|.........#.##.#|..||.##.......\n"
                                        + ".|.#.#...##.#.||..#|..|##...##...#.|..||.#.||....#\n"
                                        + ".||.|.#.#....|#|#..#|#.#..#||#..||.|....#|..|.....\n"
                                        + "##|.||.#...|.||..|..|.|...##.|||...|....#..|.|..|.\n"
                                        + ".##.|...|..|.....|#.|#.|#..|#....##|......|#.##...\n"
                                        + "|.###||...||..##...#.#||#.#.....|#..|#|..|.|.#.#..\n"
                                        + "..|#..#..|..#|||..||#.|#.......|..||......|##.....\n"
                                        + "##||...#.#...........#|###|...|..#|.....|.|..###..\n"
                                        + ".#.#...#|.|.|...|....#|.|#.#..#....##|..||..|.....\n"
                                        + "...#||...|##.#....|||....##|.|#|...|...#.||...|.##\n"
                                        + "...#.||...|.|.#...#.....#.|##|..|..#.|.#...|..|.#.\n"
                                        + "...|.#.###.|.||#|..#.|.#.#.........#...|...#.#|#|.\n"
                                        + "|..|....||.#...|#.#|.#.|.....#.###.|##.|....|.#|..\n"
                                        + "..#..#|.|.|.||##....#..||||...|.......#.#.......#.\n"
                                        + "..|....|.....#....|....#.|.#|#..||##.##......|.|#.\n"
                                        + "#..|.|..#.|||###|..|.#.#|.#...|...||........#..|#.\n"
                                        + "..#|...#..#....#....#.##...#...#.|...|......#..||.\n"
                                        + "#|#|...#...|...|..#|...|..#..|.#||..#.||.....|..#|\n"
                                        + ".||...#.|..|#||||...|..|##..|.#..|#|.##.....|#.||.\n"
                                        + "#|#.#...|...|......##|..#.|#.|.|....|.....|.#....#\n"
                                        + "...|....#..#.#|##..#..#||.....|.#|.#|#.||.||.#..#.\n"
                                        + "........#..##.......#...#..##......#.......#.#..|.\n"
                                        + ".|.|.#.||......#..||..|...##...#..#|..|.#....#|#..\n"
                                        + "##...|##....|||.##||..|....##.....|##||..|....#..#\n"
                                        + ".###.||..|..|..#||#.|#..|..#||#.#.#.##...|....#|#.\n"
                                        + "..##|.|..|.||...##|#||.........##|.....|#.|#..||..\n"
                                        + "....#...#|##.|.|.....#|...|..|....|||..|......#...\n"
                                        + "........|......|.|.....#.|......#......||....#.#.|\n"
                                        + ".....#|#..|#.||.|...#|#.....||#..|.#..|..|.#.||.#.\n"
                                        + ".||..|.##..#|...#....|..|..|.....||#......#|..|...\n"
                                        + "###.|.##..|.|##.|.||..##..#..|..#.#|.|#...##|##...\n"
                                        + "..##|..#..###..#.#......|.......|#.#.......|..||#|\n"
                                        + "#..|..|..#....|#|.......#||..#|........#..#.||.|.#\n"
                                        + "...#.|.#.......#|.....|................|..|#..#.|#\n"
                                        + "..|..#....|....|.|....#|#..#..#.###.#....#|..|.##.\n"
                                        + "#.|....#...|.|..|..||....||.#..#|#..#..###||.|#.#|\n"
                                        + ".|......|#.#|.|..#....|....#.........###..||.#.#|.\n"
                                        + "....###.#|.....#|...|.#.|..#...........#.......#||\n"
                                        + ".#|...|....#.#||..|..##|....|#.||..#.#...#.##||.#.\n"
                                        + "...|.||#.#...||.#|...||#.|||||..|..#..#|...#....#.\n"
                                        + ".##|||##.#|.|.#..#.....#.......|..|.|...|.|....#|.\n"
                                        + "#.#|....#.|..|.|.#..|.......#.....###.#..|......#|\n"
                                        + ".#.......|..#.#.#.|...#|.|...||#.||#.|........#.|.\n"
                                        + "..|.|##||.###.....|.|#.#|.||......#.##...|.#.#.#..";
}
