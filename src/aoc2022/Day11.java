package aoc2022;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.LongFunction;

public class Day11 {
    public static void main(String[] args) {
        a(TEST_INPUT);
        a(INPUT);
        b(TEST_INPUT);
        b(INPUT);
    }

    private static void a(String input) {
        List<Monkey> monkeys = parse(input);
        for (int i = 0; i < 20; i++) {
            for (Monkey monkey : monkeys) {
                while (!monkey.items.isEmpty()) {
                    Item item = monkey.items.pollFirst();
                    monkey.inspect(item);
                    item.worryLevel /= 3;
                    int nextMonkey = monkey.test(item);
                    monkeys.get(nextMonkey).items.addLast(item);
                }
            }
        }
        long[] maxInspections = monkeys.stream()
                                       .sorted((o1, o2) -> Long.compare(o2.numInspections, o1.numInspections))
                                       .limit(2)
                                       .mapToLong(m -> m.numInspections)
                                       .toArray();
        System.out.println(maxInspections[0] * maxInspections[1]);
    }

    private static void b(String input) {
        List<Monkey> monkeys = parse(input);
        long globalDivisor = 1;
        for (Monkey monkey : monkeys) {
            globalDivisor *= monkey.divisor;
        }

        for (int i = 0; i < 10_000; i++) {
            for (Monkey monkey : monkeys) {
                while (!monkey.items.isEmpty()) {
                    Item item = monkey.items.pollFirst();
                    monkey.inspect(item);
                    int nextMonkey = monkey.test(item);
                    item.worryLevel = item.worryLevel % globalDivisor;
                    monkeys.get(nextMonkey).items.addLast(item);
                }
            }
        }

        long[] maxInspections = monkeys.stream()
                                       .sorted((o1, o2) -> Long.compare(o2.numInspections, o1.numInspections))
                                       .limit(2)
                                       .mapToLong(m -> m.numInspections)
                                       .toArray();
        System.out.println(maxInspections[0] * maxInspections[1]);
    }

    private static List<Monkey> parse(String input) {
        return Arrays.stream(input.split("\n\n")).map(Monkey::new).toList();
    }

    private static class Monkey {
        final int number;
        final LinkedList<Item> items = new LinkedList<>();
        final LongFunction<Long> operation;
        final LongFunction<Integer> test;
        final int divisor;
        long numInspections = 0;

        private Monkey(String input) {
            List<String> rows = input.lines().toList();
            this.number = Integer.parseInt(rows.get(0).replaceAll("\\D", ""));

            for (String item : rows.get(1).substring(rows.get(1).indexOf(':') + 2).split(", ")) {
                this.items.add(new Item(Integer.parseInt(item)));
            }

            String op = rows.get(2).split("= ")[1];
            String[] opParts = op.split(" ");
            if (opParts[1].equals("+")) {
                if (opParts[2].equals("old")) {
                    this.operation = old -> old + old;
                } else {
                    this.operation = old -> old + Long.parseLong(opParts[2]);
                }
            } else {
                if (opParts[2].equals("old")) {
                    this.operation = old -> old * old;
                } else {
                    this.operation = old -> old * Long.parseLong(opParts[2]);
                }
            }

            divisor = extractInt(rows.get(3));
            int trueMonkey = extractInt(rows.get(4));
            int falseMonkey = extractInt(rows.get(5));
            this.test = val -> val % divisor == 0 ? trueMonkey : falseMonkey;
        }

        static int extractInt(String s) {
            return Integer.parseInt(s.replaceAll("\\D", ""));
        }

        void inspect(Item item) {
            numInspections++;
            item.worryLevel = operation.apply(item.worryLevel);
        }

        int test(Item item) {
            return test.apply(item.worryLevel);
        }
    }

    private static class Item {
        long worryLevel;

        private Item(int worryLevel) {
            this.worryLevel = worryLevel;
        }
    }

