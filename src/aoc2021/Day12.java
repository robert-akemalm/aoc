package aoc2021;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Day12 {
    public static void main(String[] args) {
        a(TEST_INPUT);
        a(TEST_INPUT_2);
        a(INPUT);
        b(TEST_INPUT);
        b(TEST_INPUT_2);
        b(INPUT);
    }


    private static void b(String input) {
        Map<String, Cave> caves = new HashMap<>();
        for (String row : input.lines().toArray(String[]::new)) {
            String[] parts = row.split("-");
            Cave a = caves.computeIfAbsent(parts[0], Cave::new);
            Cave b = caves.computeIfAbsent(parts[1], Cave::new);
            a.neighbours.add(b);
            b.neighbours.add(a);
        }
        Cave start = caves.get("start");
        Cave end = caves.get("end");

        Stack<Cave> path = new Stack<>();
        path.push(start);
        int numPaths = explore2(path, end);
        System.out.println(numPaths);
//        System.out.println(paths.stream().collect(Collectors.joining("\n")));
        System.out.println();
        System.out.println();
    }

    static Set<String> paths = new TreeSet<>();
    private static int explore2(Stack<Cave> path, Cave end) {
        int count = 0;
        Cave peek = path.peek();
        for (Cave neighbour : peek.neighbours) {
            boolean explore = neighbour.big ||!path.contains(neighbour);
            if (!explore && neighbour !=path.firstElement()) {
                Set<String> small = new HashSet<>();
                boolean hasDuplicateSmall = false;
                for (Cave cave : path) {
                    if (!cave.big) {
                        if(!small.add(cave.name)) {
                            hasDuplicateSmall = true;
                        }
                    }
                }
                if (!hasDuplicateSmall) {
                    explore = true;
                }
            }
            if (explore) {
                if (neighbour == end) {
                    count++;
                    paths.add(path.toString());
                } else {
                    path.push(neighbour);
                    count += explore2(path, end);
                }
            }
        }
        path.pop();
        return count;
    }

    private static void a(String input) {
        Map<String, Cave> caves = new HashMap<>();
        for (String row : input.lines().toArray(String[]::new)) {
            String[] parts = row.split("-");
            Cave a = caves.computeIfAbsent(parts[0], Cave::new);
            Cave b = caves.computeIfAbsent(parts[1], Cave::new);
            a.neighbours.add(b);
            b.neighbours.add(a);
        }
        Cave start = caves.get("start");
        Cave end = caves.get("end");

        Stack<Cave> path = new Stack<>();
        path.push(start);
        int numPaths = explore(path, end);
        System.out.println(numPaths);
    }

    private static int explore(Stack<Cave> path, Cave end) {
        int count = 0;
        Cave peek = path.peek();
        for (Cave neighbour : peek.neighbours) {
            if (neighbour.big||!path.contains(neighbour)) {
                if (neighbour == end) {
                    count++;
                } else {
                    path.push(neighbour);
                    count += explore(path, end);
                }
            }
        }
        path.pop();
        return count;
    }

    static class Cave {
        List<Cave> neighbours = new ArrayList<>();
        final boolean big;
        final String name;

        public Cave(String name) {
            this.name = name;
            this.big = name.equals(name.toUpperCase());
        }

        @Override
        public String toString() {
            return name;
        }
    }
    private static final String TEST_INPUT = "start-A\n"
                                             + "start-b\n"
                                             + "A-c\n"
                                             + "A-b\n"
                                             + "b-d\n"
                                             + "A-end\n"
                                             + "b-end";

    private static final String TEST_INPUT_2 = "dc-end\n"
                                               + "HN-start\n"
                                               + "start-kj\n"
                                               + "dc-start\n"
                                               + "dc-HN\n"
                                               + "LN-dc\n"
                                               + "HN-end\n"
                                               + "kj-sa\n"
                                               + "kj-HN\n"
                                               + "kj-dc";

    private static final String INPUT = "KF-sr\n"
                                        + "OO-vy\n"
                                        + "start-FP\n"
                                        + "FP-end\n"
                                        + "vy-mi\n"
                                        + "vy-KF\n"
                                        + "vy-na\n"
                                        + "start-sr\n"
                                        + "FP-lh\n"
                                        + "sr-FP\n"
                                        + "na-FP\n"
                                        + "end-KF\n"
                                        + "na-mi\n"
                                        + "lh-KF\n"
                                        + "end-lh\n"
                                        + "na-start\n"
                                        + "wp-KF\n"
                                        + "mi-KF\n"
                                        + "vy-sr\n"
                                        + "vy-lh\n"
                                        + "sr-mi";

}
