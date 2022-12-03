package aoc2019;

import java.util.Arrays;

public class Day12 {
    public static void main(String[] args) {
        String input = INPUT;
        a(input);
        b(input);
    }

    static String state(Moon[] moons) {
        return Arrays.toString(moons);
    }

    private static void b(String input) {
        Moon[] moons = Arrays.stream(input.split("\n")).map(XYZ::parse).map(Moon::new).toArray(Moon[]::new);
        long[] stepsForRepeat = new long[3];
        int[] initialX = new int[moons.length * 2];
        int[] initialY = new int[moons.length * 2];
        int[] initialZ = new int[moons.length * 2];
        for (int i = 0; i < moons.length; i++) {
            initialX[i * 2] = moons[i].pos.x;
            initialX[i * 2 + 1] = moons[i].velocity.x;

            initialY[i * 2] = moons[i].pos.y;
            initialY[i * 2 + 1] = moons[i].velocity.y;

            initialZ[i * 2] = moons[i].pos.z;
            initialZ[i * 2 + 1] = moons[i].velocity.z;
        }
        for (long steps = 0; ; steps++) {
            Moon[] newState = new Moon[moons.length];
            for (int m = 0; m < moons.length; m++) {
                newState[m] = new Moon(moons[m].pos);
                newState[m].velocity = moons[m].velocity;
            }
            for (int i = 0; i < moons.length; i++) {
                Moon oldState = moons[i];
                for (Moon moon : moons) {
                    int xDiff = Integer.compare(moon.pos.x, oldState.pos.x) % 2;
                    int yDiff = Integer.compare(moon.pos.y, oldState.pos.y) % 2;
                    int zDiff = Integer.compare(moon.pos.z, oldState.pos.z) % 2;
                    XYZ v = newState[i].velocity;
                    newState[i].velocity = new XYZ(v.x + xDiff, v.y + yDiff, v.z + zDiff);
                }
            }
            for (int m = 0; m < moons.length; m++) {
                Moon newMoon = newState[m];
                moons[m].pos = new XYZ(
                        newMoon.pos.x + newMoon.velocity.x,
                        newMoon.pos.y + newMoon.velocity.y,
                        newMoon.pos.z + newMoon.velocity.z);
                moons[m].velocity = newMoon.velocity;
            }

            if (moons[0].pos.x == initialX[0] && moons[0].velocity.x == initialX[1] &&
                moons[1].pos.x == initialX[2] && moons[1].velocity.x == initialX[3] &&
                moons[2].pos.x == initialX[4] && moons[2].velocity.x == initialX[5] &&
                moons[3].pos.x == initialX[6] && moons[3].velocity.x == initialX[7]) {
                if (stepsForRepeat[0] == 0) {
                    stepsForRepeat[0] = steps;
                }
            }

            if (moons[0].pos.y == initialY[0] && moons[0].velocity.y == initialY[1] &&
                moons[1].pos.y == initialY[2] && moons[1].velocity.y == initialY[3] &&
                moons[2].pos.y == initialY[4] && moons[2].velocity.y == initialY[5] &&
                moons[3].pos.y == initialY[6] && moons[3].velocity.y == initialY[7]) {
                if (stepsForRepeat[1] == 0) {
                    stepsForRepeat[1] = steps;
                }
            }

            if (moons[0].pos.z == initialZ[0] && moons[0].velocity.z == initialZ[1] &&
                moons[1].pos.z == initialZ[2] && moons[1].velocity.z == initialZ[3] &&
                moons[2].pos.z == initialZ[4] && moons[2].velocity.z == initialZ[5] &&
                moons[3].pos.z == initialZ[6] && moons[3].velocity.z == initialZ[7]) {
                if (stepsForRepeat[2] == 0) {
                    stepsForRepeat[2] = steps;
                }
            }
            boolean done = true;
            for (long steps4repeat : stepsForRepeat) {
                if (steps4repeat == 0) {
                    done = false;
                    break;
                }
            }
            if (done) {
                break;
            }
        }
        long lcm = lcm(stepsForRepeat[0]+1, stepsForRepeat[1]+1);
        lcm = lcm(lcm, stepsForRepeat[2]+1);
        System.out.println(lcm);
    } // < 886600176130515
    //4686774924
    //56192082519

