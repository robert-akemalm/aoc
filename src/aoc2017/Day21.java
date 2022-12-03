package aoc2017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class Day21 {
    public static void main(String[] args) throws Exception {
        part1();
        part2();
    }

    private static void part1() throws Exception {
        Map<String, String> transformations = new HashMap<>();
        for (String line : DATA.split("\n")) {
            String[] parts = line.split("=>");
            Set<String> keys = new Matrix(parts[0].trim()).allVariants();
            for (String key : keys) {
                transformations.put(key, parts[1].trim());
            }
        }

        String[] image = new Matrix(START).lines;
        for (int i = 0; i < 18; i++) { //5
            System.out.println(Arrays.toString(image));
            List<String> parts = split(image);
            List<String> enhanced = new ArrayList<>();
            for (String part : parts) {
                String transformed = transformations.get(part);
                if (transformed == null) {
                    System.out.println();
                }
                enhanced.add(transformed);
            }
            image = merge(enhanced);
        }
        int sum = Stream.of(image).mapToInt(s -> {
            int count = 0;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '#') {
                    count++;
                }
            }
            return count;
        }).sum();
        System.out.println(sum);
    }

    private static void part2() throws Exception {
    }

    private static List<String> split(String[] image) {
        int segmentSize = image.length % 2 == 0 ? 2 : 3;
        int segmentsPerRow = image.length / segmentSize;
        List<String> segments = new ArrayList<>();

        for (int i = 0; i < segmentsPerRow; i++) {
            for (int j = 0; j < segmentsPerRow; j++) {
                StringBuilder segment = new StringBuilder();
                for (int segmentRow = 0; segmentRow < segmentSize; segmentRow++) {
                    for (int segmentCol = 0; segmentCol < segmentSize; segmentCol++) {
                        int row = i * segmentSize + segmentRow;
                        int col = j * segmentSize + segmentCol;
                        segment.append(image[row].charAt(col));
                    }
                    segment.append("/");
                }
                segment.setLength(segment.length() - 1);
                segments.add(segment.toString());
            }
        }
        return segments;
    }

    private static String[] merge(List<String> segments) {
        List<Matrix> segmentsAsMatrices = new ArrayList<>();
        for (String segment : segments) {
            segmentsAsMatrices.add(new Matrix(segment));
        }

        int segmentsPerRow = (int) Math.sqrt(segments.size());
        Matrix[][] matrices = new Matrix[segmentsPerRow][];
        for (int row = 0; row < segmentsPerRow; row++) {
            matrices[row] = new Matrix[segmentsPerRow];
            for (int col = 0; col < segmentsPerRow; col++) {
                matrices[row][col] = segmentsAsMatrices.get(row * segmentsPerRow + col);
            }
        }

        int segmentSize = matrices[0][0].lines.length;
        int imageSize = matrices.length * segmentSize;
        String[] image = new String[imageSize];

        int imageRow = 0;
        for (int matrixRow = 0; matrixRow < matrices.length; matrixRow++) {
            for (int row = 0; row < segmentSize; row++) {
                StringBuilder sb = new StringBuilder();
                for (int matrixCol = 0; matrixCol < matrices.length; matrixCol++) {
                    sb.append(matrices[matrixRow][matrixCol].lines[row]);
                }
                image[imageRow++] = sb.toString();
            }
        }

        return image;
    }

    static class Matrix {
        final String[] lines;

        Matrix(String s) {
            lines = s.split("/");
        }

        static String[] turn(String[] matrix) {
            String[] turned = new String[matrix.length];
            for (int col = matrix.length - 1; col >= 0; col--) {
                StringBuilder line = new StringBuilder();
                for (String row : matrix) {
                    line.append(row.charAt(col));
                }
                turned[matrix.length - 1 - col] = line.toString();
            }
            return turned;
        }

        static String[] flip(String[] matrix) {
            String[] flipped = new String[matrix.length];
            for (int row = 0; row < matrix.length; row++) {
                flipped[row] = new StringBuilder(matrix[row]).reverse().toString();
            }
            return flipped;
        }

        static String toString(String[] matrix) {
            return Stream.of(matrix).reduce((s, s2) -> s + "/" + s2).get();
        }

        Set<String> allVariants() {
            Set<String> variants = new HashSet<>();
            String[] matrix = this.lines;
            String[] horizontal = flip(matrix);
            String[] vertical = flip(turn(matrix));
            String[] vertical2 = flip(turn(turn(turn(matrix))));
            for (int i = 0; i < 4; i++) {
                variants.add(toString(matrix));
                variants.add(toString(horizontal));
                variants.add(toString(vertical));
                variants.add(toString(vertical2));
                matrix = turn(matrix);
                horizontal = turn(horizontal);
                vertical = turn(vertical);
                vertical2 = turn(vertical2);
            }
            return variants;
        }
    }


    private static final String START = ".#./..#/###";
    private static final String DATA2 = ""
                                        + "../.# => ##./#../...\n"
                                        + ".#./..#/### => #..#/..../..../#..#";

    private static final String DATA = ""
                                       + "../.. => .../#.#/...\n"
                                       + "#./.. => ..#/..#/#..\n"
                                       + "##/.. => .../#../..#\n"
                                       + ".#/#. => #../.../...\n"
                                       + "##/#. => #.#/.#./#..\n"
                                       + "##/## => ..#/#.#/..#\n"
                                       + ".../.../... => .#../#..#/#.../.#..\n"
                                       + "#../.../... => ..##/..##/.#.#/....\n"
                                       + ".#./.../... => ..##/..##/.###/##..\n"
                                       + "##./.../... => ..../.##./#.##/..#.\n"
                                       + "#.#/.../... => ####/#.##/#.##/#.#.\n"
                                       + "###/.../... => #..#/..#./..../##.#\n"
                                       + ".#./#../... => ..#./.#../...#/#.##\n"
                                       + "##./#../... => ..../#.##/#..#/.#..\n"
                                       + "..#/#../... => ##.#/####/###./###.\n"
                                       + "#.#/#../... => ..../#.##/.###/#.#.\n"
                                       + ".##/#../... => ..#./##.#/####/..##\n"
                                       + "###/#../... => ..#./.##./...#/..#.\n"
                                       + ".../.#./... => .###/#.../.#../####\n"
                                       + "#../.#./... => ###./.#.#/#.##/##.#\n"
                                       + ".#./.#./... => ..##/..#./###./..#.\n"
                                       + "##./.#./... => #..#/..#./###./...#\n"
                                       + "#.#/.#./... => #.../##.#/#.##/#..#\n"
                                       + "###/.#./... => ...#/#..#/####/##.#\n"
                                       + ".#./##./... => #.##/#.##/..../#.#.\n"
                                       + "##./##./... => ..##/###./..#./####\n"
                                       + "..#/##./... => ..../##../##.#/.##.\n"
                                       + "#.#/##./... => ##../####/####/.#.#\n"
                                       + ".##/##./... => ..../##.#/.###/##..\n"
                                       + "###/##./... => .#../#.#./.#../..##\n"
                                       + ".../#.#/... => ####/#.#./..##/#..#\n"
                                       + "#../#.#/... => .#../.#../#..#/....\n"
                                       + ".#./#.#/... => ..##/.##./####/#.#.\n"
                                       + "##./#.#/... => ..#./###./.#../....\n"
                                       + "#.#/#.#/... => ..#./..#./...#/#...\n"
                                       + "###/#.#/... => ###./.#../##../####\n"
                                       + ".../###/... => #.##/####/####/..##\n"
                                       + "#../###/... => .#.#/...#/###./...#\n"
                                       + ".#./###/... => ..../.#.#/.#../....\n"
                                       + "##./###/... => ...#/.###/..../.##.\n"
                                       + "#.#/###/... => ..##/###./.#../#..#\n"
                                       + "###/###/... => .###/..#./..#./.###\n"
                                       + "..#/.../#.. => .##./###./####/#.#.\n"
                                       + "#.#/.../#.. => ####/#.../#.../..##\n"
                                       + ".##/.../#.. => ###./#..#/..#./.#..\n"
                                       + "###/.../#.. => .###/.##./#.#./.###\n"
                                       + ".##/#../#.. => ##.#/...#/.#.#/...#\n"
                                       + "###/#../#.. => #.##/..#./..../#..#\n"
                                       + "..#/.#./#.. => #..#/##.#/.##./####\n"
                                       + "#.#/.#./#.. => ###./..##/#..#/#..#\n"
                                       + ".##/.#./#.. => .#../..../...#/...#\n"
                                       + "###/.#./#.. => .#../##../.###/..#.\n"
                                       + ".##/##./#.. => ##../..##/##../##.#\n"
                                       + "###/##./#.. => #.##/#..#/.###/####\n"
                                       + "#../..#/#.. => ##.#/####/#.../..##\n"
                                       + ".#./..#/#.. => #..#/..../..../###.\n"
                                       + "##./..#/#.. => #..#/##.#/##.#/#.#.\n"
                                       + "#.#/..#/#.. => .###/##.#/####/#...\n"
                                       + ".##/..#/#.. => ####/.##./...#/#..#\n"
                                       + "###/..#/#.. => .#.#/####/##.#/...#\n"
                                       + "#../#.#/#.. => ..##/.##./..##/##..\n"
                                       + ".#./#.#/#.. => #.../##../..##/..#.\n"
                                       + "##./#.#/#.. => ...#/##.#/#..#/.#..\n"
                                       + "..#/#.#/#.. => #.#./##../#.##/###.\n"
                                       + "#.#/#.#/#.. => ##../##.#/#.#./....\n"
                                       + ".##/#.#/#.. => ####/...#/####/.#..\n"
                                       + "###/#.#/#.. => ..../.#../.#../....\n"
                                       + "#../.##/#.. => .#.#/..#./#..#/.###\n"
                                       + ".#./.##/#.. => #.../.#.#/.###/.##.\n"
                                       + "##./.##/#.. => #.#./#.#./.#../###.\n"
                                       + "#.#/.##/#.. => ####/##../.##./####\n"
                                       + ".##/.##/#.. => #.../#.#./#.##/###.\n"
                                       + "###/.##/#.. => ####/####/..../####\n"
                                       + "#../###/#.. => ####/.##./...#/##.#\n"
                                       + ".#./###/#.. => .#../#.##/#..#/..##\n"
                                       + "##./###/#.. => #.#./..##/#.../..##\n"
                                       + "..#/###/#.. => #.##/.###/#.#./###.\n"
                                       + "#.#/###/#.. => #.##/#.##/..../#..#\n"
                                       + ".##/###/#.. => .##./#.#./..##/####\n"
                                       + "###/###/#.. => .##./#..#/#.../###.\n"
                                       + ".#./#.#/.#. => #.#./#..#/#..#/##.#\n"
                                       + "##./#.#/.#. => ...#/#.#./##.#/###.\n"
                                       + "#.#/#.#/.#. => ##.#/..##/##.#/#.##\n"
                                       + "###/#.#/.#. => .#.#/..#./##../.##.\n"
                                       + ".#./###/.#. => #..#/..#./..##/#...\n"
                                       + "##./###/.#. => ####/.#.#/####/..#.\n"
                                       + "#.#/###/.#. => #.#./..##/##../#..#\n"
                                       + "###/###/.#. => ...#/..../..../#.#.\n"
                                       + "#.#/..#/##. => ..#./.##./###./.#.#\n"
                                       + "###/..#/##. => #.../###./...#/####\n"
                                       + ".##/#.#/##. => ..../..../.###/##..\n"
                                       + "###/#.#/##. => ##../..../#.#./.##.\n"
                                       + "#.#/.##/##. => .#.#/##../..##/#.#.\n"
                                       + "###/.##/##. => ###./####/...#/.#..\n"
                                       + ".##/###/##. => ..##/#.../..##/.#.#\n"
                                       + "###/###/##. => ..##/...#/.###/.#..\n"
                                       + "#.#/.../#.# => ..##/#.../##.#/....\n"
                                       + "###/.../#.# => #.##/#..#/..../##..\n"
                                       + "###/#../#.# => #.../..../##.#/..#.\n"
                                       + "#.#/.#./#.# => ###./..##/.#../.##.\n"
                                       + "###/.#./#.# => ..../#..#/.###/#..#\n"
                                       + "###/##./#.# => .#.#/###./##.#/.###\n"
                                       + "#.#/#.#/#.# => ..../..../.##./#..#\n"
                                       + "###/#.#/#.# => .###/.#.#/...#/.###\n"
                                       + "#.#/###/#.# => .#.#/##../.#../.#..\n"
                                       + "###/###/#.# => .#.#/.##./#.##/....\n"
                                       + "###/#.#/### => ..#./..#./..#./..##\n"
                                       + "###/###/### => ##.#/..##/.#.#/....";
}
