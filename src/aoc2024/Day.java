package aoc2024;

public class Day {
    private static void a(Input input) {

    }

    private static void b(Input input) {

    }

    record Input() {
        static Input parse(String input) {

            return new Input();
        }
    }

    private static final String TEST_INPUT = "";

    private static final String INPUT = "";

    public static void main(String[] args) {
        Util.time(() -> a(Input.parse(TEST_INPUT)));
        Util.time(() -> a(Input.parse(INPUT)));
        Util.time(() -> b(Input.parse(TEST_INPUT)));
        Util.time(() -> b(Input.parse(INPUT)));
    }
}
