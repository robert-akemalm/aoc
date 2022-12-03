package aoc2020;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day22 {
    public static void main(String[] args) {
        a(TEST_INPUT);
        a(INPUT);
        b(TEST_INPUT);
        b(INPUT);
    }


    private static void b(String input) {
        String[] players = input.split("\n\n");
        int[] p1 = players[0].lines().filter(l -> !l.startsWith("P")).mapToInt(Integer::parseInt).toArray();
        int[] p2 = players[1].lines().filter(l -> !l.startsWith("P")).mapToInt(Integer::parseInt).toArray();
        ArrayDeque<Integer> player1 = new ArrayDeque<>();
        for (int card : p1) {
            player1.addLast(card);
        }
        ArrayDeque<Integer> player2 = new ArrayDeque<>();
        for (int card : p2) {
            player2.addLast(card);
        }
        playAs1(player1, player2);
        ArrayDeque<Integer> winner = player1.isEmpty() ? player2 : player1;
        long score = 0;
        for (int i = 1, numCards = winner.size(); i <= numCards ; i++) {
            score += winner.pollLast() * i;
        }
        System.out.println(score);
    }

    private static boolean playAs1(ArrayDeque<Integer> p1, ArrayDeque<Integer> p2) {
        Set<String> seen = new HashSet<>();
        while (!p1.isEmpty() && !p2.isEmpty()) {
            String game = p1.stream().map(String::valueOf).collect(Collectors.joining(",")) + "\n"
                          + p2.stream().map(String::valueOf).collect(Collectors.joining(","));
//            System.out.println(game);
//            System.out.println();
            if (!seen.add(game)) {
                return true;
            }
            Integer card1 = p1.pollFirst();
            Integer card2 = p2.pollFirst();
            boolean p1Wins;
            if (card1 <= p1.size() && card2 <= p2.size()) {
                List<Integer> temp = new ArrayList<>();
                for (int i = 0; i < card1; i++) {
                    temp.add(p1.pollFirst());
                }
                ArrayDeque<Integer> subP1 = new ArrayDeque<>();
                for (int i = temp.size() - 1; i >= 0; i--) {
                    subP1.addFirst(temp.get(i));
                    p1.addFirst(temp.get(i));
                }
                temp.clear();
                for (int i = 0; i < card2; i++) {
                    temp.add(p2.pollFirst());
                }
                ArrayDeque<Integer> subP2 = new ArrayDeque<>();
                for (int i = temp.size() - 1; i >= 0; i--) {
                    subP2.addFirst(temp.get(i));
                    p2.addFirst(temp.get(i));
                }
                p1Wins = playAs1(subP1, subP2);
            } else {
                p1Wins = card1 > card2;
            }

            if (p1Wins) {
                p1.addLast(card1);
                p1.addLast(card2);
            } else {
                p2.addLast(card2);
                p2.addLast(card1);
            }
        }
        return p2.isEmpty();
    }

    private static void a(String input) {
        String[] players = input.split("\n\n");
        int[] p1 = players[0].lines().filter(l -> !l.startsWith("P")).mapToInt(Integer::parseInt).toArray();
        int[] p2 = players[1].lines().filter(l -> !l.startsWith("P")).mapToInt(Integer::parseInt).toArray();
        ArrayDeque<Integer> player1 = new ArrayDeque<>();
        for (int card : p1) {
            player1.addLast(card);
        }
        ArrayDeque<Integer> player2 = new ArrayDeque<>();
        for (int card : p2) {
            player2.addLast(card);
        }
        while (!player1.isEmpty() && !player2.isEmpty()) {
            Integer card1 = player1.pollFirst();
            Integer card2 = player2.pollFirst();
            if (card1 > card2) {
                player1.addLast(card1);
                player1.addLast(card2);
            } else {
                player2.addLast(card2);
                player2.addLast(card1);
            }
        }
        ArrayDeque<Integer> winner = player1.isEmpty() ? player2 : player1;
        long score = 0;
        for (int i = 1, numCards = winner.size(); i <= numCards ; i++) {
            score += winner.pollLast() * i;
        }
        System.out.println(score);
    }

    private static final String TEST_INPUT = "Player 1:\n"
                                             + "9\n"
                                             + "2\n"
                                             + "6\n"
                                             + "3\n"
                                             + "1\n"
                                             + "\n"
                                             + "Player 2:\n"
                                             + "5\n"
                                             + "8\n"
                                             + "4\n"
                                             + "7\n"
                                             + "10";

    private static final String INPUT = "Player 1:\n"
                                        + "26\n"
                                        + "14\n"
                                        + "6\n"
                                        + "34\n"
                                        + "37\n"
                                        + "9\n"
                                        + "17\n"
                                        + "39\n"
                                        + "4\n"
                                        + "5\n"
                                        + "1\n"
                                        + "8\n"
                                        + "49\n"
                                        + "16\n"
                                        + "18\n"
                                        + "47\n"
                                        + "20\n"
                                        + "31\n"
                                        + "23\n"
                                        + "19\n"
                                        + "35\n"
                                        + "41\n"
                                        + "28\n"
                                        + "15\n"
                                        + "44\n"
                                        + "\n"
                                        + "Player 2:\n"
                                        + "7\n"
                                        + "2\n"
                                        + "10\n"
                                        + "25\n"
                                        + "29\n"
                                        + "46\n"
                                        + "40\n"
                                        + "45\n"
                                        + "11\n"
                                        + "50\n"
                                        + "42\n"
                                        + "24\n"
                                        + "38\n"
                                        + "13\n"
                                        + "36\n"
                                        + "22\n"
                                        + "33\n"
                                        + "3\n"
                                        + "43\n"
                                        + "21\n"
                                        + "48\n"
                                        + "30\n"
                                        + "32\n"
                                        + "12\n"
                                        + "27";
}
