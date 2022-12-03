package aoc2020;

import java.util.Arrays;

public class Day11 {
    public static void main(String[] args) {
//        String input = TEST_INPUT;
        String input = INPUT;
        a(input);
        b(input);
    }

    private enum State {
        FLOOR('.'),
        SEAT('L'),
        OCCUPIED('#');

        private final char c;

        State(char c) {
            this.c = c;
        }

        static State parse(int c) {
            return Arrays.stream(values()).filter(s -> s.c == c).findFirst().orElseThrow(RuntimeException::new);
        }
    }

    private static void b(String input) {
    }

    private static void a(String input) {
        State[][] room = input.lines().map(r -> r.chars().mapToObj(State::parse).toArray(State[]::new)).toArray(State[][]::new);
        while (true) {
            State[][] step = step(room);
            boolean changed = false;
            for (int row = 0; row < room.length; row++) {
                for (int col = 0; col < room[row].length; col++) {
                    if (room[row][col] != step[row][col]) {
                        changed = true;
                        break;
                    }
                }
            }
            if (!changed) {
                break;
            }
            room = step;
        }

        int cnt = 0;
        for (int row = 0; row < room.length; row++) {
            for (int col = 0; col < room[row].length; col++) {
                if (room[row][col] == State.OCCUPIED) {
                    cnt++;
                }
            }
        }
        System.out.println(cnt);
    }

    private static State[][] step(State[][] current) {
        State[][] next = new State[current.length][];
        for (int row = 0; row < current.length; row++) {
            next[row] = new State[current[row].length];
            for (int col = 0; col < next[row].length; col++) {
                next[row][col] = newStateB(current, row, col);
            }
        }
        return next;
    }

