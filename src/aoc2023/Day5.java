package aoc2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day5 {
    private static void a(Input input) {
        long[] seeds = input.seeds;
        String source = "seed";
        while (!source.equals("location")) {
            String finalSource = source;
            Mappings mappings = input.maps.stream().filter(m -> m.source.equals(finalSource)).findAny().orElseThrow();
            for (int i = 0; i < seeds.length; i++) {
                seeds[i] = mappings.map(seeds[i]);
            }
            source = mappings.destination;
        }
        long minLocation = Arrays.stream(seeds).min().orElseThrow();
        System.out.println(minLocation);
    }

    private static void b(Input input) {
        long[] seeds = input.seeds;
        List<SeedRange> ranges = new ArrayList<>();
        for (int i = 0; i < seeds.length; i += 2) {
            ranges.add(new SeedRange(seeds[i], seeds[i] + seeds[i + 1]));
        }
        String source = "seed";
        while (!source.equals("location")) {
            String finalSource = source;
            Mappings mappings = input.maps.stream().filter(m -> m.source.equals(finalSource)).findAny().orElseThrow();
            List<SeedRange> next = new ArrayList<>();
            for (SeedRange range : ranges) {
                next.addAll(mappings.map(range));
            }
            source = mappings.destination;
            ranges = next;
        }
        long minLocation = ranges.stream().mapToLong(r -> r.start).min().orElseThrow();
        System.out.println(minLocation);

    }

    record SeedRange(long start, long end) {}

    record Mappings(String source, String destination, List<Mapping> mappings) {
        long map(long value) {
            for (Mapping mapping : mappings) {
                if (mapping.source <= value && value < mapping.source + mapping.length) {
                    return mapping.destination + value - mapping.source;
                }
            }
            return value;
        }

        List<SeedRange> map(SeedRange range) {
            List<SeedRange> ranges = List.of(range);
            for (Mapping mapping : mappings) {
                ranges = mapping.splitRanges(ranges);
            }

            for (int i = 0; i < ranges.size(); i++) {
                SeedRange seedRange = ranges.get(i);
                for (Mapping mapping : mappings) {
                    if (mapping.intersects(seedRange)) {
                        long diff = mapping.destination - mapping.source;
                        ranges.set(i, new SeedRange(seedRange.start + diff, seedRange.end + diff));
                    }
                }
            }
            return ranges;
        }

    }

    record Mapping(long destination, long source, long length) {
        boolean intersects(SeedRange range) {
            return range.start < source + length && range.end > source;
        }

        List<SeedRange> splitRanges(List<SeedRange> ranges) {
            List<SeedRange> newRanges = new ArrayList<>();
            for (SeedRange seedRange : ranges) {
                if (intersects(seedRange)) {
                    if (seedRange.start < source) {
                        SeedRange before = new SeedRange(seedRange.start, source);
                        newRanges.add(before);
                    }
                    long start = Math.max(seedRange.start, source);
                    long end = Math.min(seedRange.end, source + length);
                    if (start != end) {
                        newRanges.add(new SeedRange(start, end));
                    }
                    if (this.source + length < seedRange.end) {
                        SeedRange after = new SeedRange(source + length, seedRange.end);
                        newRanges.add(after);
                    }
                } else {
                    newRanges.add(seedRange);
                }
            }
            return newRanges;
        }
    }

    record Input(long[] seeds, List<Mappings> maps) {
        static Input parse(String input) {
            List<String> lines = input.lines().toList();
            long[] seeds = Util.extractLongs(lines.getFirst());
            List<Mappings> maps = new ArrayList<>();
            for (int i = 2; i < lines.size(); i++) {
                String line = lines.get(i);
                if (!line.isEmpty()) {
                    if (line.endsWith("map:")) {
                        String source = line.substring(0, line.indexOf('-'));
                        String destination = line.substring(line.lastIndexOf('-') + 1, line.indexOf(' '));
                        maps.add(new Mappings(source, destination, new ArrayList<>()));
                    } else {
                        long[] longs = Util.extractLongs(line);
                        maps.getLast().mappings.add(new Mapping(longs[0], longs[1], longs[2]));
                    }
                }
            }
            return new Input(seeds, maps);
        }
    }

    private static final String TEST_INPUT = "seeds: 79 14 55 13\n"
                                             + "\n"
                                             + "seed-to-soil map:\n"
                                             + "50 98 2\n"
                                             + "52 50 48\n"
                                             + "\n"
                                             + "soil-to-fertilizer map:\n"
                                             + "0 15 37\n"
                                             + "37 52 2\n"
                                             + "39 0 15\n"
                                             + "\n"
                                             + "fertilizer-to-water map:\n"
                                             + "49 53 8\n"
                                             + "0 11 42\n"
                                             + "42 0 7\n"
                                             + "57 7 4\n"
                                             + "\n"
                                             + "water-to-light map:\n"
                                             + "88 18 7\n"
                                             + "18 25 70\n"
                                             + "\n"
                                             + "light-to-temperature map:\n"
                                             + "45 77 23\n"
                                             + "81 45 19\n"
                                             + "68 64 13\n"
                                             + "\n"
                                             + "temperature-to-humidity map:\n"
                                             + "0 69 1\n"
                                             + "1 0 69\n"
                                             + "\n"
                                             + "humidity-to-location map:\n"
                                             + "60 56 37\n"
                                             + "56 93 4";

    private static final String INPUT =
            "seeds: 629551616 310303897 265998072 58091853 3217788227 563748665 2286940694 820803307 1966060902 108698829 190045874 3206262 4045963015 223661537 1544688274 293696584 1038807941 31756878 1224711373 133647424\n"
            + "\n"
            + "seed-to-soil map:\n"
            + "3809825462 2725979505 339457863\n"
            + "3359244708 2085610478 450580754\n"
            + "652041572 2536191232 189788273\n"
            + "841829845 3346349446 343599367\n"
            + "1408035723 73701258 732851393\n"
            + "2140887116 3689948813 88205018\n"
            + "0 3778153831 371129494\n"
            + "2953980724 0 73701258\n"
            + "3027681982 1754047752 331562726\n"
            + "2229092134 1029159162 724888590\n"
            + "1185429212 806552651 222606511\n"
            + "371129494 3065437368 280912078\n"
            + "\n"
            + "soil-to-fertilizer map:\n"
            + "201390752 0 263005475\n"
            + "772560454 263005475 186665885\n"
            + "3597849741 3228095269 216867970\n"
            + "959226339 951560560 85171934\n"
            + "2882237029 3813801625 34286208\n"
            + "0 586356609 16090261\n"
            + "1460387186 1189054013 136970257\n"
            + "2511361703 2581174071 147006778\n"
            + "201110502 1477157137 280250\n"
            + "3582774663 3444963239 15075078\n"
            + "2073881675 2245158204 30510333\n"
            + "3127914126 3163440286 64654983\n"
            + "1724767985 602446870 349113690\n"
            + "1597357443 1036732494 127410542\n"
            + "1044398273 1164143036 24910977\n"
            + "635875205 449671360 136685249\n"
            + "2916523237 2728180849 211390889\n"
            + "1069309250 1854080268 391077936\n"
            + "167223128 1820192894 33887374\n"
            + "4168481019 2454687794 126486277\n"
            + "3496254979 4048626015 86519684\n"
            + "2454687794 4238293387 56673909\n"
            + "2104392008 1648916365 171276529\n"
            + "3814717711 3460038317 353763308\n"
            + "464396227 1477437387 171478978\n"
            + "2658368481 2939571738 223868548\n"
            + "16090261 1326024270 151132867\n"
            + "3393107291 4135145699 103147688\n"
            + "3192569109 3848087833 200538182\n"
            + "\n"
            + "fertilizer-to-water map:\n"
            + "357701033 441924316 54941059\n"
            + "2047098412 1574732688 106451110\n"
            + "2414997091 2961420861 217583761\n"
            + "3647103220 3202843177 147888878\n"
            + "1781607871 3397471081 265490541\n"
            + "433955285 629676938 29320532\n"
            + "3280739425 2494455782 366363795\n"
            + "2818710889 1426835569 147897119\n"
            + "1120892574 3179004622 23838555\n"
            + "1539573533 3662961622 195295312\n"
            + "3794992098 1820059317 63264836\n"
            + "0 84223283 357701033\n"
            + "1144731129 1702496991 117562326\n"
            + "2153549522 2046878176 261447569\n"
            + "593734757 726830618 239035306\n"
            + "987137385 83279657 943626\n"
            + "2966608008 0 83279657\n"
            + "1734868845 3350732055 46739026\n"
            + "1438972249 2860819577 100601284\n"
            + "2632580852 2308325745 186130037\n"
            + "1262293455 965865924 108845646\n"
            + "412642092 1681183798 21313193\n"
            + "472462518 1305563330 121272239\n"
            + "988081011 496865375 132811563\n"
            + "463275817 2037691475 9186701\n"
            + "3049887665 1074711570 230851760\n"
            + "832770063 1883324153 154367322\n"
            + "1371139101 658997470 67833148\n"
            + "\n"
            + "water-to-light map:\n"
            + "4062286509 3839153068 91029970\n"
            + "1610728246 3827168971 11474903\n"
            + "2753947407 2725849236 1101319735\n"
            + "2525484879 1829977386 228462528\n"
            + "657837215 1095779595 241604827\n"
            + "1895347620 1337384422 492592964\n"
            + "1425623249 4009599599 185104997\n"
            + "2446068318 3930183038 79416561\n"
            + "1894838426 3838643874 509194\n"
            + "2389619503 896001044 56448815\n"
            + "3855267142 2058439914 207019367\n"
            + "1187459420 657837215 238163829\n"
            + "1622203149 2467395620 172372577\n"
            + "2387940584 952449859 1678919\n"
            + "985523081 2265459281 201936339\n"
            + "4153316479 954128778 141650817\n"
            + "1794575726 4194704596 100262700\n"
            + "899442042 2688664470 37184766\n"
            + "936626808 2639768197 48896273\n"
            + "\n"
            + "light-to-temperature map:\n"
            + "0 2682471120 43545350\n"
            + "2829609407 2423668531 227914183\n"
            + "3685065657 3821208881 65673550\n"
            + "1319277847 0 33132672\n"
            + "818263707 3091863377 5216721\n"
            + "3144636417 670795080 1340457\n"
            + "1352410519 895535914 570572224\n"
            + "2709351136 1662268878 120258271\n"
            + "115643652 2726016470 93054822\n"
            + "455333494 1538206440 124062438\n"
            + "3839611769 4030334543 30664857\n"
            + "3750739207 4258515305 36451991\n"
            + "2070721515 33132672 155555065\n"
            + "3132740473 2067641423 5192544\n"
            + "4147162986 3685065657 58311172\n"
            + "4278703737 3743376829 16263559\n"
            + "1070098598 2174489282 249179249\n"
            + "716608392 2072833967 101655315\n"
            + "43545350 1466108138 72098302\n"
            + "3057523590 3016646494 75216883\n"
            + "2700979566 887164344 8371570\n"
            + "4205474158 4060999400 73229579\n"
            + "2226276580 188687737 474702986\n"
            + "1971879519 1968799427 98841996\n"
            + "3931845119 4134228979 124286326\n"
            + "579395932 2819071292 137212460\n"
            + "3137933017 672135537 6703400\n"
            + "1062694241 663390723 7404357\n"
            + "3787191198 3886882431 52420571\n"
            + "269061216 1782527149 186272278\n"
            + "1031805835 2651582714 30888406\n"
            + "823480428 678838937 208325407\n"
            + "3870276626 3759640388 61568493\n"
            + "4056131445 3939303002 91031541\n"
            + "1922982743 3097080098 48896776\n"
            + "208698474 2956283752 60362742\n"
            + "\n"
            + "temperature-to-humidity map:\n"
            + "219529182 731674447 232727899\n"
            + "2748076784 2771987989 46463882\n"
            + "2514344851 4061235363 233731933\n"
            + "0 1369964423 219529182\n"
            + "452257081 362359049 21789881\n"
            + "4243457964 2720478657 51509332\n"
            + "3085663754 3109574959 64704581\n"
            + "1639319644 384148930 347525517\n"
            + "3150368335 3626166922 251414834\n"
            + "1986845161 0 139120377\n"
            + "1382707786 1339581093 30383330\n"
            + "1413091116 1113352565 226228528\n"
            + "2794540666 2818451871 291123088\n"
            + "2125965538 338187591 24171458\n"
            + "474046962 139120377 157229612\n"
            + "2361125570 1100881680 12470885\n"
            + "631276574 296349989 41837602\n"
            + "3401783169 3428035243 198131679\n"
            + "3989702261 3174279540 253755703\n"
            + "2224646236 964402346 136479334\n"
            + "2150136996 2299087215 74509240\n"
            + "3806048654 3877581756 183653607\n"
            + "3599914848 2514344851 206133806\n"
            + "673114176 1589493605 709593610\n"
            + "\n"
            + "humidity-to-location map:\n"
            + "4029426902 1202474782 191291587\n"
            + "2764446301 708692227 493782555\n"
            + "2188304413 3350514524 33021460\n"
            + "3318755823 4213528230 67155117\n"
            + "2000392671 620732246 87959981\n"
            + "3754724301 3075811923 274702601\n"
            + "3258228856 1393766369 60526967\n"
            + "2088352652 4113576469 99951761\n"
            + "363515622 1849258760 614077493\n"
            + "1213242541 342257124 11129119\n"
            + "1733046668 353386243 267346003\n"
            + "1224371660 4280683347 14283949\n"
            + "2577070088 2888435710 187376213\n"
            + "4220718489 2832149614 56286096\n"
            + "324294413 1810037551 39221209\n"
            + "3385910940 2463336253 368813361\n"
            + "977593115 3383535984 235649426\n"
            + "1238655609 3619185410 494391059\n"
            + "4277004585 324294413 17962711\n"
            + "2221325873 1454293336 355744215";

    public static void main(String[] args) {
        Util.time(() -> a(Input.parse(TEST_INPUT)));
        Util.time(() -> a(Input.parse(INPUT)));
        Util.time(() -> b(Input.parse(TEST_INPUT)));
        Util.time(() -> b(Input.parse(INPUT)));
    }
}
