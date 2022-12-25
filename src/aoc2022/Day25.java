package aoc2022;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day25 {
    public static void main(String[] args) {
        Util.time(() -> a(TEST_INPUT));
        Util.time(() -> a(INPUT));
    }

    private static void a(String input) {
        long sum = input.lines().map(Snafu::valueOf).mapToLong(Snafu::longValue).sum();
        System.out.println(sum + " = " + Snafu.valueOf(sum));
    }

    private record Snafu(List<Digit> digits) {
        static Snafu valueOf(String input) {
            return new Snafu(input.chars().mapToObj(ch -> Digit.valueOf((char) ch)).toList());
        }

        static Snafu valueOf(long value) {
            List<Digit> digits = new ArrayList<>();
            while (value != 0) {
                int d = (int) (value % 5);
                Digit digit = Digit.valueOf(d == 3 ? -2 : d == 4 ? -1 : d);
                digits.add(0, digit);
                value /= 5;

                if (digit == Digit.MINUS || digit == Digit.MINUS_2) {
                    value++;
                }
            }
            return new Snafu(digits);
        }

        @Override
        public String toString() {
            return digits.stream().map(d -> String.valueOf(d.ch)).collect(Collectors.joining());
        }

        long longValue() {
            return IntStream.range(0, digits.size())
                            .mapToLong(ix -> (long) Math.pow(5, digits.size() - ix - 1) * digits.get(ix).val)
                            .sum();
        }
    }

    private enum Digit {
        MINUS_2(-2, '='),
        MINUS(-1, '-'),
        ZERO(0, '0'),
        ONE(1, '1'),
        TWO(2, '2');

        final int val;
        final char ch;

        Digit(int val, char ch) {
            this.val = val;
            this.ch = ch;
        }

        static Digit valueOf(char ch) {
            return Stream.of(values()).filter(d -> d.ch == ch).findAny().orElseThrow(IllegalArgumentException::new);
        }

        static Digit valueOf(int val) {
            return Stream.of(values()).filter(d -> d.val == val).findAny().orElseThrow(IllegalArgumentException::new);
        }
    }

    private static final String TEST_INPUT = "1=-0-2\n"
                                             + "12111\n"
                                             + "2=0=\n"
                                             + "21\n"
                                             + "2=01\n"
                                             + "111\n"
                                             + "20012\n"
                                             + "112\n"
                                             + "1=-1=\n"
                                             + "1-12\n"
                                             + "12\n"
                                             + "1=\n"
                                             + "122";

    private static final String INPUT = "2112\n"
                                        + "1=210=2\n"
                                        + "1---22==12012-2=2=\n"
                                        + "1=1\n"
                                        + "21=-0\n"
                                        + "2-2=02-11==\n"
                                        + "210=-\n"
                                        + "202=11=-=12-2=\n"
                                        + "1-=01-=-1=-\n"
                                        + "1==1=20=1=12-0=2-2\n"
                                        + "10==0==222=-1\n"
                                        + "1100-20-=\n"
                                        + "1=0==01-0=-1-00-201=\n"
                                        + "100=-222\n"
                                        + "21--20-==01-01-022\n"
                                        + "2100-1=2-==11\n"
                                        + "1=-0=21=2=1112102-\n"
                                        + "2==02220200=-100\n"
                                        + "1=\n"
                                        + "1=12----0==121\n"
                                        + "221===\n"
                                        + "10--21-21-2010\n"
                                        + "120-000122-20122-=\n"
                                        + "1-2=01020-=--2\n"
                                        + "100-0-12=0101=1==2\n"
                                        + "220=-2-011-=12=0\n"
                                        + "1=-=0022000-2==-\n"
                                        + "10=-01\n"
                                        + "1-00-02122000=-\n"
                                        + "1===000=0\n"
                                        + "1111\n"
                                        + "122--=-0-2--001=0\n"
                                        + "1==020--22\n"
                                        + "1020-=2-1-1=010=0\n"
                                        + "1=-=-=02-=\n"
                                        + "2-10211-1===1\n"
                                        + "1-12-=1-11-2002\n"
                                        + "10110-\n"
                                        + "11=1=0-\n"
                                        + "12-\n"
                                        + "102-2=22=2-=-022\n"
                                        + "1=-2-0=2--=\n"
                                        + "2=21=2=0-=\n"
                                        + "2=-2212=\n"
                                        + "1=-110222=-1-2==-1\n"
                                        + "11122\n"
                                        + "1=-\n"
                                        + "2=01-1=-\n"
                                        + "1=2=10-202==0===-0=\n"
                                        + "2---\n"
                                        + "1===20=--1\n"
                                        + "1-==--1==-2-1=-2=0\n"
                                        + "10=-=\n"
                                        + "1-1=2=\n"
                                        + "110\n"
                                        + "121==0\n"
                                        + "22000=20=\n"
                                        + "1-1=-0-2\n"
                                        + "20-2-21=0022-22100\n"
                                        + "1=010-=21-=12-1\n"
                                        + "22021000222\n"
                                        + "10=20=2-111\n"
                                        + "12--20=2-20\n"
                                        + "100-==2-=10=2-1-\n"
                                        + "1120012\n"
                                        + "1=1202---01-1201=\n"
                                        + "200\n"
                                        + "100=0\n"
                                        + "1=10=01-2-=20=01=\n"
                                        + "1-=--22\n"
                                        + "1-0121-==10\n"
                                        + "110=1-1\n"
                                        + "2-210-=0=10=1-\n"
                                        + "111-=0-=0=2---\n"
                                        + "1=1210==1=-1=11--\n"
                                        + "10-01222=011=0=0=\n"
                                        + "2120\n"
                                        + "12-11-=212\n"
                                        + "1==11-2-210----2\n"
                                        + "12\n"
                                        + "2012=-100-02-0-121-\n"
                                        + "11-2021220-=0\n"
                                        + "12-0-00-222\n"
                                        + "21-1==22021-\n"
                                        + "20-0\n"
                                        + "1011-=1-10-=\n"
                                        + "2-00-0\n"
                                        + "20-2=1\n"
                                        + "1102-=1-==211\n"
                                        + "1102-2\n"
                                        + "21=\n"
                                        + "10100-1-1\n"
                                        + "1=2-1-0\n"
                                        + "1--2===0\n"
                                        + "1-=01=2=-\n"
                                        + "10-1=-202==01-1\n"
                                        + "1-01112=2010=-=101\n"
                                        + "211==0-0===0==1-\n"
                                        + "1==220\n"
                                        + "1-=1=\n"
                                        + "1-0110-12\n"
                                        + "21-==\n"
                                        + "21202011--0-\n"
                                        + "2-2\n"
                                        + "2=\n"
                                        + "200-=-2--1102100-0\n"
                                        + "1-00=2-12\n"
                                        + "1=-0--=-220212\n"
                                        + "1-=210-=\n"
                                        + "12110-00212--=\n"
                                        + "20=1\n"
                                        + "21=2=0=00\n"
                                        + "1=22--=2==20-=\n"
                                        + "111-1-0211-=000=01\n"
                                        + "2-12\n"
                                        + "10-2-120===22\n"
                                        + "21-00\n"
                                        + "1=2\n"
                                        + "1-=2-\n"
                                        + "1=-2--100202-=\n"
                                        + "2=101122\n"
                                        + "11=21----1--\n"
                                        + "1---\n"
                                        + "12-12\n"
                                        + "1=2=0--01=---212";
}
