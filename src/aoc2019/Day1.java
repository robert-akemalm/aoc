package aoc2019;

public class Day1 {
    public static void main(String[] args) {
        String input = INPUT;
        a(input);
        b(input);
    }

    private static void b(String input) {
        int[] fuelPerModule = input.lines().mapToInt(Integer::parseInt).map(i -> i / 3).map(i -> i - 2).toArray();
        int sum = 0;
        for (int fuelCost : fuelPerModule) {
            sum += fuelCost;
            while (fuelCost > 0) {
                int part = fuelCost / 3;
                part -= 2;
                if (part > 0) {
                    sum += part;
                }
                fuelCost = part;
            }
        }
        System.out.println(sum);

    }

    private static void a(String input) {
        int sum = input.lines().mapToInt(Integer::parseInt).map(i -> i / 3).map(i -> i - 2).sum();
        System.out.println(sum);
    }

    private static final String TEST_INPUT = "";

    private static final String INPUT = "104451\n"
                                        + "112406\n"
                                        + "109733\n"
                                        + "86460\n"
                                        + "53795\n"
                                        + "116181\n"
                                        + "124973\n"
                                        + "86893\n"
                                        + "142967\n"
                                        + "77371\n"
                                        + "81449\n"
                                        + "61038\n"
                                        + "67074\n"
                                        + "138470\n"
                                        + "80850\n"
                                        + "106182\n"
                                        + "104458\n"
                                        + "139358\n"
                                        + "137806\n"
                                        + "60516\n"
                                        + "72879\n"
                                        + "92775\n"
                                        + "68968\n"
                                        + "51371\n"
                                        + "50001\n"
                                        + "113500\n"
                                        + "61705\n"
                                        + "127042\n"
                                        + "52989\n"
                                        + "142698\n"
                                        + "116254\n"
                                        + "128519\n"
                                        + "85282\n"
                                        + "88955\n"
                                        + "105966\n"
                                        + "85309\n"
                                        + "85182\n"
                                        + "135414\n"
                                        + "126973\n"
                                        + "88140\n"
                                        + "105968\n"
                                        + "102361\n"
                                        + "54599\n"
                                        + "87378\n"
                                        + "133774\n"
                                        + "72266\n"
                                        + "102915\n"
                                        + "140436\n"
                                        + "103312\n"
                                        + "71966\n"
                                        + "105082\n"
                                        + "124225\n"
                                        + "106179\n"
                                        + "108271\n"
                                        + "124969\n"
                                        + "93752\n"
                                        + "138578\n"
                                        + "89071\n"
                                        + "149579\n"
                                        + "98460\n"
                                        + "98780\n"
                                        + "54179\n"
                                        + "142225\n"
                                        + "120878\n"
                                        + "96915\n"
                                        + "136992\n"
                                        + "98383\n"
                                        + "123828\n"
                                        + "65254\n"
                                        + "79860\n"
                                        + "100411\n"
                                        + "143105\n"
                                        + "73999\n"
                                        + "109390\n"
                                        + "119817\n"
                                        + "141457\n"
                                        + "140983\n"
                                        + "120983\n"
                                        + "142747\n"
                                        + "110296\n"
                                        + "132048\n"
                                        + "129606\n"
                                        + "67404\n"
                                        + "120221\n"
                                        + "148298\n"
                                        + "72329\n"
                                        + "133164\n"
                                        + "146765\n"
                                        + "85752\n"
                                        + "130554\n"
                                        + "127331\n"
                                        + "139180\n"
                                        + "89050\n"
                                        + "110535\n"
                                        + "84393\n"
                                        + "127362\n"
                                        + "143205\n"
                                        + "140756\n"
                                        + "147071\n"
                                        + "133740";
}
