package aoc2020;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;

public class Day23 {
    public static void main(String[] args) {
        a(TEST_INPUT);
        a(INPUT);
        b(TEST_INPUT);
        b(INPUT);
    }

    private static void b(String input) {
        int[] in = input.chars().map(c -> c - '0').toArray();
        int[] data = Arrays.copyOf(in, 1_000_000);
        for (int i = in.length; i < data.length; i++) {
            data[i] = i + 1;
        }

        Map<Integer, Node> map = new HashMap<>();
        Node head = new Node(data[0]);
        map.put(head.val, head);
        Node prev = head;
        for (int i = 1; i < data.length; i++) {
            Node node = new Node(data[i]);
            map.put(node.val, node);
            prev.next = node;
            prev = node;
        }
        prev.next = head;

        for (int steps = 0; steps < 10_000_000; steps++) {
            Node threeHead = head.next;
            head.next = threeHead.next.next.next;
            int dest = head.val - 1;
            while (true) {
                if (dest == 0) {
                    dest = 1_000_000;
                } else if (threeHead.val == dest || threeHead.next.val == dest || threeHead.next.next.val == dest) {
                    dest--;
                } else {
                    break;
                }
            }
            Node destNode = map.get(dest);
            Node next = destNode.next;
            destNode.next = threeHead;
            threeHead.next.next.next = next;
            head = head.next;
        }

        Node one = map.get(1);
        long val = one.next.val;
        long val2 = one.next.next.val;
        System.out.println(val * val2);
    }

    private static class Node {
        final int val;
        Node next;

        private Node(int val) {
            this.val = val;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(val);
            Node next = this.next;
            while (next.val != this.val) {
                sb.append(next.val);
                next = next.next;
            }
            return sb.toString();
        }
    }

    private static void a(String input) {
        RingBuffer buffer = new RingBuffer(input);
        int[] three = new int[3];
        for (int steps = 0; steps < 100; steps++) {
            buffer.removeAfterHead(three);
            int dest = buffer.head() - 1;
            while (true) {
                boolean changed = false;
                for (int i = 0; i < three.length; i++) {
                    if (dest == three[i]) {
                        dest--;
                        changed = true;
                    }
                }
                if (dest == 0) {
                    dest = 9;
                } else if (!changed) {
                    break;
                }
            }
            buffer.addAfter(dest, three);
        }
        System.out.println(buffer.printFromOne());
    }

    private static class RingBuffer {
        int[] buffer;
        int[] buffer2;
        int length;

        RingBuffer(int[] vals) {
            buffer = vals;
            buffer2 = new int[buffer.length];
            length = buffer.length;
        }

        RingBuffer(String input) {
            buffer = input.chars().map(c -> c - '0').toArray();
            buffer2 = new int[buffer.length];
            length = buffer.length;
        }

        public int head() {
            return buffer[0];
        }

        public void removeAfterHead(int[] toFill) {
            for (int i = 0; i < toFill.length; i++) {
                toFill[i] = buffer[i + 1];
            }
            int ix = 0;
            buffer2[ix++] = head();
            for (int i = toFill.length + 1; i < buffer.length; i++) {
                buffer2[ix++] = buffer[i];
            }
            int[] tmp = buffer;
            buffer = buffer2;
            buffer2 = tmp;
            length -= toFill.length;
        }

        public void addAfter(int dest, int[] toAdd) {
            int buffer2Ix = 0;
            for (int i = 1; i < length; i++) {
                buffer2[buffer2Ix++] = buffer[i];
                if (buffer[i] == dest) {
                    for (int j = 0; j < toAdd.length; j++) {
                        buffer2[buffer2Ix++] = toAdd[j];
                    }
                }
            }
            buffer2[buffer2Ix] = buffer[0];
            int[] tmp = buffer;
            buffer = buffer2;
            buffer2 = tmp;
            length += toAdd.length;
        }

        public String printFromOne() {
            int oneIx = 0;
            for (; buffer[oneIx] != 1; oneIx++) {
            }
            StringBuilder sb = new StringBuilder();
            for (int i = oneIx + 1; i < length; i++) {
                sb.append(buffer[i]);
            }
            for (int i = 0; i < oneIx; i++) {
                sb.append(buffer[i]);
            }
            return sb.toString();
        }
    }

    private static final String TEST_INPUT = "389125467";

    private static final String INPUT = "784235916";
}
