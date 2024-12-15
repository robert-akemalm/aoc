package aoc2024;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import aoc2024.Util.Direction;
import aoc2024.Util.Pos;

public class Day15 {
    private static void a(String input) {
        String[] parts = input.split("\n\n");
        List<String> mapLines = parts[0].lines().toList();
        Map<Character, Set<Pos>> map = new HashMap<>();
        for (int y = 0; y < mapLines.size(); y++) {
            String line = mapLines.get(y);
            for (int x = 0; x < line.length(); x++) {
                map.computeIfAbsent(line.charAt(x), __ -> new HashSet<>()).add(new Pos(x, y));
            }
        }
        Set<Pos> walls = map.get('#');
        Set<Pos> boxes = map.get('O');
        Pos robot = map.get('@').iterator().next();
        String moves = parts[1].replaceAll("\n", "");
        for (char move : moves.toCharArray()) {
            Direction direction = direction(move);
            Pos nextRobot = robot.next(direction);
            Pos boxToMove = boxes.contains(nextRobot) ? nextRobot : null;
            Pos moveTo = boxToMove;
            while (boxes.contains(moveTo)) {
                moveTo = moveTo.next(direction);
            }

            boolean canMove = !walls.contains(nextRobot) && !walls.contains(moveTo);
            if (canMove) {
                robot = nextRobot;
                if (boxToMove != null) {
                    boxes.remove(boxToMove);
                    boxes.add(moveTo);
                }
            }
        }
        long gpsSum = boxes.stream().mapToInt(box -> box.y() * 100 + box.x()).sum();
        System.out.println(gpsSum);
    }

    private static Direction direction(char move) {
        return switch (move) {
            case '^' -> Direction.UP;
            case 'v' -> Direction.DOWN;
            case '<' -> Direction.LEFT;
            case '>' -> Direction.RIGHT;
            default -> throw new IllegalArgumentException();
        };
    }

    record WideBox(Pos a, Pos b, int id) {
        boolean covers(Pos p) {
            return a.equals(p) || b.equals(p);
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof WideBox that && that.id == id;
        }

        @Override
        public int hashCode() {
            return id;
        }
    }

    private static void b(String input) {
        String[] parts = input.split("\n\n");
        List<String> mapLines = parts[0].lines().toList();
        Map<Character, Set<Pos>> map = new HashMap<>();
        for (int y = 0; y < mapLines.size(); y++) {
            String line = mapLines.get(y);
            line = line.replaceAll("#", "##").replaceAll("O", "[]").replaceAll("\\.", "..").replace("@", "@.");
            for (int x = 0; x < line.length(); x++) {
                map.computeIfAbsent(line.charAt(x), __ -> new HashSet<>()).add(new Pos(x, y));
            }
        }
        Set<Pos> walls = map.get('#');
        Set<WideBox> boxes = new HashSet<>();
        int boxId = 0;
        for (Pos p : map.get('[')) {
            boxes.add(new WideBox(p, p.next(Direction.RIGHT), boxId++));
        }
        Pos robot = map.get('@').iterator().next();
        String moves = parts[1].replaceAll("\n", "");
        for (char move : moves.toCharArray()) {
            Direction direction = direction(move);
            Pos nextRobot = robot.next(direction);
            boolean canMove = !walls.contains(nextRobot);
            Set<WideBox> boxesToMove = boxes.stream().filter(b -> b.covers(nextRobot)).collect(Collectors.toSet());
            Set<WideBox> nextBoxes = new HashSet<>(boxes);
            while (canMove && !boxesToMove.isEmpty()) {
                Set<WideBox> nextBoxesToMove = new HashSet<>();
                nextBoxes.removeAll(boxesToMove);
                for (WideBox boxToMove : boxesToMove) {
                    WideBox next = new WideBox(boxToMove.a.next(direction), boxToMove.b.next(direction), boxToMove.id());
                    if (walls.contains(next.a) || walls.contains(next.b)) {
                        canMove = false;
                        break;
                    }
                    for (Pos p : List.of(next.a, next.b)) {
                        nextBoxes.stream().filter(b -> b.covers(p)).forEach(nextBoxesToMove::add);
                    }
                    nextBoxes.add(next);
                }
                boxesToMove = nextBoxesToMove;
            }
            if (canMove) {
                robot = nextRobot;
                boxes = nextBoxes;
            }
        }
        long gpsSum = boxes.stream().distinct().map(b -> b.a).mapToInt(box -> box.y() * 100 + box.x()).sum();
        System.out.println(gpsSum);

    }

    private static final String TEST_INPUT = "########\n"
                                             + "#..O.O.#\n"
                                             + "##@.O..#\n"
                                             + "#...O..#\n"
                                             + "#.#.O..#\n"
                                             + "#...O..#\n"
                                             + "#......#\n"
                                             + "########\n"
                                             + "\n"
                                             + "<^^>>>vv<v>>v<<";

    private static final String TEST_INPUT_2 = "##########\n"
                                               + "#..O..O.O#\n"
                                               + "#......O.#\n"
                                               + "#.OO..O.O#\n"
                                               + "#..O@..O.#\n"
                                               + "#O#..O...#\n"
                                               + "#O..O..O.#\n"
                                               + "#.OO.O.OO#\n"
                                               + "#....O...#\n"
                                               + "##########\n"
                                               + "\n"
                                               + "<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^\n"
                                               + "vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v\n"
                                               + "><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<\n"
                                               + "<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^\n"
                                               + "^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><\n"
                                               + "^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^\n"
                                               + ">^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^\n"
                                               + "<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>\n"
                                               + "^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>\n"
                                               + "v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^";

