package aoc2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import aoc2024.Util.Pos;

public class Day14 {
    private static void a(Input input) {
        int maxX = 101;
        int maxY = 103;
        if (input.equals(Input.parse(TEST_INPUT))) {
            maxX = 11;
            maxY = 7;
        }
        List<Robot> robots = input.robots();
        for (int i = 0; i < 100; i++) {
            List<Robot> next = new ArrayList<>();
            for (Robot robot : robots) {
                int dx = robot.dx();
                int dy = robot.dy();
                int newX = (robot.pos().x() + dx + maxX) % maxX;
                int newY = (robot.pos().y() + dy + maxY) % maxY;
                next.add(new Robot(new Pos(newX, newY), dx, dy));
            }
            robots = next;
        }

        Map<Integer, Integer> quadrantToCount = new HashMap<>();
        for (Robot robot : robots) {
            if (robot.pos.y() < maxY / 2) {
                if (robot.pos.x() < maxX / 2) {
                    quadrantToCount.merge(1, 1, Integer::sum);
                } else if (robot.pos.x() > maxX / 2) {
                    quadrantToCount.merge(2, 1, Integer::sum);
                }
            } else if (robot.pos.y() > maxY / 2) {
                if (robot.pos.x() < maxX / 2) {
                    quadrantToCount.merge(3, 1, Integer::sum);
                } else if (robot.pos.x() > maxX / 2) {
                    quadrantToCount.merge(4, 1, Integer::sum);
                }
            }
        }
        quadrantToCount.values().stream().reduce((a, b) -> a * b).ifPresent(System.out::println);
    }

    private static void b(Input input) {
        if (input.equals(Input.parse(TEST_INPUT))) {
            return;
        }
        int maxX = 101;
        int maxY = 103;
        List<Robot> robots = input.robots();
        for (int i = 1; i < 100_000_000; i++) {
            List<Robot> next = new ArrayList<>();
            for (Robot robot : robots) {
                int dx = robot.dx();
                int dy = robot.dy();
                int newX = (robot.pos().x() + dx + maxX) % maxX;
                int newY = (robot.pos().y() + dy + maxY) % maxY;
                next.add(new Robot(new Pos(newX, newY), dx, dy));
            }
            robots = next;
            Set<Pos> positions = robots.stream().map(Robot::pos).collect(Collectors.toSet());
            if (robots.stream().map(Robot::pos).filter(p-> p.neighbours().stream().anyMatch(positions::contains)).count() > robots.size()/2) {
                Util.print(robots.stream().map(Robot::pos).toList());
                System.out.println(i);
                break;
            }
        }
    }

    record Robot(Pos pos, int dx, int dy) {}

    record Input(List<Robot> robots) {
        static Input parse(String input) {
            return new Input(input.lines()
                                  .map(Util::extractInts)
                                  .map(l -> new Robot(new Pos(l[0], l[1]), l[2], l[3]))
                                  .toList());
        }

    }

    private static final String TEST_INPUT = "p=0,4 v=3,-3\n"
                                             + "p=6,3 v=-1,-3\n"
                                             + "p=10,3 v=-1,2\n"
                                             + "p=2,0 v=2,-1\n"
                                             + "p=0,0 v=1,3\n"
                                             + "p=3,0 v=-2,-2\n"
                                             + "p=7,6 v=-1,-3\n"
                                             + "p=3,0 v=-1,-2\n"
                                             + "p=9,3 v=2,3\n"
                                             + "p=7,3 v=-1,2\n"
                                             + "p=2,4 v=2,-3\n"
                                             + "p=9,5 v=-3,-3";

