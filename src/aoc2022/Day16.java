package aoc2022;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day16 {
    public static void main(String[] args) {
        Util.time(() -> a(TEST_INPUT));
        Util.time(() -> a(INPUT));
        Util.time(() -> b(TEST_INPUT));
        Util.time(() -> b(INPUT));
    }

    private static void a(String input) {
        Data data = Data.parse(input).optimize();
        System.out.println(new Recorder(data, 30).findMax());
    }

    private static void b(String input) {
        Data data = Data.parse(input).optimize();
        System.out.println(new Recorder2(data, 26).findMax());
    }

    private static class Recorder {
        record State(Valve valve, Opened opened, int totalFlow, int flowRate, int minutes) {
        }

        final Map<Integer, Map<Opened, State>> keyToOpenedToBest = new HashMap<>();
        final Data data;
        final int totalMinutes;
        final int maxFlow;
        int max = 0;

        private Recorder(Data data, int totalMinutes) {
            this.data = data;
            this.totalMinutes = totalMinutes;
            this.maxFlow = Stream.of(data.valves).mapToInt(Valve::flowRate).sum();
        }

        int findMax() {
            expand(new State(data.valve("AA"), new Opened(new BitSet()), 0, 0, 0));
            return max;
        }

        private void expand(State state) {
            max = Math.max(max, state.totalFlow + state.flowRate * (totalMinutes - state.minutes));
            if (shouldEvaluate(state)) {
                int[] neighbours = state.valve.neighbours;
                for (int i = 0; i < neighbours.length; i++) {
                    Valve neighbour = data.valves[neighbours[i]];
                    if (!state.opened.isOpen(neighbour.id)) {
                        int distance = state.valve.distances[i];
                        Opened opened = state.opened.open(neighbour.id);
                        int totalFlow = state.totalFlow + distance * state.flowRate;
                        int flowRate = state.flowRate + neighbour.flowRate;
                        expand(new State(neighbour, opened, totalFlow, flowRate, state.minutes + distance));
                    }
                }
            }
        }

        private boolean shouldEvaluate(State state) {
            return state.minutes != totalMinutes &&
                   state.opened.cnt() != data.valves.length &&
                   canBecomeHigherThanMax(state) &&
                   isBest(state);
        }

        private boolean isBest(State explorer) {
            int key = explorerToKey(explorer);
            Map<Opened, State> openedToBest = keyToOpenedToBest.computeIfAbsent(key, k -> new HashMap<>());
            State best = openedToBest.get(explorer.opened);
            if (best == null || best.totalFlow < explorer.totalFlow) {
                openedToBest.put(explorer.opened, explorer);
                return true;
            }
            return false;
        }

        private static int explorerToKey(State state) {
            return state.flowRate * 10_000 + state.valve.id * 100 + state.minutes;
        }

        private boolean canBecomeHigherThanMax(State state) {
            return state.totalFlow + maxFlow * (totalMinutes - state.minutes) > max;
        }
    }

    private static class Recorder2 {
        record State(Valve v1, Valve v2, Opened opened, int totalFlow, int flowRate, int minutes1, int minutes2,
                     int additionalFlow) {
            int minutes() {
                return Math.min(minutes1, minutes2);
            }
        }

        final Map<Long, Map<Opened, State>> keyToOpenedToBest = new HashMap<>();
        final Data data;
        final int totalMinutes;
        final int maxFlow;
        int max = 0;

        private Recorder2(Data data, int totalMinutes) {
            this.data = data;
            this.totalMinutes = totalMinutes;
            this.maxFlow = Stream.of(data.valves).mapToInt(Valve::flowRate).sum();
        }

        int findMax() {
            Valve start = data.valve("AA");
            expand(new State(start, start, new Opened(new BitSet()), 0, 0, 0, 0, 0));
            return max;
        }

        private void expand(State s) {
            int endFlow = s.totalFlow
                          + s.flowRate * Math.abs(s.minutes1 - s.minutes2)
                          + (s.flowRate + s.additionalFlow) * (totalMinutes - Math.max(s.minutes1, s.minutes2));
            max = Math.max(max, endFlow);
            if (shouldEvaluate(s)) {
                if (s.minutes1 < s.minutes2) {
                    int[] neighbours = s.v1.neighbours;
                    for (int i = 0; i < neighbours.length; i++) {
                        Valve n = data.valves[neighbours[i]];
                        if (!s.opened.isOpen(n.id)) {
                            Opened opened = s.opened.open(n.id);
                            int minutes1 = Math.min(s.minutes1 + s.v1.distances[i], totalMinutes);
                            int distance = Math.min(minutes1 - s.minutes1, s.minutes2 - s.minutes1);
                            int totalFlow = s.totalFlow + distance * s.flowRate;
                            if (minutes1 < s.minutes2) {
                                int flowRate = s.flowRate + n.flowRate;
                                expand(new State(n, s.v2, opened, totalFlow, flowRate, minutes1, s.minutes2, s.additionalFlow));
                            } else if (minutes1 == s.minutes2) {
                                int flowRate = s.flowRate + n.flowRate + s.additionalFlow;
                                expand(new State(n, s.v2, opened, totalFlow, flowRate, minutes1, s.minutes2, 0));
                            } else {
                                int flowRate = s.flowRate + s.additionalFlow;
                                expand(new State(n, s.v2, opened, totalFlow, flowRate, minutes1, s.minutes2, n.flowRate));
                            }
                        }
                    }
                } else {
                    int[] neighbours = s.v2.neighbours;
                    for (int i = 0; i < neighbours.length; i++) {
                        Valve n = data.valves[neighbours[i]];
                        if (!s.opened.isOpen(n.id)) {
                            Opened opened = s.opened.open(n.id);
                            int minutes2 = Math.min(s.minutes2 + s.v2.distances[i], totalMinutes);
                            int distance = Math.min(minutes2 - s.minutes2, s.minutes1 - s.minutes2);
                            int totalFlow = s.totalFlow + distance * s.flowRate;
                            if (minutes2 < s.minutes1) {
                                int flowRate = s.flowRate + n.flowRate;
                                expand(new State(s.v1, n, opened, totalFlow, flowRate, s.minutes1, minutes2, s.additionalFlow));
                            } else if (minutes2 == s.minutes1) {
                                int flowRate = s.flowRate + n.flowRate + s.additionalFlow;
                                expand(new State(s.v1, n, opened, totalFlow, flowRate, s.minutes1, minutes2, 0));
                            } else {
                                int flowRate = s.flowRate + s.additionalFlow;
                                expand(new State(s.v1, n, opened, totalFlow, flowRate, s.minutes1, minutes2, n.flowRate));
                            }
                        }
                    }
                }
            }
        }

        private boolean shouldEvaluate(State state) {
            return state.minutes() != totalMinutes &&
                   state.opened.cnt() != data.valves.length &&
                   canBecomeHigherThanMax(state) &&
                   isBest(state);
        }

        private boolean isBest(State explorer) {
            long key = explorerToKey(explorer);
            Map<Opened, State> openedToBest = keyToOpenedToBest.computeIfAbsent(key, k -> new HashMap<>());
            State best = openedToBest.get(explorer.opened);
            if (best == null || best.totalFlow < explorer.totalFlow) {
                openedToBest.put(explorer.opened, explorer);
                return true;
            }
            return false;
        }

        private static long explorerToKey(State explorer) {
            if (explorer.v1.id < explorer.v2.id) {
                int a = explorer.v1.id;
                int b = explorer.v2.id;
                int c = explorer.minutes1;
                int d = explorer.minutes2;
                return explorer.flowRate * 100_000_000L + a * 1_000_000L + b * 10_000L + 100L * c + d;
            }
            int a = explorer.v2.id;
            int b = explorer.v1.id;
            int c = explorer.minutes2;
            int d = explorer.minutes1;
            return explorer.flowRate * 100_000_000L + a * 1_000_000L + b * 10_000L + 100L * c + d;
        }

        private boolean canBecomeHigherThanMax(State state) {
            return state.totalFlow + maxFlow * (totalMinutes - state.minutes()) > max;
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

        public Data optimize() {
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
                while (!toExpand.isEmpty() && distance < nameToValveIx.size()) {
                    for (Valve expand : toExpand) {
                        for (int neighbour : expand.neighbours) {
                            Valve n = valve(neighbour);
                            if (seen.add(n)) {
                                if (n.flowRate > 0) {
                                    neighbourToDistance.put(newNameToValveIx.get(n.name), distance + 1);
                                }
                                nextToExpand.add(n);
                            }
                        }
                    }
                    distance++;
                    Set<Valve> tmp = toExpand;
                    toExpand = nextToExpand;
                    nextToExpand = tmp;
                    nextToExpand.clear();
                }
                int[] neighbours = neighbourToDistance.keySet().stream().mapToInt(i -> i).toArray();
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

        Opened open(int valve) {
            BitSet b = new BitSet();
            b.or(openValves);
            b.set(valve);
            return new Opened(b, cnt + 1);
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
