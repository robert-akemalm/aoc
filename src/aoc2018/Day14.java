package aoc2018;

public class Day14 {
    public static void main(String[] args) {
//        int input = 2018;
        int input = 920831;
        a(input);
        b(input);
    }

    private static void b(int input) {
        int[] recepies = new int[100_000_000];
        recepies[0] = 3;
        recepies[1] = 7;
        int aIx = 0;
        int bIx = 1;
        int size = 2;
        while (true) {
            int a = recepies[aIx];
            int b = recepies[bIx];
            int[] newRecepies = newRecepies(a, b);
            for (int i = 0; i < newRecepies.length; i++) {
                int newRecepy = newRecepies[i];
                if (newRecepy == 1 && size > 6) {
                    if (recepies[size - 1] == 3 &&
                        recepies[size - 2] == 8 &&
                        recepies[size - 3] == 0 &&
                        recepies[size - 4] == 2 &&
                        recepies[size - 5] == 9) {
                        System.out.println(size - 5);
                        break;
                    }
                }
                recepies[size++] = newRecepy;
            }
            aIx = (aIx + a + 1) % size;
            bIx = (bIx + b + 1) % size;
        }
    }

    private static void a(int input) {
        int[] recepies = new int[input + 12];
        recepies[0] = 3;
        recepies[1] = 7;
        int aIx = 0;
        int bIx = 1;
        int size = 2;
        while (size < recepies.length - 1) {
            int a = recepies[aIx];
            int b = recepies[bIx];
            int[] newRecepies = newRecepies(a, b);
            for (int i = 0; i < newRecepies.length; i++) {
                recepies[size++] = newRecepies[i];
            }
            aIx = (aIx + a + 1) % size;
            bIx = (bIx + b + 1) % size;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = input; i < input + 10 ; i++) {
            sb.append(recepies[i]);
        }
        System.out.println(sb.toString());
    }

    private static int[] newRecepies(int a, int b) {
        return RECEPIES[a + b];
    }

    private static final int[][] RECEPIES = new int[20][];
    static {
        RECEPIES[0] = new int[]{0};
        RECEPIES[1] = new int[]{1};
        RECEPIES[2] = new int[]{2};
        RECEPIES[3] = new int[]{3};
        RECEPIES[4] = new int[]{4};
        RECEPIES[5] = new int[]{5};
        RECEPIES[6] = new int[]{6};
        RECEPIES[7] = new int[]{7};
        RECEPIES[8] = new int[]{8};
        RECEPIES[9] = new int[]{9};
        RECEPIES[10] = new int[]{1, 0};
        RECEPIES[11] = new int[]{1, 1};
        RECEPIES[12] = new int[]{1, 2};
        RECEPIES[13] = new int[]{1, 3};
        RECEPIES[14] = new int[]{1, 4};
        RECEPIES[15] = new int[]{1, 5};
        RECEPIES[16] = new int[]{1, 6};
        RECEPIES[17] = new int[]{1, 7};
        RECEPIES[18] = new int[]{1, 8};
    }

}
