package aoc2022;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day19 {
    public static void main(String[] args) {
        Util.time(() -> a(TEST_INPUT));
        Util.time(() -> a(INPUT));
        Util.time(() -> b(TEST_INPUT));
        Util.time(() -> b(INPUT));
    }

    private static void a(String input) {
        List<Blueprint> blueprints = input.lines().map(Blueprint::parse).toList();
        int sum = blueprints.stream().map(b -> new QualityTester(b, 24)).mapToInt(QualityTester::quality).sum();
        System.out.println(sum);

    }

    private static void b(String input) {
        List<Blueprint> blueprints = input.lines().map(Blueprint::parse).limit(3).toList();
        int res = 1;
        for (Blueprint blueprint : blueprints) {
            QualityTester qualityTester = new QualityTester(blueprint, 32);
            qualityTester.quality();
            res *= qualityTester.max;
        }
        System.out.println(res);
    }

    private static class QualityTester {
        final Set<State> seen = new HashSet<>();
        final Blueprint blueprint;
        final int timeLimit;
        int max = 0;

        QualityTester(Blueprint blueprint, int timeLimit) {
            this.blueprint = blueprint;
            this.timeLimit = timeLimit;
        }

        int quality() {
            expand(new State(timeLimit, 0, 0, 0, 0, 1, 0, 0, 0));
            return max * blueprint.id;
        }

        private void expand(State state) {
            if (state.minutes == 0 || state.potential() <= max || !seen.add(state)) {
                max = Math.max(max, state.numGeode);
                return;
            }

            if (blueprint.geode.canBeBuilt(state)) {
                expand(blueprint.geode.build(state));
            } else {
                boolean needMoreObsidianRobots = blueprint.maxRequired.get(Material.obsidian) > state.obsidianRobots;
                boolean needMoreClayRobots = blueprint.maxRequired.get(Material.clay) > state.clayRobots;
                boolean needMoreOreRobots = blueprint.maxRequired.get(Material.ore) > state.oreRobots;

                boolean canBuildObsidian = blueprint.obsidian.canBeBuilt(state);
                boolean canBuildClay = blueprint.clay.canBeBuilt(state);
                boolean canBuildOre = blueprint.ore.canBeBuilt(state);
                boolean canBuildEveryType = canBuildObsidian && canBuildClay && canBuildOre;

                if (needMoreObsidianRobots && canBuildObsidian) {
                    expand(blueprint.obsidian.build(state));
                }
                if (needMoreClayRobots && canBuildClay) {
                    expand(blueprint.clay.build(state));
                }
                if (needMoreOreRobots && canBuildOre) {
                    expand(blueprint.ore.build(state));
                }
                if (!canBuildEveryType) {
                    expand(state.tick());
                }
            }
        }
    }

    private record State(int minutes, int numOre, int numClay, int numObsidian, int numGeode,
                         int oreRobots, int clayRobots, int obsidianRobots, int geodeRobots) {

        int num(Material material) {
            return switch (material) {
                case ore -> numOre;
                case clay -> numClay;
                case obsidian -> numObsidian;
                case geode -> numGeode;
            };
        }

        State tick() {
            return new State(minutes - 1,
                    numOre + oreRobots, numClay + clayRobots, numObsidian + obsidianRobots, numGeode + geodeRobots,
                    oreRobots, clayRobots, obsidianRobots, geodeRobots);
        }

        int potential() {
            int num = numGeode;
            int numRobots = geodeRobots;
            for (int i = minutes; i > 0; i--) {
                num += numRobots;
                numRobots++;
            }
            return num;
        }
    }

    private record Blueprint(int id, Robot ore, Robot clay, Robot obsidian, Robot geode, Map<Material, Integer> maxRequired) {

        static Blueprint parse(String input) {
            String[] idAndRobots = input.split(":");
            int id = Util.extractInts(idAndRobots[0])[0];
            Robot[] robots = Arrays.stream(idAndRobots[1].split("\\.")).map(Robot::parse).toArray(Robot[]::new);
            Map<Material, Integer> maxResourcesRequired = new HashMap<>();
            for (Robot robot : robots) {
                for (Cost cost : robot.costs) {
                    maxResourcesRequired.compute(cost.material, (material, max) -> Math.max(cost.amount, max == null ? 0 : max));
                }
            }
            return new Blueprint(id, robots[0], robots[1], robots[2], robots[3], maxResourcesRequired);
        }
    }

    private record Robot(List<Cost> costs, Material product) {
        static Robot parse(String input) {
            Material product = Material.parse(input.substring(input.indexOf("Each") + 5, input.indexOf(" robot")));
            String costsPart = input.substring(input.indexOf("costs") + 6);
            String[] costs = costsPart.split(" and ");
            return new Robot(Arrays.stream(costs).map(Cost::parse).toList(), product);
        }

        boolean canBeBuilt(State state) {
            return costs.stream().noneMatch(cost -> state.num(cost.material) < cost.amount);
        }

        State build(State state) {
            State next = state.tick();
            int numOre = next.numOre;
            int numClay = next.numClay;
            int numObsidian = next.numObsidian;
            int numGeode = next.numGeode;
            for (Cost cost : costs) {
                switch (cost.material) {
                case ore -> numOre -= cost.amount;
                case clay -> numClay -= cost.amount;
                case obsidian -> numObsidian -= cost.amount;
                case geode -> numGeode -= cost.amount;
                }
                ;
            }

            int oreRobots = next.oreRobots;
            int clayRobots = next.clayRobots;
            int obsidianRobots = next.obsidianRobots;
            int geodeRobots = next.geodeRobots;
            switch (product) {
            case ore -> oreRobots++;
            case clay -> clayRobots++;
            case obsidian -> obsidianRobots++;
            case geode -> geodeRobots++;
            }
            ;
            return new State(next.minutes, numOre, numClay, numObsidian, numGeode,
                    oreRobots, clayRobots, obsidianRobots, geodeRobots);
        }
    }

    private record Cost(int amount, Material material) {
        static Cost parse(String input) {
            int amount = Util.extractInts(input)[0];
            Material material = Material.parse(input.substring(input.lastIndexOf(" ") + 1));
            return new Cost(amount, material);
        }
    }

    private enum Material {
        ore, clay, obsidian, geode;

        static Material parse(String input) {
            return valueOf(input.toLowerCase());
        }
    }

    private static final String TEST_INPUT = "Blueprint 1:"
                                             + "  Each ore robot costs 4 ore."
                                             + "  Each clay robot costs 2 ore."
                                             + "  Each obsidian robot costs 3 ore and 14 clay."
                                             + "  Each geode robot costs 2 ore and 7 obsidian."
                                             + "\n"
                                             + "Blueprint 2:"
                                             + "  Each ore robot costs 2 ore."
                                             + "  Each clay robot costs 3 ore."
                                             + "  Each obsidian robot costs 3 ore and 8 clay."
                                             + "  Each geode robot costs 3 ore and 12 obsidian.";

    private static final String INPUT =
            "Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 17 clay. Each geode robot costs 4 ore and 20 obsidian.\n"
            + "Blueprint 2: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 17 clay. Each geode robot costs 3 ore and 8 obsidian.\n"
            + "Blueprint 3: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 7 clay. Each geode robot costs 4 ore and 13 obsidian.\n"
            + "Blueprint 4: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 10 clay. Each geode robot costs 3 ore and 14 obsidian.\n"
            + "Blueprint 5: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 17 clay. Each geode robot costs 3 ore and 16 obsidian.\n"
            + "Blueprint 6: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 16 clay. Each geode robot costs 2 ore and 15 obsidian.\n"
            + "Blueprint 7: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 15 clay. Each geode robot costs 2 ore and 15 obsidian.\n"
            + "Blueprint 8: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 19 clay. Each geode robot costs 2 ore and 18 obsidian.\n"
            + "Blueprint 9: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 7 clay. Each geode robot costs 2 ore and 19 obsidian.\n"
            + "Blueprint 10: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 6 clay. Each geode robot costs 3 ore and 16 obsidian.\n"
            + "Blueprint 11: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 8 clay. Each geode robot costs 3 ore and 19 obsidian.\n"
            + "Blueprint 12: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 19 clay. Each geode robot costs 2 ore and 12 obsidian.\n"
            + "Blueprint 13: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 4 ore and 17 obsidian.\n"
            + "Blueprint 14: Each ore robot costs 2 ore. Each clay robot costs 2 ore. Each obsidian robot costs 2 ore and 20 clay. Each geode robot costs 2 ore and 14 obsidian.\n"
            + "Blueprint 15: Each ore robot costs 2 ore. Each clay robot costs 2 ore. Each obsidian robot costs 2 ore and 10 clay. Each geode robot costs 2 ore and 11 obsidian.\n"
            + "Blueprint 16: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 13 clay. Each geode robot costs 3 ore and 11 obsidian.\n"
            + "Blueprint 17: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 19 clay. Each geode robot costs 3 ore and 10 obsidian.\n"
            + "Blueprint 18: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 20 clay. Each geode robot costs 2 ore and 17 obsidian.\n"
            + "Blueprint 19: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 11 clay. Each geode robot costs 4 ore and 12 obsidian.\n"
            + "Blueprint 20: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 7 clay. Each geode robot costs 3 ore and 10 obsidian.\n"
            + "Blueprint 21: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 13 clay. Each geode robot costs 3 ore and 7 obsidian.\n"
            + "Blueprint 22: Each ore robot costs 2 ore. Each clay robot costs 2 ore. Each obsidian robot costs 2 ore and 15 clay. Each geode robot costs 2 ore and 7 obsidian.\n"
            + "Blueprint 23: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 20 clay. Each geode robot costs 3 ore and 18 obsidian.\n"
            + "Blueprint 24: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 18 clay. Each geode robot costs 4 ore and 8 obsidian.\n"
            + "Blueprint 25: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 4 ore and 15 obsidian.\n"
            + "Blueprint 26: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 20 clay. Each geode robot costs 3 ore and 9 obsidian.\n"
            + "Blueprint 27: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 5 clay. Each geode robot costs 3 ore and 7 obsidian.\n"
            + "Blueprint 28: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 11 clay. Each geode robot costs 2 ore and 8 obsidian.\n"
            + "Blueprint 29: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 12 clay. Each geode robot costs 3 ore and 15 obsidian.\n"
            + "Blueprint 30: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 10 clay. Each geode robot costs 3 ore and 10 obsidian.";
}
