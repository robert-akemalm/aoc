package aoc2022;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Stream;

public class Day16 {
    public static void main(String[] args) {
        a(TEST_INPUT);
        a(INPUT);
        long l = System.currentTimeMillis();
        b(TEST_INPUT);
        System.out.println("Time: " + (System.currentTimeMillis() - l));
        l = System.currentTimeMillis();
        b(INPUT);
        System.out.println("Time: " + (System.currentTimeMillis() - l));
    }

    private static void a(String input) {
        Data data = Data.parse(input);
        int totalMinutes = 30;
        int maxFlow = (int) Stream.of(data.valves).mapToLong(Valve::flowRate).max().orElse(0);
        ExplorerRecorder recorder = new ExplorerRecorder(totalMinutes);
        recorder.add(new Explorer(data.get("AA"), new Opened(new BitSet()), 0, 0, 0));
        long max = 0;

        while (recorder.hasNext()) {
            Explorer explorer = recorder.get();
            if (explorer.minutes == totalMinutes) {
                max = Math.max(max, explorer.totalFlow);
                continue;
            }
            if (!explorer.canBecomeHigherThan(max, totalMinutes, maxFlow)) {
                continue;
            }

            if (!recorder.isBest(explorer)) {
                continue;

            }

            Explorer open = explorer.openValve();
            if (open.flowRate > explorer.flowRate) {
                recorder.add(open);
            }
            for (int neighbour : explorer.current.neighbours) {
                recorder.add(explorer.moveTo(data.valves[neighbour]));
            }
        }
        System.out.println(max);
    }

    private static class ExplorerRecorder {
        final Map<Integer, Map<Opened, Explorer>> keyToOpenedToBest = new HashMap<>();
        final PriorityQueue<Explorer> heap;

        final Map<Integer, Map<Opened, DoubleExplorer>> key2ToOpenedToBest = new HashMap<>();
        final PriorityQueue<DoubleExplorer> heap2;

        private ExplorerRecorder(int totalMinutes) {
            heap  = new PriorityQueue<>(
                    (o1, o2) -> Long.compare(o2.estimatedFlow(totalMinutes), o1.estimatedFlow(totalMinutes)));
            heap2 = new PriorityQueue<>(
                    (o1, o2) -> Long.compare(o2.estimatedFlow(totalMinutes), o1.estimatedFlow(totalMinutes)));
        }

        private static int explorerToKey(Explorer explorer) {
            return explorer.flowRate * 10_000 + explorer.current.id*100 + explorer.minutes;
        }

        private static int explorerToKey2(DoubleExplorer explorer) {
            int a = Math.min(explorer.current.id, explorer.elephant.id);
            int b = Math.max(explorer.current.id, explorer.elephant.id);
            return explorer.flowRate * 1_000_000 + a*10_000 + b*100 + explorer.minutes;
        }

        boolean isBest(Explorer explorer) {
            int key = explorerToKey(explorer);
            Map<Opened, Explorer> openedToBest = keyToOpenedToBest.computeIfAbsent(key, k -> new HashMap<>());
            Explorer best = openedToBest.get(explorer.opened);
            if (best == null || best.totalFlow < explorer.totalFlow) {
                openedToBest.put(explorer.opened, explorer);
                return true;
            }
            return false;
        }

        boolean isBest(DoubleExplorer explorer) {
            int key = explorerToKey2(explorer);
            Map<Opened, DoubleExplorer> openedToBest = key2ToOpenedToBest.computeIfAbsent(key, k -> new HashMap<>());
            DoubleExplorer best = openedToBest.get(explorer.opened);
            if (best == null || best.totalFlow < explorer.totalFlow) {
                openedToBest.put(explorer.opened, explorer);
                return true;
            }
            return false;
        }

        void add(Explorer explorer) {
            heap.add(explorer);
        }

        void add(DoubleExplorer explorer) {
            heap2.add(explorer);
        }

        Explorer get() {
            return heap.poll();
        }

        DoubleExplorer get2() {
            return heap2.poll();
        }

        public boolean hasNext() {
            return !heap.isEmpty() || !heap2.isEmpty();
        }
    }


