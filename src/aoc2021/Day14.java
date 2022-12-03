package aoc2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Day14 {
    public static void main(String[] args) {
        a(TEST_INPUT);
        a(INPUT);
        b(TEST_INPUT);
        b(INPUT);
    }

    static void b(String input) {
        List<String> lines = input.lines().toList();
        String start = lines.get(0);
        Map<String, String> dict = lines.stream()
                                        .skip(2)
                                        .map(s -> s.split(" -> "))
                                        .collect(Collectors.toMap(s -> s[0], s -> s[1]));
        Map<String, Long> pairs = new HashMap<>();
        for (int i = 0; i < start.length() - 1; i++) {
            pairs.merge(start.substring(i, i + 2), 1L, Long::sum);
        }
        Map<String, Long> letters = start.chars()
                                         .boxed()
                                         .map(Character::toString)
                                         .collect(Collectors.toMap(x -> x, x -> 1L, Long::sum));

        for (int i = 0; i < 40; i++) {
            Map<String, Long> np = new HashMap<>();
            for (Entry<String, Long> e : pairs.entrySet()) {
                String pair = e.getKey();
                Long c = e.getValue();
                String m = dict.get(pair);
                String l = pair.substring(0, 1);
                String r = pair.substring(1, 2);

                np.merge(l + m, c, Long::sum);
                np.merge(m + r, c, Long::sum);
                letters.merge(m, c, Long::sum);
            }
            pairs = np;
        }
        List<Long> freq = letters.values().stream().sorted().toList();
        System.out.println(freq.get(freq.size() - 1) - freq.get(0));
    }

    private static void a(String input) {
        List<String> lines = input.lines().toList();
        String start = lines.get(0);
        List<Rule> rules = lines.stream().filter(l -> l.contains("->")).map(Rule::parse).toList();
        List<Character> chars = new ArrayList<>();
        for (int i = 0; i < start.length(); i++) {
            chars.add(start.charAt(i));
        }
        for (int i = 0; i < 40; i++) {
            List<Character> next = new ArrayList<>();
            next.add(chars.get(0));
            for (int j = 0; j < chars.size() - 1; j++) {
                char a = chars.get(j);
                char b = chars.get(j + 1);
                String rule = a + "" + b;
                ;
                Rule match = rules.stream().filter(r -> r.pair.equals(rule)).findFirst().orElse(null);
                if (match != null) {
                    next.add(match.insert);
                }
                next.add(b);
            }
            chars = next;
        }
        List<Character> characters = input.chars().mapToObj(i -> Character.valueOf((char) i)).distinct().toList();
        long[] cnt = new long[characters.size()];
        chars.forEach(c -> cnt[characters.indexOf(c)]++);
        int minIx = 0;
        int maxIx = 0;
        for (int i = 0; i < characters.size(); i++) {
            if (cnt[i] == 0) {
                continue;
            }
            if (cnt[i] < cnt[minIx]) {
                minIx = i;
            }
            if (cnt[i] > cnt[minIx]) {
                maxIx = i;
            }
        }
        System.out.println(characters.get(minIx) + "[" + cnt[minIx] + "]  " + characters.get(maxIx) + "[" + cnt[maxIx] + "]  ");
        long max = Arrays.stream(cnt).max().getAsLong();
        long min = Arrays.stream(cnt).filter(i -> i > 0).min().getAsLong();
        System.out.println(max - min);
    }

    record Rule(String pair, char insert) {
        static Rule parse(String s) {
            String[] parts = s.split(" -> ");
            return new Rule(parts[0], parts[1].charAt(0));
        }
    }

    private static final String TEST_INPUT = "NNCB\n"
                                             + "\n"
                                             + "CH -> B\n"
                                             + "HH -> N\n"
                                             + "CB -> H\n"
                                             + "NH -> C\n"
                                             + "HB -> C\n"
                                             + "HC -> B\n"
                                             + "HN -> C\n"
                                             + "NN -> C\n"
                                             + "BH -> H\n"
                                             + "NC -> B\n"
                                             + "NB -> B\n"
                                             + "BN -> B\n"
                                             + "BB -> N\n"
                                             + "BC -> B\n"
                                             + "CC -> N\n"
                                             + "CN -> C";

    private static final String INPUT = "VHCKBFOVCHHKOHBPNCKO\n"
                                        + "\n"
                                        + "SO -> F\n"
                                        + "OP -> V\n"
                                        + "NF -> F\n"
                                        + "BO -> V\n"
                                        + "BH -> S\n"
                                        + "VB -> B\n"
                                        + "SV -> B\n"
                                        + "BK -> S\n"
                                        + "KC -> N\n"
                                        + "SP -> O\n"
                                        + "CP -> O\n"
                                        + "VN -> O\n"
                                        + "HO -> S\n"
                                        + "PC -> B\n"
                                        + "CS -> O\n"
                                        + "PO -> K\n"
                                        + "KF -> B\n"
                                        + "BP -> K\n"
                                        + "VO -> O\n"
                                        + "HB -> N\n"
                                        + "PH -> O\n"
                                        + "FF -> O\n"
                                        + "FB -> K\n"
                                        + "CC -> H\n"
                                        + "FK -> F\n"
                                        + "HV -> P\n"
                                        + "CO -> S\n"
                                        + "OC -> N\n"
                                        + "KV -> V\n"
                                        + "SS -> O\n"
                                        + "FC -> O\n"
                                        + "NP -> B\n"
                                        + "OH -> B\n"
                                        + "OF -> K\n"
                                        + "KB -> K\n"
                                        + "BN -> C\n"
                                        + "OK -> C\n"
                                        + "NC -> O\n"
                                        + "NO -> O\n"
                                        + "FS -> C\n"
                                        + "VP -> K\n"
                                        + "KP -> S\n"
                                        + "VS -> B\n"
                                        + "VV -> N\n"
                                        + "NN -> P\n"
                                        + "KH -> P\n"
                                        + "OB -> H\n"
                                        + "HP -> H\n"
                                        + "KK -> H\n"
                                        + "FH -> F\n"
                                        + "KS -> V\n"
                                        + "BS -> V\n"
                                        + "SN -> H\n"
                                        + "CB -> B\n"
                                        + "HN -> K\n"
                                        + "SB -> O\n"
                                        + "OS -> K\n"
                                        + "BC -> H\n"
                                        + "OV -> N\n"
                                        + "PN -> B\n"
                                        + "VH -> N\n"
                                        + "SK -> C\n"
                                        + "PV -> K\n"
                                        + "VC -> N\n"
                                        + "PF -> S\n"
                                        + "NB -> B\n"
                                        + "PP -> S\n"
                                        + "NS -> F\n"
                                        + "PB -> B\n"
                                        + "CV -> C\n"
                                        + "HK -> P\n"
                                        + "PK -> S\n"
                                        + "NH -> B\n"
                                        + "SH -> V\n"
                                        + "KO -> H\n"
                                        + "NV -> B\n"
                                        + "HH -> V\n"
                                        + "FO -> O\n"
                                        + "CK -> O\n"
                                        + "VK -> F\n"
                                        + "HF -> O\n"
                                        + "BF -> C\n"
                                        + "BV -> P\n"
                                        + "KN -> K\n"
                                        + "VF -> C\n"
                                        + "FN -> V\n"
                                        + "ON -> C\n"
                                        + "SF -> F\n"
                                        + "SC -> C\n"
                                        + "OO -> S\n"
                                        + "FP -> K\n"
                                        + "PS -> C\n"
                                        + "NK -> O\n"
                                        + "BB -> V\n"
                                        + "HC -> H\n"
                                        + "FV -> V\n"
                                        + "CH -> N\n"
                                        + "HS -> V\n"
                                        + "CF -> F\n"
                                        + "CN -> S";
}
