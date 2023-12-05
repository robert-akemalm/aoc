package aoc2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    static void print(boolean[][] matrix, char trueChar, char falseChar) {
        for (int i = 0; i < matrix.length; i++) {
            boolean[] row = matrix[i];
            for (boolean b : row) {
                System.out.print(b ? trueChar : falseChar);
            }
            System.out.println();
        }
        System.out.println();
    }

    static String print(Map<Pos, Integer> positions, Map<Integer, Character> drawings) {
        return print(positions, drawings, true);
    }

    static String print(Map<Pos, Integer> positions, Map<Integer, Character> drawings, boolean yDesc) {
        int minX = positions.keySet().stream().mapToInt(p -> p.x).min().orElse(0);
        int maxX = positions.keySet().stream().mapToInt(p -> p.x).max().orElse(0) + 1;
        int minY = positions.keySet().stream().mapToInt(p -> p.y).min().orElse(0);
        int maxY = positions.keySet().stream().mapToInt(p -> p.y).max().orElse(0) + 1;
        int xDiff = maxX - minX;
        int yDiff = maxY - minY;
        char[][] print = new char[yDiff][];
        for (int i = 0; i < yDiff; i++) {
            print[i] = new char[xDiff];
            Arrays.fill(print[i], '#');
        }
        for (Pos pos : positions.keySet()) {
            print[pos.y - minY][pos.x - minX] = drawings.get(positions.get(pos));
        }
        StringBuilder sb = new StringBuilder();
        if (yDesc) {
            for (int i = 0; i < print.length; i++) {
                char[] row = print[i];
                for (char b : row) {
                    sb.append(b);
                }
                sb.append('\n');
            }
        } else {
            for (int i = print.length - 1; i >= 0; i--) {
                char[] row = print[i];
                for (char b : row) {
                    sb.append(b);
                }
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    static void print(Collection<Pos> positions) {
        print(positions, '\u2588', ' ');
    }

    static void print(Collection<Pos> positions, char posChar, char other) {
        int minX = positions.stream().mapToInt(p -> p.x).min().orElse(0);
        int maxX = positions.stream().mapToInt(p -> p.x).max().orElse(0) + 1;
        int minY = positions.stream().mapToInt(p -> p.y).min().orElse(0);
        int maxY = positions.stream().mapToInt(p -> p.y).max().orElse(0) + 1;
        int xDiff = maxX - minX;
        int yDiff = maxY - minY;
        boolean[][] print = new boolean[yDiff][];
        for (int i = 0; i < yDiff; i++) {
            print[i] = new boolean[xDiff];
        }
        for (Pos pos : positions) {
            print[pos.y - minY][pos.x - minX] = true;
        }
        Util.print(print, posChar, other);
    }

    public record Pos(int x, int y) {

        public List<Pos> neighbours() {
            return new ArrayList<>(List.of(new Pos(x + 1, y), new Pos(x - 1, y), new Pos(x, y + 1), new Pos(x, y - 1)));
        }

        public List<Pos> allNeighbours() {
            List<Pos> all = new ArrayList<>();
            for (int nX = x-1; nX <=x+1 ; nX++) {
                for (int nY = y-1; nY <=y+1 ; nY++) {
                    if (nX != x || nY != y) {
                        all.add(new Pos(nX, nY));
                    }
                }
            }
            return all;
        }

        @Override
        public String toString() {
            return x + "," + y;
        }

        public int manhattanDistanceTo(Pos pos) {
            return Math.abs(pos.x - x) + Math.abs(pos.y - y);
        }
    }

    public static int[] extractInts(String input) {
        Pattern p = Pattern.compile("-?[0-9]+");
        Matcher m = p.matcher(input);
        List<Integer> ints = new ArrayList<>();
        while (m.find()) {
            ints.add(Integer.parseInt(m.group()));
        }
        return ints.stream().mapToInt(i -> i).toArray();
    }

    public static long[] extractLongs(String input) {
        Pattern p = Pattern.compile("-?[0-9]+");
        Matcher m = p.matcher(input);
        List<Long> longs = new ArrayList<>();
        while (m.find()) {
            longs.add(Long.parseLong(m.group()));
        }
        return longs.stream().mapToLong(i -> i).toArray();
    }

    static void time(Runnable method) {
        long start = System.currentTimeMillis();
        method.run();
        System.out.println("Time: " + (System.currentTimeMillis()-start) + "\n");
    }
}