    public static long lcm(long a, long b) {
        long high = Math.max(Math.abs(a), Math.abs(b));
        long low = Math.min(Math.abs(a), Math.abs(b));
        long lcm = high;
        while (lcm % low != 0) {
            lcm += high;
        }
        return lcm;
    }

    private static void a(String input) {
        Moon[] moons = Arrays.stream(input.split("\n")).map(XYZ::parse).map(Moon::new).toArray(Moon[]::new);
        System.out.println("Steps 0");
        for (Moon moon : moons) {
            System.out.println(moon);
        }
        for (int state = 1; state <= 1000; state++) {
            Moon[] newState = new Moon[moons.length];
            for (int m = 0; m < moons.length; m++) {
                newState[m] = new Moon(moons[m].pos);
                newState[m].velocity = moons[m].velocity;
            }
            for (int i = 0; i < moons.length; i++) {
                Moon oldState = moons[i];
                for (Moon moon : moons) {
                    int xDiff = Integer.compare(moon.pos.x, oldState.pos.x) % 2;
                    int yDiff = Integer.compare(moon.pos.y, oldState.pos.y) % 2;
                    int zDiff = Integer.compare(moon.pos.z, oldState.pos.z) % 2;
                    XYZ v = newState[i].velocity;
                    newState[i].velocity = new XYZ(v.x + xDiff, v.y + yDiff, v.z + zDiff);
                }
            }
            System.out.println();
            System.out.println("Steps " + state);
            for (int m = 0; m < moons.length; m++) {
                Moon newMoon = newState[m];
                moons[m].pos = new XYZ(
                        newMoon.pos.x + newMoon.velocity.x,
                        newMoon.pos.y + newMoon.velocity.y,
                        newMoon.pos.z + newMoon.velocity.z);
                moons[m].velocity = newMoon.velocity;
//                System.out.println(moons[m]);
            }

            int sumEnergy = 0;
            for (Moon moon : moons) {
                int posEnergy = 0;
                posEnergy += Math.abs(moon.pos.x);
                posEnergy += Math.abs(moon.pos.y);
                posEnergy += Math.abs(moon.pos.z);

                int velEnergy = 0;
                velEnergy += Math.abs(moon.velocity.x);
                velEnergy += Math.abs(moon.velocity.y);
                velEnergy += Math.abs(moon.velocity.z);
                sumEnergy += posEnergy * velEnergy;
            }
            System.out.println(sumEnergy);
        }
    }

    private static class Moon {
        XYZ pos;
        XYZ velocity = new XYZ(0, 0, 0);

        public Moon(XYZ pos) {
            this.pos = pos;
        }

        @Override
        public String toString() {
            return "pos=" + pos + ", vel=" + velocity;
        }
    }

    private static class XYZ {
        final int x;
        final int y;
        final int z;

        private XYZ(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public String toString() {
            return "<x=" + x + ", y=" + y + ", z=" + z + ">";
        }

        public static XYZ parse(String s) {
            int x = Integer.parseInt(s.substring(s.indexOf("x=") + 2, s.indexOf(", y")));
            int y = Integer.parseInt(s.substring(s.indexOf("y=") + 2, s.indexOf(", z")));
            int z = Integer.parseInt(s.substring(s.indexOf("z=") + 2, s.indexOf(">")));
            return new XYZ(x, y, z);
        }
    }

    private static final String TEST_INPUT = "<x=-1, y=0, z=2>\n"
                                             + "<x=2, y=-10, z=-7>\n"
                                             + "<x=4, y=-8, z=8>\n"
                                             + "<x=3, y=5, z=-1>";

    private static final String TEST_INPUT_2 = "<x=-8, y=-10, z=0>\n"
                                               + "<x=5, y=5, z=10>\n"
                                               + "<x=2, y=-7, z=3>\n"
                                               + "<x=9, y=-8, z=-3>";

    private static final String INPUT = "<x=13, y=9, z=5>\n"
                                        + "<x=8, y=14, z=-2>\n"
                                        + "<x=-5, y=4, z=11>\n"
                                        + "<x=2, y=-6, z=1>";
}
