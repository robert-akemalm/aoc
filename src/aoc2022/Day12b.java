package aoc2022;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import aoc2022.Util.Pos;

public class Day12b {
    public static void main(String[] args) {
        a(TEST_INPUT);
        a(INPUT);
        b(TEST_INPUT);
        b(INPUT);
    }

    private static void a(String input) {
        Map<Pos, Character> map = parse(input);
        List<Pos> startPos = map.entrySet().stream().filter(e -> e.getValue() == 'S').map(Entry::getKey).toList();
        System.out.println(explore(map, startPos));
    }

    private static void b(String input) {
        Map<Pos, Character> map = parse(input);
        List<Pos> startPos = map.entrySet().stream().filter(e -> e.getValue() == 'S' || e.getValue() == 'a').map(Entry::getKey)
                                .toList();
        System.out.println(explore(map, startPos));
    }

    static int explore(Map<Pos, Character> map, List<Pos> startPos) {
        Map<Pos, Integer> minDist = new HashMap<>();
        Set<Pos> toExplore = Set.copyOf(startPos);
        for (Pos start : startPos) {
            minDist.put(start, 0);
        }
        while (!toExplore.isEmpty()) {
            Set<Pos> nextToExplore = new HashSet<>();
            for (Pos pos : toExplore) {
                int numStepsToNeighbour = minDist.get(pos) + 1;
                char posHeight = map.get(pos);
                posHeight = posHeight == 'S' ? 'a' : posHeight == 'E' ? 'z' : posHeight;
                for (Pos neighbour : pos.neighbours()) {
                    Character neighbourHeight = map.get(neighbour);
                    if (neighbourHeight != null) {
                        neighbourHeight = neighbourHeight == 'S' ? 'a' : neighbourHeight == 'E' ? 'z' : neighbourHeight;
                        int diff = neighbourHeight - posHeight;
                        if (diff <= 1 && minDist.getOrDefault(neighbour, Integer.MAX_VALUE) > numStepsToNeighbour) {
                            nextToExplore.add(neighbour);
                            minDist.put(neighbour, numStepsToNeighbour);
                        }
                    }
                }
            }
            toExplore = nextToExplore;
        }
        return map.entrySet().stream().filter(e -> e.getValue() == 'E').map(Entry::getKey).findAny().map(minDist::get).orElse(-1);
    }

    static Map<Pos, Character> parse(String input) {
        Map<Pos, Character> heights = new HashMap<>();
        String[] lines = input.split("\n");
        for (int row = 0; row < lines.length; row++) {
            for (int col = 0; col < lines[row].length(); col++) {
                heights.put(new Pos(col, row), lines[row].charAt(col));
            }
        }
        return heights;
    }

    private static final String TEST_INPUT = "Sabqponm\n"
                                             + "abcryxxl\n"
                                             + "accszExk\n"
                                             + "acctuvwj\n"
                                             + "abdefghi";

