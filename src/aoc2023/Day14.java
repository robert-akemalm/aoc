package aoc2023;

import static aoc2023.Day14.Platform.Direction.E;
import static aoc2023.Day14.Platform.Direction.N;
import static aoc2023.Day14.Platform.Direction.S;
import static aoc2023.Day14.Platform.Direction.W;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aoc2023.Util.Pos;

public class Day14 {
    private static void a(Input input) {
        System.out.println(input.platform.tilt(N).score());
    }

    private static void b(Input input) {
        Map<String, Integer> seen = new HashMap<>();
        Platform platform = input.platform;
        for (int c = 1_000_000_000; c > 0; c--) {
            platform = platform.cycle();
            Integer old = seen.putIfAbsent(platform.serialize(), c);
            if (old != null && c % (old - c) == 1) {
                break;
            }
        }
        System.out.println(platform.score());
    }

    record Platform(List<Pos> stones, Set<Pos> blocked, int height) {
        enum Direction {N, W, S, E}

        Platform cycle() {
            return tilt(N).tilt(W).tilt(S).tilt(E);
        }

        Platform tilt(Direction d) {
            Set<Pos> occupied = new HashSet<>(blocked);
            switch (d) {
            case N -> stones.sort(Comparator.comparingInt(Pos::y));
            case W -> stones.sort(Comparator.comparingInt(Pos::x));
            case S -> stones.sort(Comparator.comparingInt(Pos::y).reversed());
            case E -> stones.sort(Comparator.comparingInt(Pos::x).reversed());
            }
            List<Pos> newStones = new ArrayList<>();
            int xDiff = d == W ? -1 : d == E ? 1 : 0;
            int yDiff = d == N ? -1 : d == S ? 1 : 0;
            for (Pos stone : stones) {
                int x = stone.x();
                int y = stone.y();
                while (!occupied.contains(new Pos(x + xDiff, y + yDiff))) {
                    x += xDiff;
                    y += yDiff;
                }
                Pos newPos = new Pos(x, y);
                newStones.add(newPos);
                occupied.add(newPos);
            }
            return new Platform(newStones, blocked, height);
        }

        long score() {
            return stones.stream().mapToInt(stone -> height - stone.y()).sum();
        }

        String serialize() {
            return Set.copyOf(stones).toString();
        }
    }

    record Input(Platform platform) {
        static Input parse(String input) {
            char[][] rows = input.lines().map(String::toCharArray).toArray(char[][]::new);
            List<Pos> round = new ArrayList<>();
            Set<Pos> blocked = new HashSet<>();
            for (int y = 0; y < rows.length; y++) {
                blocked.addAll(List.of(new Pos(-1, y), new Pos(rows[y].length, y)));
                for (int x = 0; x < rows[y].length; x++) {
                    blocked.addAll(List.of(new Pos(x, -1), new Pos(x, rows.length)));
                    switch (rows[y][x]) {
                    case 'O' -> round.add(new Pos(x, y));
                    case '#' -> blocked.add(new Pos(x, y));
                    }
                }
            }
            return new Input(new Platform(round, blocked, rows.length));
        }
    }

    private static final String TEST_INPUT = "O....#....\n"
                                             + "O.OO#....#\n"
                                             + ".....##...\n"
                                             + "OO.#O....O\n"
                                             + ".O.....O#.\n"
                                             + "O.#..O.#.#\n"
                                             + "..O..#O..O\n"
                                             + ".......O..\n"
                                             + "#....###..\n"
                                             + "#OO..#....";

