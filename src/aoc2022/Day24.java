package aoc2022;

import static aoc2022.Day24.Blizzard.Type.D;
import static aoc2022.Day24.Blizzard.Type.L;
import static aoc2022.Day24.Blizzard.Type.R;
import static aoc2022.Day24.Blizzard.Type.U;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aoc2022.Util.Pos;

public class Day24 {
    public static void main(String[] args) {
        Util.time(() -> a(TEST_INPUT));
        Util.time(() -> a(INPUT));
        Util.time(() -> b(TEST_INPUT));
        Util.time(() -> b(INPUT));
    }

    private static void a(String input) {
        Valley valley = Valley.parse(input);
        Set<Pos> walls = valley.walls();
        List<Blizzard> blizzards = valley.blizzards;
        int minutes = explore(walls, blizzards, valley.start, valley.end);
        System.out.println(minutes);
    }

    private static void b(String input) {
        Valley valley = Valley.parse(input);
        Set<Pos> walls = valley.walls();
        List<Blizzard> blizzards = valley.blizzards;
        int minutes = explore(walls, blizzards, valley.start, valley.end);
        for (int i = 0; i < minutes; i++) {
            blizzards = blizzards.stream().map(b->b.next(walls)).toList();
        }
        int minutes2 = explore(walls, blizzards, valley.end, valley.start);
        for (int i = 0; i < minutes2; i++) {
            blizzards = blizzards.stream().map(b->b.next(walls)).toList();
        }
        int minutes3 = explore(walls, blizzards, valley.start, valley.end);

        System.out.println(minutes+minutes2+minutes3);
    }

    static int explore(Set<Pos> walls, List<Blizzard> blizzards, Pos start, Pos end) {
        Set<Explorer> possible = Set.of(new Explorer(start));
        int minutes = 0;
        while (true) {
            List<Blizzard> nextBlizzards = blizzards.stream().map(b->b.next(walls)).toList();
            Set<Explorer> nextPossible = new HashSet<>();
            Set<Pos> blocked = new HashSet<>();
            blocked.add(new Pos(start.x(), start.y() < end.y()? start.y()-1 : start.y()+1));
            blocked.addAll(walls);
            nextBlizzards.stream().map(b->b.pos).forEach(blocked::add);
            for (Explorer explorer : possible) {
                if (explorer.pos.equals(end)) {
                    return minutes;
                }
                nextPossible.addAll(explorer.possibleMoves(blocked));
            }
            blizzards = nextBlizzards;
            possible = nextPossible;
            minutes++;
        }
    }
    private record Valley(List<Blizzard> blizzards, Set<Pos> walls, Pos start, Pos end) {
        static Valley parse(String input) {
            List<Blizzard> blizzards = new ArrayList<>();
            Set<Pos> walls = new HashSet<>();
            String[] rows = input.split("\n");
            for (int y = 0; y < rows.length; y++) {
                String row = rows[y];
                for (int x = 0; x < row.length(); x++) {
                    char ch = row.charAt(x);
                    Pos pos = new Pos(x, y);
                    if (ch == '#') {
                        walls.add(pos);
                    } else if (ch =='<') {
                        blizzards.add(new Blizzard(L, pos));
                    } else if (ch =='>') {
                        blizzards.add(new Blizzard(R, pos));
                    } else if (ch =='^') {
                        blizzards.add(new Blizzard(U, pos));
                    } else if (ch =='v') {
                        blizzards.add(new Blizzard(D, pos));
                    }
                }
            }
            int maxY = rows.length - 1;
            Pos start = new Pos(rows[0].indexOf('.'), 0);
            Pos end = new Pos(rows[maxY].indexOf('.'), maxY);
            return new Valley(blizzards, walls, start, end);
        }
    }

    private record Explorer(Pos pos) {
        public Set<Explorer> possibleMoves(Set<Pos> blocked) {
            Set<Explorer> explorers = new HashSet<>();
            if (!blocked.contains(pos)) {
                explorers.add(this);
            }
            pos.neighbours().stream().filter(n -> !blocked.contains(n)).map(Explorer::new).forEach(explorers::add);
            return explorers;
        }
    }

    record Blizzard(Type type, Pos pos) {
        enum Type {U, D, L, R}

