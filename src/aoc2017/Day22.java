package aoc2017;

public class Day22 {
    public static void main(String[] args) throws Exception {
        part1();
        part2();
    }

    private static void part1() throws Exception {
        boolean[][] matrix = new boolean[10_000][];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = new boolean[matrix.length];
        }

        boolean[][] data = parse(DATA);
        int y = matrix.length / 2 - data.length / 2;
        int x = matrix[0].length / 2 - data[0].length / 2;


        for (int row = 0; row < data.length ; row++) {
            for (int col = 0; col < data[0].length ; col++) {
                matrix[y + row][x + col] = data[row][col];
            }
        }

        y = y + (data.length / 2);
        x = x + (data[0].length / 2);
        int infections = 0;
        int dir = 0;

        for (int i = 0; i < 10000; i++) {
            if (matrix[y][x]) {
                matrix[y][x] = false;
                //turn right
                dir++;
                if (dir > 3) {
                    dir -= 4;
                }
            } else {
                infections++;
                matrix[y][x] = true;
                //turn left
                dir--;
                if (dir < 0) {
                    dir += 4;
                }
            }

            if (dir == 0) { //N
                y--;
            } else if (dir == 1) { // E
                x++;
            } else if (dir == 2) { // S
                y++;
            } else if (dir == 3) { // W
                x--;
            }
        }
        System.out.println(infections);
    }

    private static boolean[][] parse(String data) {
        String[] lines = data.split("\n");
        boolean[][] matrix = new boolean[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            matrix[i] = new boolean[lines[i].length()];
            for (int c = 0; c < lines[i].length(); c++) {
                if (lines[i].charAt(c) == '#') {
                    matrix[i][c] = true;
                }
            }
        }
        return matrix;
    }

    private static void part2() throws Exception {
        State[][] matrix = new State[10_000][];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = new State[matrix.length];
        }

        boolean[][] data = parse(DATA);
        int y = matrix.length / 2 - data.length / 2;
        int x = matrix[0].length / 2 - data[0].length / 2;


        for (int row = 0; row < data.length ; row++) {
            for (int col = 0; col < data[0].length ; col++) {
                if (data[row][col]) {
                    matrix[y + row][x + col] = State.INFECTED;
                }
            }
        }

        y = y + (data.length / 2);
        x = x + (data[0].length / 2);
        int infections = 0;
        int dir = 0;

        for (int i = 0; i < 10_000_000; i++) {
            if (matrix[y][x] == State.INFECTED) {
                // If it is infected, it turns right.
                matrix[y][x] = State.FLAGGED;
                dir++;
                if (dir > 3) {
                    dir -= 4;
                }
            } else if (matrix[y][x] == null) {
                // If it is clean, it turns left.
                matrix[y][x] = State.WEAKENED;
                dir--;
                if (dir < 0) {
                    dir += 4;
                }
            } else if (matrix[y][x] == State.FLAGGED) {
                // If it is flagged, it reverses direction, and will go back the way it came.
                matrix[y][x] = null;
                dir -= 2;
                if (dir < 0) {
                    dir += 4;
                }
            } else if (matrix[y][x] == State.WEAKENED) {
                // If it is weakened, it does not turn, and will continue moving in the same direction.
                infections++;
                matrix[y][x] = State.INFECTED;
            }

            if (dir == 0) { //N
                y--;
            } else if (dir == 1) { // E
                x++;
            } else if (dir == 2) { // S
                y++;
            } else if (dir == 3) { // W
                x--;
            }
        }
        System.out.println(infections);
    }

    enum State {
        WEAKENED {
            State change() {
                return INFECTED;
            }
        },
        INFECTED {
            State change() {
                return FLAGGED;
            }
        },
        FLAGGED {
            State change() {
                return null;
            }
        };

        abstract State change();
    }
    private static final String DATA2 = ""
                                        + "..#\n"
                                        + "#..\n"
                                        + "...";

    private static final String DATA = ""
                                       + "#.#...###.#.##.#....##.##\n"
                                       + "..####.#.######....#....#\n"
                                       + "###..###.#.###.##.##..#.#\n"
                                       + "...##.....##.###.##.###..\n"
                                       + "....#...##.##..#....###..\n"
                                       + "##.#..###.#.###......####\n"
                                       + "#.#.###...###..#.#.#.#.#.\n"
                                       + "###...##..##..#..##......\n"
                                       + "##.#.####.#..###....#.###\n"
                                       + ".....#..###....######..##\n"
                                       + ".##.#.###....#..#####..#.\n"
                                       + "########...##.##....##..#\n"
                                       + ".#.###.##.#..#..#.#..##..\n"
                                       + ".#.##.##....##....##.#.#.\n"
                                       + "..#.#.##.#..##..##.#..#.#\n"
                                       + ".####..#..#.###..#..#..#.\n"
                                       + "#.#.##......##..#.....###\n"
                                       + "...####...#.#.##.....####\n"
                                       + "#..##..##..#.####.#.#..#.\n"
                                       + "#...###.##..###..#..#....\n"
                                       + "#..#....##.##.....###..##\n"
                                       + "#..##...#...##...####..#.\n"
                                       + "#.###..#.#####.#..#..###.\n"
                                       + "###.#...#.##..#..#...##.#\n"
                                       + ".#...#..#.#.#.##.####....";
}
