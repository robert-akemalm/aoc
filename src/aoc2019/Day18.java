package aoc2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

import aoc2019.Util.Pos;

public class Day18 {
    static class Edge {
        final Node node;
        final int steps;

        Edge(Node node, int steps) {
            this.node = node;
            this.steps = steps;
        }
    }

    static class Node {
        final Pos pos;
        final List<Edge> edges = new ArrayList<>();
        final char symbol;

        Node(Pos pos, char symbol) {
            this.pos = pos;
            this.symbol = symbol;
        }
    }

    static Map<Pos, Node> map = new HashMap<>();
    static Map<Pos, Character> keys = new HashMap<>();
    static Map<Pos, Character> doors = new HashMap<>();

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        String[] rows = INPUT_B.split("\n");
        List<Node> start = new ArrayList<>();
        for (int y = 0; y < rows.length; y++) {
            for (int x = 0; x < rows[y].length(); x++) {
                char ch = rows[y].charAt(x);
                if (ch != '#') {
                    Pos pos = new Pos(x, y);
                    Node node = new Node(pos, ch);
                    map.put(pos, node);
                    if (ch == '@') {
                        start.add(node);
                    } else if (ch >= 'a' && ch <= 'z') {
                        keys.put(pos, ch);
                    } else if (ch >= 'A' && ch <= 'Z') {
                        doors.put(pos, ch);
                    }
                }
            }
        }
        while (true) {
            List<Pos> toRemove = new ArrayList<>();
            for (Entry<Pos, Node> e : map.entrySet()) {
                Pos pos = e.getKey();
                List<Edge> edges = e.getValue().edges;
                edges.clear();

                Node left = map.get(new Pos(pos.x - 1, pos.y));
                if (left != null) {
                    edges.add(new Edge(left, 1));
                }
                Node right = map.get(new Pos(pos.x + 1, pos.y));
                if (right != null) {
                    edges.add(new Edge(right, 1));
                }
                Node up = map.get(new Pos(pos.x, pos.y - 1));
                if (up != null) {
                    edges.add(new Edge(up, 1));
                }
                Node down = map.get(new Pos(pos.x, pos.y + 1));
                if (down != null) {
                    edges.add(new Edge(down, 1));
                }

                if (edges.size() <= 1 && (e.getValue().symbol == '.' || doors.containsKey(e.getKey()))) {
                    toRemove.add(pos);
                }
            }
            for (Pos pos : toRemove) {
                map.remove(pos);
            }
            if (toRemove.isEmpty()) {
                break;
            }
        }

