package aoc2020;

import java.util.HashMap;
import java.util.Map;

public class Day10 {
    public static void main(String[] args) {
//        String input = TEST_INPUT;
        String input = INPUT;
        a(input);
        b(input);
    }


    private static void b(String input) {
        int[] adaptor = input.lines().mapToInt(Integer::parseInt).sorted().toArray();
        System.out.println(numWays(adaptor, 0, 0));
    }

    private static final Map<Integer, Map<Integer,Long>> CACHE = new HashMap<>();
    private static long numWays(int[] adaptors, int currentIx, int lastJolts) {
        if (currentIx == adaptors.length) {
            return 1;
        }
        Map<Integer, Long> subCache = CACHE.computeIfAbsent(currentIx, k -> new HashMap<>());
        Long cachedCount = subCache.get(lastJolts);
        if (cachedCount != null) {
            return cachedCount;
        }
        long cnt = 0;
        if (adaptors[currentIx] <= lastJolts + 3) {
            cnt += numWays(adaptors, currentIx + 1, adaptors[currentIx]);
            if (adaptors.length > currentIx + 1 && adaptors[currentIx + 1] <= lastJolts + 3) {
                cnt += numWays(adaptors, currentIx + 2, adaptors[currentIx+1]);
                if (adaptors.length > currentIx + 2 && adaptors[currentIx + 2] <= lastJolts + 3) {
                    cnt += numWays(adaptors, currentIx + 3, adaptors[currentIx + 2]);
                }
            }
        }
        subCache.put(lastJolts, cnt);
        return cnt;
    }

    private static void a(String input) {
        int[] adaptor = input.lines().mapToInt(Integer::parseInt).sorted().toArray();
        int lastJolts = 0;
        int num1s = 0;
        int num3s = 1;
        for (int i = 0; i < adaptor.length; i++) {
            int diff = adaptor[i] - lastJolts;
            if (diff == 1) {
                num1s++;
            } else if (diff == 3) {
                num3s++;
            }
            lastJolts = adaptor[i];
        }
        System.out.println(num1s * num3s);
    }

    private static final String TEST_INPUT = "";

    private static final String INPUT = "107\n"
                                        + "13\n"
                                        + "116\n"
                                        + "132\n"
                                        + "24\n"
                                        + "44\n"
                                        + "56\n"
                                        + "69\n"
                                        + "28\n"
                                        + "135\n"
                                        + "152\n"
                                        + "109\n"
                                        + "42\n"
                                        + "112\n"
                                        + "10\n"
                                        + "43\n"
                                        + "122\n"
                                        + "87\n"
                                        + "49\n"
                                        + "155\n"
                                        + "175\n"
                                        + "71\n"
                                        + "39\n"
                                        + "173\n"
                                        + "50\n"
                                        + "156\n"
                                        + "120\n"
                                        + "145\n"
                                        + "176\n"
                                        + "45\n"
                                        + "149\n"
                                        + "148\n"
                                        + "15\n"
                                        + "1\n"
                                        + "68\n"
                                        + "9\n"
                                        + "168\n"
                                        + "131\n"
                                        + "150\n"
                                        + "59\n"
                                        + "83\n"
                                        + "167\n"
                                        + "3\n"
                                        + "169\n"
                                        + "6\n"
                                        + "123\n"
                                        + "174\n"
                                        + "81\n"
                                        + "138\n"
                                        + "72\n"
                                        + "157\n"
                                        + "144\n"
                                        + "65\n"
                                        + "75\n"
                                        + "33\n"
                                        + "19\n"
                                        + "140\n"
                                        + "160\n"
                                        + "16\n"
                                        + "57\n"
                                        + "93\n"
                                        + "90\n"
                                        + "8\n"
                                        + "58\n"
                                        + "98\n"
                                        + "130\n"
                                        + "141\n"
                                        + "114\n"
                                        + "84\n"
                                        + "29\n"
                                        + "22\n"
                                        + "94\n"
                                        + "113\n"
                                        + "129\n"
                                        + "108\n"
                                        + "36\n"
                                        + "14\n"
                                        + "115\n"
                                        + "102\n"
                                        + "151\n"
                                        + "78\n"
                                        + "139\n"
                                        + "170\n"
                                        + "82\n"
                                        + "2\n"
                                        + "70\n"
                                        + "126\n"
                                        + "101\n"
                                        + "25\n"
                                        + "62\n"
                                        + "95\n"
                                        + "104\n"
                                        + "23\n"
                                        + "163\n"
                                        + "32\n"
                                        + "103\n"
                                        + "121\n"
                                        + "119\n"
                                        + "48\n"
                                        + "166\n"
                                        + "7\n"
                                        + "53";
}
