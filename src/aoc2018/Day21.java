package aoc2018;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Day21 {
    public static void main(String[] args) {
        String input = INPUT;
//        a(input);
        long t = System.currentTimeMillis();
        b(input);
        System.out.println(System.currentTimeMillis() - t);
    }

    // 14596916
    // 2146336636 to high
    // 1082516 to low
    private static void b(String input) {
        int val = f(7216956);
        Set<Integer> toTry = new HashSet<>();
        for (int i = 0; i < 1; i++) {
            int val2 = f(i);
            if (val2 == val) {
                toTry.add(i);
            }
        }

        String[] values = Util.parseStrings(input);
        int pointer = Integer.parseInt(values[0].substring(values[0].length() - 1));
        Instruction[] instructions = Stream.of(values).filter(s -> !s.startsWith("#")).map(Instruction::parse).toArray(Instruction[]::new);
        Register register = new Register(pointer);
        int max = 0;
        List<Integer> test = new ArrayList<>(toTry);
        test.sort(null);
//        System.out.println(test.get(test.size() - 1));
            Arrays.fill(register.registers, 0);
            register.registers[0] = 0;
            Program program = new Program(register, instructions);
            int instructionsNeeded = program.run(Integer.MAX_VALUE);
            max = Math.max(max,instructionsNeeded);
        System.out.println(max);

    }

    static int f(int i) {
        int a = i & 1677215;
        int b = a * 65899;
        int c = b & 1677215;
        return c;
    }

    private static void a(String input) {
        String[] values = Util.parseStrings(input);
        int pointer = Integer.parseInt(values[0].substring(values[0].length() - 1));
        Instruction[] instructions = Stream.of(values).filter(s -> !s.startsWith("#")).map(Instruction::parse).toArray(Instruction[]::new);
        Register register = new Register(pointer);
        int min = 1000_000;
        for (int i = 0; i < 1; i++) {
            Arrays.fill(register.registers, 0);
            register.registers[0] = 7216956;
            Program program = new Program(register, instructions);
            int instructionsNeeded = program.run(min);
            min = Math.min(min,instructionsNeeded);
        }
        System.out.println(min);
    }

    private static class Program {
        final Register register;
        private final Instruction[] instructions;
        int pointer = 0;
        Set<Integer> cache = new HashSet<>();
        int prev = 0;

        private Program(Register register, Instruction[] instructions) {
            this.register = register;
            this.instructions = instructions;
        }

        int run(int min) {
            while (true) {
                register.setPointerValue(pointer);
                if (pointer >= instructions.length) {
                    return 0;
                }
//                System.out.println(register);
                if (pointer == 28) {
                    int current = register.registers[3];
                    if (cache.add(current)) {
//                        System.out.println("Added " + current);
                        prev = current;
                    } else {
                        System.out.println("Repeated " + prev);
                        return 0;
                    }
                }
                Instruction instruction = instructions[pointer];
                instruction.run(register);

                pointer = register.getPointerValue() + 1;
            }
        }
    }

    public static List<Integer> divisors(long number) {
        List<Integer> divisors = new ArrayList<>();
        for (int i = 1; i <= number; i++) {
            if (number % i == 0) {
                divisors.add(i);
            }
        }
        return divisors;
    }

    private static class Instruction {
        final Operation op;
        final int a;
        final int b;
        final int c;

        private Instruction(Operation op, int a, int b, int c) {
            this.op = op;
            this.a = a;
            this.b = b;
            this.c = c;
        }

        void run(Register register) {
            op.run(register, a, b, c);
        }

        static Instruction parse(String data) {
            String[] parts = data.split(" ");
            Operation op = Operation.parse(parts[0]);
            int a = Integer.parseInt(parts[1]);
            int b = Integer.parseInt(parts[2]);
            int c = Integer.parseInt(parts[3]);
            return new Instruction(op, a, b, c);
        }
    }

    private static class Register {
        final int[] registers = new int[6];
        final int ip;

        Register(int pointer) {
            this.ip = pointer;
        }

        @Override
        public String toString() {
            return Arrays.toString(registers);
        }

        void setPointerValue(int value) {
            registers[ip] = value;
        }
        int getPointerValue() {
            return (int) registers[ip];
        }
    }

    private interface Operation {
        void run(Register register, int a, int b, int c);

        static Operation parse(String data) {
            switch (data) {
            case "addr":
                return ADDR;
            case "addi":
                return ADDI;
            case "mulr":
                return MULR;
            case "muli":
                return MULI;
            case "banr":
                return BANR;
            case "bani":
                return BANI;
            case "borr":
                return BORR;
            case "bori":
                return BORI;
            case "setr":
                return SETR;
            case "seti":
                return SETI;
            case "gtir":
                return GTIR;
            case "gtri":
                return GTRI;
            case "gtrr":
                return GTRR;
            case "eqir":
                return EQIR;
            case "eqri":
                return EQRI;
            case "eqrr":
                return EQRR;
            }
            throw new RuntimeException();
        }
    }

    private static final Operation ADDR = (r, a, b, c) -> r.registers[c] = r.registers[a] + r.registers[b];
    private static final Operation ADDI = (r, a, b, c) -> r.registers[c] = r.registers[a] + b;

    private static final Operation MULR = (r, a, b, c) -> r.registers[c] = r.registers[a] * r.registers[b];
    private static final Operation MULI = (r, a, b, c) -> r.registers[c] = r.registers[a] * b;

    private static final Operation BANR = (r, a, b, c) -> r.registers[c] = r.registers[a] & r.registers[b];
    private static final Operation BANI = (r, a, b, c) -> r.registers[c] = r.registers[a] & b;

    private static final Operation BORR = (r, a, b, c) -> r.registers[c] = r.registers[a] | r.registers[b];
    private static final Operation BORI = (r, a, b, c) -> r.registers[c] = r.registers[a] | b;

    private static final Operation SETR = (r, a, b, c) -> r.registers[c] = r.registers[a];
    private static final Operation SETI = (r, a, b, c) -> r.registers[c] = a;

    private static final Operation GTIR = (r, a, b, c) -> r.registers[c] = a > r.registers[b] ? 1 : 0;
    private static final Operation GTRI = (r, a, b, c) -> r.registers[c] = r.registers[a] > b ? 1 : 0;
    private static final Operation GTRR = (r, a, b, c) -> r.registers[c] = r.registers[a] > r.registers[b] ? 1 : 0;

    private static final Operation EQIR = (r, a, b, c) -> r.registers[c] = a == r.registers[b] ? 1 : 0;
    private static final Operation EQRI = (r, a, b, c) -> r.registers[c] = r.registers[a] == b ? 1 : 0;
    private static final Operation EQRR = (r, a, b, c) -> r.registers[c] = r.registers[a] == r.registers[b] ? 1 : 0;

    private static final String INPUT = "#ip 1\n"
                                        + "seti 123 0 3\n"
                                        + "bani 3 456 3\n"
                                        + "eqri 3 72 3\n"
                                        + "addr 3 1 1\n"
                                        + "seti 0 0 1\n"
                                        + "seti 0 1 3\n"
                                        + "bori 3 65536 2\n"
                                        + "seti 1505483 6 3\n"
                                        + "bani 2 255 4\n"
                                        + "addr 3 4 3\n"
                                        + "bani 3 16777215 3\n"
                                        + "muli 3 65899 3\n"
                                        + "bani 3 16777215 3\n"
                                        + "gtir 256 2 4\n"
                                        + "addr 4 1 1\n"
                                        + "addi 1 1 1\n"
                                        + "seti 27 6 1\n"
                                        + "seti 0 3 4\n"
                                        + "addi 4 1 5\n"
                                        + "muli 5 256 5\n"
                                        + "gtrr 5 2 5\n"
                                        + "addr 5 1 1\n"
                                        + "addi 1 1 1\n"
                                        + "seti 25 4 1\n"
                                        + "addi 4 1 4\n"
                                        + "seti 17 3 1\n"
                                        + "setr 4 1 2\n"
                                        + "seti 7 4 1\n"
                                        + "eqrr 3 0 4\n"
                                        + "addr 4 1 1\n"
                                        + "seti 5 9 1";
}
