package aoc2017;

import java.util.Arrays;

public class Day3 {
    //1, 2
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(1000);
        for (int data = 3; data <= 53; data++) {
            int rbj = rbj(data);
            int rak = part1(data);
            if (rbj != rak) {
                System.out.println(rak);
            }
        }
        part2();
    }

    private static int rbj(double number) {
        double sqrt = Math.round(Math.sqrt(number));
        double half = sqrt / 2;

        double ret;
        if ((number % sqrt) < half) {
            double a = -number % half;
            if (a < 0)
                a = -a;
            ret = a + half + 1;
        } else {
            ret = (number % half) + half - 1;
        }

        return (int) Math.round(ret);
    }

    private static int part1(int data) {
        int size = estimateSize(data);
        int[][] distance = new int[size][size];
        for (int i = 0; i < size; i++) {
            distance[i] = new int[size];
            Arrays.fill(distance[i], -1);
        }

        int x = size / 2;
        int y = size / 2;
        distance[x][y] = 0;
        Move move = Move.DOWN;
        int number = 1;
        while (number <= data) {
            number++;
            int xIfTurn = x + move.nextDirection().diffInX();
            int yIfTurn = y + move.nextDirection().diffInY();
            if (distance[xIfTurn][yIfTurn] < 0) {
                move = move.nextDirection();
            }

            x += move.diffInX();
            y += move.diffInY();
            int d = Integer.MAX_VALUE;
            for (Move m : Move.values()) {
                int distanceFormNeighbour = distance[x + m.nextDirection().diffInX()][y + m.nextDirection().diffInY()];
                if (distanceFormNeighbour != -1) {
                    d = Math.min(d, distanceFormNeighbour + 1);
                }
            }
            if (number == data) {
                return d;
            }
            distance[x][y] = d;
        }

        return 0;
//        printMatrix(distance);
    }

    private static void part2() {
        int data = 289326;
        int size = estimateSize(data);
        int[][] distance = new int[size][size];
        for (int i = 0; i < size; i++) {
            distance[i] = new int[size];
            Arrays.fill(distance[i], -1);
        }

        int x = size / 2;
        int y = size / 2;
        distance[x][y] = 1;
        Move move = Move.DOWN;
        int number = 1;
        while (number <= data) {
            number++;
            int xIfTurn = x + move.nextDirection().diffInX();
            int yIfTurn = y + move.nextDirection().diffInY();
            if (distance[xIfTurn][yIfTurn] < 0) {
                move = move.nextDirection();
            }

            x += move.diffInX();
            y += move.diffInY();
            int sum = 0;
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    if (distance[i][j] > 0) {
                        sum += distance[i][j];
                    }
                }
            }
            if (sum > data) {
                System.out.println(sum);
                return;
            }
            distance[x][y] = sum;
        }

//        printMatrix(distance);
    }

    private static void printMatrix(int[][] distance) {
        for (int yPos = 0; yPos < distance.length; yPos++) {
            for (int xPos = 0; xPos < distance.length; xPos++) {
                System.out.print(distance[xPos][yPos]);
                System.out.print('\t');
            }
            System.out.println();
        }

    }

    private enum Move {
        RIGHT {
            @Override
            int diffInX() {
                return 1;
            }

            @Override
            int diffInY() {
                return 0;
            }

            @Override
            Move nextDirection() {
                return UP;
            }
        },
        UP {
            @Override
            int diffInX() {
                return 0;
            }

            @Override
            int diffInY() {
                return -1;
            }

            @Override
            Move nextDirection() {
                return LEFT;
            }
        },
        LEFT {
            @Override
            int diffInX() {
                return -1;
            }

            @Override
            int diffInY() {
                return 0;
            }

            @Override
            Move nextDirection() {
                return DOWN;
            }
        },
        DOWN {
            @Override
            int diffInX() {
                return 0;
            }

            @Override
            int diffInY() {
                return 1;
            }

            @Override
            Move nextDirection() {
                return RIGHT;
            }
        };

        abstract int diffInX();

        abstract int diffInY();

        abstract Move nextDirection();
    }

    private static int estimateSize(int data) {
        return (int) Math.sqrt(data) + 4;
    }
}
