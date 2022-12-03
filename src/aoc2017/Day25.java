package aoc2017;

import java.util.stream.IntStream;


public class Day25 {

    public static void main(String[] args) throws Exception {
        State state = State.A;
        int[] tape = new int[100_000];
        int cursor = tape.length / 2;
        int minI = tape.length;
        int maxI = 0;
        for (int i = 0; i < 12_919_244; i++) {
            minI = Math.min(minI, cursor);
            maxI = Math.max(maxI, cursor);
            switch (state) {
            case A:
                if (tape[cursor] == 0) {
                    tape[cursor] = 1;
                    cursor++;
                    state = State.B;
                } else {
                    tape[cursor] = 0;
                    cursor--;
                    state = State.C;
                }
                break;
            case B:
                if (tape[cursor] == 0) {
                    tape[cursor] = 1;
                    cursor--;
                    state = State.A;
                } else {
                    tape[cursor] = 1;
                    cursor++;
                    state = State.D;
                }
                break;
            case C:
                if (tape[cursor] == 0) {
                    tape[cursor] = 1;
                    cursor++;
                    state = State.A;
                } else {
                    tape[cursor] = 0;
                    cursor--;
                    state = State.E;
                }
                break;
            case D:
                if (tape[cursor] == 0) {
                    tape[cursor] = 1;
                    cursor++;
                    state = State.A;
                } else {
                    tape[cursor] = 0;
                    cursor++;
                    state = State.B;
                }
                break;
            case E:
                if (tape[cursor] == 0) {
                    tape[cursor] = 1;
                    cursor--;
                    state = State.F;
                } else {
                    tape[cursor] = 1;
                    cursor--;
                    state = State.C;
                }
                break;
            case F:
                if (tape[cursor] == 0) {
                    tape[cursor] = 1;
                    cursor++;
                    state = State.D;
                } else {
                    tape[cursor] = 1;
                    cursor++;
                    state = State.A;
                }
                break;
            }
        }
        int sum = IntStream.of(tape).sum();
        System.out.println(sum);
    }

    enum State {A, B, C, D, E, F}
}
