package aoc2018;

import static aoc2018.Day24.DamageType.bludgeoning;
import static aoc2018.Day24.DamageType.cold;
import static aoc2018.Day24.DamageType.fire;
import static aoc2018.Day24.DamageType.radiation;
import static aoc2018.Day24.DamageType.slashing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;


public class Day24 {
    // 279 to low
    public static void main(String[] args) {
        for (int boost = 36; boost < 37; boost++) {
            System.out.println(boost);
            List<Group> groups = input();
            for (Group group : groups) {
                if (group.immuneSystem) {
                    group.damagePerUnit += boost;
                }
            }
            long immuneLeft = groups.stream().filter(g -> g.immuneSystem).count();
            long infectionLeft = groups.stream().filter(g -> !g.immuneSystem).count();
            while (immuneLeft > 0 && infectionLeft > 0) {
//                System.out.println();
//                for (Group g : groups) {
//                    System.out.println((g.immuneSystem ? "immune" : "infection") + " with " + g.units + " units");
//                }
                // select targets
                groups.sort((g1, g2) -> {
                    int cmp = Integer.compare(g2.effectivePower(), g1.effectivePower());
                    if (cmp != 0) {
                        return cmp;
                    }
                    return Integer.compare(g2.initiatve, g1.initiatve);
                });
                Map<Group, Group> toAttack = new TreeMap<>(Comparator.comparingInt(g -> -g.initiatve));
                Set<Group> possibleTargets = new HashSet<>(groups);
                for (Group group : groups) {
                    Group target = null;
                    int damageToTarget = 0;
                    int targetEffectivePower = 0;
                    int targetInitiative = 0;
                    for (Group t : possibleTargets) {
                        if (t.immuneSystem != group.immuneSystem) {
                            int damage = group.effectivePower();
                            if (t.immuneTo.contains(group.damageType)) {
                                damage = 0;
                            } else if (t.weakAgainst.contains(group.damageType)) {
                                damage *= 2;
                            }
                            if (damage == 0 || damage < damageToTarget) {
                                continue;
                            }

                            int effectivePowerT = t.effectivePower();
                            if (damage == damageToTarget) {
                                if (effectivePowerT < targetEffectivePower) {
                                    continue;
                                }
                                if (effectivePowerT == targetEffectivePower) {
                                    if (t.initiatve < targetInitiative) {
                                        continue;
                                    }
                                }
                            }
                            target = t;
                            damageToTarget = damage;
                            targetEffectivePower = effectivePowerT;
                            targetInitiative = t.initiatve;
                        }
                    }
                    if (target != null) {
                        toAttack.put(group, target);
                        possibleTargets.remove(target);
                    }
                }
                // attack
                for (Entry<Group, Group> e : toAttack.entrySet()) {
                    e.getKey().attack(e.getValue());
                }
                groups = groups.stream().filter(g -> g.units > 0).collect(Collectors.toList());
                immuneLeft = groups.stream().filter(g -> g.immuneSystem).count();
                infectionLeft = groups.stream().filter(g -> !g.immuneSystem).count();
            }
            int unitsLeft = groups.stream().mapToInt(g -> g.units).sum();
            if (boost == 0) {
                System.out.println("part 1: " + unitsLeft);
            }
            if (infectionLeft == 0) {
                System.out.println("part 2: " + unitsLeft);
                return;
            }
        }
    }

    private static class Group {
        private String name;
        final boolean immuneSystem;
        int units;
        final int helthPerUnit;
        final EnumSet<DamageType> immuneTo;
        final EnumSet<DamageType> weakAgainst;
        int damagePerUnit;
        final DamageType damageType;
        final int initiatve;

