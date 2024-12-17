package aoc2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day17 {
    private static void a(Input input) {
        System.out.println(runProgram(input.registerA, input.registerB, input.registerC, input.program));
    }

    private static void b(Input input) {
        String expected = Arrays.stream(input.program).mapToObj(String::valueOf).collect(Collectors.joining(","));
        long a = 0;
        while (!runProgram(a, input.registerB, input.registerC, input.program).equals(expected)) {
            for (long i = 0; i < 896; i++) {
                long testA = a * 8L + i;
                String output = runProgram(testA, input.registerB, input.registerC, input.program);
                if (!output.isEmpty() && expected.endsWith(output)) {
                    a = testA;
                    break;
                }
            }
        }
        System.out.println(a);
    }

    private static String runProgram(long initialA, long initialB, long initialC, int[] program) {
        List<Long> out = new ArrayList<>();
        long[] registers = {0, 1, 2, 3, initialA, initialB, initialC};
        for (int pointer = 0; pointer < program.length; pointer += 2) {
            int op = program[pointer + 1];
            switch (program[pointer]) {
            case 0 -> registers[4] = registers[4] >> registers[op];
            case 1 -> registers[5] ^= op;
            case 2 -> registers[5] = registers[op] % 8;
            case 3 -> pointer = registers[4] == 0 ? pointer : op - 2;
            case 4 -> registers[5] ^= registers[6];
            case 5 -> out.add(registers[op] % 8);
            case 6 -> registers[5] = registers[4] >> registers[op];
            case 7 -> registers[6] = registers[4] >> registers[op];
            }
        }
        return out.stream().map(Object::toString).collect(Collectors.joining(","));
    }

    record Input(int registerA, int registerB, int registerC, int[] program) {
        static Input parse(String input) {
            List<String> lines = input.lines().toList();
            int registerA = Util.extractInts(lines.get(0))[0];
            int registerB = Util.extractInts(lines.get(1))[0];
            int registerC = Util.extractInts(lines.get(2))[0];
            int[] program = Util.extractInts(lines.get(4));
            return new Input(registerA, registerB, registerC, program);
        }
    }

    private static final String TEST_INPUT = "Register A: 729\n"
                                             + "Register B: 0\n"
                                             + "Register C: 0\n"
                                             + "\n"
                                             + "Program: 0,1,5,4,3,0";

    private static final String INPUT = "Register A: 61657405\n"
                                        + "Register B: 0\n"
                                        + "Register C: 0\n"
                                        + "\n"
                                        + "Program: 2,4,1,2,7,5,4,3,0,3,1,7,5,5,3,0";
    /*
     * 2, 4 B = A % 8
     * 1, 2 B = B ^ 2
     * 7, 5 C = A / Math.pow(2,B)
     * 4, 3 B = B ^ C
     * 0, 3 A = A / 8
     * 1, 7 B = B ^ 7
     * 5, 5 OUT B
     * 3, 0 A == 0 ? Exit : Jump to 0
     * ---
     * Max to test:
     * 1, 7 => B=7
     * 4, 3 => C=7
     * 7, 5 => A = 7 * Math.pow(2,7) = 896
     */

    public static void main(String[] args) {
        Util.time(() -> a(Input.parse(TEST_INPUT)));
        Util.time(() -> a(Input.parse(INPUT)));
        Util.time(() -> b(Input.parse(INPUT)));
    }
}
