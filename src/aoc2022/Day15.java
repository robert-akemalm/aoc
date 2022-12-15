package aoc2022;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aoc2022.Util.Pos;

public class Day15 {
    public static void main(String[] args) {
        a(TEST_INPUT, 10);
        a(INPUT, 2_000_000);
        fasterA(TEST_INPUT, 10);
        fasterA(INPUT, 2_000_000);
        b(TEST_INPUT, 20);
        b(INPUT, 4_000_000);
    }

    private static void fasterA(String input, int y) {
        Data data = Data.parse(input);
        Ranges ranges = new Ranges();
        for (Sensor sensor : data.sensors.values()) {
            Pos pos = sensor.pos;
            int distance = pos.manhattanDistanceTo(sensor.closestBeacon.pos) - Math.abs(pos.y() - y);
            if (distance > 0) {
                ranges.add(sensor.rangeX(y, pos.x() - distance, pos.x() + distance));
            }
        }
        int cnt = 0;
        for (Range range : ranges.ranges) {
            for (int x = range.min; x <= range.max ; x++) {
                if (!data.beacons.containsKey(new Pos(x, y))) {
                    cnt++;
                }
            }
        }
        System.out.println(cnt);
    }

    private static void a(String input, int y) {
        Data data = Data.parse(input);
        Set<Pos> covered = new HashSet<>();
        for (Sensor sensor : data.sensors.values()) {
            covered.addAll(sensor.coveredPos(y));
        }
        covered.removeAll(data.beacons.keySet());
        long cnt = covered.stream().filter(p -> p.y() == y).count();
        System.out.println(cnt);
    }

    private static void b(String input, final int max) {
        Data data = Data.parse(input);
        Pos beacon = findNotCovered(data.sensors.values(), max);
        BigInteger res = BigInteger.valueOf(beacon.x()).multiply(BigInteger.valueOf(4_000_000));
        System.out.println(res.add(BigInteger.valueOf(beacon.y())));
    }

    private static Pos findNotCovered(Collection<Sensor> sensors, int max) {
        int min = 0;
        for (int y = min; y <= max; y++) {
            Ranges ranges = new Ranges();
            for (Sensor sensor : sensors) {
                Range sensorRange = sensor.rangeX(y, min, max);
                if (sensorRange != Range.NONE) {
                    ranges.add(sensorRange);
                }
            }

            if (ranges.size() == 1) {
                Range range = ranges.get(0);
                if (range.min == min + 1 && range.max == max) {
                    return new Pos(min, y);
                }
                if (range.min == min && range.max == max - 1) {
                    return new Pos(max, y);
                }
            } else if (ranges.size() == 2) {
                Range a = ranges.get(0);
                Range b = ranges.get(1);
                if (a.min == min && a.max + 2 == b.min && b.max == max) {
                    return new Pos(a.max + 1, y);
                }
                if (b.min == min && b.max + 2 == a.min && a.max == max) {
                    return new Pos(b.max + 1, y);
                }
            }
        }
        return null;
    }

    private static class Ranges {
        final List<Range> ranges = new ArrayList<>();

        void add(Range sensorRange) {
            int firstOverlap = findFirstOverlap(sensorRange);
            if (firstOverlap == -1) {
                ranges.add(sensorRange);
            } else {
                Range stored = ranges.get(firstOverlap);
                stored.add(sensorRange);
                for (firstOverlap = findFirstOverlap(stored); firstOverlap != -1; firstOverlap = findFirstOverlap(stored)) {
                    Range overlap = ranges.get(firstOverlap);
                    stored.add(overlap);
                    ranges.remove(firstOverlap);
                }
            }
        }

        private int findFirstOverlap(Range range) {
            for (int j = 0; j < ranges.size(); j++) {
                Range r = ranges.get(j);
                if (r != range && range.overlaps(r)) {
                    return j;
                }
            }
            return -1;
        }

        public int size() {
            return ranges.size();
        }

        public Range get(int index) {
            return ranges.get(index);
        }
    }

    private static class Range {
        static final Range NONE = new Range(-1, -1);
        int min;
        int max;

        public Range(int min, int max) {
            this.min = min;
            this.max = max;
        }

        boolean overlaps(Range other) {
            return !(max < other.min || other.max < min);
        }

        void add(Range other) {
            min = Math.min(min, other.min);
            max = Math.max(max, other.max);
        }
    }

    private record Data(Map<Pos, Sensor> sensors, Map<Pos, Beacon> beacons) {
        static Data parse(String input) {
            Map<Pos, Beacon> beacons = new HashMap<>();
            Map<Pos, Sensor> sensors = new HashMap<>();
            for (String row : input.split("\n")) {
                String[] parts = row.split(":");
                int[] beaconCoordinates = Util.extractInts(parts[1]);
                Beacon beacon = new Beacon(new Pos(beaconCoordinates[0], beaconCoordinates[1]));
                beacons.put(beacon.pos, beacon);
                int[] sensorCoordinates = Util.extractInts(parts[0]);
                Pos sensorPos = new Pos(sensorCoordinates[0], sensorCoordinates[1]);
                sensors.put(sensorPos, new Sensor(sensorPos, beacon));
            }
            return new Data(sensors, beacons);
        }
    }

