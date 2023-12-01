package aoc2022;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day17 {
    public static void main(String[] args) {
        a(TEST_INPUT);
        a(INPUT);
        b(TEST_INPUT);
        b(INPUT);
    }

    private static void a(String input) {
        Data data = new Data(input);
        Set<Pos> blocked = new HashSet<>();
        IntStream.range(0, 7).mapToObj(x -> new Pos(x, 0)).forEach(blocked::add);
        int currentJet = 0;
        long maxHeight = 0;
        int spawnedShapes = 0;
        Shape current = data.spawnShape(spawnedShapes++, maxHeight + 4);
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if (i % 2 == 1) {
                if (data.stuck(current, blocked)) {
                    for (Pos position : current.positions) {
                        maxHeight = Math.max(maxHeight, position.y());
                        blocked.add(position);
                    }
                    if (spawnedShapes == 2022) {
                        System.out.println(maxHeight);
                        return;
                    }
                    current = data.spawnShape(spawnedShapes++, maxHeight + 4);
                } else {
                    current = current.move(Move.D, blocked);
                }
            } else {
                Move move = data.jet(currentJet++);
                current = current.move(move, blocked);
            }
        }
    }

    private static void b(String input) {
        long limit = 1_000_000_000_000L;
        Data data = new Data(input);
        Set<Pos> blocked = new HashSet<>();
        IntStream.range(0, 7).mapToObj(x -> new Pos(x, 0)).forEach(blocked::add);
        int currentJet = 0;
        long maxHeight = 0;
        long spawnedShapes = 0;
        Set<Pos> currentRound = new HashSet<>();
        Map<Set<Pos>, SimpleEntry<Long, Long>> seenToHeightAndNumSpawned = new HashMap<>();
        Pos relativePos = new Pos(0, 0);
        Shape current = data.spawnShape(spawnedShapes++, maxHeight + 4);
        long extraHeight = 0;
        long extraSpawn = 0;
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if (i % 2 == 1) {
                if (data.stuck(current, blocked)) {
                    for (Pos position : current.positions) {
                        maxHeight = Math.max(maxHeight, position.y());
                        blocked.add(position);
                        currentRound.add(new Pos(position.x() - relativePos.x(), position.y() - relativePos.y()));
                    }
                    if (spawnedShapes + extraSpawn == limit) {
                        System.out.println(maxHeight + extraHeight);
                        return;
                    }
                    if (spawnedShapes % (data.shapes.size() * 2L) == 1 && extraSpawn == 0) {
                        SimpleEntry<Long, Long> heightAndNumSpawned = seenToHeightAndNumSpawned.get(currentRound);
                        if (heightAndNumSpawned != null) {
                            long heightDiff = maxHeight - heightAndNumSpawned.getKey();
                            long spawnDiff = spawnedShapes - heightAndNumSpawned.getValue();
                            long x = (limit - spawnedShapes) / spawnDiff;
                            extraHeight = x * heightDiff;
                            extraSpawn = x * spawnDiff;
                            if (spawnedShapes + extraSpawn == limit) {
                                System.out.println(maxHeight + extraHeight);
                                return;
                            }

                        }
                        seenToHeightAndNumSpawned.put(currentRound, new SimpleEntry<>(maxHeight, spawnedShapes));
                        currentRound = new HashSet<>();
                        relativePos = new Pos(0, maxHeight);
                    }
                    current = data.spawnShape(spawnedShapes++, maxHeight + 4);
                } else {
                    current = current.move(Move.D, blocked);
                }
            } else {
                Move move = data.jet(currentJet++);
                current = current.move(move, blocked);
            }
        }
    }

    record Shape(Set<Pos> positions) {

        static Shape parse(String s) {
            Set<Pos> positions = new HashSet<>();
            String[] rows = s.split("\n");
            for (int i = 0; i < rows.length; i++) {
                int y = rows.length - i - 1;
                for (int x = 0; x < rows[i].length(); x++) {
                    if (rows[i].charAt(x) == '#') {
                        positions.add(new Pos(x, y));
                    }
                }
            }
            return new Shape(positions);
        }

        public Shape move(Move move, Set<Pos> blocked) {
            Set<Pos> newPositions = new HashSet<>();
            int xDiff = move == Move.L ? -1 : move == Move.R ? 1 : 0;
            int yDiff = move == Move.D ? -1 : 0;
            for (Pos pos : positions) {
                Pos newPos = new Pos(pos.x() + xDiff, pos.y() + yDiff);
                if (newPos.x() < 0 || newPos.x() >= 7 || blocked.contains(newPos)) {
                    return this;
                }
                newPositions.add(newPos);
            }
            return new Shape(newPositions);
        }
    }

    private enum Move {
        L, R, D
    }

    private static class Data {
        final List<Move> jets;
        final List<Shape> shapes;

        private Data(String jetsInput) {
            jets = jetsInput.chars().mapToObj(c -> c == '<' ? Move.L : Move.R).toList();
            shapes = Stream.of(SHAPE_INPUT.split("\n\n")).map(Shape::parse).toList();
        }

        public Shape spawnShape(long shapeIx, long y) {
            Shape type = shapes.get((int) (shapeIx % shapes.size()));
            return new Shape(type.positions.stream().map(p -> new Pos(p.x() + 2, p.y() + y)).collect(Collectors.toSet()));
        }

        public Move jet(int ix) {
            return jets.get(ix % jets.size());
        }

        public boolean stuck(Shape shape, Set<Pos> blocked) {
            for (Pos pos : shape.positions) {
                Pos below = new Pos(pos.x(), pos.y() - 1);
                if (blocked.contains(below)) {
                    return true;
                }
            }
            return false;
        }
    }

    public record Pos(int x, long y) {
    }

    private static final String SHAPE_INPUT = "####\n"
                                              + "\n"
                                              + ".#.\n"
                                              + "###\n"
                                              + ".#.\n"
                                              + "\n"
                                              + "..#\n"
                                              + "..#\n"
                                              + "###\n"
                                              + "\n"
                                              + "#\n"
                                              + "#\n"
                                              + "#\n"
                                              + "#\n"
                                              + "\n"
                                              + "##\n"
                                              + "##";

    private static final String TEST_INPUT = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>";

    private static final String INPUT =
            ">>>><<<><<<>><<>>>><>>><<<>><<<>><<<<>>>><>>><<>><<<>>>><<<<>>><<><>>><<<<>>>><>><<><<<><<<><<>><><><<<<>>><<<<><<>>>><<>>>><>><<<<>><<<>><<<<>>><<<>>>><<>>><>><>>><>><<>>>><<><><<<>>><<><><<<<>><<<>>><<<<>>>><<><<>>><<<>>><<<><<<<>>>><<<>><><<<>>><<<>>><<<<><<<>>>><<>>><>>><<<<>>>><>><<<<>><<<>>>><<>><>>>><<<>>><<>><<<>>>><>>>><><<>>>><<<>><<>><<>>>><<<>>><<>>><<<<>>><<<<>>>><<<<>>>><>><<<>>><<>><>>><>>><<>>><>>><<<>>><<<<>><<><<<><>>>><<<><>>>><<<<>>><<<<>><><<<<>>><<><<<<>>><<<<>><>><>>><<<><<<<>>>><><><<<>><<<<>><<<>><<<><<<<>>>><<<<>>><<<>>><>><<<<><<<>><<<>><>><<<<><><>>><<>>>><>>><>>><<<<>><<>><<>><<<>><<><<<<>><><<<<>><<>>>><<>>>><>>><<<>><<<<>><<<>>>><<<<>><<<<>>><<>>>><>>><<<<>>><<<<><<><<<><<<<>><<<>><<<><<<>>><<<<>><<<>>><><<>>><>>>><<<<>>>><<<>>><<>><><>>><<<><<>><>><<>>>><<<<><<>><<<<><<>>><<<<>><<<>>><<<>>>><>><<><<<><>><<<>>>><<<<>>><><<<<><<><<<>><<<<>><<<>><>>><<><<><<>>>><<>>>><<>>>><><<>>>><<<<>>><<<>>><<<>>><<<<>>><<<>><<<<><><><<<>><<<<>>>><<<<>>>><<<>>><<>>>><>>>><<<<>><<<><<<>>>><><><<><<<>>><<>>>><>>><>>><<>><>>>><<<>><><<<><<<<>>><<<<><><><>>><>><>>>><>>>><<<<>><<<>>><<<>>>><<<>><<>>>><<<><<<<>><<><<<>>>><<<<>>><><>>><>>>><<<<>><<><<<>>><<>>><<>><<><<>><>>><<<>><<<<>><>>><<><<>><<<<>><<<>><>>>><<<>>><<>><<><>>><<<<>>>><<<<>><<<>>>><><<>><<>>><><<<<>>>><<<<>><>>>><<<>>><<<>>>><<<<>><<><<<<>><><<>>><<<<>>>><<<<>>><<><<<>>><<<<>>><<<<><<<<>>>><<>>>><<>>><<>><<<<><<<>><<<>>>><<<<>>><<<<><>>>><<<<>>>><<<><<<>>>><<>>><<<<>>><<<<><<<>><<<<><>>><>>><<<<>>><>><<>>>><<<>><<><<<>>>><<<>>>><<<>>><<<<>>>><<>>><<<><<<<>>><<<>><<<<><><>><<>>>><<>><<><<>>>><<<>>>><<<<><>><>>><<>>><><<<>>>><<>><<><<<<>>>><<<>>><<<<>>>><<<>><<<<><<<<>><<><>>><>><<>>>><<<<>>>><<<>>><><<><<><>>>><<>>>><<<><>>><<<>><<<<>>><><<<><<>>>><><<>>><<<<>><<>>><<>>>><<<<>>>><<>>><><>>>><<<<>>><<<<>>><<<<>>>><<<>><>>><<<<><<<>><<<<>>><>><<>><<<<><<<><<>>><>>><<<>><<<>>><<>><<><>><<<>>>><>>><<<<>><<>>>><<<>>><<><<<>>>><<<><<<>>>><<<<>>><>>><>><<<>>>><<<<>>><<<>>>><<>><<<<>>>><>><>>><<<>>>><>>>><<>><<<><><<>>><<<>>>><>>>><>>><<><<<<>>>><<<>>>><<<>>><<<<>>><>>>><<<>>>><<<><<<>><<<<><<<><<<<><<<>>>><>><>><<<>><<<><<<><><<<>>><<>>>><<<<>>>><<>>>><>><<<>>><<><<<<><<<<>>>><<>>><<<><>>><>>><<<>>><>><<<<><<<<>>>><<>>>><>><<<<><><><<><<>>>><<>><>><>>>><<>>>><<<<>>>><>><>>><<<>>><<>><<<>>><><<<><<<>><<><<>>>><>>>><<>>>><><<<>>>><<<>>>><<<>>><<>>>><<>>>><<<>>><<<<>>>><<<<>><>>>><<>>><<<>>><<>>>><<<<>><<<>><<><>>>><<<>>><<>>>><<<<>>>><<>><<<>>><>><>><>>><><<>><<<>><<><<>>><>>>><<><>>><<<<><<<<>><>><<<<>>><<<>>>><>><<<>>><<<<>>>><<<>>>><<>><<><<<>>><><>><>><>>>><><<<>>><<>>>><<<<>><><<>>>><<<>>><<>><<<>><<<><<>><<>>><<>>><>><<>>>><<><<<<><<<<>>><<<<>>>><<>>><<>><<<><<<><<<>>><<<<>><>>><<>>><<>>>><<>>>><<>>>><<<>><><<<<><>><<><<<>><<<<>>>><<<<>>>><<<><<<>><<<>>><>><>>>><<<<>>><<<>>><>>>><<<>>>><>><<<><<><<<>>>><<<>>><>>><<<>>><<<><<>>>><<<>><<><<>><<<><<<<>>><<<>><<>>><<<<>><>>>><<>>><<><<<<>><><<>>>><<<><<>><<>><<>>><<<<>>>><<<<>>><<<><<<<>>><<<>>><>>><<>><<<<><>>><<<<>><<<<>>><<><<<<>>><<<>>><<<<>><<<<>>>><><><<>>>><>><<<><><<<<>>><><<<<>>>><><><<<>>><>><<<<><>><<<<>>>><<>>>><<<>>><>>><>>><><>>>><<>><<<>>>><<<>>>><<<>>>><<<<>>>><<<><>><<<<>>>><<><<<<><<<>>>><<><>>>><<><<<>>>><><<<><<>><<<<>><<>><>><<<>><<<>>><>>>><>>>><<><<<>><<<>>>><<<<><<<>>><>><<>>><<>>><<<>>><<>>><>><<>>><<<<><<>><<<<>><<<>><>>>><>><><<<>><<<>><>>><<<<><<<>><<>><<<<>>>><<<<>>><>>><<<>>>><<<>>>><<<>><<<>>>><<<>><<<>><<<<>>><><<>><<<<>><>><<>><<<>><<<>>><<<<>>>><<<><<<<>>>><>>><<<<>>><<>>>><<<<>>><>>>><<<>><>>>><<><<>>><>>>><<>>>><<<>><<<><<<<>>><<<>>><<<>>>><<><>>>><<<>>>><<<<>>>><>>><<<><>><<>>>><<<<>>><>>><<<<>>><<<<>><<<<>><<<>><<>>><<<>>>><><<>>><>>>><<>>><<<<><<><<>>>><<<<>><<<<>>>><<<>><<><<>><<<<>>>><>><<<<>><<<>>><>>>><<<>>>><<><<<<><<<>><<<>>><<<<>>><<<>>><<<>>>><<<>>><><<<>>><>><<<>>><<>>><><<<>>>><<<<>>>><<<><<<<>>><<><<<>>><<<<>><<<>><>>>><<<>><<><<<<>>>><>><<<<>><<<>>><<>>><<<>>><<<>>><<<<>>><<<<><>>><>><><<<<>><<<>>><<<<><<<>>><<>><>><<>>>><<>>>><>>>><<><<>><>>>><<><<<<><<<<>>><>><<>>>><<<<>>><>>><<<<><<>><<><<<>>>><<>>>><<>>>><<<<><<>><>><><<<>><<<<>>><<<><<<>><>><<<>><><>>>><><<<>>><<<<><<<<>>><<<>>>><>>><<>>><<>>>><>>>><><<<<>>>><<<>>>><<<>><<>>><<><<<>>>><<<>>><<<><><>><<<>>><<<>>>><<>><<<<>><><<<<>><<<>><<<<><<>>>><<>>><<>><<>>>><<>>><<>>><<<<>>>><>>><<>>><<<>>><>>>><<<><<<<>><<><<<<>><<<>><<<>>><<<<>><>><<>><<<>>>><<>>><<<>><<>>>><<<<>><<>>>><<<>>><<<<><<<>>>><<<>>><<<>><<><>>>><<<<><<<<>>><<<<>>>><>>><><>>>><<<>>><<<<>>>><>>>><<<>>>><<><<<<>>><<>><<<<>><<<><<><<<>><<<>>><<<>>><<>>>><<<<>>><<>>><<>><><<<<>>><<<<>>>><><<><>><<<>>>><<<><<>>><<>>>><<<>>><>>>><>><>>><<<><>><<<<>>><>>><>><>><<<<>>><<>>><>>>><<<>><<>>><<<>><<<>><<<<><<>>>><>>><>><<<<>>>><><<<><<><<>>><<><<<>>>><<>><<>>><<<>>>><<>>>><<><<>>><>>>><<>><<<>><><>><<<>>>><<>><>><<>>>><<<<>>>><<<<>>>><<<><<>><<<<>>><<<<>>><<<<>>><<<<>><<<<>>><<<>><<>>><<<<>>><<>>>><<<>><>><>>><>>>><>><<<<><><<>>>><>>><<<>>>><<<>>>><<>>><<<<>><>>><<<<>>>><><<<<><<>><>>>><>>><<>>>><><<><<<<>><<<><><<<<>><<>>>><<<<>><>><<>><<>><<><<<>>><><>>><<<>>>><<<<><>>>><><>>>><<<<>>><<>><<>><<<<>>>><<>>>><<<<>>>><><<<><<>>><<<<>>>><<>>>><>>>><<<<>><<<>><<>>><<<<>>>><<<<>>>><<<<>><<<<><>>>><>>><<<<><<<<>>>><<<><<<<><<>>><>>>><<<>>><<<<>>><<>>>><<>>><<<<><<<<>>>><>>>><<<>><<<<>><>>>><>>><<<<>>>><>>>><<>>><<<>>>><>><<<<>>><<>>><<><<<><<<<>><<<<>><<><<<<><<<>>><<>>>><<<><<<<>>><<>><<<<>>>><>><<>>><<<<>>>><<<>>>><><>>><>>><<<<><>><<>>>><>>>><<>>>><<<<><>>>><<<>>>><<<<>>>><>><<<<><<<<>><<<>><<>>><<><<>>><<>><>>>><><<<>>>><<<<><<<>><<<<><<><>>><<>>>><<><<<<>><><<<>>><<<<>>><<<<><<<<>><>>>><<><<<>>><<>><<<<>>>><>>>><>>>><>><<<<><<<>>>><<<>><<<>>>><>>>><<<<>>>><<<>>>><<><<><<>><<><<<>>><<<<>><>>><<><<<<>><<<<>>>><<<<>>><<>>>><>><<<<>><><<<<>><<>><<<<>>>><<>>>><<<<>><>>><<<>><<><>>><<>><<<><<<<>>><>>>><<>><<>>><<>><<><<>>><<<<><<>>>><<>>>><<>><>>>><<>><>><>><<<<>>>><>>>><<<>>>><<<<>>>><<><<>>><><<>>><>>><><<<>><<<<>>><<<><<<<>>>><<<>>>><><>>><<<><<<>>><<<<><<<<>>><<>><<>><><<>>><<<><<<<>><>><<><<<>>><<>>><<>>>><>><>>><<<>><>>>><<<<><<<<>>>><><>>><>><><<<>>>><<<<>><<>><><>>><<<><<><<>>>><<<>>><>>>><<<<>>>><>><<<<><>>>><>>>><<<>>><<><<>>>><<>>><<<>>><<<>>><><<<>><<<<>>>><<>>>><<<<>>><<<>>><<<<><<>><>>>><>>>><<><<<<>><<<<>>><<<<>>><>>><><<>><<<><<<><<<>><<>>>><>><>>><><<<><<><>>>><<<<><<<>>>><><<><>>><<<<>>><<<>><<<<>>><<>>>><>>><>><<>><<<<>><<><<>><>>>><<>>><<<>><><<>>>><<>>><<>><<>><<<>>><<<>>>><<<<><<<<>>><<<>><>>><<<>><<<>>><<<>>>><<<<>><<<<>>><<><<<<>>><><<>>>><<<><<<<>>><<<<>>><<<>>>><<><<<<>>>><>>><<><<<<>>><>>>><<<<>>><><<<>>><>>><<>>>><<<>>><<<<>><<<<>><<>>><<<<>><>>>><<<<>>>><<<<>>><>><<>>>><<<<>>>><<<<>><<<>>><<<>>><>>><>>>><>><><>><<><<><><<>><>>><<<><<<>><<<>><<<>>><>><<>>>><<>><<<>>>><><<<><<>><<<<><<<><<>>>><>><<><<>><<>>><<<><>>><<<>>>><<<<>><<>>>><<<>>>><><>>><><<<<><<>>><>><<<><<<>>>><><>>>><>>>><<<<>>><><<<>><<<>>><<>>><<<><<<<>><<>>>><<<>>>><<>><<><<<>>>><<>>><<<>><><<><<<<>>><><>>>><<>><><<<<>>>><>>>><<<<><<>>>><<>>>><<<<><>><<<>>>><<<<><<<<>>><<<>><<<<><<<>>><>>>><<<<>>><<<>>><<>>><<>><<<><<<>>><<<>>>><<<><<<<>>><<<>><>>>><>><<<<>>>><><<<>><<>>>><<<<>>><><>><<>><>>><<<>>><>>>><<<<><><<<<>><<>>>><<<<>>><<><<<<>>><<<>>>><<<<><>>><>><><<>><<<><>>><<<><><<>><<<>><<<<><<<>>>><><><<<<><<<>>>><>>>><<>>><<>>><<<>><<>>><<<><<>>>><><<><><>>><<<>><<<<><<<><<>><<><<<>>>><<>>>><<><>>>><<>>><<<<><<<<><<<<>><<<<>>>><<><<<><>>><<<<>>><<<>><<<<>>>><<<>>>><<<>><<><><><><<><<<<><<<>><<<>><<<>><>><<<<>><<>>><<><<>>><<<><<<<>><<<>>>><<>>>><>>><<><>><<<<>>><>><<<>>><<<><<>>>><<<<>>><<<<>>><<<<>>><>>><>>><>><<>>>><<<><<<><<<>><<<>><<<>><<<>>><>>><<>>><<<<><>>><<<<><<<>>><<<>>>><<<<>>><<>><>>><>>><>><<<<>>><<<<><><<<><<>><<<>>>><<<>>><<><>>><<>><>>><<<>>>><<<>>>><<<><>>>><<><<><<<<>>><<<<><<><<>>>><<<>><<<<>>>><<<<>>>><><<<<>><<>>><<<<>>>><<>>><<<>>><>>><>>>><<<>><><<>>><<<>>><<><<<><<<<><<<<><>><<>>><<<<>>>><<<><<><<<<>>>><<<>><<<<><<>><<<><<<>><<<>>>><<<<>>><<<>><<<<><<<>>><<<<>><<>>>><<<>><<><<<<><<<>><>>><>>><<>>>><<<<><<<><<<<>><<<<>>>><><>>><<>>>><<>>><>><<>>><<<>>>><<<>><>>><<>><<><<<>>><<<><<<>>>><<>>>><<<<>>><<>>>><<<>>>><<><>>><<>>><<>>><<<>>><><<><<<<>><<>><><<<>>>><<<>>><<<><><<<<><<<<><<>>>><>>><>>><<<>>><<>>><<<<>>>><<<><<><<<<><<<><<<<>>><>>>><>>>><<<>>><<<<>>>><<<>><<>><<>>><<>><><<>>><<<>>><<>><<<<>><<<>>><<>><<<<>><>><><>>><><<<<>>>><<>>>><<<>><><<<<>><<<<><<<>><<<<>><><<<><<>><<<>>>><>><<<>>><><<<<>><<>><<>>>><<>><<<<>><<<<>><<>><<<<><<<<>>><<<><<<<>>>><<><>><<<<><>>><<<<><<><<<<>><>><<><<<>>>><<<<>>>><<<<><<>>>><<<<><<<><<>><>><<<>>>><>><<>>><<<<><<>>><<><<<<>><<><<<>><>><<<>>>><<<<><<<<>><>><<<>><<<>>><<>>><<<<>>><<>>><<<<><<<<>><<<<>>><<<>><><<<><>><<<<><<<<>><<<<><<<>>>><<><<><>>><<<<>><<<<>>>><>>><>><<>><<>><<<<>><>>>><<<<>>>><<<>>>><<>><><<<<>>><<<<>>>><<<>>><<><>>><<<<>>>><<<<>>>><<><>>>><<<<><<<><<<<>>><<<<><<<>>>><<<<>><<<>>><<<>>>><<><<><>>>><>>>><<<<>><>><<>>><<<><><<>>><<<>><<>>><<<><<<>>><<>>><<>>>><<<><>>>><><<<>>><>><<<>><<><<<>>><>>><<<<>>>><<<>>><>>><<>><<<>>><>><<>><>>>><<<<><<<>>>><><<<<><<<<><<<<><<>>>><<>>>><<>><<<<>><<<>>><<><<<>>>><<<<>>>><<<>>>><<<><<<>>>><<>>>><<<<>><>><<>>>><>>>><<<>><<<>><>>>><<<>>><>><>>>><>><<<<>><<>>><>><<>>><<<<>><><<<>><<<<>>><>>>><<<<>>><<<><<<>><<><<>>>><>>><<<><<>><<<><<>>>><><<<<>><<<>>><<>>><>>>><>>><>>>><<<><>><<<>>>><>>><<<>>><<><<>><<<<><<><>><<<>>><<<>>><<<>>>><<<><<<<>>><<<<><<<><<<>>>><<>>><>>>><<<>><<>>>><<><<<<><<<<>><<>>><<><<>><<><>>>><>><<<<>>><<<<>>>><<<><<<<>>>><>>>><<<><<>><<>>><<<<>><<>><<>>>><>>><>>><<>><>>><<>><<<<>>><><<>>><<<<>><<<>>><<<>><><<<<>><<<<>>><<<>><<<>>><>>>><<<>>>><<>>><>><>><<<<>>>><<<>>>><<>><<<<>>><<<<>>><><>><<><>>>><<<<>><<>>><>>>><<<><<<<>>>><>>><<<<>>>><<><<<<>>><<<<>>><<<<>>>><><<>><<>><<<>><<><<<>>>><<<>>>><<>><<<<>><>><<<>>>><<<<><<<>>>><<<>>><<<<>>>><<>>>><<<><<<<>>>><><>><<>>><<<<><><<>>>><<<>>>><<<><<<<>><<<<>>><<<><<<>><<><<<>>>><<>>><>><>><<>>>><><<><<<<>>><<>>>><<<<>>><<<<><><<<><<<>>><<><<>>>><<<>>><<<>><<>>>><<<<>>>><>><<>>><<>>>><<<><<>>>><<<>>>><<<<>>>><<<<><<<<><<<><<<<>>>><<>>><<>>><<>><<><<>>><<<>>>><>><<<><<>>>><<>><<><<<>>>><>><>>>><<>>>><><>><<<<><<<<>>><<<><<>><<>>><<<<>>>><>>>><<><<<>>><<<>>>><<>>><<>><<>";
}