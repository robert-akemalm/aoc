package aoc2018;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class Day10 {
    public static void main(String[] args) {
        String input = INPUT;
        a(input);
        b(input);
    }

    private static void b(String input) {

    }

    private static void a(String input) {
        String[] lines = Util.parseStrings(input);
        Point[] points = new Point[lines.length];
        for (int i = 0; i < lines.length; i++) {
            points[i] = Point.parse(lines[i]);
        }
        Set<Point> seen = new HashSet<>();
        int time = 0;
        while (true) {
            seen.clear();
            time++;
            for (Point p : points) {
                p.move();
                seen.add(p);
            }

            if (inCLuster(points, seen)) {
                System.out.println(time);
                print(points);
                break;
            }
        }

    }

    private static boolean inCLuster(Point[] points, Set<Point> seen) {
        for (Point p : points) {
            boolean inCluster = inCLuster(p, seen);
            if(!inCluster) {
                return false;
            }
        }
        return true;

    }

    private static Point lookUp = new Point(0, 0);
    private static boolean inCLuster(Point p, Set<Point> points) {
        for (int y = -1; y <= 1 ; y++) {
            for (int x = -1; x <= 1 ; x++) {
                if (y == 0 && x == 0) {
                    continue;
                }
                lookUp.x = p.x - x;
                lookUp.y = p.y - y;
                if (points.contains(lookUp)) {
                    return true;
                }
            }
        }
        return false;
    }

    static void print(Point[] points) {
        int minX = Stream.of(points).mapToInt(p -> p.x).min().getAsInt();
        int maxX = Stream.of(points).mapToInt(p -> p.x).max().getAsInt();
        int minY = Stream.of(points).mapToInt(p -> p.y).min().getAsInt();
        int maxY = Stream.of(points).mapToInt(p -> p.y).max().getAsInt();

        StringBuilder sb = new StringBuilder();
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                char c = '.';
                   for (Point p : points) {
                       if (p.x == x && p.y == y) {
                           c = '#';
                       }
                   }
                   sb.append(c);
            }
            sb.append('\n');
        }

        sb.append('\n');
        System.out.println(sb);
    }
    static class Point {
        int x;
        int y;
        final int velX;
        final int velY;

        Point(int velX, int velY) {
            this.velX = velX;
            this.velY = velY;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Point point = (Point) o;
            return x == point.x &&
                   y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        public static Point parse(String input) {
            int x = Integer.parseInt(input.substring(input.indexOf('<') + 1, input.indexOf(',')).trim());
            int y = Integer.parseInt(input.substring(input.indexOf(',') + 1, input.indexOf('>')).trim());
            int velX = Integer.parseInt(input.substring(input.lastIndexOf('<') + 1, input.lastIndexOf(',')).trim());
            int velY = Integer.parseInt(input.substring(input.lastIndexOf(',') + 1, input.lastIndexOf('>')).trim());
            Point p = new Point(velX, velY);
            p.x = x;
            p.y = y;
            return p;
        }

        public void move() {
            x+= velX;
            y += velY;
        }
    }
    private static final String TEST_INPUT = "position=< 9,  1> velocity=< 0,  2>\n"
                                             + "position=< 7,  0> velocity=<-1,  0>\n"
                                             + "position=< 3, -2> velocity=<-1,  1>\n"
                                             + "position=< 6, 10> velocity=<-2, -1>\n"
                                             + "position=< 2, -4> velocity=< 2,  2>\n"
                                             + "position=<-6, 10> velocity=< 2, -2>\n"
                                             + "position=< 1,  8> velocity=< 1, -1>\n"
                                             + "position=< 1,  7> velocity=< 1,  0>\n"
                                             + "position=<-3, 11> velocity=< 1, -2>\n"
                                             + "position=< 7,  6> velocity=<-1, -1>\n"
                                             + "position=<-2,  3> velocity=< 1,  0>\n"
                                             + "position=<-4,  3> velocity=< 2,  0>\n"
                                             + "position=<10, -3> velocity=<-1,  1>\n"
                                             + "position=< 5, 11> velocity=< 1, -2>\n"
                                             + "position=< 4,  7> velocity=< 0, -1>\n"
                                             + "position=< 8, -2> velocity=< 0,  1>\n"
                                             + "position=<15,  0> velocity=<-2,  0>\n"
                                             + "position=< 1,  6> velocity=< 1,  0>\n"
                                             + "position=< 8,  9> velocity=< 0, -1>\n"
                                             + "position=< 3,  3> velocity=<-1,  1>\n"
                                             + "position=< 0,  5> velocity=< 0, -1>\n"
                                             + "position=<-2,  2> velocity=< 2,  0>\n"
                                             + "position=< 5, -2> velocity=< 1,  2>\n"
                                             + "position=< 1,  4> velocity=< 2,  1>\n"
                                             + "position=<-2,  7> velocity=< 2, -2>\n"
                                             + "position=< 3,  6> velocity=<-1, -1>\n"
                                             + "position=< 5,  0> velocity=< 1,  0>\n"
                                             + "position=<-6,  0> velocity=< 2,  0>\n"
                                             + "position=< 5,  9> velocity=< 1, -2>\n"
                                             + "position=<14,  7> velocity=<-2,  0>\n"
                                             + "position=<-3,  6> velocity=< 2, -1>";

    private static final String INPUT = "position=< 51730, -30721> velocity=<-5,  3>\n"
                                        + "position=< 41413, -30718> velocity=<-4,  3>\n"
                                        + "position=< 10546,  31104> velocity=<-1, -3>\n"
                                        + "position=< 31125, -41028> velocity=<-3,  4>\n"
                                        + "position=< 10522, -41024> velocity=<-1,  4>\n"
                                        + "position=<-20382, -30716> velocity=< 2,  3>\n"
                                        + "position=<-40971, -51332> velocity=< 4,  5>\n"
                                        + "position=<-51291, -20414> velocity=< 5,  2>\n"
                                        + "position=< 10522,  20803> velocity=<-1, -2>\n"
                                        + "position=<-51305,  10495> velocity=< 5, -1>\n"
                                        + "position=< 31170, -20420> velocity=<-3,  2>\n"
                                        + "position=< 41429,  31099> velocity=<-4, -3>\n"
                                        + "position=< 41426,  10493> velocity=<-4, -1>\n"
                                        + "position=< 41433, -20413> velocity=<-4,  2>\n"
                                        + "position=< 41455, -51328> velocity=<-4,  5>\n"
                                        + "position=<-10075, -51324> velocity=< 1,  5>\n"
                                        + "position=< 41434,  20799> velocity=<-4, -2>\n"
                                        + "position=< 10561,  20799> velocity=<-1, -2>\n"
                                        + "position=<-30707, -30716> velocity=< 3,  3>\n"
                                        + "position=< 10536,  31099> velocity=<-1, -3>\n"
                                        + "position=< 20821,  20801> velocity=<-2, -2>\n"
                                        + "position=<-30694, -30719> velocity=< 3,  3>\n"
                                        + "position=< 10522,  41403> velocity=<-1, -4>\n"
                                        + "position=< 31117, -10111> velocity=<-3,  1>\n"
                                        + "position=< 31109,  10494> velocity=<-3, -1>\n"
                                        + "position=<-30715, -41020> velocity=< 3,  4>\n"
                                        + "position=<-41017,  31103> velocity=< 4, -3>\n"
                                        + "position=< 20861,  51714> velocity=<-2, -5>\n"
                                        + "position=< 31125, -51325> velocity=<-3,  5>\n"
                                        + "position=< 20849, -10112> velocity=<-2,  1>\n"
                                        + "position=< 20829, -41027> velocity=<-2,  4>\n"
                                        + "position=<-10054, -51330> velocity=< 1,  5>\n"
                                        + "position=<-20399,  31100> velocity=< 2, -3>\n"
                                        + "position=< 31117,  31105> velocity=<-3, -3>\n"
                                        + "position=< 31113, -51333> velocity=<-3,  5>\n"
                                        + "position=< 31129,  51715> velocity=<-3, -5>\n"
                                        + "position=< 51761,  51712> velocity=<-5, -5>\n"
                                        + "position=< 51746,  31108> velocity=<-5, -3>\n"
                                        + "position=<-10107, -20420> velocity=< 1,  2>\n"
                                        + "position=<-51291, -51329> velocity=< 5,  5>\n"
                                        + "position=<-30691, -10114> velocity=< 3,  1>\n"
                                        + "position=< 31109,  51707> velocity=<-3, -5>\n"
                                        + "position=< 41418,  41404> velocity=<-4, -4>\n"
                                        + "position=< 41440, -51324> velocity=<-4,  5>\n"
                                        + "position=< 31112,  41407> velocity=<-3, -4>\n"
                                        + "position=< 51749, -41029> velocity=<-5,  4>\n"
                                        + "position=<-41019,  31104> velocity=< 4, -3>\n"
                                        + "position=< 41431, -41025> velocity=<-4,  4>\n"
                                        + "position=< 51773, -51333> velocity=<-5,  5>\n"
                                        + "position=< 41413, -41024> velocity=<-4,  4>\n"
                                        + "position=<-41016, -51329> velocity=< 4,  5>\n"
                                        + "position=<-40967,  41407> velocity=< 4, -4>\n"
                                        + "position=<-20398,  41411> velocity=< 2, -4>\n"
                                        + "position=< 20817,  20796> velocity=<-2, -2>\n"
                                        + "position=<-20379, -51327> velocity=< 2,  5>\n"
                                        + "position=<-40985, -10117> velocity=< 4,  1>\n"
                                        + "position=<-40990,  31108> velocity=< 4, -3>\n"
                                        + "position=<-41003,  20802> velocity=< 4, -2>\n"
                                        + "position=<-10086, -20418> velocity=< 1,  2>\n"
                                        + "position=< 41454, -41024> velocity=<-4,  4>\n"
                                        + "position=< 41458,  41406> velocity=<-4, -4>\n"
                                        + "position=<-10067,  51711> velocity=< 1, -5>\n"
                                        + "position=< 10549,  10498> velocity=<-1, -1>\n"
                                        + "position=<-40995, -41029> velocity=< 4,  4>\n"
                                        + "position=< 31118, -20416> velocity=<-3,  2>\n"
                                        + "position=< 10549,  10500> velocity=<-1, -1>\n"
                                        + "position=<-20393,  41407> velocity=< 2, -4>\n"
                                        + "position=< 51774,  10495> velocity=<-5, -1>\n"
                                        + "position=<-10063,  51708> velocity=< 1, -5>\n"
                                        + "position=< 20853,  31107> velocity=<-2, -3>\n"
                                        + "position=<-41019, -20416> velocity=< 4,  2>\n"
                                        + "position=< 10535, -10117> velocity=<-1,  1>\n"
                                        + "position=< 41446, -20421> velocity=<-4,  2>\n"
                                        + "position=<-40985, -41025> velocity=< 4,  4>\n"
                                        + "position=<-20395, -20412> velocity=< 2,  2>\n"
                                        + "position=< 51738, -10117> velocity=<-5,  1>\n"
                                        + "position=< 10546, -51325> velocity=<-1,  5>\n"
                                        + "position=<-10089, -20418> velocity=< 1,  2>\n"
                                        + "position=<-30715, -20415> velocity=< 3,  2>\n"
                                        + "position=<-40982, -30723> velocity=< 4,  3>\n"
                                        + "position=< 41437, -10114> velocity=<-4,  1>\n"
                                        + "position=<-51299, -20417> velocity=< 5,  2>\n"
                                        + "position=< 41463, -20417> velocity=<-4,  2>\n"
                                        + "position=< 41449,  10495> velocity=<-4, -1>\n"
                                        + "position=< 10511,  51712> velocity=<-1, -5>\n"
                                        + "position=<-51262, -30716> velocity=< 5,  3>\n"
                                        + "position=< 51718, -51333> velocity=<-5,  5>\n"
                                        + "position=< 31154,  10497> velocity=<-3, -1>\n"
                                        + "position=< 41437,  51711> velocity=<-4, -5>\n"
                                        + "position=< 20858, -30724> velocity=<-2,  3>\n"
                                        + "position=<-10095,  10492> velocity=< 1, -1>\n"
                                        + "position=< 41473, -20414> velocity=<-4,  2>\n"
                                        + "position=< 10549, -30717> velocity=<-1,  3>\n"
                                        + "position=< 10517,  31103> velocity=<-1, -3>\n"
                                        + "position=<-30667, -10114> velocity=< 3,  1>\n"
                                        + "position=< 31133,  10498> velocity=<-3, -1>\n"
                                        + "position=<-51282, -51332> velocity=< 5,  5>\n"
                                        + "position=< 31141, -51326> velocity=<-3,  5>\n"
                                        + "position=<-20363,  20797> velocity=< 2, -2>\n"
                                        + "position=<-20383, -20412> velocity=< 2,  2>\n"
                                        + "position=<-40982, -30724> velocity=< 4,  3>\n"
                                        + "position=<-20386,  51716> velocity=< 2, -5>\n"
                                        + "position=< 10525, -20421> velocity=<-1,  2>\n"
                                        + "position=<-51320, -51333> velocity=< 5,  5>\n"
                                        + "position=< 31162, -41027> velocity=<-3,  4>\n"
                                        + "position=<-51310, -51330> velocity=< 5,  5>\n"
                                        + "position=< 41461,  31108> velocity=<-4, -3>\n"
                                        + "position=< 10503, -30721> velocity=<-1,  3>\n"
                                        + "position=<-41003,  20799> velocity=< 4, -2>\n"
                                        + "position=< 31117,  41412> velocity=<-3, -4>\n"
                                        + "position=< 51717,  51716> velocity=<-5, -5>\n"
                                        + "position=< 20853,  51707> velocity=<-2, -5>\n"
                                        + "position=< 51766,  51707> velocity=<-5, -5>\n"
                                        + "position=< 20841, -10113> velocity=<-2,  1>\n"
                                        + "position=<-20371,  51714> velocity=< 2, -5>\n"
                                        + "position=< 10546,  31107> velocity=<-1, -3>\n"
                                        + "position=<-40963,  31103> velocity=< 4, -3>\n"
                                        + "position=<-20403, -30718> velocity=< 2,  3>\n"
                                        + "position=< 31141, -20420> velocity=<-3,  2>\n"
                                        + "position=<-20370,  41404> velocity=< 2, -4>\n"
                                        + "position=< 20865, -30725> velocity=<-2,  3>\n"
                                        + "position=< 20862,  20795> velocity=<-2, -2>\n"
                                        + "position=< 41445,  10492> velocity=<-4, -1>\n"
                                        + "position=< 51730, -51327> velocity=<-5,  5>\n"
                                        + "position=< 20842,  31101> velocity=<-2, -3>\n"
                                        + "position=<-51279,  51708> velocity=< 5, -5>\n"
                                        + "position=< 31121,  51712> velocity=<-3, -5>\n"
                                        + "position=< 31154, -10108> velocity=<-3,  1>\n"
                                        + "position=<-51275, -10115> velocity=< 5,  1>\n"
                                        + "position=< 31165, -20413> velocity=<-3,  2>\n"
                                        + "position=<-20376, -30721> velocity=< 2,  3>\n"
                                        + "position=<-10065,  41403> velocity=< 1, -4>\n"
                                        + "position=<-10107, -51325> velocity=< 1,  5>\n"
                                        + "position=<-20353,  41403> velocity=< 2, -4>\n"
                                        + "position=< 20837, -10117> velocity=<-2,  1>\n"
                                        + "position=< 51720,  41403> velocity=<-5, -4>\n"
                                        + "position=<-20363, -41024> velocity=< 2,  4>\n"
                                        + "position=<-30699,  31101> velocity=< 3, -3>\n"
                                        + "position=<-51286,  20798> velocity=< 5, -2>\n"
                                        + "position=<-20350,  31101> velocity=< 2, -3>\n"
                                        + "position=<-51310, -41027> velocity=< 5,  4>\n"
                                        + "position=<-51311, -51328> velocity=< 5,  5>\n"
                                        + "position=< 10512,  51712> velocity=<-1, -5>\n"
                                        + "position=<-30699, -10114> velocity=< 3,  1>\n"
                                        + "position=< 20818,  31105> velocity=<-2, -3>\n"
                                        + "position=< 41417,  41407> velocity=<-4, -4>\n"
                                        + "position=< 20863, -20421> velocity=<-2,  2>\n"
                                        + "position=< 41437,  20803> velocity=<-4, -2>\n"
                                        + "position=<-30699,  10500> velocity=< 3, -1>\n"
                                        + "position=<-51267,  31100> velocity=< 5, -3>\n"
                                        + "position=<-51307, -51333> velocity=< 5,  5>\n"
                                        + "position=< 31150, -41024> velocity=<-3,  4>\n"
                                        + "position=< 51719, -20421> velocity=<-5,  2>\n"
                                        + "position=< 31168,  51707> velocity=<-3, -5>\n"
                                        + "position=<-20352, -10113> velocity=< 2,  1>\n"
                                        + "position=<-20398,  20804> velocity=< 2, -2>\n"
                                        + "position=< 51743, -51324> velocity=<-5,  5>\n"
                                        + "position=< 20857,  51707> velocity=<-2, -5>\n"
                                        + "position=< 51774,  41403> velocity=<-5, -4>\n"
                                        + "position=<-10107, -20420> velocity=< 1,  2>\n"
                                        + "position=<-20363, -20417> velocity=< 2,  2>\n"
                                        + "position=< 31144,  41403> velocity=<-3, -4>\n"
                                        + "position=<-20355, -20414> velocity=< 2,  2>\n"
                                        + "position=<-10075,  51710> velocity=< 1, -5>\n"
                                        + "position=< 41426,  10494> velocity=<-4, -1>\n"
                                        + "position=<-20363, -30717> velocity=< 2,  3>\n"
                                        + "position=< 20837, -51324> velocity=<-2,  5>\n"
                                        + "position=< 10535, -10113> velocity=<-1,  1>\n"
                                        + "position=<-41000, -41024> velocity=< 4,  4>\n"
                                        + "position=< 10552,  51707> velocity=<-1, -5>\n"
                                        + "position=< 20864, -30725> velocity=<-2,  3>\n"
                                        + "position=< 10528, -10108> velocity=<-1,  1>\n"
                                        + "position=<-30694, -10113> velocity=< 3,  1>\n"
                                        + "position=< 51736, -10112> velocity=<-5,  1>\n"
                                        + "position=< 10510, -10116> velocity=<-1,  1>\n"
                                        + "position=< 51768, -41025> velocity=<-5,  4>\n"
                                        + "position=< 41469,  10494> velocity=<-4, -1>\n"
                                        + "position=< 41453, -20418> velocity=<-4,  2>\n"
                                        + "position=<-20371, -20419> velocity=< 2,  2>\n"
                                        + "position=<-40987,  10495> velocity=< 4, -1>\n"
                                        + "position=< 10533, -10115> velocity=<-1,  1>\n"
                                        + "position=<-40998, -20421> velocity=< 4,  2>\n"
                                        + "position=< 20833,  41412> velocity=<-2, -4>\n"
                                        + "position=< 10518,  51709> velocity=<-1, -5>\n"
                                        + "position=<-30654,  51716> velocity=< 3, -5>\n"
                                        + "position=< 51754, -20418> velocity=<-5,  2>\n"
                                        + "position=<-30674, -41028> velocity=< 3,  4>\n"
                                        + "position=< 10541,  10500> velocity=<-1, -1>\n"
                                        + "position=<-10047,  20795> velocity=< 1, -2>\n"
                                        + "position=< 41463,  31099> velocity=<-4, -3>\n"
                                        + "position=< 51733, -51328> velocity=<-5,  5>\n"
                                        + "position=<-41019, -10114> velocity=< 4,  1>\n"
                                        + "position=< 41439, -30716> velocity=<-4,  3>\n"
                                        + "position=< 20826,  51714> velocity=<-2, -5>\n"
                                        + "position=< 41437,  41411> velocity=<-4, -4>\n"
                                        + "position=<-30667, -41029> velocity=< 3,  4>\n"
                                        + "position=< 41429,  20803> velocity=<-4, -2>\n"
                                        + "position=< 41469,  10493> velocity=<-4, -1>\n"
                                        + "position=< 51736,  41409> velocity=<-5, -4>\n"
                                        + "position=< 31168, -51328> velocity=<-3,  5>\n"
                                        + "position=<-30715,  20795> velocity=< 3, -2>\n"
                                        + "position=<-30715, -30723> velocity=< 3,  3>\n"
                                        + "position=< 20816,  41408> velocity=<-2, -4>\n"
                                        + "position=<-30702, -51330> velocity=< 3,  5>\n"
                                        + "position=< 51762,  41410> velocity=<-5, -4>\n"
                                        + "position=<-30659, -51329> velocity=< 3,  5>\n"
                                        + "position=< 51759,  51712> velocity=<-5, -5>\n"
                                        + "position=< 10525, -41025> velocity=<-1,  4>\n"
                                        + "position=< 51738, -20418> velocity=<-5,  2>\n"
                                        + "position=<-30715, -51329> velocity=< 3,  5>\n"
                                        + "position=<-30683, -41025> velocity=< 3,  4>\n"
                                        + "position=< 31159, -20421> velocity=<-3,  2>\n"
                                        + "position=< 20861, -10117> velocity=<-2,  1>\n"
                                        + "position=<-20387,  31105> velocity=< 2, -3>\n"
                                        + "position=<-51302,  10497> velocity=< 5, -1>\n"
                                        + "position=< 51778, -30723> velocity=<-5,  3>\n"
                                        + "position=< 41425, -20416> velocity=<-4,  2>\n"
                                        + "position=< 10538,  41405> velocity=<-1, -4>\n"
                                        + "position=<-10051,  20804> velocity=< 1, -2>\n"
                                        + "position=<-40963, -20416> velocity=< 4,  2>\n"
                                        + "position=<-51302, -51331> velocity=< 5,  5>\n"
                                        + "position=< 41455,  10491> velocity=<-4, -1>\n"
                                        + "position=<-20371, -51328> velocity=< 2,  5>\n"
                                        + "position=<-10075,  20800> velocity=< 1, -2>\n"
                                        + "position=<-51263,  41410> velocity=< 5, -4>\n"
                                        + "position=<-30704, -51328> velocity=< 3,  5>\n"
                                        + "position=< 51766,  20799> velocity=<-5, -2>\n"
                                        + "position=< 20841,  41403> velocity=<-2, -4>\n"
                                        + "position=<-30711, -51329> velocity=< 3,  5>\n"
                                        + "position=< 10537, -30721> velocity=<-1,  3>\n"
                                        + "position=< 10557,  51712> velocity=<-1, -5>\n"
                                        + "position=< 51728, -10117> velocity=<-5,  1>\n"
                                        + "position=< 20866, -10114> velocity=<-2,  1>\n"
                                        + "position=< 20818,  20801> velocity=<-2, -2>\n"
                                        + "position=< 41461, -51330> velocity=<-4,  5>\n"
                                        + "position=<-10064,  10491> velocity=< 1, -1>\n"
                                        + "position=<-10083, -51328> velocity=< 1,  5>\n"
                                        + "position=<-10107, -20421> velocity=< 1,  2>\n"
                                        + "position=< 10502, -41029> velocity=<-1,  4>\n"
                                        + "position=<-51283,  20803> velocity=< 5, -2>\n"
                                        + "position=< 41437, -10108> velocity=<-4,  1>\n"
                                        + "position=<-30667, -20414> velocity=< 3,  2>\n"
                                        + "position=< 41469,  31101> velocity=<-4, -3>\n"
                                        + "position=<-51304,  20801> velocity=< 5, -2>\n"
                                        + "position=< 51730,  10500> velocity=<-5, -1>\n"
                                        + "position=<-10102,  10494> velocity=< 1, -1>\n"
                                        + "position=<-51262,  41412> velocity=< 5, -4>\n"
                                        + "position=<-41015, -51333> velocity=< 4,  5>\n"
                                        + "position=<-40971, -30724> velocity=< 4,  3>\n"
                                        + "position=< 51765, -10108> velocity=<-5,  1>\n"
                                        + "position=<-10094,  31103> velocity=< 1, -3>\n"
                                        + "position=< 10541,  20797> velocity=<-1, -2>\n"
                                        + "position=< 20845, -30720> velocity=<-2,  3>\n"
                                        + "position=< 31113,  10495> velocity=<-3, -1>\n"
                                        + "position=<-20387, -30720> velocity=< 2,  3>\n"
                                        + "position=<-20403, -30721> velocity=< 2,  3>\n"
                                        + "position=< 41429, -41029> velocity=<-4,  4>\n"
                                        + "position=< 10517,  10500> velocity=<-1, -1>\n"
                                        + "position=< 51722, -20420> velocity=<-5,  2>\n"
                                        + "position=< 41445, -10117> velocity=<-4,  1>\n"
                                        + "position=< 51730,  41405> velocity=<-5, -4>\n"
                                        + "position=< 31141,  31102> velocity=<-3, -3>\n"
                                        + "position=< 51774,  41403> velocity=<-5, -4>\n"
                                        + "position=< 20849, -20416> velocity=<-2,  2>\n"
                                        + "position=<-20361, -20421> velocity=< 2,  2>\n"
                                        + "position=< 10553,  10495> velocity=<-1, -1>\n"
                                        + "position=<-10064,  20795> velocity=< 1, -2>\n"
                                        + "position=< 31109,  20797> velocity=<-3, -2>\n"
                                        + "position=<-30696,  10496> velocity=< 3, -1>\n"
                                        + "position=< 41418, -20420> velocity=<-4,  2>\n"
                                        + "position=<-20410, -10117> velocity=< 2,  1>\n"
                                        + "position=< 10558,  10495> velocity=<-1, -1>\n"
                                        + "position=< 20805,  41409> velocity=<-2, -4>\n"
                                        + "position=< 10509, -41026> velocity=<-1,  4>\n"
                                        + "position=< 10553,  41403> velocity=<-1, -4>\n"
                                        + "position=<-30654,  41411> velocity=< 3, -4>\n"
                                        + "position=<-40976,  10496> velocity=< 4, -1>\n"
                                        + "position=<-10099, -41027> velocity=< 1,  4>\n"
                                        + "position=< 10501, -10114> velocity=<-1,  1>\n"
                                        + "position=< 10559,  51711> velocity=<-1, -5>\n"
                                        + "position=< 41474,  51710> velocity=<-4, -5>\n"
                                        + "position=<-51303, -51326> velocity=< 5,  5>\n"
                                        + "position=<-30656, -10112> velocity=< 3,  1>\n"
                                        + "position=< 51768,  41407> velocity=<-5, -4>\n"
                                        + "position=< 51741, -41020> velocity=<-5,  4>\n"
                                        + "position=<-10094, -51325> velocity=< 1,  5>\n"
                                        + "position=<-30699,  31106> velocity=< 3, -3>\n"
                                        + "position=< 51741, -10112> velocity=<-5,  1>\n"
                                        + "position=< 41434, -30724> velocity=<-4,  3>\n"
                                        + "position=< 41450, -30722> velocity=<-4,  3>\n"
                                        + "position=< 20853, -51328> velocity=<-2,  5>\n"
                                        + "position=< 31149, -51329> velocity=<-3,  5>\n"
                                        + "position=<-10051,  51712> velocity=< 1, -5>\n"
                                        + "position=< 20837,  51709> velocity=<-2, -5>\n"
                                        + "position=< 41437, -41022> velocity=<-4,  4>\n"
                                        + "position=<-41019, -10110> velocity=< 4,  1>\n"
                                        + "position=<-20398,  51714> velocity=< 2, -5>\n"
                                        + "position=<-20387,  10491> velocity=< 2, -1>\n"
                                        + "position=< 10525,  31108> velocity=<-1, -3>\n"
                                        + "position=<-40966,  51710> velocity=< 4, -5>\n"
                                        + "position=<-10090,  10493> velocity=< 1, -1>\n"
                                        + "position=< 10557, -51327> velocity=<-1,  5>\n"
                                        + "position=<-51275,  10491> velocity=< 5, -1>\n"
                                        + "position=<-30673, -51333> velocity=< 3,  5>\n"
                                        + "position=<-10107, -20413> velocity=< 1,  2>\n"
                                        + "position=<-10094,  51711> velocity=< 1, -5>\n"
                                        + "position=<-40958,  51708> velocity=< 4, -5>\n"
                                        + "position=< 31130,  41404> velocity=<-3, -4>\n"
                                        + "position=<-10055, -51329> velocity=< 1,  5>\n"
                                        + "position=< 51737,  41411> velocity=<-5, -4>\n"
                                        + "position=< 20822, -30723> velocity=<-2,  3>\n"
                                        + "position=< 31137,  20804> velocity=<-3, -2>\n"
                                        + "position=< 20818,  31104> velocity=<-2, -3>\n"
                                        + "position=< 41458, -20419> velocity=<-4,  2>\n"
                                        + "position=< 41434, -30718> velocity=<-4,  3>\n"
                                        + "position=<-51318, -41027> velocity=< 5,  4>\n"
                                        + "position=< 41416,  51707> velocity=<-4, -5>\n"
                                        + "position=<-51275,  31102> velocity=< 5, -3>\n"
                                        + "position=< 20845, -30719> velocity=<-2,  3>\n"
                                        + "position=< 10546,  20801> velocity=<-1, -2>\n"
                                        + "position=< 20826,  51716> velocity=<-2, -5>\n"
                                        + "position=<-41019, -41021> velocity=< 4,  4>\n"
                                        + "position=< 31152, -10112> velocity=<-3,  1>\n"
                                        + "position=< 10557,  31101> velocity=<-1, -3>\n"
                                        + "position=< 41458,  41407> velocity=<-4, -4>\n"
                                        + "position=< 41429,  10496> velocity=<-4, -1>\n"
                                        + "position=<-30659, -10108> velocity=< 3,  1>\n"
                                        + "position=< 51767,  41407> velocity=<-5, -4>\n"
                                        + "position=< 51752, -41025> velocity=<-5,  4>\n"
                                        + "position=<-41017,  31099> velocity=< 4, -3>\n"
                                        + "position=< 10534, -51329> velocity=<-1,  5>\n"
                                        + "position=<-10075, -20418> velocity=< 1,  2>\n"
                                        + "position=< 41423, -10112> velocity=<-4,  1>\n"
                                        + "position=<-51286,  10492> velocity=< 5, -1>\n"
                                        + "position=<-51315,  20800> velocity=< 5, -2>\n"
                                        + "position=< 20850,  51715> velocity=<-2, -5>\n"
                                        + "position=<-20398, -51325> velocity=< 2,  5>\n"
                                        + "position=< 51776, -41024> velocity=<-5,  4>\n"
                                        + "position=<-30667,  51713> velocity=< 3, -5>\n"
                                        + "position=< 10561, -30719> velocity=<-1,  3>\n"
                                        + "position=< 10512, -41029> velocity=<-1,  4>\n"
                                        + "position=< 31169, -30718> velocity=<-3,  3>\n"
                                        + "position=< 51722,  51709> velocity=<-5, -5>\n"
                                        + "position=<-51315,  41412> velocity=< 5, -4>\n"
                                        + "position=<-51299, -41028> velocity=< 5,  4>\n"
                                        + "position=< 20826,  41408> velocity=<-2, -4>\n"
                                        + "position=<-20371, -10114> velocity=< 2,  1>\n"
                                        + "position=< 41429,  41405> velocity=<-4, -4>\n"
                                        + "position=< 20853, -51328> velocity=<-2,  5>\n"
                                        + "position=<-51286,  20796> velocity=< 5, -2>\n"
                                        + "position=<-51291,  10496> velocity=< 5, -1>\n"
                                        + "position=< 20826, -20413> velocity=<-2,  2>\n"
                                        + "position=< 20845,  20798> velocity=<-2, -2>\n"
                                        + "position=<-10048, -10113> velocity=< 1,  1>\n"
                                        + "position=<-10099, -30717> velocity=< 1,  3>\n"
                                        + "position=<-41009, -20421> velocity=< 4,  2>\n"
                                        + "position=< 31110,  41407> velocity=<-3, -4>\n"
                                        + "position=< 20861, -51326> velocity=<-2,  5>\n"
                                        + "position=< 20826, -20413> velocity=<-2,  2>\n"
                                        + "position=<-10055,  31099> velocity=< 1, -3>\n"
                                        + "position=< 20837, -51325> velocity=<-2,  5>\n"
                                        + "position=< 20866,  51710> velocity=<-2, -5>\n"
                                        + "position=< 20858, -20420> velocity=<-2,  2>\n"
                                        + "position=< 31130, -51332> velocity=<-3,  5>\n"
                                        + "position=<-10074, -51333> velocity=< 1,  5>\n"
                                        + "position=< 20813,  10495> velocity=<-2, -1>\n"
                                        + "position=< 31162, -51331> velocity=<-3,  5>\n"
                                        + "position=<-30698,  51708> velocity=< 3, -5>";
}
