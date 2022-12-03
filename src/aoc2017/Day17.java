package aoc2017;

public class Day17 {
    public static void main(String[] args) throws Exception {
        part1();
        part2();
    }

    private static void part1() throws Exception {
        Hurricane hurricane = new Hurricane(DATA);
        for (int i = 0; i < 2018; i++) {
            hurricane.add(i);
        }
        System.out.println(hurricane.current.next.value);
    }

    private static void part2() throws Exception {
        Hurricane hurricane = new Hurricane(DATA);
        for (int i = 0; i < 50_000_000; i++) {
            if ((i %1_000_000) == 0) {
                System.out.println(i / 1_000_000);
            }
            hurricane.add(i);
        }
        System.out.println(hurricane.zero.next.value);
    }

    static class Hurricane {
        Node current = null;
        Node zero = null;
        final int spins;

        Hurricane(int spins) {
            this.spins = spins;
        }

        void add(int value) {
            Node n = new Node(value);
            if (current == null) {
                zero = n;
                current = n;
                current.next = current;
            } else {
                for (int i = 0; i < spins; i++) {
                    current = current.next;
                }
                Node next = current.next;
                current.next = n;
                n.next = next;
                current = n;
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            Node itr = current;
            do {
                if (itr == current) {
                    sb.append("*");
                }
                sb.append(itr.value).append(",");
                itr = itr.next;
            } while (itr != current);
            return sb.toString();
        }
    }

    private static class Node {
        private final int value;
        Node next = null;

        private Node(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
    private static final int DATA2 = 3;

    private static final int DATA = 312;

}
