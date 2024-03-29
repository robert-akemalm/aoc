package aoc2019;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import aoc2019.Day5.IO;
import aoc2019.Day5.IntComputer;
import aoc2019.Util.Pos;

public class Day15a {
    public static void main(String[] args) {
        String input = INPUT;
        a(input);
    }

    private static void a(String input) {
        IO read = new IO();
        IO write = new IO();
        IntComputer intComputer = new IntComputer(read, write, input);
        Map<Pos, Boolean> visited = new HashMap<>();
        visited.put(new Pos(0, 0), true);
        Pos current = new Pos(0, 0);
        Pos oxygen = null;
        int minLength = Integer.MAX_VALUE;
        Stack<Pos> path = new Stack<>();
        while (true) {
            int distance = Math.abs(current.x) + Math.abs(current.y);
            if (distance < minLength) {
                Pos n = Movements.from(current, Movements.N);
                Pos s = Movements.from(current, Movements.S);
                Pos e = Movements.from(current, Movements.E);
                Pos w = Movements.from(current, Movements.W);
                if (!visited.containsKey(n)) {
                    path.push(current);
                    current = n;
                    read.write(Movements.N.ordinal());
                } else if (!visited.containsKey(s)) {
                    path.push(current);
                    current = s;
                    read.write(Movements.S.ordinal());
                } else if (!visited.containsKey(e)) {
                    path.push(current);
                    current = e;
                    read.write(Movements.E.ordinal());
                } else if (!visited.containsKey(w)) {
                    path.push(current);
                    current = w;
                    read.write(Movements.W.ordinal());
                }
            }
            if (read.values.isEmpty()) {
                if (path.empty()) {
                    break;
                }
                Pos prev = path.peek();
                if (prev.y < current.y) {
                    read.write(Movements.N.ordinal());
                } else if (prev.y > current.y) {
                    read.write(Movements.S.ordinal());
                } else if (prev.x < current.x) {
                    read.write(Movements.W.ordinal());
                } else if (prev.x > current.x) {
                    read.write(Movements.E.ordinal());
                }
                current = path.pop();
            }
            intComputer.run();
            Long status = write.read();
            if (status == null) {
                print(visited, current, oxygen);
                System.out.println();
            }
            if (status == 0) {
                visited.put(current, false);
                current = path.pop();
            } else if (status == 1) {
                visited.put(current, true);
            } else if (status == 2) {
                visited.put(current, true);
                oxygen = current;
                minLength = path.size();
            }
        }
        print(visited, current, oxygen);
        int vis = (int) visited.entrySet().stream().filter(e -> e.getValue()).count();
        while (true) {
            for (Entry<Pos, Boolean> entry : visited.entrySet().toArray(new Entry[0])) {
                if (entry.getValue()) {
                    Pos p = entry.getKey();
                    Pos n = Movements.from(p, Movements.N);
                    Pos s = Movements.from(p, Movements.S);
                    Pos e = Movements.from(p, Movements.E);
                    Pos w = Movements.from(p, Movements.W);
                    int count = 0;
                    if (visited.getOrDefault(n, false) || n.equals(new Pos(0, 0) )|| n.equals(oxygen)) {
                        count++;
                    }
                    if (visited.getOrDefault(s, false) || s.equals(new Pos(0, 0) )|| s.equals(oxygen)) {
                        count++;
                    }
                    if (visited.getOrDefault(e, false) || e.equals(new Pos(0, 0) )|| e.equals(oxygen)) {
                        count++;
                    }
                    if (visited.getOrDefault(w, false) || w.equals(new Pos(0, 0)) || w.equals(oxygen)) {
                        count++;
                    }
                    if (count <= 1) {
                        visited.put(p, false);
                    }
                }
            }
            int vi2 = (int) visited.entrySet().stream().filter(e -> e.getValue()).count();
            if (vis == vi2) {
                break;
            }
            vis = vi2;
        }
        print(visited, current, oxygen);
        System.out.println(minLength);
        int shortest = explore(path, current, minLength, oxygen, visited);
        System.out.println(shortest);
    }

