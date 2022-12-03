package aoc2019;

import aoc2019.Day5.IO;
import aoc2019.Day5.IntComputer;

public class Day23 {
    public static void main(String[] args) {
        String input = INPUT;
        a(input);
        b(input);
    }

    private static void b(String input) {
        int numComputers = 50;
        IntComputer[] computers = new IntComputer[numComputers];
        IO[] read = new IO[numComputers];
        IO[] write = new IO[numComputers];
        for (int i = 0; i < numComputers; i++) {
            read[i] = new IO();
            write[i] = new IO();
            computers[i] = new IntComputer(read[i], write[i], INPUT);
            read[i].write(i);
        }

        long natX = -1;
        long natY = Long.MAX_VALUE;
        long lastWrittenY = Long.MIN_VALUE;
        boolean idle;
        while (true) {
            idle = true;
            for (int i = 0; i < numComputers; i++) {
                while (true) {
                    if (read[i].values.isEmpty()) {
                        read[i].write(-1);
                    }
                    int numWrite = write[i].values.size();
                    computers[i].run();
                    if (write[i].values.size() == numWrite) {
                        break;
                    }
                    idle = false;
                }
                while (!write[i].values.isEmpty()) {
                    int target = write[i].read().intValue();
                    long x = write[i].read();
                    long y = write[i].read();
                    if (target == 255) {
                        natX = x;
                        natY = y;
                    } else {
                        read[target].write(x);
                        read[target].write(y);
                    }
                }
            }
            if (idle) {
                if (natY != Long.MAX_VALUE) {
                    read[0].write(natX);
                    read[0].write(natY);
                    if (lastWrittenY == natY) {
                        System.out.println(natY);
                        return;
                    }
                    lastWrittenY = natY;
                }
            }
        }
    }

    private static void a(String input) {
        int numComputers = 50;
        IntComputer[] computers = new IntComputer[numComputers];
        IO[] read = new IO[numComputers];
        IO[] write = new IO[numComputers];
        for (int i = 0; i < numComputers; i++) {
            read[i] = new IO();
            write[i] = new IO();
            computers[i] = new IntComputer(read[i], write[i], INPUT);
            read[i].write(i);
        }

        while (true) {
            for (int i = 0; i < numComputers; i++) {
                while (true) {
                    if (read[i].values.isEmpty()) {
                        read[i].write(-1);
                    }
                    int numWrite = write[i].values.size();
                    computers[i].run();
                    if (write[i].values.size() == numWrite) {
                        break;
                    }
                }
                while (!write[i].values.isEmpty()) {
                    int target = write[i].read().intValue();
                    long x = write[i].read();
                    long y = write[i].read();
                    if (target == 255) {
                        System.out.println(x + ", " + y);
                        return;
                    }
                    read[target].write(x);
                    read[target].write(y);
                }
            }
        }
    }