        private Group(boolean immuneSystem, int units, int helthPerUnit, EnumSet<DamageType> immuneTo,
                      EnumSet<DamageType> weakAgainst, int damagePerUnit, DamageType damageType, int initiatve) {
            this("", immuneSystem, units, helthPerUnit, immuneTo, weakAgainst, damagePerUnit, damageType, initiatve);
        }
        private Group(String name, boolean immuneSystem, int units, int helthPerUnit, EnumSet<DamageType> immuneTo,
                      EnumSet<DamageType> weakAgainst, int damagePerUnit, DamageType damageType, int initiatve) {
            this.name = name;
            this.immuneSystem = immuneSystem;
            this.units = units;
            this.helthPerUnit = helthPerUnit;
            this.immuneTo = immuneTo;
            this.weakAgainst = weakAgainst;
            this.damagePerUnit = damagePerUnit;
            this.damageType = damageType;
            this.initiatve = initiatve;
        }

        int effectivePower() {
            return units * damagePerUnit;
        }

        void attack(Group target) {
            int damage = effectivePower() * (target.weakAgainst.contains(damageType) ? 2 : 1);
            int unitsLost = damage / target.helthPerUnit;
//            System.out.println(name + " with " +units + " " + (immuneSystem ? "immune system" : "infection") + " attack " + target.name + ". Dealing " + damage + ". " + unitsLost + " lost");
            target.units -= Math.min(target.units, unitsLost);
        }
    }

    enum DamageType {
        bludgeoning, fire, cold, slashing, radiation
    }
    private static final EnumSet<DamageType> empty = EnumSet.noneOf(DamageType.class);
    private static List<Group> test() {
        List<Group> groups = new ArrayList<>();
        groups.add(new Group("immune 1", true, 17, 5390, empty, EnumSet.of(radiation, bludgeoning), 4507, fire, 2));
        groups.add(new Group("immune 2", true, 989, 1274, EnumSet.of(fire), EnumSet.of(bludgeoning, slashing), 25, slashing, 3));

        groups.add(new Group("infection 1", false, 801, 4706, empty, EnumSet.of(radiation), 116, bludgeoning, 1));
        groups.add(new Group("infection 2", false, 4485, 2961, EnumSet.of(radiation), EnumSet.of(fire, cold), 12, slashing, 4));
        return groups;
    }

    private static final String TEST_INPUT = "Immune System:\n"
                                             + "17 units each with 5390 hit points (weak to radiation, bludgeoning) with\n"
                                             + " an attack that does 4507 fire damage at initiative 2\n"
                                             + "989 units each with 1274 hit points (immune to fire; weak to bludgeoning,\n"
                                             + " slashing) with an attack that does 25 slashing damage at initiative 3\n"
                                             + "\n"
                                             + "Infection:\n"
                                             + "801 units each with 4706 hit points (weak to radiation) with an attack\n"
                                             + " that does 116 bludgeoning damage at initiative 1\n"
                                             + "4485 units each with 2961 hit points (immune to radiation; weak to fire,\n"
                                             + " cold) with an attack that does 12 slashing damage at initiative 4";