    private static void b(String input) {
        Data data = Data.parse(input);
        int totalMinutes = 26;
        int numPositiveValves = (int) Arrays.stream(data.valves).filter(v -> v.flowRate > 0).count();
        int maxFlow = (int) Stream.of(data.valves).mapToLong(Valve::flowRate).max().orElse(0);
        ExplorerRecorder recorder = new ExplorerRecorder(totalMinutes);
        recorder.add(new DoubleExplorer(data.get("AA"), data.get("AA"), new Opened(new BitSet()), 0, 0, 0));
        int max = 0;

        while (recorder.hasNext()) {
            DoubleExplorer explorer = recorder.get2();
            if (explorer.minutes == totalMinutes) {
                max = Math.max(max, explorer.totalFlow);
                continue;
            }
            if (explorer.opened.count() == numPositiveValves) {
                int sum = explorer.totalFlow + explorer.flowRate*(totalMinutes-explorer.minutes);
                max = Math.max(max, sum);
                continue;
            }
            if (!explorer.canBecomeHigherThan(max, totalMinutes, maxFlow)) {
                continue;
            }

            if (!recorder.isBest(explorer)) {
                continue;
            }
            DoubleExplorer bothOpen = explorer.bothOpen();
            if (bothOpen.flowRate > explorer.flowRate) {
                recorder.add(bothOpen);
            }
            if (!explorer.opened.isOpen(explorer.elephant.id)) {
                for (int neighbour : explorer.current.neighbours) {
                    recorder.add(explorer.moveCurrentElephantOpen(data.valves[neighbour]));
                }
            }

            if (!explorer.opened.isOpen(explorer.current.id)) {
                for (int neighbour : explorer.elephant.neighbours) {
                    recorder.add(explorer.moveElephantCurrentOpen(data.valves[neighbour]));
                }
            }

            for (int neighbour : explorer.current.neighbours) {
                Valve valve = data.valves[neighbour];
                for (int elephantNeighbour : explorer.elephant.neighbours) {
                    recorder.add(explorer.moveBoth(valve, data.valves[elephantNeighbour]));
                }
            }
        }
        System.out.println(max);
    }

    private static class DoubleExplorer {
        final Valve current;
        final Valve elephant;
        final Opened opened;
        final int totalFlow;
        final int flowRate;
        final int minutes;

        public DoubleExplorer(Valve current, Valve elephant, Opened opened, int totalFlow, int flowRate, int minutes) {
            this.current = current;
            this.elephant = elephant;
            this.opened = opened;
            this.totalFlow = totalFlow;
            this.flowRate = flowRate;
            this.minutes = minutes;
        }

        DoubleExplorer moveBoth(Valve valve, Valve elephant) {
            return new DoubleExplorer(valve, elephant, opened, totalFlow + flowRate, flowRate, minutes + 1);
        }

        DoubleExplorer moveElephantCurrentOpen(Valve valve) {
            if (opened.isOpen(current.id)) {
                return this;
            }
            return new DoubleExplorer(current, valve, opened.add(current.id), totalFlow + flowRate, flowRate + current.flowRate, minutes + 1);
        }

        DoubleExplorer moveCurrentElephantOpen(Valve valve) {
            if (opened.isOpen(elephant.id)) {
                return this;
            }
            return new DoubleExplorer(valve, elephant, opened.add(elephant.id), totalFlow + flowRate, flowRate + elephant.flowRate, minutes + 1);
        }

        DoubleExplorer bothOpen() {
            if (current.id == elephant.id ||opened.isOpen(current.id) || opened.isOpen(elephant.id)) {
                return this;
            }
            Opened opened = this.opened.add(current.id).add(elephant.id);
            return new DoubleExplorer(current, elephant, opened, totalFlow + flowRate,
                    flowRate + current.flowRate + elephant.flowRate, minutes + 1);

        }

        int estimatedFlow(int timeLimit) {
            return totalFlow + flowRate * (timeLimit - minutes);
        }

        public boolean canBecomeHigherThan(long max, int maxMinutes, int maxAdditionalFlow) {
            int potential = totalFlow;
            int flow = flowRate;
            boolean addFlow = true;
            for (int i = minutes; i <= maxMinutes; i++) {
                potential += flow;
                if (addFlow) {
                    flow += maxAdditionalFlow;
                }
                addFlow = !addFlow;
            }
            return potential > max;
        }
    }

    private static class Explorer {
        final Valve current;
        final Opened opened;
        final int totalFlow;
        final int flowRate;
        final int minutes;

