package aoc2018;

public class Day6 {
    public static void main(String[] args) {
        a();
        b();
    }

    private static void b() {
        String[] lines = Util.parseStrings(INPUT);
        int maxX = 0;
        int maxY = 0;
        Point[] points = new Point[lines.length];
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            Point point = Point.parse(line);
            points[i] = point;
            maxX = Math.max(maxX, point.x);
            maxY = Math.max(maxY, point.y);
        }

        int regionSize = 0;
        for (int x = 0; x <= maxX; x++) {
            for (int y = 0; y <= maxY; y++) {
                int sumDistance = 0;
                for (Point point : points) {
                    sumDistance += point.distance(x, y);
                }
                if (sumDistance < 10_000) {
                    regionSize++;
                }
            }
        }
        System.out.println(regionSize);
    }

    private static void a() {
        String[] lines = Util.parseStrings(INPUT);
        int maxX = 0;
        int maxY = 0;
        Point[] points = new Point[lines.length];
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            Point point = Point.parse(line);
            points[i] = point;
            maxX = Math.max(maxX, point.x);
            maxY = Math.max(maxY, point.y);
        }

        for (int i = 0; i < points.length; i++) {
            points[i].setIsRand(points);
        }

        for (int x = -maxX; x <= 2*maxX; x++) {
            for (int y = -maxY; y <= 2*maxY; y++) {
                int minDistance = Integer.MAX_VALUE;
                int closestIx = -1;
                for (int i = 0; i < points.length; i++) {
                    Point point = points[i];
                    int distance = point.distance(x, y);
                    if (distance < minDistance) {
                        minDistance = distance;
                        closestIx = i;
                    } else if (distance == minDistance) {
                        closestIx = -1;
                    }
                }
                if (closestIx != -1) {
                    points[closestIx].closest++;
                }
            }
        }

        int maxClosest = 0;
        for (Point point : points) {
            if (!point.randPoint) {
                maxClosest = Math.max(maxClosest, point.closest);
            }
        }
        System.out.println(maxClosest);
    }

    private static class Point {
        final int x;
        final int y;
        int closest;
        boolean randPoint;

        private Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        static Point parse(String data) {
            String[] xy = data.split(", ");
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            return new Point(x, y);
        }

        public int distance(int x, int y) {
            return Math.abs(this.x - x) + Math.abs(this.y - y);
        }

        public void setIsRand(Point[] points) {
            this.randPoint = testIsRand(points, 100_000, 0) || testIsRand(points, -100_000, 0) ||
                             testIsRand(points, 0, 100_000) || testIsRand(points, 0, -100_000);
        }

        boolean testIsRand(Point[] points, int x, int y) {
            int testX = this.x  + x;
            int testY = this.y + y;
            int myDistance = distance(testX, testY);
            for (Point point : points) {
                if (point != this && point.distance(testX, testY) <= myDistance) {
                 return false;
                }
            }
            return true;
        }
    }
    // D is closest to 9 locations, and E is closest to 17
    private static final String TEST_INPUT = "1, 1\n"
                                             + "1, 6\n"
                                             + "8, 3\n"
                                             + "3, 4\n"
                                             + "5, 5\n"
                                             + "8, 9";

    private static final String INPUT = "357, 59\n"
                                        + "312, 283\n"
                                        + "130, 47\n"
                                        + "89, 87\n"
                                        + "87, 58\n"
                                        + "158, 169\n"
                                        + "182, 183\n"
                                        + "300, 318\n"
                                        + "82, 257\n"
                                        + "200, 194\n"
                                        + "71, 259\n"
                                        + "112, 67\n"
                                        + "82, 163\n"
                                        + "107, 302\n"
                                        + "58, 194\n"
                                        + "40, 88\n"
                                        + "288, 339\n"
                                        + "64, 245\n"
                                        + "243, 302\n"
                                        + "41, 43\n"
                                        + "147, 276\n"
                                        + "143, 116\n"
                                        + "103, 178\n"
                                        + "262, 226\n"
                                        + "253, 157\n"
                                        + "313, 71\n"
                                        + "202, 236\n"
                                        + "353, 192\n"
                                        + "96, 74\n"
                                        + "167, 50\n"
                                        + "125, 132\n"
                                        + "90, 315\n"
                                        + "174, 232\n"
                                        + "185, 237\n"
                                        + "126, 134\n"
                                        + "152, 191\n"
                                        + "104, 315\n"
                                        + "283, 90\n"
                                        + "95, 193\n"
                                        + "252, 286\n"
                                        + "48, 166\n"
                                        + "69, 75\n"
                                        + "48, 349\n"
                                        + "59, 124\n"
                                        + "334, 95\n"
                                        + "263, 134\n"
                                        + "50, 314\n"
                                        + "196, 66\n"
                                        + "342, 221\n"
                                        + "60, 217";
}