    static int explore(Stack<Pos> path, Pos step, int minLength, Pos target,
                       Map<Pos, Boolean> map) {
        if (!map.getOrDefault(step, false)) {
            return minLength;
        }
        if (!path.empty() && path.peek().equals(step)) {
            return minLength;
        }
        int distance = path.size() + Math.abs(step.x - target.x) + Math.abs(step.y - target.y);
        if (distance >= minLength) {
            return minLength;
        }

        path.push(step);
        if (step.equals(target)) {
            System.out.println(path.size() + ": " + path);
            return path.size();
        }
        Pos n = Movements.from(step, Movements.N);
        minLength = Math.min(minLength, explore(path, n, minLength, target, map));
        Pos s = Movements.from(step, Movements.S);
        minLength = Math.min(minLength, explore(path, s, minLength, target, map));
        Pos e = Movements.from(step, Movements.E);
        minLength = Math.min(minLength, explore(path, e, minLength, target, map));
        Pos w = Movements.from(step, Movements.W);
        minLength = Math.min(minLength, explore(path, w, minLength, target, map));
        path.pop();
        return minLength;
    }

    static void print(Map<Pos, Boolean> visited, Pos current, Pos oxygen) {
        Map<Pos, Integer> map = new HashMap<>();
        for (Entry<Pos, Boolean> entry : visited.entrySet()) {
            map.put(entry.getKey(), entry.getValue() ? 1 : 0);
        }
        map.put(current, 3);
        map.put(new Pos(0, 0), 4);
        if (oxygen != null) {
            map.put(oxygen, 5);
        }
        Map<Integer, Character> symbols = new HashMap<>();
        symbols.put(0, '#');
        symbols.put(1, '.');
        symbols.put(3, 'c');
        symbols.put(4, '¤');
        symbols.put(5, 'O');
        System.out.println(Util.print(map, symbols));

    }

enum Movements {
    NONE, N, S, W, E;

    static Pos from(Pos pos, Movements m) {
        switch (m) {
        case N:
            return new Pos(pos.x, pos.y - 1);
        case S:
            return new Pos(pos.x, pos.y + 1);
        case W:
            return new Pos(pos.x - 1, pos.y);
        case E:
            return new Pos(pos.x + 1, pos.y);
        }
        return null;
    }

}