        public Explorer(Valve current, Opened opened, int totalFlow, int flowRate, int minutes) {
            this.current = current;
            this.opened = opened;
            this.totalFlow = totalFlow;
            this.flowRate = flowRate;
            this.minutes = minutes;
        }

        Explorer moveTo(Valve valve) {
            return new Explorer(valve, opened, totalFlow + flowRate, flowRate, minutes + 1);
        }

        Explorer openValve() {
            if (opened.isOpen(current.id)) {
                return this;
            }
            return new Explorer(current, opened.add(current.id), totalFlow + flowRate, flowRate + current.flowRate,
                    minutes + 1);
        }

        int estimatedFlow(int timeLimit) {
            return totalFlow + flowRate * (timeLimit - minutes);
        }

        public boolean canBecomeHigherThan(long max, int maxMinutes, int maxAdditionalFlow) {
            int potential = totalFlow;
            int flow = flowRate;
            boolean addFlow = true;
            for (int i = minutes; i <= maxMinutes; i++) {
                potential += flow;
                if (addFlow) {
                    flow += maxAdditionalFlow;
                }
                addFlow = !addFlow;
            }
            return potential > max;
        }
    }

    private record Data(Valve[] valves, Map<String, Integer> nameToValveIx) {
        static Data parse(String input) {
            String[] lines = input.split("\n");
            Valve[] valves = new Valve[lines.length];
            Map<String, Integer> nameToValveIx = new HashMap<>();

            for (int i = 0; i < lines.length; i++) {
                String name = lines[i].substring(6, 8);
                nameToValveIx.put(name, i);
            }

            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                String name = line.substring(6, 8);
                String[] parts = line.split(";");
                int flowRate = Integer.parseInt(parts[0].substring(parts[0].indexOf('=') + 1));
                String neighboursPart = parts[1].substring(parts[1].indexOf("valve") + (parts[1].contains("valves") ? 7 : 6));
                int[] neighbours = Stream.of(neighboursPart.split(", ")).mapToInt(nameToValveIx::get).toArray();
                valves[i] = new Valve(i, name, flowRate, neighbours);
            }
            return new Data(valves, nameToValveIx);
        }

