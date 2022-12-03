package aoc2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aoc2019.Day5.IO;
import aoc2019.Day5.IntComputer;

public class Day7 {
    public static void main(String[] args) {
        String input = INPUT;
        a(input);
        b(input);
    }

    private static void b(String input) {
        List<int[]> possibleSettings = new ArrayList<>();
        for (int a = 5; a < 10; a++) {
            for (int b = 5; b < 10; b++) {
                for (int c = 5; c < 10; c++) {
                    for (int d = 5; d < 10; d++) {
                        for (int e = 5; e < 10; e++) {
                            Set<Integer> unique = new HashSet<>();
                            if (unique.add(a) && unique.add(b) &&  unique.add(c) &&  unique.add(d) && unique.add(e)) {
                                possibleSettings.add(new int[] {a, b, c, d, e});
                            }
                        }
                    }
                }
            }
        }
        long max = Integer.MIN_VALUE;
        for (int[] setting : possibleSettings) {
            System.out.println("*" + Arrays.toString(setting));
            IntComputer[] computers = new IntComputer[setting.length];
            IO[] ios = new IO[setting.length];
            for (int i = 0; i < setting.length; i++) {
                ios[i] = new IO();
                computers[i] = new IntComputer(ios[i], null, input);
            }
            for (int i = 0; i < setting.length; i++) {
                computers[i].write = ios[(i + 1) % setting.length];
            }


                long lasOutput = 0;
            boolean firstRun = true;
            long lastOutPutFromE = 0;
            while(true) {
                long output = lasOutput;
                for (int i = 0; i < setting.length; i++) {
                    if (firstRun) {
                        computers[i].read.write(setting[i]);
                    }
                    computers[i].read.write(lasOutput);
                    if (computers[i].reader.ix != Integer.MAX_VALUE) {
                        int ret = computers[i].run();
                        System.out.println(" aaa " + ret);
                        if (computers[i].reader.ix != Integer.MAX_VALUE) {
                            System.out.println(" xxx " + ret);
                        }
                    }
                    lasOutput = computers[i].write.values.pollLast();
                    System.out.println(lasOutput + "  " + computers[i].write.values.size());
                    if (i == setting.length -1) {
                        lastOutPutFromE = lasOutput;
                        System.out.println(lastOutPutFromE);
                    }
                }
                firstRun = false;
                boolean done = true;
                for (int i = 0; i < setting.length; i++) {
                    if (computers[i].reader.ix != Integer.MAX_VALUE) {
                        done = false;
                    }
                }
                if (done) {
                    break;
                }
            }
            max = Math.max(max, lastOutPutFromE);
        }
        System.out.println(max);
    }

    private static void a(String input) {
        List<int[]> possibleSettings = new ArrayList<>();
        for (int a = 0; a < 5; a++) {
            for (int b = 0; b < 5; b++) {
                for (int c = 0; c < 5; c++) {
                    for (int d = 0; d < 5; d++) {
                        for (int e = 0; e < 5; e++) {
                            Set<Integer> unique = new HashSet<>();
                            if (unique.add(a) && unique.add(b) &&  unique.add(c) &&  unique.add(d) && unique.add(e)) {
                                possibleSettings.add(new int[] {a, b, c, d, e});
                            }
                        }
                    }
                }
            }
        }
        long max = Integer.MIN_VALUE;
        for (int[] setting : possibleSettings) {
            long lasOutput = 0;
            for (int i = 0; i < setting.length; i++) {
                IO io = new IO();
                IntComputer intComputer = new IntComputer(io, io, input);
                io.write(setting[i]);
                io.write(lasOutput);
                intComputer.run();
                lasOutput = io.values.getLast();
            }
            max = Math.max(max, lasOutput);
            System.out.println(lasOutput);
        }
        System.out.println(max);
    }

    private static final String TEST_INPUT = "3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5";
    private static final String TEST_INPUT_2 = "3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,"
                                             + "-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,"
                                             + "53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10";

    private static final String INPUT = "3,8,1001,8,10,8,105,1,0,0,21,38,63,72,81,106,187,268,349,430,99999,3,9,101,5,9,9,1002,9,3,9,101,3,9,9,4,9,99,3,9,102,3,9,9,101,4,9,9,1002,9,2,9,1001,9,2,9,1002,9,4,9,4,9,99,3,9,1001,9,3,9,4,9,99,3,9,102,5,9,9,4,9,99,3,9,102,4,9,9,1001,9,2,9,1002,9,5,9,1001,9,2,9,102,3,9,9,4,9,99,3,9,1001,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,99,3,9,1001,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,1,9,4,9,99,3,9,102,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,99,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,99,3,9,102,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,99";
}
