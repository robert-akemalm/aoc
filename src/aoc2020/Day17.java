package aoc2020;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Day17 {
    public static void main(String[] args) {
        a(TEST_INPUT);
        a(INPUT);
        b(TEST_INPUT);
        b(INPUT);
    }

    private static void b(String input) {
    }

    private static void a(String input) {
        Map<Node, Node> nodes = new TreeMap<>();
        String[] rows = input.lines().toArray(String[]::new);
        for (int y = 0; y < rows.length; y++) {
            for (int x = 0; x < rows[y].length(); x++) {
                Node node = new Node(x, y, 0, 0, rows[y].charAt(x) == '#');
                nodes.put(node, node);
            }
        }
//        print(nodes, 0);

        for (int step = 0; step < 6; step++) {
            List<Node> nextNodes = new ArrayList<>();
            for (Node node : nodes.values().toArray(new Node[0])) {
                for (int x = node.x - 1; x <= node.x + 1; x++) {
                    for (int y = node.y - 1; y <= node.y + 1; y++) {
                        for (int z = node.z - 1; z <= node.z + 1; z++) {
                            for (int w = node.w - 1; w <= node.w + 1; w++) {
                                Node n = new Node(x, y, z, w, false);
                                nodes.putIfAbsent(n, n);
                            }
                        }
                    }
                }
            }

            for (Node node : nodes.values()) {
                int numActive = 0;
                for (int x = node.x - 1; x <= node.x + 1; x++) {
                    for (int y = node.y - 1; y <= node.y + 1; y++) {
                        for (int z = node.z - 1; z <= node.z + 1; z++) {
                            for (int w = node.w - 1; w <= node.w + 1; w++) {
                                Node n = new Node(x, y, z, w, false);
                                if (!n.equals(node)) {
                                    Node neighbour = nodes.getOrDefault(n, n);
                                    numActive += neighbour.active ? 1 : 0;
                                }
                            }
                        }
                    }
                }
                if (node.active) {
                    if (numActive == 2 || numActive == 3) {
                        nextNodes.add(node);
                    } else {
                        nextNodes.add(new Node(node.x, node.y, node.z, node.w, false));
                    }
                } else {
                    if (numActive == 3) {
                        nextNodes.add(new Node(node.x, node.y, node.z, node.w, true));
                    } else {
                        nextNodes.add(node);
                    }
                }
            }
            for (Node node : nextNodes) {
                nodes.put(node, node);
            }
//            print(nodes, step+1);
        }
        System.out.println(nodes.values().stream().filter(n -> n.active).count());
    }

    private static void print(Map<Node, Node> nodes, int step) {
        System.out.println("Step: " + step);
        List<Node> list = new ArrayList<>(nodes.values());
        int z = Integer.MAX_VALUE;
        int y = list.get(0).y;
        for (Node node : list) {
            if (node.z != z) {
                z = node.z;
                System.out.println("\nz = " + z);
            }
            if (node.y != y) {
                y = node.y;
                System.out.println();
            }
            System.out.print(node.active ? "#" : ".");
        }
        System.out.println();
    }

    private static class Node implements Comparable<Node> {
        final int x;
        final int y;
        final int z;
        final int w;
        boolean active;

        Node(int x, int y, int z, int w, boolean active) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
            this.active = active;
        }

        @Override
        public boolean equals(Object o) {
            Node node = (Node) o;
            return x == node.x && y == node.y && z == node.z && w == node.w;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + z;
            result = 31 * result + w;
            return result;
        }

        @Override
        public String toString() {
            return x + ", " + y + ", " + z + ", " + w + ", " + active;
        }

        @Override
        public int compareTo(Node o) {
            int cmp = Integer.compare(this.w, o.w);
            if (cmp != 0) {
                return cmp;
            }
            cmp = Integer.compare(this.z, o.z);
            if (cmp != 0) {
                return cmp;
            }
            cmp = Integer.compare(this.y, o.y);
            if (cmp != 0) {
                return cmp;
            }
            return Integer.compare(this.x, o.x);
        }
    }

    private static final String TEST_INPUT = ".#.\n"
                                             + "..#\n"
                                             + "###";

    private static final String INPUT = "#.##.##.\n"
                                        + ".##..#..\n"
                                        + "....#..#\n"
                                        + ".##....#\n"
                                        + "#..##...\n"
                                        + ".###..#.\n"
                                        + "..#.#..#\n"
                                        + ".....#..";
}