    private static final String INPUT = "3,62,1001,62,11,10,109,2231,105,1,0,1614,1241,1082,2035,711,1020,2128,1750,1841,678,1719,1655,2198,1876,1119,2099,944,1280,771,837,1482,1515,1451,1581,2068,913,1779,608,643,1550,1909,1414,1183,983,1348,1315,866,740,1942,1810,2006,1212,1379,2157,1686,1977,571,804,1051,1148,0,0,0,0,0,0,0,0,0,0,0,0,3,64,1008,64,-1,62,1006,62,88,1006,61,170,1105,1,73,3,65,20101,0,64,1,20101,0,66,2,21102,105,1,0,1105,1,436,1201,1,-1,64,1007,64,0,62,1005,62,73,7,64,67,62,1006,62,73,1002,64,2,132,1,132,68,132,1001,0,0,62,1001,132,1,140,8,0,65,63,2,63,62,62,1005,62,73,1002,64,2,161,1,161,68,161,1101,0,1,0,1001,161,1,169,102,1,65,0,1101,1,0,61,1101,0,0,63,7,63,67,62,1006,62,203,1002,63,2,194,1,68,194,194,1006,0,73,1001,63,1,63,1106,0,178,21102,1,210,0,105,1,69,1202,1,1,70,1101,0,0,63,7,63,71,62,1006,62,250,1002,63,2,234,1,72,234,234,4,0,101,1,234,240,4,0,4,70,1001,63,1,63,1105,1,218,1105,1,73,109,4,21102,1,0,-3,21102,0,1,-2,20207,-2,67,-1,1206,-1,293,1202,-2,2,283,101,1,283,283,1,68,283,283,22001,0,-3,-3,21201,-2,1,-2,1105,1,263,22101,0,-3,-3,109,-4,2106,0,0,109,4,21101,1,0,-3,21102,1,0,-2,20207,-2,67,-1,1206,-1,342,1202,-2,2,332,101,1,332,332,1,68,332,332,22002,0,-3,-3,21201,-2,1,-2,1106,0,312,22102,1,-3,-3,109,-4,2106,0,0,109,1,101,1,68,358,21002,0,1,1,101,3,68,366,21001,0,0,2,21102,1,376,0,1106,0,436,21202,1,1,0,109,-1,2106,0,0,1,2,4,8,16,32,64,128,256,512,1024,2048,4096,8192,16384,32768,65536,131072,262144,524288,1048576,2097152,4194304,8388608,16777216,33554432,67108864,134217728,268435456,536870912,1073741824,2147483648,4294967296,8589934592,17179869184,34359738368,68719476736,137438953472,274877906944,549755813888,1099511627776,2199023255552,4398046511104,8796093022208,17592186044416,35184372088832,70368744177664,140737488355328,281474976710656,562949953421312,1125899906842624,109,8,21202,-6,10,-5,22207,-7,-5,-5,1205,-5,521,21101,0,0,-4,21102,1,0,-3,21101,0,51,-2,21201,-2,-1,-2,1201,-2,385,470,21001,0,0,-1,21202,-3,2,-3,22207,-7,-1,-5,1205,-5,496,21201,-3,1,-3,22102,-1,-1,-5,22201,-7,-5,-7,22207,-3,-6,-5,1205,-5,515,22102,-1,-6,-5,22201,-3,-5,-3,22201,-1,-4,-4,1205,-2,461,1105,1,547,21102,1,-1,-4,21202,-6,-1,-6,21207,-7,0,-5,1205,-5,547,22201,-7,-6,-7,21201,-4,1,-4,1105,1,529,21201,-4,0,-7,109,-8,2106,0,0,109,1,101,1,68,564,20102,1,0,0,109,-1,2105,1,0,1102,37061,1,66,1102,4,1,67,1101,0,598,68,1101,0,302,69,1102,1,1,71,1102,606,1,72,1106,0,73,0,0,0,0,0,0,0,0,42,79162,1102,10177,1,66,1102,3,1,67,1102,635,1,68,1101,302,0,69,1102,1,1,71,1102,1,641,72,1105,1,73,0,0,0,0,0,0,42,118743,1102,86291,1,66,1101,1,0,67,1101,0,670,68,1101,0,556,69,1101,0,3,71,1102,1,672,72,1106,0,73,1,5,31,189578,31,284367,43,3574,1101,2999,0,66,1101,1,0,67,1102,1,705,68,1101,0,556,69,1101,2,0,71,1101,707,0,72,1105,1,73,1,4021,38,305871,21,5059,1101,0,28661,66,1102,1,1,67,1101,0,738,68,1102,1,556,69,1101,0,0,71,1102,740,1,72,1105,1,73,1,1004,1101,0,3769,66,1101,0,1,67,1101,767,0,68,1102,556,1,69,1102,1,1,71,1101,769,0,72,1105,1,73,1,18,46,74122,1101,0,9857,66,1102,1,1,67,1101,798,0,68,1102,556,1,69,1101,0,2,71,1101,800,0,72,1106,0,73,1,10,31,94789,43,1787,1101,0,32969,66,1102,1,1,67,1102,1,831,68,1102,556,1,69,1102,2,1,71,1101,0,833,72,1105,1,73,1,3,27,10177,27,20354,1102,1,88799,66,1102,1,1,67,1102,1,864,68,1102,556,1,69,1101,0,0,71,1101,0,866,72,1106,0,73,1,1699,1101,0,9293,66,1101,1,0,67,1102,1,893,68,1102,556,1,69,1101,9,0,71,1102,895,1,72,1105,1,73,1,1,46,37061,30,34457,16,17734,12,91502,20,68777,13,170162,8,269013,33,276604,21,10118,1101,0,14657,66,1101,1,0,67,1102,1,940,68,1101,0,556,69,1102,1,1,71,1102,942,1,72,1106,0,73,1,33521,30,68914,1101,0,8867,66,1102,5,1,67,1101,971,0,68,1101,302,0,69,1102,1,1,71,1101,0,981,72,1106,0,73,0,0,0,0,0,0,0,0,0,0,42,39581,1102,1,69151,66,1102,4,1,67,1101,0,1010,68,1101,302,0,69,1102,1,1,71,1101,1018,0,72,1106,0,73,0,0,0,0,0,0,0,0,3,92362,1101,0,33493,66,1102,1,1,67,1101,1047,0,68,1102,1,556,69,1101,1,0,71,1101,1049,0,72,1106,0,73,1,997,16,8867,1101,0,69959,66,1102,1,1,67,1101,1078,0,68,1101,0,556,69,1102,1,1,71,1101,1080,0,72,1106,0,73,1,223,33,138302,1102,21893,1,66,1101,4,0,67,1101,1109,0,68,1101,253,0,69,1102,1,1,71,1102,1117,1,72,1106,0,73,0,0,0,0,0,0,0,0,44,66089,1102,27751,1,66,1102,1,1,67,1101,1146,0,68,1101,556,0,69,1102,0,1,71,1102,1148,1,72,1105,1,73,1,1295,1102,1,53381,66,1101,0,3,67,1101,1175,0,68,1101,0,302,69,1101,0,1,71,1101,0,1181,72,1106,0,73,0,0,0,0,0,0,2,65679,1102,23677,1,66,1101,1,0,67,1101,1210,0,68,1101,556,0,69,1102,1,0,71,1101,0,1212,72,1105,1,73,1,1080,1101,0,3257,66,1101,1,0,67,1102,1239,1,68,1101,556,0,69,1101,0,0,71,1101,0,1241,72,1106,0,73,1,1993,1102,1,99961,66,1102,1,1,67,1101,0,1268,68,1102,1,556,69,1102,1,5,71,1101,1270,0,72,1106,0,73,1,2,16,44335,38,101957,8,179342,43,5361,43,7148,1101,10883,0,66,1101,0,3,67,1101,0,1307,68,1101,302,0,69,1101,0,1,71,1102,1313,1,72,1106,0,73,0,0,0,0,0,0,2,21893,1101,10859,0,66,1101,2,0,67,1101,0,1342,68,1101,0,302,69,1101,0,1,71,1102,1,1346,72,1106,0,73,0,0,0,0,38,203914,1101,0,90163,66,1102,1,1,67,1102,1375,1,68,1102,1,556,69,1101,0,1,71,1101,0,1377,72,1106,0,73,1,37,46,111183,1101,0,39581,66,1101,3,0,67,1101,1406,0,68,1101,253,0,69,1101,0,1,71,1101,0,1412,72,1106,0,73,0,0,0,0,0,0,35,21718,1101,0,94789,66,1101,0,4,67,1102,1,1441,68,1102,302,1,69,1101,1,0,71,1101,0,1449,72,1105,1,73,0,0,0,0,0,0,0,0,43,8935,1101,1571,0,66,1101,1,0,67,1102,1,1478,68,1102,1,556,69,1102,1,1,71,1102,1,1480,72,1105,1,73,1,-12,21,15177,1101,0,68777,66,1102,1,2,67,1101,1509,0,68,1102,1,302,69,1102,1,1,71,1102,1513,1,72,1105,1,73,0,0,0,0,13,85081,1102,1,5059,66,1102,1,3,67,1101,1542,0,68,1102,302,1,69,1101,0,1,71,1101,0,1548,72,1105,1,73,0,0,0,0,0,0,17,10883,1101,0,2099,66,1102,1,1,67,1101,1577,0,68,1102,1,556,69,1101,1,0,71,1102,1579,1,72,1105,1,73,1,546,46,148244,1102,1,1187,66,1101,1,0,67,1101,0,1608,68,1101,0,556,69,1102,1,2,71,1102,1,1610,72,1106,0,73,1,31,16,26601,16,35468,1102,1,30757,66,1101,1,0,67,1101,0,1641,68,1102,556,1,69,1101,0,6,71,1101,0,1643,72,1105,1,73,1,24015,3,46181,17,21766,17,32649,49,53381,49,106762,49,160143,1101,41759,0,66,1101,0,1,67,1102,1682,1,68,1101,556,0,69,1101,0,1,71,1102,1684,1,72,1105,1,73,1,160,43,10722,1101,0,66089,66,1101,2,0,67,1102,1713,1,68,1101,0,351,69,1102,1,1,71,1102,1717,1,72,1106,0,73,0,0,0,0,255,30757,1102,1093,1,66,1101,1,0,67,1101,1746,0,68,1101,0,556,69,1102,1,1,71,1101,0,1748,72,1105,1,73,1,32,35,10859,1101,85091,0,66,1102,1,1,67,1102,1777,1,68,1101,0,556,69,1102,0,1,71,1102,1,1779,72,1106,0,73,1,1773,1102,1,70177,66,1102,1,1,67,1102,1806,1,68,1101,556,0,69,1101,1,0,71,1101,0,1808,72,1106,0,73,1,125,31,379156,1102,1,9281,66,1101,0,1,67,1101,1837,0,68,1102,556,1,69,1102,1,1,71,1102,1,1839,72,1105,1,73,1,17987,12,45751,1102,89671,1,66,1101,0,3,67,1101,1868,0,68,1101,302,0,69,1101,1,0,71,1101,0,1874,72,1106,0,73,0,0,0,0,0,0,33,69151,1102,85081,1,66,1102,1,2,67,1102,1903,1,68,1102,302,1,69,1101,1,0,71,1101,1907,0,72,1106,0,73,0,0,0,0,8,89671,1102,1,34457,66,1102,1,2,67,1101,0,1936,68,1102,302,1,69,1102,1,1,71,1101,0,1940,72,1106,0,73,0,0,0,0,27,30531,1102,1,101957,66,1101,3,0,67,1102,1,1969,68,1101,0,302,69,1102,1,1,71,1102,1975,1,72,1105,1,73,0,0,0,0,0,0,2,43786,1102,1,39191,66,1102,1,1,67,1102,1,2004,68,1101,556,0,69,1101,0,0,71,1102,1,2006,72,1106,0,73,1,1654,1101,48197,0,66,1101,1,0,67,1102,1,2033,68,1102,1,556,69,1102,1,0,71,1102,2035,1,72,1105,1,73,1,1410,1101,46181,0,66,1102,2,1,67,1102,2062,1,68,1102,1,302,69,1102,1,1,71,1101,0,2066,72,1105,1,73,0,0,0,0,2,87572,1102,1,36671,66,1102,1,1,67,1102,2095,1,68,1102,556,1,69,1102,1,1,71,1101,0,2097,72,1105,1,73,1,184,33,207453,1101,29863,0,66,1102,1,1,67,1102,1,2126,68,1101,556,0,69,1102,1,0,71,1102,1,2128,72,1105,1,73,1,1733,1102,30577,1,66,1101,1,0,67,1101,0,2155,68,1102,1,556,69,1101,0,0,71,1101,0,2157,72,1105,1,73,1,1195,1102,1,1787,66,1102,6,1,67,1101,2184,0,68,1102,1,302,69,1102,1,1,71,1102,2196,1,72,1106,0,73,0,0,0,0,0,0,0,0,0,0,0,0,44,132178,1101,45751,0,66,1101,0,2,67,1102,1,2225,68,1101,0,302,69,1101,0,1,71,1102,2229,1,72,1105,1,73,0,0,0,0,20,137554";
}