    private static final String INPUT = "p=31,100 v=-36,-71\n"
                                        + "p=29,22 v=9,29\n"
                                        + "p=26,16 v=-32,-28\n"
                                        + "p=89,102 v=-63,35\n"
                                        + "p=84,78 v=-59,-49\n"
                                        + "p=28,66 v=-87,81\n"
                                        + "p=34,58 v=-29,-17\n"
                                        + "p=13,20 v=26,37\n"
                                        + "p=11,29 v=23,-9\n"
                                        + "p=32,3 v=-17,36\n"
                                        + "p=7,54 v=-75,65\n"
                                        + "p=58,68 v=-56,-76\n"
                                        + "p=69,9 v=4,-39\n"
                                        + "p=11,51 v=-68,84\n"
                                        + "p=82,73 v=-60,-38\n"
                                        + "p=86,58 v=-99,-15\n"
                                        + "p=59,40 v=58,19\n"
                                        + "p=99,66 v=27,82\n"
                                        + "p=14,60 v=25,88\n"
                                        + "p=50,24 v=56,22\n"
                                        + "p=3,24 v=62,-75\n"
                                        + "p=94,73 v=-62,-76\n"
                                        + "p=32,66 v=58,-87\n"
                                        + "p=51,19 v=-46,90\n"
                                        + "p=4,27 v=27,86\n"
                                        + "p=5,33 v=30,-96\n"
                                        + "p=90,83 v=-8,-34\n"
                                        + "p=34,29 v=-29,-51\n"
                                        + "p=69,93 v=-4,-52\n"
                                        + "p=2,96 v=-34,-31\n"
                                        + "p=3,12 v=-79,86\n"
                                        + "p=86,87 v=-16,39\n"
                                        + "p=15,27 v=31,-1\n"
                                        + "p=74,73 v=41,-42\n"
                                        + "p=55,10 v=51,40\n"
                                        + "p=65,32 v=99,-47\n"
                                        + "p=39,5 v=-90,32\n"
                                        + "p=68,65 v=51,-64\n"
                                        + "p=81,11 v=99,36\n"
                                        + "p=31,28 v=-93,71\n"
                                        + "p=50,31 v=20,-64\n"
                                        + "p=52,67 v=-94,-11\n"
                                        + "p=35,2 v=-34,13\n"
                                        + "p=53,88 v=-89,-48\n"
                                        + "p=20,88 v=-61,-83\n"
                                        + "p=95,86 v=32,-18\n"
                                        + "p=85,11 v=-34,4\n"
                                        + "p=19,78 v=72,-49\n"
                                        + "p=91,27 v=86,41\n"
                                        + "p=11,14 v=-12,-18\n"
                                        + "p=90,57 v=87,80\n"
                                        + "p=70,11 v=-47,-74\n"
                                        + "p=49,91 v=10,43\n"
                                        + "p=91,60 v=28,67\n"
                                        + "p=78,91 v=-94,43\n"
                                        + "p=44,57 v=-92,38\n"
                                        + "p=22,42 v=50,59\n"
                                        + "p=25,33 v=-75,-66\n"
                                        + "p=97,81 v=-25,62\n"
                                        + "p=84,64 v=91,-72\n"
                                        + "p=98,48 v=84,-8\n"
                                        + "p=54,63 v=-44,27\n"
                                        + "p=61,69 v=-86,-99\n"
                                        + "p=47,72 v=20,-22\n"
                                        + "p=100,7 v=-22,-63\n"
                                        + "p=95,26 v=38,23\n"
                                        + "p=67,27 v=-50,18\n"
                                        + "p=10,98 v=84,-71\n"
                                        + "p=59,13 v=16,-61\n"
                                        + "p=14,45 v=71,-58\n"
                                        + "p=11,81 v=-26,66\n"
                                        + "p=29,72 v=-39,46\n"
                                        + "p=2,24 v=-80,-94\n"
                                        + "p=79,95 v=-9,-37\n"
                                        + "p=47,86 v=-86,-41\n"
                                        + "p=9,92 v=24,-14\n"
                                        + "p=18,69 v=-4,45\n"
                                        + "p=49,3 v=14,-71\n"
                                        + "p=90,0 v=-64,-44\n"
                                        + "p=75,79 v=96,-83\n"
                                        + "p=82,27 v=41,-70\n"
                                        + "p=92,85 v=66,71\n"
                                        + "p=5,57 v=-80,31\n"
                                        + "p=92,21 v=-15,41\n"
                                        + "p=91,43 v=88,-66\n"
                                        + "p=62,7 v=58,-52\n"
                                        + "p=87,23 v=89,-97\n"
                                        + "p=54,56 v=62,27\n"
                                        + "p=29,65 v=-36,-34\n"
                                        + "p=84,72 v=36,27\n"
                                        + "p=29,36 v=17,41\n"
                                        + "p=47,49 v=-92,-46\n"
                                        + "p=20,81 v=36,-80\n"
                                        + "p=12,42 v=27,46\n"
                                        + "p=87,58 v=88,38\n"
                                        + "p=96,25 v=-20,-70\n"
                                        + "p=44,72 v=-4,24\n"
                                        + "p=59,83 v=-99,35\n"
                                        + "p=31,74 v=-32,5\n"
                                        + "p=7,10 v=20,-30\n"
                                        + "p=43,44 v=84,-53\n"
                                        + "p=24,32 v=-33,-81\n"
                                        + "p=26,80 v=-82,73\n"
                                        + "p=99,95 v=-98,44\n"
                                        + "p=22,73 v=-49,54\n"
                                        + "p=96,48 v=79,41\n"
                                        + "p=85,21 v=42,48\n"
                                        + "p=53,72 v=-42,-53\n"
                                        + "p=58,35 v=-38,41\n"
                                        + "p=13,41 v=-53,86\n"
                                        + "p=57,12 v=-46,-21\n"
                                        + "p=21,28 v=19,-78\n"
                                        + "p=59,32 v=1,7\n"
                                        + "p=12,24 v=-27,10\n"
                                        + "p=9,101 v=-36,-60\n"
                                        + "p=6,22 v=-24,-94\n"
                                        + "p=31,67 v=-93,-65\n"
                                        + "p=84,1 v=92,24\n"
                                        + "p=24,55 v=68,-50\n"
                                        + "p=93,86 v=78,96\n"
                                        + "p=85,65 v=-14,-99\n"
                                        + "p=14,96 v=29,-3\n"
                                        + "p=30,20 v=57,-57\n"
                                        + "p=70,80 v=46,43\n"
                                        + "p=49,39 v=-45,95\n"
                                        + "p=33,42 v=-20,11\n"
                                        + "p=24,6 v=-1,69\n"
                                        + "p=2,90 v=29,-14\n"
                                        + "p=75,6 v=51,28\n"
                                        + "p=29,88 v=-92,58\n"
                                        + "p=72,24 v=44,88\n"
                                        + "p=78,63 v=-56,-45\n"
                                        + "p=84,35 v=95,-68\n"
                                        + "p=29,97 v=90,-94\n"
                                        + "p=97,1 v=28,-74\n"
                                        + "p=70,34 v=-31,71\n"
                                        + "p=79,52 v=43,84\n"
                                        + "p=67,93 v=-51,-75\n"
                                        + "p=55,84 v=11,-29\n"
                                        + "p=85,48 v=-66,-35\n"
                                        + "p=34,82 v=-87,-64\n"
                                        + "p=21,89 v=-29,-41\n"
                                        + "p=81,89 v=25,-55\n"
                                        + "p=87,22 v=-12,25\n"
                                        + "p=75,41 v=-60,-39\n"
                                        + "p=22,100 v=-32,-52\n"
                                        + "p=81,19 v=-14,-54\n"
                                        + "p=76,95 v=-11,-14\n"
                                        + "p=87,45 v=39,-88\n"
                                        + "p=95,77 v=-66,96\n"
                                        + "p=41,25 v=-40,2\n"
                                        + "p=37,99 v=63,-75\n"
                                        + "p=22,50 v=-68,8\n"
                                        + "p=57,39 v=32,-39\n"
                                        + "p=73,81 v=-17,43\n"
                                        + "p=50,58 v=-97,38\n"
                                        + "p=3,78 v=-17,-30\n"
                                        + "p=39,76 v=10,-91\n"
                                        + "p=95,67 v=79,-18\n"
                                        + "p=13,6 v=-76,40\n"
                                        + "p=93,21 v=-96,-2\n"
                                        + "p=67,72 v=-50,-53\n"
                                        + "p=88,64 v=-61,-84\n"
                                        + "p=30,20 v=40,-59\n"
                                        + "p=77,83 v=94,-7\n"
                                        + "p=41,71 v=10,58\n"
                                        + "p=88,12 v=43,74\n"
                                        + "p=49,88 v=-97,20\n"
                                        + "p=22,9 v=-26,1\n"
                                        + "p=37,86 v=-37,58\n"
                                        + "p=91,77 v=78,-14\n"
                                        + "p=44,79 v=7,16\n"
                                        + "p=25,26 v=68,37\n"
                                        + "p=52,41 v=7,-16\n"
                                        + "p=53,19 v=12,33\n"
                                        + "p=50,66 v=52,25\n"
                                        + "p=40,65 v=61,-69\n"
                                        + "p=8,13 v=26,2\n"
                                        + "p=9,79 v=77,-41\n"
                                        + "p=20,14 v=21,21\n"
                                        + "p=40,97 v=12,-44\n"
                                        + "p=8,43 v=-71,2\n"
                                        + "p=5,45 v=-80,49\n"
                                        + "p=90,81 v=69,-86\n"
                                        + "p=60,33 v=-56,-69\n"
                                        + "p=14,48 v=-75,-96\n"
                                        + "p=26,18 v=62,30\n"
                                        + "p=47,37 v=-78,59\n"
                                        + "p=56,4 v=-97,-51\n"
                                        + "p=67,8 v=47,9\n"
                                        + "p=74,47 v=-65,77\n"
                                        + "p=12,47 v=75,-77\n"
                                        + "p=17,66 v=72,12\n"
                                        + "p=77,34 v=29,11\n"
                                        + "p=77,90 v=91,-44\n"
                                        + "p=0,52 v=6,-68\n"
                                        + "p=25,97 v=73,5\n"
                                        + "p=61,30 v=19,-69\n"
                                        + "p=52,2 v=54,-29\n"
                                        + "p=40,12 v=-45,-75\n"
                                        + "p=65,96 v=92,5\n"
                                        + "p=73,39 v=-56,11\n"
                                        + "p=17,98 v=25,-75\n"
                                        + "p=15,16 v=-25,-28\n"
                                        + "p=5,97 v=-78,-59\n"
                                        + "p=54,100 v=-47,78\n"
                                        + "p=18,61 v=-79,-15\n"
                                        + "p=51,37 v=50,-4\n"
                                        + "p=92,16 v=78,99\n"
                                        + "p=70,74 v=-55,-99\n"
                                        + "p=82,44 v=48,-19\n"
                                        + "p=20,95 v=-83,-10\n"
                                        + "p=52,59 v=-43,99\n"
                                        + "p=49,51 v=-78,8\n"
                                        + "p=31,24 v=49,61\n"
                                        + "p=9,73 v=46,4\n"
                                        + "p=64,52 v=50,-92\n"
                                        + "p=51,45 v=-49,-16\n"
                                        + "p=83,60 v=47,-68\n"
                                        + "p=8,44 v=68,85\n"
                                        + "p=81,98 v=45,5\n"
                                        + "p=51,77 v=59,-11\n"
                                        + "p=40,66 v=-71,-89\n"
                                        + "p=8,21 v=-16,18\n"
                                        + "p=9,51 v=33,64\n"
                                        + "p=59,32 v=99,37\n"
                                        + "p=46,83 v=-66,-56\n"
                                        + "p=53,36 v=5,83\n"
                                        + "p=48,84 v=-48,77\n"
                                        + "p=19,57 v=-81,-23\n"
                                        + "p=73,82 v=-26,7\n"
                                        + "p=21,7 v=-88,-28\n"
                                        + "p=49,92 v=-87,-26\n"
                                        + "p=5,59 v=-82,-18\n"
                                        + "p=84,6 v=-69,-13\n"
                                        + "p=34,90 v=-87,-44\n"
                                        + "p=6,79 v=-40,-97\n"
                                        + "p=42,69 v=-88,88\n"
                                        + "p=95,53 v=84,30\n"
                                        + "p=33,86 v=-18,-19\n"
                                        + "p=77,98 v=-10,70\n"
                                        + "p=87,98 v=96,82\n"
                                        + "p=16,13 v=73,-17\n"
                                        + "p=85,57 v=88,-27\n"
                                        + "p=24,99 v=-85,-14\n"
                                        + "p=98,36 v=-61,-1\n"
                                        + "p=60,58 v=3,42\n"
                                        + "p=5,56 v=-58,85\n"
                                        + "p=24,92 v=18,-94\n"
                                        + "p=82,87 v=-10,-87\n"
                                        + "p=69,32 v=51,95\n"
                                        + "p=63,51 v=-52,15\n"
                                        + "p=44,21 v=51,32\n"
                                        + "p=83,2 v=-21,-64\n"
                                        + "p=79,38 v=85,-43\n"
                                        + "p=53,44 v=-98,-31\n"
                                        + "p=41,42 v=-83,26\n"
                                        + "p=36,59 v=-38,-61\n"
                                        + "p=6,62 v=81,-23\n"
                                        + "p=57,68 v=40,-71\n"
                                        + "p=25,101 v=-1,-66\n"
                                        + "p=53,35 v=38,39\n"
                                        + "p=74,10 v=61,53\n"
                                        + "p=83,90 v=83,-29\n"
                                        + "p=81,13 v=-59,-74\n"
                                        + "p=25,0 v=26,5\n"
                                        + "p=19,42 v=99,-50\n"
                                        + "p=84,75 v=-58,8\n"
                                        + "p=99,83 v=85,-34\n"
                                        + "p=55,19 v=-26,-78\n"
                                        + "p=84,44 v=-11,72\n"
                                        + "p=82,60 v=-13,-60\n"
                                        + "p=30,23 v=67,26\n"
                                        + "p=52,57 v=-89,-83\n"
                                        + "p=88,35 v=84,-93\n"
                                        + "p=40,10 v=-45,-10\n"
                                        + "p=72,26 v=-51,44\n"
                                        + "p=76,19 v=-50,48\n"
                                        + "p=81,52 v=38,-8\n"
                                        + "p=26,35 v=-95,-68\n"
                                        + "p=52,63 v=-54,35\n"
                                        + "p=16,11 v=-77,6\n"
                                        + "p=99,69 v=70,-97\n"
                                        + "p=53,6 v=85,44\n"
                                        + "p=73,12 v=-99,-29\n"
                                        + "p=3,86 v=-93,-64\n"
                                        + "p=46,18 v=12,-86\n"
                                        + "p=74,0 v=-59,13\n"
                                        + "p=25,95 v=67,63\n"
                                        + "p=71,98 v=-12,-40\n"
                                        + "p=71,93 v=-12,-87\n"
                                        + "p=36,86 v=-40,-3\n"
                                        + "p=10,36 v=-32,-73\n"
                                        + "p=71,96 v=-34,-89\n"
                                        + "p=71,1 v=-4,-17\n"
                                        + "p=31,5 v=8,-78\n"
                                        + "p=100,2 v=31,13\n"
                                        + "p=98,44 v=6,-44\n"
                                        + "p=84,66 v=-13,23\n"
                                        + "p=32,30 v=20,-67\n"
                                        + "p=70,53 v=83,-99\n"
                                        + "p=15,93 v=25,1\n"
                                        + "p=2,71 v=-71,84\n"
                                        + "p=13,94 v=-25,47\n"
                                        + "p=73,34 v=-60,88\n"
                                        + "p=90,33 v=86,98\n"
                                        + "p=15,52 v=16,68\n"
                                        + "p=10,70 v=-87,-91\n"
                                        + "p=59,50 v=5,95\n"
                                        + "p=16,77 v=23,20\n"
                                        + "p=49,24 v=80,-2\n"
                                        + "p=29,97 v=-84,-64\n"
                                        + "p=20,48 v=-81,-65\n"
                                        + "p=78,39 v=50,-62\n"
                                        + "p=68,67 v=-5,-72\n"
                                        + "p=81,100 v=-58,-14\n"
                                        + "p=35,23 v=21,-43\n"
                                        + "p=85,91 v=92,-37\n"
                                        + "p=35,97 v=6,-29\n"
                                        + "p=80,51 v=-16,-98\n"
                                        + "p=70,76 v=71,44\n"
                                        + "p=17,53 v=-37,-12\n"
                                        + "p=8,1 v=-22,78\n"
                                        + "p=100,74 v=-79,-87\n"
                                        + "p=43,39 v=-90,-81\n"
                                        + "p=2,83 v=73,-68\n"
                                        + "p=96,14 v=84,-4\n"
                                        + "p=32,18 v=-47,-78\n"
                                        + "p=66,87 v=-3,-45\n"
                                        + "p=24,93 v=73,-26\n"
                                        + "p=83,63 v=87,23\n"
                                        + "p=44,93 v=-46,1\n"
                                        + "p=84,46 v=-63,15\n"
                                        + "p=81,77 v=85,-90\n"
                                        + "p=83,6 v=87,-6\n"
                                        + "p=47,44 v=7,-19\n"
                                        + "p=95,71 v=-9,20\n"
                                        + "p=91,77 v=-8,-75\n"
                                        + "p=64,89 v=-4,12\n"
                                        + "p=95,37 v=-14,-59\n"
                                        + "p=55,62 v=35,82\n"
                                        + "p=41,70 v=62,50\n"
                                        + "p=78,43 v=-8,-20\n"
                                        + "p=92,57 v=86,84\n"
                                        + "p=25,82 v=-30,16\n"
                                        + "p=11,7 v=-78,44\n"
                                        + "p=19,38 v=51,25\n"
                                        + "p=17,101 v=-28,-21\n"
                                        + "p=97,59 v=38,15\n"
                                        + "p=34,95 v=65,9\n"
                                        + "p=17,88 v=45,-95\n"
                                        + "p=80,27 v=-12,-93\n"
                                        + "p=66,72 v=-52,-91\n"
                                        + "p=21,64 v=24,-16\n"
                                        + "p=37,18 v=-86,48\n"
                                        + "p=53,15 v=-89,-13\n"
                                        + "p=0,45 v=28,-73\n"
                                        + "p=47,79 v=-99,99\n"
                                        + "p=70,95 v=-88,-10\n"
                                        + "p=72,50 v=-6,80\n"
                                        + "p=77,58 v=-83,91\n"
                                        + "p=15,29 v=-57,94\n"
                                        + "p=7,36 v=61,18\n"
                                        + "p=3,44 v=-19,-19\n"
                                        + "p=52,92 v=8,35\n"
                                        + "p=8,77 v=-22,12\n"
                                        + "p=88,33 v=36,83\n"
                                        + "p=32,45 v=-42,-85\n"
                                        + "p=52,98 v=-44,-10\n"
                                        + "p=1,51 v=-63,26\n"
                                        + "p=7,64 v=-61,79\n"
                                        + "p=35,11 v=66,-59\n"
                                        + "p=80,92 v=92,97\n"
                                        + "p=77,49 v=-53,-15\n"
                                        + "p=80,100 v=-10,78\n"
                                        + "p=47,33 v=58,43\n"
                                        + "p=37,91 v=-92,24\n"
                                        + "p=75,1 v=47,97\n"
                                        + "p=7,59 v=93,8\n"
                                        + "p=98,72 v=-67,50\n"
                                        + "p=76,1 v=-59,63\n"
                                        + "p=94,88 v=37,-79\n"
                                        + "p=54,82 v=-54,-87\n"
                                        + "p=43,1 v=-92,-86\n"
                                        + "p=54,63 v=-54,69\n"
                                        + "p=13,80 v=-76,-60\n"
                                        + "p=56,42 v=-46,3\n"
                                        + "p=48,52 v=-95,-95\n"
                                        + "p=93,67 v=-68,69\n"
                                        + "p=37,65 v=64,-61\n"
                                        + "p=95,79 v=32,-91\n"
                                        + "p=96,95 v=-68,-14\n"
                                        + "p=23,89 v=-79,-79\n"
                                        + "p=43,63 v=-88,-19\n"
                                        + "p=40,62 v=56,98\n"
                                        + "p=52,26 v=6,-70\n"
                                        + "p=26,12 v=-63,2\n"
                                        + "p=89,61 v=88,42\n"
                                        + "p=81,55 v=91,38\n"
                                        + "p=72,92 v=-63,-2\n"
                                        + "p=64,77 v=-51,-45\n"
                                        + "p=38,68 v=-71,25\n"
                                        + "p=58,32 v=-33,-53\n"
                                        + "p=55,60 v=97,-80\n"
                                        + "p=49,102 v=-11,-26\n"
                                        + "p=95,4 v=36,82\n"
                                        + "p=83,33 v=67,-70\n"
                                        + "p=90,59 v=-57,-80\n"
                                        + "p=12,16 v=-28,36\n"
                                        + "p=50,24 v=-92,-28\n"
                                        + "p=13,72 v=76,-84\n"
                                        + "p=91,98 v=-80,-69\n"
                                        + "p=88,10 v=55,-24\n"
                                        + "p=97,67 v=-19,92\n"
                                        + "p=72,93 v=-7,5\n"
                                        + "p=85,41 v=-64,83\n"
                                        + "p=5,5 v=-42,87\n"
                                        + "p=3,46 v=-22,72\n"
                                        + "p=33,37 v=-30,77\n"
                                        + "p=83,27 v=8,46\n"
                                        + "p=11,2 v=-75,-82\n"
                                        + "p=41,7 v=-87,78\n"
                                        + "p=51,30 v=58,-47\n"
                                        + "p=24,15 v=-33,-97\n"
                                        + "p=92,50 v=-69,35\n"
                                        + "p=74,38 v=-64,-70\n"
                                        + "p=12,62 v=-76,-65\n"
                                        + "p=11,74 v=-80,5\n"
                                        + "p=96,65 v=-70,69\n"
                                        + "p=1,32 v=-71,-77\n"
                                        + "p=82,39 v=33,26\n"
                                        + "p=28,46 v=80,-98\n"
                                        + "p=51,85 v=-2,5\n"
                                        + "p=69,52 v=-52,76\n"
                                        + "p=22,68 v=-23,-22\n"
                                        + "p=56,13 v=61,71\n"
                                        + "p=79,78 v=-95,-50\n"
                                        + "p=4,90 v=-71,-14\n"
                                        + "p=76,31 v=-50,94\n"
                                        + "p=69,72 v=95,78\n"
                                        + "p=5,96 v=-70,-85\n"
                                        + "p=17,87 v=28,-67\n"
                                        + "p=10,3 v=95,-27\n"
                                        + "p=44,94 v=-40,-29\n"
                                        + "p=29,49 v=18,53\n"
                                        + "p=4,97 v=-19,-29\n"
                                        + "p=1,98 v=32,47\n"
                                        + "p=25,34 v=11,72\n"
                                        + "p=39,81 v=-89,1\n"
                                        + "p=37,42 v=-39,45\n"
                                        + "p=64,4 v=-8,52\n"
                                        + "p=7,8 v=-37,-37\n"
                                        + "p=58,94 v=54,-82\n"
                                        + "p=63,27 v=39,74\n"
                                        + "p=8,30 v=25,75\n"
                                        + "p=60,90 v=-51,70\n"
                                        + "p=94,90 v=-23,-9\n"
                                        + "p=91,45 v=-14,38\n"
                                        + "p=63,42 v=-51,30\n"
                                        + "p=66,83 v=28,4\n"
                                        + "p=58,79 v=5,-72\n"
                                        + "p=34,99 v=-42,10\n"
                                        + "p=51,69 v=-47,69\n"
                                        + "p=38,77 v=64,-45\n"
                                        + "p=18,59 v=20,-38\n"
                                        + "p=79,31 v=43,-62\n"
                                        + "p=29,0 v=-84,63\n"
                                        + "p=72,12 v=38,71\n"
                                        + "p=61,56 v=-50,88\n"
                                        + "p=32,37 v=-51,-23\n"
                                        + "p=42,43 v=-24,61\n"
                                        + "p=51,41 v=-95,92\n"
                                        + "p=39,12 v=12,73\n"
                                        + "p=83,21 v=-72,1\n"
                                        + "p=12,79 v=-77,12\n"
                                        + "p=15,60 v=67,30\n"
                                        + "p=62,92 v=-47,62\n"
                                        + "p=42,95 v=53,-64\n"
                                        + "p=100,45 v=-68,-92\n"
                                        + "p=50,73 v=-95,-95\n"
                                        + "p=30,88 v=16,-79\n"
                                        + "p=64,51 v=45,64\n"
                                        + "p=98,72 v=-64,46\n"
                                        + "p=65,34 v=-3,76\n"
                                        + "p=26,88 v=65,78\n"
                                        + "p=5,60 v=36,42\n"
                                        + "p=3,33 v=-39,84\n"
                                        + "p=41,90 v=-64,2\n"
                                        + "p=90,52 v=-65,-65\n"
                                        + "p=39,63 v=16,-45\n"
                                        + "p=85,19 v=37,98\n"
                                        + "p=64,29 v=-5,-31\n"
                                        + "p=82,9 v=-21,2\n"
                                        + "p=34,93 v=-39,-60\n"
                                        + "p=86,1 v=91,40\n"
                                        + "p=49,57 v=-50,25\n"
                                        + "p=26,82 v=67,-25\n"
                                        + "p=10,23 v=-81,21\n"
                                        + "p=7,7 v=25,82\n"
                                        + "p=87,24 v=-83,92";

    public static void main(String[] args) {
        Util.time(() -> a(Input.parse(TEST_INPUT)));
        Util.time(() -> a(Input.parse(INPUT)));
        Util.time(() -> b(Input.parse(TEST_INPUT)));
        Util.time(() -> b(Input.parse(INPUT)));
    }
}
