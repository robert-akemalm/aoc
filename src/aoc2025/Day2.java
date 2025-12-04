package aoc2025;

import java.util.List;
import java.util.function.LongPredicate;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day2 {
    private static void a(Input input) {
        LongPredicate invalid = id -> {
            String v = String.valueOf(id);
            return v.substring(0, v.length() / 2).repeat(2).equals(v);
        };
        System.out.println(input.ranges.stream()
                                       .flatMapToLong(r -> LongStream.rangeClosed(r.first, r.last))
                                       .filter(invalid)
                                       .sum());
    }

    private static void b(Input input) {
        LongPredicate invalid = id -> {
            String v = String.valueOf(id);
            return IntStream.rangeClosed(1, v.length() / 2).anyMatch(i -> v.substring(0, i).repeat(v.length() / i).equals(v));
        };
        System.out.println(input.ranges.stream()
                                       .flatMapToLong(r -> LongStream.rangeClosed(r.first, r.last))
                                       .filter(invalid)
                                       .sum());
    }

    record Range(long first, long last) {
    }

    record Input(List<Range> ranges) {
        static Input parse(String input) {
            return new Input(input.lines()
                                  .flatMap(l -> Stream.of(l.split(",")))
                                  .map(r -> new Range(
                                          Long.parseLong(r.substring(0, r.indexOf('-'))),
                                          Long.parseLong(r.substring(r.indexOf('-') + 1))))
                                  .toList());
        }
    }

    private static final String TEST_INPUT = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,\n"
                                             + "1698522-1698528,446443-446449,38593856-38593862,565653-565659,\n"
                                             + "824824821-824824827,2121212118-2121212124";

    private static final String INPUT =
            "52-75,71615244-71792700,89451761-89562523,594077-672686,31503-39016,733-976,1-20,400309-479672,458-635,836793365-836858811,3395595155-3395672258,290-391,5168-7482,4545413413-4545538932,65590172-65702074,25-42,221412-256187,873499-1078482,118-154,68597355-68768392,102907-146478,4251706-4487069,64895-87330,8664371543-8664413195,4091-5065,537300-565631,77-115,83892238-83982935,6631446-6694349,1112-1649,7725-9776,1453397-1493799,10240-12328,15873-20410,1925-2744,4362535948-4362554186,3078725-3256936,710512-853550,279817-346202,45515-60928,3240-3952";

    public static void main(String[] args) {
        Util.time(() -> a(Input.parse(TEST_INPUT)));
        Util.time(() -> a(Input.parse(INPUT)));
        Util.time(() -> b(Input.parse(TEST_INPUT)));
        Util.time(() -> b(Input.parse(INPUT)));
    }
}
