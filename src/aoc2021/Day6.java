package aoc2021;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Day6 {
    public static void main(String[] args) {
        a(TEST_INPUT);
        a(INPUT);
        b(TEST_INPUT);
        b(INPUT);
    }

    private static void b(String input) {
        Map<Integer, Long> initial = new HashMap<>();
        Arrays.stream(input.split(",")).map(Integer::parseInt).forEach(i -> initial.merge(i, 1L, Long::sum));
        Map<Integer, Long> current = new HashMap<>(initial);
        Map<Integer, Long> next = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            for (Entry<Integer, Long> e : current.entrySet()) {
                if (e.getKey() == 0) {
                    next.merge(6, e.getValue(), Long::sum);
                    next.put(8, e.getValue());
                } else {
                    next.merge(e.getKey() - 1, e.getValue(), Long::sum);
                }
            }
            Map<Integer, Long> tmp = current;
            current = next;
            next = tmp;
            next.clear();
        }
        System.out.println(current.values().stream().mapToLong(i->i).sum());
    }

    private static void a(String input) {
        Map<Integer, Integer> initial = new HashMap<>();
        Arrays.stream(input.split(",")).map(Integer::parseInt).forEach(i -> initial.merge(i, 1, Integer::sum));
        Map<Integer, Integer> current = new HashMap<>(initial);
        Map<Integer, Integer> next = new HashMap<>();
        for (int i = 0; i < 80; i++) {
            for (Entry<Integer, Integer> e : current.entrySet()) {
                if (e.getKey() == 0) {
                    next.merge(6, e.getValue(), Integer::sum);
                    next.put(8, e.getValue());
                } else {
                    next.merge(e.getKey() - 1, e.getValue(), Integer::sum);
                }
            }
            Map<Integer, Integer> tmp = current;
            current = next;
            next = tmp;
            next.clear();
        }
        System.out.println(current.values().stream().mapToInt(i->i).sum());
    }

    private static final String TEST_INPUT = "3,4,3,1,2";

    private static final String INPUT = "1,2,1,3,2,1,1,5,1,4,1,2,1,4,3,3,5,1,1,3,5,3,4,5,5,4,3,1,1,4,3,1,5,2,5,2,4,1,1,1,1,1,1,1,4,1,4,4,4,1,4,4,1,4,2,1,1,1,1,3,5,4,3,3,5,4,1,3,1,1,2,1,1,1,4,1,2,5,2,3,1,1,1,2,1,5,1,1,1,4,4,4,1,5,1,2,3,2,2,2,1,1,4,3,1,4,4,2,1,1,5,1,1,1,3,1,2,1,1,1,1,4,5,5,2,3,4,2,1,1,1,2,1,1,5,5,3,5,4,3,1,3,1,1,5,1,1,4,2,1,3,1,1,4,3,1,5,1,1,3,4,2,2,1,1,2,1,1,2,1,3,2,3,1,4,5,1,1,4,3,3,1,1,2,2,1,5,2,1,3,4,5,4,5,5,4,3,1,5,1,1,1,4,4,3,2,5,2,1,4,3,5,1,3,5,1,3,3,1,1,1,2,5,3,1,1,3,1,1,1,2,1,5,1,5,1,3,1,1,5,4,3,3,2,2,1,1,3,4,1,1,1,1,4,1,3,1,5,1,1,3,1,1,1,1,2,2,4,4,4,1,2,5,5,2,2,4,1,1,4,2,1,1,5,1,5,3,5,4,5,3,1,1,1,2,3,1,2,1,1";
}
