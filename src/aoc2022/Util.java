package aoc2022;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class Util {

    static void print(boolean[][] matrix, char trueChar, char falseChar) {
        for (boolean[] row : matrix) {
            for (boolean b : row) {
                System.out.print(b ? trueChar : falseChar);
            }
            System.out.println();
        }
        System.out.println();
    }

    static String print(Map<Pos, Integer> positions, Map<Integer, Character> drawings) {
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
        for (int i = print.length - 1; i >= 0; i--) {
            char[] row = print[i];
            for (char b : row) {
                sb.append(b);
            }
            sb.append('\n');
        }
//        System.out.println(sb);
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
        @Override
        public String toString() {
            return x + "," + y;
        }
    }
}