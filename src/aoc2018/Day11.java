package aoc2018;

public class Day11 {
    public static void main(String[] args) {
        long t = System.currentTimeMillis();
        a();
        b();
        System.out.println(System.currentTimeMillis() - t);
    }

    private static void b() {
        int serialNumber = 5034;
        int[][] grid = new int[301][];
        for (int y = 1; y <= 300; y++) {
            grid[y] = new int[301];
            for (int x = 1; x <= 300; x++) {
                grid[y][x] = powerLevel(x, y, serialNumber);
            }
        }

        int max = Integer.MIN_VALUE;
        String bestInput = "";
        for (int y = 1; y <= 300; y++) {
            for (int x = 1; x <= 300; x++) {
                int maxSize = Math.min(300 - y, 300 - x);
                for (int size = 1; size <= maxSize; size++) {
                    int powerSum = powerSum(grid, x, y, size, max);
                    if (powerSum > max) {
                        max = powerSum;
                        bestInput = x + "," + y + "," + size;
                    }
                }
            }
        }
        System.out.println(max + " " + bestInput);
    }

    static int powerSum(int[][] grid, int topLeftX, int topLeftY, int size, int max) {
        int sum = 0;
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                sum += grid[topLeftY + y][topLeftX + x];
            }
        }
        return sum;

    }

    private static void a() {
        int serialNumber = 5034;
        int[][] grid = new int[301][];
        for (int y = 1; y <= 300; y++) {
            grid[y] = new int[301];
            for (int x = 1; x <= 300; x++) {
                grid[y][x] = powerLevel(x, y, serialNumber);
            }
        }

        int max = Integer.MIN_VALUE;
        String topLeft = "";
        for (int y = 2; y < 300; y++) {
            for (int x = 2; x < 300; x++) {
                int powerSum = powerSum(grid, x, y);
                if (powerSum > max) {
                    max = powerSum;
                    topLeft = (x - 1) + "," + (y - 1);
                }
            }
        }
        System.out.println(max + " " + topLeft);
    }

    private static int powerSum(int[][] grid, int centerX, int centerY) {
        int sum = 0;
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                sum += grid[centerY + y][centerX + x];
            }
        }
        return sum;
    }

    private static int powerLevel(int x, int y, int serialNumber) {
        int rackId = x + 10;
        int powerLevel = rackId * y;
        powerLevel += serialNumber;
        powerLevel *= rackId;
        int hundreds = (powerLevel / 100) % 10;
        return hundreds - 5;
    }
}