    private static final String INPUT =
            "..O.O....O...O...O...O.#O#.#.#O#O..#.#O....O...#..#O#...............#O..#.#..O..O.##.#O#..O...#.....\n"
            + ".#O.#.#O.##..O..#.......###...#.O#.##....#O#O.OO..##...#..O.O...#O.......O.O.OO##...#.##OOO.O.#..#.O\n"
            + "#.##..O.O.....#.OO....O#..OOO..OO..O....#..#...O..#..O..O.O#O.....O...O.#.O.O......O..OO..O...O...OO\n"
            + "O.....#.O...#....#.#O.#.O.........O...O..#.O.O...O#..O.OO......O.#O......O#.OO.#.##..OO.O#O.O.#.....\n"
            + "#O...#..........#..#......O..O..#......#O..OO...#O.O.............O..O.......#....O.OO#..OO#....O#.O.\n"
            + "##O.O.....OOO..OO..O...O.OO...#.O.O...#..O...OO...O..O.#OO.#........O........O.O....O...O...........\n"
            + "O..O...#.O.##.....OO.O#.......OO...OOO.#..#OO....#..O#...O.....O..O.#####..........O#..........O#...\n"
            + "OO......OO.O...#.#......#.#....OO###...O####OO..#...........##..O#O...#.........#..#....#OO.O.#....O\n"
            + ".##..O.#...O.#.O#..#.O.#...OO##O.....O.O.......O...........##...O....#OOO.....#...O.....O.O##.O###OO\n"
            + "O..O...O.O...O#O.OOOO.O.O......O#...O...#.#.......O#.O...#..#O.....#........O#...O..O..O.#.#.#O#OOO.\n"
            + "O#......#.#.O...OO#..#.#.#.O#.O#.......O#...O.O..O..........O........#.#O....#..OO..#..O..#.O..##...\n"
            + "OOO.#.OO.O..OO.OO..O....O...OO..#..#OO.O...#....#.OO.#..O.O#.O.#OO.O.....O......#.O..O#O...OO##.#...\n"
            + ".O#O..#OOOO#.O##.........OO#O#.#.O#..#..OO.....O.##.#..O.O....#...#..O....O.#.O#..O....O.O.....O.O..\n"
            + "..O#.OO..........#OO...O..O...#.####.#...#.......#####O.#.##..#O...O...O.....O......#.O.....O.......\n"
            + ".O........#.O.###OO..............#.......O...O.....OO#...#.O...#.O.O.O.O.......#.O..#O....##..#..O.#\n"
            + ".#.O#..#....O...#..O.#.O....O#......#...#...OO.O....#.O.O..#.......##OO..O.....OO...OO#...OO...###..\n"
            + ".#O.#.#...O.#..#.......#....#O.O.#....OO.O##.O.#..OO.O......##.....OO...O#..O.O.OO.#.O.O....#.......\n"
            + "O..O.OO.O.....O..O..#....O.......O.##O#...O.....O#OO#.....O..O.....O..#..O...............##.....#O..\n"
            + "....O..#OO#..#.#.O.#.#.OOO.OO#....OOO#....#.........O..O.#....#....OO.O.##...##O.O..#.O..#....O..O#.\n"
            + "#O.O.O.O.#O##...O..O##.O#....#......O...#.O..O.O....O.##O....O..#...O....##.#O.O#O...#.O#.#...O..O..\n"
            + "#..O.O..O##.O..O....O..OO#.OO........O#....O....O............O#.#.OO..#.#O.#....##.....OO..O...O...O\n"
            + ".O..OO.OOOO.#.#.O.#.O..O##....##O..##..O.#O.O#.#...#....#.....#OO..O#O......O........O...O........#.\n"
            + "...OO.O###.O.###.......#O#..O..#.#O...O#...O...O#..##..OO.O...O......O....#.#....OO.O.O#...OO...##O#\n"
            + "#..OO.....#.O.OOO.#...O.......O......#O...##...#O##..OO.#.O.........O.O...O...O..##...O..#..O#.....O\n"
            + "...O.#.#.O....##O...#O#...O....O..OO..#O.O....#O..#...O.OO.....OO.OOO.O.O.#.....OO#O.##..#..#.O#...O\n"
            + "..O#.O......##O#.OO..........#......#.OOOOO...#.O##......O...O.#OOO.O#O..#..O....#...O....#O..O.O...\n"
            + "...#...##O.O......#O...O#O...##OO.O......O..#O#..O.....#.OOO..#.O.O#..###.O....O.O.OOO....OO#O....#.\n"
            + "O#O..##..#O.#..........OO...O.O..###.O..O#.O.O.O.#.#.O.#.O...O.OO....#.#.....##..OOO#..OO......O#.##\n"
            + "O..O#O..#.#O.OO...#.OO.OO.##.O....#.....##O..O#..O.O.OO##.O#O.O#.O.#O..O.#.O....O#.#.O....#..OO....O\n"
            + ".O..##O#OO...O...O.....O....O.O.O........O........##O#OO.O.OO.O.##...##......O....#.O.O....O#.O...#.\n"
            + ".O.......#.#...O.....O...#........O#....O#.OO....#...O#.#O...#..##.#....##.OOO.#......#..O...OOO.#.O\n"
            + "......#O..##........##O#.......###.....O..OOO.......#.OO.#.....#...#.##.#..O......#O...O.O..O.....O.\n"
            + ".OOO#..#.....#.....#......O#O#....O.OOOO###.....O....OO.#O#...#.OO......O.O........#OO#.O..O........\n"
            + "#O#.#.OOO###...O..OO.O...#.#........#.#.#......OO#.....O...O....O.#....#..#..##.#.OOO#....#.#.O....O\n"
            + "#O#.#.##....#.O....##.....##.O.O.OO#.....#O...O#.....#..O.....#.#......#O..O...O.....#..O....#...#..\n"
            + ".O.......#.....O.O#.#.O.....##..#OO.#OO.O.O.#O.O.#O##OO.#..#O.O.....#......O#........#.#O...#.#.O...\n"
            + "#..O.##......O...O.O#..OO#...#..O..#OO.#O.O#....O#..#....O....O..#O..#.#......O...#..OO#....O.O....O\n"
            + ".#......#.#......#O............#OOO......#.OO..O#.O#.......#....#....##.O.......#...O.O#...O...#O..#\n"
            + "O...OO..O#......#.O........#.#....OO.#......O....O##..........O#..#....#O...O#.O..#....OOO..O....#O#\n"
            + "O...O...#.O.O.O.OOO..OO.##.O....O......#.#O...#...O...O#....O.#...#.......#.##....#.#.O.OOO..O...O.O\n"
            + ".#.O#...##O#.O..O....O.#.....#O.....#.O.....#...O#.O.O..#....O#OO..OOO..#O.OO.O...OO.OOO.#...##.O..#\n"
            + "........O#O#...#..OOO#...#O.O#..#.O.....O.OO.O#O..##.O.....O.#OO.........O.#OO.....OOO.....O.O.O.#..\n"
            + ".#....OO.#............O#..#.....OO....O.O.#..#...OO..#....#...#.#O....##..O....#O..#OO..O.....O....#\n"
            + "O..#.....O.O...O........O....#.#O...#......O....O......O......O..#.OO......#O...#..O.##......O#....O\n"
            + "...O.###..#...#.#..O.#.............#OO.#...#...##.#.O#.........O..##.##.#....O.....O.......O..O...O.\n"
            + ".O##....O...#....OO.##.#...O......OO..........#.O.......OO#.O.#..#.OO...#......OO#O.......#..#...#..\n"
            + ".#.O..#..#.OO.....O..O#.O..O....#.....O#O.OO.#.OOOO.##.O..O...O.O..#....O.....O.#...O.#...#.....O..#\n"
            + ".#.#O..#....#O..O.OO..O.....O....###..#...O.#....O#O...#....##...##.O.##..O...OOO.#..#O.....OOO#.O..\n"
            + "O..#O....#..............O.O.O..O.O.#..........O.....O#.O#.O..O.#.......O.OO...#.....##.#..O.#.#.#..#\n"
            + "#O..O..#.O.O..O#.OO....#O..#.....O..#..OO.#..##O.........#...O.#.....#..#.OO..O.##...#...#O.#O##..OO\n"
            + "#.........O...#..O.##O......#.O..##..#O..........#.#O#O........##....O....O....#..#...#O......O#.O.#\n"
            + "#.#..O.O.O.#.OO#.#..O....O#O.O.O.O..#....#..#.....#..O......O#...O.O.O....OOO##O.###.....O.O#.#..#.#\n"
            + "O...#OO..#...O..O..#..OOO.#....#..#.##...#..#....OO.O.O............#.#O#O#OO.OO...O.##O.##O#O....OOO\n"
            + "O..O.......O.#OO.O#........O###...O........O..#.O.OO.O...OO..OO.#O.O.#O.OO.O....OOO#.#.O.......O.O.O\n"
            + "#O..#O.......#..##.#....O.O#O#O.O#OO..O...O.O.O.OO.#..O#.......OO.O.##.O....O...#O......O....#O.O...\n"
            + "#..O........#..#..#O.OO##..OO...#..O#.O##....#.#.#.#...#....O.....O.....O...OO#O...#..O....#..##..O.\n"
            + "..O.....O.OOO#O....#.#.......#.OO....O.....OO#..O.O#.#.O...#...#..#.#OO..O.....#.O.....#...O...#.O#.\n"
            + "#.....#.....#O.#.O#O.O....#.....O#O#O#OO...O.....O#.........O..#....O.....O.......O.O#OO##....O#....\n"
            + "OO....#..O.#.O#....O#.O..#.#....#.#..#...#....#..OO......OO.O..###O....OOO.O..OOO#.#.O...#.OO.#..O#.\n"
            + ".##.#O...#......####......#...#..#.O.......O#..#.#...O#...O..O.........O##.#....O#OO..OO......O..##.\n"
            + "...O..#.O.O..#O..#O............#O#O..#.#...O..#......O..O...O##....#.#.............O..#...#......#..\n"
            + "#.#..#...##....#O....O.O.O.....#...#O..O.#..#.#O.O.#OOO.....#..#..#OO..O#........#.#.O..........O...\n"
            + "...#.O#......#...O...OO..#.#O.O..###O#O#...#.#..#....O#..O.#OO...#...O.##O...#.O..O#..#.O.#..#..OO..\n"
            + "...#....O...#.#.......OO#..OO.O..####.....##O.#...#.O....O......#......O#...#..#.O..O.OOO.O......#..\n"
            + "#..##..O..O....O..#O.O.O..O#.#.#..O..#.#...O.......OO#.OO...O..#.O#..O...OOO..O...O#..O....#....O...\n"
            + ".#.#...#.O.O..O..#..O........O....O#.#...O.O...#...O.O##O....#.#.O...#.O..#OOO..O.....#.O....##..##.\n"
            + ".......#OO..O..#.#....O.....O#.OO.O....O.........##.....OO......O...O#.....##..O#...#....OOO...##...\n"
            + "..#O...O.....O.O.##O..O.....O...O#O......#.........O..OO.....O.#.#OO..#....O.#...O..#.....#..O...O#.\n"
            + ".....O.O...#O.##.#O#..#.OO#.O..#O.O.#.OO.#OO...OO#O.##.O.#.O.##.#..#.#.............O.O.#.O..O..#...#\n"
            + "...O.#.....#.#.#..OO.O.O..#.O.....#.##......OO.#....OO.#.#O..#.O.....#.....##..O.#O#..OO.........#..\n"
            + ".O.#O.........#O....O.O....#.O.O..#.O#O..O.#.OO......O#....O#O.O.............O..#...O.OO#...O..O...O\n"
            + ".......#...#....#.....O......O.OO.O....O#.O...#....#...........#.O#..#..OOO.#O..#.O...O#..##.....O.#\n"
            + "........#....##...O..O.............#.O...O..#..#...O.##.#...O...#...#.O..#....###.#O.#.O.O..O....O.O\n"
            + "O.#....OOO..O.O.##.........O...O...OO#.....#O..OO..#.....#O..O#.O#.#O.#OO#...O#O....O...O...O......O\n"
            + ".O..........##.O.#..OO....#.O..........O.#....#.O...O.O.....OO.#..O...O....##...#OO..#...OO#.O#O.#..\n"
            + "O......O#O....#.##O.##OO.#....OO.....O.O.OO..O#.....#..OO.O..OO......#..O.OO..OO..O...#..O...#O.....\n"
            + ".###....O.....#O..#O....O.....#.OO..##....#.....O.#......#.....O##O...OO...O##O#....O#.........O#O#.\n"
            + "OO.#O##....#O.#...O........OO.....#..O...O..#.O#.#.#..OO#O.##.....#.....O.O....OO.....O.O....#...O#.\n"
            + "....O#OO..OO......#O.....#.O...OOO..OOO.#O....O..#.#.###..O..O..O#..........#..OOO...OO.O#O.#.#..#O.\n"
            + "O....#O.O.O.......#......O.......O.O...O...O....##O..O.OO#O....O.#............O..#O.##...O..........\n"
            + "O.......O....O##....#......OO..##.O....#O..##..O......O.O.......O#.#...###..#.....##..##.....O......\n"
            + ".O..O###O.........#.O.....OO..O.#.O.O...#O#.O.O..##..O..O...O..#...#O.#..#..O..#....O..O.O#.O#.O.OO.\n"
            + "..#.........O#..#OO......O.O#..O#.........O.#OOOO.....O..#O..#..#..OO.OO...OO...#..OO#...##..O...#..\n"
            + "..OO..#....O..O.O..OOO..#O#O##..#...O#O...#....O..O..#.O.OO..O.O......#O........#.....O.#...O#O...OO\n"
            + "..#......O##O.....OO...##......O...OO#....O..#OO#..#.O..O.#.......#..O.#.O#....#....#.OOO.#.....#...\n"
            + "..#.O.O.O.#....#.#.O.##.....#.O#.OO..OOO..O......O.........#....O..#..O.###.#..##O#...O#.O.O.O.#...#\n"
            + "#..O..O..#.#.O...#O#..O.....#O.O.....#............O..#.O#.....O....O..O.OO.O..#....OO.....###..#....\n"
            + ".O.O.#.....O#O##...#..#...#........#..O#.....O..#..O...O.#.....OO..O.#.##....OO#.#.....O..O....O.O..\n"
            + "........O..O.#..O#O...#........O.#O#...O#O##......O..#....#...O...O..O.O..##.OO....#.....#.O...#O...\n"
            + ".O.O..##O..O...OO...O..O.O#O...OOOO..O....O........O#...O..O#....O...O#OOO.O##....##..O.....OO.O#..#\n"
            + ".....#O.#..O......O...OO.O..#..#O....O....O....O#....O.O..O...#O.#....##......##..#..#O..#..OO...O.O\n"
            + "...#O.#.#..OO..O..........##..O#.#OO.......#...#O###.#O..O#O...O...#..O....O#O#O.#.....O.O.O#...O..#\n"
            + "##..#......O##..O.#...O....OOOO.....#....O.#...#.........#..O.#..O.....#...O...O.......O#......O#O..\n"
            + ".....O......O..##O#.....#O#...#O....O..O..O.......O#O##....OOOO..##...O.O#...OOOO...#OOOO##O..##....\n"
            + "#...O.O.O#..OOO...#O..O##.....#.#..#O...O###OO...O.OO.......O.O..#...###...........O..#....#.##...OO\n"
            + "O.OO......#....#........O#.#.#O.....#..........O#.#..##O...#..OO...#OO.#..O.#O..#........O.O..O#.O##\n"
            + "#.##...O.#.....OO...O..#.#.O.O.O.#.#O...#....#.#......#...#...#.O.#O#..#..O...#.OOO..#OO##..OO...#.#\n"
            + "O.....#.....#.O.O...O#.#...OO..OOO..O..O.O.#...O..#....O.#..OOO.O.O#O..OO#O..OO.O...O.........##.O..\n"
            + "..#....O........#.#O....O..#....O.###OO..#.#O.O..O.....###..OO.O..##...#.#..#O..O.....O.#.#O...O....\n"
            + ".O.OO..O#........O#..O..##...#O.......#..#.....#.........OO..#.O..OO#O..#....#OO......O............O";

    public static void main(String[] args) {
        Util.time(() -> a(Input.parse(TEST_INPUT)));
        Util.time(() -> a(Input.parse(INPUT)));
        Util.time(() -> b(Input.parse(TEST_INPUT)));
        Util.time(() -> b(Input.parse(INPUT)));
    }
}
