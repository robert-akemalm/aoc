package aoc2017;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day6 {
    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }

    private static void part1() throws IOException {
        String[] lines = DATA.split("\t");
        int[] memory = new int[lines.length];
        for (int i = 0; i < lines.length; i++) {
            memory[i] = Integer.parseInt(lines[i]);
        }

        Set<String> seen = new HashSet<>();
        String memoryAsString = Arrays.toString(memory);
        while (!seen.contains(memoryAsString)) {
            seen.add(Arrays.toString(memory));
            reArrange(memory);
            memoryAsString = Arrays.toString(memory);
        }
        System.out.println(seen.size());
    }

    private static void reArrange(int[] memory) {
     int max = 0;
        for (int i = 0; i < memory.length; i++) {
            if (memory[i] > memory[max]) {
                max = i;
            }
        }
        int blocks = memory[max];
        memory[max] = 0;
        while (blocks > 0) {
            max++;
            memory[max % memory.length]++;
            blocks--;
        }
    }

    private static void part2() throws IOException {
        String[] lines = DATA.split("\t");
        int[] memory = new int[lines.length];
        for (int i = 0; i < lines.length; i++) {
            memory[i] = Integer.parseInt(lines[i]);
        }

        List<String> seen = new ArrayList<>();
        String memoryAsString = Arrays.toString(memory);
        while (!seen.contains(memoryAsString)) {
            seen.add(Arrays.toString(memory));
            reArrange(memory);
            memoryAsString = Arrays.toString(memory);
        }
        int pos = seen.indexOf(memoryAsString);
        System.out.println(seen.size() - pos);
    }

    private static final String DATA = "4\t10\t4\t1\t8\t4\t9\t14\t5\t1\t14\t15\t0\t15\t3\t5";
}
