package aoc2022;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import aoc2022.Util.Pos;

public class Day23 {
    public static void main(String[] args) {
        Util.time(() -> a(TEST_INPUT));
        Util.time(() -> a(INPUT));
        Util.time(() -> b(TEST_INPUT));
        Util.time(() -> b(INPUT));
    }

    private static void a(String input) {
        Set<Pos> elves = new HashSet<>();
        String[] rows = input.lines().toArray(String[]::new);
        for (int y = 0; y < rows.length; y++) {
            for (int x = 0; x < rows[y].length(); x++) {
                if (rows[y].charAt(x) == '#') {
                    elves.add(new Pos(x, y));
                }
            }
        }
        Direction dir = Direction.N;
        for (int i = 0; i < 10; i++) {
            Map<Pos, Pos> elfToMove = new HashMap<>();
            Map<Pos, Integer> newPosToCount = new HashMap<>();
            for (Pos elf : elves) {
                List<Pos> neighbours = elf.allNeighbours();
                boolean shouldMove = false;
                for (Pos neighbour : neighbours) {
                    if (elves.contains(neighbour)) {
                        shouldMove = true;
                        break;
                    }
                }
                if (shouldMove) {
                    for (int j = 0; j < 4; j++) {
                        Direction d = Direction.values()[(dir.ordinal() + j) % Direction.values().length];
                        if (d.toCheck(elf).stream().noneMatch(elves::contains)) {
                            Pos newPos = d.move(elf);
                            elfToMove.put(elf, newPos);
                            Integer cnt = newPosToCount.get(newPos);
                            cnt = cnt == null ? 1 : cnt+1;
                            newPosToCount.put(newPos, cnt);
                            break;
                        }
                    }
                }
            }
            Set<Pos> newElves = new HashSet<>();
            for (Pos elf : elves) {
                Pos newPos = elfToMove.get(elf);
                if (newPos != null && newPosToCount.get(newPos) == 1) {
                    newElves.add(newPos);
                } else {
                    newElves.add(elf);
                }
            }
            elves = newElves;
            dir = Direction.values()[(dir.ordinal()+1) %Direction.values().length];
        }

        int minY = elves.stream().mapToInt(Pos::y).min().orElse(0);
        int maxY = elves.stream().mapToInt(Pos::y).max().orElse(0);
        int minX = elves.stream().mapToInt(Pos::x).min().orElse(0);
        int maxX = elves.stream().mapToInt(Pos::x).max().orElse(0);
        int cnt = 0;
        for (int y = minY; y <=maxY ; y++) {
            for (int x = minX; x <=maxX; x++) {
                if (!elves.contains(new Pos(x, y))) {
                    cnt++;
                }
            }
        }
        System.out.println(cnt);
    }

