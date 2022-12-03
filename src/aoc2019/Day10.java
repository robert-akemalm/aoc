package aoc2019;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day10 {
    public static void main(String[] args) {
        String input = INPUT;
        a(input);
        b(input);
    }

    private static void b(String input) {
        String[] lines = input.split("\n");
        int[][] map = new int[lines.length][];
        boolean[][] asteroidsOnMap = new boolean[lines.length][];
        List<Asteroid> asteroids = new ArrayList<>();
        for (int y = 0; y < lines.length; y++) {
            map[y] = new int[lines[y].length()];
            asteroidsOnMap[y] = new boolean[lines[y].length()];
            for (int x = 0; x < lines[y].length(); x++) {
                if (lines[y].charAt(x) == '#') {
                    asteroidsOnMap[y][x] = true;
                    asteroids.add(new Asteroid(x, y));
                }
            }
        }
        int max = 0;
        Asteroid best = null;
        for (Asteroid a : asteroids) {
            Set<Double> seenAngles = new HashSet<>();
            for (Asteroid b : asteroids) {
                if (a != b) {
                    double angle = Math.atan2(a.y - b.y, a.x - b.x);
                    seenAngles.add(angle);
                }
            }
            if (seenAngles.size() > max) {
                max = seenAngles.size();
                best = a;
            }
        }
        final Asteroid bestAsteroid = best;
        final Asteroid z = new Asteroid(12, 1);
        double angle = (Math.atan2(bestAsteroid.y-z.y, bestAsteroid.x-z.x) + 3 * Math.PI / 2) % (2 * Math.PI);
        System.out.println(angle);
        if (angle < 7) {
//            return;
        }

        System.out.println("Best " + best.x + ", " + best.y);
        asteroids.remove(best);
        List<Asteroid> sorted = new ArrayList<>(asteroids);
        sorted.sort(new Comparator<Asteroid>() {
            @Override
            public int compare(Asteroid a, Asteroid b) {
                double angleA = (Math.atan2(bestAsteroid.y-a.y, bestAsteroid.x-a.x) + 3 * Math.PI / 2) % (2 * Math.PI);
                double angleB = (Math.atan2(bestAsteroid.y-b.y, bestAsteroid.x-b.x) + 3 * Math.PI / 2) % (2 * Math.PI);
                int cmp = Double.compare(angleA, angleB);
                if (cmp != 0) {
                    return cmp;
                }
                double aDist = Math.sqrt((Math.pow(a.x - bestAsteroid.x, 2) + Math.pow(a.y - bestAsteroid.y, 2)));
                double bDist = Math.sqrt((Math.pow(b.x - bestAsteroid.x, 2) + Math.pow(b.y - bestAsteroid.y, 2)));
                return Double.compare(aDist, bDist);
            }
        });
        int destroyed = 0;
        Asteroid nbr200 = null;
        while (nbr200 == null) {
            List<Asteroid> nextRound = new ArrayList<>();
            double lastAngle = 7;
            for (int i = 0; i < sorted.size(); i++) {
                Asteroid a = sorted.get(i);
                double angleA = (Math.atan2(bestAsteroid.y-a.y, bestAsteroid.x-a.x) + 3 * Math.PI / 2) % (2 * Math.PI);
                System.out.println(angleA);
                if (angleA == lastAngle) {
                    nextRound.add(a);
                } else {
                    lastAngle = angleA;
                    destroyed++;
                    System.out.println("D " + destroyed + ": " + a.x + "," + a.y);
                    if (destroyed == 200) {
                        nbr200 = a;
                        break;
                    }
                }
            }
            sorted = nextRound;
        }
        System.out.println(nbr200.x * 100 + nbr200.y);
        //< 506
    }

    private static void a(String input) {
        String[] lines = input.split("\n");
        int[][] map = new int[lines.length][];
        boolean[][] asteroidsOnMap = new boolean[lines.length][];
        List<Asteroid> asteroids = new ArrayList<>();
        for (int y = 0; y < lines.length; y++) {
            map[y] = new int[lines[y].length()];
            asteroidsOnMap[y] = new boolean[lines[y].length()];
            for (int x = 0; x < lines[y].length(); x++) {
                if (lines[y].charAt(x) == '#') {
                    asteroidsOnMap[y][x] = true;
                    asteroids.add(new Asteroid(x, y));
                }
            }
        }
        int max = 0;
        for (Asteroid a : asteroids) {
            Set<Double> seenAngles = new HashSet<>();
            for (Asteroid b : asteroids) {
                if (a != b) {
                    double angle = Math.atan2(a.y - b.y, a.x - b.x);
                    seenAngles.add(angle);
                }
            }
            max = Math.max(max, seenAngles.size());
        }
        System.out.println(max);
    }

    static class Asteroid {
        final int x;
        final int y;

        Asteroid(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static final String TEST_INPUT_2 = ".#....#####...#..\n"
                                               + "##...##.#####..##\n"
                                               + "##...#...#.#####.\n"
                                               + "..#.....X...###..\n"
                                               + "..#.#.....#....##";
    private static final String TEST_INPUT = ".#..##.###...#######\n"
                                             + "##.############..##.\n"
                                             + ".#.######.########.#\n"
                                             + ".###.#######.####.#.\n"
                                             + "#####.##.#.##.###.##\n"
                                             + "..#####..#.#########\n"
                                             + "####################\n"
                                             + "#.####....###.#.#.##\n"
                                             + "##.#################\n"
                                             + "#####.##.###..####..\n"
                                             + "..######..##.#######\n"
                                             + "####.##.####...##..#\n"
                                             + ".#####..#.######.###\n"
                                             + "##...#.##########...\n"
                                             + "#.##########.#######\n"
                                             + ".####.#.###.###.#.##\n"
                                             + "....##.##.###..#####\n"
                                             + ".#.#.###########.###\n"
                                             + "#.#.#.#####.####.###\n"
                                             + "###.##.####.##.#..##";

    private static final String INPUT = "##.#..#..###.####...######\n"
                                        + "#..#####...###.###..#.###.\n"
                                        + "..#.#####....####.#.#...##\n"
                                        + ".##..#.#....##..##.#.#....\n"
                                        + "#.####...#.###..#.##.#..#.\n"
                                        + "..#..#.#######.####...#.##\n"
                                        + "#...####.#...#.#####..#.#.\n"
                                        + ".#..#.##.#....########..##\n"
                                        + "......##.####.#.##....####\n"
                                        + ".##.#....#####.####.#.####\n"
                                        + "..#.#.#.#....#....##.#....\n"
                                        + "....#######..#.##.#.##.###\n"
                                        + "###.#######.#..#########..\n"
                                        + "###.#.#..#....#..#.##..##.\n"
                                        + "#####.#..#.#..###.#.##.###\n"
                                        + ".#####.#####....#..###...#\n"
                                        + "##.#.......###.##.#.##....\n"
                                        + "...#.#.#.###.#.#..##..####\n"
                                        + "#....#####.##.###...####.#\n"
                                        + "#.##.#.######.##..#####.##\n"
                                        + "#.###.##..##.##.#.###..###\n"
                                        + "#.####..######...#...#####\n"
                                        + "#..#..########.#.#...#..##\n"
                                        + ".##..#.####....#..#..#....\n"
                                        + ".###.##..#####...###.#.#.#\n"
                                        + ".##..######...###..#####.#";
}
