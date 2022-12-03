package aoc2020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import aoc2020.Util.Pos;

public class Day24 {
    public static void main(String[] args) {
        a(TEST_INPUT);
        a(INPUT);
        b(TEST_INPUT);
        b(INPUT);
    }

    private static void b(String input) {
        List<List<Dir>> lines = input.lines().map(Day24::parse).collect(Collectors.toList());
        Map<Pos, Boolean> tiles = new HashMap<>();
        for (List<Dir> line : lines) {
            Pos pos = new Pos(0, 0);
            for (Dir dir : line) {
                switch (dir) {
                case e:
                    pos = new Pos(pos.x + 2, pos.y);
                    break;
                case se:
                    pos = new Pos(pos.x + 1, pos.y + 1);
                    break;
                case sw:
                    pos = new Pos(pos.x - 1, pos.y + 1);
                    break;
                case w:
                    pos = new Pos(pos.x - 2, pos.y);
                    break;
                case nw:
                    pos = new Pos(pos.x - 1, pos.y - 1);
                    break;
                case ne:
                    pos = new Pos(pos.x + 1, pos.y - 1);
                    break;
                }
            }
            tiles.put(pos, !tiles.getOrDefault(pos, Boolean.FALSE));
        }

        for (int i = 0; i < 100; i++) {
            for (Pos pos : tiles.keySet().toArray(new Pos[0])) {
                tiles.putIfAbsent(new Pos(pos.x + 2, pos.y), Boolean.FALSE);
                tiles.putIfAbsent(new Pos(pos.x + 1, pos.y + 1), Boolean.FALSE);
                tiles.putIfAbsent(new Pos(pos.x - 1, pos.y + 1), Boolean.FALSE);
                tiles.putIfAbsent(new Pos(pos.x - 2, pos.y), Boolean.FALSE);
                tiles.putIfAbsent(new Pos(pos.x - 1, pos.y - 1), Boolean.FALSE);
                tiles.putIfAbsent(new Pos(pos.x + 1, pos.y - 1), Boolean.FALSE);
            }

            Map<Pos, Boolean> nextTiles = new HashMap<>();
            for (Entry<Pos, Boolean> e : tiles.entrySet()) {
                int adjacentBlackTiles = 0;
                Pos pos = e.getKey();
                if (tiles.getOrDefault(new Pos(pos.x + 2, pos.y), Boolean.FALSE)) {
                    adjacentBlackTiles++;
                }
                if (tiles.getOrDefault(new Pos(pos.x + 1, pos.y + 1), Boolean.FALSE)) {
                    adjacentBlackTiles++;

                }
                if (tiles.getOrDefault(new Pos(pos.x - 1, pos.y + 1), Boolean.FALSE)) {
                    adjacentBlackTiles++;

                }
                if (tiles.getOrDefault(new Pos(pos.x - 2, pos.y), Boolean.FALSE)) {
                    adjacentBlackTiles++;

                }
                if (tiles.getOrDefault(new Pos(pos.x - 1, pos.y - 1), Boolean.FALSE)) {
                    adjacentBlackTiles++;

                }
                if (tiles.getOrDefault(new Pos(pos.x + 1, pos.y - 1), Boolean.FALSE)) {
                    adjacentBlackTiles++;
                }

                if (e.getValue()) {
                    if (adjacentBlackTiles == 0 || adjacentBlackTiles > 2) {
                        nextTiles.put(pos, Boolean.FALSE);
                    } else {
                        nextTiles.put(pos, Boolean.TRUE);
                    }
                } else {
                    if (adjacentBlackTiles == 2) {
                        nextTiles.put(pos, Boolean.TRUE);
                    } else {
                        nextTiles.put(pos, Boolean.FALSE);
                    }
                }
            }
            tiles = nextTiles;
        }
        System.out.println(tiles.values().stream().filter(v -> v).count());
    }

    private static void a(String input) {
        List<List<Dir>> lines = input.lines().map(Day24::parse).collect(Collectors.toList());
        Map<Pos, Boolean> tiles = new HashMap<>();
        for (List<Dir> line : lines) {
            Pos pos = new Pos(0, 0);
            for (Dir dir : line) {
                switch (dir) {
                case e:
                    pos = new Pos(pos.x + 2, pos.y);
                    break;
                case se:
                    pos = new Pos(pos.x + 1, pos.y + 1);
                    break;
                case sw:
                    pos = new Pos(pos.x - 1, pos.y + 1);
                    break;
                case w:
                    pos = new Pos(pos.x - 2, pos.y);
                    break;
                case nw:
                    pos = new Pos(pos.x - 1, pos.y - 1);
                    break;
                case ne:
                    pos = new Pos(pos.x + 1, pos.y - 1);
                    break;
                }
            }
            tiles.put(pos, !tiles.getOrDefault(pos, Boolean.FALSE));
        }

        System.out.println(tiles.values().stream().filter(v -> v).count());
    }

