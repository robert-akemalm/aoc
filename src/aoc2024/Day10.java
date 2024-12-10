package aoc2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aoc2024.Util.Pos;

public class Day10 {
    private static void a(Input input) {
        Map<Pos, Set<Pos>> headToPeaks = new HashMap<>();
        for (int y = 0; y < input.map.length; y++) {
            for (int x = 0; x < input.map[y].length; x++) {
                if (input.map[y][x] == 9) {
                    Pos peak = new Pos(x, y);
                    Set<Pos> toExplore = Set.of(peak);
                    int nextNumber = 8;
                    while (!toExplore.isEmpty()) {
                        Set<Pos> nextToExplore = new HashSet<>();
                        for (Pos pos : toExplore) {
                            for (Pos neighbour : pos.neighbours()) {
                                if (0 <= neighbour.x() && 0 <= neighbour.y()
                                    && neighbour.y() < input.map.length
                                    && neighbour.x() < input.map[neighbour.y()].length){
                                    if (input.map[neighbour.y()][neighbour.x()] == nextNumber) {
                                        if (input.map[neighbour.y()][neighbour.x()] == 0) {
                                            headToPeaks.computeIfAbsent(neighbour, __ -> new HashSet<>()).add(peak);
                                        } else {
                                            nextToExplore.add(neighbour);
                                        }
                                    }
                                }
                            }
                        }
                        nextNumber--;
                        toExplore = nextToExplore;
                    }
                }
            }
        }
        System.out.println(headToPeaks.values().stream().mapToInt(Set::size).sum());
    }

    private static void b(Input input) {
        Map<Pos, Set<List<Pos>>> headToPeaks = new HashMap<>();
        for (int y = 0; y < input.map.length; y++) {
            for (int x = 0; x < input.map[y].length; x++) {
                if (input.map[y][x] == 9) {
                    Pos peak = new Pos(x, y);
                    Set<List<Pos>> toExplore = Set.of(List.of(peak));
                    int nextNumber = 8;
                    while (!toExplore.isEmpty()) {
                        Set<List<Pos>> nextToExplore = new HashSet<>();
                        for (List<Pos> path : toExplore) {
                            for (Pos neighbour : path.getLast().neighbours()) {
                                if (0 <= neighbour.x() && 0 <= neighbour.y()
                                    && neighbour.y() < input.map.length
                                    && neighbour.x() < input.map[neighbour.y()].length){
                                    if (input.map[neighbour.y()][neighbour.x()] == nextNumber) {
                                        List<Pos> nextPath = new ArrayList<>(path);
                                        nextPath.add(neighbour);
                                        if (input.map[neighbour.y()][neighbour.x()] == 0) {
                                            headToPeaks.computeIfAbsent(neighbour, __ -> new HashSet<>()).add(nextPath);
                                        } else {
                                            nextToExplore.add(nextPath);
                                        }
                                    }
                                }
                            }
                        }
                        nextNumber--;
                        toExplore = nextToExplore;
                    }
                }
            }
        }
        System.out.println(headToPeaks.values().stream().mapToInt(Set::size).sum());

    }

    record Input(int[][] map) {
        static Input parse(String input) {
            return new Input(input.lines().map(l -> l.chars().map(c -> c - '0').toArray()).toArray(int[][]::new));
        }
    }

    private static final String TEST_INPUT = "89010123\n"
                                             + "78121874\n"
                                             + "87430965\n"
                                             + "96549874\n"
                                             + "45678903\n"
                                             + "32019012\n"
                                             + "01329801\n"
                                             + "10456732";

    private static final String INPUT = "123454078104569871014321021321012349878967874\n"
                                        + "002965169245678562105489110456701256767456983\n"
                                        + "411873254324329453456976522343812323454305432\n"
                                        + "320984589010012306567887431098943210563216761\n"
                                        + "458976673407873210698790346787654329874109850\n"
                                        + "367887654356912348787891245497645698556780943\n"
                                        + "656792343105401059678712312338562107649891234\n"
                                        + "343001643234332769589600404329431236032321104\n"
                                        + "102198756121049878465521565010120345121430213\n"
                                        + "234567987012156912374437674320345034560345412\n"
                                        + "126898776103457809983398983011276127672126703\n"
                                        + "045678945434967618812287034567689018984035894\n"
                                        + "034989876325898507600176127698548109123445665\n"
                                        + "121070165016785216010165018345630678006510778\n"
                                        + "015676234245234345323234509216721565217329889\n"
                                        + "104389892104101059654965432101892674398456788\n"
                                        + "010210743233298128760870123438984583212105998\n"
                                        + "327805654654567637201256754347673294303614854\n"
                                        + "476912348723498542112349861243100185454783763\n"
                                        + "585210239010101443009659870652231256765692152\n"
                                        + "694332108212012356788778778701645899801087001\n"
                                        + "783445098302345653299894389614556732112396503\n"
                                        + "612956789401298764108763210543678945013765412\n"
                                        + "107810987567889655895654101012109876894894321\n"
                                        + "016721076101910346781565092325012345765765010\n"
                                        + "145432345234581232690478783234540332345654321\n"
                                        + "298703876501698701541089654129651231056310145\n"
                                        + "387212965012787650132108543098774340987234236\n"
                                        + "456903454323456043236719612012589656891105687\n"
                                        + "125876561210798101045898703343478797650112793\n"
                                        + "034329870325887232784387643296556788943289832\n"
                                        + "105610711456976545693211234187645019850176541\n"
                                        + "612789804567803456789800105098732134567654320\n"
                                        + "523898714326512101234764306789678325478956010\n"
                                        + "630145625613456780345605219812569210329547854\n"
                                        + "549234034702102395653216984503431678017632903\n"
                                        + "678432129899801234764567893689120589098701012\n"
                                        + "569562101234710109803891012778021432176512343\n"
                                        + "450691032105676541012764569865434501089400154\n"
                                        + "541782145694589132123453678345123671078321965\n"
                                        + "432679238785410016787652101254010982361267873\n"
                                        + "121018019856321125696545610763010973450398654\n"
                                        + "038967025657689434545236789892129887650367743\n"
                                        + "127652134768976521430129865430034796341459812\n"
                                        + "234543089867987010321010178921015601232378103";

    public static void main(String[] args) {
        Util.time(() -> a(Input.parse(TEST_INPUT)));
        Util.time(() -> a(Input.parse(INPUT)));
        Util.time(() -> b(Input.parse(TEST_INPUT)));
        Util.time(() -> b(Input.parse(INPUT)));
    }
}
