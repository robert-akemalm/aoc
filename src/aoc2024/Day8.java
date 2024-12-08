package aoc2024;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import aoc2024.Util.Pos;

public class Day8 {
    private static void a(Input input) {
        Predicate<Pos> valid = p -> 0 <= p.x() && p.x() < input.maxX && 0 <= p.y() && p.y() < input.maxY;
        Set<Pos> antiNodes = new HashSet<>();
        for (int i = 0; i < input.antennas.size(); i++) {
            Antenna a = input.antennas.get(i);
            for (int j = i + 1; j < input.antennas.size(); j++) {
                Antenna b = input.antennas.get(j);
                if (a.frequency == b.frequency) {
                    int dx = a.pos.x() - b.pos.x();
                    int dy = a.pos.y() - b.pos.y();
                    Pos antiNode = a.pos.move(dx, dy);
                    if (valid.test(antiNode)) {
                        antiNodes.add(antiNode);
                    }
                    antiNode = b.pos.move(-dx, -dy);
                    if (valid.test(antiNode)) {
                        antiNodes.add(antiNode);
                    }
                }
            }
        }
        System.out.println(antiNodes.size());
    }

    private static void b(Input input) {
        Predicate<Pos> valid = p -> 0 <= p.x() && p.x() < input.maxX && 0 <= p.y() && p.y() < input.maxY;
        Set<Pos> antiNodes = new HashSet<>();
        for (int i = 0; i < input.antennas.size(); i++) {
            Antenna a = input.antennas.get(i);
            for (int j = i + 1; j < input.antennas.size(); j++) {
                Antenna b = input.antennas.get(j);
                if (a.frequency == b.frequency) {
                    antiNodes.addAll(List.of(a.pos, b.pos));

                    int dx = a.pos.x() - b.pos.x();
                    int dy = a.pos.y() - b.pos.y();

                    Pos antiNode = a.pos.move(dx, dy);
                    while (valid.test(antiNode)) {
                        antiNodes.add(antiNode);
                        antiNode = antiNode.move(dx, dy);
                    }

                    antiNode = a.pos.move(-dx, -dy);
                    while (valid.test(antiNode)) {
                        antiNodes.add(antiNode);
                        antiNode = antiNode.move(-dx, -dy);
                    }
                }
            }
        }
        System.out.println(antiNodes.size());
    }

    record Antenna(Pos pos, char frequency) {}

    record Input(List<Antenna> antennas, int maxX, int maxY) {
        static Input parse(String input) {
            List<Antenna> antennas = new ArrayList<>();
            List<String> lines = input.lines().toList();
            for (int y = 0; y < lines.size(); y++) {
                String line = lines.get(y);
                for (int x = 0; x < line.length(); x++) {
                    char c = line.charAt(x);
                    if (c != '.') {
                        antennas.add(new Antenna(new Pos(x, y), c));
                    }
                }
            }
            return new Input(antennas, lines.get(0).length(), lines.size());
        }
    }

    private static final String TEST_INPUT = "............\n"
                                             + "........0...\n"
                                             + ".....0......\n"
                                             + ".......0....\n"
                                             + "....0.......\n"
                                             + "......A.....\n"
                                             + "............\n"
                                             + "............\n"
                                             + "........A...\n"
                                             + ".........A..\n"
                                             + "............\n"
                                             + "............";

    private static final String INPUT = ".....................................O..V.........\n"
                                        + "..................................................\n"
                                        + "................................O.........Z.......\n"
                                        + "....W....................................V....v...\n"
                                        + "........................m................8........\n"
                                        + ".....................................n........Z..v\n"
                                        + ".............F.....3...n....5m....................\n"
                                        + "................................................V.\n"
                                        + "................3............iv....Z.............V\n"
                                        + "...........................O..n..i........p......H\n"
                                        + "......W..6..............................i.........\n"
                                        + "......................................b...........\n"
                                        + "..................................n........p......\n"
                                        + "........M.......c...........m..5......1...........\n"
                                        + "...M............................L..5..A...........\n"
                                        + "...w...........9.............F5..................q\n"
                                        + ".W.....................................q....p.....\n"
                                        + ".......W........r.......H.....LA......q...........\n"
                                        + "................4.F....................A..........\n"
                                        + "........3.......a.....F...................A..L....\n"
                                        + "....ME...............................Q..........q.\n"
                                        + ".E..................ih...................Z........\n"
                                        + "................E...H...........h.................\n"
                                        + ".........m.........X..............................\n"
                                        + "..................0......C.................h......\n"
                                        + ".M......l.................Q.h.....................\n"
                                        + "..........C..............0........................\n"
                                        + ".............lX............3.c....................\n"
                                        + "......8.X.........c....r..a......H.....9..........\n"
                                        + ".................QE.....C.........................\n"
                                        + "..R................a........Q...................7.\n"
                                        + "...........................a......................\n"
                                        + "l..........X.R............1..I..........9.........\n"
                                        + ".................0R..............b.....z......x...\n"
                                        + ".......l.....w....r..........................b....\n"
                                        + ".8..........0...................P1z...............\n"
                                        + ".............c.........................L..........\n"
                                        + ".................C..N............o............9...\n"
                                        + "...........e..f..N................................\n"
                                        + "8.............................B...................\n"
                                        + "...........4...............................x......\n"
                                        + "....w....RY..........4.......................P....\n"
                                        + ".........yw.....Y.............o2...............7..\n"
                                        + "..6y........4..............fo..............7......\n"
                                        + ".........Y..6............o......................x.\n"
                                        + ".....Y....e.....y..I.r...........2................\n"
                                        + "....e.............................P.......z.bB....\n"
                                        + ".............6.................B........7......x..\n"
                                        + "..y.N........f...........1....I....z....B.........\n"
                                        + ".....e....f.............I.................2.......";

    public static void main(String[] args) {
        Util.time(()-> a(Input.parse(TEST_INPUT)));
        Util.time(()->a(Input.parse(INPUT)));
        Util.time(()->b(Input.parse(TEST_INPUT)));
        Util.time(()->b(Input.parse(INPUT)));
    }
}