    private static final String INPUT =
            "abccccccccccccccccccccaaaaaaaacccccccccccccaacaaaaacccccccccccccccccccccaaaaaacccccaaaaaccccccccccccccccccccaaaccccccccccccccccccccccccccccccccccccccccccccccccccccccccccaaaa\n"
            + "abcccccccccccccccccccaaaaaaaaacccccccccccccaaaaaaaaccccccccccccccaacccccaaaaaaaaaaaaaaaaccccccccccccccccccccaaaccccccccccccccccccccccccaccaccccccccccccccccccccccccccccaaaaaa\n"
            + "abccccccccccccccccccaaaaaaaaaacccccaacccccccaaaaaccccccccccccccaaaaaacccaaaaaaaaaaaaaaaaccccccccccccccccccaacaaaaacccccccccccccccccccccaaaaccccccccccccccccccccccccccccaaaaaa\n"
            + "abccccccccccaaacaaacaaacaaacccccacaaaccccccccaaaaacccccccccccccaaaaaacaaaaaaaaaaaaaaaaaaccccccccccccccccccaaaaaaaaccccccccccccccccccccaaaaaccccccccaaaccccaaaccccccccccaaacaa\n"
            + "abccccccccaaaaaccaaaaaccaaaccccaaaaaaaacccccaaacaaccccccccccccccaaaaccaaaaaaaaccccaaaaaacccccccccccccccccccaaaaaccccccccccccccccccccccaaaaaacccccccaaaacccaaaccccccccccccccaa\n"
            + "abccccccccaaaaaaccaaaaaaaaaaaccaaaaaaaaccccccaacccccccccccccccccaaaacaaaacaaaacccccaaacccccccaacaaccccccccccaaaaacccaaccccccccccccccccaaaaaaccccccccaaaaaaaacccccccccccccccaa\n"
            + "abccccccccaaaaaaaaaaaaaaaaaaacccaaaaaaccccccccccccccccccccccccccaccaccccccaaaaaccccccccccccccaaaaacccccccccaaacaacaaaaaaccccccccccccccccaacccccccckkkkkkaaaaccccccccccccccccc\n"
            + "abccccccccaaaaacaaaaaccaaaaaaaacaaaaaccccccccccccccccccccccccccccccccccccccaaaccccccccccccccccaaaaacccccccccaaccccaaaaaaccccccccccccccccccccccccckkkkkkklaaccccccccaacccccccc\n"
            + "abccccccccaaaaacaacaaacaaaaaaaacaaaaaacccccccccccccccccccccaacccccccccccccccaaaccccccccccccccaaaaaacccccccccccccccaaaaaacccccccccccccccccccccccckkkkkkkklllccccccccccaaaacccc\n"
            + "abcaaccccccccccccccaaaccaaaaaaaccccaaccccccccccccccccccaaccaacccccccccccccccccccaacccccccccccaaaacccccccccccccccccaaaaacccccccaaaacccccccccccccckkkoppppllllllccccccccaaccccc\n"
            + "abcaacccccccccccccccccccaaaaaccccccccccccccccccccccccccaaaaaacccccccccccccccccaaaaaacccccccccccaaccccccccccccccccccaaaacccccccaaaaacccccccccccckkkooppppplllllllllccccdaccccc\n"
            + "abaaaccccccaaacccccccccaaaaaaccccccccaaaccccccccccccccccaaaaaaacccccccccccccccaaaaaacccccccccccccccccccccccccccccccccccccccccaaaaaaccccccccccccjkoooopuppplllllllmmmddddacccc\n"
            + "abaaaaaccccaaaaacccccccccccaaaaacccccaaaaccccccccccccccccaaaaaaccccccccccccccccaaaacccccccccccccccccaaaccccccccccccccccccccccaaaaaacccccccccccjjjooouuuuppppppqqmmmmmdddacccc\n"
            + "abaaaaacccaaaaaacccaacaaacccaaaaaacccaaaacccccccccccccccaaaaaccccccccccaaccccccaaaaccccccaacccccacccaaccccccccccaacaaccccccccaaaaaacccaaaccccjjjjoouuuuuuppppqqqqqmmmdddacccc\n"
            + "abaaccacccaaaaaacccaaaaaacccaaaaaacccaaacccccccccccccccaaaaaaccccccccaaaaaaccccaccacccccaaaacccaaaaaaaccccccccccaaaaaccccccccccaacacccaaccccjjjjooouuuxuuupppqqqqqmmmdddccccc\n"
            + "abaaaccccccaaaaacccaaaaaacccaaaaaccccccccccccccccccccccccccaaccccccccaaaaaacccccccccccccaaaaccccaaaaaaaaccccccccaaaaaaccccccccccccaaaaaaacjjjjjoooouuxxxuuvvvvvvqqqmmdddccccc\n"
            + "abaaaccccccaacaacccaaaaaaacccaaaaacccccccccccccccaaacccccccccccccccccaaaaaccccaaacccccccaaaaccccaaaaaaaaacccccccaaaaaaccccccccccccaaaaaacjjjjjoooouuuxxxuuvvvvvvqqqmmdddccccc\n"
            + "abccccccccccccccccaaaaaaaacccaaaaacccccccccccccccaaaccccccccccccccccccaaaaacccaaacacccccccccccccaaaaaaaaacccccccaaaaaccccccaacccccaaaaaaajjjnoooottuuxxxxvyyyvvvqqmmmdddccccc\n"
            + "abccccccccccccccccaaaaaaaacccccccccccccccccaaaaaaaaaccaaccccccccccccccaaaaacaacaaaaaccccccccccccaaaaaaaaccccccccccaaacccccaaaaccccaaaaaajjjnnnntttttxxxxyyyyyvvvqqmmmdddccccc\n"
            + "abccccccaaaccccccccccaaacccaaccccccccccccccaaaaaaaaaaaaaaaccccccccaaacccccccaaaaaaaacccccccccccaaaaaaaaccccccccccccaacccccaaaaacacaaaaaaiiinnntttxxxxxxxyyyyyvvqqqmmdddcccccc\n"
            + "SbccccccaaaaaccccccccaaccccaaaccccccccccccccaaaaaaaaaaaaaacccccccaaaaaaccccccaaaaaccccccccacccaaaccaaacccccccccaaacaaccaaaaaaaaaaaaaaaaaiiinnntttxxxEzzzzyyyvvqqqmmmeeecccccc\n"
            + "abcccccaaaaaacccccccccccaaaaaaaaccccccccccccaaaaaaaaaaaaaccccccccaaaaaacccccccaaaaacccccccaacaaaccccaaacccccccccaaaaaccaaaaaaaaaacaccaaaiiinnntttxxxxxyyyyyvvvqqqnnneeecccccc\n"
            + "abaacccaaaaaacccccccccccaaaaaaaaccccaaaccccaaaaaaaaaaaaaaacccccccaaaaaccccccccaacaaaaaccccaaaaacccccccccccccccccaaaaaaaccaaaaaacccccccaaiiinnnttttxxxxyyyyyyvvvrrnnneeecccccc\n"
            + "abaaaaaaaaaaaccccccccccccaaaaaaccccaaaacccaaaaaaaaaaaaacaaccccccccaaaaacccccccaaccccaaaacccaaaaaacaacaaccccccccaaaaaaaaccaaaaaaccccccccciiiinnnttttxxwyywyyyyvvrrrnneeecccccc\n"
            + "abaaaaacaacaaccccccccccccaaaaaaccccaaaacccaaacaaaacaaaccccccccccccaacaaccccaacccccaaaaaacaaaaaaaacaaaaacccccaaaaaaaaaaaccaaaaaacccccccccciiiinnnttttwwyywwywwwvrrrnneeecccccc\n"
            + "abaaaacccccccccccccccccccaaaaaacccccaaacccccccaaacccacaaccccccccccccccccccaaccccccaaaaaccaaaaaaaaaaaaaccccccaaaaaaaaacccaaaaaaaacccccccccciiinnnnntswwywwwwwwwwrrrnnneecccccc\n"
            + "abaaaaaccccccccccccccccccaacaaacccccccccccccccaacaaacaaacccccccccccccccaaaaacaaccccaaaaaccccaacccaaaaaacccccaaacaaaaaccccacccccccaaacccccciiiiinnmsswwwwwswwwwrrrrnnneecccccc\n"
            + "abaaaaaaccaaaccccccccccccccccccccccccccccccccccccaaaaaaaccccccccaaaccccaaaaaaaaccccaaccaccccaacccccaaaaccaaaaaaaaaaccccccccccccccaaaaacccccciihmmmsswwwwssrrrrrrrrnnneecccccc\n"
            + "abaaccaacaaaaccccccccccccccccccccccccccccccccccccaaaaaacccccccccaaaaaccccaaaaacccccccccccccccccccccacccccaaaaaaaaacccccccaaccaacaaaaacccccccchhhmmssswwsssrrrrrrrnnneeecccccc\n"
            + "abaacccccaaaacccccccccccccccccccccccccccccccccccccaaaaaaaacccccaaaaaacccaaaaacccccccccccccccccccccccccccccaaaaaaaccccccccaaaaaacaaaaacccccccchhhmmssssssslllllllnnnnfeecccccc\n"
            + "abccccccccaaacccccccccccccccaaccccccccccccccaaaccaaaaaaaaacccccaaaaaacccaacaaaccccccccccccccccccccccccaacccaaaaaaccccccccaaaaacccaaaaaccccccchhhmmmssssslllllllllnnfffeaacccc\n"
            + "abcccccccccccccccccccccccacaaaccccccccccccccaaaaaaaaaaaaaaccaaccaaaaaccccccaaccaacccccccccccccccccccccaaaccaaaaaaacccccccaaaaaaccaaccccccccccchhhmmmmsmllllllllllfffffaaacccc\n"
            + "abccccccccccccccccccccccaaaaaaaaccccccccccccaaaaaaacaaacaaaaaaccaaaaccccccccccaaaacccccccccccccccccaaaaaaaaaaacaaaccccccaaaaaaaacccccccccccccchhhmmmmmmlllggfffffffffaacccccc\n"
            + "abccccccccccccccccccccccaaaaaaaaccccccccccccaaacccccaaacaaaaacccccccccccaaacccaaaacccccccccccccccccaaaaaaaaaacccccccccccaaaaaaaaccccccccccccccchhhmmmmmlggggffffffffaaacccccc\n"
            + "abccccccccccccccccaaaacccaaaaaacccccccccccccccccccccaaacaaaaaaccccccccccaaacccaaaaccccccacccccccccccaaaaaacccccccccccccccccaacccaaccccccccccccchhhhgmgggggggffaccccccaacccccc\n"
            + "abccccccccccccccccaaaacccaaaaacccccccccccccccccccccccccaaaaaaaacccccccccaaaaccaaacccccccaaacaaacccccaaaaaacccccccccccccccccaaccaaccccccccccccccchhhgggggggaaaaacccccccccccccc\n"
            + "abccccccccccccccccaaaacccaaaaaaccccccccccccccccccccccccaaaaaaaaccccccccccaaaaaaaaaccccccaaaaaaacccccaaaaaaccccccccccccccccccaaaaacaaccccccccccccchggggggaacccccccccccccccccca\n"
            + "abcccccccccccccccccaacccccccaaccccccccccccccccccccccccccccaacaccaaacccaacaaaaaaaacccccccaaaaaaccccccaaccaacaaaccacccccaaccccaaaaaaaaccccccccccccccccccaaaccccccccccccccccccca\n"
            + "abcccccaaccccccccccccccccaaccccccccaacccccccccaaacccccccccaacaaaaaacccaaacaaaaaacccccccaaaaaaacccccccccccccaaaaaacccaaaaccccccaaaaaccccaaacccccccccccccaaccccccccccccccaaaaaa\n"
            + "abccccaaaacccccccccccccccaaaacccaaaaccccccccccaaaacccccccccccaaaaaaccaaaaaaaaaacccccccaaaaaaaaaaccccccccccccaaaaacccaaaaaacccaaaaacccccaaaacccccccccccaaccccccccccccccccaaaaa\n"
            + "abccccaaaacccccccccccccaaaaaacccaaaaaaccccccccaaaaccccccccccaaaaaaaacaaaaaaaaaaaaaccccaaaaaaaaaaccccccccccaaaaaaaacccaaaaccccaacaaaccccaaaacccccccccccccccccccccccccccccaaaaa";
}
