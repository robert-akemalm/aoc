package aoc2019;

import aoc2019.Day5.IO;
import aoc2019.Day5.IntComputer;
import aoc2019.Util.Pos;

public class Day19 {
    public static void main(String[] args) {
        a();
        b();
    }

    private static void b() {
        Pos first = null;
        for (int xStart = 990; xStart > 900 ; xStart--) {
            System.out.println(xStart);
            for (int yStart = 1200; yStart <1443 ; yStart++) {
                Pos upperLeft = new Pos(xStart, yStart);
                Pos upperRight = new Pos(xStart + 99, yStart);
                Pos lowerLeft = new Pos(xStart, yStart + 99);
                Pos lowerRight = new Pos(xStart + 99, yStart + 99);

                if (test(upperLeft)) {
                    if (test(lowerRight)) {
                        if (test(upperRight)) {
                            if (test(lowerLeft)) {
                                first = new Pos(xStart, yStart);
                                System.out.println(first);
                                break;
                            }
                        }
                    }
                }
            }
        }
        System.out.println(first);
        // 1080,1474 to high
    }

    private static boolean test(Pos pos) {
        IO read = new IO();
        IO write = new IO();
        IntComputer intComputer = new IntComputer(read, write, INPUT);
        read.write(pos.x);
        read.write(pos.y);
        intComputer.run();
        return write.read() == 1;
    }

    private static void a() {
        int count = 0;
        for (int x = 0; x < 50; x++) {
            for (int y = 0; y < 50; y++) {
                IO read = new IO();
                IO write = new IO();
                IntComputer intComputer = new IntComputer(read, write, INPUT);
                read.write(x);
                read.write(y);
                intComputer.run();
                if (write.read() == 1) {
                    count++;
                }
            }
        }
        System.out.println(count);
    }

    private static final String INPUT = "109,424,203,1,21102,1,11,0,1106,0,282,21101,0,18,0,1105,1,259,1201,1,0,221,203,1,21101,31,0,0,1105,1,282,21102,38,1,0,1105,1,259,21001,23,0,2,21201,1,0,3,21101,1,0,1,21102,57,1,0,1106,0,303,2102,1,1,222,21001,221,0,3,20102,1,221,2,21101,259,0,1,21102,80,1,0,1106,0,225,21101,0,167,2,21101,0,91,0,1105,1,303,2102,1,1,223,20102,1,222,4,21102,1,259,3,21102,1,225,2,21102,225,1,1,21102,1,118,0,1106,0,225,21001,222,0,3,21102,1,93,2,21101,0,133,0,1105,1,303,21202,1,-1,1,22001,223,1,1,21101,148,0,0,1105,1,259,2101,0,1,223,21001,221,0,4,20102,1,222,3,21102,21,1,2,1001,132,-2,224,1002,224,2,224,1001,224,3,224,1002,132,-1,132,1,224,132,224,21001,224,1,1,21102,1,195,0,106,0,108,20207,1,223,2,21001,23,0,1,21101,-1,0,3,21102,214,1,0,1106,0,303,22101,1,1,1,204,1,99,0,0,0,0,109,5,1202,-4,1,249,21202,-3,1,1,21202,-2,1,2,21201,-1,0,3,21101,0,250,0,1105,1,225,22101,0,1,-4,109,-5,2106,0,0,109,3,22107,0,-2,-1,21202,-1,2,-1,21201,-1,-1,-1,22202,-1,-2,-2,109,-3,2106,0,0,109,3,21207,-2,0,-1,1206,-1,294,104,0,99,22101,0,-2,-2,109,-3,2106,0,0,109,5,22207,-3,-4,-1,1206,-1,346,22201,-4,-3,-4,21202,-3,-1,-1,22201,-4,-1,2,21202,2,-1,-1,22201,-4,-1,1,22102,1,-2,3,21102,343,1,0,1105,1,303,1106,0,415,22207,-2,-3,-1,1206,-1,387,22201,-3,-2,-3,21202,-2,-1,-1,22201,-3,-1,3,21202,3,-1,-1,22201,-3,-1,2,21201,-4,0,1,21102,384,1,0,1106,0,303,1106,0,415,21202,-4,-1,-4,22201,-4,-3,-4,22202,-3,-2,-2,22202,-2,-4,-4,22202,-3,-2,-3,21202,-4,-1,-2,22201,-3,-2,1,22102,1,1,-4,109,-5,2105,1,0";
}
