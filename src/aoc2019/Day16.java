package aoc2019;

public class Day16 {
    public static void main(String[] args) {
        String input = INPUT;
        a(input);
        b(input);
    }

    private static void b(String input) {
        input = input.repeat(10_000);
        int offset = Integer.parseInt(input.substring(0, 7));

        int[] signal = new int[input.length() - offset + 1];
        for (int i = 0; i+offset < input.length(); i++) {
            signal[i] = input.charAt(offset + i) - '0';
        }
        signal[signal.length - 1] = 0;

        for (int i = 0; i < 100; i++) {
            int[] newSignal = new int[input.length() - offset];
            for (int j = newSignal.length - 2; j >= 0 ; j--) {
                newSignal[j] = Math.abs((newSignal[j+1] + signal[j])%10);
            }
            signal = newSignal;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(signal[i]);
        }
        System.out.println(sb);

    }

    private static void a(String input) {
        int[] signal = input.chars().map(c -> c - '0').toArray();
        int[][] patterns = new int[signal.length][];
        for (int i = 0; i < signal.length; i++) {
            patterns[i] = pattern(i, signal.length);
        }

        for (int phases = 0; phases < 100; phases++) {
            for (int i = 0; i < signal.length; i++) {
                long sum = 0;
                for (int k = i; k < signal.length; k++) {
                    sum += signal[k] * patterns[i][k];
                }
                signal[i] = Math.abs((int) (sum % 10));
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(signal[i]);
        }
        System.out.println(sb);
    }

    static final int[] basePattern = new int[] {0, 1, 0, -1};

    static int[] pattern(int elementIx, int length) {
        elementIx++;
        int repeats = elementIx;
        int[] pattern = new int[length];
        int ix = 0;
        while(ix < length) {
            for (int basePatternIx = 0; basePatternIx < basePattern.length; basePatternIx++) {
                for (int r = 0; r < repeats; r++) {
                    if (ix != 0 || r != 0 || basePatternIx != 0) {
                        pattern[ix++] = basePattern[basePatternIx];
                        if (ix == length) {
                            return pattern;
                        }
                    }
                }
            }
        }
        return pattern;
    }

    //24176176
    private static final String TEST_INPUT = "80871224585914546619083218645595";

    //73745418
    private static final String TEST_INPUT_2 = "19617804207202209144916044189917";

    //52432133
    private static final String TEST_INPUT_3 = "69317163492948606335995924319873";

    static final String INPUT =
            "59754835304279095723667830764559994207668723615273907123832849523285892960990393495763064170399328763959561728553125232713663009161639789035331160605704223863754174835946381029543455581717775283582638013183215312822018348826709095340993876483418084566769957325454646682224309983510781204738662326823284208246064957584474684120465225052336374823382738788573365821572559301715471129142028462682986045997614184200503304763967364026464055684787169501819241361777789595715281841253470186857857671012867285957360755646446993278909888646724963166642032217322712337954157163771552371824741783496515778370667935574438315692768492954716331430001072240959235708";
}