    private static List<Dir> parse(String input) {
        List<Dir> moves = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (ch == 'e') {
                moves.add(Dir.e);
            } else if (ch == 'w') {
                moves.add(Dir.w);
            } else if (ch == 's') {
                ch = input.charAt(++i);
                if (ch == 'e') {
                    moves.add(Dir.se);
                } else if (ch == 'w') {
                    moves.add(Dir.sw);
                }
            } else if (ch == 'n') {
                ch = input.charAt(++i);
                if (ch == 'e') {
                    moves.add(Dir.ne);
                } else if (ch == 'w') {
                    moves.add(Dir.nw);
                }
            }
        }
        return moves;
    }

    private enum Dir {e, se, sw, w, nw, ne}

    private static final String TEST_INPUT = "sesenwnenenewseeswwswswwnenewsewsw\n"
                                             + "neeenesenwnwwswnenewnwwsewnenwseswesw\n"
                                             + "seswneswswsenwwnwse\n"
                                             + "nwnwneseeswswnenewneswwnewseswneseene\n"
                                             + "swweswneswnenwsewnwneneseenw\n"
                                             + "eesenwseswswnenwswnwnwsewwnwsene\n"
                                             + "sewnenenenesenwsewnenwwwse\n"
                                             + "wenwwweseeeweswwwnwwe\n"
                                             + "wsweesenenewnwwnwsenewsenwwsesesenwne\n"
                                             + "neeswseenwwswnwswswnw\n"
                                             + "nenwswwsewswnenenewsenwsenwnesesenew\n"
                                             + "enewnwewneswsewnwswenweswnenwsenwsw\n"
                                             + "sweneswneswneneenwnewenewwneswswnese\n"
                                             + "swwesenesewenwneswnwwneseswwne\n"
                                             + "enesenwswwswneneswsenwnewswseenwsese\n"
                                             + "wnwnesenesenenwwnenwsewesewsesesew\n"
                                             + "nenewswnwewswnenesenwnesewesw\n"
                                             + "eneswnwswnwsenenwnwnwwseeswneewsenese\n"
                                             + "neswnwewnwnwseenwseesewsenwsweewe\n"
                                             + "wseweeenwnesenwwwswnew";

    private static final String INPUT = "nenwwwnwsenwneswnwnwnwnwnw\n"
                                        + "senwwneeneeneneneenenesweneswenenene\n"
                                        + "newnwnwnwnwnwnwesenwenenwenwnwnwwnwsw\n"
                                        + "wswnesewwwnwwnewswswnesw\n"
                                        + "wnwwwwswwwwswwswswwswnesweww\n"
                                        + "esenwneswseeseseseneseeweseseseseswse\n"
                                        + "neseesweenweeeewwswee\n"
                                        + "senwnesewnwwsenenwsewwewswwnwwswew\n"
                                        + "nwesesewswnwnwsenw\n"
                                        + "nwnenewnwnwwsenwnwswnwwnww\n"
                                        + "eeseeeeweneeeseneweneeeeeee\n"
                                        + "nwweeeeeeeswnwneneneeesenenese\n"
                                        + "wewwwwwwwwswnwnewswwwwseww\n"
                                        + "sweneenwswswneewswnwnwneeneneswnew\n"
                                        + "swsenewwsenesewseneswsenwseneswsesenee\n"
                                        + "nenwnenwneswsenenenenenewnwne\n"
                                        + "wwwwwwwwwew\n"
                                        + "nenenenwnesenwnenesewswnenenenwne\n"
                                        + "seeswnwsewwnweswneswswseseseseseswsw\n"
                                        + "swswwenwnweswwseneneewsenwwwe\n"
                                        + "eeneneenewneeewneeseneneeeneene\n"
                                        + "nenweenwenenenenesenesenee\n"
                                        + "eswweeswsenenwnwnwwseswwnwnwneenene\n"
                                        + "nwswnenwnwneenenwnwneweneswneneenwnene\n"
                                        + "newneneneeneneneneseenenenenewnwnewne\n"
                                        + "swwwswwswwwwwwswneww\n"
                                        + "swnwsenwsewnwnwneeswneeswneneswwnesene\n"
                                        + "neneneenenwneeswswnenenenenene\n"
                                        + "wnwswnwsenwnenwnwwnwewnwwnwwnwnwnww\n"
                                        + "seswnwwesenwwnwneswwnwweenwnwenw\n"
                                        + "wseseseseseseseseseewesenwseese\n"
                                        + "seesesesesenwesesweneseweeesesesw\n"
                                        + "ewnewnesenwswnesenwwsenenwenenenenew\n"
                                        + "eeenenenenenwneneneswneneneenee\n"
                                        + "seeeseeneeeseseseseseeeswe\n"
                                        + "seswseseswneswseswswswswswseswswswse\n"
                                        + "newwwwwswwswsenenewwwsenwswwww\n"
                                        + "nenenenwnenwswenenenenenene\n"
                                        + "nwnwnwnwnwnwnwsenwwwnwnwnwnwnwnwnwnesw\n"
                                        + "seenewnwnenesewnenesweeneeneneesw\n"
                                        + "nenewswenwnwnwwnewnwseneneneswenwswsene\n"
                                        + "nwseesenwnenewnwswswsesenesewsesesesw\n"
                                        + "sewseseeseseswseswse\n"
                                        + "wswwswswsweswswswswsenwswneneseswswse\n"
                                        + "eeeweneseeeeeeneneeweeneee\n"
                                        + "enesweenwweneneeeseeeeneenewe\n"
                                        + "wwenwnenwnwswsenwnenwwwnwweswww\n"
                                        + "nenwenwnwnwnwnwneswnwnwswnwnwnwsenwnwnw\n"
                                        + "nenwewneeneseswswesw\n"
                                        + "nwswenwseswswswswsewswswswseseseneswsese\n"
                                        + "nwswsesesenewsweswsweseswnesew\n"
                                        + "nwnwnwneneenenwnenenewnenwsenwnenenwnew\n"
                                        + "nwewwwswswswwwsenwwesweswswswnew\n"
                                        + "sweseneswswswseswseswseswseswnwswswnwsw\n"
                                        + "sweswenewneneneneneseneneeenenwnene\n"
                                        + "swswswswswswswswnwswswswswswswswseeswnwse\n"
                                        + "swseswenwseseseseswswsesewswseswswsese\n"
                                        + "enwwwwwseenwewwwnwwwnwwwww\n"
                                        + "wwswwwwwnwwwwwnenw\n"
                                        + "eeswenwseeesenweeneeswseseeeese\n"
                                        + "sweenwwneneenene\n"
                                        + "enweswseeseeeeeeenweeeseee\n"
                                        + "wswwwswsweswswswswnewwwsw\n"
                                        + "nenwswnewnenwnwnwsewnenesenenenenenenw\n"
                                        + "swwneweseswswswswswwswswneswswswnesw\n"
                                        + "nenwnenwnwnwnenwnwsenenwnwnwnwnenwnw\n"
                                        + "wnenenewneenenenwseneneneseneswwe\n"
                                        + "wseenwnenwneswseneneenwneswnenwswnew\n"
                                        + "enwwenenenesewsweneswseswsewnenewsw\n"
                                        + "eweeeeeseneeseswsesenwswsenwweee\n"
                                        + "seseeeenwseeeseenwseeeeeeee\n"
                                        + "nwnwneneenenenenwnwnwnwnwnwneneswswnenw\n"
                                        + "seeeseseseswnwseseneswnwseseswnenwsesee\n"
                                        + "swswswswswswswswswswswswseewswswswswne\n"
                                        + "wswwwswwswnwwsew\n"
                                        + "wwnwwnewwwwswsewwwwwwnwseww\n"
                                        + "enwnwswewneneseswnenenwswnwnwenwsenenene\n"
                                        + "seseesesewneswnwsewswenwseneswwsesee\n"
                                        + "swseswswswsenwswswswswswseswseswnw\n"
                                        + "seseseswseeseseeseswnwnewseeesesese\n"
                                        + "nwnenenwnewsewnwswenenwenenene\n"
                                        + "ewwsesenwneneeesweeeneneeneenenew\n"
                                        + "swswwneeneswswswswnwswsw\n"
                                        + "neswswwwswnewwseneswnewwnwweswsew\n"
                                        + "nwnenewwswnwwwwwesewweswwwsesw\n"
                                        + "enweewwseeneeseneseenweseneee\n"
                                        + "weswneeneseneenwnwswweeeneeswwe\n"
                                        + "enweneeeseseeseww\n"
                                        + "nenwswnenwnwnwswnwnwenwnenwnwsenenwnenene\n"
                                        + "wweswseesenwsesenwe\n"
                                        + "neswnwswnwneneenene\n"
                                        + "nwnenenewnwswenenwenwnenwnwnenenwnenw\n"
                                        + "eneneewswnwwseswneswsewnenwwswnewwse\n"
                                        + "nwsesweeneeweseseseseseenesesesee\n"
                                        + "seeseseseeeewsenwsesesenwseesesese\n"
                                        + "neneswnenenwneneeneneneswnwnenenwnenenene\n"
                                        + "seseswwsesenesesesesesesesesesesenesesese\n"
                                        + "seneswesenesewneweseseneseseweeesee\n"
                                        + "eeswswswseesenwsenwnweeeneeneesesw\n"
                                        + "eeeseeseseesesenwseseseseeseenwnw\n"
                                        + "swnwswswenwswsewneswseseswwswseswnwe\n"
                                        + "nwneeenenewsenene\n"
                                        + "nesesenwneseswseswswwnwseswswseswseseswsw\n"
                                        + "sesenesewnesesesesesewseseseseesesesw\n"
                                        + "wnesewsewseseswnwswswsenwswwneswnenewne\n"
                                        + "eeseeeeeeseesweeeeeenwe\n"
                                        + "neswsewnwneswwsewneeseswsee\n"
                                        + "neswseseenenwswneneenwenweneneswew\n"
                                        + "eeeeswseeeeeenweeeeewene\n"
                                        + "nwwnwnwnwnwnwnwnwnwnenwswnwnwnw\n"
                                        + "weeeesewseswsenwnwnwneswnwwnwnwne\n"
                                        + "weneswswewewswseswswswswwnenwswnesw\n"
                                        + "nwewnwenwnwnenwnwnwswnenenwnwnwnwnwnw\n"
                                        + "swswswseswneeswswswwswwswswswswswswswsw\n"
                                        + "sesewswenwsesewsesewnwnesesesenenwne\n"
                                        + "eeeeseesweenweseeeneesweee\n"
                                        + "newswsewnenwnwsewsenw\n"
                                        + "wswnewwwsewsewwwwswwwsewwnenw\n"
                                        + "seenwnwnwesenewseeswsew\n"
                                        + "nenwwwwnwnwnwwsewwnwnwwwwwsenew\n"
                                        + "eseeeeeeeeneeneeeeeeewseew\n"
                                        + "nwneweeswnwnenenwweeweswseswene\n"
                                        + "swswwwswneswswswswswseswsweswswnwswswswsw\n"
                                        + "nwswesenweeenwsenweewnweseswneee\n"
                                        + "swswseseswswnwswswswswswswseswswnwseswe\n"
                                        + "wenewnwwwewwswwweesw\n"
                                        + "wswswswwwswwnewswswneswseswwwswswsw\n"
                                        + "nwswwesewsesewnenenwswwwwwwww\n"
                                        + "eeneswnwseeeeenwweneneswnwswne\n"
                                        + "wwwswwwnwwewwwnwwwwweswww\n"
                                        + "sesenwseseseeseswnwsesewsesesenesesese\n"
                                        + "seseswnwswswswswsesweswswnwswswswseswswsw\n"
                                        + "enwseseswwseneseeneeseneseewseseew\n"
                                        + "nwsenwnwnwnewnenwwseswnwsenwww\n"
                                        + "ewneeeneweeenenweeneeeswenene\n"
                                        + "swswswswwswneeswesesewneswswnwnewswsw\n"
                                        + "sewseswnewseneswwnw\n"
                                        + "wwnwnenenwnweeneneneenwnenenesw\n"
                                        + "eseeneneneeswnweswesewsewneswwnene\n"
                                        + "nwnwwnwnwnwnwwnwenwnwswnwnwswnwwnwe\n"
                                        + "nwsenwseswwewnenwnewnwnwsenesenwnwnw\n"
                                        + "newswenwseweswwswswswswswneswnwsesww\n"
                                        + "swwswswseswwswnwsweswnenewwswswswsw\n"
                                        + "neneneeswneneewneneneeneeeneswenenw\n"
                                        + "eneeneneeeseweeneene\n"
                                        + "swseswseseswwneseseswswsweswseseseswsw\n"
                                        + "eeseeeseeeneseeeeeswseee\n"
                                        + "eswnwswwswsenwseswswwswseswswneeswsenwsw\n"
                                        + "swwnwnwswwseswwswwswenewswswwwswsw\n"
                                        + "seseswseswseneseneeseswseswsesewswswsw\n"
                                        + "nenenenenenwneswnwnwene\n"
                                        + "wwwswneweswswwwseswswneswswswswwsw\n"
                                        + "wswwswneneswwswwsenwnenesewwwswse\n"
                                        + "nwewwwwwnwnwnwenwsenwnwwnweswswsw\n"
                                        + "seseeeeeeseseswsenwsenwewseseese\n"
                                        + "seenwsewseseseseeseseswseswseswsesenw\n"
                                        + "nwswewwseneneseswseswnwswsenwsenwswnw\n"
                                        + "swswswswswswneswnweswswswswnwswswswwsesw\n"
                                        + "eeseeeseseswseweeesenwseeeeese\n"
                                        + "wnwwwwwwwwwwenwnw\n"
                                        + "eseseseseswswwneneseneeeswenesesee\n"
                                        + "swneswnwnewwneeneseseneneneewnenenw\n"
                                        + "weeeeeenweeeewswneeeeeese\n"
                                        + "eeenwsweneneesweneneeeeeeee\n"
                                        + "sewwewwswnwwswswesw\n"
                                        + "newswneneneneeneeswnew\n"
                                        + "nenenwnwwseswnenwnwnenwnenwseswneewnenw\n"
                                        + "nenwnwwnwnwnwsenwnwwnwnwnwnwsenwnwnwnw\n"
                                        + "wwnwnwwswwwnwwnwnwneewnwnwwsenw\n"
                                        + "sweswswwneneswesesenwsenwwswseswsesesese\n"
                                        + "wseseswseseseseseseseesenenwseseswwnwse\n"
                                        + "wnwnwswwnwnwwnenwwnwwseenwwwwnwsew\n"
                                        + "wenwseeseseswenwnweeeswneseesese\n"
                                        + "eenenwewewneneneeswe\n"
                                        + "swswseswswswnwseeswnwswswsenwswswsenesenw\n"
                                        + "nwwnwseewnesweeeeeseeeeeeswee\n"
                                        + "neneswnenenenenenenwenwwseswnenee\n"
                                        + "swswswewswwwswnewsewswswsw\n"
                                        + "nwnwwnwnwnwnweeswsenwnwnwnw\n"
                                        + "swseseswneseswseswseseseseswseswsesw\n"
                                        + "swsewsenwswswseseswswswswsweseswswswnw\n"
                                        + "sesenwnwnwnenwnwnwnwnwnwnwnwnwnwnwnwnww\n"
                                        + "swnesweswswswswswnwwwswswswswwswwsesw\n"
                                        + "seeeeeeseeseeesweeswnweseswnwenw\n"
                                        + "neesweeeweeewneeeseneeee\n"
                                        + "nwwsenenwsenenwwnwnenwwnwnwnwsenenwnenene\n"
                                        + "seseeseeseeseeneenwseweneswwnwse\n"
                                        + "sewseseseseseesesesewseseseswseenesese\n"
                                        + "wnwnesenwwwnenwwneswswwnenwwwwwsw\n"
                                        + "nwneenenenenenwneseneseneneneswnenwnene\n"
                                        + "wnenwseewwwwwwnenwswwwswnwwnese\n"
                                        + "eneewwwsewwnenwnwwnewswnwswsese\n"
                                        + "sweseswseeneswnenwneseesewewnwwse\n"
                                        + "eeneeenewweeseseseseseeseseseeee\n"
                                        + "wnwwnwnwnwwwwsewnwwenwnwnwwwnwne\n"
                                        + "nwnwnwnwnwnwnwnesewnwnwnwnwnwnwewnwnwnwsw\n"
                                        + "nenwwneeenenwnwnenwnwwswnenenesenenwne\n"
                                        + "ewneeneeseweenweeeseeenweenesw\n"
                                        + "enwweeseesweenwnesweweeeee\n"
                                        + "seeeseseswseseswswsesewseweswsenwswsene\n"
                                        + "eneeweeeeeeseswseeeeswenwee\n"
                                        + "swswnwseswswsenwnwnwewwswswswwnwswese\n"
                                        + "esenwnwseeeswswenwnwseneenwsenwswsene\n"
                                        + "nwnwwnwnwnwnwnwsenwnwww\n"
                                        + "swsesenwseseneswswesesesesenwseseseswsese\n"
                                        + "eseswswseenwseseseseseseswwse\n"
                                        + "wneenwnwnwnwnwnenenwswswnenwnesenenwsw\n"
                                        + "seseeseneeseeseeeswneeswsee\n"
                                        + "wswwswnewswnwweneseewwwnwwwww\n"
                                        + "nwwwwwwnewwswwwwweeswnwwsw\n"
                                        + "sewwwewwseewnewswwwnwnewnww\n"
                                        + "weseseseneswesenwsesenwswseseswswsenene\n"
                                        + "nwewnwswnwnenwnwwwnwwweewseswnww\n"
                                        + "nenenwswwnwswnwnwsenwwnenwwenwnwsew\n"
                                        + "wseeeseseseeeneseseseswseenwseee\n"
                                        + "sesesesesesewseseeseseseseesesesenwse\n"
                                        + "swseeseeeseeeeseswseweenwnwneee\n"
                                        + "nweswnenwswnwnwweneswswnwsenwnweswnwnenw\n"
                                        + "newwsewnwnwnwnwnw\n"
                                        + "wwwswwswwwswwwswwnweswwswwe\n"
                                        + "wswneswneseseseswsesesenwseseeswnwswsesesw\n"
                                        + "nwnweneseesesweeneneeneswnewwnew\n"
                                        + "enwnwwnwnwnwnwnwnwnwnwnenwwnwsesenenwnw\n"
                                        + "nwwnwwnwwnwnewnwnwsenwnwwesenwnwswnw\n"
                                        + "neenwewewesweneweeeseswnwee\n"
                                        + "nwneseseseesweeeeeseweesesesee\n"
                                        + "nwswenwswnwnwnwnwnwnesenenwswnenwsenwnw\n"
                                        + "wwwwwswsewenwwwswnesenwwnwwnw\n"
                                        + "wneswnwnwwwwwnewsewwwwww\n"
                                        + "swnenenewswwsenwseseswnwseseswseseeseswe\n"
                                        + "wewwwnwwseewwnwwnenwnwwwww\n"
                                        + "swwwwwwwwwwnewwwweewww\n"
                                        + "eeeneeewseweseeeeeeeewe\n"
                                        + "wnwnwnwnenenenenenenwnesenwnw\n"
                                        + "nenwswwnwsweneeseweneesw\n"
                                        + "nenenewnewneeneneeneeneneneene\n"
                                        + "seseseeseseseenwseenwseseeeseeesww\n"
                                        + "nenwwsenwsewsewnwnwnwnwwnwnw\n"
                                        + "seneeseseseseweeeeweseswse\n"
                                        + "wswswswsweseseswsenwsese\n"
                                        + "wnewnweenewsewswnwsenwwswswnwnwnese\n"
                                        + "wwsenenwnewwwwwwswwse\n"
                                        + "nenwswswswsewseseenwnwsweswswswsenwe\n"
                                        + "wnwseeswseenwwnenweseswswswswnesww\n"
                                        + "nenewneneeneseswnenesewnwnesenenenenwnwnw\n"
                                        + "wenwnenesenwnenewnenenewnenesenenenwne\n"
                                        + "swswnwswswswseswswswswswswswswswnwseswe\n"
                                        + "sesesesesesesesesesenwseeseswsewsenenese\n"
                                        + "wnwwweewwwewwswwwwwwwne\n"
                                        + "weenwseeeeweswwneesewwwene\n"
                                        + "wwswwwwwwwwwnwwwnwwnew\n"
                                        + "swswseswswswnewswsewnenewswswswwwwsww\n"
                                        + "wewwwnesenwswenwwswwwenweswnewsw\n"
                                        + "eneneseneswnwneseneswneswenenenwneswwnwne\n"
                                        + "eneseeneneneeweneneneneneneene\n"
                                        + "swsewnwewseeneseeswseweenwne\n"
                                        + "sesenwwseeswswseseseswsweswseswswswswsw\n"
                                        + "neneeneneneneneneneneneswneswnwneneneene\n"
                                        + "seseesesenwsesesesesesesesese\n"
                                        + "neneenwnenwnwnwnewnwenenwnenwseneneswnw\n"
                                        + "neeweeewneeeeeeeeeseeesee\n"
                                        + "nwwwswswnwewnwnwwwnwewnwe\n"
                                        + "wwwwwnewwswwwwse\n"
                                        + "seswneneenewwnenenenenwnenesenenesewne\n"
                                        + "wsesenwnwswneeswswseseswsweseenwswnwsw\n"
                                        + "wseseneneswnenenwwnenwnwnwnewnwnwnwsenw\n"
                                        + "seseseseswseseseseneswsesesesesenwsesese\n"
                                        + "eeswswswnwseswsewnwswnwswnwswe\n"
                                        + "wswswesewswweswswswwnwnwnesw\n"
                                        + "neeneeneeewnewseeneseeneneeeee\n"
                                        + "seesenwnesenwseseseseneswsesesenwsesesesw\n"
                                        + "neswswwsenwseseseseswseseneseswnwwswnese\n"
                                        + "nenesenwneneenenenewnwnenenwnwnenenenew\n"
                                        + "wwswswwswnwswwswwswwwswseswsw\n"
                                        + "wnwsesenewsewwswswnwsweswswenewswnesw\n"
                                        + "eeeseswneesweenweenw\n"
                                        + "senwswenesewnesenwswswweswswswseswnee\n"
                                        + "wnwwwsewnwnwwswnenww\n"
                                        + "neneneswswnenenenenweswneswneenenenenwne\n"
                                        + "seseseseseenwsesee\n"
                                        + "nwnwneneswenwwwsenewnesenewseswwse\n"
                                        + "neswseweewenwewwwnenwsenesesesee\n"
                                        + "wsewnenewewwwswswnwswwwewnwwwsw\n"
                                        + "nweeeseweswseseewneeeseeenenwe\n"
                                        + "wswnwenwenwwnenwswseswenwwnwnwesenwne\n"
                                        + "seneswswneseseseswwseseseseseseseseseswse\n"
                                        + "ewnweseseseseneeesewneewsweeneese\n"
                                        + "seneneneneneneewnenewsenenewwsesew\n"
                                        + "wwnwswwswsesweswwwwwswswswswnesw\n"
                                        + "nwswnwswnwnwnwenenwnwnenwwnwnwenwnwnw\n"
                                        + "seeneseeeeneneeneneneweneeenwnwe\n"
                                        + "nenesewnwnwnwseswsenwnwsweswwwsenenwne\n"
                                        + "seseseeweeeseseeeneseneseseesesesw\n"
                                        + "nwneeneneneeseneeeneneenenewnesenee\n"
                                        + "sesenwneswseswswswenenwswnwswswswswswsw\n"
                                        + "nwwwnwnwsenwwwnwnwwnwwnwnwewnwsw\n"
                                        + "nenenwseneswnenesenenenenwnenenenenwnwwne\n"
                                        + "swswswswwweswwwswneswswwwswswswsw\n"
                                        + "swswwswswwswnwswneseswswnewswwseseswswsw\n"
                                        + "nwsenwswsweseseseseseseenwseswswseswsesesw\n"
                                        + "ewnwswswneneswneseneeswswwnwseneswne\n"
                                        + "wwwwwsewwwwsewneewnwwwww\n"
                                        + "sewneswseseswnweeseeeeeseeneesewse\n"
                                        + "seswneswswwswswseneswse\n"
                                        + "nwneswnwnwnwnwnwnwnwnwenenwne\n"
                                        + "wwewwwwwwwnwweseswswwwwnwsw\n"
                                        + "eeswseneenweeenwnweeseeeenenwsw\n"
                                        + "swswwwnwwswnwwwewweswewnwnwsewsw\n"
                                        + "nwwsenwnwnwwnwwwnewesesw\n"
                                        + "wwwnewnewwwnwwsenwsewwwwwww\n"
                                        + "swsewswswseswsewseeneswseneswseswswswsw\n"
                                        + "nwnenwnwnwnenwsenwnwenwnwnwswswnwnwwnwnwnw\n"
                                        + "sweeswnewwwnwnweseneswsewnesewswnw\n"
                                        + "swneseseeswseneswnwsw\n"
                                        + "swweswnewswswwwswseneswnwwswnwsesesw\n"
                                        + "eenwnwnenenewswswnwneenwnenwnwswnwne\n"
                                        + "eseseseseeseswseeseseseneseeseseenwew\n"
                                        + "swsweeneswswswnwwswswswwswwnwswsweswsw\n"
                                        + "swsweswseswseswnwsesenewnenwswneswswswsw\n"
                                        + "seswsesenweseseeeseenwseee\n"
                                        + "seseeseeneeseseswsenweseewsesesesee\n"
                                        + "swnwnwnewnesenenwnenesenwnwnwwneneenwnw\n"
                                        + "seswswsesenwswseseseneswseseweenwswsw\n"
                                        + "wnwnwnwnwnwnwwwnwwnenwnwnwsw\n"
                                        + "nwnwswnesenwneewnwnwnwnweneseneeww\n"
                                        + "seswwswneswswseswswswneseswswswswswswsw\n"
                                        + "newwnwsesenwnenenwnenenwwenwsenenwswnwe\n"
                                        + "eenewneseesweenwneesenwwsenweese\n"
                                        + "seewswwwnwwseswswwnwswenenewnew\n"
                                        + "seseseseseeewnenwee\n"
                                        + "eeeeswwneneweneewenenweewe\n"
                                        + "wsenenwswenenwesenwneswsenesenwneswnw\n"
                                        + "nwnenenenenenenenwneswnwnenewswnwenee\n"
                                        + "newswseseenweesenweswsewwseeenenw\n"
                                        + "swsenenwnwnesewwwnwwwnwwsewnewnww\n"
                                        + "neneneneneneneenenwnenwweneswnenewne\n"
                                        + "swswseenwwsweswswswswswswewenwswwsw\n"
                                        + "neeeeneneenewnewneneneseeenene\n"
                                        + "senwnesenwseswswsesesesenesewwswseseswne\n"
                                        + "wwsenewswewnwwwwwwnwwwswswswww\n"
                                        + "eswwwwnenweweswewwwswnwnwsww\n"
                                        + "nenwnwwnwnwsenwnenwnwsenwwnwnwnwnwnwesw\n"
                                        + "eneseeneeneeseeneneeewnewneee\n"
                                        + "eenenwnwneneneeswnweswseeenwneeesw\n"
                                        + "senwseswswswnenwnwswwseesweeseswswsw\n"
                                        + "swwnewswsenwwwwwwwnwsenwnwwnewnew\n"
                                        + "nwnwnwnwnwnwseswnwnwnwnwnenwnenwnwnwnwnw\n"
                                        + "swnwwswsenwnwwnwnwwnwenenwnwsenwnenwne\n"
                                        + "nenenwnenewewnenwneeeeswswneese\n"
                                        + "eeneeneeneneneeesweenwneneeswee\n"
                                        + "swseswswnwseswseseseswswseswswswswneswsw\n"
                                        + "wwewwwenwewnewwswwwswnwww\n"
                                        + "wwnenwwsenwnwnwwwnwwwwwenwnwnww\n"
                                        + "nwnenenenwnwneenenwnenwwsenwwnwnwnwnene\n"
                                        + "swswsweswwwwswwwwwwsw\n"
                                        + "eeneeeneswenenweenenene\n"
                                        + "eseeswnwswneeeseenwsewseeeenwnwe\n"
                                        + "seseseseseswsesesenesesesesewwwseesee\n"
                                        + "esweeseseseseeenwsesesenweeeswe\n"
                                        + "wwnewwenwnwsewswwnwnwwnwwwnww\n"
                                        + "nenenesenenenenenenewnwnenewenenenene\n"
                                        + "nweeeseeseweeenwnwsweeeeee\n"
                                        + "senwnwnwnwwswnwenwnwwnwnwnwenwneswnw\n"
                                        + "nwnwswnenenwnwsewsenwswsewsewnwnenenwnene\n"
                                        + "wneewwwsenwewnwnwesewewnwwwsw\n"
                                        + "swswneswswswwenwwseswseswnwswswseswswse\n"
                                        + "swwswswswswswneswseswswswwswswneswsww\n"
                                        + "wwseswswswswneswswswswswswswswswnwsww\n"
                                        + "newwwsenewswswswwsw\n"
                                        + "neneenenweneneneeneneenwneneswseswne\n"
                                        + "eeesesesesesewseseenew\n"
                                        + "weweneswsweeenenwewneeswseenene\n"
                                        + "swnewneneseeeeweneeenwnenwweswsw\n"
                                        + "seswsenwseesesenwseswsesesese\n"
                                        + "eeweeseeeeeeneeee\n"
                                        + "nwnwnesenenenenewnenwnwnwwene\n"
                                        + "neseswswseeswseswswswseseneswnwseswsesw\n"
                                        + "nwnwenwnwwwwnweweewwsewswswwnwe\n"
                                        + "nesesenwewenwseseeseswseswseneeese\n"
                                        + "ewnwwsenewenwwweewwnwwnwwnw\n"
                                        + "nesewnwnwnwswwwswnwwnwsenwnwwnwnenewnw\n"
                                        + "sewsesesewswseseneesesesesese\n"
                                        + "newnwnwsesenenenwsenenwwwnwsenwnesenwnwse\n"
                                        + "esesenwesesenweseseseseseswswseesesese\n"
                                        + "sewwwswswneswwnewwsweewwwwnwsww\n"
                                        + "swsewwwswwwswnwwnwwwwwewwww\n"
                                        + "nenwswneneneneenwneewnesesenenenenenee\n"
                                        + "nwnwnwnwneswsenwnwnwnwnwnwnwwnwenwnwnwnwnw\n"
                                        + "eeeeesweeeeewseeeneneeswe\n"
                                        + "nwnwnwnesesewnwnwnwwnwsenwwnwwnwnwnwne\n"
                                        + "nenwneeneneewneeneeneneneswneeee\n"
                                        + "seneswneneeswswswseseswseseseenwwswsesw\n"
                                        + "swnesweswseswsenwsenwewswsenwnesenwse\n"
                                        + "seseseeswsesesesewsesesenesesesenwsenesese\n"
                                        + "wnwnenwnwwnwwwwnwseewwnwwewsew\n"
                                        + "nwnwwnwnwnwwnwenwnwwnwnwnwnwnww\n"
                                        + "sewsenwnenwnewnwnwnenwnwnwnwwnenwnwenw\n"
                                        + "nwewwnesewwwwwsenwwwnwnwwwwsww\n"
                                        + "swnwswswwswswsesewswswwwewnwswswsw\n"
                                        + "swseneseseseswseswseswwswseswsesw\n"
                                        + "swwnwwswnwnenwnwnwewsewewwnwwnw\n"
                                        + "nwnwwnwnwwwwnwsesewnwnwnwnw\n"
                                        + "wwnwwwenwnwseswwenwnwnwnwenwnwnwnw\n"
                                        + "nwnwnenwnwwnwnwneesenwwnenenenwseswne\n"
                                        + "swneenwswwsenwseswswswsenweseswneswseswse\n"
                                        + "swsweswsewsweneswswswsenwseswsese\n"
                                        + "eseeswnweeeseeeseswneesesesesesee\n"
                                        + "nweswwewswnwewnwwnewsenwswwwwnw\n"
                                        + "nwnenenwswneeeswewseneswneswseeenw\n"
                                        + "wnwwswwnwnwswenenwwenwwnwnw\n"
                                        + "eeeeneeeswnweeswenweeneeene\n"
                                        + "swseeeweswnwneewwswnwswswesenwswnw\n"
                                        + "seseswsesweseswsewswesesenwseseswsesw\n"
                                        + "wnwwwnwnwwsenwnwnwnwnwnwwwwnenw\n"
                                        + "wswnwwwwswwewswseswwwwneneseswww\n"
                                        + "neseeeneenenwnweswene\n"
                                        + "wwwwwwwwnewwwwswnwesewwe\n"
                                        + "swnesewenesesewneweswnwswenw\n"
                                        + "wsweeenwseswenwsenenweenwsee\n"
                                        + "neneenwnwnwsenesenenenenenwnewsenwseswnesw\n"
                                        + "wnwnwnwswnenenenwneneneneneenwsenenenenw\n"
                                        + "swswswswswseneswswswswswwswswswswswnesw\n"
                                        + "sewnenwneneneenenenwnwnwwnwnenenenenwne\n"
                                        + "swwnwswewswswnwswneswswwswswswwsesw\n"
                                        + "wseenwseesewseesesesene\n"
                                        + "wsenweweeneeeneeneewseeeenee\n"
                                        + "wswswwwswswwswswsweswwswnewswewsw\n"
                                        + "wnewwwwnwseweswwswwwswwswwww\n"
                                        + "wnwswnwseswswwswwwewewwswnew\n"
                                        + "senweeeeeeewsweneeneeeeee\n"
                                        + "sweeseeseeneswenwenenwnwenesenenwsw\n"
                                        + "seenweseseseseseseseseseseesenewneswsese\n"
                                        + "ewnweneeseeneeeeeneeeneenee\n"
                                        + "nenwwsenwenwswnesewnwnenenesewswnene\n"
                                        + "sewseswswswseseswswsenwseseswsesesesene\n"
                                        + "nesenenwswswseswsenwneswswswseewwsew\n"
                                        + "wnwnwnwnwwseeneneeswnewenwnwnwsenwne\n"
                                        + "neneeneneswwneeenwneenee\n"
                                        + "esenwenewneeweneneesesweesewe\n"
                                        + "nenwnwnwnenenwnwnwnesenwnewnw\n"
                                        + "nwenwswenwnwnwnwswenwsenwnwnwnwwnenwwnw\n"
                                        + "wnenesenenenwnesenwnwnenenesenene\n"
                                        + "eseeeeseewenenewnwsweeneenwee\n"
                                        + "swswnewneseswseswwseneneneneswwswneswne\n"
                                        + "enwenwwwswnwsenewe\n"
                                        + "enwwswwseswwww\n"
                                        + "nwnwswswnenwnenwnenwnwnwsenenenenenenenenwe\n"
                                        + "wswwneswsenwnwnewwsweew\n"
                                        + "seswseseseseeesesenweesesewesesenwe\n"
                                        + "newswswswnenenwnwenenenwsenenwswnwwe\n"
                                        + "nwneneeeneneneeswwenenenweeesesene\n"
                                        + "wneneneseeneneeswwneneneswnenenenenene\n"
                                        + "nwneeneneewneneneneneneneneswnenene\n"
                                        + "nwnwneswnenwenewenenwnenewnwnesenene\n"
                                        + "nwnenwenwswsenesesenwseneenwnwnwwnwsw\n"
                                        + "swneweswswwwwswwwweseseneeww\n"
                                        + "nwwnenesesenenenenenwnenwnewneneenene\n"
                                        + "sesenwswseseseseseswenwseswswseswsesese\n"
                                        + "newneeneneeweseneewneneneweeee\n"
                                        + "newnwwswswwwswswwewesewwwwwnw\n"
                                        + "nenwenenenewnwnenwnenwsenenenewnwnenwne\n"
                                        + "nwnenwsenwnenwnwnwnwnenwnenenesenewnwnene\n"
                                        + "wnweswnwneenwwwsenwnwsenwenwsenesesw\n"
                                        + "enesenesenwwsweswneneswnenesenewswswnww\n"
                                        + "nwnenwnesenwnenenenwnenenwseswnwnwnewnwnw\n"
                                        + "nwswseneseseseswseseswswsesewnwswseseesw\n"
                                        + "nwnwnwnwnwwsenwnwnwsenesesewnw\n"
                                        + "neneneeneneeneneneenewnenenesenenenesw\n"
                                        + "wsenenenwnenwnwnwwnenwnwsenwneenwnenww\n"
                                        + "swwsenewneswseswnewww\n"
                                        + "wswnwswswseswswswsweseneneseeswswnesew\n"
                                        + "seneenenewswneneneenenenenenenenenene\n"
                                        + "wnwnwnwsewesenwwwnewwwesww\n"
                                        + "newswsewweenwewwnwswneeenwnwswnw\n"
                                        + "neneneeswneneneneeeenene\n"
                                        + "nwswwsenwwnwnwnweenwnwwewnwnwswnwne\n"
                                        + "nwnwnenwneeeenesewnwswswsenwnwnwwnw\n"
                                        + "ewnwneswseneeneenweneseesenenenww\n"
                                        + "nwnenenwnwenwnwnesewnenewnwswneseenene\n"
                                        + "neeweneneeneeneenenewneeeneneswse\n"
                                        + "nwneswnwseseswswesewnwwswswswneesewe\n"
                                        + "swswneswswsenwwswswwswswnwswswswswese\n"
                                        + "swswneswswenewsweneswwwswnwse\n"
                                        + "eeeeseeeeeeseewenese\n"
                                        + "wewwwseswwwswwwwnwewnwwwww\n"
                                        + "senwnwnwnwnwnenwwnenwswnwnwnenwnwnenwnwnw\n"
                                        + "wwwsenwwswwweweswneswnwswwsww\n"
                                        + "newwwwewwwwwwwewwwwwwse\n"
                                        + "eeneeeeeweneneeeeeneweee\n"
                                        + "seseswneseswnewsewseseseneseneseseswsew\n"
                                        + "seeswswseswswswswseswswswseswswwswnwse\n"
                                        + "sesewwseseseseseseneseseseesesesenwsese\n"
                                        + "swewnwnenewswwewseswesenenwnwwnwwnw\n"
                                        + "swenwseswwwswswwswswwwwenwnewsw\n"
                                        + "swsesenwesesesenesesenesenwsew\n"
                                        + "swwswswwnwswwwswewswwnwswewwwe\n"
                                        + "wswseeneswesewneswswwsewenew\n"
                                        + "swwsewnwwewwswwwnwwswswsw\n"
                                        + "enweeeseneeeeneeeeee\n"
                                        + "sweeeseeswenwenweeeeeeeeeee\n"
                                        + "sewnwnwswswseeswsweswswsweswswswswsenesw\n"
                                        + "swwswwswswwswwneswwswswsw\n"
                                        + "seswnewneenesenwnesenwweneeneswew\n"
                                        + "wnwesenwsenenweseseseswsesenesewswsene\n"
                                        + "swwwwswwsewswwwneswwww\n"
                                        + "wswwwswwswswewwwswwwewswwsw\n"
                                        + "esewswswswswswswnewswswneswswnwswseswsw\n"
                                        + "nwsenwnwnesenenenenewnwnwnenenwnenenenene\n"
                                        + "swneneeneeseswnwneswwwneneenenenenenw\n"
                                        + "swswswswwnwsweswswswswswesenwswswnesw\n"
                                        + "eswswswwwswswnwweswswswwnweswswswnw\n"
                                        + "seneneneeeneeneewswneeeenenenee\n"
                                        + "swswswswsesweswswswnewswswswswswswnesw\n"
                                        + "nwswnwewswwswwnwwneweenwnewnwe\n"
                                        + "newseneswewnwseswnesesenwswneswswswse\n"
                                        + "swwwwnwwewenenwwseww\n"
                                        + "swsewneswseseseseseswnwsesesewenenwese\n"
                                        + "sewsesweewseneeeseeseeeweee\n"
                                        + "eeeewneseeeeeeswneeesweswne\n"
                                        + "nenenwsewsenesesenwwwsenenwnwseneswnenww\n"
                                        + "eeseesesesesewsesesesese\n"
                                        + "newneenenwnesweseneneenenene\n"
                                        + "swswnwweswneseseswnwswseswseseswswswse\n"
                                        + "nwswwnwsenweseswswseswswswsw\n"
                                        + "nwwnwnwwnwwnwnesewnesewsenewnwwswnw\n"
                                        + "swnenwwnenenenenweseswsenenwenewnwswe\n"
                                        + "nwnewnwseseswsewwneswneseseeswsesesenwse\n"
                                        + "nwwnwwswnwnenwnwnwnwwsenwenwnwnwnwsw\n"
                                        + "seeseeesenwweeeneweseee\n"
                                        + "nwwwwwwsewwwswnewwwwwwww\n"
                                        + "nwnwnwwwnwwwsewwneswnweewwsww\n"
                                        + "esenwnenewnenenwnenenwnenweeseesenese\n"
                                        + "wnenenenenenenwneenenenesenenenenenenew\n"
                                        + "eswneeneeeneeneswnenenwneeeeenee\n"
                                        + "wswseswsenewswwnwswwwswswwwswnesw\n"
                                        + "swswsesesenewswneseseswswwneswswsw\n"
                                        + "sesewswsenwswneenenenweswee\n"
                                        + "seswswswswswswswneseswnwewswseswswswsw\n"
                                        + "sesenwseseswswseswswsweseswswsenwsesesw\n"
                                        + "eswswswseswnwswswswswseseswswswnesewsesw\n"
                                        + "enenweeneweneneeeesweenenesene\n"
                                        + "esenenwneswsenweswwwnwnenwnwnwwnwwnw\n"
                                        + "swswesewswswswnwnwswwswswswweswswwsw\n"
                                        + "seeseseseseneenwsesewnesenesewsesew\n"
                                        + "eseswseseeswesenweenenweeseeeese\n"
                                        + "senwnwswwwwwwseseneswsewswswneswnw\n"
                                        + "esenwwwseswseseneenwe\n"
                                        + "swneswseseswseswneneswwswswswseswswswnwsw\n"
                                        + "swwwneswwswswswswneswesw\n"
                                        + "nweeeneseseeeeeenenweeenwesw\n"
                                        + "nesenwswnenwnwnwnwsenwneenenwnenenwnenw\n"
                                        + "neeeeeswneesewnenenwe\n"
                                        + "swsesesesesesesesenesewsesesese";
}
