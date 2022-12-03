package aoc2017;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Day23 {
    public static void main(String[] args) throws Exception {
        part1();
        part2();
    }

    private static void part1() throws Exception {
        List<Instruction> instructions = parse(DATA);
        AtomicLong sound = new AtomicLong();
        Consumer<Long> sender = sound::set;
        Supplier<Long> receiver = () -> null;
        Program program = new Program(sender, receiver, instructions);
        program.run();
        System.out.println(program.muls);
    }

    private static void part2() throws Exception {
        // For each b(incremented by 17) test all values of e and d. If e*d == b => f=0 -> h++
        // "All values of e and d. If e*d == b" == "b not prime"
        List<Integer> primes = new ArrayList<>();
        int b = 79 * 100 + 100_000;
        int c = b + 17_000;
        for (int i = 2; i <= c ; i++) {
            boolean isPrime = true;
            for (int prime : primes) {
                if (i % prime == 0) {
                    isPrime = false;
                    break;
                }
            }
            if(isPrime) {
                primes.add(i);
            }
        }

        int count = 0;
        for (int i = b; i <= c; i+=17) {
            if (!primes.contains(i)) {
                count++;
            }
        }
        System.out.println(count);
    }

    private static List<Instruction> parse(String data) {
        List<Instruction> instructions = new ArrayList<>();
        for (String line : data.split("\n")) {
            String[] parts = line.split(" ");
            InstructionType type = InstructionType.valueOf(parts[0]);
            Val x = Val.parse(parts[1]);
            Val y = parts.length == 3 ? Val.parse(parts[2]) : Val.EMPTY;
            instructions.add(new Instruction(type, x, y));
        }
        return instructions;
    }

    static class Program {
        final long[] registers = new long['z' + 1];
        private final List<Instruction> instructions;
        private final Consumer<Long> sender;
        private final Supplier<Long> receiver;
        private int ix;
        private int muls = 0;

        Program(Consumer<Long> sender, Supplier<Long> receiver, List<Instruction> instructions) {
            this.sender = sender;
            this.receiver = receiver;
            this.instructions = instructions;
        }

        void run() {
            while (ix < instructions.size()) {
                Instruction instr = instructions.get(ix++);
                switch (instr.type) {
                case set:
                    registers[instr.x.regIx(registers)] = instr.y.val(registers);
                    break;
                case sub:
                    registers[instr.x.regIx(registers)] -= instr.y.val(registers);
                    break;
                case mul:
                    muls++;
                    registers[instr.x.regIx(registers)] *= instr.y.val(registers);
                    break;
                case jnz:
                    if (instr.x.val(registers) != 0) {
                        ix += instr.y.val(registers) - 1;
                    }
                    break;
                }
            }
        }
    }

    interface Val {
        Val EMPTY = new Value(-1);

        long val(long[] registers);

        int regIx(long[] registers);

        static Val parse(String s) {
            try {
                return new Value(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                return new RegValue(s.charAt(0));
            }
        }
    }

    static class Value implements Val {
        final int x;

        Value(int x) {
            this.x = x;
        }

        @Override
        public long val(long[] registers) {
            return x;
        }

        @Override
        public int regIx(long[] registers) {
            return x;
        }
    }

    static class RegValue implements Val {
        final int reg;

        RegValue(int reg) {
            this.reg = reg;
        }

        @Override
        public long val(long[] registers) {
            return registers[reg];
        }

        @Override
        public int regIx(long[] registers) {
            return reg;
        }
    }

    enum InstructionType {
        set, sub, mul, jnz
    }

    static class Instruction {
        final InstructionType type;
        final Val x;
        final Val y;

        Instruction(InstructionType type, Val x, Val y) {
            this.type = type;
            this.x = x;
            this.y = y;
        }
    }

    private static final String DATA = ""
                                       + "set b 79\n"
                                       + "set c b\n"
                                       + "jnz a 2\n"
                                       + "jnz 1 5\n"
                                       + "mul b 100\n"
                                       + "sub b -100000\n"
                                       + "set c b\n"
                                       + "sub c -17000\n"
                                       + "set f 1\n"
                                       + "set d 2\n"
                                       + "set e 2\n"
                                       + "set g d\n"
                                       + "mul g e\n"
                                       + "sub g b\n"
                                       + "jnz g 2\n"
                                       + "set f 0\n"
                                       + "sub e -1\n"
                                       + "set g e\n"
                                       + "sub g b\n"
                                       + "jnz g -8\n"
                                       + "sub d -1\n"
                                       + "set g d\n"
                                       + "sub g b\n"
                                       + "jnz g -13\n"
                                       + "jnz f 2\n"
                                       + "sub h -1\n"
                                       + "set g b\n"
                                       + "sub g c\n"
                                       + "jnz g 2\n"
                                       + "jnz 1 3\n"
                                       + "sub b -17\n"
                                       + "jnz 1 -23";
}
