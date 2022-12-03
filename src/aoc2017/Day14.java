package aoc2017;

public class Day14 {
    public static void main(String[] args) throws Exception {
        part1();
        part2();
    }

    private static void part1() throws Exception {
        int used = 0;
        for (int row = 0; row < 128; row++) {
            String hexString = Day10.part2(DATA + "-" + row);
            for (int i = 0; i < hexString.length(); i++) {
                int c = Integer.parseInt(hexString.substring(i, i + 1), 16);
                for (int bit = 3; bit >= 0; bit--) {
                    if (getBit(c, bit) == 1) {
                        used++;
                    }
                }
            }
        }
        System.out.println(used);
    }

    private static int getBit(int n, int bit) {
        return (n >> bit) & 1;
    }

    private static void part2() throws Exception {
        boolean[][] grid = new boolean[128][];
        for (int row = 0; row < 128; row++) {
            grid[row] = new boolean[128];
            String hexString = Day10.part2(DATA + "-" + row);
            int rowIx = 0;
            for (int i = 0; i < hexString.length(); i++) {
                int c = Integer.parseInt(hexString.substring(i, i + 1), 16);
                for (int bit = 3; bit >= 0; bit--) {
                    if (getBit(c, bit) == 1) {
                        grid[row][rowIx] = true;
                    }
                    rowIx++;
                }
            }
        }

        int sectors = 0;
        while (removeSector(grid)) {
            sectors++;
        }
        System.out.println(sectors);
    }

    private static boolean removeSector(boolean[][] grid) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                if (grid[row][col]) {
                    removeSector(grid, row, col);
                    return true;
                }
            }
        }
        return false;
    }

    private static void removeSector(boolean[][] grid, int row, int col) {
        grid[row][col] = false;
        if (row > 0 && grid[row - 1][col]) {
            removeSector(grid, row - 1, col);
        }

        if (row < grid.length - 1 && grid[row + 1][col]) {
            removeSector(grid, row + 1, col);
        }

        if (col > 0 && grid[row][col - 1]) {
            removeSector(grid, row, col - 1);
        }

        if (col < grid.length - 1 && grid[row][col + 1]) {
            removeSector(grid, row, col + 1);
        }
    }

    private static final String DATA2 = "flqrgnkx";

    private static final String DATA = "hxtvlmkl";
}