    private static final String TEST_INPUT = "Monkey 0:\n"
                                             + "  Starting items: 79, 98\n"
                                             + "  Operation: new = old * 19\n"
                                             + "  Test: divisible by 23\n"
                                             + "    If true: throw to monkey 2\n"
                                             + "    If false: throw to monkey 3\n"
                                             + "\n"
                                             + "Monkey 1:\n"
                                             + "  Starting items: 54, 65, 75, 74\n"
                                             + "  Operation: new = old + 6\n"
                                             + "  Test: divisible by 19\n"
                                             + "    If true: throw to monkey 2\n"
                                             + "    If false: throw to monkey 0\n"
                                             + "\n"
                                             + "Monkey 2:\n"
                                             + "  Starting items: 79, 60, 97\n"
                                             + "  Operation: new = old * old\n"
                                             + "  Test: divisible by 13\n"
                                             + "    If true: throw to monkey 1\n"
                                             + "    If false: throw to monkey 3\n"
                                             + "\n"
                                             + "Monkey 3:\n"
                                             + "  Starting items: 74\n"
                                             + "  Operation: new = old + 3\n"
                                             + "  Test: divisible by 17\n"
                                             + "    If true: throw to monkey 0\n"
                                             + "    If false: throw to monkey 1";

    private static final String INPUT = "Monkey 0:\n"
                                        + "  Starting items: 52, 60, 85, 69, 75, 75\n"
                                        + "  Operation: new = old * 17\n"
                                        + "  Test: divisible by 13\n"
                                        + "    If true: throw to monkey 6\n"
                                        + "    If false: throw to monkey 7\n"
                                        + "\n"
                                        + "Monkey 1:\n"
                                        + "  Starting items: 96, 82, 61, 99, 82, 84, 85\n"
                                        + "  Operation: new = old + 8\n"
                                        + "  Test: divisible by 7\n"
                                        + "    If true: throw to monkey 0\n"
                                        + "    If false: throw to monkey 7\n"
                                        + "\n"
                                        + "Monkey 2:\n"
                                        + "  Starting items: 95, 79\n"
                                        + "  Operation: new = old + 6\n"
                                        + "  Test: divisible by 19\n"
                                        + "    If true: throw to monkey 5\n"
                                        + "    If false: throw to monkey 3\n"
                                        + "\n"
                                        + "Monkey 3:\n"
                                        + "  Starting items: 88, 50, 82, 65, 77\n"
                                        + "  Operation: new = old * 19\n"
                                        + "  Test: divisible by 2\n"
                                        + "    If true: throw to monkey 4\n"
                                        + "    If false: throw to monkey 1\n"
                                        + "\n"
                                        + "Monkey 4:\n"
                                        + "  Starting items: 66, 90, 59, 90, 87, 63, 53, 88\n"
                                        + "  Operation: new = old + 7\n"
                                        + "  Test: divisible by 5\n"
                                        + "    If true: throw to monkey 1\n"
                                        + "    If false: throw to monkey 0\n"
                                        + "\n"
                                        + "Monkey 5:\n"
                                        + "  Starting items: 92, 75, 62\n"
                                        + "  Operation: new = old * old\n"
                                        + "  Test: divisible by 3\n"
                                        + "    If true: throw to monkey 3\n"
                                        + "    If false: throw to monkey 4\n"
                                        + "\n"
                                        + "Monkey 6:\n"
                                        + "  Starting items: 94, 86, 76, 67\n"
                                        + "  Operation: new = old + 1\n"
                                        + "  Test: divisible by 11\n"
                                        + "    If true: throw to monkey 5\n"
                                        + "    If false: throw to monkey 2\n"
                                        + "\n"
                                        + "Monkey 7:\n"
                                        + "  Starting items: 57\n"
                                        + "  Operation: new = old + 2\n"
                                        + "  Test: divisible by 17\n"
                                        + "    If true: throw to monkey 6\n"
                                        + "    If false: throw to monkey 2";
}
