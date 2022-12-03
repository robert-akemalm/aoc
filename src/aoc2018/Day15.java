package aoc2018;

import java.util.Arrays;

public class Day15 {
    public static void main(String[] args) {
        String input = INPUT;
        a(input);
        b(input);
    }

    private static void b(String input) {
        BattleField battleField = null;
        for (int power = 4; power < 10_000; power++) {
            battleField = new BattleField(Util.parseStrings(input), power);
            int numberOfElfs = battleField.elfsLeft;
            while (battleField.runRound()) {
            }
            if (numberOfElfs == battleField.elfsLeft) {
                break;
            }
        }
        System.out.println(battleField.roundsPlayed() * battleField.hpLeft());

    }

    private static void a(String input) {
        BattleField battleField = new BattleField(Util.parseStrings(input), 3);
        while (battleField.runRound()) {
        }
        System.out.println(battleField.roundsPlayed() * battleField.hpLeft());
    }


    private interface PositionHolder {}

    private static final PositionHolder WALL = new PositionHolder() {
        @Override
        public String toString() {
            return "#";
        }
    };

    private static class Unit implements PositionHolder {
        final boolean elf;
        private final int power;
        int hp = 200;
        int roundsPlayed = 0;

        private Unit(boolean elf, int power) {
            this.elf = elf;
            this.power = power;
        }

        boolean damage(int amount) {
            hp -= amount;
            return hp <= 0;
        }

        @Override
        public String toString() {
            return elf ? "E" : "G";
        }
    }

    private static class BattleField {
        private static final int[] POSITION_CHECK_ORDER = new int[]{-1, 0, 0, -1, 0, 1, 1, 0};
        final PositionHolder[][] field;
        final int[][] distanceCalculator;

        int elfsLeft = 0;
        int goblinsLeft = 0;
        int roundsCompleted = 0;

        private BattleField(String[] state, int elfPower) {
            this.field = new PositionHolder[state.length][];
            this.distanceCalculator = new int[state.length][];
            for (int y = 0; y < state.length; y++) {
                String row = state[y];
                this.field[y] = new PositionHolder[row.length()];
                this.distanceCalculator[y] = new int[row.length()];
                for (int x = 0; x < row.length(); x++) {
                    char ph = row.charAt(x);
                    PositionHolder positionHolder = null;
                    if (ph == '#') {
                        positionHolder = WALL;
                    } else if (ph == 'G') {
                        positionHolder = new Unit(false, 3);
                        goblinsLeft++;
                    } else if (ph == 'E') {
                        positionHolder = new Unit(true, elfPower);
                        elfsLeft++;
                    }
                    this.field[y][x] = positionHolder;
                }
            }
        }

        int hpLeft() {
            int sum = 0;
            for(PositionHolder[] row : field) {
                for (PositionHolder positionHolder : row) {
                    if (positionHolder instanceof Unit) {
                        sum += ((Unit) positionHolder).hp;
                    }
                }
            }
            return sum;
        }

        boolean runRound() {
            for (int y = 0; y < field.length; y++) {
                for (int x = 0; x < field[y].length; x++) {
                    PositionHolder ph = field[y][x];
                    if (ph instanceof Unit) {
                        if (((Unit) ph).roundsPlayed > roundsCompleted) {
                            continue;
                        }
                        ((Unit) ph).roundsPlayed++;
                        boolean unitIsElf = ((Unit) ph).elf;
                        // move
                        clearDistanceCalculator();
                        distanceCalculator[y][x] = 0;
                        int distanceToEnemy = Integer.MAX_VALUE;
                        for (int i = 0; i < POSITION_CHECK_ORDER.length; i+=2) {
                            int yOffset = POSITION_CHECK_ORDER[i];
                            int xOffset = POSITION_CHECK_ORDER[i + 1];
                            PositionHolder neighbour = field[y + yOffset][x + xOffset];
                            if (neighbour instanceof Unit && ((Unit) neighbour).elf != unitIsElf) {
                                distanceToEnemy = 0;
                            }
                        }

                        int minX = x;
                        int minY = y;

                        int distance = calculateDistances(y - 1, x, unitIsElf, 1, distanceToEnemy);
                        if (distance < distanceToEnemy) {
                            distanceToEnemy = distance;
                            minX = x;
                            minY = y - 1;
                        }

                        distance = calculateDistances(y, x - 1, unitIsElf, 1, distanceToEnemy);
                        if (distance < distanceToEnemy) {
                            distanceToEnemy = distance;
                            minX = x - 1;
                            minY = y;
                        }

                        distance = calculateDistances(y, x + 1, unitIsElf, 1, distanceToEnemy);
                        if (distance < distanceToEnemy) {
                            distanceToEnemy = distance;
                            minX = x + 1;
                            minY = y;
                        }

                        distance = calculateDistances(y + 1, x, unitIsElf, 1, distanceToEnemy);
                        if (distance < distanceToEnemy) {
                            distanceToEnemy = distance;
                            minX = x;
                            minY = y + 1;
                        }

                        field[y][x] = null;
                        field[minY][minX] = ph;

                        if (distanceToEnemy > 1) {
                            continue;
                        }

                        // attack
                        int toAttackX = -1;
                        int toAttackY = -1;
                        Unit toAttack = null;
                        for (int i = 0; i < POSITION_CHECK_ORDER.length; i+=2) {
                            int yOffset = POSITION_CHECK_ORDER[i];
                            int xOffset = POSITION_CHECK_ORDER[i + 1];
                            PositionHolder neighbour = field[minY + yOffset][minX + xOffset];
                            if (neighbour instanceof Unit && ((Unit) neighbour).elf != unitIsElf && shouldAttackB(toAttack, (Unit) neighbour)) {
                                toAttackX = minX + xOffset;
                                toAttackY = minY + yOffset;
                                toAttack = (Unit) neighbour;
                            }
                        }

                        if (toAttack != null) {
                            boolean killed = toAttack.damage(((Unit) ph).power);
                            if (killed) {
                                field[toAttackY][toAttackX] = null;
                                if (unitIsElf) {
                                    goblinsLeft--;
                                } else {
                                    elfsLeft--;
                                }
                            }
                            if(goblinsLeft == 0 || elfsLeft == 0) {
                                boolean movesLeft = false;
                                while (y < field.length) {
                                    while (x < field[y].length) {
                                        if (field[y][x] instanceof Unit && ((Unit)field[y][x]).roundsPlayed == roundsCompleted) {
                                            movesLeft = true;
                                            break;
                                        }
                                        x++;
                                    }
                                    y++;
                                }
                                if (!movesLeft) {
                                    roundsCompleted++;
                                }
                                return y == field.length - 1 && x == field[y].length - 1;
                            }
                        }
                    }
                }
            }

            roundsCompleted++;
            return true;
        }