    private static final String INPUT =
            "3,1033,1008,1033,1,1032,1005,1032,31,1008,1033,2,1032,1005,1032,58,1008,1033,3,1032,1005,1032,81,1008,1033,4,1032,1005,1032,104,99,101,0,1034,1039,1002,1036,1,1041,1001,1035,-1,1040,1008,1038,0,1043,102,-1,1043,1032,1,1037,1032,1042,1105,1,124,1001,1034,0,1039,1001,1036,0,1041,1001,1035,1,1040,1008,1038,0,1043,1,1037,1038,1042,1105,1,124,1001,1034,-1,1039,1008,1036,0,1041,1001,1035,0,1040,101,0,1038,1043,102,1,1037,1042,1106,0,124,1001,1034,1,1039,1008,1036,0,1041,1001,1035,0,1040,101,0,1038,1043,1001,1037,0,1042,1006,1039,217,1006,1040,217,1008,1039,40,1032,1005,1032,217,1008,1040,40,1032,1005,1032,217,1008,1039,9,1032,1006,1032,165,1008,1040,9,1032,1006,1032,165,1101,2,0,1044,1106,0,224,2,1041,1043,1032,1006,1032,179,1102,1,1,1044,1105,1,224,1,1041,1043,1032,1006,1032,217,1,1042,1043,1032,1001,1032,-1,1032,1002,1032,39,1032,1,1032,1039,1032,101,-1,1032,1032,101,252,1032,211,1007,0,73,1044,1105,1,224,1101,0,0,1044,1106,0,224,1006,1044,247,101,0,1039,1034,102,1,1040,1035,102,1,1041,1036,1002,1043,1,1038,1001,1042,0,1037,4,1044,1106,0,0,57,60,59,78,42,22,33,69,5,77,92,10,55,22,99,62,27,32,75,13,82,48,40,83,95,38,62,65,70,77,79,61,2,47,27,84,46,48,16,15,87,87,23,95,97,16,93,79,27,7,98,97,76,44,6,75,85,51,8,91,94,99,35,28,84,67,83,82,1,80,40,99,81,92,41,97,87,28,81,52,93,37,27,85,76,4,18,80,96,61,83,16,90,86,77,8,76,55,51,61,72,90,90,5,96,75,13,56,40,82,11,97,21,55,95,17,93,97,16,91,30,77,28,32,77,96,49,13,97,30,14,26,93,61,18,32,85,95,81,65,98,49,65,84,46,19,81,45,76,22,88,79,63,84,60,24,37,4,34,80,98,61,95,46,88,99,76,3,92,75,12,95,9,98,94,57,41,77,52,17,80,83,17,83,59,87,85,2,95,88,41,32,98,72,95,23,91,83,65,82,47,90,17,81,67,81,6,90,61,44,85,40,85,19,82,26,86,1,74,62,6,75,98,93,12,84,11,38,57,88,91,76,66,43,46,84,38,48,99,57,20,97,34,72,75,47,94,33,83,82,55,76,34,90,9,74,34,87,18,84,57,38,76,3,31,87,77,2,86,15,23,92,37,20,75,94,27,88,90,87,28,84,31,92,15,75,14,10,82,48,99,49,79,4,96,91,30,16,78,7,75,67,98,1,75,49,70,87,95,89,30,54,31,92,71,57,39,79,19,55,85,25,92,4,23,77,74,77,89,40,39,77,87,39,44,81,98,80,94,29,40,88,69,74,92,54,2,1,83,30,87,85,15,80,97,94,15,93,89,71,69,86,81,75,56,92,58,95,51,93,29,82,79,24,82,9,46,5,88,53,75,90,31,6,91,69,88,8,10,23,67,5,85,78,71,36,98,19,98,53,66,43,17,91,62,28,86,44,79,33,91,79,66,71,86,98,86,60,83,44,94,96,29,85,89,50,85,98,14,8,43,5,75,62,89,90,33,30,86,48,75,99,88,82,45,99,38,60,76,69,45,89,27,93,71,84,96,15,52,85,58,97,99,20,5,79,75,33,80,84,63,87,1,77,31,44,49,99,79,3,83,16,91,42,7,99,66,82,93,76,31,55,77,30,45,88,3,81,53,30,95,24,90,96,26,33,93,32,40,25,76,21,75,17,89,31,81,18,29,97,72,60,90,14,99,93,9,55,92,26,86,28,99,93,1,98,86,60,26,68,95,7,17,86,94,48,68,95,94,88,86,67,82,77,55,76,68,79,76,65,76,21,23,78,28,82,75,23,84,74,67,69,83,70,93,38,75,81,61,87,23,86,92,43,13,23,81,65,65,76,41,34,95,64,89,90,72,79,16,47,84,47,28,83,74,1,41,59,87,84,34,32,94,4,78,37,17,99,86,91,58,97,69,26,93,41,81,36,78,80,18,79,96,51,37,74,45,94,54,98,90,74,27,8,89,79,81,71,60,88,40,86,33,95,98,21,13,9,20,94,48,82,42,69,15,84,45,89,42,98,99,72,72,84,86,54,80,74,68,74,5,85,80,75,34,29,98,16,85,16,86,13,25,74,95,51,86,90,28,81,90,20,70,7,89,37,74,28,86,29,81,95,66,1,64,83,85,27,74,4,69,54,79,66,50,96,43,94,95,45,52,83,17,37,88,85,55,35,66,78,66,86,4,92,2,99,35,89,13,76,71,81,92,96,18,85,68,95,61,97,76,82,66,85,99,82,6,93,31,81,76,80,27,95,38,94,85,98,41,91,0,0,21,21,1,10,1,0,0,0,0,0,0";
}
