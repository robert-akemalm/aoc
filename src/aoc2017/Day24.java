package aoc2017;

import java.util.ArrayList;
import java.util.List;

public class Day24 {
    public static void main(String[] args) throws Exception {
        part1();
        part2();
    }

    private static void part1() throws Exception {
        List<Node> nodes = parse(DATA);
        int max = search(nodes, 0);
        System.out.println(max);
    }

    private static int search(List<Node> nodes, int endPoint) {
        int max = 0;
        for (Node node : nodes) {
            if (node.a == endPoint || node.b == endPoint) {
                List<Node> subNodes = new ArrayList<>(nodes);
                subNodes.remove(node);
                int newEndPoint = node.a == endPoint ? node.b : node.a;
                int sum = search(subNodes, newEndPoint);
                sum += node.a + node.b;
                max = Math.max(max, sum);
            }
        }
        return max;
    }

    private static void part2() throws Exception {
        List<Node> nodes = parse(DATA);
        LengthWeight max = search2(nodes, 0);
        System.out.println(max.weight);
    }

    private static LengthWeight search2(List<Node> nodes, int endPoint) {
        LengthWeight max = new LengthWeight(0, 0);
        for (Node node : nodes) {
            if (node.a == endPoint || node.b == endPoint) {
                List<Node> subNodes = new ArrayList<>(nodes);
                subNodes.remove(node);
                int newEndPoint = node.a == endPoint ? node.b : node.a;
                LengthWeight sum = search2(subNodes, newEndPoint);
                sum = new LengthWeight(sum.length + 1, sum.weight + node.a + node.b);
                if (sum.length > max.length ) {
                    max = sum;
                } else if (sum.length == max.length && sum.weight > max.weight) {
                    max = sum;
                }
            }
        }
        return max;
    }

    static class LengthWeight {
        final int length;
        final int weight;

        LengthWeight(int length, int weight) {
            this.length = length;
            this.weight = weight;
        }
    }
    static List<Node> parse(String data) {
        List<Node> nodes = new ArrayList<>();
        for (String line : data.split("\n")) {
            int a = Integer.parseInt(line.substring(0, line.indexOf("/")));
            int b = Integer.parseInt(line.substring(line.indexOf("/") + 1));
            nodes.add(new Node(a, b));
        }
        return nodes;
    }

    static class Node {
        final int a;
        final int b;

        Node(int a, int b) {
            this.a = a;
            this.b = b;
        }
    }

    private static final String DATA2 = ""
                                        + "0/2\n"
                                        + "2/2\n"
                                        + "2/3\n"
                                        + "3/4\n"
                                        + "3/5\n"
                                        + "0/1\n"
                                        + "10/1\n"
                                        + "9/10";

    private static final String DATA = ""
                                       + "14/42\n"
                                       + "2/3\n"
                                       + "6/44\n"
                                       + "4/10\n"
                                       + "23/49\n"
                                       + "35/39\n"
                                       + "46/46\n"
                                       + "5/29\n"
                                       + "13/20\n"
                                       + "33/9\n"
                                       + "24/50\n"
                                       + "0/30\n"
                                       + "9/10\n"
                                       + "41/44\n"
                                       + "35/50\n"
                                       + "44/50\n"
                                       + "5/11\n"
                                       + "21/24\n"
                                       + "7/39\n"
                                       + "46/31\n"
                                       + "38/38\n"
                                       + "22/26\n"
                                       + "8/9\n"
                                       + "16/4\n"
                                       + "23/39\n"
                                       + "26/5\n"
                                       + "40/40\n"
                                       + "29/29\n"
                                       + "5/20\n"
                                       + "3/32\n"
                                       + "42/11\n"
                                       + "16/14\n"
                                       + "27/49\n"
                                       + "36/20\n"
                                       + "18/39\n"
                                       + "49/41\n"
                                       + "16/6\n"
                                       + "24/46\n"
                                       + "44/48\n"
                                       + "36/4\n"
                                       + "6/6\n"
                                       + "13/6\n"
                                       + "42/12\n"
                                       + "29/41\n"
                                       + "39/39\n"
                                       + "9/3\n"
                                       + "30/2\n"
                                       + "25/20\n"
                                       + "15/6\n"
                                       + "15/23\n"
                                       + "28/40\n"
                                       + "8/7\n"
                                       + "26/23\n"
                                       + "48/10\n"
                                       + "28/28\n"
                                       + "2/13\n"
                                       + "48/14";
}
