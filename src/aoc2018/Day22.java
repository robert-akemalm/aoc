package aoc2018;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

//1992 to high
//1985 to high
//1072 to high !1064 !1057 !1048 !1049
// 789 to low
public class Day22 {
    static final int[][] EROSION_LEVEL = new int[1500][];

    static {
        for (int y = 0; y < EROSION_LEVEL.length; y++) {
            EROSION_LEVEL[y] = new int[1500];
            Arrays.fill(EROSION_LEVEL[y], -1);
            for (int x = 0; x < EROSION_LEVEL.length; x++) {
                erosionLevel(x, y, 7863);
            }
        }
        EROSION_LEVEL[0][0] = 0;
        EROSION_LEVEL[760][14] = 0;
    }

    public static void main(String[] args) {
        int depth = 7863;
        int targetX = 14;
        int targetY = 760;
        a(depth, targetX, targetY);
        b(depth, targetX, targetY);
    }

    private static void a(int depth, int targetX, int targetY) {
        int sum = 0;
        for (int y = 0; y <= targetY; y++) {
            for (int x = 0; x <= targetX; x++) {
                if ((x == 0 && y == 0) || (x == targetX && y == targetY)) {
                } else {
                    sum += type(erosionLevel(x, y, depth)).ordinal();
                }
            }
        }
        System.out.println(sum);
    }

    private static void b(int depth, int targetX, int targetY) {
        State[][] bestTimes = new State[EROSION_LEVEL.length][];
        Type[][] types = new Type[EROSION_LEVEL.length][];
        for (int y = 0; y < EROSION_LEVEL.length; y++) {
            types[y] = new Type[EROSION_LEVEL.length];
            bestTimes[y] = new State[EROSION_LEVEL.length];
            for (int x = 0; x < EROSION_LEVEL.length; x++) {
                types[y][x] = type(EROSION_LEVEL[y][x]);
            }
        }
        types[0][0] = Type.ROCKY;
        types[targetY][targetX] = Type.ROCKY;
        PriorityQueue<State> pq = new PriorityQueue<>(1000, Comparator.comparingInt(o -> o.time));
        State currentState = new State(0, 0, Tool.TORCH, 0);
        bestTimes[0][0] = currentState;
        int bestTime = 1073;
        while (currentState != null && currentState.time < bestTime) {
            List<State> possibleNext = currentState.possibleNext(types, bestTimes);
            for (State state : possibleNext) {
                if (state.x == targetX && state.y == targetY) {
                    if (state.tool != Tool.TORCH) {
                        state.time += 7;
                    }
                    bestTime = Math.min(bestTime, state.time);
                }
                State best = bestTimes[state.y][state.x];
                if (best == null || state.time < best.time) {
                    bestTimes[state.y][state.x] = state;
                }
            }
            pq.addAll(possibleNext);
            currentState = pq.poll();
        }
        for (int y = 0; y <= targetY+10; y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x <= targetX + 100; x++) {
                State state = bestTimes[y][x];
                int spacesToAdd = 5;
                if (y == targetY && x == targetX) {
                    sb.append('*');
                    spacesToAdd--;
                }
                if (state != null) {
                    spacesToAdd--;
                    if (state.time > 9) {
                        spacesToAdd--;
                    }
                    if (state.time > 99) {
                        spacesToAdd--;
                    }
                    if (state.time > 999) {
                        spacesToAdd--;
                    }
                }
                for (int i = 0; i < spacesToAdd; i++) {
                    sb.append(' ');
                }
                if (state != null) {
                    sb.append(state.time);
                }
                sb.append(types[y][x].c).append(',');

            }
            System.out.println(sb.toString());

        }
        System.out.println(bestTime);
    }

    private static Type type(int erosionLevel) {
        return Type.values()[erosionLevel % 3];
    }

    private static int erosionLevel(int x, int y, int depth) {
        int el = EROSION_LEVEL[y][x];
        if (el != -1) {
            return el;
        }

        int geoIndex;
        if (y == 0) {
            geoIndex = x * 16807;
        } else if (x == 0) {
            geoIndex = y * 48271;
        } else {
            geoIndex = erosionLevel(x - 1, y, depth) * erosionLevel(x, y - 1, depth);
        }
        EROSION_LEVEL[y][x] = (geoIndex + depth) % 20183;
        return EROSION_LEVEL[y][x];
    }

    private static class State {
        final int x;
        final int y;
        final Tool tool;
        int time;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            State state = (State) o;
            return x == state.x &&
                   y == state.y &&
                   tool == state.tool;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, tool);
        }

        private State(int x, int y, Tool tool, int time) {
            this.x = x;
            this.y = y;
            this.tool = tool;
            this.time = time;
        }

        static int[] toCheck = new int[] {-1, 0, 1, 0, 0, 1, 0, -1};

        public static Map<State, Integer> seen = new HashMap<>();
        public List<State> possibleNext(Type[][] types, State[][] bestTimes) {
            List<State> possibleNexts = new ArrayList<>();
            Type thisType = types[y][x];
            for (int i = 0; i < toCheck.length; i += 2) {
                int nextY = y + toCheck[i];
                int nextX = x + toCheck[i + 1];
                if (nextX >= 0 && nextX < types.length && nextY >= 0 && nextY < types.length) {
                    Type nextType = types[nextY][nextX];
                    if (nextType.canHave(tool)) {
                        int nextTime = this.time + 1;
                        State possible = new State(nextX, nextY, tool, nextTime);
                        int saved = seen.getOrDefault(possible, Integer.MAX_VALUE);
                        if (nextTime < saved) {
                            seen.put(possible, nextTime);
                            possibleNexts.add(possible);
                        }
                    } else {
                        int nextTime = time + 8;
                        for (Tool t : nextType.tools) {
                            if (thisType.canHave(t)) {
                                State possible = new State(nextX, nextY, t, nextTime);
                                int saved = seen.getOrDefault(possible, Integer.MAX_VALUE);
                                if (nextTime < saved) {
                                    seen.put(possible, nextTime);
                                    possibleNexts.add(possible);
                                }
                            }
                        }
                    }
                }
            }
            return possibleNexts;
        }
    }

    private enum Tool {CLIMBING_GEAR, TORCH, NEITHER}

    private enum Type {
        ROCKY('R',Tool.CLIMBING_GEAR, Tool.TORCH),
        WET('W', Tool.CLIMBING_GEAR, Tool.NEITHER),
        NARROW('N',Tool.TORCH, Tool.NEITHER);

        private final Tool[] tools;
        public final char c;

        Type(char c, Tool... tools) {
            this.c = c;
            this.tools = tools;
        }

        public boolean canHave(Tool tool) {
            for (Tool t : tools) {
                if (t == tool) {
                    return true;
                }
            }
            return false;
        }
    }

}