        while (reduce()) {
        }

//        print(map);
//        System.out.println(map.size());
        System.out.println(System.currentTimeMillis() - time);
        Map<String, Map<String, Integer>> best = new HashMap<>();
        minSteps = Integer.MAX_VALUE;//3866; // 41990
        PriorityQueue<Path> queue = new PriorityQueue<>((o1, o2) -> {
            int cmp = Integer.compare(o2.foundKeys.size(), o1.foundKeys.size());
            if (cmp == 0) {
                cmp = Integer.compare(o1.steps, o2.steps);
            }
            return cmp;
        });
        queue.add(new Path(start.toArray(new Node[0])));
        while (!queue.isEmpty()) {
            Path path = queue.poll();
            if (path.steps >= minSteps) {
                continue;
            }
            List<Path> possible = path.possibleSteps();
            for (Path p : possible) {
                if (p.steps >= minSteps) {
                    continue;
                }
                String nextState = p.state();
                String foundKeys = p.keys();
                Map<String, Integer> b = best.computeIfAbsent(nextState, k -> new HashMap<>());
                if (p.steps < b.getOrDefault(foundKeys, Integer.MAX_VALUE)) {
                    b.put(foundKeys, p.steps);
                    queue.add(p);
                }
            }
        }
        System.out.println(minSteps);
        System.out.println(System.currentTimeMillis() - time);
    }

    static Set<Pos> reduced = new HashSet<>();

    private static boolean reduce() {
        Pos toRemove = null;
        for (Entry<Pos, Node> e : map.entrySet()) {
            Node node = e.getValue();
            if (node.edges.size() == 2 && node.symbol == '.') {
                toRemove = e.getKey();
                Node a = node.edges.get(0).node;
                Node b = node.edges.get(1).node;
                int steps = node.edges.get(0).steps + node.edges.get(1).steps;
                for (int i = 0; i < a.edges.size(); i++) {
                    if (a.edges.get(i).node.pos == node.pos) {
                        a.edges.set(i, new Edge(b, steps));
                    }
                }
                for (int i = 0; i < b.edges.size(); i++) {
                    if (b.edges.get(i).node.pos == node.pos) {
                        b.edges.set(i, new Edge(a, steps));
                    }
                }
                break;
            }
        }
        if (toRemove != null) {
            reduced.add(toRemove);
            map.remove(toRemove);
        }
        return toRemove != null;
    }

    static int minSteps = Integer.MAX_VALUE;

    static class Path {
        final Set<Pos> visited = new HashSet<>();
        Set<Character> foundKeys = new HashSet<>();
        Set<Character> foundDoors = new HashSet<>();
        Node[] current = new Node[4];
        Node[] previous = new Node[4];
        int steps;

        public Path(Path path, Node current, int ix) {
            Edge e = path.current[ix].edges.stream().filter(edge -> edge.node == current).findAny().get();
            this.steps = path.steps + e.steps;
            this.visited.addAll(path.visited);
            this.visited.add(current.pos);
            this.current = new Node[path.current.length];
            for (int i = 0; i < path.current.length; i++) {
                this.previous[i] = path.previous[i];
                this.current[i] = path.current[i];
                if (i == ix) {
                    this.previous[i] = this.current[i];
                    this.current[i] = current;
                }
            }
            this.foundDoors.addAll(path.foundDoors);
            Character door = doors.get(current.pos);
            if (door != null) {
                this.foundDoors.add(door);
            }
            this.foundKeys.addAll(path.foundKeys);
            Character key = keys.get(current.pos);
            if (key != null) {
                this.foundKeys.add(key);
                if (foundKeys.size() == keys.size()) {
                    minSteps = Math.min(minSteps, steps);
                    System.out.println(minSteps);
                }
            }
        }

        String keys() {
            return foundKeys.stream().sorted().map(String::valueOf).collect(Collectors.joining());
        }

        public Path(Node[] start) {
            visited.addAll(Arrays.stream(start).map(n -> n.pos).collect(Collectors.toSet()));
            this.current = start;
            this.steps = 0;
        }

        public boolean isPrevious(Pos pos, int ix) {
            return pos.equals(this.previous[ix].pos);
        }

        public String state() {
            return Arrays.stream(current).map(n -> n.pos.toString()).collect(Collectors.joining(","));
        }

        public List<Path> possibleSteps() {
            List<Path> possible = new ArrayList<>();
            for (int i = 0; i < current.length; i++) {
                Node c = current[i];
                for (Edge edge : c.edges) {
                    Character door = doors.get(edge.node.pos);
                    if (door == null || foundKeys.contains(Character.toLowerCase(door))) {
                        possible.add(new Path(this, edge.node, i));
                    }
                }
            }
            return possible;
        }
    }

    static void print(Map<Pos, Node> map) {
        Map<Integer, Character> symbols = new HashMap<>();
        Map<Pos, Integer> print = new HashMap<>();
        for (Entry<Pos, Node> e : map.entrySet()) {
            print.put(e.getKey(), (int) e.getValue().symbol);
            symbols.put((int) e.getValue().symbol, e.getValue().symbol);
        }
        for (Pos p : reduced) {
            print.put(p, -1);
            symbols.put(-1, ' ');
        }
        System.out.println(Util.print(print, symbols));

    }

    private static final String INPUT = "#################################################################################\n"
                                        + "#.....#.#.....#.....#.......#..p#.......#..y......#...#..................w......#\n"
                                        + "#.###.#.#.#.###.#.#.#.###.#.#.#.#.#####.#.#####.#.#.#.#######.#.#####Z###########\n"
                                        + "#...#...#.#.....#.#.#.#.#.#...#.#.#.....#.#...#.#...#.........#.#...#.#k....#...#\n"
                                        + "#.#.###.#.#######.#.#.#.#.#.#####.#.###.#.#.###.#############.###.#.###.###R#.#.#\n"
                                        + "#.#.#...#...#.....#.#...#.#.#.....#.#.#.#.#...#.#...........#.#...#.....#.#.#.#.#\n"
                                        + "#.#.#######.#.#####.#####.###.#####.#.#.#.###.#.#####.#####.#.#.#########.#.#.#.#\n"
                                        + "#.#.#.....F.#...#.......#.....#.N...#.#.#...#.#.....#.....#.#.#.#...#.....#...#.#\n"
                                        + "###.#.#######.#.#######.#.#####.#####.#.###.#.#####.#####.#.#.#.###.#.###.#####.#\n"
                                        + "#...#...#...#.#.....#.#.#.....#.#.......#.#.....#.#.#.....#...#...#.#.#.....#...#\n"
                                        + "#.#####.#.#.#######.#.#.#######.#######.#.#####.#.#.#.#########.#.#.#.#.###.#.###\n"
                                        + "#...I.#.#.#.....#...#.#.....#...#.....#.#.#...#...#.#.#.......#.#.#.#.#.#...#.#.#\n"
                                        + "#.###.#.#.#####.#.###.#####.#.###.###.#.#.#.#.#.###.###.#####.###E#.#.#.#####.#.#\n"
                                        + "#...#...#.#...#...#.......#.#.....#...#.#...#...#...#...#...#.....#...#.....#.#.#\n"
                                        + "#########.###.#########.#.#.#######.###.#########.###.###.#########.#######.#.#.#\n"
                                        + "#.....S.#...#...C.......#.#.........#.#.#.......#...#.#...#.......#.#.......#..s#\n"
                                        + "#.#####.###.#######.#####.###########.#.#.#####.#.#.#B###.#.#####.#.#.#####.###.#\n"
                                        + "#r#.........#d....#.....#.#.....#...#...#.#.....#.#.#...#.......#...#...#.#...#.#\n"
                                        + "#.#########.#.###.#####.###.#.#.#.#.#.###.###.#####.###.#######.#######.#.###.#.#\n"
                                        + "#.........#.#.#.#...D.#.....#.#.#.#.#.#.#...#.#.......#.#.....#...#.....#.....#.#\n"
                                        + "#.#######.#.#.#.#####.#.#####.#.###.#.#.###.#.###.###.#.#.###L###.#.#####.#####.#\n"
                                        + "#.#.....#.#.#.#.....#.#.#...#.#...#.#...#...#.....#...#...#.#j#.#.#.#...#.#..i#.#\n"
                                        + "#.#####.#.###.#.###.###.#.###.###.#.###.#.#########.#######.###.#.#.#.#.#.#.#.#.#\n"
                                        + "#.#.....#.#.M.#...#.....#.#...#.......#.#.#...#...#...#.......#...#...#.#.#.#.#.#\n"
                                        + "#.#.###.#.#.#########.###.#.###########.#.#.#.#.#.###.#.#######.#######.###.#.#.#\n"
                                        + "#...#...#.#..m......#.....#...#.........#.#.#...#......h#.......#...#.G.#...#...#\n"
                                        + "#####.###.#########.#.#######.#.#########.###############.#######.###.#.#.#######\n"
                                        + "#.....#...#...#q..#.#.#.......#.#.......#....v....#...#...#.........#.#.#..f..#.#\n"
                                        + "#.#####.#.###.#.#.#Q#.#.#######.#.#.#####.#######.#.#.#.#########.#.#.#.#####.#.#\n"
                                        + "#e#.....#.....#.#.#.#.#.#.......#.#.....#.#.....#...#.#.#.......#.#.#.#.#...T.#.#\n"
                                        + "###.#####.#####.#.#.#.#.#.###########.#.#.#####.#####.#.#.#####.###.#.###.#####.#\n"
                                        + "#...#.....#.....#...#.#...#.#.......#.#.#...........#.#...#...#...#.#.....#.....#\n"
                                        + "#.#####K###J#########.#####.#.#.###.###.###########.#.#####.#.###.#.###########.#\n"
                                        + "#.....#.#.#.#...#u........#.#x#.#.#.#..a#.....#.#...#.....U.#..c#...#.........#.#\n"
                                        + "#.###.#.#.#.#.#.#########.#.#.#.#.#.#.###.###.#.#.#####.#.#########.#.###.###.#.#\n"
                                        + "#.#...#...#...#.....#.#...#...#.#.#.#...#...#.#.#.#...#.#.#...#.....#.#...#.#...#\n"
                                        + "#.#.#####.#########.#.#.#####.#.#.#.###.###.#.#.#.###.#.###.#.#.#######.###.###.#\n"
                                        + "#.#.#b..#...#..l....#.#.....#.#.#...#...#...#...#...#.#...#.#...#.....#.#...#...#\n"
                                        + "#.#.#.#.#####.#######.#####.###.#.###.#.#.#########.#.###.#.#######.#.#.#.#.#.###\n"
                                        + "#.#...#.......#.................#.....#.................#...........#...#.#.....#\n"
                                        + "#######################################.@.#######################################\n"
                                        + "#...........#.....#...#.......#.............#.....#...............#.....#.....#.#\n"
                                        + "#.#########.#.###.#.#.###.###.#.###.#####.#.#####.#.#############.#.###.#.#.#.#.#\n"
                                        + "#..t..#.#...#...#...#...#.#...#...#.....#.#.......#.#.#.....#...X.#...#.#.#.#.#.#\n"
                                        + "#.###.#.#.###.#.#######.#.#.#####.#####.#.#.#######.#.#.#.#.#.#######.#.###.#.#.#\n"
                                        + "#.#...#.#.#...#.....#.#...#.#...#.#.....#.#.#.......#...#.#.#.......#.#.#...#...#\n"
                                        + "#.#.###.#.#.#######.#.#####.#.#.#.#.#####.###.#########.#.#######.#.#.#.#.#####.#\n"
                                        + "#.#.#...#.#.#.......#...#...#.#...#.#...#.....#.......#.#...#...#.#...#.#.....#.#\n"
                                        + "#.#.#.###.###.#######.#.#.###.#####.#.#.#.#####.###.#.#.###.#.#.#.#####.#.###.###\n"
                                        + "#.#.#...#.#...#...#...#.#.#.......#...#.#.#...#.#...#.#.#...#.#.#.#.....#...#...#\n"
                                        + "#.#.###.#.#.###.#.#.#.#.#.###.#########.#.#.###.#.###.###.#.#.#.#.#.###########.#\n"
                                        + "#.#...#...#...#.#...#.#.#...#.#.........#.#.....#.#.....#.#.#.#.#.#...#.........#\n"
                                        + "#####.#.###.#.#.#####.#.###.#.#.#########.#.#####.#####.#.###.#.#####.#.#.#######\n"
                                        + "#...#.#...#.#.#...#...#...#.#.#.........#.#.....#.....#.#.#...#...#...#.#.......#\n"
                                        + "#.#.#.###.###.#.###.###.###.#.#########.#.#####.#####.#.#.#.#####.#.###.#######.#\n"
                                        + "#.#...#.#.....#.#...#...#...#...#.#...V.#.#...#.#.....#...#...#.#...#...#.....#.#\n"
                                        + "#.#####.#######.#.###.###.#####.#.#.###.#.#.###.#.#######.###.#.#####.###.#####.#\n"
                                        + "#...#...#.....#.#.#...#...#.......#...#.#.#.....#.#...#...#.#.#.....#.#...#...#.#\n"
                                        + "###.#.#.#.#.#.###.#####.#############.###.#####.#.#.#.#.###.#.#.#####.###.#.#.#.#\n"
                                        + "#.#.#.#...#.#...#.#.....#...........#...#.A...#.#.#.#.#.#...#.#.....#...#.#.#...#\n"
                                        + "#.#.#.#####.###.#.#.###.###.#######.###.#####.###.#.#.#.#.#.#.#.###.###.#.#.###.#\n"
                                        + "#.#.#...#...#.....#.#.#.....#.....#.....#...#.....#.#.#.#.#.#.#.#.#...#...#...#.#\n"
                                        + "#.#.###.#.#########.#.###########.#####.#.#.#######.#.#.###.###.#.#.#####.###.###\n"
                                        + "#.#...#.#...........#...#.........#.....#.#...#.....#.#...#...#...#.......#.#...#\n"
                                        + "#.###.#.#########.#####.###.###.###.#####.#.#.#####.#.###.#.#.#.#########.#.###.#\n"
                                        + "#...#.#...#.....#.#.#...#...#...#...#...#.#.#.....#.#...#.#.#.#.#.....W.#...#...#\n"
                                        + "#.#.#.#####.###.#.#.#.###.###.###.###.#.#.#.#####.###.###.#.#.#.#.#####.###.#.###\n"
                                        + "#.#.#.......#.#.#...#.#...#...#.#.#...#.#.#.....#.....#...#.#...#.#.....#...#...#\n"
                                        + "#.#.#########.#.#####.#.###.###.#.#####.#.#####.#####.#.#####.###.#.#######.###.#\n"
                                        + "#.#.#.#.......#.......#.#...#...#.....#.#.#...#.....#.#.....#...#.#.......#.#...#\n"
                                        + "#.#.#.#O#####.#########.#.#####.#####.#.#.###.#####.#######.###.#.#######.###.###\n"
                                        + "#.#...#.....#...#.......#.....#.....#.#.#...#...#.#.......#...#.#.....#.#...#...#\n"
                                        + "#.#########.###.#.#####.#####.#####.#.#.###.#.#.#.#######.###.#######.#.###.###.#\n"
                                        + "#.#.......#.#.#.#...#.#.#...#.......#.#.#.#.#.#.#.......#z..#.....#...#...#....o#\n"
                                        + "#.#.#####.#.#.#.###.#.#.#.#.#####.###.#.#.#.#.#.#.#.###.#.#######.#.###.#######.#\n"
                                        + "#.#...#.#...#.#...#...#...#.#.....#...#.#.#.#.#.#.#.#...#.#.......#.#.#...#.....#\n"
                                        + "#.###.#.#####.###.###.#####.#######.###.#.#.#.#.###.#.###.#Y#######.#.#.#.#.#####\n"
                                        + "#g....#.........#...#...#...#...#...#...#.#...#.....#.#.#.#...#.....#.#.#...#...#\n"
                                        + "#.###########.#.###.###.#.###.#.#.#####.#.###########.#.#.###.###.###.#.#######.#\n"
                                        + "#.............#...#..n..#.....#...H.....#...............#.........#...P.........#\n"
                                        + "#################################################################################";

    private static final String INPUT_B = "#################################################################################\n"
                                        + "#.....#.#.....#.....#.......#..p#.......#..y......#...#..................w......#\n"
                                        + "#.###.#.#.#.###.#.#.#.###.#.#.#.#.#####.#.#####.#.#.#.#######.#.#####Z###########\n"
                                        + "#...#...#.#.....#.#.#.#.#.#...#.#.#.....#.#...#.#...#.........#.#...#.#k....#...#\n"
                                        + "#.#.###.#.#######.#.#.#.#.#.#####.#.###.#.#.###.#############.###.#.###.###R#.#.#\n"
                                        + "#.#.#...#...#.....#.#...#.#.#.....#.#.#.#.#...#.#...........#.#...#.....#.#.#.#.#\n"
                                        + "#.#.#######.#.#####.#####.###.#####.#.#.#.###.#.#####.#####.#.#.#########.#.#.#.#\n"
                                        + "#.#.#.....F.#...#.......#.....#.N...#.#.#...#.#.....#.....#.#.#.#...#.....#...#.#\n"
                                        + "###.#.#######.#.#######.#.#####.#####.#.###.#.#####.#####.#.#.#.###.#.###.#####.#\n"
                                        + "#...#...#...#.#.....#.#.#.....#.#.......#.#.....#.#.#.....#...#...#.#.#.....#...#\n"
                                        + "#.#####.#.#.#######.#.#.#######.#######.#.#####.#.#.#.#########.#.#.#.#.###.#.###\n"
                                        + "#...I.#.#.#.....#...#.#.....#...#.....#.#.#...#...#.#.#.......#.#.#.#.#.#...#.#.#\n"
                                        + "#.###.#.#.#####.#.###.#####.#.###.###.#.#.#.#.#.###.###.#####.###E#.#.#.#####.#.#\n"
                                        + "#...#...#.#...#...#.......#.#.....#...#.#...#...#...#...#...#.....#...#.....#.#.#\n"
                                        + "#########.###.#########.#.#.#######.###.#########.###.###.#########.#######.#.#.#\n"
                                        + "#.....S.#...#...C.......#.#.........#.#.#.......#...#.#...#.......#.#.......#..s#\n"
                                        + "#.#####.###.#######.#####.###########.#.#.#####.#.#.#B###.#.#####.#.#.#####.###.#\n"
                                        + "#r#.........#d....#.....#.#.....#...#...#.#.....#.#.#...#.......#...#...#.#...#.#\n"
                                        + "#.#########.#.###.#####.###.#.#.#.#.#.###.###.#####.###.#######.#######.#.###.#.#\n"
                                        + "#.........#.#.#.#...D.#.....#.#.#.#.#.#.#...#.#.......#.#.....#...#.....#.....#.#\n"
                                        + "#.#######.#.#.#.#####.#.#####.#.###.#.#.###.#.###.###.#.#.###L###.#.#####.#####.#\n"
                                        + "#.#.....#.#.#.#.....#.#.#...#.#...#.#...#...#.....#...#...#.#j#.#.#.#...#.#..i#.#\n"
                                        + "#.#####.#.###.#.###.###.#.###.###.#.###.#.#########.#######.###.#.#.#.#.#.#.#.#.#\n"
                                        + "#.#.....#.#.M.#...#.....#.#...#.......#.#.#...#...#...#.......#...#...#.#.#.#.#.#\n"
                                        + "#.#.###.#.#.#########.###.#.###########.#.#.#.#.#.###.#.#######.#######.###.#.#.#\n"
                                        + "#...#...#.#..m......#.....#...#.........#.#.#...#......h#.......#...#.G.#...#...#\n"
                                        + "#####.###.#########.#.#######.#.#########.###############.#######.###.#.#.#######\n"
                                        + "#.....#...#...#q..#.#.#.......#.#.......#....v....#...#...#.........#.#.#..f..#.#\n"
                                        + "#.#####.#.###.#.#.#Q#.#.#######.#.#.#####.#######.#.#.#.#########.#.#.#.#####.#.#\n"
                                        + "#e#.....#.....#.#.#.#.#.#.......#.#.....#.#.....#...#.#.#.......#.#.#.#.#...T.#.#\n"
                                        + "###.#####.#####.#.#.#.#.#.###########.#.#.#####.#####.#.#.#####.###.#.###.#####.#\n"
                                        + "#...#.....#.....#...#.#...#.#.......#.#.#...........#.#...#...#...#.#.....#.....#\n"
                                        + "#.#####K###J#########.#####.#.#.###.###.###########.#.#####.#.###.#.###########.#\n"
                                        + "#.....#.#.#.#...#u........#.#x#.#.#.#..a#.....#.#...#.....U.#..c#...#.........#.#\n"
                                        + "#.###.#.#.#.#.#.#########.#.#.#.#.#.#.###.###.#.#.#####.#.#########.#.###.###.#.#\n"
                                        + "#.#...#...#...#.....#.#...#...#.#.#.#...#...#.#.#.#...#.#.#...#.....#.#...#.#...#\n"
                                        + "#.#.#####.#########.#.#.#####.#.#.#.###.###.#.#.#.###.#.###.#.#.#######.###.###.#\n"
                                        + "#.#.#b..#...#..l....#.#.....#.#.#...#...#...#...#...#.#...#.#...#.....#.#...#...#\n"
                                        + "#.#.#.#.#####.#######.#####.###.#.###.#.#.#########.#.###.#.#######.#.#.#.#.#.###\n"
                                        + "#.#...#.......#.................#.....#@#@..............#...........#...#.#.....#\n"
                                        + "################################################################################\n"
                                        + "#...........#.....#...#.......#........@#@..#.....#...............#.....#.....#.#\n"
                                        + "#.#########.#.###.#.#.###.###.#.###.#####.#.#####.#.#############.#.###.#.#.#.#.#\n"
                                        + "#..t..#.#...#...#...#...#.#...#...#.....#.#.......#.#.#.....#...X.#...#.#.#.#.#.#\n"
                                        + "#.###.#.#.###.#.#######.#.#.#####.#####.#.#.#######.#.#.#.#.#.#######.#.###.#.#.#\n"
                                        + "#.#...#.#.#...#.....#.#...#.#...#.#.....#.#.#.......#...#.#.#.......#.#.#...#...#\n"
                                        + "#.#.###.#.#.#######.#.#####.#.#.#.#.#####.###.#########.#.#######.#.#.#.#.#####.#\n"
                                        + "#.#.#...#.#.#.......#...#...#.#...#.#...#.....#.......#.#...#...#.#...#.#.....#.#\n"
                                        + "#.#.#.###.###.#######.#.#.###.#####.#.#.#.#####.###.#.#.###.#.#.#.#####.#.###.###\n"
                                        + "#.#.#...#.#...#...#...#.#.#.......#...#.#.#...#.#...#.#.#...#.#.#.#.....#...#...#\n"
                                        + "#.#.###.#.#.###.#.#.#.#.#.###.#########.#.#.###.#.###.###.#.#.#.#.#.###########.#\n"
                                        + "#.#...#...#...#.#...#.#.#...#.#.........#.#.....#.#.....#.#.#.#.#.#...#.........#\n"
                                        + "#####.#.###.#.#.#####.#.###.#.#.#########.#.#####.#####.#.###.#.#####.#.#.#######\n"
                                        + "#...#.#...#.#.#...#...#...#.#.#.........#.#.....#.....#.#.#...#...#...#.#.......#\n"
                                        + "#.#.#.###.###.#.###.###.###.#.#########.#.#####.#####.#.#.#.#####.#.###.#######.#\n"
                                        + "#.#...#.#.....#.#...#...#...#...#.#...V.#.#...#.#.....#...#...#.#...#...#.....#.#\n"
                                        + "#.#####.#######.#.###.###.#####.#.#.###.#.#.###.#.#######.###.#.#####.###.#####.#\n"
                                        + "#...#...#.....#.#.#...#...#.......#...#.#.#.....#.#...#...#.#.#.....#.#...#...#.#\n"
                                        + "###.#.#.#.#.#.###.#####.#############.###.#####.#.#.#.#.###.#.#.#####.###.#.#.#.#\n"
                                        + "#.#.#.#...#.#...#.#.....#...........#...#.A...#.#.#.#.#.#...#.#.....#...#.#.#...#\n"
                                        + "#.#.#.#####.###.#.#.###.###.#######.###.#####.###.#.#.#.#.#.#.#.###.###.#.#.###.#\n"
                                        + "#.#.#...#...#.....#.#.#.....#.....#.....#...#.....#.#.#.#.#.#.#.#.#...#...#...#.#\n"
                                        + "#.#.###.#.#########.#.###########.#####.#.#.#######.#.#.###.###.#.#.#####.###.###\n"
                                        + "#.#...#.#...........#...#.........#.....#.#...#.....#.#...#...#...#.......#.#...#\n"
                                        + "#.###.#.#########.#####.###.###.###.#####.#.#.#####.#.###.#.#.#.#########.#.###.#\n"
                                        + "#...#.#...#.....#.#.#...#...#...#...#...#.#.#.....#.#...#.#.#.#.#.....W.#...#...#\n"
                                        + "#.#.#.#####.###.#.#.#.###.###.###.###.#.#.#.#####.###.###.#.#.#.#.#####.###.#.###\n"
                                        + "#.#.#.......#.#.#...#.#...#...#.#.#...#.#.#.....#.....#...#.#...#.#.....#...#...#\n"
                                        + "#.#.#########.#.#####.#.###.###.#.#####.#.#####.#####.#.#####.###.#.#######.###.#\n"
                                        + "#.#.#.#.......#.......#.#...#...#.....#.#.#...#.....#.#.....#...#.#.......#.#...#\n"
                                        + "#.#.#.#O#####.#########.#.#####.#####.#.#.###.#####.#######.###.#.#######.###.###\n"
                                        + "#.#...#.....#...#.......#.....#.....#.#.#...#...#.#.......#...#.#.....#.#...#...#\n"
                                        + "#.#########.###.#.#####.#####.#####.#.#.###.#.#.#.#######.###.#######.#.###.###.#\n"
                                        + "#.#.......#.#.#.#...#.#.#...#.......#.#.#.#.#.#.#.......#z..#.....#...#...#....o#\n"
                                        + "#.#.#####.#.#.#.###.#.#.#.#.#####.###.#.#.#.#.#.#.#.###.#.#######.#.###.#######.#\n"
                                        + "#.#...#.#...#.#...#...#...#.#.....#...#.#.#.#.#.#.#.#...#.#.......#.#.#...#.....#\n"
                                        + "#.###.#.#####.###.###.#####.#######.###.#.#.#.#.###.#.###.#Y#######.#.#.#.#.#####\n"
                                        + "#g....#.........#...#...#...#...#...#...#.#...#.....#.#.#.#...#.....#.#.#...#...#\n"
                                        + "#.###########.#.###.###.#.###.#.#.#####.#.###########.#.#.###.###.###.#.#######.#\n"
                                        + "#.............#...#..n..#.....#...H.....#...............#.........#...P.........#\n"
                                        + "#################################################################################";
}
