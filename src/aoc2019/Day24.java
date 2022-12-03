package aoc2019;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day24 {
    //392000 high
    public static void main(String[] args) {
        String input = INPUT;
        a(input);
        b(input);
    }

    private static void b(String input) {
        Map<XY, List<LayerXY>> adjacent = adjacent();
        World world = new World();
        String[] rows = input.split("\n");
        boolean[][] alive = new boolean[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            alive[i] = new boolean[rows.length];
            for (int j = 0; j < rows[i].length(); j++) {
                alive[i][j] = rows[i].charAt(j) == '#';
            }
        }
        world.layers.get(205).alive = alive;

        for (int i = 0; i < 200; i++) {
            World next = new World();
            for (int layer = 1; layer < world.layers.size()-1; layer++) {
                alive = world.layers.get(layer).alive;
                for (int row = 0; row < rows.length; row++) {
                    for (int col = 0; col < rows[row].length(); col++) {
                        int bugs = 0;
                        List<LayerXY> adj = adjacent.getOrDefault(new XY(col, row), Collections.emptyList());
                        for (LayerXY a : adj) {
                            if (world.layers.get(layer + a.layerModifier).alive[a.xy.y][a.xy.x]) {
                                bugs++;
                            }
                        }
                        if (bugs != 1 && alive[row][col]) {
                            next.layers.get(layer).alive[row][col] = false;
                        } else if ((bugs == 1 || bugs == 2) && !alive[row][col]) {
                            next.layers.get(layer).alive[row][col] = true;
                        } else {
                            next.layers.get(layer).alive[row][col] = alive[row][col];
                        }
                    }
                }
            }
            world = next;
        }

        int bugs = 0;
        for (int layer = 1; layer < world.layers.size()-1; layer++) {
            alive = world.layers.get(layer).alive;
//            System.out.println(layer - 205);
//            Util.print(alive, '#', '.');
            for (int row = 0; row < rows.length; row++) {
                for (int col = 0; col < rows[row].length(); col++) {
                    if (alive[row][col]) {
                        bugs++;
                    }
                }
            }
        }
        System.out.println(bugs);

    }

    private static Map<XY, List<LayerXY>> adjacent() {
        Map<XY, List<LayerXY>> adjacent = new HashMap<>();
        XY xy00 = new XY(0, 0);
        XY xy01 = new XY(0, 1);
        XY xy02 = new XY(0, 2);
        XY xy03 = new XY(0, 3);
        XY xy04 = new XY(0, 4);

        XY xy10 = new XY(1, 0);
        XY xy11 = new XY(1, 1);
        XY xy12 = new XY(1, 2);
        XY xy13 = new XY(1, 3);
        XY xy14 = new XY(1, 4);

        XY xy20 = new XY(2, 0);
        XY xy21 = new XY(2, 1);
//        XY xy22 = new XY(2, 2);
        XY xy23 = new XY(2, 3);
        XY xy24 = new XY(2, 4);

        XY xy30 = new XY(3, 0);
        XY xy31 = new XY(3, 1);
        XY xy32 = new XY(3, 2);
        XY xy33 = new XY(3, 3);
        XY xy34 = new XY(3, 4);

        XY xy40 = new XY(4, 0);
        XY xy41 = new XY(4, 1);
        XY xy42 = new XY(4, 2);
        XY xy43 = new XY(4, 3);
        XY xy44 = new XY(4, 4);
        adjacent.put(xy00, Arrays.asList(new LayerXY(1, xy21), new LayerXY(1, xy12), new LayerXY(0, xy10), new LayerXY(0, xy01)));
        adjacent.put(xy10, Arrays.asList(new LayerXY(1, xy21), new LayerXY(0, xy00), new LayerXY(0, xy20), new LayerXY(0, xy11)));
        adjacent.put(xy20, Arrays.asList(new LayerXY(1, xy21), new LayerXY(0, xy10), new LayerXY(0, xy30), new LayerXY(0, xy21)));
        adjacent.put(xy30, Arrays.asList(new LayerXY(1, xy21), new LayerXY(0, xy20), new LayerXY(0, xy40), new LayerXY(0, xy31)));
        adjacent.put(xy40, Arrays.asList(new LayerXY(1, xy21), new LayerXY(0, xy30), new LayerXY(1, xy32), new LayerXY(0, xy41)));

        adjacent.put(xy01, Arrays.asList(new LayerXY(0, xy00), new LayerXY(1, xy12), new LayerXY(0, xy11), new LayerXY(0, xy02)));
        adjacent.put(xy11, Arrays.asList(new LayerXY(0, xy10), new LayerXY(0, xy01), new LayerXY(0, xy21), new LayerXY(0, xy12)));
        adjacent.put(xy21, Arrays.asList(new LayerXY(0, xy20), new LayerXY(0, xy11), new LayerXY(0, xy31), /*new LayerXY(0, xy22)*/
                new LayerXY(-1, xy00), new LayerXY(-1, xy10), new LayerXY(-1, xy20), new LayerXY(-1, xy30), new LayerXY(-1, xy40)));
        adjacent.put(xy31, Arrays.asList(new LayerXY(0, xy30), new LayerXY(0, xy21), new LayerXY(0, xy41), new LayerXY(0, xy32)));
        adjacent.put(xy41, Arrays.asList(new LayerXY(0, xy40), new LayerXY(0, xy31), new LayerXY(1, xy32), new LayerXY(0, xy42)));

        adjacent.put(xy02, Arrays.asList(new LayerXY(0, xy01), new LayerXY(1, xy12), new LayerXY(0, xy12), new LayerXY(0, xy03)));
        adjacent.put(xy12, Arrays.asList(new LayerXY(0, xy11), new LayerXY(0, xy02) /*,new LayerXY(0, xy22)*/, new LayerXY(0, xy13),
                new LayerXY(-1, xy00), new LayerXY(-1, xy01), new LayerXY(-1, xy02), new LayerXY(-1, xy03), new LayerXY(-1, xy04)));
//        adjacent.put(xy22, Arrays.asList(new LayerXY(0, xy21), new LayerXY(0, xy11), new LayerXY(0, xy31), new LayerXY(0, xy21?)));
        adjacent.put(xy32, Arrays.asList(new LayerXY(0, xy31)/*, new LayerXY(0, xy22)*/, new LayerXY(0, xy42), new LayerXY(0, xy33),
                new LayerXY(-1, xy40), new LayerXY(-1, xy41), new LayerXY(-1, xy42), new LayerXY(-1, xy43), new LayerXY(-1, xy44)));
        adjacent.put(xy42, Arrays.asList(new LayerXY(0, xy41), new LayerXY(0, xy32), new LayerXY(1, xy32), new LayerXY(0, xy43)));

        adjacent.put(xy03, Arrays.asList(new LayerXY(0, xy02), new LayerXY(1, xy12), new LayerXY(0, xy13), new LayerXY(0, xy04)));
        adjacent.put(xy13, Arrays.asList(new LayerXY(0, xy12), new LayerXY(0, xy03), new LayerXY(0, xy23), new LayerXY(0, xy14)));
        adjacent.put(xy23, Arrays.asList(/*new LayerXY(0, xy22),*/ new LayerXY(0, xy13), new LayerXY(0, xy33), new LayerXY(0, xy24),
                new LayerXY(-1, xy04), new LayerXY(-1, xy14), new LayerXY(-1, xy24), new LayerXY(-1, xy34), new LayerXY(-1, xy44)));
        adjacent.put(xy33, Arrays.asList(new LayerXY(0, xy32), new LayerXY(0, xy23), new LayerXY(0, xy43), new LayerXY(0, xy34)));
        adjacent.put(xy43, Arrays.asList(new LayerXY(0, xy42), new LayerXY(0, xy33), new LayerXY(1, xy32), new LayerXY(0, xy44)));

        adjacent.put(xy04, Arrays.asList(new LayerXY(0, xy03), new LayerXY(1, xy12), new LayerXY(0, xy14), new LayerXY(1, xy23)));
        adjacent.put(xy14, Arrays.asList(new LayerXY(0, xy13), new LayerXY(0, xy04), new LayerXY(0, xy24), new LayerXY(1, xy23)));
        adjacent.put(xy24, Arrays.asList(new LayerXY(0, xy23), new LayerXY(0, xy14), new LayerXY(0, xy34), new LayerXY(1, xy23)));
        adjacent.put(xy34, Arrays.asList(new LayerXY(0, xy33), new LayerXY(0, xy24), new LayerXY(0, xy44), new LayerXY(1, xy23)));
        adjacent.put(xy44, Arrays.asList(new LayerXY(0, xy43), new LayerXY(0, xy34), new LayerXY(1, xy32), new LayerXY(1, xy23)));
        return adjacent;
    }

    static class XY {
        final int x;
        final int y;

        XY(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            XY xy = (XY) o;
            return x == xy.x &&
                   y == xy.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
    static class LayerXY {
        final int layerModifier;
        final XY xy;

        LayerXY(int layerModifier, XY xy) {
            this.layerModifier = layerModifier;
            this.xy = xy;
        }
    }
    static class World {
        final List<Layer> layers = IntStream.range(0, 410).mapToObj(i -> new Layer()).collect(Collectors.toList());
    }

    static class Layer {
        boolean[][] alive;

        public Layer() {
            this.alive = new boolean[5][];
            ;
            for (int i = 0; i < 5; i++) {
                this.alive[i] = new boolean[5];
            }
        }
    }

    private static void a(String input) {
        String[] rows = input.split("\n");
        boolean[][] alive = new boolean[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            alive[i] = new boolean[rows.length];
            for (int j = 0; j < rows[i].length(); j++) {
                alive[i][j] = rows[i].charAt(j) == '#';
            }
        }
        Set<Integer> seen = new HashSet<>();
        seen.add(encode(alive));
        while (true) {
            boolean[][] next = new boolean[rows.length][];
            for (int row = 0; row < rows.length; row++) {
                next[row] = new boolean[rows.length];
                for (int col = 0; col < rows[row].length(); col++) {
                    int bugs = 0;
                    if (row > 0 && alive[row - 1][col]) {
                        bugs++;
                    }
                    if (row < rows.length - 1 && alive[row + 1][col]) {
                        bugs++;
                    }
                    if (col > 0 && alive[row][col - 1]) {
                        bugs++;
                    }
                    if (col < alive[row].length - 1 && alive[row][col + 1]) {
                        bugs++;
                    }
                    if (bugs != 1 && alive[row][col]) {
                        next[row][col] = false;
                    } else if ((bugs == 1 || bugs == 2) && !alive[row][col]) {
                        next[row][col] = true;
                    } else {
                        next[row][col] = alive[row][col];
                    }
                }
            }

            int encode = encode(next);
            if (!seen.add(encode)) {
                System.out.println(encode);
                return;
            }
            alive = next;
        }

    }

    static int encode(boolean[][] alive) {
        int ix = 0;
        int encode = 0;
        for (int i = 0; i < alive.length; i++) {
            for (int j = 0; j < alive[i].length; j++) {
                if (alive[i][j]) {
                    encode += 1 << ix;
                }
                ix++;
            }
        }
        return encode;
    }

    private static final String TEST_INPUT = "....#\n"
                                             + "#..#.\n"
                                             + "#..##\n"
                                             + "..#..\n"
                                             + "#....";

    private static final String INPUT = "#..#.\n"
                                        + "#.#.#\n"
                                        + "...#.\n"
                                        + "....#\n"
                                        + "#.#.#";
}
