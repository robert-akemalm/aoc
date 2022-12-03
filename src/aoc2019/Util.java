package aoc2019;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class Util {

    static void print(boolean[][] matrix, char trueChar, char falseChar) {
        for (boolean[] row : matrix) {
            for (boolean b : row) {
                System.out.print(b ? trueChar : falseChar);
            }
            System.out.println();
        }
    }

    static String print(Map<Pos, Integer> positions, Map<Integer, Character> drawings) {
        int minX = positions.keySet().stream().mapToInt(p->p.x).min().orElse(0);
        int maxX = positions.keySet().stream().mapToInt(p->p.x).max().orElse(0)+1;
        int minY = positions.keySet().stream().mapToInt(p->p.y).min().orElse(0);
        int maxY = positions.keySet().stream().mapToInt(p->p.y).max().orElse(0)+1;
        int xDiff = maxX - minX;
        int yDiff = maxY - minY;
        char[][] print= new char[yDiff][];
        for (int i = 0; i < yDiff; i++) {
            print[i] = new char[xDiff];
            Arrays.fill(print[i], '#');
        }
        for (Pos pos : positions.keySet()) {
            print[pos.y - minY][pos.x - minX] = drawings.get(positions.get(pos));
        }
        StringBuilder sb = new StringBuilder();
        for (char[] row : print) {
            for (char b : row) {
                sb.append(b);
            }
            sb.append('\n');
        }
//        System.out.println(sb);
        return sb.toString();
    }

    static void print(Collection<Pos> positions, char posChar, char other) {
        int minX = positions.stream().mapToInt(p->p.x).min().orElse(0);
        int maxX = positions.stream().mapToInt(p->p.x).max().orElse(0)+1;
        int minY = positions.stream().mapToInt(p->p.y).min().orElse(0);
        int maxY = positions.stream().mapToInt(p->p.y).max().orElse(0)+1;
        int xDiff = maxX - minX;
        int yDiff = maxY - minY;
        boolean[][] print= new boolean[yDiff][];
        for (int i = 0; i < yDiff; i++) {
            print[i] = new boolean[xDiff];
        }
        for (Pos pos : positions) {
            print[pos.y - minY][pos.x - minX] = true;
        }
        Util.print(print, posChar, other);
    }

    static class Pos {
        final int x;
        final int y;

        Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return x + "," + y;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            Pos pos = (Pos) o;
            return x == pos.x && y == pos.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}