package aoc2023;

import java.util.ArrayList;
import java.util.List;

public class Day2 {
    public static void main(String[] args) {
        Util.time(() -> a(TEST_INPUT));
        Util.time(() -> a(INPUT));
        Util.time(() -> b(TEST_INPUT));
        Util.time(() -> b(INPUT));
    }

    private static void a(String input) {
        List<Game> games = parse(input);
        long sum = games.stream().filter(Game::valid).mapToInt(Game::id).sum();
        System.out.println(sum);
    }

    private static void b(String input) {
        List<Game> games = parse(input);
        long sum = games.stream().mapToInt(Game::power).sum();
        System.out.println(sum);
    }

    record Game(int id, List<Round> rounds) {
        boolean valid() {
            return rounds.stream().allMatch(Round::valid);
        }

        int power() {
            int maxRed = rounds.stream().mapToInt(r -> r.count("red")).max().orElseThrow();
            int maxGreen = rounds.stream().mapToInt(r -> r.count("green")).max().orElseThrow();
            int maxBlue = rounds.stream().mapToInt(r -> r.count("blue")).max().orElseThrow();
            return maxRed * maxGreen * maxBlue;
        }
    }

    record Round(List<ColorAndCount> colorsAndCounts) {
        boolean valid() {
            return colorsAndCounts.stream().allMatch(ColorAndCount::valid);
        }

        int count(String color) {
            return colorsAndCounts.stream().filter(c -> c.color.equals(color)).mapToInt(ColorAndCount::cnt).findAny().orElse(0);
        }
    }

    record ColorAndCount(String color, int cnt) {
        boolean valid() {
            return switch (color) {
                case "red" -> cnt <= 12;
                case "green" -> cnt <= 13;
                case "blue" -> cnt <= 14;
                default -> false;
            };
        }
    }

    static List<Game> parse(String input) {
        List<Game> games = new ArrayList<>();
        for (String line : input.lines().toList()) {
            List<Round> rounds = new ArrayList<>();
            for (String round : line.substring(line.indexOf(": ") + 2).split("; ")) {
                List<ColorAndCount> colorsAndCounts = new ArrayList<>();
                for (String countAndColor : round.split(", ")) {
                    int count = Integer.parseInt(countAndColor.substring(0, countAndColor.indexOf(' ')));
                    String color = countAndColor.substring(countAndColor.indexOf(' ') + 1);
                    colorsAndCounts.add(new ColorAndCount(color, count));
                }
                rounds.add(new Round(colorsAndCounts));
            }
            int id = Integer.parseInt(line.substring(line.indexOf(' ') + 1, line.indexOf(':')));
            games.add(new Game(id, rounds));
        }
        return games;
    }

    private static final String TEST_INPUT = "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green\n"
                                             + "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue\n"
                                             + "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red\n"
                                             + "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red\n"
                                             + "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green";

