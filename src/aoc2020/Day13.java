package aoc2020;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Day13 {
    public static void main(String[] args) {
        a(TEST_INPUT);
        a(INPUT);
        b(TEST_INPUT);
        b(INPUT);
    }

    private static void b(String input) {
        long[] busses = input.lines()
                             .filter(l -> l.contains(","))
                             .flatMap(l -> Arrays.stream(l.split(",")))
                             .mapToLong(b -> b.equals("x") ? 0 : Long.parseLong(b))
                             .toArray();
        int[] ixsToCheck = IntStream.range(1, busses.length).filter(ix -> busses[ix] != 0).toArray();
        long time = busses[0];
        long jump = time;
        for (int ix : ixsToCheck) {
            while ((time + ix) % busses[ix] != 0) {
                time += jump;
            }
            jump *= busses[ix];
        }
        System.out.println(time);
    }

    private static void a(String input) {
        String[] rows = input.lines().toArray(String[]::new);
        long depart = Long.parseLong(rows[0]);
        String[] busses = rows[1].split(",");
        long[] valid = Arrays.stream(busses).filter(b -> !b.equals("x")).mapToLong(Long::parseLong).toArray();
        int bestIx = 0;
        long earliest = Integer.MAX_VALUE;
        for (int i = 0; i < valid.length; i++) {
            long busTime = valid[i];
            while (busTime < depart) {
                busTime += valid[i];
            }
            if (busTime < earliest) {
                bestIx = i;
                earliest = busTime;
            }
        }
        System.out.println(valid[bestIx] + " " + earliest + " => " + (earliest - depart) * valid[bestIx]);
    }

    private static final String TEST_INPUT = "939\n"
                                             + "1789,37,47,1889";

    private static final String INPUT = "1008833\n"
                                        + "19,x,x,x,x,x,x,x,x,41,x,x,x,x,x,x,x,x,x,643,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,17,13,x,x,x,x,23,x,x,x,x,x,x,x,509,x,x,x,x,x,37,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,29";
}
