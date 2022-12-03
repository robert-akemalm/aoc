package aoc2017;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Day18 {
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
        System.out.println(sound.get());
    }

    private static void part2() throws Exception {
        Queue<Long> messagesTo0 = new ArrayBlockingQueue<>(100_000);
        Queue<Long> messagesTo1 = new ArrayBlockingQueue<>(100_000);

        AtomicInteger nbrOfSentMessages = new AtomicInteger();
        Consumer<Long> sender0 = messagesTo1::add;
        Consumer<Long> sender1 = val -> {
            messagesTo0.add(val);
            nbrOfSentMessages.incrementAndGet();
        };
        Supplier<Long> receiver0 = messagesTo0::poll;
        Supplier<Long> receiver1 = messagesTo1::poll;
        List<Instruction> instructions = parse(DATA);
        Program p0 = new Program(sender0, receiver0, instructions);
        p0.registers['p'] = 0;
        Program p1 = new Program(sender1, receiver1, instructions);
        p1.registers['p'] = 1;

        while(true) {
            p0.run();
            p1.run();
            if (messagesTo0.size() == 0 && messagesTo1.size() == 0) {
                System.out.println(nbrOfSentMessages.get());
                return;
            }
        }
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

        Program(Consumer<Long> sender, Supplier<Long> receiver, List<Instruction> instructions) {
            this.sender = sender;
            this.receiver = receiver;
            this.instructions = instructions;
        }

        void run() {
            while (ix < instructions.size()) {
                Instruction instr = instructions.get(ix++);
                switch (instr.type) {
                case snd:
                    sender.accept(instr.x.val(registers));
                    break;
                case set:
                    registers[instr.x.regIx(registers)] = instr.y.val(registers);
                    break;
                case add:
                    registers[instr.x.regIx(registers)] += instr.y.val(registers);
                    break;
                case mul:
                    registers[instr.x.regIx(registers)] *= instr.y.val(registers);
                    break;
                case mod:
                    registers[instr.x.regIx(registers)] %= instr.y.val(registers);
                    break;
                case rcv:
                    Long val = receiver.get();
                    if (val == null) {
                        ix--;
                        return;
                    }
                    registers[instr.x.regIx(registers)] = val;

//                    if (registers[instr.x.regIx(registers)] > 0) {
//                        return;
//                    }
                    break;
                case jgz:
                    if (instr.x.val(registers) > 0) {
                        ix += instr.y.val(registers) - 1;
                    }
                    break;
                }

            }
            throw new IllegalArgumentException();
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
        snd, set, add, mul, mod, rcv, jgz
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

    private static final String DATA2 = "set a 1\n"
                                        + "add a 2\n"
                                        + "mul a a\n"
                                        + "mod a 5\n"
                                        + "snd a\n"
                                        + "set a 0\n"
                                        + "rcv a\n"
                                        + "jgz a -1\n"
                                        + "set a 1\n"
                                        + "jgz a -2";

    private static final String DATA = "set i 31\n"
                                       + "set a 1\n"
                                       + "mul p 17\n"
                                       + "jgz p p\n"
                                       + "mul a 2\n"
                                       + "add i -1\n"
                                       + "jgz i -2\n"
                                       + "add a -1\n"
                                       + "set i 127\n"
                                       + "set p 952\n"
                                       + "mul p 8505\n"
                                       + "mod p a\n"
                                       + "mul p 129749\n"
                                       + "add p 12345\n"
                                       + "mod p a\n"
                                       + "set b p\n"
                                       + "mod b 10000\n"
                                       + "snd b\n"
                                       + "add i -1\n"
                                       + "jgz i -9\n"
                                       + "jgz a 3\n"
                                       + "rcv b\n"
                                       + "jgz b -1\n"
                                       + "set f 0\n"
                                       + "set i 126\n"
                                       + "rcv a\n"
                                       + "rcv b\n"
                                       + "set p a\n"
                                       + "mul p -1\n"
                                       + "add p b\n"
                                       + "jgz p 4\n"
                                       + "snd a\n"
                                       + "set a b\n"
                                       + "jgz 1 3\n"
                                       + "snd b\n"
                                       + "set f 1\n"
                                       + "add i -1\n"
                                       + "jgz i -11\n"
                                       + "snd a\n"
                                       + "jgz f -16\n"
                                       + "jgz a -19";
}
