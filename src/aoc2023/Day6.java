package aoc2023;

import java.util.List;
import java.util.stream.IntStream;

public class Day6 {
    private static void a(Input input) {
        System.out.println(input.races.stream().mapToLong(Race::waysToWin).reduce((a, b) -> a * b).orElseThrow());
    }

    private static void b(Input input) {
        System.out.println(input.raceB.waysToWin());
    }

    record Race(int time, long distance) {
        long waysToWin() {
            long firstWin = IntStream.range(0, time).filter(i -> ((long) i) * (time - i) > distance).findFirst().orElseThrow();
            return time + 1 - 2 * firstWin;
        }
    }

    record Input(List<Race> races, Race raceB) {
        static Input parse(String input) {
            List<String> lines = input.lines().toList();

            int[] times = Util.extractInts(lines.getFirst());
            int[] distances = Util.extractInts(lines.getLast());
            List<Race> races = IntStream.range(0, times.length).mapToObj(i-> new Race(times[i], distances[i])).toList();

            int timeB = Util.extractInts(lines.getFirst().replaceAll(" ", ""))[0];
            long distanceB = Util.extractLongs(lines.getLast().replaceAll(" ", ""))[0];
            return new Input(races, new Race(timeB, distanceB));
        }
    }

    private static final String TEST_INPUT = "Time:      7  15   30\n"
                                             + "Distance:  9  40  200";

    private static final String INPUT = "Time:        47     84     74     67\n"
                                        + "Distance:   207   1394   1209   1014";

    public static void main(String[] args) {
        Util.time(() -> a(Input.parse(TEST_INPUT)));
        Util.time(() -> a(Input.parse(INPUT)));
        Util.time(() -> b(Input.parse(TEST_INPUT)));
        Util.time(() -> b(Input.parse(INPUT)));
    }
}
