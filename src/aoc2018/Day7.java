package aoc2018;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class Day7 {
    public static void main(String[] args) {
        String input = INPUT;
        a(input);
        b(input);

    }

    private static void b(String input) {
        Map<Character,Node> nodes = new HashMap<>();
        String[] lines = Util.parseStrings(input);
        for (String line : lines) {
            Node first = nodes.computeIfAbsent(line.charAt(5), Node::new);
            Node later = nodes.computeIfAbsent(line.charAt(line.indexOf(" can") - 1), Node::new);
            later.addRequirement(first);
        }
        int delay = 60;

        int[] workers = new int[5];

        for (int time = 0; time < 26_000; time++) {
            if (nodes.isEmpty()) {
                System.out.println(time);
                return;
            }
            for (int worker = 0; worker < workers.length; worker++) {
                if (workers[worker] == 0) {
                    Node lowest = null;
                    for (Node node : nodes.values()) {
                        if (node.workerIx != -1 || !node.required.isEmpty()) {
                            continue;
                        }
                        if (lowest == null || node.c < lowest.c) {
                            lowest = node;
                        }
                    }
                    if (lowest != null) {
                        lowest.workerIx = worker;
                        workers[worker] = delay + 1 + lowest.c - 'A';
                    }
                }
            }

            for (int worker = 0; worker < workers.length; worker++) {
                if (workers[worker] == 1) {
                    Node toRemove = null;
                    for (Node node : nodes.values()) {
                        if (node.workerIx == worker) {
                            toRemove = node;
                        }
                    }

                    for (Node node : nodes.values()) {
                        node.required.remove(toRemove.c);
                    }

                    nodes.remove(toRemove.c);
                }
                if (workers[worker] > 0) {
                    workers[worker]--;
                }
            }
        }
    }

    // CABDFE
    private static void a(String input) {
        Map<Character,Node> nodes = new HashMap<>();
        String[] lines = Util.parseStrings(input);
        for (String line : lines) {
            Node first = nodes.computeIfAbsent(line.charAt(5), Node::new);
            Node later = nodes.computeIfAbsent(line.charAt(line.indexOf(" can") - 1), Node::new);
            later.addRequirement(first);
        }
        StringBuilder result = new StringBuilder();
        while (!nodes.isEmpty()) {
            char lowest = Character.MAX_VALUE;
            for (Node node : nodes.values()) {
                if (node.required.isEmpty() && node.c < lowest) {
                    lowest = node.c;
                }
            }

            for (Node node : nodes.values()) {
                node.required.remove(lowest);
            }

            nodes.remove(lowest);
            result.append(lowest);
        }
        System.out.println(result);
    }

    private static class Node {
        final Set<Character> required = new HashSet<>();
        final char c;
        public int workerIx = -1;

        private Node(char c) {
            this.c = c;
        }

        void addRequirement(Node node) {
            required.add(node.c);
        }

        @Override
        public int hashCode() {
            return c;
        }

        @Override
        public boolean equals(Object obj) {
            return c == ((Node) obj).c;
        }
    }

    private static final String TEST_INPUT = "Step C must be finished before step A can begin.\n"
                                             + "Step C must be finished before step F can begin.\n"
                                             + "Step A must be finished before step B can begin.\n"
                                             + "Step A must be finished before step D can begin.\n"
                                             + "Step B must be finished before step E can begin.\n"
                                             + "Step D must be finished before step E can begin.\n"
                                             + "Step F must be finished before step E can begin.";

    private static final String INPUT = "Step W must be finished before step B can begin.\n"
                                        + "Step G must be finished before step T can begin.\n"
                                        + "Step B must be finished before step P can begin.\n"
                                        + "Step R must be finished before step M can begin.\n"
                                        + "Step K must be finished before step Q can begin.\n"
                                        + "Step Z must be finished before step X can begin.\n"
                                        + "Step V must be finished before step S can begin.\n"
                                        + "Step D must be finished before step U can begin.\n"
                                        + "Step Y must be finished before step J can begin.\n"
                                        + "Step A must be finished before step C can begin.\n"
                                        + "Step M must be finished before step U can begin.\n"
                                        + "Step E must be finished before step X can begin.\n"
                                        + "Step T must be finished before step F can begin.\n"
                                        + "Step U must be finished before step C can begin.\n"
                                        + "Step C must be finished before step Q can begin.\n"
                                        + "Step S must be finished before step N can begin.\n"
                                        + "Step X must be finished before step H can begin.\n"
                                        + "Step F must be finished before step L can begin.\n"
                                        + "Step Q must be finished before step J can begin.\n"
                                        + "Step P must be finished before step J can begin.\n"
                                        + "Step I must be finished before step L can begin.\n"
                                        + "Step J must be finished before step L can begin.\n"
                                        + "Step L must be finished before step N can begin.\n"
                                        + "Step H must be finished before step O can begin.\n"
                                        + "Step N must be finished before step O can begin.\n"
                                        + "Step B must be finished before step S can begin.\n"
                                        + "Step A must be finished before step T can begin.\n"
                                        + "Step G must be finished before step K can begin.\n"
                                        + "Step Z must be finished before step N can begin.\n"
                                        + "Step V must be finished before step I can begin.\n"
                                        + "Step Z must be finished before step Q can begin.\n"
                                        + "Step I must be finished before step J can begin.\n"
                                        + "Step S must be finished before step I can begin.\n"
                                        + "Step P must be finished before step I can begin.\n"
                                        + "Step B must be finished before step C can begin.\n"
                                        + "Step M must be finished before step L can begin.\n"
                                        + "Step G must be finished before step Z can begin.\n"
                                        + "Step M must be finished before step C can begin.\n"
                                        + "Step U must be finished before step F can begin.\n"
                                        + "Step B must be finished before step Y can begin.\n"
                                        + "Step W must be finished before step U can begin.\n"
                                        + "Step G must be finished before step M can begin.\n"
                                        + "Step M must be finished before step J can begin.\n"
                                        + "Step C must be finished before step L can begin.\n"
                                        + "Step K must be finished before step D can begin.\n"
                                        + "Step S must be finished before step X can begin.\n"
                                        + "Step Q must be finished before step N can begin.\n"
                                        + "Step V must be finished before step N can begin.\n"
                                        + "Step R must be finished before step C can begin.\n"
                                        + "Step E must be finished before step H can begin.\n"
                                        + "Step D must be finished before step P can begin.\n"
                                        + "Step H must be finished before step N can begin.\n"
                                        + "Step X must be finished before step O can begin.\n"
                                        + "Step K must be finished before step Y can begin.\n"
                                        + "Step R must be finished before step F can begin.\n"
                                        + "Step L must be finished before step O can begin.\n"
                                        + "Step Y must be finished before step M can begin.\n"
                                        + "Step T must be finished before step I can begin.\n"
                                        + "Step T must be finished before step Q can begin.\n"
                                        + "Step B must be finished before step F can begin.\n"
                                        + "Step C must be finished before step N can begin.\n"
                                        + "Step V must be finished before step M can begin.\n"
                                        + "Step T must be finished before step N can begin.\n"
                                        + "Step S must be finished before step L can begin.\n"
                                        + "Step P must be finished before step H can begin.\n"
                                        + "Step X must be finished before step Q can begin.\n"
                                        + "Step Z must be finished before step I can begin.\n"
                                        + "Step Q must be finished before step O can begin.\n"
                                        + "Step I must be finished before step N can begin.\n"
                                        + "Step E must be finished before step P can begin.\n"
                                        + "Step R must be finished before step L can begin.\n"
                                        + "Step P must be finished before step L can begin.\n"
                                        + "Step T must be finished before step H can begin.\n"
                                        + "Step G must be finished before step X can begin.\n"
                                        + "Step J must be finished before step H can begin.\n"
                                        + "Step G must be finished before step V can begin.\n"
                                        + "Step K must be finished before step N can begin.\n"
                                        + "Step R must be finished before step Q can begin.\n"
                                        + "Step Z must be finished before step T can begin.\n"
                                        + "Step E must be finished before step F can begin.\n"
                                        + "Step Y must be finished before step H can begin.\n"
                                        + "Step P must be finished before step N can begin.\n"
                                        + "Step S must be finished before step O can begin.\n"
                                        + "Step L must be finished before step H can begin.\n"
                                        + "Step W must be finished before step E can begin.\n"
                                        + "Step X must be finished before step N can begin.\n"
                                        + "Step Z must be finished before step D can begin.\n"
                                        + "Step A must be finished before step H can begin.\n"
                                        + "Step T must be finished before step X can begin.\n"
                                        + "Step E must be finished before step Q can begin.\n"
                                        + "Step K must be finished before step U can begin.\n"
                                        + "Step M must be finished before step T can begin.\n"
                                        + "Step J must be finished before step O can begin.\n"
                                        + "Step D must be finished before step N can begin.\n"
                                        + "Step K must be finished before step A can begin.\n"
                                        + "Step G must be finished before step E can begin.\n"
                                        + "Step R must be finished before step H can begin.\n"
                                        + "Step W must be finished before step M can begin.\n"
                                        + "Step U must be finished before step N can begin.\n"
                                        + "Step Q must be finished before step H can begin.\n"
                                        + "Step Y must be finished before step A can begin.";
}
