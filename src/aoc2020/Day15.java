package aoc2020;

import java.util.Arrays;
import java.util.function.IntSupplier;

public class Day15 {
    public static void main(String[] args) {
        a(TEST_INPUT);
        a(INPUT);
        b(TEST_INPUT);
        b(INPUT);
        b2(TEST_INPUT);
        b2(INPUT);
    }


    private static void b2(String input) {
        Speaker speaker = new Speaker(Arrays.stream(input.split(",")).mapToInt(Integer::parseInt).toArray());
        int last = 0;
        for (int i = 0; i < 30_000_000; i++) {
            last = speaker.getAsInt();
        }
        System.out.println(last);
    }

    private static void b(String input) {
        int[] last = new int[100_000_000];
        int[] beforeLast = new int[100_000_000];
        int cnt = 1;
        int spoken = 0;
        boolean isFirst = true;
        for (int toSay : Arrays.stream(input.split(",")).mapToInt(Integer::parseInt).toArray()) {
            last[toSay] = cnt;
            cnt++;
            spoken = toSay;
        }
        for (; cnt <= 30_000_000; cnt++) {
            int toSay = isFirst ? 0 : last[spoken] - beforeLast[spoken];
            isFirst = last[toSay] == 0;
            beforeLast[toSay] = last[toSay];
            last[toSay] = cnt;
            spoken = toSay;
        }
        System.out.println(spoken);

    }

    private static void a(String input) {
        int[] last = new int[100_000];
        int[] beforeLast = new int[100_000];
        int cnt = 1;
        int spoken = 0;
        boolean isFirst = true;
        for (int toSay : Arrays.stream(input.split(",")).mapToInt(Integer::parseInt).toArray()) {
            last[toSay] = cnt;
            cnt++;
            spoken = toSay;
        }
        for (; cnt <= 2020; cnt++) {
            int toSay = isFirst ? 0 : last[spoken] - beforeLast[spoken];
            isFirst = last[toSay] == 0;
            beforeLast[toSay] = last[toSay];
            last[toSay] = cnt;
            spoken = toSay;
        }
        System.out.println(spoken);
    }

    private static class Speaker implements IntSupplier {
        final int[] initial;
        final int[] last = new int[100_000_000];
        final int[] beforeLast = new int[100_000_000];
        int initialIx = 0;
        int cnt = 1;
        int spoken = 0;
        boolean isFirst = true;

        private Speaker(int[] initial) {
            this.initial = initial;
        }

        @Override
        public int getAsInt() {
            int toSay;
            if (initialIx < initial.length) {
                toSay = initial[initialIx++];
            } else {
                toSay = isFirst ? 0 : last[spoken] - beforeLast[spoken];
            }
            isFirst = last[toSay] == 0;
            beforeLast[toSay] = last[toSay];
            last[toSay] = cnt;
            spoken = toSay;
            cnt++;
            return spoken;
        }
    }

    private static final String TEST_INPUT = "0,3,6";

    private static final String INPUT = "12,20,0,6,1,17,7";
}