    private record Sensor(Pos pos, Beacon closestBeacon) {
        public Set<Pos> coveredPos(int y) {
            int distance = pos.manhattanDistanceTo(closestBeacon.pos) - Math.abs(pos.y() - y);
            Set<Pos> covered = new HashSet<>();
            for (int x = -distance; x <= distance; x++) {
                covered.add(new Pos(pos.x() + x, y));
            }
            return covered;
        }

        public Range rangeX(int y, int min, int max) {
            int distance = pos.manhattanDistanceTo(closestBeacon.pos) - Math.abs(pos.y() - y);
            min = Math.max(min, pos.x() - distance);
            max = Math.min(max, pos.x() + distance);
            return max < min ? Range.NONE : new Range(min, max);
        }
    }

    private record Beacon(Pos pos) {
    }

    private static final String TEST_INPUT = "Sensor at x=2, y=18: closest beacon is at x=-2, y=15\n"
                                             + "Sensor at x=9, y=16: closest beacon is at x=10, y=16\n"
                                             + "Sensor at x=13, y=2: closest beacon is at x=15, y=3\n"
                                             + "Sensor at x=12, y=14: closest beacon is at x=10, y=16\n"
                                             + "Sensor at x=10, y=20: closest beacon is at x=10, y=16\n"
                                             + "Sensor at x=14, y=17: closest beacon is at x=10, y=16\n"
                                             + "Sensor at x=8, y=7: closest beacon is at x=2, y=10\n"
                                             + "Sensor at x=2, y=0: closest beacon is at x=2, y=10\n"
                                             + "Sensor at x=0, y=11: closest beacon is at x=2, y=10\n"
                                             + "Sensor at x=20, y=14: closest beacon is at x=25, y=17\n"
                                             + "Sensor at x=17, y=20: closest beacon is at x=21, y=22\n"
                                             + "Sensor at x=16, y=7: closest beacon is at x=15, y=3\n"
                                             + "Sensor at x=14, y=3: closest beacon is at x=15, y=3\n"
                                             + "Sensor at x=20, y=1: closest beacon is at x=15, y=3";

    private static final String INPUT = "Sensor at x=251234, y=759482: closest beacon is at x=-282270, y=572396\n"
                                        + "Sensor at x=2866161, y=3374117: closest beacon is at x=2729330, y=3697325\n"
                                        + "Sensor at x=3999996, y=3520742: closest beacon is at x=3980421, y=3524442\n"
                                        + "Sensor at x=3988282, y=3516584: closest beacon is at x=3980421, y=3524442\n"
                                        + "Sensor at x=3005586, y=3018139: closest beacon is at x=2727127, y=2959718\n"
                                        + "Sensor at x=3413653, y=3519082: closest beacon is at x=3980421, y=3524442\n"
                                        + "Sensor at x=2900403, y=187208: closest beacon is at x=2732772, y=2000000\n"
                                        + "Sensor at x=1112429, y=3561166: closest beacon is at x=2729330, y=3697325\n"
                                        + "Sensor at x=3789925, y=3283328: closest beacon is at x=3980421, y=3524442\n"
                                        + "Sensor at x=3991533, y=3529053: closest beacon is at x=3980421, y=3524442\n"
                                        + "Sensor at x=3368119, y=2189371: closest beacon is at x=2732772, y=2000000\n"
                                        + "Sensor at x=2351157, y=2587083: closest beacon is at x=2727127, y=2959718\n"
                                        + "Sensor at x=3326196, y=2929990: closest beacon is at x=3707954, y=2867627\n"
                                        + "Sensor at x=3839244, y=1342691: closest beacon is at x=3707954, y=2867627\n"
                                        + "Sensor at x=2880363, y=3875503: closest beacon is at x=2729330, y=3697325\n"
                                        + "Sensor at x=1142859, y=1691416: closest beacon is at x=2732772, y=2000000\n"
                                        + "Sensor at x=3052449, y=2711719: closest beacon is at x=2727127, y=2959718\n"
                                        + "Sensor at x=629398, y=214610: closest beacon is at x=-282270, y=572396\n"
                                        + "Sensor at x=3614706, y=3924106: closest beacon is at x=3980421, y=3524442\n"
                                        + "Sensor at x=3999246, y=2876762: closest beacon is at x=3707954, y=2867627\n"
                                        + "Sensor at x=3848935, y=3020496: closest beacon is at x=3707954, y=2867627\n"
                                        + "Sensor at x=123637, y=2726215: closest beacon is at x=-886690, y=3416197\n"
                                        + "Sensor at x=4000000, y=3544014: closest beacon is at x=3980421, y=3524442\n"
                                        + "Sensor at x=2524955, y=3861248: closest beacon is at x=2729330, y=3697325\n"
                                        + "Sensor at x=2605475, y=3152151: closest beacon is at x=2727127, y=2959718";
}
