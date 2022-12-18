package aoc2022;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.IntStream;
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
        ExplorerRecorder recorder = new ExplorerRecorder(totalMinutes, maxFlow);
        recorder.add(new Explorer(data.valve("AA"), new Opened(new BitSet()), 0, 0, 0));
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

        final Map<Long, Map<Opened, DoubleExplorer>> key2ToOpenedToBest = new HashMap<>();
        final PriorityQueue<DoubleExplorer> heap2;
        final int totalMinutes;
        private final int maxFlowRate;
        int max = 0;

        private ExplorerRecorder(int totalMinutes, int maxFlowRate) {
            this.maxFlowRate = maxFlowRate;
            this.totalMinutes = totalMinutes;
            heap = new PriorityQueue<>(
                    (o1, o2) -> Integer.compare(o2.estimatedFlow(totalMinutes), o1.estimatedFlow(totalMinutes)));
            heap2 = new PriorityQueue<>(
                (o1, o2) -> Integer.compare(o2.estimatedFlow(totalMinutes), o1.estimatedFlow(totalMinutes)));
        }

        private static int explorerToKey(Explorer explorer) {
            return explorer.flowRate * 10_000 + explorer.current.id * 100 + explorer.minutes;
        }

        private static long explorerToKey2(DoubleExplorer explorer) {
            if (explorer.current.id < explorer.elephant.id) {
                int a = explorer.current.id;
                int b = explorer.elephant.id;
                int c = explorer.currentMinutes;
                int d = explorer.elephantMinutes;
                return explorer.flowRate * 100_000_000L + a * 1_000_000L + b * 10_000L + 100L*c + d;
            }
            int a = explorer.elephant.id;
            int b = explorer.current.id;
            int c = explorer.elephantMinutes;
            int d = explorer.currentMinutes;
            return explorer.flowRate * 100_000_000L + a * 1_000_000L + b * 10_000L + 100L*c + d;
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
            long key = explorerToKey2(explorer);
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
            if (explorer.minutes() == totalMinutes) {
                max = Math.max(max, explorer.totalFlow);
                return;
            }
            if (!explorer.canBecomeHigherThan(max, totalMinutes, maxFlowRate)) {
                return;
            }
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
        Data data = Data.parse(input).removeZeroFlowValves();
        int totalMinutes = 26;
        int maxFlowRate = Arrays.stream(data.valves).mapToInt(v->v.flowRate).sum();
        ExplorerRecorder recorder = new ExplorerRecorder(totalMinutes, maxFlowRate);
        recorder.add(new DoubleExplorer(data.valve("AA"), data.valve("AA"), new Opened(new BitSet()), 0, 0, 0, 0));

        while (recorder.hasNext()) {
            DoubleExplorer explorer = recorder.get2();
            if (explorer.flowRate == maxFlowRate) {
                int sum = explorer.totalFlow + explorer.flowRate * (totalMinutes - explorer.minutes());
                recorder.max = Math.max(recorder.max, sum);
                continue;
            }

            if (!recorder.isBest(explorer)) {
                continue;
            }
            int size = recorder.heap2.size();
            if (explorer.elephantMinutes == explorer.currentMinutes) {
                recorder.add(explorer.bothOpen());

                for (int i = 0; i < explorer.current.neighbours.length; i++) {
                    int neighbour = explorer.current.neighbours[i];
                    int distance = explorer.current.distances[i];
                    Valve valve = data.valve(neighbour);
                    for (int j = 0; j < explorer.elephant.neighbours.length; j++) {
                        int elephantNeighbour = explorer.elephant.neighbours[j];
                        int elephantDistance = explorer.elephant.distances[j];
                        Valve elephantValve = data.valve(elephantNeighbour);
                        recorder.add(explorer.moveBoth(valve, distance, elephantValve, elephantDistance));
                    }
                }

                if (!explorer.opened.isOpen(explorer.elephant.id) && explorer.elephantMinutes == explorer.minutes()) {
                    for (int i = 0; i < explorer.current.neighbours.length; i++) {
                        int neighbour = explorer.current.neighbours[i];
                        int distance = explorer.current.distances[i];
                        recorder.add(explorer.moveCurrentElephantOpen(data.valves[neighbour], distance));
                    }
                }

                if (!explorer.opened.isOpen(explorer.current.id) && explorer.currentMinutes == explorer.minutes()) {
                    for (int i = 0; i < explorer.elephant.neighbours.length; i++) {
                        int neighbour = explorer.elephant.neighbours[i];
                        int distance = explorer.elephant.distances[i];
                        recorder.add(explorer.moveElephantCurrentOpen(data.valves[neighbour], distance));
                    }
                }
            } else if (explorer.currentMinutes < explorer.elephantMinutes) {
                if (!explorer.opened.isOpen(explorer.current.id)) {
                    recorder.add(explorer.openCurrent());
                }
                for (int i = 0; i < explorer.current.neighbours.length; i++) {
                    int neighbour = explorer.current.neighbours[i];
                    int distance = explorer.current.distances[i];
                    recorder.add(explorer.moveCurrent(data.valves[neighbour], distance));
                }
            } else {
                if (!explorer.opened.isOpen(explorer.elephant.id)) {
                    recorder.add(explorer.openElephant());
                }
                for (int i = 0; i < explorer.elephant.neighbours.length; i++) {
                    int neighbour = explorer.elephant.neighbours[i];
                    int distance = explorer.elephant.distances[i];
                    recorder.add(explorer.moveElephant(data.valves[neighbour], distance));
                }

            }
            if (recorder.heap2.size() == size && explorer.flowRate > 0) {
                recorder.add(explorer.keepStateMoveTime());
            }

        }
        System.out.println("->" + recorder.max);
    }

    private static class DoubleExplorer {
        final Valve current;
        final Valve elephant;
        final Opened opened;
        final int totalFlow;
        final int flowRate;
        final int currentMinutes;
        final int elephantMinutes;

        public DoubleExplorer(Valve current, Valve elephant, Opened opened, int totalFlow, int flowRate, int currentMinutes,
                              int elephantMinutes) {
            this.current = current;
            this.elephant = elephant;
            this.opened = opened;
            this.totalFlow = totalFlow;
            this.flowRate = flowRate;
            this.currentMinutes = currentMinutes;
            this.elephantMinutes = elephantMinutes;
        }

        DoubleExplorer moveBoth(Valve valve, int distance, Valve elephant, int elephantDistance) {
            distance = Math.min(distance, 26-currentMinutes);
            elephantDistance = Math.min(elephantDistance, 26- elephantMinutes);
            int steps = Math.min(distance, elephantDistance);

            return new DoubleExplorer(valve, elephant, opened, totalFlow + steps*flowRate, flowRate, currentMinutes+distance, elephantMinutes+elephantDistance);
        }

        DoubleExplorer moveElephant(Valve elephant, int elephantDistance) {
            elephantDistance = Math.min(elephantDistance, 26- elephantMinutes);
            int steps = Math.min(elephantMinutes-currentMinutes, elephantDistance);
            return new DoubleExplorer(current, elephant, opened, totalFlow + steps*flowRate, flowRate, currentMinutes, elephantMinutes+elephantDistance);
        }

        DoubleExplorer moveCurrent(Valve valve, int distance) {
            distance = Math.min(distance, 26-currentMinutes);
            int steps = Math.min(elephantMinutes-currentMinutes, distance);
            return new DoubleExplorer(valve, elephant, opened, totalFlow + steps*flowRate, flowRate, currentMinutes+distance, elephantMinutes);
        }

        public DoubleExplorer openCurrent() {
            return new DoubleExplorer(current, elephant, opened.add(current.id), totalFlow + flowRate,
                    flowRate + current.flowRate, currentMinutes + 1, elephantMinutes);
        }

        public DoubleExplorer openElephant() {
            return new DoubleExplorer(current, elephant, opened.add(elephant.id), totalFlow + flowRate,
                    flowRate + elephant.flowRate, currentMinutes, elephantMinutes+1);
        }

        DoubleExplorer moveElephantCurrentOpen(Valve valve, int distance) {
            if (opened.isOpen(current.id)) {
                return this;
            }

            if (currentMinutes == elephantMinutes) {
                return new DoubleExplorer(current, valve, opened.add(current.id), totalFlow + flowRate,
                        flowRate + current.flowRate, currentMinutes + 1, elephantMinutes + distance);
            } else {
                return new DoubleExplorer(current, elephant, opened.add(current.id), totalFlow + flowRate,
                        flowRate + current.flowRate, currentMinutes+1, elephantMinutes);

            }
        }

        DoubleExplorer moveCurrentElephantOpen(Valve valve, int distance) {
            if (opened.isOpen(elephant.id)) {
                return this;
            }
            if (currentMinutes == elephantMinutes) {
                return new DoubleExplorer(valve, elephant, opened.add(elephant.id), totalFlow + flowRate,
                        flowRate + elephant.flowRate, currentMinutes + distance, elephantMinutes + 1);
            } else {
                return new DoubleExplorer(current, elephant, opened.add(elephant.id), totalFlow + flowRate,
                        flowRate + elephant.flowRate, currentMinutes, elephantMinutes + 1);
            }
        }

        DoubleExplorer bothOpen() {
            if (current.id == elephant.id || opened.isOpen(current.id) || opened.isOpen(elephant.id)) {
                return this;
            }
            Opened opened = this.opened.add(current.id).add(elephant.id);
            return new DoubleExplorer(current, elephant, opened, totalFlow + flowRate,
                    flowRate + current.flowRate + elephant.flowRate, currentMinutes + 1, elephantMinutes+1);
        }

        DoubleExplorer keepStateMoveTime() {
            int cMinutes = currentMinutes == minutes() ? currentMinutes+1:currentMinutes;
            int eMinutes = elephantMinutes == minutes() ? elephantMinutes+1 : elephantMinutes;
            return new DoubleExplorer(current, elephant, opened, totalFlow + flowRate,
                    flowRate, cMinutes, eMinutes);
        }

        public boolean canBecomeHigherThan(long max, int maxMinutes, int maxFlowRate) {
            int potential = totalFlow + (maxMinutes - minutes()) * maxFlowRate;
            return potential >= max;
        }

        int minutes() {
            return Math.min(currentMinutes, elephantMinutes);
        }

        int estimatedFlow(int timeLimit) {
            return totalFlow + flowRate * (timeLimit - minutes());
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
                int[] distances = Arrays.stream(neighbours).map(n -> 1).toArray();
                valves[i] = new Valve(i, name, flowRate, neighbours, distances);
            }
            return new Data(valves, nameToValveIx);
        }

        Valve valve(String name) {
            return valves[nameToValveIx().get(name)];
        }

        Valve valve(int ix) {
            return valves[ix];
        }

        Data removeZeroFlowValves() {
            List<Valve> nonZeroValves = Stream.of(valves).filter(v -> v.flowRate() > 0).toList();
            Map<String, Integer> newNameToValveIx = new HashMap<>();
            for (int i = 0; i < nonZeroValves.size(); i++) {
                newNameToValveIx.put(nonZeroValves.get(i).name, i);
            }
            newNameToValveIx.put("AA", nonZeroValves.size());
            Valve[] newValves = new Valve[nonZeroValves.size() + 1];

            for (Entry<String, Integer> e : newNameToValveIx.entrySet()) {
                Valve old = valve(e.getKey());
                Map<Integer, Integer> neighbourToDistance = new HashMap<>();
                Set<Valve> toExpand = new HashSet<>();
                toExpand.add(old);
                int distance = 1;
                Set<Valve> nextToExpand = new HashSet<>();
                Set<Valve> seen = new HashSet<>();
                seen.add(old);
                while (!toExpand.isEmpty()) {
                    for (Valve expand : toExpand) {
                        for (int neighbour : expand.neighbours) {
                            Valve n = valve(neighbour);
                            if (seen.add(n)) {
                                if (n.flowRate > 0) {
                                    neighbourToDistance.put(newNameToValveIx.get(n.name), distance);
                                } else {
                                    nextToExpand.add(n);
                                }
                            }
                        }

                    }
                    distance++;
                    Set<Valve> tmp = toExpand;
                    toExpand = nextToExpand;
                    nextToExpand = tmp;
                    nextToExpand.clear();
                }
                int[] neighbours = neighbourToDistance.keySet().stream().mapToInt(i->i).toArray();
                int[] distances = IntStream.of(neighbours).map(neighbourToDistance::get).toArray();
                newValves[e.getValue()] = new Valve(e.getValue(), old.name, old.flowRate, neighbours, distances);
            }
            return new Data(newValves, newNameToValveIx);
        }
    }

    private record Valve(int id, String name, int flowRate, int[] neighbours, int[] distances) {
        @Override
        public boolean equals(Object o) {
            if (o instanceof Valve other) {
                return id == other.id;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return id;
        }
    }

    private record Opened(BitSet openValves, int cnt) {
        public Opened(BitSet openValves) {
            this(openValves, openValves.cardinality());
        }

        Opened add(int valve) {
            BitSet b = new BitSet();
            b.or(openValves);
            b.set(valve);
            return new Opened(b, cnt+1);
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
            return cnt;
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