        public Blizzard next(Set<Pos> walls) {
            int maxY = walls.stream().mapToInt(Pos::y).max().orElse(0);
            int maxX = walls.stream().mapToInt(Pos::x).max().orElse(0);
            return switch (type) {
                case U -> new Blizzard(type, new Pos(pos.x(), pos.y() == 1 ? maxY - 1 : pos.y() - 1));
                case D -> new Blizzard(type, new Pos(pos.x(), pos.y() == maxY - 1 ? 1 : pos.y() + 1));
                case L -> new Blizzard(type, new Pos(pos.x() == 1 ? maxX - 1 : pos.x() - 1, pos.y()));
                case R -> new Blizzard(type, new Pos(pos.x() == maxX - 1 ? 1 : pos.x() + 1, pos.y()));
            };
        }
    }

    private static final String TEST_INPUT = """
            #.######
            #>>.<^<#
            #.<..<<#
            #>v.><>#
            #<^v^^>#
            ######.#""";

    private static final String INPUT = """
            #.########################################################################################################################
            #>v<^^><<>^>^>v<>>^^.v>^vv<^v<v><vv.^^^^v<v<v><<^v.<>^^^>>>>><^<.vvvvv.^v>vv<>v<v<^>^v<^v.<>>v<><v^v>.v>^vv^>><v>v>.^>><>#
            #<.v<v>>>.>>>>^<vv><^^v>>v>^><v^>^<.<v^<>>><v<vv<<^^<^^v^<.>v.v>>.v<^><v<v<>^><^>^>><.<<<<^>^^^v.v.>v><^<^v^>.>^>>vv.v^<<#
            #<<>v>^<<^v^<<<vv.>^^>^^v^>>v<>v>v<><^^v<^>><^<>^^v^vv<v<<<>^v^v<.<^>^<>>>.<><<<v<v<<<vv<>>v.^^<v.vv<^<^><^<vv^v<v<v>v.^<#
            #.v^v<v<>>>.v^vv<<v^<vv<<vvv^>^^v.v^v^^v<>^^^^^><.v^v>>v>>^>vvv^^<<^>^v^.v<^<<^v^v>>vv.<<^<<v<v>>^v>^>v.v^><><vvv^>v<>^<<#
            #<v<^<v<^<>v>>v^>>^<<>>v.^^<>vv^<><.v^>><<^<v><<^>>^^^<vv>^<v>.>><<.v^v^<v>^<v>>>>vvv<><^><<<v>v>v.^<<.<v^<>vv<v<<>v>^>v>#
            #.^.<>v>v^v<v>>v<>v>vv<.^><^v<<><>^.^>>v<^v.v^^^^<vv>^<vv^>vv^>^.<>.<^^v<>><^^^.><^v>v^^<>v>>v><>>v.^^<>>v<<^<^<v<vv.>^<<#
            #>^v^>^v>...^<>^.<v>.v<v<>^^v<<<^.v>v>^.><<.v<v<>.>v>>v>>><<v<><^.v>.>>>^>^>.v<>>>v<^^>^v<<^v>^v^<v.v>><>><vvv^v>v^v<v<<<#
            #<^.vv><<><>v^^<^.<^<><..v.^vv<v.vvv><v<^v>v<^v>v^v^^^^<>^^^.^^^v^v>>v^>><>v>^<>v^^.^<^^<v>>v^vvvv>>v^><^<<>>>^<<^v^^<<><#
            #<>><^<^<<^v^>>>v<.>..v>.^>>^v^<<v^<<vv<^v^><<.><^^><.vvv^>>.>>^<^vv><.v>^^>>>.>^vv<^<<>vv<>vv^^^v.>>>><..v>.^<^v<>v.<vv<#
            #<<.<><>.<^<<<<^>^<>^vv^>^>^>vv>>v>>v^>.^.>vv<><.v><<>>vvv.v^^vv>>>v<v<><^<^<>^.v<^^>^><.>>^^^^^vv<<^>^<<v<^v^<.v<<v<<.<<#
            #>>v>.^.<><>^.vvv<vv>^>.^^.v>^v.v>^<vv^v>v>^v^vv<<>><v.<><<v><>^<^<v<>.<^<^^<>^<>>v^v.^<v^^<v^>v<<^.^>>^v>^^^>vv.v.>^v>^>#
            #>^<>v^^^v<v^vv>>v.^>>>^v>><>^v^><v^.v><<>>><^<<.<>v<><><^v^>^^<^^>>^v^>^>^vv^<>>>^v^<.v^^^v<v<v...>^>^<^^v<v^^v>^^>.<^^>#
            #<v<v^^v<>><<<^vvv<vv>>.^.<>>.^v^^<>>v^^>v><>v><v<^^.v^>>.vv^v><>^^v<>>^>^<<^.>v<v^vvv^.><<<v.^^<vvv^<.>>^.<<vv^<<<>.^>><#
            #>.>^.v.>>^<v><><^<.>^v<><>v^^<.>vvv<><>.<v<>.><<.^v^^v^.>vvvv^>.<>><<^v<<>^<<<><<>>^<^>^v<^v<>v^>v<v<.>^<^vv<>^<^>>^>^<<#
            #<.^.>^vv<><>^^<^<.<>vv.>^<<<>v><.<<v<>^<>>v<^^<v^v^^>v<v<^<vv<.v^<>v<^>>.<>^<^.vv><.><v<^<<^<<>^<>v<vvv^v^^>><v>v>..v<<>#
            #<^v>><<v.^^.<^>^<^vvv.^v^v<>vv^v<^vvvvvv^v><>>.^<<^v>v.v.>.^<<^^>>>v^><v^>>>.v^^>.>><^.^^.>.>v^<v^>^<<v<<^><>v<^^>^<v<v<#
            #.v><v>>>v^>^^v<>^<<>>^<v>^><<v^><^.<<v<vv<<^v^^><>><^^><><^^^vv>.^>.<v.^v<v<^<<.v.>>>><<<^v<<.<^<>v.^<^<vvv<.v<.v><^>v<>#
            #<v<v^<^v><<^<>^>>>>^^v<>^^>v<v.^^^^^.^<^<<^><<v^><^.><^><v^>.^>v^v>v.><^<^>v<>^>.<>^..<<>^^>^<><^<^..vv>v^^^v>^^<..^.v<>#
            #>v<<v<<<^.^vvv>>v.>v^v>v<><v.^><^v<>^v<v^v<v<.^^.>v>.v>vvvv^>vv^>^^v><v^>>>.<v^>><.<v><<.>>^^v^vvv<vv^^^<^>v^>vv>^>^vv>>#
            #><<v>^>>>>vv<<><>^>^v>^vv^<^v><>v^.<.^.v>^<<^v>v<.^v.>^v<^^^>v^^.^>vv.<v^>>^.><<>>^<<>.>v.v>>^..>><<..v^>><>v<>.<^v.>>^<#
            #.<<^^<.^v>>>>.>^vv<^<<>>>>^v>v<^.v^<^>>v^<^>^^>>v<>^<v^^^^>><^<^^>>v<.<<v.^v<>>.vv^^^<v^^.^v.<>^>^>><><^.vv<v>v>v<^^>.<>#
            #<<<v>.^v>v>v<^^>vv^<vv<vv><v>v<vv<.>vv<^^^<.v<>..^<<<<<^^^>><v^^>>^<<^v^>><.^<^.<^.<v<vv<..^.><<>vv<^<v<v.<>.<<^v<<^>vv<#
            #.>^^.<<>.>>>.<^<>.<<v<^>v>v>v^v^<v>v^<^>^<<>v^^v^v^^^<>^vvv^vv<.^^>.v^<.>>^>vvv^>.v^>v^v^>.^^<v<<vv^^>v>^vvvvv<vvv^<<>>>#
            #<^v^.<<<><^<<v^^^v^^^<^^<<>.^<^v<.>.^^..^v<<^>^.<<>>.<^>v.^<v>.v^^.<vv^^>v^><v^><^.>>>>>^v<v<^vv^^>^v^.^.^^^>v^<.><>^^>>#
            #.v^vv.^v>vv>>v<vv^^v<v>><.<>><^^>^v.vvvv>><^^>v^v>.v^<.<^.<^<^v.><v>^v<<^^<>vv>>>^v^v^<^<^^^>..<><^<^.vv<<<v>v^.^>^<>^><#
            ########################################################################################################################.#""";
}