    private static State newState(State[][] room, int row, int col) {
        int adjacentOccupied = 0;

        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                if (r == row && c == col) {
                    continue;
                }
                if (get(room, r, c) == State.OCCUPIED) {
                    adjacentOccupied++;
                }
            }
        }

        State state = get(room, row, col);
        if (state == State.SEAT && adjacentOccupied == 0) {
            return State.OCCUPIED;
        } else if (state == State.OCCUPIED && adjacentOccupied >= 4) {
            return State.SEAT;
        }
        return state;
    }

    private static State nextSeat (State[][] room, int row, int col, int dRow, int dCol) {
        if (row+dRow < 0 || row+dRow >= room.length || col+dCol < 0 || col+dCol >= room[row].length) {
            return null;
        }
        State state = get(room, row + dRow, col + dCol);
        return state == State.FLOOR ? nextSeat(room, row+dRow, col+dCol, dRow, dCol) : state;
    }

    private static State newStateB(State[][] room, int row, int col) {
        int adjacentOccupied = 0;
        if (nextSeat(room, row, col, -1, -1) == State.OCCUPIED) {
            adjacentOccupied++;
        }
        if (nextSeat(room, row, col, -1, 0) == State.OCCUPIED) {
            adjacentOccupied++;
        }
        if (nextSeat(room, row, col, -1, 1) == State.OCCUPIED) {
            adjacentOccupied++;
        }
        if (nextSeat(room, row, col, 0, -1) == State.OCCUPIED) {
            adjacentOccupied++;
        }
        if (nextSeat(room, row, col, 0, 1) == State.OCCUPIED) {
            adjacentOccupied++;
        }
        if (nextSeat(room, row, col, 1, -1) == State.OCCUPIED) {
            adjacentOccupied++;
        }
        if (nextSeat(room, row, col, 1, 0) == State.OCCUPIED) {
            adjacentOccupied++;
        }
        if (nextSeat(room, row, col, 1, 1) == State.OCCUPIED) {
            adjacentOccupied++;
        }

        State state = get(room, row, col);
        if (state == State.SEAT && adjacentOccupied == 0) {
            return State.OCCUPIED;
        } else if (state == State.OCCUPIED && adjacentOccupied >= 5) {
            return State.SEAT;
        }
        return state;
    }

    private static State get(State[][] room, int row, int col) {
        if (row < 0 || row >= room.length || col < 0 || col >= room[row].length) {
            return State.FLOOR;
        }
        return room[row][col];
    }

    private static final String TEST_INPUT = "L.LL.LL.LL\n"
                                             + "LLLLLLL.LL\n"
                                             + "L.L.L..L..\n"
                                             + "LLLL.LL.LL\n"
                                             + "L.LL.LL.LL\n"
                                             + "L.LLLLL.LL\n"
                                             + "..L.L.....\n"
                                             + "LLLLLLLLLL\n"
                                             + "L.LLLLLL.L\n"
                                             + "L.LLLLL.LL";

    private static final String INPUT = "LLLLLLLL.LLLL.LLLLL.LLLLLLLLLLLLLL.LLLLLLLLLLLLLL.LLLLL.LLLLLLLLLLLLLL.LLLLLLL.LLLLLL.LLLLLLLLLL.L\n"
                                        + "LLLLLLLLLLLLLLLLLLL.L.LL.LLL.LLLLL.LLLLLLLLLLL.LL.LLLLL.LLLLLLLL.LLLLLLLLL.LLL.LLLLLLLLLLLLLLLLLLL\n"
                                        + "LLLLLLLLLLLLL.LLLLLLLLLLLLLL.LL.LL.LLLLLLL.LLLLLL.LLLLLLLLL.LLLL.LLLLL.LLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLLLLL.LLLL.LLLLL..LLLLLLL.L.LLL.LLLLLLL.LLLLLL.LLLLL.LLLLLLLL.LLLLLLLLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLLL.L.LLLL.L.LLL.LLLLLLLLLLLLLL.LLL.LLL.LLLLLL.LLLLL.LLLLLLLLLLLLLL.LLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLLLLL.LLLL.LLLLLLLLLLLLLL.LLLLL.LLLLLLL.LLLLLLLLLLLLLLLLLLLLL.LLLLL.LLLLL.L.LLLLLLLLLLLLLLLLLLL\n"
                                        + "L...LLL...L....L......L..L....LL...L.LLL...............L..LL.LLL.LL..L..........L......L.L....L.LL\n"
                                        + "LLLLLLLL.LLLL.LLLLL.LLLLLLLL.LLLLL.L.LLLL.LLLLLLL.LLLLLLLLLLLLLL.LLLLLLLLLLLLLL.LLLLLLL.LLLLLLLLLL\n"
                                        + "LLLLLLLL.LLLL.LLLLL.LLLLLL.L.LLLLL.LLL.LLL.LLLLLL.LLLLL.LLLL.LLL.LLLLL.LLLLLLLLLLLL.L.LLLLLLLLL.LL\n"
                                        + "LLLLLLLL.LLLL.L.LLL.LLLLLLLLLLLL.L.LLLLLLLLLL.LLLLLLLLL.LLLLLLLL..L.LL.LLLLLLLLLLLLLL..L.LLLLLLLLL\n"
                                        + "LLLLLLLL.LLLL.LLLLLLLLLLLLLL.LLLLL.LLLLLLL.LLLLLL.LLLL.LLLLLLLLLLLLLLL..LLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "LL.LLLLLLLLLL.LLLLLLLLLLLLLL.LLLLL.LLLLLLLLLLLLLL.LLLLLLLLLLLLLLLLLLLL.LLLLLLL.LLLLLL.LLLLLLLLLLL.\n"
                                        + "LLLLLLLLLLLLL.LLLLL.LLLLLLLLLLLLLL.LLL.LLLLLLLLLL.LLLLLLLLLLLLLL.LLLLL.LLL.LLLLLLLL.L.LLLL.LLLLLLL\n"
                                        + "L.LL.....L...LLL..L..L..L...L..L..L.LLLL...L...LLL...L.L...LL.L.....L.....L.......L..L.L....L..L..\n"
                                        + "LLLLLLLLLLLLL.LLLLLLLLLLLLLL.LLLLL.LLLLLLL.LLLLLL.L.LLL.L.LLLLLLLLLLLL.LLLLLLLLLLLLLL.LLLLL.LLLLLL\n"
                                        + "LLLLLLLL.LLLL.LLLLL.LLLLLLLL.LLLLLLLLLLLLL.LLLL.L.LLLLL.LLLLLLLL.LLLLL.LLLLLLL.LLLLLLLLLLLLLLLLLLL\n"
                                        + "LLLLLLLLLLLLLLL.LLL.LLLLLLLL.LLLLL.LLLLLLLLLLLLLLLLLLLL.LLLLLL.LLLLLLLLLLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "LL.LLLLLLLLLLLLLLLL.LLLLL.LLLLLLLL.LLLLLLL.LLLLLL.LLLLL.LLLLLLLL.LLLLLLLLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLLLLLLLLLL.LLLLL.LLLLLLLL.LLLLLLLLLLLLL.LLLLLLLLLLLL.LLLLLLLL.LLLLL.LLL.LLL.L.LLLL.LLLLLLLLLLLL\n"
                                        + "L...L.LLL..L..L....LL.....L.LL...L......L...L...LL.LLL....LL........L.L.....LL..L...L..LL.LL.L.LL.\n"
                                        + "LLLLLLLLLLLLLLLLLLL.LLLLL.LL.LLLLL.LLLLLLLLLLLLLL.L.LLL.LLLLLLLL.LLLLL.LLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLLLLLLLLLL.LLLLL.LLLLLLLLLLLLLLLLLLLLLL.LLLLLL.LLL.L.LLLLLLLL.LLLLL.LLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLLLLL.L.LLL.LLLL.LLLLLLLL.LL.LL.LLLLLLL.LLLLLL.LLLLL.LLLLLLLL.LLLLLLL.LLLLL.LLLLLLLLLLLLLLLLLLL\n"
                                        + "LLLLLLLLLLLLLLLLLLLLLLLLLLL..LLLLL.LLLLLLLLLLLLLL.LLLLL.LLLLLLLLLLLLLL.LLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + ".L..LL.L.LLLL.LLLLLLLLLLLLLL.LLL.LLLLLLLLL.LLL.LL.LLLLLLLLLLLLLL.LLLLL.LLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "....L..........L.L.L......LL.L....L.LLL...L.L.L.L..L.....L........L...L...L.L.......LL...L.L..LLL.\n"
                                        + "LLLLLLLL.LLLL.LLLLL.LLLLLLLL.LL.LL.LLLLLLL.LLLLLLLLLLLL.LLLLLLLL.LLLLL.LLLLLLL.LLLLLLLLLLLLLLLLLLL\n"
                                        + "LLLLLLLLLLLLL.LLLL..LLLL.LLL.LLLLL.LLLLLLL.LLLLLLLLLLLL.LLLLLLLL.LLLLL.LLLLLL..L.LLL.LLLLLLLLLLLLL\n"
                                        + "LLLLLLLL.LLLL.LLLLL.LLLLLLLLLLL.LL.LLLLLLL.LLLLLLLLLLLLL.LLLLLLL.LLLLL.LLLLLLL.LLLLLLLLLLLLLLLLLL.\n"
                                        + "LLLLLLLLLLLLL.LLLLLLLLLLLLLL.LLLLLLLLLLLLL.LLLLLLL.LLLLLLLLLLLLLLLLLLL.LLLLLLLLLL.LLL.LLLLLLLLLLLL\n"
                                        + "LLLLLLLLLLLLL.LLLLL.LLLLLLLLLLLLL..LLLLLLL.LLLLLL.LLLLL.LLLLLLLL.LLLLL.LLLLLLLLLLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLLLLL.LLLL.LLLLL.LLLLLLLL.LLLLL.LLL.LLLLLLLLLL.LLLLL.LLLLLLLL.LLLLL.LLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "L.LLLLLL.LLLL.LL.LL.LLLLLLLL.LLLLL.LLLLLLLLLLLLLLL.LLLL.LLLLLLLL.LLLLL.LLLLLLLLLLLLLLLL.LLLLLLLLLL\n"
                                        + ".LL.LL...L.L...L..LL..L........L..L...........L.L..L...L.L...L..LL..L..L......L.L.....L..L......L.\n"
                                        + "LLLLLLLL.LLLLLLLLLL.LLLLLLLL.LLLLL.LLLLLLL.L.LLLLLLLLLL.LLLLLLLLLLLLLL.LLL.LLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLLLLL.LLLLL.LLLL.LLLLLLLLLLLLLLLLLLLLLLLLL.LLL.LL.LL.LLLLLLLL.L.LLL.LLLLLLLLLLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLLLLL.LLLL.LL.LL.LLLLLLLL.LLLLLLLLLLLLL.LLLLLLLLLLLL.LLLLLLLL.LLLLL.L.LLLLL.LLLLLL.LLLLLLLLLLL.\n"
                                        + "LLLLLLLLLLLLLLLLLLLLLLLLLLLL.LLLLL.LLLLLLL.LLLLLL.LLLLLL.LLLLLLL.LLLLL.LLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "LL.LLLLL.LLLLLLLLLLLLLLLLLLLLLLL.L.LLLLLLLLLLLLLL.LLLLLLLLLLLLLL.LLLLLLLLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLL.LLL.LLLL.LLLLL.LLLLLLLL.LLLLLLLLLL.LLLLLLLLL.LLLLLLLLLLLLLLLLLLLL.LLLLLLL.LLLLLLLLL.LLLLLLLLL\n"
                                        + "..L.L.L.L.LL..LL....L....L..L.L.....L...LL.....L.LLL.....L.L.LLLL.L....L.L.....L..L...L.L.LL.L....\n"
                                        + "LLLLLLLL.LLLLLL.LLLLLLLLLLL..LLLLL.LLLLLLL.LLLLLLLLLLLL.LLLLL.LL.LLLLL.LLLLLLL.LLLLLL.LLL.LLLLLLLL\n"
                                        + "LLLLLLLL.LLLL.LLLLL.LLLLLLLLLLLLLL.LLLLLLL.LLLLLLLLLLLLLLLLLLLLL.LLL.L.LLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLLLLL.LLLL..LLLL.LLLLLLLL.LLLLL.LLLLLLL.LLLLLLLLLLLL.LLLLLLLLLLLLLL.LLLLLLLL.LLLLLLLLLLLLLLLLLL\n"
                                        + "LLLLLLLL.LLLL.LLLLL.LLLLLLLL.LLLLL.LLLLLLL.LLLLLL.LLLLL.LLLLLLLLLLLL.L.LL.LLLL.LLLLLLLLLLLLLLLLLLL\n"
                                        + "LLLLLLLLLLLLL.LLLLLLLLLLLLLL.LLLLL.LLLLLLL.L.LLLL.LLLLLLLLLLLLLL.LLLLLLLLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLLLLLLLLLL.LLLL.LLLLLLLLLLLLLLL.LLLLLLLLLLLLLL.LLLLLLLLLLLLLL.LLLLLLLLLLLLL...LL.L.LLLLLLLLL.LL\n"
                                        + "LLLLLLLL.LLLL.LLLLLLLLLLLLLL.LLLLL..L.LLLL.LLLLLL.LL.LL.LLLLLLLLLLLLLLLLLL.LLL.LLLLLLLLLLLLLLLLLLL\n"
                                        + "LLLLLLLL.L.LL.LLLLL.LLLLLLLL.LLLLLLL.LLLLLLLL.L.L.LLLLL..LLLLLLL.LLLLL.LLL.LLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + ".....L.L.LL.......L..LLL...LL....L.L.L....L..L.L.L.LL...L..L....L....LL.L.L....L....LL..L.L....L..\n"
                                        + "LLLLLLLL.LLLLLLLLLL.LLLLLLLL.LLLLL.LLLLLLLLLLLLLL.LLLLLLLLLLLLLL.LLLLL.LLLLLLL.LLLLLLLLLLLLLLLLLLL\n"
                                        + "LLLLLLLL.LL.L.LLLL.LLLLLLLLL.LLLLL.LLLLLLLLLLLLLL.LLLLL.LLLLLLLLLLLLLLLLLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + ".LLLLLLL.LLLLLLLLLL.LLLLLL.L.LLLLL.LLLLLLL.LLLLLL.LLLLL.LLLLLLLL.LLLLL.LLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLLLLL.LLLL.LLLLL.LLLLLLLLLLLLLL.LLLLLLL.LLLLLL.LLLLL.LLL.LLLL.LLLLL.L.LLLLL.LLLLLL.LLLLLLLLLL.L\n"
                                        + "LLLLLLLLLLLLL.LLLLL.LLLLLLLL.LLLLL.LLLLLLLLLLLLLL.LLLLLLLLLLLLLL.LLLLLLLLLLLLL.LLLL.LLLLLLLLLLLLLL\n"
                                        + "LLLLLLLL.LLL..LLLLL.LLLLLLLL.LLLLLLLLLLLLL.LLLLLL.LLLLL.LLL.LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL\n"
                                        + "..L....L..L..L..L....L.......L.....L.LL.L.L...LL.L.....LL.L...LL.LL.L...L.L....LL..L.....LL.L..L..\n"
                                        + "LLLLLLLLLLLLL.LLLLLLLLLLLLLLLLLLL..LLLLLLL.LLLLLL.LLLLL.LLLLLLLL.LLLLLLLLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLL.LL.LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL.LLL.LL.LLLLL.LLLL.LLLLLLLLL.LLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLLLLL.LLLL.L.LLL.L.LLLLLLLLLLLL.LLLLLLLLLLLLLL.LLLLLLLLLLLLLL.LLLLLLLLLLLLLLLLLLLL.LLLLLL.LLLLL\n"
                                        + "LLLLLLLL.LL.LLLLLLL.L.LLLLLL.LLLLL.LLLLLLLLLLLLLL.LLLLLLLLLLLLLL.LLLLLLLLLLLLL.LLLLLL.LLLL.LLLLLL.\n"
                                        + "LLLLLLLL.LLLL.LLLLLLLLLLLLLLLLLLLL.LLLLLLLLLLLLLL.LLLLLLLLLLLLLL.LLLLLLLLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "..L....LL.L.L..L.L.L.LL..L.L.....L.......LLLL...L.....L...LL..L..L..L....L.L.LL...L..L...L......L.\n"
                                        + "LLLLLLLL.LLLL.LLLLL.LLLL.LLLLLLLLL.LLLLLLLLLLLLLL.LLLLL.LLLLLLLL.LLL.LLLLLLLLL.LLL.LLLLLLLLLLLLLLL\n"
                                        + "LLLLLLLLLLLLL.LL.LL.LLLLLLLL.LLLLL.LL.LLLL.LLL.LL.LLLLL.LLLLLLLL.LLLLL.LLLLLLL.LLLLLLLLLLLLLLLLLLL\n"
                                        + "LLLLL..L.LLLL.LLLLLLLLLLLLLL.LLLLL.LLLLLLL.LLLLLLLLLLLL.LLLLLLLLLLLLLLLLLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLLLLL.LLLL.LLLL.LLLLLLLLL.LLLLL.LLLLLLL.LL.LLL.LLLLLLLLLLLLLL.LLLLL.L.LLLLL.LLLLLL.LL.LLLLL.LLL\n"
                                        + "LLLLLLLL.LLLLLLLLLL.LLLLLLLLLLLLLL.LLLLLLLLLLLLLL.LLLLLLLLLLLLLL.LLLLL.LLLLLLL.LLLLLLLLLLLL.LLLLLL\n"
                                        + "LLLLLLLL.LLLL.LLLLL.LLLLLLLL.LLLLL.LLLLLLLLLLLLLL.LLLLL.LLLL.LLL.LLLLLLLLLLLLLLLLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLLLLL.LLLL.LLLLLLLLLL.LLLLLLLLLLLLLLLLL.LLLLLL.LLLLLLLLLLLLLLLLLLLL.LLLLLLL.LLLLLLL.LLLLLLLL.LL\n"
                                        + "LLLLLLLL.LLLL.LLLLL.LLLLLL.L.LLLLL.LL.LL..LLLLLLL.LLLLL.LLLLLLLL.LLLLL.LLLLLLL.LLLLLLLLLLLLLLLLLLL\n"
                                        + "LLLLLLLL.LLLL..LLLLLLLLLLLLL.LLLLL.LLLLLLL.LLLLLLLLLLLL.LLLLLL.L.LLLLLLLLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "........LLLL.L.LL...L..L.L...LLLL.L.L.L.L....L...L..L.L.LLL....LL.L.....L...LLL.....L.LL..L......L\n"
                                        + "LLLLLLLL.LLLLLLLLLLLLLLLLLLL.LLLL..LLLLLLLLLLLLLLLL.LLLLLLLLLLLL.LLLLL.LLLLLLL.LLLLLL.LLLL.LLLLLLL\n"
                                        + "LLLLLLLL.LLLL.LLLLL.LLLLLLLL.LLLLLLLLLLLL.LLLLLLL.LLLLL.LLLLLLLL.LLLLLLLLLLLLLLLLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL.LLLLLLLLL..LLLLLLLLLLLLLLLLLLLLLLLL.LLLLLLL.LLL.LL.LLLLLLLLLLLL\n"
                                        + "LLLLLLLLLLLLLLLLLLL.LLLLLLLL.LLLLL.LLLLLLL.LLLLLL.LL.LL.LLLLLLLLLLLLLL.LLLLL.L.LLLLLLLLLLLLLLLL.LL\n"
                                        + "LLLLLLLL.LLLL.LLLLL.LLLL.LLL.LLLLL.LLLLLLL.LLLLLL.LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL.LLLLLLLLLLL.\n"
                                        + "LLLLLLLLLLLLL.LLLLL.LLLLLLLLLLLLLL.LLLLLLL.LLLLLL.LLLLL.LLLL.LLL.LLLLL.LLLLLLL.LLLLLLLLLLLLLLLLL.L\n"
                                        + "LLLLLLLL.LLLL.LLLLL.LLLLLLLL.LLLL..LLLLLLL.LLLLLL.LL.LL.L.LLLLLL.LLLL..LLLLLL..LLLLLLLLLLLLLLLLLLL\n"
                                        + "LLLLLLLLLLLLLLLLLLL.LLLLLLLL.LLLLLLLLLLLLL.LLLLLLLLLLLL.LLLLLLLL.LLLLL.LLLLLLL.LLLLLLLLLLLLLLLLLLL\n"
                                        + "L......LLLLL..LL.L.....L......L..L....L..LLL.L....LLLLL....L..L...LL.L..L...L...L......LL.........\n"
                                        + "LLLLLLLL.LLLL.LLLLL.LLLLLLLLLLLLLLLLLLLLLL.LLLLLL.LLLLL.LLLLLLLL.LLLLL.LLLLLLL.LLLLLLLLLLLLLLLLLLL\n"
                                        + "LLLLLLLL.LLLL.LLLLL.LLLLLLL..LLLLL.LLLLLLL.LLLLLL.LLLLL.LLLLLLLL.L.LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL\n"
                                        + ".LLLLLLLLLLLL.LLLLLLLLLLLLLL.LLLLL.LLLLLLL.LLLLLL.LLLL.LLLLLLLLL.LLLLL.LLLLLLL.LLLLLLLLLLLLLLLLLLL\n"
                                        + "LLLLLLLL.LLLLLLLLLLLLLLLLLLLLLLLLL.LLLLLLLLLLLLLLLLLLLL.LLLLLLLLLLLLLL.LLLLL.L.LLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLLLLL.LLLL.LLLLL.LLLLLLLL.LLLLL.LLLLLLL.LLLLLL.LLLLL.LLLLLLLL.LLLLLLLLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLLLLLLLLL..LLLLL.LL.LLL.LLLLL.LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL.LLLLL.LLLLLLL.LLLLLL.LLLLLLLLLLL.\n"
                                        + "LLLLLLLL.LLLL.LLLLL.LLLLLLLL.LLLLL.LLLLLLLLLLLLLL.LLLLLLLLLLLLLLLLLLLL.LLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "LLL.LLLL.LLLLLLLLLLLLLLLLLLL.L.LLLLLL.LLLLLLLLLLLLLLLLLLL.LLLLLL.LLLLLLLLLLLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLLLLL.LLLL.LLLLL.LLLLLLLL.LLLLL.LLLLLLL.LLLLLLLLLLLLLLLLLLLLL..LLLL.LLLL.L.LLLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLLLLL.LLLL.LLLLL.LLLLLLLL.L.LLL.LLLLLLLLLLLL.LLLLLLLLLLLLLLLL.LLLLL.LL.LLLL.LLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLLLL...LLLLLLLLL.LLLLLLLL.LL.LLLLLLLLLL.LLLLLL.LLLLL.LLLLLLLL.LLLLLLLLLLLLL.LLLLLLLLLLLLLLLLLLL\n"
                                        + "LLLLLLLLLLLLL.LLLLL.LLLLLLLLLLLLLL.LLLLLLL.LLLLLL.LLLL.LLLLLLLLLLLLLLL.L.LLLLLLLLLLLL.LLLLLLLLLLLL\n"
                                        + "LLLLLLLL.LLLLLL.LLL.LLLLLLLLLLLLLLLLLLLLLL.LLLLLL.LLLLLLLLLLLLLL.LLLLLLLLLLLLL.LLLLLLLLLLLLLLLLLLL";
}
