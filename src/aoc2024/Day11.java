package aoc2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Day11 {
    private static void a(Input input) {
        List<Long> stones = Arrays.stream(input.stones).boxed().toList();
        for (int i = 0; i < 25; i++) {
            List<Long> next = new ArrayList<>();
            for (long stone : stones) {
                if (stone == 0) {
                    next.add(1L);
                } else if (String.valueOf(stone).length() % 2 == 0) {
                    String s = String.valueOf(stone);
                    int mid = s.length() / 2;
                    next.add(Long.parseLong(s.substring(0, mid)));
                    next.add(Long.parseLong(s.substring(mid)));
                } else {
                    next.add(stone * 2024);
                }
            }
            stones = next;
        }
        System.out.println(stones.size());
    }

    private static void b(Input input) {
        Map<Long, Long> stoneToCount = new HashMap<>();
        for (long stone : input.stones) {
            stoneToCount.put(stone, stoneToCount.getOrDefault(stone, 0L) + 1);
        }
        for (int i = 0; i < 75; i++) {
            Map<Long, Long> next = new HashMap<>();
            for (Entry<Long, Long> stoneCount : stoneToCount.entrySet()) {
                Long stone = stoneCount.getKey();
                Long count = stoneCount.getValue();
                if (stone == 0) {
                    next.put(1L, next.getOrDefault(1L, 0L) + count);
                } else if (String.valueOf(stone).length() % 2 == 0) {
                    String s = String.valueOf(stone);
                    int mid = s.length() / 2;
                    long left = Long.parseLong(s.substring(0, mid));
                    long right = Long.parseLong(s.substring(mid));
                    next.put(left, next.getOrDefault(left, 0L) + count);
                    next.put(right, next.getOrDefault(right, 0L) + count);
                } else {
                    long newStone = stone * 2024;
                    next.put(newStone, next.getOrDefault(newStone, 0L) + count);
                }
            }
            stoneToCount = next;
        }
        System.out.println(stoneToCount.values().stream().mapToLong(v->v).sum());

    }

    record Input(long[] stones) {
        static Input parse(String input) {
            return new Input(Util.extractLongs(input));
        }
    }

    private static final String TEST_INPUT = "125 17";

    private static final String INPUT = "554735 45401 8434 0 188 7487525 77 7";

    public static void main(String[] args) {
        Util.time(() -> a(Input.parse(TEST_INPUT)));
        Util.time(() -> a(Input.parse(INPUT)));
        Util.time(() -> b(Input.parse(TEST_INPUT)));
        Util.time(() -> b(Input.parse(INPUT)));
    }
}