    private static final String INPUT =
            "Game 1: 9 red, 2 green, 13 blue; 10 blue, 2 green, 13 red; 8 blue, 3 red, 6 green; 5 green, 2 red, 1 blue\n"
            + "Game 2: 2 green, 2 blue, 16 red; 14 red; 13 red, 13 green, 2 blue; 7 red, 7 green, 2 blue\n"
            + "Game 3: 6 red, 4 green, 7 blue; 7 blue, 9 red, 3 green; 2 red, 4 green; 6 red, 2 blue; 7 blue, 9 red, 5 green\n"
            + "Game 4: 8 red, 2 green, 2 blue; 5 red, 2 blue, 3 green; 7 red, 9 green, 2 blue; 10 blue, 16 green, 1 red; 6 blue, 1 green, 8 red\n"
            + "Game 5: 12 blue, 6 red, 1 green; 10 green, 8 blue, 15 red; 1 green, 12 red, 11 blue; 3 green, 11 blue, 1 red; 7 blue, 5 red, 5 green; 11 green, 2 blue\n"
            + "Game 6: 3 blue, 10 green, 2 red; 5 green; 6 blue, 3 red\n"
            + "Game 7: 3 green, 1 blue, 4 red; 3 red, 13 blue; 14 blue, 6 red; 7 green, 15 blue, 9 red; 1 green, 3 red, 9 blue\n"
            + "Game 8: 19 red, 1 green, 4 blue; 14 blue; 2 red, 3 blue\n"
            + "Game 9: 5 green, 14 blue, 3 red; 8 red, 16 blue, 5 green; 12 green, 15 red, 4 blue; 14 blue, 17 red; 6 red, 11 blue, 8 green\n"
            + "Game 10: 9 red, 7 blue; 6 red, 3 blue, 4 green; 1 red, 5 green, 3 blue; 5 red, 9 green, 6 blue; 7 blue, 6 green\n"
            + "Game 11: 2 blue, 10 green; 11 green; 2 green, 1 red, 2 blue; 2 blue, 1 green, 1 red; 2 blue, 18 green\n"
            + "Game 12: 6 green, 2 red; 2 green; 11 red, 6 green, 1 blue\n"
            + "Game 13: 1 blue, 10 red, 2 green; 2 green, 7 red; 9 green, 9 red; 1 red, 4 green\n"
            + "Game 14: 7 red, 15 green, 17 blue; 10 green, 6 red, 12 blue; 3 blue, 7 red, 3 green\n"
            + "Game 15: 8 green; 10 red, 17 green, 1 blue; 9 red, 1 blue; 6 green, 8 red; 1 green, 1 red; 17 green, 2 red\n"
            + "Game 16: 3 blue, 2 green, 7 red; 12 blue, 9 red, 5 green; 8 green, 3 blue, 5 red; 6 red, 2 blue, 11 green; 3 green, 8 blue, 9 red; 1 green, 5 red, 12 blue\n"
            + "Game 17: 2 green, 1 blue, 3 red; 1 blue, 1 green, 4 red; 2 blue, 2 green, 11 red; 1 red, 2 green; 2 blue, 10 red, 2 green\n"
            + "Game 18: 3 blue, 3 red; 8 blue, 1 green, 5 red; 4 green, 6 blue, 3 red\n"
            + "Game 19: 9 red, 3 green, 14 blue; 15 red, 2 blue, 1 green; 2 green, 15 red, 5 blue; 3 red, 3 blue, 1 green\n"
            + "Game 20: 2 red, 1 blue, 5 green; 11 blue, 1 red, 4 green; 6 blue, 2 red, 2 green; 13 blue, 2 red, 10 green; 7 green, 13 blue\n"
            + "Game 21: 3 red, 1 green, 1 blue; 3 red, 7 blue, 1 green; 1 green, 4 red, 7 blue; 1 green; 6 blue, 3 red; 2 red, 6 blue\n"
            + "Game 22: 7 red, 5 green, 2 blue; 14 red; 2 blue, 5 green, 15 red; 1 blue, 5 green, 14 red; 13 red, 1 green; 3 red, 1 green\n"
            + "Game 23: 2 green, 8 blue, 5 red; 3 blue, 9 red; 9 blue, 2 green; 9 red, 2 green, 5 blue; 5 red, 2 green\n"
            + "Game 24: 14 green, 7 blue, 2 red; 7 blue, 3 red, 16 green; 1 blue, 5 red, 6 green; 3 blue, 7 red, 2 green; 7 red, 9 green\n"
            + "Game 25: 3 blue, 8 red, 15 green; 2 red, 8 green, 2 blue; 16 red, 3 blue, 13 green\n"
            + "Game 26: 1 blue, 9 red, 8 green; 6 green, 1 red, 3 blue; 7 red, 8 blue, 6 green; 9 red, 7 green\n"
            + "Game 27: 6 red, 9 green, 1 blue; 8 green, 3 red, 1 blue; 8 green, 15 red; 14 red, 11 green, 2 blue; 11 green, 4 red, 2 blue; 1 blue, 16 red\n"
            + "Game 28: 6 green, 10 blue, 4 red; 6 red, 2 blue, 6 green; 6 green, 14 blue, 17 red; 9 green, 1 red, 9 blue\n"
            + "Game 29: 10 blue, 2 red; 3 red, 11 green, 7 blue; 8 green, 2 blue, 7 red; 17 green, 5 blue, 9 red; 19 green, 10 red, 9 blue\n"
            + "Game 30: 6 blue, 9 green, 6 red; 17 green, 10 blue, 8 red; 17 green, 12 red, 7 blue\n"
            + "Game 31: 9 red, 1 green, 1 blue; 3 red; 6 red, 1 blue, 1 green; 2 blue, 7 red\n"
            + "Game 32: 3 green, 8 blue; 5 red, 4 green, 2 blue; 10 blue, 3 green; 2 green, 4 blue; 2 green, 16 blue, 2 red\n"
            + "Game 33: 2 red, 8 blue; 2 green, 6 red, 1 blue; 7 red, 12 blue; 1 red, 1 green, 7 blue; 1 green, 3 red, 2 blue\n"
            + "Game 34: 15 green; 1 red, 6 green; 6 blue, 1 red, 17 green; 14 green, 3 blue; 11 green, 1 red; 4 green, 1 blue\n"
            + "Game 35: 4 red, 3 green, 12 blue; 20 blue, 2 red, 2 green; 4 red, 4 green, 8 blue\n"
            + "Game 36: 8 red, 7 green; 15 red, 2 blue, 3 green; 2 red, 1 blue, 5 green; 4 green, 1 blue, 9 red; 14 red, 1 blue, 1 green; 11 green, 12 red\n"
            + "Game 37: 3 red, 3 green, 8 blue; 3 blue; 1 green, 3 blue, 2 red; 1 red, 2 blue, 2 green\n"
            + "Game 38: 18 red, 12 blue, 7 green; 7 blue, 10 red, 10 green; 1 green, 6 blue, 10 red; 7 red, 3 blue, 12 green; 9 green, 5 blue, 3 red\n"
            + "Game 39: 2 blue, 2 green, 2 red; 5 blue, 1 green; 1 blue; 1 red, 5 blue, 2 green\n"
            + "Game 40: 4 blue, 1 green; 5 blue, 1 red, 7 green; 2 red, 5 green, 5 blue\n"
            + "Game 41: 15 blue, 3 green, 16 red; 16 blue, 18 red, 8 green; 8 green, 1 blue, 12 red; 11 blue, 5 green, 18 red; 5 green, 20 red, 17 blue\n"
            + "Game 42: 7 red, 10 green; 1 blue, 5 red, 6 green; 6 red, 3 blue, 9 green; 6 green, 3 blue, 8 red; 4 green, 4 red, 2 blue; 2 blue, 1 green\n"
            + "Game 43: 6 blue, 5 red, 1 green; 2 green; 2 blue, 1 red; 4 green, 5 blue, 2 red\n"
            + "Game 44: 10 green; 8 green; 10 green, 2 blue, 10 red\n"
            + "Game 45: 9 green, 3 red; 8 green, 2 blue; 2 green, 8 blue\n"
            + "Game 46: 2 red, 9 blue, 9 green; 6 red, 10 green, 11 blue; 5 red, 4 green, 3 blue; 3 red, 2 green, 14 blue; 5 green, 5 red, 12 blue; 1 red, 9 green, 18 blue\n"
            + "Game 47: 2 red, 3 blue; 2 green; 2 green, 3 blue\n"
            + "Game 48: 9 green, 6 red, 1 blue; 3 blue, 12 green, 8 red; 4 red, 5 green, 5 blue; 14 green, 5 red; 1 red, 18 green, 4 blue\n"
            + "Game 49: 11 red, 10 green, 12 blue; 4 green, 6 red, 6 blue; 9 red, 3 blue, 7 green; 6 red, 10 green, 14 blue; 8 blue, 10 red, 5 green; 6 blue, 17 green, 6 red\n"
            + "Game 50: 8 blue, 4 green, 2 red; 1 red; 10 green, 4 blue, 1 red; 5 green, 8 blue, 2 red; 6 red, 1 blue, 3 green; 6 blue, 6 red, 1 green\n"
            + "Game 51: 3 red, 3 blue, 2 green; 8 green, 5 red; 11 red, 11 green, 2 blue; 12 green, 2 blue, 19 red; 12 red, 1 blue, 12 green; 10 green, 1 blue, 3 red\n"
            + "Game 52: 2 blue, 15 red; 19 red, 4 green; 11 red, 3 green, 6 blue; 1 green, 2 blue, 8 red; 3 blue, 6 red; 1 green, 7 blue, 1 red\n"
            + "Game 53: 1 green; 2 green, 1 blue; 1 red\n"
            + "Game 54: 1 red, 1 green, 12 blue; 1 green, 5 red, 12 blue; 2 green, 4 blue; 13 blue, 2 red, 2 green; 2 green, 10 blue, 1 red; 5 red, 1 green, 7 blue\n"
            + "Game 55: 4 green, 16 blue, 11 red; 6 red, 15 blue; 6 blue, 4 green, 5 red; 10 blue, 10 red, 3 green\n"
            + "Game 56: 3 red, 8 blue, 11 green; 9 green, 5 red, 4 blue; 1 blue, 4 red, 4 green\n"
            + "Game 57: 3 red, 12 green, 7 blue; 13 green, 14 blue, 1 red; 4 red, 6 green, 9 blue; 12 blue, 13 green\n"
            + "Game 58: 4 red, 4 blue, 3 green; 3 blue, 4 red; 11 green, 2 blue, 2 red\n"
            + "Game 59: 12 green, 5 red, 1 blue; 7 red, 1 blue, 3 green; 17 green, 2 blue, 4 red; 12 red, 17 green; 7 red, 10 green, 2 blue\n"
            + "Game 60: 5 blue, 3 green; 5 green, 11 red, 12 blue; 1 red, 2 blue, 15 green\n"
            + "Game 61: 12 blue, 1 green, 12 red; 14 blue, 12 green, 5 red; 7 red, 12 blue, 16 green\n"
            + "Game 62: 1 green, 8 red, 8 blue; 11 blue, 2 red; 1 green, 10 blue, 12 red; 7 red, 2 blue, 1 green; 6 red, 1 green, 11 blue; 1 green, 6 red, 6 blue\n"
            + "Game 63: 17 green, 16 red, 10 blue; 9 red, 14 green, 11 blue; 5 green, 16 red\n"
            + "Game 64: 12 red, 1 green, 1 blue; 4 blue, 7 red; 10 blue, 4 green, 6 red; 8 blue, 4 red, 13 green; 13 green, 9 blue, 9 red; 13 blue, 7 red, 3 green\n"
            + "Game 65: 4 blue, 3 green, 4 red; 10 blue, 5 red, 7 green; 1 red, 4 blue, 3 green; 13 green, 1 red, 9 blue; 8 green, 3 blue\n"
            + "Game 66: 17 green, 13 red, 3 blue; 15 green, 2 red, 4 blue; 10 red, 4 blue, 5 green; 6 red, 5 blue, 7 green; 3 red, 10 green, 1 blue\n"
            + "Game 67: 10 blue; 4 blue, 4 red; 4 blue, 3 green, 3 red; 1 green, 5 blue, 3 red\n"
            + "Game 68: 6 green, 6 blue, 3 red; 13 blue, 1 red; 14 blue, 6 red, 2 green; 14 blue, 7 red, 2 green\n"
            + "Game 69: 2 green, 2 blue, 3 red; 1 red, 6 blue, 4 green; 8 green, 9 red; 2 green\n"
            + "Game 70: 2 red, 7 green; 1 red, 10 green, 1 blue; 6 blue, 14 green; 6 green, 2 blue, 1 red; 5 blue, 10 green, 2 red; 2 blue, 11 green\n"
            + "Game 71: 6 blue, 4 red, 7 green; 13 green, 6 red, 3 blue; 8 green, 2 blue, 4 red\n"
            + "Game 72: 3 blue, 1 green, 11 red; 5 green, 11 blue, 4 red; 7 blue, 13 red; 14 blue, 12 red, 5 green\n"
            + "Game 73: 8 red, 19 blue, 4 green; 15 green, 3 red, 16 blue; 7 blue, 9 red\n"
            + "Game 74: 5 green, 8 red, 6 blue; 8 green, 9 red; 11 green, 6 blue\n"
            + "Game 75: 1 green, 6 red, 4 blue; 2 green, 13 blue, 6 red; 10 red, 1 green, 10 blue\n"
            + "Game 76: 3 blue, 6 green, 2 red; 7 red, 6 blue, 4 green; 5 blue, 6 red, 3 green\n"
            + "Game 77: 17 red, 6 green; 7 green, 12 red, 4 blue; 4 red, 7 green, 14 blue\n"
            + "Game 78: 1 green, 15 red, 5 blue; 16 green, 3 red, 18 blue; 13 blue, 1 red, 13 green\n"
            + "Game 79: 7 red, 1 blue, 3 green; 2 green, 5 red; 5 green, 2 blue, 8 red; 11 red, 1 blue, 3 green\n"
            + "Game 80: 9 blue, 4 red; 4 green, 3 blue, 4 red; 7 red, 9 blue, 4 green; 5 red, 9 blue, 4 green; 3 red, 11 blue, 5 green; 6 red, 4 green, 11 blue\n"
            + "Game 81: 1 red, 16 green, 2 blue; 4 red, 15 green, 10 blue; 2 red, 9 blue, 17 green; 10 blue, 16 green\n"
            + "Game 82: 4 blue, 3 green; 2 green, 3 blue; 4 blue, 2 red, 2 green; 2 red, 1 green\n"
            + "Game 83: 10 blue, 8 red, 19 green; 8 red, 4 blue, 17 green; 2 blue, 9 red, 6 green; 11 blue, 2 red, 17 green; 1 red, 17 green, 8 blue\n"
            + "Game 84: 4 blue, 1 red, 6 green; 7 blue, 10 green, 4 red; 1 green, 7 blue, 1 red\n"
            + "Game 85: 1 blue, 2 red, 1 green; 5 blue, 2 green, 9 red; 1 green, 2 red, 10 blue\n"
            + "Game 86: 17 blue, 9 green, 9 red; 19 red, 12 green, 11 blue; 19 red, 9 green, 17 blue\n"
            + "Game 87: 11 green, 6 red; 1 green, 4 blue; 16 green, 5 red; 14 green, 1 red, 14 blue; 12 blue, 8 green, 6 red; 4 red, 11 blue, 2 green\n"
            + "Game 88: 6 green, 7 red, 1 blue; 5 red, 1 green, 1 blue; 3 red, 6 green\n"
            + "Game 89: 1 green, 8 blue, 1 red; 8 red, 1 green; 4 red, 6 blue; 4 red, 10 blue; 8 red, 9 blue, 1 green\n"
            + "Game 90: 6 blue, 12 green; 1 red, 4 blue, 14 green; 5 red, 5 blue, 6 green; 1 blue, 3 red, 3 green; 7 green, 4 blue; 2 green, 2 red, 1 blue\n"
            + "Game 91: 2 blue, 12 red, 4 green; 7 green, 6 red; 1 blue, 7 green, 6 red; 5 green, 5 red, 1 blue; 1 blue, 11 green, 9 red; 4 red, 1 blue, 2 green\n"
            + "Game 92: 4 blue, 12 green; 6 red, 4 blue, 2 green; 5 blue, 1 red, 17 green; 6 red, 4 blue, 15 green; 3 blue, 5 red, 13 green\n"
            + "Game 93: 8 green, 2 blue, 16 red; 7 green, 3 blue, 8 red; 9 green, 4 red, 3 blue; 13 red, 5 blue; 5 blue, 1 green, 10 red; 1 blue, 9 red\n"
            + "Game 94: 7 blue, 5 red, 14 green; 8 green, 3 blue, 1 red; 4 blue, 8 red\n"
            + "Game 95: 3 blue, 4 green, 10 red; 5 blue, 17 red, 2 green; 18 red, 2 green, 5 blue; 3 blue, 3 green, 2 red; 4 blue, 18 red; 6 red, 2 green\n"
            + "Game 96: 1 blue, 9 green, 2 red; 3 red, 10 green; 2 red, 8 green, 4 blue; 17 green, 2 blue; 10 green, 1 blue, 1 red; 8 green, 1 blue, 3 red\n"
            + "Game 97: 9 green, 1 blue; 1 blue, 6 green; 7 blue, 3 green; 3 red; 17 green, 5 red; 1 blue, 17 green, 5 red\n"
            + "Game 98: 4 blue, 7 green, 2 red; 15 green, 10 blue, 1 red; 4 blue, 5 green; 1 green, 2 red, 5 blue\n"
            + "Game 99: 2 green, 1 blue, 14 red; 11 red, 6 blue, 5 green; 10 red, 18 blue, 1 green; 5 green, 9 blue, 9 red\n"
            + "Game 100: 5 blue, 5 green; 7 blue, 15 green; 4 red, 7 green, 12 blue; 7 green, 1 blue; 5 blue, 9 green, 1 red";
}