        Valve get(String name) {
            return valves[nameToValveIx().get(name)];
        }
    }

    private record Valve(int id, String name, int flowRate, int[] neighbours) {
    }

    private record Opened(BitSet openValves) {
        Opened add(int valve) {
            BitSet b = new BitSet();
            b.or(openValves);
            b.set(valve);
            return new Opened(b);
        }

        public boolean isOpen(int id) {
            return openValves.get(id);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Opened opened = (Opened) o;
            return openValves.equals(opened.openValves);
        }

        @Override
        public int hashCode() {
            return openValves.hashCode();
        }

        public int count() {
            return openValves.cardinality();
        }
    }

    private static final String TEST_INPUT = "Valve AA has flow rate=0; tunnels lead to valves DD, II, BB\n"
                                             + "Valve BB has flow rate=13; tunnels lead to valves CC, AA\n"
                                             + "Valve CC has flow rate=2; tunnels lead to valves DD, BB\n"
                                             + "Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE\n"
                                             + "Valve EE has flow rate=3; tunnels lead to valves FF, DD\n"
                                             + "Valve FF has flow rate=0; tunnels lead to valves EE, GG\n"
                                             + "Valve GG has flow rate=0; tunnels lead to valves FF, HH\n"
                                             + "Valve HH has flow rate=22; tunnel leads to valve GG\n"
                                             + "Valve II has flow rate=0; tunnels lead to valves AA, JJ\n"
                                             + "Valve JJ has flow rate=21; tunnel leads to valve II";

    private static final String INPUT = "Valve VR has flow rate=11; tunnels lead to valves LH, KV, BP\n"
                                        + "Valve UV has flow rate=0; tunnels lead to valves GH, RO\n"
                                        + "Valve OH has flow rate=0; tunnels lead to valves AJ, NY\n"
                                        + "Valve GD has flow rate=0; tunnels lead to valves TX, PW\n"
                                        + "Valve NS has flow rate=0; tunnels lead to valves AJ, AA\n"
                                        + "Valve KZ has flow rate=18; tunnels lead to valves KO, VK, PJ\n"
                                        + "Valve AH has flow rate=0; tunnels lead to valves ZP, DI\n"
                                        + "Valve SA has flow rate=0; tunnels lead to valves VG, JF\n"
                                        + "Valve VK has flow rate=0; tunnels lead to valves RO, KZ\n"
                                        + "Valve GB has flow rate=0; tunnels lead to valves XH, AA\n"
                                        + "Valve AJ has flow rate=6; tunnels lead to valves IC, OH, ZR, NS, EM\n"
                                        + "Valve PJ has flow rate=0; tunnels lead to valves KZ, SP\n"
                                        + "Valve KO has flow rate=0; tunnels lead to valves KZ, LE\n"
                                        + "Valve AA has flow rate=0; tunnels lead to valves TW, GB, TI, NS, UL\n"
                                        + "Valve TW has flow rate=0; tunnels lead to valves TU, AA\n"
                                        + "Valve VG has flow rate=25; tunnel leads to valve SA\n"
                                        + "Valve BP has flow rate=0; tunnels lead to valves RO, VR\n"
                                        + "Valve XH has flow rate=0; tunnels lead to valves GB, RI\n"
                                        + "Valve TX has flow rate=0; tunnels lead to valves RI, GD\n"
                                        + "Valve IR has flow rate=10; tunnels lead to valves TN, NY, JF\n"
                                        + "Valve TU has flow rate=0; tunnels lead to valves JD, TW\n"
                                        + "Valve KC has flow rate=0; tunnels lead to valves SP, RO\n"
                                        + "Valve LN has flow rate=0; tunnels lead to valves EM, RI\n"
                                        + "Valve HD has flow rate=0; tunnels lead to valves FE, SC\n"
                                        + "Valve KE has flow rate=0; tunnels lead to valves OM, RI\n"
                                        + "Valve VY has flow rate=0; tunnels lead to valves PW, BS\n"
                                        + "Valve LH has flow rate=0; tunnels lead to valves OM, VR\n"
                                        + "Valve EM has flow rate=0; tunnels lead to valves AJ, LN\n"
                                        + "Valve SO has flow rate=22; tunnels lead to valves ZP, FE\n"
                                        + "Valve EC has flow rate=0; tunnels lead to valves OM, UL\n"
                                        + "Valve KV has flow rate=0; tunnels lead to valves SP, VR\n"
                                        + "Valve FE has flow rate=0; tunnels lead to valves SO, HD\n"
                                        + "Valve TI has flow rate=0; tunnels lead to valves AA, PW\n"
                                        + "Valve SC has flow rate=14; tunnel leads to valve HD\n"
                                        + "Valve ZP has flow rate=0; tunnels lead to valves SO, AH\n"
                                        + "Valve RO has flow rate=19; tunnels lead to valves UV, BP, VK, KC\n"
                                        + "Valve ZR has flow rate=0; tunnels lead to valves OM, AJ\n"
                                        + "Valve JL has flow rate=21; tunnels lead to valves GN, TN\n"
                                        + "Valve PW has flow rate=9; tunnels lead to valves TI, GN, VY, GD, IC\n"
                                        + "Valve UL has flow rate=0; tunnels lead to valves EC, AA\n"
                                        + "Valve GN has flow rate=0; tunnels lead to valves JL, PW\n"
                                        + "Valve TN has flow rate=0; tunnels lead to valves JL, IR\n"
                                        + "Valve NV has flow rate=0; tunnels lead to valves RI, JD\n"
                                        + "Valve DI has flow rate=23; tunnels lead to valves LE, AH\n"
                                        + "Valve IC has flow rate=0; tunnels lead to valves PW, AJ\n"
                                        + "Valve JF has flow rate=0; tunnels lead to valves SA, IR\n"
                                        + "Valve LE has flow rate=0; tunnels lead to valves DI, KO\n"
                                        + "Valve BS has flow rate=0; tunnels lead to valves JD, VY\n"
                                        + "Valve JD has flow rate=15; tunnels lead to valves NV, TU, BS\n"
                                        + "Valve SP has flow rate=24; tunnels lead to valves KC, KV, PJ\n"
                                        + "Valve NY has flow rate=0; tunnels lead to valves IR, OH\n"
                                        + "Valve OM has flow rate=7; tunnels lead to valves EC, GH, KE, ZR, LH\n"
                                        + "Valve GH has flow rate=0; tunnels lead to valves OM, UV\n"
                                        + "Valve RI has flow rate=3; tunnels lead to valves NV, KE, LN, XH, TX";
}