        private int calculateDistances(int y, int x, boolean unitIsElf, int stepsTaken, int closestFound) {
            if (distanceCalculator[y][x] <= stepsTaken || stepsTaken >= closestFound || field[y][x] != null) {
                return Integer.MAX_VALUE;
            }
            distanceCalculator[y][x] = stepsTaken;
            for (int i = 0; i < POSITION_CHECK_ORDER.length; i+=2) {
                int yOffset = POSITION_CHECK_ORDER[i];
                int xOffset = POSITION_CHECK_ORDER[i + 1];
                PositionHolder neighbour = field[y + yOffset][x + xOffset];
                if (neighbour instanceof Unit && ((Unit) neighbour).elf != unitIsElf) {
                    return stepsTaken;
                }
            }

            int closestEnemy = Integer.MAX_VALUE;
            for (int i = 0; i < POSITION_CHECK_ORDER.length; i+=2) {
                int yOffset = POSITION_CHECK_ORDER[i];
                int xOffset = POSITION_CHECK_ORDER[i + 1];
                PositionHolder neighbour = field[y + yOffset][x + xOffset];
                if (neighbour == null) {
                    closestEnemy = Math.min(closestEnemy, calculateDistances(y + yOffset, x + xOffset, unitIsElf, stepsTaken + 1, closestFound));
                }
            }
            return closestEnemy;
        }

        private void clearDistanceCalculator() {
            for (int[] row : distanceCalculator) {
                Arrays.fill(row, Integer.MAX_VALUE);
            }
        }

        boolean shouldAttackB(Unit a, Unit b) {
            return a == null || a.hp > b.hp;
        }

        int roundsPlayed() {
            return roundsCompleted;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int y = 0; y < field.length; y++) {
                for (int x = 0; x < field[y].length; x++) {
                    if (field[y][x] == null) {
                        sb.append('.');
                    } else {
                        sb.append(field[y][x].toString());
                    }
                }
                sb.append('\n');
            }
            return sb.toString();
        }
    }

    // 47 rounds, 200+131+59+200 = 590 => 47 * 590 = 27730
    private static final String TEST_INPUT = "#######\n" +
                                             "#.G...#\n" +
                                             "#...EG#\n" +
                                             "#.#.#G#\n" +
                                             "#..G#E#\n" +
                                             "#.....#\n" +
                                             "#######";

    private static final String INPUT = "################################\n"
                                        + "##############..######....######\n"
                                        + "###########GG.G.#######.########\n"
                                        + "############....######..#..#####\n"
                                        + "############...#######.....#####\n"
                                        + "##############..#G.####....#####\n"
                                        + "#############..G#..####...######\n"
                                        + "######.#####.G...G..###.#.######\n"
                                        + "######...###..........#.########\n"
                                        + "######G.................#.######\n"
                                        + "######....G.#............G.#####\n"
                                        + "######G......G............######\n"
                                        + "######.......E#####E.G.....#####\n"
                                        + "#####...G....#######.......#####\n"
                                        + "#####.......#########......#####\n"
                                        + "########....#########.....######\n"
                                        + "########G.G.#########...########\n"
                                        + "#########...#########.......#.##\n"
                                        + "########.G..#########..........#\n"
                                        + "#######.E....#######........#..#\n"
                                        + "#...........G.#####...E...######\n"
                                        + "####.....##................#####\n"
                                        + "#####..#.####.#.............####\n"
                                        + "########...##EE..G....E.#..E.###\n"
                                        + "##########..#................###\n"
                                        + "##########.............#.....###\n"
                                        + "###########.E.G..........##.####\n"
                                        + "###########.........###..##.####\n"
                                        + "############.##........E.#######\n"
                                        + "################.###.###########\n"
                                        + "################.###############\n"
                                        + "################################";
}