    private static List<Group> input() {
        List<Group> groups = new ArrayList<>();
        groups.add(new Group(true, 790, 3941, empty, empty, 48, bludgeoning, 5));
        groups.add(new Group(true, 624, 2987, empty, empty, 46, bludgeoning, 16));
        groups.add(new Group(true, 5724, 9633, EnumSet.of(bludgeoning, slashing, fire), empty, 16, slashing, 9));
        groups.add(new Group(true, 1033, 10664, empty, empty, 89, slashing, 1));
        groups.add(new Group(true, 6691, 9773, empty, EnumSet.of(slashing), 13, bludgeoning, 12));
        groups.add(new Group(true, 325, 11916, empty, EnumSet.of(bludgeoning), 276, slashing, 8));
        groups.add(new Group(true, 1517, 6424, empty, empty, 35, bludgeoning, 13));
        groups.add(new Group(true, 1368, 9039, EnumSet.of(bludgeoning), empty, 53, slashing, 4));
        groups.add(new Group(true, 3712, 5377, EnumSet.of(cold, radiation), EnumSet.of(fire), 14, slashing, 14));
        groups.add(new Group(true, 3165, 8703, empty, EnumSet.of(slashing, bludgeoning), 26, radiation, 11));

        groups.add(new Group(false, 1113, 44169, EnumSet.of(bludgeoning), EnumSet.of(radiation), 57, fire, 7));
        groups.add(new Group(false, 3949, 20615, empty, EnumSet.of(radiation, cold), 9, bludgeoning, 6));
        groups.add(new Group(false, 602, 35167,  EnumSet.of(bludgeoning, cold), EnumSet.of(fire), 93, radiation, 20));
        groups.add(new Group(false, 1209, 34572,  empty, empty, 55, bludgeoning, 3));
        groups.add(new Group(false, 902, 12983,  EnumSet.of(fire), empty, 28, fire, 19));
        groups.add(new Group(false, 1132, 51353,  empty, empty, 66, radiation, 15));
        groups.add(new Group(false, 7966, 49894,  EnumSet.of(bludgeoning), empty, 9, cold, 10));
        groups.add(new Group(false, 3471, 18326, empty, EnumSet.of(radiation), 8, fire, 18));
        groups.add(new Group(false, 110, 38473, EnumSet.of(fire), EnumSet.of(bludgeoning), 640, slashing, 2));
        groups.add(new Group(false, 713, 42679, empty, EnumSet.of(slashing), 102, bludgeoning, 17));
        return groups;
    }

    private static final String INPUT =
            "Immune System:\n"
            + "790 units each with 3941 hit points with an attack that does 48 bludgeoning damage at initiative 5\n"
            + "624 units each with 2987 hit points with an attack that does 46 bludgeoning damage at initiative 16\n"
            + "5724 units each with 9633 hit points (immune to bludgeoning, slashing, fire) with an attack that does 16 slashing damage at initiative 9\n"
            + "1033 units each with 10664 hit points with an attack that does 89 slashing damage at initiative 1\n"
            + "6691 units each with 9773 hit points (weak to slashing) with an attack that does 13 bludgeoning damage at initiative 12\n"
            + "325 units each with 11916 hit points (weak to bludgeoning) with an attack that does 276 slashing damage at initiative 8\n"
            + "1517 units each with 6424 hit points with an attack that does 35 bludgeoning damage at initiative 13\n"
            + "1368 units each with 9039 hit points (immune to bludgeoning) with an attack that does 53 slashing damage at initiative 4\n"
            + "3712 units each with 5377 hit points (immune to cold, radiation; weak to fire) with an attack that does 14 slashing damage at initiative 14\n"
            + "3165 units each with 8703 hit points (weak to slashing, bludgeoning) with an attack that does 26 radiation damage at initiative 11\n"
            + "\n"
            + "Infection:\n"
            + "1113 units each with 44169 hit points (immune to bludgeoning; weak to radiation) with an attack that does 57 fire damage at initiative 7\n"
            + "3949 units each with 20615 hit points (weak to radiation, cold) with an attack that does 9 bludgeoning damage at initiative 6\n"
            + "602 units each with 35167 hit points (immune to bludgeoning, cold; weak to fire) with an attack that does 93 radiation damage at initiative 20\n"
            + "1209 units each with 34572 hit points with an attack that does 55 bludgeoning damage at initiative 3\n"
            + "902 units each with 12983 hit points (immune to fire) with an attack that does 28 fire damage at initiative 19\n"
            + "1132 units each with 51353 hit points with an attack that does 66 radiation damage at initiative 15\n"
            + "7966 units each with 49894 hit points (immune to bludgeoning) with an attack that does 9 cold damage at initiative 10\n"
            + "3471 units each with 18326 hit points (weak to radiation) with an attack that does 8 fire damage at initiative 18\n"
            + "110 units each with 38473 hit points (weak to bludgeoning; immune to fire) with an attack that does 640 slashing damage at initiative 2\n"
            + "713 units each with 42679 hit points (weak to slashing) with an attack that does 102 bludgeoning damage at initiative 17";
}