    private static void b(String input) {
        Set<Pos> elves = new HashSet<>();
        String[] rows = input.lines().toArray(String[]::new);
        for (int y = 0; y < rows.length; y++) {
            for (int x = 0; x < rows[y].length(); x++) {
                if (rows[y].charAt(x) == '#') {
                    elves.add(new Pos(x, y));
                }
            }
        }
        Direction dir = Direction.N;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            Map<Pos, Pos> elfToMove = new HashMap<>();
            Map<Pos, Integer> newPosToCount = new HashMap<>();
            for (Pos elf : elves) {
                List<Pos> neighbours = elf.allNeighbours();
                boolean shouldMove = false;
                for (Pos neighbour : neighbours) {
                    if (elves.contains(neighbour)) {
                        shouldMove = true;
                        break;
                    }
                }
                if (shouldMove) {
                    for (int j = 0; j < 4; j++) {
                        Direction d = Direction.values()[(dir.ordinal() + j) % Direction.values().length];
                        if (d.toCheck(elf).stream().noneMatch(elves::contains)) {
                            Pos newPos = d.move(elf);
                            elfToMove.put(elf, newPos);
                            Integer cnt = newPosToCount.get(newPos);
                            cnt = cnt == null ? 1 : cnt+1;
                            newPosToCount.put(newPos, cnt);
                            break;
                        }
                    }
                }
            }
            Set<Pos> newElves = new HashSet<>();
            boolean someoneMoved = false;
            for (Pos elf : elves) {
                Pos newPos = elfToMove.get(elf);
                if (newPos != null && newPosToCount.get(newPos) == 1) {
                    newElves.add(newPos);
                    someoneMoved = true;
                } else {
                    newElves.add(elf);
                }
            }
            if (!someoneMoved) {
                System.out.println(i+1);
                return;
            }
            elves = newElves;
            dir = Direction.values()[(dir.ordinal()+1) %Direction.values().length];
        }
    }

    private enum Direction {
        N {
            @Override
            List<Pos> toCheck(Pos pos) {
                return IntStream.rangeClosed(-1, 1).mapToObj(dx -> new Pos(pos.x() + dx, pos.y() - 1)).toList();
            }

            @Override
            Pos move(Pos pos) {
                return new Pos(pos.x(), pos.y()-1);
            }
        },
        S {
            @Override
            List<Pos> toCheck(Pos pos) {
                return IntStream.rangeClosed(-1, 1).mapToObj(dx -> new Pos(pos.x() + dx, pos.y() + 1)).toList();
            }
            @Override
            Pos move(Pos pos) {
                return new Pos(pos.x(), pos.y()+1);
            }
        },
        W {
            @Override
            List<Pos> toCheck(Pos pos) {
                return IntStream.rangeClosed(-1, 1).mapToObj(dy -> new Pos(pos.x() -1, pos.y() + dy)).toList();
            }
            @Override
            Pos move(Pos pos) {
                return new Pos(pos.x()-1, pos.y());
            }
        },
        E {
            @Override
            List<Pos> toCheck(Pos pos) {
                return IntStream.rangeClosed(-1, 1).mapToObj(dy -> new Pos(pos.x() +1, pos.y() + dy)).toList();
            }
            @Override
            Pos move(Pos pos) {
                return new Pos(pos.x()+1, pos.y());
            }
        };
        abstract List<Pos> toCheck(Pos pos);

        abstract Pos move(Pos pos);
    }

    private static final String TEST_INPUT = "....#..\n"
                                             + "..###.#\n"
                                             + "#...#.#\n"
                                             + ".#...##\n"
                                             + "#.###..\n"
                                             + "##.#.##\n"
                                             + ".#..#..";

    private static final String INPUT = ".##.#.#......###..######...##.##.#..#.#.#.#######.###.#####...#...#..#\n"
                                        + "#...######.#.#.#.##....#..##.###.#..##.#..#.....##.....#....###.##.##.\n"
                                        + ".##.#.##..#.....#...###..#...##.##...........#.#..###.###...####.##..#\n"
                                        + "##.#.##.#.####......#####..#.....#..#....###..#..####.##.#.###....####\n"
                                        + ".....#...#.######...###..#....##.#.####.###.#..###...#.#.#..###.##...#\n"
                                        + "#.#...#.##.....#..#######..##.###.###.####.#.##..##........#.#####..#.\n"
                                        + ".#..###..#.....#..##.....#..#.##....#....##..###..#####.#....##.#.#..#\n"
                                        + "#..##.##.#.##.#..#.#.#.#..#.#.#..#.#...######.###.#..##.##...##.#.##.#\n"
                                        + ".##....#....#..#..#.###.......##.#..###..#....#.####....##.#.##...###.\n"
                                        + ".....#.###.#....##.....#...###...##.#.#.#.....###.#..#..##..#..##.#.##\n"
                                        + "..#..####..#####.####..#...#####..###.#..###.#.#####.####..##...###...\n"
                                        + "..#.#.#.##.##.#.##.###.....##...##...#....#...#...##...#.#..####.#.###\n"
                                        + ".##.#..##..........##.#....###..##....##.#.##....##.##.##.#.#.###.##..\n"
                                        + "..###....#.#.#.....#.##.#.....#####.#.#...##..#..##.#....##.....##....\n"
                                        + "###..##.#.#.#...#.#..#.#.#.##.#.#.....#..##.##.....##.#.#.##..###..###\n"
                                        + "#....#.#...#..##.###...###.#.#####...#.###...##.####..##.####.#..##..#\n"
                                        + "##..#.#.#####.#..######..###.......#..##..#..#.#.###.###.#..#.#..###..\n"
                                        + "..####.#.##.###.#...##.##.......#...#...##.#.##..#.#.#.#.#...#..#..###\n"
                                        + "..#..##...#.#.#.#..#...###..#.....###.##.####.#..#.#..#.....##.##.#.##\n"
                                        + ".#.##.####..#.#.#...#.#.##....###.#.##....###...###.#...#####.##.#.#..\n"
                                        + "..##....###...#.#..######.##.#...#.##.##..#.#.##..#.######..#.#.#..##.\n"
                                        + "..#.......#...##.#...##.##...#####...##..##.#.#.##...###...#.#..####.#\n"
                                        + "#.##..#####.......#.#.#.####..##..##...####...#...###.....##..#.#....#\n"
                                        + "..#...#####.#.#..##.#..#.#.#.####..##....######..##........##..#.#...#\n"
                                        + "###....#..###..#.###.####...##..#..##..##.#..##.###....#.####.#....##.\n"
                                        + ".#.##.###.#.#####.#.#.#.#...#.#.#....#####.###.#.##..#.....##.#..###..\n"
                                        + "#.##..##.#.####....#..##.#.#####...#.##....####...##.##.#..##......#..\n"
                                        + ".#..........####....#.#.###...#....#.#.##...#.#...#...#...##.#.......#\n"
                                        + "..#.#...#..#..#...#.#..##..##.#.....#...##.##.##.###.##.#.###.##.#.#..\n"
                                        + "..##..##..#.#..#..#...##.##......#.#..#.#..##..#.##.####...####..#.#.#\n"
                                        + "###...#....#.##.....###...#..#.#.....#.#..###.#..#####..#.#.#..#.##...\n"
                                        + "#.#..#.###..##.##.#.#######.#.#..#.....#.#...########..####.##.#.#####\n"
                                        + ".#.###.....##..##...####.##.###.....###....#.#....##.#.###..###.##.#.#\n"
                                        + "######.#....##..##.#.##.##.###.####.##.###..#..##...###.#....#.#.#....\n"
                                        + "#..###...#####.....##..##.#.#....#.######.####....##..#..###...#.#.#..\n"
                                        + "......#.###....#...##.##.###.#.#....##...#..#..#..####.#.#..#.###.#.#.\n"
                                        + ".##........#...#########.##...##.#..##..#.#..#.##.####..##....###.####\n"
                                        + "#.#.###.#....#.##.##.#...##..######..#.#..####..#.####.#...#..#.##..#.\n"
                                        + "####..##...##..#.####.#.#.#.....##...###.#.#....##....##.###.##.....#.\n"
                                        + "#.####..#....####.#.#.##.....##.#####.#.#.####.##...##..#####.....##..\n"
                                        + ".##.##.....##..#.....##.###.##.###.#.#..##..####.###..##..##...##.#..#\n"
                                        + ".....#...#.##.#.#.....#.#.#.#..######..#..#.##.#.#......#....#..#..###\n"
                                        + ".#..#...##.#..#.###....#..##..###...##.##.#.#..##..##...#.#.##.###...#\n"
                                        + "..#.#.#.####.#.####....#.#....#..###....#..#.##.#....##..#.#..###..##.\n"
                                        + ".#..#.####..#.....##.######.#.##..##..#.##.##..##.#..##.#.####..#..##.\n"
                                        + "####.#...##..########..#.#..#...#.#.#####.#..#.......##.###..###.###.#\n"
                                        + ".###.###....#..#..###.....#.#.#.#...#.###.#.##.##.####.#.###..##..#..#\n"
                                        + "...##.#.#.######..##..#.#.#..#.#.#....#.....#.#..##.#......##..#####.#\n"
                                        + "#####.#.#.#.##..####.####.....##...#.#####...#.###..#...#...#######..#\n"
                                        + "...####..##..##....####.........#.#.#.##.#..#.###...##..###..#...#...#\n"
                                        + ".#..##...###.####.##.....#..#..#....#.#.#.#####.#...##....#####.####..\n"
                                        + "###.#..##.#.#.##......#.##.####...#...#..##..#..####..###...##.#..####\n"
                                        + "####.#...#.....##..#....#........###.#..#.#...#..##..#..#.#...####.#..\n"
                                        + "#.#...###.##.##..#####.##....##.#....#.##....####...###.....##.#.#.###\n"
                                        + "#.#..#.###.......#.###..#....###..#...##.###.###..###........#..#.##.#\n"
                                        + ".##.#..##..#..##..###.#...#.#.#.#.#.#.##.###.#.##.#######.#.##...#...#\n"
                                        + "#.####..###.#.....#........#...###..#..#.#.#.#..#.##....##.#...#...##.\n"
                                        + "##..###..###...#..#.######....###.##...##..##.#....#.....##..##..##..#\n"
                                        + "##.#....#.##.###..###.######.#..#.#..#.##..#.#....##.#.##..#......###.\n"
                                        + ".##..##.#..#.#.##..##..#..#..###....#..#.#.###.#....##.#..###..#.#..##\n"
                                        + "..#..########.###.#########.##.#####.....#....#####....#.#...#.#..#.##\n"
                                        + "..####...#####.#..###.#.#.###.####.###...##..#..#..##.......###.###..#\n"
                                        + ".#.####.#..##.#####.###.#########.#...#...###.###.#.##...##.#####..#.#\n"
                                        + "##...#..#..#.##.####....#....##.#.###.#....#.#.....###..#.###.#..#.##.\n"
                                        + ".#.##.....#...#...#......#..#.##..###....#.##..###......#..#.####..#.#\n"
                                        + "#.##.##.#####.##........####.##.#.###...##..####.#....#.##..##.#####..\n"
                                        + "#..#.###.##.####..#..#.##.#.#####..##...#######.#.##.####.#.#.....#.#.\n"
                                        + "###........#...#...####...#.############.#...#...#..#.#.#..#.....##.#.\n"
                                        + "...##...#...#.##...###..#..###.##....#.###.....####....##..#.#..#..##.\n"
                                        + "###...###.####.###..#.#..###.####..#.##..#..#...##.###........#.##..#.";
}
