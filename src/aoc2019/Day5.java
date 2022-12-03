package aoc2019;

import java.util.Arrays;
import java.util.LinkedList;

import aoc2019.Day5.Param.Mode;

public class Day5 {
    public static void main(String[] args) {
        String input = INPUT;
        a(input);
        b(input);
    }

    private static void b(String input) {
        IO io = new IO();
        IntComputer intComputer = new IntComputer(io, io, input);
        io.write(5);
        intComputer.run();
        System.out.println(io.values.getLast());
    }

    private static void a(String input) {
        IO io = new IO();
        IntComputer intComputer = new IntComputer(io, io, input);
        io.write(1);
        intComputer.run();
        System.out.println(io.values.getLast());

    }

    static class IntComputer {
        long relativeAddress = 0;
        IO read;
        IO write;
        final Reader reader;
        boolean player;

        IntComputer(IO read, IO write, String input) {
            this.read = read;
            this.write = write;
            this.reader = new Reader(input);
        }

        int run() {
            long[] reg = reader.reg;
            while (true) {
                Instruction instruction = reader.next();
                if (instruction.opCode == OpCode.ADJUST_RELATIVE && instruction.params[0].mode == Mode.EXIT) {
                    reader.ix = Integer.MAX_VALUE;
                    return 0;
                }

                Param[] params = instruction.params;
                long ix;
                switch (instruction.opCode) {
                case ADD:
                    ix = params[2].value;
                    if (params[2].mode == Mode.RELATIVE) {
                        ix += relativeAddress;
                    }
                    reg[(int) ix] = params[0].value(reg, relativeAddress) + params[1].value(reg, relativeAddress);
                    break;
                case MULTIPLY:
                    ix = params[2].value;
                    if (params[2].mode == Mode.RELATIVE) {
                        ix += relativeAddress;
                    }
                    reg[(int) ix] = params[0].value(reg, relativeAddress) * params[1].value(reg, relativeAddress);
                    break;
                case INPUT:
                    try {
                        if (params[0].mode == Mode.RELATIVE) {
                            reg[(int) (relativeAddress + params[0].value)] = read.read();
                        } else {
                            reg[(int) params[0].value] = read.read();
                        }
                    } catch (NullPointerException e) {
                        reader.ix -= 2;
                        return 1;
                    }
                    break;
                case OUTPUT:
                    if (player) {
                        System.out.println("Player write");
                    }
                    write.write(params[0].value(reg, relativeAddress));
                    break;
                case JUMP_IF_TRUE:
                    if (params[0].value(reg, relativeAddress) > 0) {
                        reader.ix = (int) params[1].value(reg, relativeAddress);
                    }
                    break;
                case JUMP_IF_FALSE:
                    if (params[0].value(reg, relativeAddress) == 0) {
                        reader.ix = (int) params[1].value(reg, relativeAddress);
                    }
                    break;
                case LESS_THAN:
                    ix = params[2].value;
                    if (params[2].mode == Mode.RELATIVE) {
                        ix += relativeAddress;
                    }
                    if (params[0].value(reg, relativeAddress) < params[1].value(reg, relativeAddress)) {
                        reg[(int) ix] = 1;
                    } else {
                        reg[(int) ix] = 0;
                    }
                    break;
                case EQUALS:
                    ix = params[2].value;
                    if (params[2].mode == Mode.RELATIVE) {
                        ix += relativeAddress;
                    }

                    if (params[0].value(reg, relativeAddress) == params[1].value(reg, relativeAddress)) {
                        reg[(int) ix] = 1;
                    } else {
                        reg[(int) ix] = 0;
                    }
                    break;
                case ADJUST_RELATIVE:
                    relativeAddress += params[0].value(reg, relativeAddress);
                    break;
                }
            }
        }
    }

    static class IO {
        LinkedList<Long> values = new LinkedList<>();

        void write(long value) {
            values.addLast(value);
        }

        Long read() {
            return values.pollFirst();
        }
    }

    static class Reader {
        final long[] reg;
        int ix = 0;

        Reader(String input) {
            long[] inp = Arrays.stream(input.split(",")).mapToLong(Long::parseLong).toArray();
            this.reg = new long[1_000_000];
            for (int i = 0; i <inp.length ; i++) {
                reg[i] = inp[i];
            }
        }

        Instruction next() {
            long type = reg[ix++];
            if (type == 99) {
                return new Instruction(OpCode.ADJUST_RELATIVE, new Param(Mode.EXIT, 9));
            }
            OpCode opCode = OpCode.parse(type);
            switch (opCode) {
            case ADD:
            case MULTIPLY:
            case LESS_THAN:
            case EQUALS:
                return new Instruction(opCode,
                        new Param(Mode.value(type, 1), reg[ix++]),
                        new Param(Mode.value(type, 2), reg[ix++]),
                        new Param(Mode.value(type, 3), reg[ix++]));
            case INPUT:
            case OUTPUT:
            case ADJUST_RELATIVE:
                return new Instruction(opCode, new Param(Mode.value(type, 1), reg[ix++]));

            case JUMP_IF_TRUE:
            case JUMP_IF_FALSE:
                return new Instruction(opCode,
                        new Param(Mode.value(type, 1), reg[ix++]),
                        new Param(Mode.value(type, 2), reg[ix++]));
            }
            throw new RuntimeException();
        }
    }

    private static class Instruction {
        final OpCode opCode;
        final Param[] params;

