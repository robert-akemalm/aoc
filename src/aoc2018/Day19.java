package aoc2018;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Day19 {
    public static void main(String[] args) {
        String input = INPUT;
        a(input);
        long t = System.currentTimeMillis();
        b(input);
        System.out.println(System.currentTimeMillis() - t);
    }

    private static void b(String input) {
        String[] values = Util.parseStrings(input);
        int pointer = Integer.parseInt(values[0].substring(values[0].length() - 1));
        Instruction[] instructions = Stream.of(values).filter(s -> !s.startsWith("#")).map(Instruction::parse).toArray(Instruction[]::new);
        Register register = new Register(pointer);
        register.registers[0] = 1;
        Program program = new Program(register, instructions);
        program.run();
        System.out.println(register);
    }

    private static void a(String input) {
        String[] values = Util.parseStrings(input);
        int pointer = Integer.parseInt(values[0].substring(values[0].length() - 1));
        Instruction[] instructions = Stream.of(values).filter(s -> !s.startsWith("#")).map(Instruction::parse).toArray(Instruction[]::new);
        Register register = new Register(pointer);
        Program program = new Program(register, instructions);
        program.run();
        System.out.println(register);
    }

    private static class Program {
        final Register register;
        private final Instruction[] instructions;
        int pointer = 0;

        private Program(Register register, Instruction[] instructions) {
            this.register = register;
            this.instructions = instructions;
        }

        void run() {
            while (true) {
                register.setPointerValue(pointer);
                if (pointer >= instructions.length) {
                    return;
                }
                Instruction instruction = instructions[pointer];
                if (pointer == 4 && register.registers[1] == register.registers[4]) {
                    List<Integer> divisors = divisors(register.registers[4]);
                    for (int divisor : divisors) {
                        register.registers[0] += divisor;
                    }
                    register.registers[1] = 0;
                } else {
                    instruction.run(register);
                }

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
        final long[] registers = new long[6];
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

    private static final String TEST_INPUT = "#ip 0\n"
                                             + "seti 5 0 1\n"
                                             + "seti 6 0 2\n"
                                             + "addi 0 1 0\n"
                                             + "addr 1 2 3\n"
                                             + "setr 1 0 0\n"
                                             + "seti 8 0 4\n"
                                             + "seti 9 0 5";

    private static final String INPUT = "#ip 2\n"
                                        + "addi 2 16 2\n"
                                        + "seti 1 4 3\n"
                                        + "seti 1 5 1\n"
                                        + "mulr 3 1 5\n"
                                        + "eqrr 5 4 5\n"
                                        + "addr 5 2 2\n"
                                        + "addi 2 1 2\n"
                                        + "addr 3 0 0\n"
                                        + "addi 1 1 1\n"
                                        + "gtrr 1 4 5\n"
                                        + "addr 2 5 2\n"
                                        + "seti 2 9 2\n"
                                        + "addi 3 1 3\n"
                                        + "gtrr 3 4 5\n"
                                        + "addr 5 2 2\n"
                                        + "seti 1 6 2\n"
                                        + "mulr 2 2 2\n"
                                        + "addi 4 2 4\n"
                                        + "mulr 4 4 4\n"
                                        + "mulr 2 4 4\n"
                                        + "muli 4 11 4\n"
                                        + "addi 5 7 5\n"
                                        + "mulr 5 2 5\n"
                                        + "addi 5 4 5\n"
                                        + "addr 4 5 4\n"
                                        + "addr 2 0 2\n"
                                        + "seti 0 1 2\n"
                                        + "setr 2 1 5\n"
                                        + "mulr 5 2 5\n"
                                        + "addr 2 5 5\n"
                                        + "mulr 2 5 5\n"
                                        + "muli 5 14 5\n"
                                        + "mulr 5 2 5\n"
                                        + "addr 4 5 4\n"
                                        + "seti 0 6 0\n"
                                        + "seti 0 6 2";
}
