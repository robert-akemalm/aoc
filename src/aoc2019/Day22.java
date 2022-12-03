package aoc2019;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Day22 {
    public static void main(String[] args) {
        String input = INPUT;
        a(input);
        b(input);
    }

    private static void b(String input) {
        List<Op> ops = new ArrayList<>();
        String[] lines = input.split("\n");
        for (String line : lines) {
            if (line.equals("deal into new stack")) {
                ops.add(new DealIntoNewStack());
            } else if (line.startsWith("deal with increment")) {
                int n = Integer.parseInt(line.substring(line.lastIndexOf(" ") + 1));
                ops.add(new DealWithIncrement(n));
            } else if (line.startsWith("cut")) {
                int n = Integer.parseInt(line.substring(line.lastIndexOf(" ") + 1));
                ops.add(new Cut(n));
            }
        }

        int pos = 2020;
        BigInteger numCards = num(119315717514047L);
        BigInteger times = num(101741582076661L);
        Collections.reverse(ops);
        var calc = new BigInteger[] {BigInteger.ONE, BigInteger.ZERO};
        for (Op op : ops) {
            calc = op.exec(calc, numCards);
        }
        var pow = calc[0].modPow(times, numCards);
        BigInteger card = pow.multiply(num(pos))
                             .add(calc[1].multiply(pow.add(numCards).subtract(num(1)))
                                         .multiply(calc[0].subtract(num(1)).modPow(numCards.subtract(num(2)), numCards)))
                             .mod(numCards);
        System.out.println(card);
    }

    private static void a(String input) {
        int[] cards = IntStream.range(0, 10007).toArray();
//        int[] cards = IntStream.range(0, 10).toArray();
        List<Op> ops = new ArrayList<>();
        String[] lines = input.split("\n");
        for (String line : lines) {
            if (line.equals("deal into new stack")) {
                ops.add(new DealIntoNewStack());
            } else if (line.startsWith("deal with increment")) {
                int n = Integer.parseInt(line.substring(line.lastIndexOf(" ") + 1));
                ops.add(new DealWithIncrement(n));
            } else if (line.startsWith("cut")) {
                int n = Integer.parseInt(line.substring(line.lastIndexOf(" ") + 1));
                ops.add(new Cut(n));
            }
        }

        for (Op op : ops) {
            cards = op.execOn(cards);
        }
        for (int i = 0; i < cards.length; i++) {
            if (cards[i] == 2019) {
                System.out.println(i);
            }
        }
//        System.out.println(cards[2020]);
    }

    private abstract static class Op {
        public abstract int[] execOn(int[] cards);
        public abstract BigInteger[] exec(BigInteger[] input, BigInteger deckSize);
        }

    private static class Cut extends Op {
        final int n;

        private Cut(int n) {
            this.n = n;
        }

        @Override
        public int[] execOn(int[] cards) {
            int[] after = new int[cards.length];
            int n = this.n;
            if (n < 0) {
                n += cards.length;
            }
            for (int i = n; i < cards.length; i++) {
                after[i - n] = cards[i];
            }
            for (int i = 0; i < n; i++) {
                after[cards.length - n + i] = cards[i];
            }
            return after;
        }

        @Override
        public BigInteger[] exec(BigInteger[] input, BigInteger deckSize) {
                input[1] = input[1].add(num(n));
            return input;
        }

    }

    private static class DealWithIncrement extends Op {
        final int n;

        private DealWithIncrement(int n) {
            this.n = n;
        }

        @Override
        public int[] execOn(int[] cards) {
            int[] after = new int[cards.length];
            int ix = 0;
            for (int card : cards) {
                after[ix] = card;
                ix = (ix + n) % cards.length;
            }
            return after;
        }

        @Override
        public BigInteger[] exec(BigInteger[] input, BigInteger deckSize) {
            var p = num(n).modPow(deckSize.subtract(num(2)), deckSize);
            for (int i = 0; i < input.length; i++) {
                input[i] = input[i].multiply(p);
            }
            return input;
        }

    }

    private static class DealIntoNewStack extends Op {
        @Override
        public int[] execOn(int[] cards) {
            int[] after = new int[cards.length];
            for (int i = 0; i < cards.length; i++) {
                after[cards.length - i - 1] = cards[i];
            }
            return after;
        }

        @Override
        public BigInteger[] exec(BigInteger[] input, BigInteger deckSize) {
            input[0] = input[0].multiply(num(-1));
            input[1] = input[1].add(num(1)).multiply(num(-1));
            return input;
        }
    }

    private static BigInteger num(long n) {
        return new BigInteger(Long.toString(n));
    }

    private static final String TEST_INPUT = "deal into new stack\n"
                                             + "cut -2\n"
                                             + "deal with increment 7\n"
                                             + "cut 8\n"
                                             + "cut -4\n"
                                             + "deal with increment 7\n"
                                             + "cut 3\n"
                                             + "deal with increment 9\n"
                                             + "deal with increment 3\n"
                                             + "cut -1";

    public static final String INPUT = "cut 7167\n"
                                       + "deal with increment 59\n"
                                       + "cut 4836\n"
                                       + "deal into new stack\n"
                                       + "deal with increment 9\n"
                                       + "cut -4087\n"
                                       + "deal with increment 68\n"
                                       + "cut 227\n"
                                       + "deal with increment 71\n"
                                       + "cut -8524\n"
                                       + "deal into new stack\n"
                                       + "deal with increment 17\n"
                                       + "cut 441\n"
                                       + "deal with increment 30\n"
                                       + "cut -6438\n"
                                       + "deal with increment 24\n"
                                       + "deal into new stack\n"
                                       + "deal with increment 72\n"
                                       + "deal into new stack\n"
                                       + "deal with increment 51\n"
                                       + "cut 2636\n"
                                       + "deal with increment 59\n"
                                       + "deal into new stack\n"
                                       + "cut 5477\n"
                                       + "deal into new stack\n"
                                       + "cut -7129\n"
                                       + "deal with increment 54\n"
                                       + "cut -9355\n"
                                       + "deal with increment 64\n"
                                       + "cut 6771\n"
                                       + "deal with increment 71\n"
                                       + "cut 1585\n"
                                       + "deal with increment 61\n"
                                       + "cut 7973\n"
                                       + "deal with increment 62\n"
                                       + "cut 5741\n"
                                       + "deal with increment 42\n"
                                       + "cut 6630\n"
                                       + "deal with increment 12\n"
                                       + "cut 2023\n"
                                       + "deal with increment 68\n"
                                       + "cut -3696\n"
                                       + "deal with increment 5\n"
                                       + "cut 312\n"
                                       + "deal with increment 40\n"
                                       + "deal into new stack\n"
                                       + "deal with increment 4\n"
                                       + "deal into new stack\n"
                                       + "deal with increment 50\n"
                                       + "cut -1789\n"
                                       + "deal with increment 58\n"
                                       + "cut -902\n"
                                       + "deal with increment 71\n"
                                       + "cut -6724\n"
                                       + "deal with increment 43\n"
                                       + "deal into new stack\n"
                                       + "deal with increment 30\n"
                                       + "cut -5158\n"
                                       + "deal with increment 3\n"
                                       + "deal into new stack\n"
                                       + "cut -1662\n"
                                       + "deal into new stack\n"
                                       + "cut -8906\n"
                                       + "deal into new stack\n"
                                       + "deal with increment 35\n"
                                       + "cut -562\n"
                                       + "deal into new stack\n"
                                       + "cut 5473\n"
                                       + "deal with increment 53\n"
                                       + "cut 618\n"
                                       + "deal with increment 21\n"
                                       + "cut -9703\n"
                                       + "deal into new stack\n"
                                       + "deal with increment 62\n"
                                       + "cut 3906\n"
                                       + "deal with increment 8\n"
                                       + "cut -978\n"
                                       + "deal with increment 4\n"
                                       + "cut 139\n"
                                       + "deal with increment 2\n"
                                       + "cut 4352\n"
                                       + "deal with increment 66\n"
                                       + "cut 255\n"
                                       + "deal into new stack\n"
                                       + "deal with increment 18\n"
                                       + "deal into new stack\n"
                                       + "deal with increment 33\n"
                                       + "cut 9829\n"
                                       + "deal into new stack\n"
                                       + "deal with increment 7\n"
                                       + "cut -512\n"
                                       + "deal with increment 33\n"
                                       + "cut 3188\n"
                                       + "deal with increment 46\n"
                                       + "cut -6352\n"
                                       + "deal into new stack\n"
                                       + "cut -1271\n"
                                       + "deal with increment 9\n"
                                       + "cut -4747\n"
                                       + "deal with increment 6";
}