        private Instruction(OpCode opCode, Param... params) {
            this.opCode = opCode;
            this.params = params;
        }

        @Override
        public String toString() {
            return opCode + ": " + Arrays.toString(params);
        }
    }

    static class Param {
        enum Mode {
            POSITION, IMMEDIATE, RELATIVE, A, B, C, D, E, F, EXIT;
            static final Mode[] MODES = Mode.values();

            static Mode value(long input, int paramIx) {
                input /= Math.pow(10, paramIx + 1);
                return MODES[(int) (input % 10)];
            }
        }

        final Mode mode;
        final long value;

        Param(Mode mode, long value) {
            this.mode = mode;
            this.value = value;
        }

        public long value(long[] reg, long relativeAddress) {
            if (mode == Mode.IMMEDIATE) {
                return value;
            } else if (mode == Mode.POSITION) {
                return reg[(int) value];
            } else {
                return reg[(int) (relativeAddress + value)];
            }
        }

        @Override
        public String toString() {
            return mode + ":" + value;
        }
    }

    enum OpCode {
        ADD(1), MULTIPLY(2), INPUT(3), OUTPUT(4), JUMP_IF_TRUE(5), JUMP_IF_FALSE(6), LESS_THAN(7), EQUALS(8), ADJUST_RELATIVE(9);
        private final int id;

        OpCode(int id) {
            this.id = id;
        }

        static OpCode parse(long id) {
            id = id % 10;
            for (OpCode opCode : values()) {
                if (opCode.id == id) {
                    return opCode;
                }
            }
            throw new RuntimeException();
        }

    }

    private static final String TEST_INPUT = "";

    private static final String INPUT =
            "3,225,1,225,6,6,1100,1,238,225,104,0,1102,46,47,225,2,122,130,224,101,-1998,224,224,4,224,1002,223,8,223,1001,224,6,224,1,224,223,223,1102,61,51,225,102,32,92,224,101,-800,224,224,4,224,1002,223,8,223,1001,224,1,224,1,223,224,223,1101,61,64,225,1001,118,25,224,101,-106,224,224,4,224,1002,223,8,223,101,1,224,224,1,224,223,223,1102,33,25,225,1102,73,67,224,101,-4891,224,224,4,224,1002,223,8,223,1001,224,4,224,1,224,223,223,1101,14,81,225,1102,17,74,225,1102,52,67,225,1101,94,27,225,101,71,39,224,101,-132,224,224,4,224,1002,223,8,223,101,5,224,224,1,224,223,223,1002,14,38,224,101,-1786,224,224,4,224,102,8,223,223,1001,224,2,224,1,223,224,223,1,65,126,224,1001,224,-128,224,4,224,1002,223,8,223,101,6,224,224,1,224,223,223,1101,81,40,224,1001,224,-121,224,4,224,102,8,223,223,101,4,224,224,1,223,224,223,4,223,99,0,0,0,677,0,0,0,0,0,0,0,0,0,0,0,1105,0,99999,1105,227,247,1105,1,99999,1005,227,99999,1005,0,256,1105,1,99999,1106,227,99999,1106,0,265,1105,1,99999,1006,0,99999,1006,227,274,1105,1,99999,1105,1,280,1105,1,99999,1,225,225,225,1101,294,0,0,105,1,0,1105,1,99999,1106,0,300,1105,1,99999,1,225,225,225,1101,314,0,0,106,0,0,1105,1,99999,1008,677,226,224,1002,223,2,223,1005,224,329,1001,223,1,223,107,677,677,224,102,2,223,223,1005,224,344,101,1,223,223,1107,677,677,224,102,2,223,223,1005,224,359,1001,223,1,223,1108,226,226,224,1002,223,2,223,1006,224,374,101,1,223,223,107,226,226,224,1002,223,2,223,1005,224,389,1001,223,1,223,108,226,226,224,1002,223,2,223,1005,224,404,1001,223,1,223,1008,677,677,224,1002,223,2,223,1006,224,419,1001,223,1,223,1107,677,226,224,102,2,223,223,1005,224,434,1001,223,1,223,108,226,677,224,102,2,223,223,1006,224,449,1001,223,1,223,8,677,226,224,102,2,223,223,1006,224,464,1001,223,1,223,1007,677,226,224,1002,223,2,223,1006,224,479,1001,223,1,223,1007,677,677,224,1002,223,2,223,1005,224,494,1001,223,1,223,1107,226,677,224,1002,223,2,223,1006,224,509,101,1,223,223,1108,226,677,224,102,2,223,223,1005,224,524,1001,223,1,223,7,226,226,224,102,2,223,223,1005,224,539,1001,223,1,223,8,677,677,224,1002,223,2,223,1005,224,554,101,1,223,223,107,677,226,224,102,2,223,223,1006,224,569,1001,223,1,223,7,226,677,224,1002,223,2,223,1005,224,584,1001,223,1,223,1008,226,226,224,1002,223,2,223,1006,224,599,101,1,223,223,1108,677,226,224,102,2,223,223,1006,224,614,101,1,223,223,7,677,226,224,102,2,223,223,1005,224,629,1001,223,1,223,8,226,677,224,1002,223,2,223,1006,224,644,101,1,223,223,1007,226,226,224,102,2,223,223,1005,224,659,101,1,223,223,108,677,677,224,1002,223,2,223,1006,224,674,1001,223,1,223,4,223,99,226";
}
