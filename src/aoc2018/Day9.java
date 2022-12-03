package aoc2018;

import java.util.Arrays;

public class Day9 {
    public static void main(String[] args) {
        a(405, 7095300);
        b("");
    }
    /*
    9 players; last marble is worth 25 points: high score is 32
    10 players; last marble is worth 1618 points: high score is 8317
    13 players; last marble is worth 7999 points: high score is 146373
    17 players; last marble is worth 1104 points: high score is 2764
    21 players; last marble is worth 6111 points: high score is 54718
    30 players; last marble is worth 5807 points: high score is 37305
     */

    private static void b(String input) {

    }

    private static void a(int players, int last) {
        Game game = new Game(players);
        for (int i = 0; i < last; i++) {
            game.addMarble();
        }
        System.out.println(game.highScore());
    }

    private static class Game {
        final long playerScores[];
        int currentPlayer = -1;
        Marble current;
        int nextValue;

        private Game(int players) {
            this.playerScores = new long[players];
            this.current = new Marble(0);
            nextValue = 1;
        }

        void addMarble() {
            currentPlayer = (currentPlayer + 1) % playerScores.length;
            if (nextValue % 23 == 0) {
                playerScores[currentPlayer] += nextValue++;
                for (int i = 0; i < 7; i++) {
                    current = current.left;
                }
                playerScores[currentPlayer] += current.value;
                current.left.right = current.right;
                current.right.left = current.left;
                current = current.right;
            } else {
                Marble left = current.right;
                Marble right = current.right.right;
                current = new Marble(nextValue++, left, right);
            }
        }

        long highScore() {
            long max = 0;
            for (long score : playerScores) {
                max = Math.max(max, score);
            }
            return max;
        }

        @Override
        public String toString() {
            String scores =  Arrays.toString(playerScores);
            Marble m = current;
            StringBuilder sb = new StringBuilder();
            return scores;
        }
    }

    private static class Marble {
        final int value;
        Marble left;
        Marble right;

        private Marble(int value, Marble left, Marble right) {
            this.value = value;
            this.left = left;
            this.left.right = this;
            this.right = right;
            this.right.left = this;
        }

        public Marble(int value) {
            this.value = value;
            this.left = this;
            this.right = this;
        }
    }

    private static final String TEST_INPUT = "";

    private static final String INPUT = "405 players; last marble is worth 70953 points";
}
