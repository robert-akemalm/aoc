package aoc2019;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Day14 {
    public static void main(String[] args) {
        String input = INPUT; // 4_076_489 to low
        //a(input);
        b(input);
    }

    static List<Translation> translations = new ArrayList<>();
    static Map<String, Translation> materialToTranslations = new HashMap<>();
    private static void b(String input) {
        for(String line : input.split("\n")) {
            String[] parts = line.split(" => ");
            String[] materialsFrom = parts[0].split(", ");
            List<Tuple> from = new ArrayList<>();
            for (String f : materialsFrom) {
                String[] fromParts = f.split(" ");
                from.add(new Tuple(Integer.parseInt(fromParts[0]), fromParts[1]));
            }
            String[] toParts = parts[1].split(" ");
            Tuple to = new Tuple(Integer.parseInt(toParts[0]), toParts[1]);
            Translation translation = new Translation(from, to);
            translations.add(translation);
            materialToTranslations.put(to.material, translation);
        }

        double totalOre = 1E12;
        int low = 0;
        int mid = -1;
        int high = (int) totalOre;
        double oreNeeded;
        while (low <= high) {
            mid = (low + high) / 2;
            oreNeeded = oreNeeded(mid);
            if (oreNeeded < totalOre) {
                low = mid + 1;
            } else if (oreNeeded > totalOre) {
                high = mid - 1;
            } else {
                System.out.println(mid);
                return;
            }
        }
        System.out.println((mid - 1) + ": " + oreNeeded(mid-1));
        System.out.println((mid) + ": " + oreNeeded(mid));
        System.out.println((mid + 1) + ": " + oreNeeded(mid+1));
    }

    private static long oreNeeded(long amount) {
        Map<String, Long> extraMaterial = new HashMap<>();
        Map<String, Long> materialUsed = new HashMap<>();
        materialUsed.put("FUEL", amount);
        while (materialUsed.size() > 1 || !materialUsed.containsKey("ORE")) {
            for (Entry<String, Long> e : materialUsed.entrySet().toArray(new Entry[0])) {
                String material = e.getKey();
                if (!material.equals("ORE")) {
                    Translation translation = materialToTranslations.get(material);
                    boolean first = true;
                    for (Tuple from : translation.from) {
                        long req = e.getValue();
                        int create = (int) Math.ceil(req / (double) translation.to.amount);
                        if (first) {
                            first = false;
                            long extra = translation.to.amount * create - req;
                            extraMaterial.compute(material, (s, i) -> (i == null ? 0 : i) + extra);
                        }

                        long have = extraMaterial.getOrDefault(from.material, 0L);
                        extraMaterial.remove(from.material);
                        long used = create * from.amount - have;
                        if (used < 0) {
                            extraMaterial.put(from.material, -used);
                        } else if (used > 0){
                            materialUsed.compute(from.material, (s,i) -> (i == null ? 0 : i) + used);
                        }
                    }
                    materialUsed.remove(material);
                }
            }
        }
        return materialUsed.get("ORE");
    }

//    private static void a(String input) {
//        List<Translation> translations = new ArrayList<>();
//        Map<String, List<Translation>> materialToTranslations = new HashMap<>();
//        for(String line : input.split("\n")) {
//            String[] parts = line.split(" => ");
//            String[] materialsFrom = parts[0].split(", ");
//            List<Tuple> from = new ArrayList<>();
//            for (String f : materialsFrom) {
//                String[] fromParts = f.split(" ");
//                from.add(new Tuple(Integer.parseInt(fromParts[0]), fromParts[1]));
//            }
//            String[] toParts = parts[1].split(" ");
//            Tuple to = new Tuple(Integer.parseInt(toParts[0]), toParts[1]);
//            Translation translation = new Translation(from, to);
//            translations.add(translation);
//            materialToTranslations.computeIfAbsent(to.material, k -> new ArrayList<>()).add(translation);
//        }
//
//        Map<String, Long> costApprox = new HashMap<>();
//        costApprox.put("ORE", 1L);
//        while (costApprox.size() <= materialToTranslations.size()) {
//            for (Entry<String, List<Translation>> entry : materialToTranslations.entrySet()) {
//                if (!costApprox.containsKey(entry.getKey())) {
//                    translations:
//                    for (Translation t : entry.getValue()) {
//                        long cost = 0;
//                        for (Tuple from : t.from) {
//                            Long amount = costApprox.get(from.material);
//                            if (amount == null) {
//                                continue translations;
//                            } else {
//                                cost += amount * from.amount;
//                            }
//                        }
//                        costApprox.put(entry.getKey(), cost);
//                    }
//                }
//            }
//        }
//        int minCost = 339544;
//        PriorityQueue<Path> queue = new PriorityQueue<>(Comparator.comparingInt(o -> o.costApprox));
//        queue.add(new Path(Collections.singletonMap("FUEL", 1), Collections.emptyMap(), costApprox));
//        while (!queue.isEmpty()) {
//            Path path = queue.poll();
//            if (path.materialNeeded.getOrDefault("ORE", 0) > minCost) {
//                break;
//            }
//
//            if (path.materialNeeded.size() == 1 && path.materialNeeded.containsKey("ORE")) {
//                minCost = Math.min(minCost, path.materialNeeded.get("ORE"));
//                continue;
//            }
//            for (Entry<String, Integer> e : path.materialNeeded.entrySet()) {
//                if (e.getKey().equals("ORE")) {
//                    continue;
//                }
//                for (Translation t : materialToTranslations.get(e.getKey())) {
//                    Map<String, Integer> newMaterialNeeded = new HashMap<>(path.materialNeeded);
//                    newMaterialNeeded.remove(e.getKey());
//                    int needed = e.getValue();
//                    for (Tuple from : t.from) {
//                        double amount = newMaterialNeeded.getOrDefault(from.material, 0);
//                        newMaterialNeeded.put(from.material, amount + from.amount);
//                    }
//                    needed -= t.to.amount;
//                    Map<String, Integer> materialExtra = new HashMap<>(path.materialExtra);
//                    if (needed <= 0) {
//                        materialExtra.put(e.getKey(), materialExtra.getOrDefault(e.getKey(), 0) -needed);
//                    } else {
//                        newMaterialNeeded.put(e.getKey(), needed);
//                    }
//                    queue.add(new Path(newMaterialNeeded, materialExtra, costApprox));
//                }
//            }
//        }
//        System.out.println(minCost);
//
//    }

    static class Path {
        final Map<String, Integer> materialNeeded;
        final Map<String, Integer> materialExtra;
        final int minCost;
        final int costApprox;

        // 339544 to high
        // 367765
        public Path(Map<String, Integer> materialNeeded, Map<String, Integer> materialExtra, Map<String, Long> costApprox) {
            this.materialNeeded = materialNeeded;
            minCost = materialNeeded.values().stream().mapToInt(Integer::intValue).sum();
            this.materialExtra = materialExtra;
            for (Entry<String, Integer> entry : materialExtra.entrySet().toArray(new Entry[0])) {
                if (materialNeeded.containsKey(entry.getKey())) {
                    int amount = entry.getValue() - materialNeeded.getOrDefault(entry.getKey(), 0);
                    if (amount > 0) {
                        materialNeeded.remove(entry.getKey());
                        materialExtra.put(entry.getKey(), amount);
                    } else if (amount < 0) {
                        materialNeeded.put(entry.getKey(), -amount);
                        materialExtra.remove(entry.getKey());
                    } else {
                        materialNeeded.remove(entry.getKey());
                        materialExtra.remove(entry.getKey());
                    }
                }
            }
            this.costApprox = cost(costApprox);
        }

        private int cost(Map<String, Long> costApprox) {
            long cost = 0;
            for (Entry<String, Integer> e : materialNeeded.entrySet()) {
                cost += ((long)e.getValue()) * costApprox.get(e.getKey());
            }
            if (cost > Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            }
            return (int) cost;
        }
    }

    static class Translation {
        final List<Tuple> from;
        final Tuple to;

        Translation(List<Tuple> from, Tuple to) {
            this.from = from;
            this.to = to;
        }
    }

    static class Tuple {
        final long amount;
        final String material;

        Tuple(long amount, String material) {
            this.amount = amount;
            this.material = material;
        }
    }

    private static final String TEST_INPUT_2 = "171 ORE => 8 CNZTR\n"
                                               + "7 ZLQW, 3 BMBT, 9 XCVML, 26 XMNCP, 1 WPTQ, 2 MZWV, 1 RJRHP => 4 PLWSL\n"
                                               + "114 ORE => 4 BHXH\n"
                                               + "14 VRPVC => 6 BMBT\n"
                                               + "6 BHXH, 18 KTJDG, 12 WPTQ, 7 PLWSL, 31 FHTLT, 37 ZDVW => 1 FUEL\n"
                                               + "6 WPTQ, 2 BMBT, 8 ZLQW, 18 KTJDG, 1 XMNCP, 6 MZWV, 1 RJRHP => 6 FHTLT\n"
                                               + "15 XDBXC, 2 LTCX, 1 VRPVC => 6 ZLQW\n"
                                               + "13 WPTQ, 10 LTCX, 3 RJRHP, 14 XMNCP, 2 MZWV, 1 ZLQW => 1 ZDVW\n"
                                               + "5 BMBT => 4 WPTQ\n"
                                               + "189 ORE => 9 KTJDG\n"
                                               + "1 MZWV, 17 XDBXC, 3 XCVML => 2 XMNCP\n"
                                               + "12 VRPVC, 27 CNZTR => 2 XDBXC\n"
                                               + "15 KTJDG, 12 BHXH => 5 XCVML\n"
                                               + "3 BHXH, 2 VRPVC => 7 MZWV\n"
                                               + "121 ORE => 7 VRPVC\n"
                                               + "7 XCVML => 6 RJRHP\n"
                                               + "5 BHXH, 4 VRPVC => 5 LTCX";
    private static final String TEST_INPUT = "171 ORE => 8 CNZTR\n"
                                             + "7 ZLQW, 3 BMBT, 9 XCVML, 26 XMNCP, 1 WPTQ, 2 MZWV, 1 RJRHP => 4 PLWSL\n"
                                             + "114 ORE => 4 BHXH\n"
                                             + "14 VRPVC => 6 BMBT\n"
                                             + "6 BHXH, 18 KTJDG, 12 WPTQ, 7 PLWSL, 31 FHTLT, 37 ZDVW => 1 FUEL\n"
                                             + "6 WPTQ, 2 BMBT, 8 ZLQW, 18 KTJDG, 1 XMNCP, 6 MZWV, 1 RJRHP => 6 FHTLT\n"
                                             + "15 XDBXC, 2 LTCX, 1 VRPVC => 6 ZLQW\n"
                                             + "13 WPTQ, 10 LTCX, 3 RJRHP, 14 XMNCP, 2 MZWV, 1 ZLQW => 1 ZDVW\n"
                                             + "5 BMBT => 4 WPTQ\n"
                                             + "189 ORE => 9 KTJDG\n"
                                             + "1 MZWV, 17 XDBXC, 3 XCVML => 2 XMNCP\n"
                                             + "12 VRPVC, 27 CNZTR => 2 XDBXC\n"
                                             + "15 KTJDG, 12 BHXH => 5 XCVML\n"
                                             + "3 BHXH, 2 VRPVC => 7 MZWV\n"
                                             + "121 ORE => 7 VRPVC\n"
                                             + "7 XCVML => 6 RJRHP\n"
                                             + "5 BHXH, 4 VRPVC => 5 LTCX";

    public static final String INPUT = "12 JSMPL, 1 RFSHT => 8 NLTCF\n"
                                       + "6 LTSZQ, 22 KLSMX, 12 CWLGT => 2 MZXFC\n"
                                       + "4 WMVD, 3 PLBT, 1 ZKDMR => 5 CWLGT\n"
                                       + "5 SDTGC => 2 LSFKV\n"
                                       + "189 ORE => 3 TNTDN\n"
                                       + "20 CZKW => 4 BGNFD\n"
                                       + "5 XFMH => 7 SFRQ\n"
                                       + "7 NLTCF => 1 KLSMX\n"
                                       + "1 NLTCF => 4 HTDFH\n"
                                       + "2 RFPT, 5 JFXPH => 5 KRCQ\n"
                                       + "178 ORE => 7 XGLBX\n"
                                       + "1 NHQH => 3 NDMT\n"
                                       + "4 BNVTZ, 13 KXFJ, 14 QRBK, 56 SJSLP, 18 SPFP, 9 WMVD, 12 JFXPH, 1 MHXF => 1 FUEL\n"
                                       + "1 XQRX, 2 DPRVM, 1 HTDFH, 24 NLTCF, 8 SPBXP, 20 TSRNS, 2 VJDBK, 1 PXKL => 7 SPFP\n"
                                       + "6 WMVD => 3 SPBXP\n"
                                       + "1 XGLBX => 8 QXLMV\n"
                                       + "1 PLBT => 5 ZKDMR\n"
                                       + "25 VJDBK, 5 MZXFC, 3 BDGCJ => 9 BNVTZ\n"
                                       + "2 TNTDN, 1 SZNCS => 2 LMXBH\n"
                                       + "3 TNTDN => 6 RVRD\n"
                                       + "4 RFPT => 6 VHMQ\n"
                                       + "7 QXLMV, 1 LMXBH, 4 CSZP => 8 XFMH\n"
                                       + "5 SZNCS => 5 JSMPL\n"
                                       + "5 MHXF, 5 LTSZQ => 4 RFPT\n"
                                       + "5 XQMBJ, 1 BGNFD, 5 TQPGR => 3 NHQH\n"
                                       + "10 CHWS => 2 BDGCJ\n"
                                       + "19 DPRVM, 13 NHQH, 7 CZKW => 6 FWMXM\n"
                                       + "1 KLSMX, 1 PLBT, 5 XFMH => 3 SDTGC\n"
                                       + "20 LMXBH => 9 RFSHT\n"
                                       + "3 XGLBX => 1 TNPVZ\n"
                                       + "3 FBWF => 7 WMVD\n"
                                       + "1 QXLMV, 1 LMXBH => 3 ZMNV\n"
                                       + "5 JSMPL, 12 SFRQ => 8 CZKW\n"
                                       + "2 TNPVZ => 9 MHXF\n"
                                       + "2 MNVX, 1 RBMLP, 6 LSFKV => 9 VJDBK\n"
                                       + "26 SZNCS, 1 XGLBX => 6 CSZP\n"
                                       + "6 FBWF, 2 SPBXP, 4 BDGCJ => 2 TQPGR\n"
                                       + "5 LSFKV, 5 DPRVM => 9 QNFC\n"
                                       + "33 BDGCJ, 3 CWLGT => 4 XQRX\n"
                                       + "2 TQPGR, 22 LSFKV, 2 RFPT, 1 BDGCJ, 1 ZKDMR, 7 TSRNS, 6 DPRVM, 11 KRCQ => 2 QRBK\n"
                                       + "13 XQRX, 3 FWMXM, 2 CWLGT, 1 XQMBJ, 3 BGNFD, 6 HTDFH, 10 TSRNS => 5 KXFJ\n"
                                       + "1 ZKDMR => 9 CHWS\n"
                                       + "14 MNVX, 5 XFMH => 7 LTSZQ\n"
                                       + "2 NDMT, 2 QNFC, 11 ZMNV => 6 PXKL\n"
                                       + "7 SFRQ => 5 MNVX\n"
                                       + "2 WMPKD, 1 QXLMV => 9 SJSLP\n"
                                       + "14 JFXPH => 3 XQMBJ\n"
                                       + "14 SFRQ => 7 FBWF\n"
                                       + "1 WMPKD, 30 GBQGR, 4 SPBXP => 9 DPRVM\n"
                                       + "129 ORE => 4 SZNCS\n"
                                       + "5 JSMPL => 8 JFXPH\n"
                                       + "9 JFXPH, 2 VHMQ => 5 RBMLP\n"
                                       + "6 JSMPL => 7 GBQGR\n"
                                       + "25 SFRQ, 19 HRMT => 5 WMPKD\n"
                                       + "3 ZMNV => 9 PLBT\n"
                                       + "7 ZMNV, 9 RVRD, 8 SFRQ => 7 HRMT\n"
                                       + "8 RBMLP => 6 TSRNS";
}