    private static final String INPUT = "##################################################\n"
                                        + "#...O.....O.O.O#..O.....#.OOO..O#.....O..O#..O#..#\n"
                                        + "#.O.#.O#.........#O..O..#O...O..O#......O..O.....#\n"
                                        + "#...O..O...OO..O#..OO#.O.......................O.#\n"
                                        + "##....#OO....O.O#.OO#.OOOOO...#OO.OO....#O....#..#\n"
                                        + "#O...#.O...###.OOO...O.OO..#....OOOO#O.O.......O.#\n"
                                        + "#OOOO..O#O.O.O..OOO..O....O......#OO.O..O..O.....#\n"
                                        + "#....#O.O....OO.......O...OO..O.O.O..O......OO#O##\n"
                                        + "#....OO........#O...#.O....O.OO...O.OOO.........##\n"
                                        + "#.#OOOO..O.O......O..O...OOO....OO.#OO.#.........#\n"
                                        + "##OO......O.OO..#...OO.O##.O....OO..O.....#......#\n"
                                        + "#O..O.##O....O...##.O....O..#..O...O.O........O..#\n"
                                        + "#O......#.....O.OO..O......O...#...O....O.O..OO..#\n"
                                        + "#...O..OO.OO.....O#OO.....O..OO.#.....OO..O..##..#\n"
                                        + "#.#.O#.O..O...............O..O......#....#....O..#\n"
                                        + "#O...O....O.....O....O...#..##.#......O..O...##..#\n"
                                        + "#..O.OO.....O.##OO.OOO..O......O.O.O...O..O.O.O..#\n"
                                        + "#...##..O..O.O.........OO.O.#.O.O...O...OO......O#\n"
                                        + "##...O#.......OO.O....##....##O.OO.OO.O....#.....#\n"
                                        + "#.O.OO#...OOOO#..O..O..#..OO..O......O..O....#O.O#\n"
                                        + "#..O........OOOO#OO.OO...........O.O..OO....OO...#\n"
                                        + "#..OO.OO#.OO...O........#..O..O..#OO#.....O..O...#\n"
                                        + "#O....O..OOOOO...O.#...O.#.#.O.........OOO#.O...O#\n"
                                        + "#O....OOO.O...O.OO......O.O#.O.O..#O..O#.OOO..O.O#\n"
                                        + "#O........O.OO...#...O..@......O.O..#O..OO.O.#..O#\n"
                                        + "#OO....#.O......OOO..O.......OO....O#.OOOO.O.....#\n"
                                        + "#.O.O.O...OOO..O.OOO.O#.O#O...OOO#..#.......OO.#.#\n"
                                        + "#.O..#.OO..#.#...O.........O....O..O.....O.OO.O.O#\n"
                                        + "#.O#OO.O..O#....O......O...O#.O..O.OO.O...#O....##\n"
                                        + "#..O.OO..O.#..........#.O....OOOOO#OO.....OOOO.#.#\n"
                                        + "#.O..O.........O##O....O.#.#......OO.....O..#.O#.#\n"
                                        + "#.#..O#.#...#...O.....O..#.....OOO..O#.O..O##O...#\n"
                                        + "#.O#..O...OO..O...O.#..O.O....O..OO..O.......O..O#\n"
                                        + "#...O.OO...O....O.......#O.O.O##.O...O..O#...OO.O#\n"
                                        + "#....O.OO...O....O...O....#.#..#..O...#.O.O.#....#\n"
                                        + "#.OO.#.#..OO#....O.O..#..#..O.O..O......O..O.O...#\n"
                                        + "#..O...O#.O...OO....OO...O.#..O...#.....#...O#O.##\n"
                                        + "##..O..O.#.O...O..O..O..#.......O..O..O#..O.O...O#\n"
                                        + "##....O...O.O..OO......O...O.OO..#.O#..........OO#\n"
                                        + "#O...O..O#O...OO...OO.O.....O...O.#....O.O..O..O.#\n"
                                        + "#....O...O.OO....#O.O...#.#......O.#...O...#...#.#\n"
                                        + "#.....O..O.O...OOO#O..#OO.O...O...O..OOO...O...O.#\n"
                                        + "#O#O....O#....O.#..OOO....OO...#...#......#..O.O.#\n"
                                        + "#OO..#.....#.O...OO...O#.O.O.O..#....#O.O.....#..#\n"
                                        + "#...O....#O.O.O..#..OO...#.OO....#..#.O......O...#\n"
                                        + "#O....OO..OO.OOOO...OO....O.....O...#..OO.#.#....#\n"
                                        + "#O....O..........OOO.O..OO.O..O..#..O#..OO..O...O#\n"
                                        + "#.O.OO..O....O....#O#O......#...O###...#O....O..O#\n"
                                        + "#.#O.....O..O...#O.....#O........O.OO....O.#....##\n"
                                        + "##################################################\n"
                                        + "\n"
                                        + "<<>^><>v^v<<<v<>>>vv>v><>>v^>><>v>>><><<^^<v^^>v^><<^><<v<>^v>v>><v>>^v<<<v><<<<vv>^>>v^v>>v>>^v>v>vv><>^<><<>v>v>^>>^<^v<^>v<<><^>^^>^<^><<^><v^><^><v>v><<v><>^<<v<<<<<^^<>>>>>>v^^^>>^v<v>>><^>v<^<^v^>><vvvv>^><vv<v<>vv<^v^v<vvvv^>>v<^^vv^>vv^<>>^<v<><^v^<>^vvvv>>v>^<<v>vv><<<>v^v>^<<<v<v>>vvv^>vv>><<>v>v<<<<^v<>v>vv><>^<vv<<<<^<^>^v^v^v>v^>>v^v<<v^^>vv>^>><<><<>>^><>v>^>^^^>^v>>^^v^<v><>vv>v<>v><^<^v<<vv>>^^^<v^^^^^<<^v<^vv^>v><<^^^<^v<^v^v<v>vv^^>v<><>v^<<v>^><vv>vv<<<^^vvv<v>vvv^^^>>^v<<vv<><^<^<vv>^v<vv^v<<^^^<v>v^^v><><><^<^><v>v>><>^^>^<<v^<^v<v><<v><>^<<^<>^>v>^><<v^>>v^^><v><^>v<^<v<^>v<<^<<^>^>v^><vv>vv^v>><>^^^<>>^<^^vv<<<<^>^>^^^>v<vv<^v>>^v^^<^>^<^<<>v^>^>v>>>v<v<^v>v><<^<^vv>^><v<vv>^^^<<<v>vv><><^^<>v>><>^><^>^v^><^vv>><vv^v>^<^vv^<^<v><<v>v^>^^^<<<><>vv><v^<><^>^<^<<<>^v><vv>^>v^v<><vv>v>^><>>^><><^^v<<<<vv<>^vv<^^v^><>vv^^^<>>vv>>><<><v>>^>v>>><>v^<^<^^><v><vvv^>v<^v<>v^><^>^^^<^^v<^>^<><>vv>v<^v<v<>^^^v^>>vvvv>><>^>vv>v<v<v^v^>>v<>^v><v^^v<<v^v>><^>>>vv<<>>^vv^v>vv^<>\n"
                                        + ">^<<v>>v^<>^^<<^<^>>v^>v^^v>>><>^<<^><v>>^>vv<>v<>^<>^v<v><<<<<v^^>^>v<v^^^<<<^<^<<<<>>v<^v^v<<><v^>>^>^^<^<<^^^v^>>^v^>><>^<^<><v^>^^^vv<^^v<>v>v>v<^<v<v>^v^^><>>>^><^v<vv<>v<><vvv<<^<<vv<^>^<v<<>>vv^<>>v><^<>v^<<v>^<>><>><<>>^<^^<>^v<^^>>>^><v<vv>>^vv<v^>vv^>^^<^v>>^^>^>^^^vv^^>><<v^<>>^^<vv^><v<vv<><<v>>^^^v^<<^>v>^<^vvv>v^^^>><<^v^<^v>v^vv^^vv<v^><vv^>^><><^>v^><<<><^v<vv<>^vv<v><>^^><<<^><<>><>>^v>v><<><^<<<v<>^v>>^<<^v<>><^v>^>^<vv<^^v><v>v><<^vvv<>>v<^^>^>>><v<><<>vvv^^v^<v<v<>vvv^^<>>>vv><^>>vv<>v>v>>>^^<<<>^v^^^^^vv><v>^v^v^><v>^vv>vvvv^^^>><^^v<^><^><vv><<v^><<>>v>>v><^>^>v^>v<^>v^^>^>>>vv^><>v<<><<^v<^<>v>^^<<^<<^^^^^^<><>><^vv>vv><>^<^>v>^vvv^<v>v^>^vv<^>>^^^^<v<^^<>>^vv<<<^v<vv>v^^<><><<<><v<v<v^v>vv>vv>^^>><<>>>>>><<v<<<<vv>v<v^^v^>v^<>v<>v^>^^v<^v>>^>^<^>>>^<<<<v<vv^^>>^v<>^><<><^<>v^^^<><<^<<>>vvv^v>>>><<v<>^>>^<>>>vv^<v^v<^>^v>vv><<v<v<<<<v>^vvvv^v^<v>vv>>vv<^v<<v^<vv<<><v>^><v<^>^<^>><<><><<>v>><><>^^^<><^v<v<>>^v<<^v^v>vv<^^vvv<>v><v<^<<>^v^v>>v<<>^^<vvv<^^<><>>>^v>v\n"
                                        + ">^v^v^<^v>><<<^vv<>><>^<>^<<<^<^^v<<v<v<v<>>><^<vv<>^v<><<<>>^<>v^^><<<^<<<^>vvv<^<<<<<<<><>^<v>v^^vv<><>^>>v^v>^<vv^vvv>v^^<^^^<v^^<^^<<vv>^^v<<<<<vvv>v<>^v^<<^^><>v<<<^v^>vv><>v>^v>v^^v^<<<>v<vv><>>><>>><^<v>v>>v>^>v^<><v^<>^^vv^^^<>^v>^v<v^v><<v<<v<>>v<v^v<v<<v^v><<^>>^^v>>^>^>><v>v^^^v>><><vv>>v^>>>v<>^><<><>>>v^<<^><>><v><^><v<^>vvv^vv<<>v<>v><>>vv<v>^v<<^v^>v<v^<<<^v<v>vv>>^v>^>^<><<v^>v^vv>^vv^^v^><<>>><>v^>><v^^<^>v^^v>v^v<<>v>v>><^^^v<^v^>^<vv^>>^><<>^vv^^v<^>><^v<^<<<><vv^<><v>^<v^v<<>>>>>v<<v<<^<>>^<v^^<vv^><><v>>vv<^^><>v<^vvv<<<^^^>^v><v<v>^^<v<v<vvvv>vv>><>^<>>>^><v>^<<v<v<<<^<<^>v<>>>^vvv<v^v<>^^v>vvv<^v>v^^v>v>vvv><<^<vv><v^>vv^v>><>v^v><^<^v^v<^v^<><<<v<^^><v>^><^<vv^v<<><^>^^^>vv>^vvv><<>><^<>>>^<^><^>^vv<><^>><><><>^><>^v>>v^^>vvv>v<><<v<<<>v^<vv>><v>><>>>v>^>^^^v><^<^v<^<^<^v^<v^^<<<v<<v<^>v>^v>><v^<v^<v<<v<<>^>^<vv>v>v^v<<^v^><^>>>^^^<>><^^><^v><><v^vvv>v^v>>v<^^><^vv><<^v>^v^^<^vv>v<v<<vv><>^<v<<v<<<^^^v<<>><^<<>>>>^^^^^<v>v<<v>><<><<>><v^v^>v>>>^v>>>^^^<^^>>^><^<\n"
                                        + "v<vv<^>v<v<><v^<>^^<vv><<<^<<<>^^>>><^^<v^v<^<>v<>v<^^^v<^^v<<v<v^<><^^<>v>v<>v>v^^><<<^^v^<^vv<>>v^>^<v<>vvv>><v>^>v<v^<^><<^>v^^^^<>^>>>^^><<<v^^>^>v>v>^<vv>v^v>^<>^^<v<<^v>^<<<>v<<>v^<<><<>vv^v>>>>v<>^v><>^v>v<^<>v^<^>v<><>^<^vv>^><>^v<^>><vv^<^<v>^v^<>>vv^><<^^><^<^v^>><v>^>v<v^v^>>>>>><^><v^<v<<<^<^<^v><<<<>v^<^>^<v>^^^<^<<v>vvv^^^v><<v>><<><^^>^v>v^<vvvvv>>>^>v>>>^<><^v^v^<^^^v^<vv^>vv><<v<^v^<v><^^<^<^v>>v<v^<vvv^>^>v>v<<^>^^^>^<^><>^v<^><>v<>>v^v<^vv^>v^vv^<<^<<<<^>^>>>^vvv>>vv>^>>v>^vv><<>vvv^^<^^>^<><^v><<^^v^<^<^v<^vv<><v>>>^>^><>v^>vv^^>^vvv>>>v>>vv<<<<v<^>^^^>^><>^^>v^<<^^>v^vv>v>^>>^^^^vv<^vv<<>><^v<vvv><<>v^>^^<<vv<<<<<^v<<>>^v^<>v^<<v><>v^v^v<>v^^^<v^vvvv^<<>^vv<><<>^>>>^>^^>^^^^^v>^^^^^^v>^v<v>^v<v>v^^>^<><^<v<>^>><>^<<<v<v>vv><^><^<<><v>>^^v^vvv<>>^>v>^^v<^v<v><^v<>^vv<<^v><v^v^v<<^^^>v>vv<<>>>>v<>vv<^<v^^v^<vv>^^>v>^>^^>vvv>^<v><^><<v^v<v>>^^^<^v<<<^vv>^<v<>><><^<<^^vv>>^<<^^v^<<^>^<vv<^v<^>>>v<^^>v^^>v^>>^vv^^>v<<v^>^<><<<v<^^><^vv>><v^^<^^<<vvvvv^>^><<^>^<<>>>^vv><\n"
                                        + "^<^<^<^^vv><^><<<v^^v<<>>>^v><v>><<<<<>>v^>^^v<^^>^<<v<>^v<vv^^<vv^v>>^><<v<>vvv>v>v<<vvv<>^<>^><^v<>^^^><v<<^<^<v>^^>^^v<^>vv><^^<>v^>v>^^>v^>v>>v>^>vv>v^<<<<v<<v^<>v>v<>>v<^^<<^>^v<<>v>><>v^v<>^^<<v^<v<vv^><^>vv^^>v<v^<^><<^vv<>v<>v<^^><^^v^^<^>^v^>>v^v^<<vvv^^<vv>v>v^>^<>^<<^v><>^v<vvv^<v><v<<^v<^v<^<><><v<^^^^v>^^>v^^^<>><^<>^^>^<<<^^<^>^>^v>>>v^vv><<<^<^v>vv<^<v><^^><v<>v<<v<<>v>>>>>^v>v<>^vvvvv><^^^>v^>>v^^<^^>^<>^^^^<>^<>^<v^v<<^>>v>v^v^^<v<<v^^^<v>v<>^^><v>>^vv^<<^>>>^^vv>^<>vvv<<>>^><>><<v^^<^<>v>vv^<^<><<v^^v<<<>v<<<vv^^^<>>^<>v>v>>v^>v>><>vv>>^^^<v><>^^^v^<<><^vv^><>^>v<^><^>>v^^<>v^v^v^^v>vv>>^^v^^v^v<<>^<><^^v>v><<<<>v^^v>^^<><^^^vvv^<v^vvv><>v>>^^^v^^>^><v<><^><>>>^<v^^v<^<<<vv<<v^v<<vvv^>^<v^>>>^<>vv>><vv^v>v<>vvv><>>vvvv>v<<<^^>v<v>^^>>>><v^v<>>v<v^v><^<v>vv^><<^vv>><<vv<>^^<v<>^v><>vvv^v<^^><^^v<>^<<^^^<v<>v<^>v^^<><<v<>>v^>>>^<^^>vv^v^<>v<>>vvv^>^<v><>v^^v^<v<>v^>>v^v<<^>><><^>^<>^v^^vv<><v>><^^v^v^<^>>^><^v<^>vv^<>v>^>v<<>v<<^>^<<<>v><<<^^>^><v><v^<<><<<v^>>v^><<<v<>\n"
                                        + "v<<<<><<>v^<>v^^><><>v<vv>^v<vvvvvv^^>v^v><>^^<<v<vvv>vvv^^v<^>vv^<v<vv<<>vvv<<v<^^><vvv>^^<^<>>^^><<<>><^<vv<><<vvvv^^^<<<<<^>vv<<^>v^>^v<^>^<>>^v^v>^<v^v>>v>>v<v><<>^>^<v<<><^vv>v>^><^^>><^^vvv<><>vvv^<v<v>^>^^<vv<<^vv<^^><>^v^<^<v><^^>v>^v>v^>v^><v<>>^v><v>vv<^>>v^^v^^<>^v>v>^<<><^<>^<^^>>^^>v^v^>>v>^>v^>v<^<>>>vvv>><v<^v<^vvv>>>^v^>>^vv<v<<v><^<>>>^>vvv>><^vv<<<>^>^v<<^^<^^^<>v<v>^><^<^<^><><v<v^v<>>^>^<<<<v<^^<>>>vv<<^<^v>^<<>>>vv^v^^><<vv<<vv<<^^><<>>>><vv<v><v>><^<v>^<<vv<v^<>v><v<>>v<<v^>^<<>^<vv^>>v><v^v>^^^v>>^>>>v^vv>>v<vv<v<>>^>>><^<>>v>v><<v<v<>v^>v>>vv<<^>v^><^v>v^>>>><>^>vv^^^^v<^^<<^>^^>^><><v<<<>v^<v<^v^>^>^^vv<<<><v^v^v<<^vv<<^><<vv>><vv>^vv^<v>^^v<>>^vv>^>v^v>>>>vv>><^v^^^vv^^v<>><>^v>^<v<>v>v^v<<^><>v<<>^><^vv>>>>^<><^<<v<vvv<<>^><v>^vv^^<>v>v<^><><^vvv<^^<>v^>^<^^v^v><<^>^v^v<v><<v>^<>>><>vvv^^v<^>>vv<v<>^^v>^>v^^>><>>>^>><<><<>><^<^v<v><>^^v<><v><<^vvv^<>vv^>><>^<^^v^v>v^>v<<>v<<^^>>v<<v<^v><<^^<^>>>^<>v<><>^<<v^v>vv>^<<^v^><><v^>v<^>><^><>vvv<>vv^^^<><v>><<v<>^<v\n"
                                        + "vvv>>v^^v>v^^<><v^vvvv<>v^^v<><v^>^v>v^^v^>v^>^<>>><>>>vv>v<v^v>^vvv<<>^>>v^vv<vvvv<^v<^<<>><v^v<><<<^v>^>v^<>^^v><<v<><vv>>v<vvvv>vvvv^^>^><v<v<^^vv<v^^>^><vv<>vv<>^v^^v^<<<^^^^<><vv<<^^<^>>v<^vv>><v<>^v>v^v^^>^v<><^v><>>^^v<><>^>><^^>^>v^><v<^>>^v<>>>vvv^^^<^v^>>^<>^vvv^>>^<^^<><^v^>>>v<<^>^>^^v<vvv<>v^v^^v>v^^<<<>><<^vv^^<>><>^^^v>^v^vvv^>><v<^^>^vv<^vv^<<<v>>vv^<^v^<^^><<v^^^<^<>>>^<^v><>v<^<<>vv<>^<v<^>vv>v>^>^^v<<<^>^v>^<><v<^^^>>>>^v>><^^>^<><^<><<vv^>>^^^^>^>^v<<<^v<v^><>^>>vv^v<>v<^v><>^v^v<^><>>>^v><>>^vv^^>v^<vv^^<>^<^>^<>>v>^>^<v>>>vv>><^>>^v>>>^<vv>v>^<^v>><v^<v<v^>v^^<^>>><^v>>v>v>^<^^<<>v^v<<v^v<v^vv^<^^>>>^v<>>vv><v<vvv<>v^><>>^>vv>^>>^v^>vvv<^^<v<><v>>^^<<<^<^>v^><>>^>^v^vvv<<^>^<v><>>>>><v^vv><>>^<v^>vvv><<v>v^<>vvv>>><<^^^><<^vv^v><<><vvv^<><^^>^<>vv<<v^>^>>^<^^<<<>><^><^<^<>>v>vvv<><><^>^><v^v>v>^v<>>^<v<vvv^^v>>>><>vvv^^^vvv<>v>>>>v>>^>>^v^v<<<^^vv^^v<>^<>>v<v<<>^>v<>><>^<^^^<v>^><^vv>v^^^><v^<vvvv^<v<v<^><vvvv>>>>v<vvv^<vvv<>v>^^<>v<<<v><<^^^<<^^v>^>^<^>^<><^^<>>v\n"
                                        + "^<v<v<<^><>>vv>^<<>>>^^>v>v^^>vv^>><^<>^vv<>v<v^><vv<>>vv<v>^^<^><>^<v>v>vv^>v^^>>v>>^^<^<<><v<>v><>><^>><>v^^<v>^v^>^<v^^<>v>>^vv>vvvv<<<^<v<^<>>^>>><^><<<v>v><>^>vv<^v^v^^v><<^vv^<v^>>><>^^<^<^<vv<<<vv><<>v<^<>>^vv><^><vv^>^^^>^v>vv^v>v<<><^^<^<><^<>v><<>><v^><v<<<<<<vv^vvv^^v<<<>v<^>><>^vv>>>><vv^v>^v<^vv>>>><><^<<^v<^<^><<>vv<>^vv^vvv<^>v^v^<v>>^v>>>>v^^vv<<>>^^<^<>>vvv<>v^v<<v><<v^<<>vvv^><vvv><>^v^v^>vv>v><^vv^^v<<v<^^>>^<>v^>v<v>^^v>^><v>^>v>>><>^><vv<v>^v>vv<>v<^^>^v<v>^>^><v^^^^<<vv>>v>^^^>><^v<><v>^<<<v^^>v>>><v>v^^<^<<<vv>>^^v<><<v<<<><^^^<vvv^v>>^^<<^^v<><^v>^>>^<<vv<>^<<^^v^vv^^><^><<>v^^<^v><><^^><><^><^>vv>^^vv^><<^<>^^vv^<v^^v^><>^v^v<>v^<^v^>^<>v<^^<^<^<^vv^^<<>^^^<<<>vv><>v>^v^v^<v^^v<^>^><>v<v>>^<<^^^<v>>^<v^>>^vv^<v<<><^vv^<v^vv^v>^^<><^<>><^v<v>>>>vv<>vv<>v^>v>>^^vvv<^<^><^v<>v^v<^>>v^<<>><^>>v^<^^<<<>vvv^^v^^>>><v<v^^<v^v<<vvv^<>>vv<v>v>^>v^<vv>v<^<>v<><v<v<>v><^>v>^<^>>><<<<vv>v<vvv<<>>^^>v>><v<vvv^^<v^>^v>^v^>v<v^>^v<v^><><^>>^<^><^<>^>^<^^^^<<<^vvv><>v>>^<>>^^^\n"
                                        + "^<v<^^<><v>>^>>v<^<<^>>v<<v^>>^<^>v^v>>v<>vv<^vv^v<<v^^><<>v><^^<vv><>><<><v<><v>>^v^><v^>^>>^vv^v^^>^>v^>>v<<>^vvv<>v^<<^<<^>^<vv^<<<v>^<>>><^<^>v>v><v<<<v>>>>>v^>^<><<>v^^vv<<<>v^v^><<^>vv<>>^><^^<^>^^><<<>v^<v^>^<v^v<<v^<>>^^^><><^<<>^v<vvvv>^v^>^>>>^v>>^v>^^^<v^vv^^<^>v<v<v>>^><<<^>^<^<<v>^^^v^<^<^<v><<>><<<<v^<^v>^vv^<>^^<><>v>>v>^>vv<^><vv^<^v<<<<v><>^v>vv^>>v<<v^>v^<vvv^><<^v>vv<^v^<>v^v^^^v^<<v<><v>vv^v^>^<vv<<vv><^v^^v^^><v><^vv^>>><>>v^^^<v^^^^<>^<^<>v><^>^v^^v^v<^vv^>^<<^v^v^^v<^v^<^^><>v><v><^v^>v>^><vv<^v><v<^v>^^<^v<^<v^<vv^vv<<<<^<v<>>^>v>vv>v<^^>^v<v>><<v^<><^^>v<>^^>^<<<<vv>v><^><<<vv><<^v<><<^v^^>>^<v>^<<v<<<>^v<^>v^^<^<^<<>^v<><v<vv^^^v<^^>^v<vv^v>^<vv^<>^<<v>>vvv<><><v<<>v^vv<<<<>v^^v><^vv<^>^>v<>vv<vv^><^v^>v><v<>>v<^vvv^<vv<v>v>>v>><>>v^>vv><<>>vv^^v>v^v^v<>><>>^vv>v^vv^>^>vv><^^v^^>>>^v<^<<v>v<>^^<v<v><>>^^<>><<<<vv<vv^^><vv<^>v^<^>^v<<v<>^<v>^^v^v<^^v^<><vv^^<v^^>^^^>v^<v<v>v^<v^^<>v><>^<v^<v^v<v>v><<^>v^^vv><>>>v<v^v<><<<v^^<v>v<^^<<<<v>^vv<>><v^v>>^>v<vv>>^<>^\n"
                                        + "^v^<>><^v<<>>^^<<^<>>>v<<^<>v><^^v><^v>v^v^v<>>v>>v>^v^v>^>>><<<^^>^>v>>^><<^>^^<>vv<<v<v^vv>>>>>^<<><<vv><v^^><v^v><<^^>^^<^v<^<<v>^<<v^>v^>>^<v^>^<>^^v<><<^>>v<^v<vv<>v<<>^vv<v^<vv>>>vv<^<>><v^>v>v^v<v^^v^>v>>>>^>>^v^v<>>vv<^^>^^^^<v>^^vv^vv<^<vvv>>v<^><^>v>>>>><<>v<v>>><>^>>><>v>>>>>^<^>^<^<>^vv<<^<<<<>^<<<^<^^<>>>^<<^><^<<<v>^^^^v><><<<>^<<<v><^>>v<><v<<v<vv<^v^v>v>^^<v>v><^<v><^>v><<<><v^><<^v^>><vvv^<>v<v><<<v^^^vvvvv>^<vv>^<><^v<<<>v<>v>vv^v>^v<^>vv^><>^v<^v<<v^v^vv^v^vv<^v>>vv^<v>>vv<<^v^v<^v^<^>>vv^>vv><v<^>^>>^<v<^v>vv<>>v^<^>v><<^<vv>><>^>^>^>v>^^<^v<><v^^v<<<v<>>^^>^>^><vvv><>>>><v<^^>v^>v<v^<><v<v>v^v<^^<^>>^<^<>^^>vv>vvv>^<>>v^v^vv<^^<<vv^^^^><v^>><^<<v<v^vv<><><<^v^v<>>^<^vvvv^>^v<>><<<><^^vv<<<^<<v<v^>v^^>v^v>^<>>>^>v>>^v^^^v<v^><>>>^^v^^v^<>>^>vv<>^><v^<<<><<^^<^<<^v^v^v>^>^<<v><>^<^^v^><<><^v^><vv>>v>^>^>>><>v^>><^v<<<<<^v>^^^^v^v^vv^^>^^>vv^<^<v>>^<>v<^>><>v<v^v<vv^>^^v<^^<v^>v>^<<>^>^<<v<vvv><^^v<<<>^^^v><>vvv^^<^<>^>v><>v>><^<>>^>^vv<><vvv^^v><v<>>^>vv>>v^<^<><vv^v\n"
                                        + "<<>>v>^^<<<vv>v><<<<v^v<<<<^v><<<vv^vv^^^v^><^vvv>^<<^><>v>><<<^v^^>>^v>v<>>><v^v^v>>^^v>vv^v>v^<^<vv<v>vv<^^^><^v>><>>>^<v^^^v<^^>^<>^^^<>vv^<>><<<v^^v<^v^v>v<^^^<>v^>vv^<<^v<<<<<<>v>>>><<<^<v^<<vv^>vv<^><<>^>>>>v<v^>^>>^v>vv^v^v^<v>v>v>>>v<<>v><><<^^>>>^vvv^^^<v^v^><^v^<<<^^^^^^<<v^<>^v^>v<vv>><vv>v<><>v>^>v<>vv^>^<>v<<<vv<^>^v^<^>^v^<^>^v<<>^vv^><>vv><<<^>>^^^^>^<>>v<<>vv>v><<><>><>^>^>^v^<^><<^v^>>^<v<<<>^<<>><^>v>^<<<<^<^^v^><>>v^^>^<<<<<v^^<><>^v^^v<>>^^^<<<v>v^><v^^<<<^>>v>vv^v^>>>vv>>v^<<^v<<^<><^vv>^>^^vvvv^<v<^<<v<^><^^<^<vv<<<^^vv^^^<<^^<>v<>>^^^v<^<^^<v><<<v^>^v>^<<vv^^^^v^<<^^<^^<<<<<v>^^<^^>>v<>vv><<<vvv<>^^<<><><^^<v^>>^><>v>vv<<><^vv<>v>v<v>^^v>><v><<v<^>><>^><^<<<>>v<><<^v<^<>>>^^v<^vvvv<<>v<<<^<>^^<<^>vv<><>>v<><^vv<^<^<v><<vv<^v<v<>^<<<v><^^<^<><<>>v<^vvv^^<><<^<<>^>>vv<^v>vv^<^^v>>v><^v<>v>v^v<<<<<<<vv^^^<vvv<v^<><<><><^>^>v^>v^^v<v>vvv>^>v<^v<>v<<^^^^<^<>^^^<v^^>v<v^v>v^<^v><>><><^^vvv><^v<^^v^>^vvvv<>^<><<<><>>vv^>>^^^><><<v><^>v^><>^^^^v^<^<<<><<>^v<^v>v<<>>v<v^v\n"
                                        + "v<^>><^v^>v<v<v^<^^vvv<v>><>v^<>v<^<v^>v<^^<^v>>^^^<><^<^^^^^<<>><><>v^vv^<>v^><v^^^vvv>^v^<v^v>><>>v>>v^<v^v^>^vv^v<v>>v<><><><^<^^v^^>v^>>>^>^<^<^v>vvv<^>><>>>^>>vv>^^vv^>v<<><<^>v<>>><<><v^^<^^v>^<<<<v><v^^<vv^<^>>^<<v^v^vvvvv>>^<^<^>>>>^<><^<>vv<vvv<>>>>>v<^^vv<v^<v^><>^^^>^<v<<^vv<v^<^<v<v>^><<<^<v>vvvv<<^<<<><>v>>>>^vvv><^><^<^>^<v><^<><<v>>^>v<>v<^^<<^^^<<<><^<v^^<<^^><<v>>vv>^^v<>>^>>v>vv<<><<^^>>^<<v>><<>^<v><v<^<^<^<^><^v<^><^><<^>^vv^v^>>v<^><<^>>>^><^>>^^v>^>v<<^^^<^vv^v<<>>^>><v<>>>vvv>vv^v<<^v<^^v<<vvv<>>^<^<vv<^<^<^>>vv<>v^>>v^<>><v<<><vv<<>>>v<^<^><^v<v<<>v<><vvv<^<^<<><>><v^><^^<>><>^vv>v^^<>v^<^^^^^^vv>>^vv^v>vv<<>><<^v^v<v^^^v<<<<<><<<<v<v>>^><^><v<>v>^^vv^^><^v^v>><^>>vv><>>^^v^^<v>^<<^>>v<v>^<^^^<>^v^v<<^v^^^>>v<>v^>^>>v<^v>v^v^^<>^v<<>>v><^^^vvv>v<<<><v^<<^^^^>v<>><^vv^<>><>^vv><>>vv^>v^<v>v<^<^^^><>v<<<>v>>v^>vv^v>>^<<><><v>>>^<<^>v^v<<>^v^<<<<v^<^>><<^v><^v><vv>><v^v>>^vv><>^^^>><<^^v><vv<<^^<^vv>><>v<<<^v<<>>v<<vv<>v><v^^vv>^<^vv><v^<>^<v<v<<<<vvvvv>><vv^v<v<<<\n"
                                        + "^<^<vv<>^>v^<<<v>vvv^vv^>^^><><>v<v><<^^<v^<><^^^>^^^><v^vv>^v>v>>v^^v><<v^^><>>>>><><>>^>^^>vvv<<><<^v<v^v^v>vv<>^<>>^<vvv<>^><<^v^^<><v>v>>^vv^^<^^>v>>>v<<^v>^<>v>v^vv<>v>>^<v><v<>^^>>^<vv<>^v^^v^>v>v>^<v>>><v<>v^<^v<v>>v<v<^v<>^<>^<<<>vv>vvv<>>>>vvvvv><<>>^^^v><v<v^<^><^v<vv<><^>^>^^^>v>>v^<>v<^<v^^v><<v^>>>^^^<<^vv>>^<<^^<v>^<>>>vv<<<v^v<>vv<<>v<vvvv>vv<><vvv^v^v<^v>^vv<>>vv<>><>>v<^^<<^^v>v^^>^vv>v<<vv>^>^<<<vvvv>>^^<<v^^<vv>><^<<^><v<vv<><<>>v<vvv>v><<>v>>>v>^v^>><<<<>><<<>^><^>^^<<^^<<>>^vv<><>>^<>v<<<>>v<><<>><v^<<^vv<><v^^><>>><><^^^^<>v>v<vv^<>v^v^^<v<>>v<>><<<>v>><^<>^^>^^>>^<^v^>>^^^>^v<<v<^vv>v<vv><><><>^vv<>v<<<^><v<^>v>vv^^v<>v>v^><^v<^>v>><>>^>vv<><v><vv>^>><><<>><vv<^>>^v>>><<>>v<<v<^^<v<>v><<<<>>>v>vv^v^v^vv>>>>vv>^<><>>v<>^<<^>v>^>>><v><^><><<>><>v><<>^>v<^v>>^^><v<<<<<<vvv^><>^<<<<v>vv><^>>^^vv^^v<<>^>><v^<<>v>vv^^>>^><<<v>vvv>>><v><>>vv^>>><<v>>><v^vv><^vv<>^^^^><<<<^vv<<v>^v<<><v<>^<<<vv>>^><v<^v>^v>>>v<>v><<>vv^<^^^v^v^><>v<>>vvvv<<>^^<vv>^><v<^^^^<>>^v>>vv<vv^>v\n"
                                        + "^<>>^^^<<^>^>>^v^^>>>><^<>>v<>^>v<<<vv^^<<><^><^^<vv^>^^>v<^^v<<v<v^vv<<v<v>^><^<^v^<^v<^v^<^v<<>v^>>v>vv<<^vv^v><<v<v>><><v<v<v>v>><<^<<v>><^^^^vv^vvv>><>>v^^<>v<<<v^^^^><v<<<<>^^v^^vvvv<>^^v<<v<<^^>v^^<^><^>^<^vv<^v^v^>v^><>v^v<<>^<^>v>>v>>^v^^^v><>>>>>v<<<v>>v<>v<^<>v^^v<vv<<<^<^><<>^>vv^<<>v^^vv>>^>>>vv<vvv<>>^v^^<^>>vv>v^v^><<^><v>^vvv<v>^v>^vv^v><^^^<<^<<^^^<<>^<>^v^^<^^^^>>><vv<>>vvv>v^<v>>^<><vv><^v>>>v^<<v^>>>v^>>>><^v<v^^>><v^<^vv<v><<^^v^>>>^<><<><>>^<^>^>^<^<^v^vvv>>vv^v<^v^v>^<v>>>>^v^<>v<^^^>^^^>^<^>^>^>^vv^v<vv^^v^>^>>v^>v<v^^^^v^><v><^v^v><vv>v>^vv<vvv<<>v>^vv^>v><^^>vv>v><<^>v^vvvvv><>^^v^^>v<v<^v><<v<^>^<><v>v><vvv^^<v<v<v>v^^v^<>vv<v^<v^^<^^vvv>>^^>><vvv>v^^^vv<^>v^><<v^vvv^<v>vvv<>v^<v>^vvv<>^>vv^<^v<^^><<<<<>v>^v>^<^vvv<v>^<>>>^<<<^>v>^vv^<v<v>>v^<<<^^v^<><v>^vv>^^^>vv<v>v^<<<^>vvv^v><v^<<^<>^<^>>vv>v^^<v>vv^><<v<^<^<<^<^^^<v^>^v><<^v><<^><><<<><>>v>^<><vv^^>^<<<>^^v^><v^v<<<^^>vv<<v><^<<<><v>^^v^v^<<<><^<^^>v^^><>^<^^^>^^<><>^>>vv>vv>v^^<>>v^<>^^<^^^><^vv^>><>^><v\n"
                                        + "><<^v<v<^v>^>^^v<<<^v>^<>v>^^><^<v>^^v>vv<v>^>v>><<v^^>^^<<<><>><><>v^<>^^<<>^vv>>^^^<v<<>^^^^v<^<^<^><^<^^><^>v^><^>^>>^>>^^v>><<v>^<<^v><vv>>^^v<>>vvv^<^v^^<^^<>v><<^vv<v<v<^>^v>vv><^<>v>^^>vv>v>^^^>v<<v><^v^>^>>^>v<<<vv<v<v^<^>>^<^v>>v<<<vv^v>><vv<v>^<^^>v<v>v>vvv^^<><>v<^>^v^v>vv<^<^v><<><v>>v^^^^^vv^^<^><>>v^><^>>^<<^>^^>^<^v<v^><v<<^>>vv>>v>>^v><>vv^v>v<v>>v^<>vv><>v^^^>^^<<^<v<<v<vv>^>>vv>^<^^>^<<v<><<>^<<^<<^v^>>^>v><><^vv>>vv^v>>^v<vv<^<v>v^v<vv>>v^<vv<vv>>^<v><>^v<^>><^<<v>^^^v^v>^^<^^>><^<>v^<^<v>>^vvv<v<v<^<<^<>><v>v>v<^<v^>^<>^^^v>><v<>^^v><>>^<<^><v^<<>v>^>>^>v>>^><v><><<^v<<^v<<<><<v>^v>^>>v>>v><><><<v><^v<^<v^vv>^<vv^>>v<<vv<v^^>v^^v>^v><<<^>^<><><<^<v><>v^<<<>^>^>v>v>vv<v^^<^^^v<v>^<vvv<v<>^v<^^<<<^^>v<>vv<^^^><<>^^<v<v^^^v^vvv<^v^>^><v<<<^>v>^>v>v<^vvvv>^><<vv^<<<<^v<v<vvv^><v>^<>^<<<<v<><v>vv>^vvv<<<>^<^v>vvvv<>vv<>^<<>v<<vvv<>v>^<>^v^<^<v><vv<^><^^>>vv>>v>vvvvvvv<^<v<>v>>>>>>^^^vv><v^v<<<^v^^vv<v><v<^v<<v<>>>^<^^>><<>^<><^vv><><><^<^v^<^><v^<^>><><^^>v>>^>vv^v<>><^v\n"
                                        + "<v<><<>><v>>v<<^<>><>v<>vv^^vvv<^v>>>><v^><>>vv^^<vvvv><<><v>^^<^>^><^^<^>^^v^^^^>><>v><<<<v<>vv><<vv<vv><v<v<^<^v>v<>v^>>><^<^v<><^v<v>v>>^<^>^<^^>^v<<>v>^v>vv^<v^>v^<vv^<v^v<vv^vvvv<<>><^^vvv^v^<^v<<<<v^<>^v<v>v^<v><<>v>^v<>v^v<vv>v<>v<<><<>v^^^<v<^<v^v^<v^>^>^><<><<^v>^vv<<<>vv^<<^<>^^^v^^<v^v^v<^<>^>v<><<>^v^<<^v<<^<^^vv^v>v>^><>v^>^>><v<^<vv<^<<>v^v>>>v>>^^v<^<vvv<<v>^<<v<<^^><v<v^<^<vvvv<<v>^<^^v<>vv><^^^>v^vv<>^^^<^>>v<>^v^^<v<>>v<<<^<>^v<v<^<>>v>><<^<^v^<<><><^v^^v<^^^v<v>^^^vv<<^vv<<v>>^vvv>v^>v<>^^<<<>v<v<<^vv>v>^>v^<^^^>^^^^>^>>>vv>>v<v>^<v^^<>>vv><^^>>^^>><^^v<^<^<<^<^>^v<^v<>^<>^>v<vv<v^v<<>>vv<^v<^>^><<><v^><^<>^>>>>v^^<>>>^v<v^><<><>>>>^^^>v<^v><v^^<>v>>>v<^v^<>v^><<^>vvv>>^<^<>^<^v^>^^<vv<>v>^<><<v><>^v>>>v<^v><v^>^><<<v^<<v><>^><^^><vvv>>><v^^v><<v<^>^>^><^>v<>^^>vv<^<>>><^^v^>^vv<<><v>^v>v>>>>vv>v>v<v<><>>>v^^<<>><^^><><><v<<>^><^>^vvv^><>>>v><<v<^>^vv^^v<vv>>vv>v<^^<>>^><>>><v<<<vv^vv^^^><<v>>vv^<<^<>v<^<vv>>v<>>^<>^>^<>v><^^^^vv><<^<<vv>vv^>^^>vvvv>v><>^<<>><<<>^>v^\n"
                                        + "^^^<<>v<<^^<^^>^<v>v^<^^>^v<<^>>v><^v><<<><>^v<^v>^v<>>>vvvvv>v^^<<>^>>>>>>>vv>>v><>v>vv>v<>v^vv^v^>v><<<^^<^v^v^<^<^^^^><><>>>v><><^vvv<^>vv^^<><<v^^vv^<v><^>vvv^<<<^<<<<<<>^>><>^>vv^>v^<>vv<><<^<<<>><>v>v^^v^^<>^v^<<>><^v<<>><^v>^>v^>v^^vv>>v<>^<^<^>>^>vvv>v^<vv>>v<>>v<^<v>^<^<v<v<^>><<^v<>v^>>><^vv>vv>vv>^>^<>><<v><v^>>>>v<>^<^<><^^^v><<<v><<<<>^<^v<^vvv^<^v^^>^>>v>^><v<vvvv<>^v^vv>v<<v<>>^>^^vv<<<^v<^<<<<v^v><>>><v^^>^v^^^>>vv<>^<<>v<v^>^v><<><><v<v^^<vv<<>^>>><^^v><^>^><><<v^^<vv^<<v^v<<<>^^<^>>v<<>><<<<<v<v^^^<v><^^v>>^>>>^<v><v>>vvv>^^>vv^^^^<<><<^><<<><vv<<v^>>vv>>^vv^^<^^^>>^>v>><vv^><^>^<>^><<><><vvv><^^>^^<vv>v<^>v>v>>>>^<^^^<<<v<v^v^><^^^^v^<^v<<>^v>>^<>vv>v^^<<v><>>>^^v>^<v^vv^<v<>^v<>v^><v<^v^>v^^<^vv^^^^<^<^vv^<^<^^<>^v>^<<^v>^v<<<>^v<^v><^><v^>^v<<vv^^>^<v<^^v><vv>v^vv^^v>v^>vv<<^v>v>><^v<^^<v<>^>^<<<<>^<>v<<<<^^<<^<^v>^vvv><>><vvv>^<^^v^v^><><><v<vvv^v<<v<^vv^>v<^^<<>v<<>^><><<v^<>><>>v<>^<^^<v^>^><vvv^^>^^><<<<>^^^<<v<^v><v>>^v<v>>>><^^>v<vv<<v^v^<<<<<v^>><vv^v><<^>>v\n"
                                        + "^><vv^vv^^vv>v>^v^v^^<>^v<^>v>^>vvvv>vv<v<>>><>>^^<v<<^<vv<^>v>v>^>>>^<<^><^>>>>vvv<v^>^^<^^v<>^>>^vvv^^>^v>>vvv<^^<^><>>^>>v^v><<>v>>^>^v>>^<^^vv><<^v^>>>^^^^>>vv><v<<^^<v>^^>^<<^>><>><^>>vvv<^<^<v>^<>^>vv^><<>^<<v^<<^v^v<v^^<v<^^^><^v<v>vv^^>^^vv^v<>^^v^v<>>>^vv>v>>^^v<^v^^>><vvv<>v>>^v^v>^><v<^^^^><<>^<^><>v>v>>^v^^^v^^<<v>^<<>^<><<^<^>v<<<^^^>v<>v^vv^v<^v^^<>v<^<>vv<><<>v<v^>>vv<<vv>^^^<<<v<vv^<>^v<v<vv<<<v^<^v>>^<<v><v<>^v^^>>v^<v><^v<^^><^v<>^>v^^v<vv^><><vvv<vv>v><^>^<>v^<>v<^>>><v^<^<><<^<^^>>v^^v>^^^^^<>>^v^^>v<><>vv<^<v>>v>vv^^^<<^v<>v<^^^><<v<^v><<<><<>vv<<v^^v><v<>v>^v^>>>v^<>v<vv>vvv<v><>>><<>v^<^<v<<>>><<^vv<><v>>^>^<<<<v^>>>^^>^v>>v<^<^^>^<v<v><<<^v><vv^>^>^vvvv>^>v>^v^<<>v^v<<<v>>^^>^><>^<>v^>>vv^^>^><vv>v^<^<<<^^<>>>>><^>^^><><^>^^<v<>><>>^v>><<>>>><^<<<^vv>>>^<v<^^v^v^<><>>^^>^^>><v>>>><v>>v^v^v<<v<v>>v<>^v^>>>v^<<<^<><><^<v<^>>>^>v>^vv^>v<v<v>^<>^<vv^<v^><<v^^>^^v><<v>v>v>^vv^vv><^<v^>v<^^<vv>v<vvv^>^v><^<>v><<<^^v>>^^vv<^^<v<^>^^<^vvv^<>^^vvv>vv^^v><v<<^^v<<v^<<><>>\n"
                                        + "^v>>^v><vv><>^v^^><^^v<>v^^^>^^<<>^v<^<v<^>>v^vv^<^v^<v^^<v^>^>v>>^<v<v>vv<vvv^<<>><<vv><>^<>v^vv^v^^^<v<vv^^><v>^<v^v>^<<>^^vvv^<v><^v^^<<^>^^<v>v<v<^<<>vv><^><>v>>><><<<><v^^^><v^<v<<<><^>vv<<<^v<<>><>^>>>>><v>>^<v<^<v^>>^<>>>v>><^^v<><<><vv<^<^^>><^>^vv<<<v<<^><^><<^^v^^^^<^v><v>><vv<<v>v>^^>^^>>v>^^vv>v<^^^<v^<<<^v^^>vv<^^<^>vvv>^<v^>^^vv<<v^<<>v^>vvvv>^v>><^>v<vv>^>><^^^<vv<>v^^>^^>^v<><<<^>>vvvvv<<<<v>>v^><>^^v><<v><v^v^^v><v^<<^v^<^>v<v<<<<>vv>^^<^<<^<>^><^<v<^v^>^>>v>v^>><v<><>^^vv<^><v^<<<^v^^>v<>>^>>>v<>^^v>v<<v>^^<^^v<<>vvv<v<>><^vv^v>>v^^<v>^v^vv<v^>vv^^<v<>^<><<<>>v^v>^<<vvv>^^><^>v><>v^^^^><^<>^v^^>><v^^v^^v<^<^^^v^vv>^v^vv^v^^v^^<^<^^^><^>v<vv<><>^^>v<^^^v<<<^^vvv<v>v^<^<<<v^<>>>^<>vv<>v^<>^^^v<>vv<^><v^<^>^^^<v<<>^<<v^><<<<v>v><v^>v>^v<vvvv<^<<vv<^<>^^<vvvvv^<<^<vvv><vv<^^v<<><^<>><v<>v>v><vv>>v><v>^<v>>><<<^^>><v^^^vv><^v<v><<vv^vv^vv<^>^><>>v^^<>>>>v>vv^<v<>^v>v>^>^>v<<>v^v>vv^^>v^v><<^>>v><>v>>^<v^^>v><^<<<>><v>^><>^<vv>^^<^<v>^v^v^vv<^>>v<^^^><<^><vvv><^v><<<<<>vv<<\n"
                                        + ">^^v^v^<v^<<v^^^<vv<>v^<>><v>><vvv^^vv<^<><^^vv^^^<v<>^><v<<^v<<<>>>^><<<><<vv>^<><v^^<<<^<<^v<vv<^^^>><v^^>>><^><vv>vv>>^<v^>>^>v>>>v^>v^>^<v>>><v>><v<vv^v>vvv<>>^<<<>^^^^<<<>>>>vv<v<^><<>^v<v^<>>v^v^>^<^>v>><vv^>v><v<<^v>^^v>v<v<<<^^<<>>v>^<^^<<>>^^<^<v>^>vvvv<v^>>vv^<^>^<>><<<<^v><vv<vv<v^><^<>>^>v>v>>><v<>v^<^<^>^<><^>>^^v^vv<>vv><^v<>><<><^<vvvv><^v^^v>>>v<<v<v^>^<>^<<><v<^<<<^<><<>>><^^^v^>>v>><<vv^^^<<^<>v>^^^v<<v><<<^>v^>^v<^v<<<>vv^^>>^>>vv<vv>^^<<v^><^<>v<^^v<v<v<v>>^>><<^<><>vv<v<v<>^>>>>v>v<<^^<<^vv>v<>v<><<<><^^^<<>v>>^^v<>vv^^<vv>^>^>>><>>^<<>^^>>^vv>v>^v<^<^vvv>v<<v<vv^<><<v^^>v>v^vv<<vv^<<^><v<^>^v><v><v^<>v^><<^^<v^><>v<<v<<><>v>v>>>>><>^<>v>^^><<>vv<^<<>v^>v^>>>^><><<<><>vv^v^><v>^^^v^v>>^v^>^>^<>>^v>^<><vvv><<<^v>><vv>>v<>>^v<^>>^vv><>v>><^^>v<<<v<<<vv>v>v<>^v^<<<vv^<^^v<^<vvvv<v<^<^><vv<>vv^>^v^<>>v<v>><>>><^^^><>vv<<<^^v>^>^v<vv^^v><v<<>>v>vvv<><^^>^>^<<vvvv<v><v^><>v^>v>vv<v<><vv>^<v<^v<vvvvv>^^<^^^<v>^vv^><<v>^vv>^>>^^^v<><>vvv<>>vv^<><><vv<^<>v><>v>^>^<v<<>>v>>^";

    public static void main(String[] args) {
        Util.time(() -> a(TEST_INPUT));
        Util.time(() -> a(INPUT));
        Util.time(() -> b(TEST_INPUT_2));
        Util.time(() -> b(INPUT));
    }
}
