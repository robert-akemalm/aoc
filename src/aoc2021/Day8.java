package aoc2021;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class Day8 {
    public static void main(String[] args) {
        a(TEST_INPUT_B);
        a(INPUT);
        b(TEST_INPUT_B);
        b(INPUT);
    }


    private enum Digit {
        ZERO('a', 'b', 'c', 'e', 'f', 'g'),
        ONE('c', 'f'),
        TWO('a', 'c', 'd', 'e', 'g'),
        THREE('a', 'c', 'd', 'f', 'g'),
        FOUR('b', 'c', 'd', 'f'),
        FIVE('a', 'b', 'd', 'f', 'g'),
        SIX('a', 'b', 'd', 'e', 'f', 'g'),
        SEVEN('a', 'c', 'f'),
        EIGHT('a', 'b', 'c', 'd', 'e', 'f', 'g'),
        NINE('a', 'b', 'c', 'd', 'e', 'g');

        private final Set<Character> chars;
        Digit(Character... chars) {
            this.chars = Set.of(chars);
        }
    }

    private static void a(String input) {
        String[] lines = input.split("\n");
        int cnt = 0;
        for (String line : lines) {
            String[] parts = line.split(" \\| ");
            String[] signal = parts[0].split(" ");
            String[] output = parts[1].split(" ");
            for (String s : output) {
                if(Set.of(2, 4, 3, 7).contains(s.length())) {
                    cnt++;
                }
            }
        }
        System.out.println(cnt);
    }

    private static void b(String input) {
        String[] lines = input.split("\n");
        long sum = 0;
        for (String line : lines) {
            String[] parts = line.split(" \\| ");
            String[] signal = parts[0].split(" ");

        Map<Character, Set<Character>> possibleRemapping = new HashMap<>();
        for (char c = 'a'; c <= 'g'; c++) {
            possibleRemapping.put(c, new HashSet<>(Digit.EIGHT.chars));
        }
        Map<Digit, Integer> unique = new HashMap<>();
        for (Digit digit : Digit.values()) {
            boolean uniq = true;
            for (Digit d : Digit.values()) {
                if (d != digit && d.chars.size() == digit.chars.size()) {
                    uniq = false;
                    break;
                }
            }
            if (uniq) {
                unique.put(digit, digit.chars.size());
            }
        }
        for (String s : signal) {
            if (unique.containsValue(s.length())) {
                Digit digit = unique.entrySet().stream().filter(e->e.getValue() == s.length()).findAny().map(Entry::getKey).get();
                for (char c : s.toCharArray()) {
                    possibleRemapping.get(c).retainAll(digit.chars);
                }
            }
        }

        Set<Character> cf = possibleRemapping.entrySet().stream()
                                              .filter(e->e.getValue().equals(Digit.ONE.chars))
                                              .map(Entry::getKey)
                                              .collect(Collectors.toSet());

        for (Set<Character> remapping : possibleRemapping.values()) {
            if (remapping.size() != 2) {
                remapping.removeAll(Digit.ONE.chars);
            }
        }

        Character a = possibleRemapping.entrySet().stream()
                                       .filter(e -> e.getValue().size() == 1)
                                       .map(Entry::getKey)
                                       .findFirst().get();
        possibleRemapping.put(a, Set.of('a'));

        Set<Character> bd = new HashSet<>();
        for (String s : signal) {
            if (s.length() == 4) {
                Set<Character> chars = new HashSet<>(s.chars().mapToObj(c-> (char) c).toList());
                chars.removeAll(cf);
                if (chars.size() == 2) {
                    bd = chars;
                    break;
                }
            }
        }

        Character g = null;
        for (String s : signal) {
            if (s.length() == 6) {
                Set<Character> chars = new HashSet<>(s.chars().mapToObj(c-> (char) c).toList());
                chars.remove(a);
                chars.removeAll(cf);
                chars.removeAll(bd);
                if (chars.size() == 1) {
                    g = chars.iterator().next();
                    break;
                }
            }
        }

        possibleRemapping.put(g, Set.of('g'));

        for (Set<Character> remapping : possibleRemapping.values()) {
            if (remapping.size() != 1) {
                remapping.remove('g');
            }
        }

        Character e = null;
        for (String s : signal) {
            if (s.length() == 7) {
                Set<Character> chars = new HashSet<>(s.chars().mapToObj(c-> (char) c).toList());
                chars.remove(a);
                chars.remove(g);
                chars.removeAll(cf);
                chars.removeAll(bd);
                if (chars.size() == 1) {
                    e = chars.iterator().next();
                    break;
                }
            }
        }

        possibleRemapping.put(e, Set.of('e'));

        for (Set<Character> remapping : possibleRemapping.values()) {
            if (remapping.size() != 1) {
                remapping.remove('e');
            }
        }

        Character b = null;
        for (String s : signal) {
            if (s.length() == 6) {
                Set<Character> chars = new HashSet<>(s.chars().mapToObj(c-> (char) c).toList());
                chars.remove(a);
                chars.remove(g);
                chars.remove(e);
                chars.removeAll(cf);
                if (chars.size() == 1) {
                    b = chars.iterator().next();
                    break;
                }
            }
        }

        possibleRemapping.put(b, Set.of('b'));

        for (Set<Character> remapping : possibleRemapping.values()) {
            if (remapping.size() != 1) {
                remapping.remove('b');
            }
        }

        Character f = null;
        for (String s : signal) {
            if (s.length() == 5) {
                Set<Character> chars = new HashSet<>(s.chars().mapToObj(c-> (char) c).toList());
                chars.remove(a);
                chars.remove(g);
                chars.removeAll(bd);
                if (chars.size() == 1) {
                    f = chars.iterator().next();
                    break;
                }
            }
        }

        possibleRemapping.put(f, Set.of('f'));

        for (Set<Character> remapping : possibleRemapping.values()) {
            if (remapping.size() != 1) {
                remapping.remove('f');
            }
        }

            Character finalF = f;
            Character finalB = b;
            Character c = cf.stream().filter(x-> x != finalF).findFirst().get();
            Character d = bd.stream().filter(x-> x != finalB).findFirst().get();
        Set<Character> o0 = Set.of(a, b, c, e, f, g);
        Set<Character> o1 = Set.of(c, f);
        Set<Character> o2 = Set.of(a, c, d, e, g);
        Set<Character> o3 = Set.of(a, c, d, f, g);
        Set<Character> o4 = Set.of(b, c, d, f);
        Set<Character> o5 = Set.of(a, b, d, f, g);
        Set<Character> o6 = Set.of(a, b, d, e, f, g);
        Set<Character> o7 = Set.of(a, c, f);
        Set<Character> o8 = Set.of(a, b, c, d, e, f, g);
        Set<Character> o9 = Set.of(a, b, c, d, f, g);

            String[] output = parts[1].split(" ");
            StringBuilder value = new StringBuilder();
            for (String s : output) {
                Set<Character> chars = new HashSet<>(s.chars().mapToObj(y-> (char) y).toList());
                if (chars.equals(o0)) {
                    value.append('0');
                } else if(chars.equals(o1)) {
                    value.append('1');
                } else if(chars.equals(o2)) {
                    value.append('2');
                } else if(chars.equals(o3)) {
                    value.append('3');
                } else if(chars.equals(o4)) {
                    value.append('4');
                } else if(chars.equals(o5)) {
                    value.append('5');
                } else if(chars.equals(o6)) {
                    value.append('6');
                } else if(chars.equals(o7)) {
                    value.append('7');
                } else if(chars.equals(o8)) {
                    value.append('8');
                } else if(chars.equals(o9)) {
                    value.append('9');
                }
            }
            int v = Integer.parseInt(value.toString());
            sum += v;
        }
        System.out.println(sum);

    }

    private static final String TEST_INPUT = "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf";


    private static final String TEST_INPUT_B = "be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe\n"
                                        + "edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc\n"
                                        + "fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg\n"
                                        + "fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb\n"
                                        + "aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea\n"
                                        + "fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb\n"
                                        + "dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe\n"
                                        + "bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef\n"
                                        + "egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb\n"
                                        + "gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce";

    private static final String INPUT = "badc bd dbeaf cfdbge dfb cfbdea efbag edcfgab dcafe degfca | eacfd acdfbe cbdegf fcbaedg\n"
                                        + "cd fdbac egcfab gbadcfe cfgdeb cbadfe deca cdf dfabg abefc | dcf cfbad gbafced fcd\n"
                                        + "cg agecfb cbg eabgfdc egdc fdgba bafecd cbdfe bfcdeg cfgdb | efdcb adcfeb fbdcg gbc\n"
                                        + "bfceg gfadb dbcfgea bgaef efad abe bcdgfa ea fbdgea agecbd | eadf ceadbg abfge fecdbga\n"
                                        + "gdcafe eacb adc gbfda afdceb edgbcf badfc ecgbafd ac fdbce | ebfcd cefdab bdfgeca egbdacf\n"
                                        + "aefbgd fbdc fbecg egdcb edcbag bf gcefa fbedcg bagfced fbg | afgce cfbd bdcf geafdbc\n"
                                        + "ebgacd edacg gcdeaf cabfg dbc bd cdbgaef ebad fecgbd acbgd | dgbac db caegbdf gcbfa\n"
                                        + "defbga gead ge efagcb gdafbc cegbdfa egf bfdce bgdef dgabf | gef eg bfcdag ge\n"
                                        + "bae adcgfbe cdfeag ab dcgeb bfdage agecb cagfe abcf bfcaeg | bae aeb agfceb afceg\n"
                                        + "ae adefcb gaceb gadceb edag efadcgb badgc cfgbe cadbfg cea | ceabgd fbeacdg aged aged\n"
                                        + "aegbdc dbegfac gcbaf df febd egdafc deabg gdf gdfeab adfbg | bcdegaf dfeb bcegad df\n"
                                        + "aegcdfb becdaf gdecf ag fag dcfgba badg fcbad cdgaf acefgb | abcdf cefdab ga ga\n"
                                        + "gbcfad cefadg cegad aefdb gedacb gcfe fc cfbgead cfade caf | degac egcf geacfbd afc\n"
                                        + "bcfad afcdbe fd ecfba cfd afde cfeabg gcbfed dacbg fdgabec | cafbged edfa fcd dabgc\n"
                                        + "afdc adbfe bgecfad ecagdb dbcafe dfe fd agbfe bdace gdbfec | fadbe efbdgc fadbe edcba\n"
                                        + "bdface geba ecbdag acegd afcdg ge eadbc bfecgda edg fgbdce | eg abge fadgceb abge\n"
                                        + "fagbde fcegbd gec efcdg fdgbe ebfcag gc adcfe cgdb cagbdfe | defcg cbgdfea gcbd ecfgd\n"
                                        + "acfbg debgf gfcdba edcbfa fbdag fgbeca cfbagde gadc da bda | dacg acbgf cedfgab caefdgb\n"
                                        + "ca dfegca edcbgf gfcde aedbg gfdeacb dcbfea facg dgcea ace | fedcg fdagec cea ac\n"
                                        + "acdf gcfde debcga ecbgaf cf ebgdf bagfdce cedag cef gedafc | dgcbea fc dcgeaf cdaeg\n"
                                        + "ebagf cabgfed ed cbgad afcbge afdbge edg eagbd cedgfb aefd | fcgdbe de cgbeaf ed\n"
                                        + "fbcdeag cafbd fgcbea fdgacb daecfb cg gca defga gbdc fdcga | abfcged agc acfgd cbfaeg\n"
                                        + "dacfe ed ebdcag gcadfe dfbcaeg dce dfeg fbcadg bfeca acdfg | fcbdga gcbade aegdcf fbcgda\n"
                                        + "cfdegb cbef fgdec gdfbca gdecbfa egfdca bc gbc cbegd aedgb | befc bc abegd ebcdfga\n"
                                        + "agcefdb fdbg bf bdafe gdebaf eafdg acdbe gcebaf bef edcfga | bf fdbg efdab gefda\n"
                                        + "gcb bdcgef dcbfg bg edbg facegb cbedf edgafbc dfgca edacbf | gb cdebfa deagfcb bg\n"
                                        + "eafbg gfad afegbd caefbg befdgc ebagd gd bdg cfbdage acdeb | gd geafb cabed dbg\n"
                                        + "bcedfg dc dgc aedfbcg acgbed bgfead gedbf bfdcg cedf facbg | gabfed cdebag dafegb cd\n"
                                        + "fa adbceg afecd cafdbge eacgfb daegcf aef bcdfe fdga daceg | acedf afe fae afdg\n"
                                        + "cdfaeg agcfb bcdgea dcafeb cef fabec ef dfgcbae defb eabcd | gdabce fdeb ebdf ecf\n"
                                        + "cdefg ceafg gebfd cdg dc efbcdg cdgbea cfedbga cbfd fdagbe | fbcd afdgbe fabedg dcfb\n"
                                        + "cdafgb egdb fecgabd cbfdg fcbedg feb acefg begcf be dacebf | be be ebfdca abgfcd\n"
                                        + "eafgcd facegb acb cdbfaeg fagec dgcabe baefd cgbf cb afecb | bcafeg fcgb gfdeac eagcf\n"
                                        + "ecgdfb deb edfgab eb faedcg agfed bfaed dbacf gbae efgdcba | fabde eb ecdgfb eabg\n"
                                        + "aefbg bdegfa gacdb gefc fbc fc bfdcae gefbac cbfag dbgefac | ecfg fc cfge efgcdab\n"
                                        + "gcfbed gcbae dcaef cbegad bdag bgeacf adefgcb deg adgec gd | agcbde bgad gdbfec gcfabe\n"
                                        + "dfacb egbcda dac agdcfb fecab adecfgb bgadf cd fgeabd cgfd | gabfdec acd dc dc\n"
                                        + "bfecg dcgeab af gadf aedbg caefdbg bgfade bafeg abf eabdcf | ebcfg gafd ebacfd fgbdace\n"
                                        + "ge fdeab gcebfda fadcg fgead abcdef eag ebdgfa acgbef dgeb | cdagf cfdgbae fdeab eag\n"
                                        + "dba dgfceb fbcegad bagde bcdeag bcedg bdafce agfbe ad cgad | bdcafe agdc ebdcg cdga\n"
                                        + "edfg fedac fbegcad acgfe fabcd ead bgefac de daecgf bcaged | bafcd ade bacefg fdbac\n"
                                        + "gdecbf edabc ef fcgead fcadg bdcfga fec ceadf dgaefbc agfe | faedgc ef gabfcd fe\n"
                                        + "deafg eb egbaf cadegf fcdeab degb bcgaf bfe bfcdega gefdba | abgfe eb gfdaeb efdcga\n"
                                        + "fabcd beadf efabgc fc cafdgb gcfd adfcgeb abgdce cfb dgcba | dcgf bdcgaef gdcf cfdg\n"
                                        + "ebac acedf ceadfb cfe dbcgaf ec efcgdab degaf abfcd edgbcf | ce edgaf dgbceaf fce\n"
                                        + "ecbag fg gacdfe edafgcb cgefa eacdfb deafc bdagcf gcf dgef | fged gedf cefdga cabge\n"
                                        + "dgebc bcg cg gdeabf cgfeba gdfc gefbdc adbec bdefg fgcdbae | begdf cfgd eadbc gbfde\n"
                                        + "aefdb ecgb dgaeb agdbce ega fgdacb bcadg cbdeafg ge edacfg | age fcbdaeg gea bcfegda\n"
                                        + "cdeabg fcgda bdagc acfb dfa af cdeabfg efdgab fbcgda decgf | bcagd cfba gedfc af\n"
                                        + "ceadb caebgfd bdfaeg cbgaf ge ega gadcbf gfce ebgcfa gebca | aecdb febgcad bgcaf fadcgbe\n"
                                        + "daegb gfdae ceagfd bgfade feagcb fbgd bg cbagdfe ebdac ebg | gefbacd dbgf geb adgbfe\n"
                                        + "ecadfg acdfb aefgc ecbfdag dce cfbega fcdae edga gbecdf ed | fcdba ced egfcba dega\n"
                                        + "defgbc cgdea efag fdbcgae acbdf gcdaeb fe dfcega efc defca | fage fec dcafeg ecagfdb\n"
                                        + "bg dbcafg gdafecb dgfcae defcg gebd cefgb bgfecd fbaec fbg | gbf dfgec bedg cfdgae\n"
                                        + "ebgda ebd adce cdgaeb edfcgb bcagd agdcebf ed agbef gcbfda | efagb gbefa dbcfgae eadc\n"
                                        + "ebcfdg dbe de cdabgf fgdab dgae aecbf abfgced edbaf eagdfb | bface cdafebg gadcefb fedab\n"
                                        + "agc dabcge agbcd cabe dbagf cfgeadb gecafd gbcdfe ac gbced | cbegd beac dgbce ac\n"
                                        + "fdagb bdeacf fga eagd gcfdb gecafb ga feabd abfgde dcbfega | bdafe abgcfe gbecfad abdgefc\n"
                                        + "fbecd dbaef bdgc debafgc daecgf ebfcgd ebc abegfc bc gfced | fadecbg cb cdgeaf bfade\n"
                                        + "fagbe aebfgcd gef bgadf cbagfd deafbg cfdeag ge begd beafc | bcfadeg edbgaf fcgaed ge\n"
                                        + "gabfc gabde fbdga bfegca agfbcd df cdfagbe dcfg caefbd dbf | decfagb bdf gfcd fd\n"
                                        + "bdafeg gf gbadf gefb cgefda daecbfg bacfed cdagb fdg fdaeb | daegcf eagdfb adgcb bagfed\n"
                                        + "dafegb efdac bda fgedbac egfcdb bgfa fbaed bgdef debagc ba | ba bgcead bad ab\n"
                                        + "cfged cbegdf cb ecgdb adefbcg fdcabg cdb cdagef efbc gedab | bc gdebcf gecdb bagdcf\n"
                                        + "fbegcd gcedba eba acegf dbfa efbgd ebfadg ab fgceadb bgfea | fgbae gbedcf degfb agdebc\n"
                                        + "ceadf fdacbe ef edgafb dfe bgaedc cadfg bcef cfebgad ecabd | cafde fabedc debac eacfd\n"
                                        + "cd dgafe cbgfae decfg cfebg cfd gdbc febgdc fbegacd fceadb | dc beagfc aebcfg gcfeabd\n"
                                        + "adcefbg caefgb bafd dface bdcfe cegbd fbe gdfcae dceabf fb | efb ebcgd gbdcaef fb\n"
                                        + "fbdg gebac degbfc gd dfeagbc decfb gcd dafgce faecbd bdgec | gfdcae gfdb dg afgdce\n"
                                        + "fb fgedca fbe gfcde gcbfde dbceaf bgeca eafbdcg dgfb fbegc | fbe cbdegf bfe gedfc\n"
                                        + "acfbe fcbaged bgdcf cabgfd ag cagd fcbga bcfegd gfa eadbfg | afgcb gbdecf efdbcg fag\n"
                                        + "fbegc dgca dfc fagbedc bfadcg dc ebdfca fadbg cgbfd gfdbae | dabefg egdbaf gaebdf dc\n"
                                        + "cdbgf bdfaeg ca fgcab dgacfbe feac facgbe ebgcad bca gfbea | dafbeg cab cdagbe debfga\n"
                                        + "gfbe ebcfag bcedaf be egfac fegdca gebca eab cbgad gfacdbe | dabcgef bcagfed gcfea bea\n"
                                        + "bdefacg fbgedc efdagb beg geabf eg dafbce dage gfabc eabdf | gead gcafb ecfdab egfbdac\n"
                                        + "gd gfecbd dacefb dgbf cdegf acfge agbfced becdf aedbgc gdc | gdcef cgbead gd gefacbd\n"
                                        + "cgbfea cafed dbgefac gacb bc afbge fdabge efbac bcfgde cbe | gbac fcdbge agcb aecgdfb\n"
                                        + "abgefc cfeba gcdba fgdcea gf fcbgaed eadfcb gbfe fga bacgf | efcba fabecd dgcbfae eagcfd\n"
                                        + "gcdbfe fdec gcfdb bdfeg de efgba cbgdafe afgcbd cgdbea ged | cedgbf fced agfcdeb cgfbed\n"
                                        + "dbacfge cafdb gbcfea aegc afbec ea bae bgcedf befgc edgafb | dfgeabc bfgcde faceb fbecag\n"
                                        + "bfcgad bfegd fg dcgeb egaf bfedcag efabdc dfegba gfb abefd | gf abdgcf dcbfag fgb\n"
                                        + "degab dgebac bfdeacg cadeb fbdeac bag debgf cgda abgecf ga | dceba fdbeg edbac dbacfge\n"
                                        + "abdef gcbead bgce cgdab deg dgcbfa aedbg fbacdge eadcfg ge | cgfdba gcafde agfcedb fdacbge\n"
                                        + "defabg cabdg gcfed cabe gbeacd fgadbc eb geb aedcfgb bdgce | eb dbegc dfecg dbecag\n"
                                        + "egdabcf edcgb bcedgf cgb bgfe fcgabd bg acgfed cbaed dfegc | cgb gbc egcfad degbc\n"
                                        + "fe agebdc gfbead fcdea abecgfd bafcde beacd cbef def cgfad | dabgfe fde adfcg bfgead\n"
                                        + "ba fcabed bad dgacf dfgecab dcgba gdecb bdgcfa abgf caegfd | gdecb dgcfba gcdafe afbedgc\n"
                                        + "bdgef cdbgfa acbed gedcfb efdgba cg abcgedf cgb cgdbe cfeg | edabc ebdca edbcg ecgf\n"
                                        + "edfba bcfgead cbged defbcg ag gfadce gcaebd edabg cabg dga | fedagc bcag bdegc gaebfdc\n"
                                        + "edab fed egcfb ecgbfad fegbd fagdcb degcaf afgebd gabdf ed | def gdefb fgedb edcagf\n"
                                        + "fc ebfgda ebcfd cabed gcadbf baecfdg cgfe fdc egdfb gdecbf | defbg aecbfdg cadbegf adgfbe\n"
                                        + "bgdfc fgc fdegb dgecfb fcabd egbafdc gc dgce gabefc gedbfa | fbdgce gc dbfegca cg\n"
                                        + "bgedc ged gdfcea dcefb cdabg bgfe defbgc bdecaf dcgbfae ge | badefc gbfe cbegd fdagbce\n"
                                        + "cdefga cgdafeb cbe becdg gdefc cfbg cfdbeg cb febdac edgba | edbgcf abecfd ebgfcd fgbc\n"
                                        + "de aegcf agedcb afegbdc edbf fbdga facgdb gdaef dbaegf dea | cdabgf ead afgbcd edbf\n"
                                        + "cfagde eagfbc cbegf cg fcaegdb ebgfd gcf dafebc efabc acbg | bacfe fgc gcf cg\n"
                                        + "gadfce fbcaeg bdag eba bedcgfa dcfbe ab ebdgca ceagd ecabd | eabdc badec cdaeg dagb\n"
                                        + "aedb ebcagf fdeac be bdgfc eafbdc abcfegd aefgcd feb ecdfb | fecbd adeb be bfe\n"
                                        + "dg fgdecb dfecb fdcgeba gedcb gceab fdge cgd cgdbfa baedcf | ecbgd cagfdeb dcbfeg gd\n"
                                        + "fgcead bfdcag gecdf dfeacb dce aged gcbfe dcfgeab ed fgcda | cfegd de gcafd gefdc\n"
                                        + "dbec eafbdg dafegc eac gbacf decagb bcgae bdgea gbcefad ce | becag edgab cbadgfe edbc\n"
                                        + "gefcab agbdc febd bcegd de ecbfg afgdce ebafgdc dfebgc egd | de gdecfba edbf cdegb\n"
                                        + "abdcefg fbdega dcfag bd fegba badgec fgadb edbf fgaceb bdg | acdfg fagbce adcfg abgfd\n"
                                        + "ag agdb bfeag fbgdace bcfeda dgafeb egafdc begfc dfeba eag | ecbfg agbd dabg bfeacd\n"
                                        + "dagc dae fdaec edcafg fbadeg cgefa abcgef ad dfecb fdgcbae | eda eagcbf agfec debfc\n"
                                        + "ead beafd dfagb faceb bfedca facegd aefcbg cbed ed gfcbaed | abgdf eda agbfce ead\n"
                                        + "afdgb eabcgf dfgbe fecdgba dagceb gfa cafgdb af cdfa abdcg | abdfg dceagb decfagb fagcedb\n"
                                        + "dbcfga cb acb gcbafde fcbg egbfda dceaf gbfda dcfba cdbgae | fcadgb cdefgba agbfd cgfb\n"
                                        + "gceb fcgba cefdba caebfdg egfadb eagfcb cb beafg acb gfacd | abcedf bca gfcda bcge\n"
                                        + "eabdc caebf ad dgabcef fcaedg cfegba deacbf dabf dca begdc | dbgce dbfa fgebadc acedb\n"
                                        + "fbgce ec gcea dafcgeb fgbed fec adgbcf abcfeg fcbga cfbade | cbdfag ebgfc fgcbe agbfdc\n"
                                        + "afcg afebcg becgf fgadeb bdgaefc fa afe acedb abecf gdfebc | fgecb eacdfgb ceabf cgfa\n"
                                        + "fb gadeb aegdfb abgfe fab dfgb adfecbg bcaegd ebdafc cafeg | becdfga bf fbadec bgfd\n"
                                        + "ab dgafe fdaeb adb agcedf ecbfd agfb caedgb bdfage egfabdc | fedbc dfecag afdcge becfd\n"
                                        + "bgedf dgecfb fae gbaec fa cedafg dcabfge gedfab gafbe fbda | fcgdea geadcfb efbdg fa\n"
                                        + "cgfaed fdebg fcgabd fcegb bd dfaeg gfbeda fgdceba edba dgb | db ebcgf fgcbe edgfa\n"
                                        + "fgc egcafb dcgb fagdb bedafgc cdbgaf fedac cg gacfd dfbage | febgad cgfaeb fadbg gc\n"
                                        + "fdaebcg deabgc bfdeg dfcaeg efcgd cdaf cfbeag fcg cf cgeda | bgefcad bgdfe dafc ebagfc\n"
                                        + "egcfad dbecf cbgf fbe aecbd bcfdge dfbgea aebgdcf ecfdg fb | cgfb gecfd gefacdb fbcg\n"
                                        + "cdb adcf gbcdae bdceaf dfaeb cdfeb febgc fgedba cd cgdafeb | eadfb fcgeb cbedga bdacge\n"
                                        + "gd dacg gbdafc gfbdc bdegaf fbgca cbgdefa cbafge edfcb dfg | gbcafd dgac dgcafb cbfgae\n"
                                        + "cbf dafeb fbceag bc egbdfc edbgafc fbced ecdfg cdgb gdafec | bcf dbfea ebagcf gadfcbe\n"
                                        + "dcebgf eagfc aebfcd edcbg baefgdc fd cdefg dgaebc fde bfgd | ebagfcd acgefdb fagec gbdaec\n"
                                        + "afgdc bagec bdfg fb ecgdbfa fab dfecga bcgadf dcafbe bgafc | afgced fb dcafg feabcd\n"
                                        + "gef febag ebagc egcfbd fg cafg abfde abcfge afcgebd ebcgda | egf gaebf dfeba gadbce\n"
                                        + "ag cdeafg gdbfce daceb acgf efgbda adg cegda gaedbfc efcgd | debac eadbc bgaecdf cgbaedf\n"
                                        + "acefdbg debgac ecdgf ead facebg eafgb ad fedbga bfda eafgd | adbf eagfd eabcfgd ecagfb\n"
                                        + "cd bafegc fgcaed fcaeg dgc fdcag agdbf fedc gbadce egdabcf | fedbcga bgafd fcde cfed\n"
                                        + "decg egf ecgbfa degfa edbagfc dgcabf cdfag eg fcaedg faebd | gcadef efadg egf eg\n"
                                        + "feacbg cgfadbe efb efbga dfagb eb aefgc bgec cgedaf dcebfa | eb be be fbe\n"
                                        + "begc eacfdb dfacg agcfe ce eagbf eac eafdgb aefbcg dfgaecb | ce ce ce gbce\n"
                                        + "dfeagb dgaeb dbagefc gbcfae degf decba cbgdfa bgfda eg gae | ge agfdcbe bdace fdeg\n"
                                        + "cgdab cegdbf cfbad bg aegb caedfg eacgbfd dgb dgaec gdbaec | gbea eacgd gb cgaebd\n"
                                        + "ag ecdgbaf edcba eagcd aeg gcfa dfcgae egcdf gfdeba fcgbde | fgeadb dacge fdagceb egafbd\n"
                                        + "fdbgec bge fdacbe bfdga edfgb cbgaed gefc cefdb ge baegdfc | ebfgd gbe begdf eg\n"
                                        + "cedafb gdab fgced cabdf fgbaecd dcbgf gb bgc gabfdc ebfgca | bcadgf cbfda cdgfab agdb\n"
                                        + "fdcbg bgface ecdabf ad fgcbaed gcefad dac efgac cfagd daeg | dcfabge edga ad gcfea\n"
                                        + "bce gaecdb aefc bedfgca acbfge aebgf fbceg defgba dgfcb ce | ceaf ce bfgce ce\n"
                                        + "gefadc gefadb agd ebfcdag dbeg dfagb abfed gafcb dg adbcef | fbgad bged gd cfebda\n"
                                        + "dcfge egdbf adcgf bafecgd ecd gdfeac bdecaf ec cfgdab cage | acge dacgf facdg cgea\n"
                                        + "bagdec gecdfba gdabe deb eabc be acdgfe afgdb deagc gdecfb | aebgd cgdea adbge eabc\n"
                                        + "gebca bedgaf ac gcbeaf bcagedf bafge egcfda bedgc cabf cea | ecbdg efcgab ebagc bgadfec\n"
                                        + "edacbg gcfad efdabg bc cgfdbae bdc cfedab efbc beadf fadbc | dbc fcbe fbedacg cabfd\n"
                                        + "adfgce acgfb bgfadc dgbca fc gfc fgacdbe fagbe bcdf ebagcd | cbagdfe gfc fgbac cgbaed\n"
                                        + "gdbfec bea acdb ba bcged edabgc gcfaebd afebcg gedba gdeaf | ba fgcebd efgda gadfecb\n"
                                        + "gd gcda bgd gafbcde bdcfag bcgdf cedfb bgefda eacfbg cfbga | fgcab fdgcba bgacf bfedga\n"
                                        + "fgcadeb abdgc gce bagec fcbea ge aebcgd gead cedfgb bagdfc | gbdace baefc fdcagb cdgab\n"
                                        + "fbca aedfcg eagcbd fadbec bdgef fec cf bgcfade dbcef cbade | fgedb bgefadc fc cf\n"
                                        + "dbgfa feca gfbced cf abfgce fcb cafbg eabgc acdbge edafcbg | abgdf dbcgef geabfc fcb\n"
                                        + "dbaf fag bdfgea ecbgfa dgbfe fa edacg agcfdbe cefdbg fdgea | fgbaed cdegbf dacfbge gdcfbe\n"
                                        + "abc acefbd gacedbf gbcde dgabf ca fgcedb cega dgbca cgeadb | ca bcged abgfd bca\n"
                                        + "ceabgd cgfeb ea aedf gcbdfa cadbf acdfeb abfec eac dgcefba | cgdbafe cea adfe aec\n"
                                        + "dbcfgae agebc ebgd cebdfa ebacdg deagc be fdecga gbacf bce | deagcb agecdf caefdg cegad\n"
                                        + "dcaeg gea gdaf befgac cefgd facbdeg dcgbfe ga dagfec adecb | gadf gaedc age cabedgf\n"
                                        + "bead dagfbe gda bagfec ad egfba ecgfad fdcgb ecfabdg dfgab | agefdc dga bgfea dafcgbe\n"
                                        + "abcgd gecbfda cegd badfge bcaed fcaeb ed adcgfb gcabed edb | acefb acbgde deb ed\n"
                                        + "cgefdab dgbcfe gbe beca be fdecga bgcafe bfgda abgfe fgace | bfgda eb gbe bdgaf\n"
                                        + "age cefbadg bcfea cdagbe begca ag gedfca cbfdeg ebcgd dbga | efbac ega fecab dgba\n"
                                        + "cgedf fc defbgc cdf fceb dgebf bafgdce eadbgf ecgad gcfadb | cf deabgfc efgdbc fgbcda\n"
                                        + "bdgeacf bagdef gb becg bcfage cafbg bga fdbac caefdg eafcg | bcedafg cbeg bg gb\n"
                                        + "dfceb gcdab ecbad gfbdea eab gcbeda bgcdeaf ea abfgdc cgea | baedc egca dgacbe cedfb\n"
                                        + "gdaeb dgfbc cefg fe cfbgaed gdfeb fadcbg defgcb abcdfe dfe | gdcfba eagdb fe cfeg\n"
                                        + "abfcedg afgcde acgfd ae bdecga efbcd dea fdgabc feacd gfae | afdcge gaef aefgcd geacfd\n"
                                        + "beadfg cfdgb fdb agfcbde db abcgfd ecdgf cbagf bafgce bdca | efgcd dcefg cdgfe bd\n"
                                        + "fagbe fedbcag afbgcd agdfbe age adgcbe ea fead egbcf abgdf | gae cbegf afgdb ea\n"
                                        + "badfcg fagdc gaf fedcgba dagb acefd dbgcf cgfdbe ag gbecfa | ag bagd bdfgca fbadgc\n"
                                        + "cfebg gc fcbea dcbafe gbc gcedafb cegdba cagf fbgde baecgf | cg agcf befgc bgc\n"
                                        + "fegb ecdaf ecb gacefdb eb gdacbf cdegba abgfc afceb acegfb | ebafgcd afdgceb ebc be\n"
                                        + "adgb cgbae bg abecdg gcefa ecbad dbgfec gbe cbfdega baecfd | badg badg dbag aegbc\n"
                                        + "ab bfa gbedcaf adbgfe bfecg acefb fgdcae bcad fceda ebafdc | gebdaf edcfag ab ba\n"
                                        + "ceadbg cd gfdeca ebgfd gdfec dce fegbca ecfag fadc gdeabfc | gfaec gface fdac ecadgf\n"
                                        + "fbagd egdafb bdg adcefg gbfac daegbcf eadgf bd bfde dgceab | dbfe bd agfde agdef\n"
                                        + "ebg bgdfe acebdg dcfage abfdge agefd dgbcf eb bdfegac afbe | cdfbg aefb dcagef edfga\n"
                                        + "bfcd efc bfdcge gbdec bagfe agdceb cf dacgbef bfgec eadgfc | cfe dcbf cgebdf dbecag\n"
                                        + "cda cefdbg fgeca fdgbeca acgdf bdcgf da deafcb fbgcad bdga | afgbcd cfega gbfcda gdebcf\n"
                                        + "dcaf cagfb gdacb bgdfca fgaecb ebdag cd fdgcbae dbc bcfegd | cfdegb dc abcgd cbfedag\n"
                                        + "gabedc dgfab ecdfbg adegf afbc dbagc bacedgf bf fdb fdcbag | cfedgb dgcfab gbfcead dcbga\n"
                                        + "bcagde efd bgfcd fdecb aefb fe dcfbae gacfebd caebd afecgd | ebcdf afeb ef befa\n"
                                        + "abfc gcdaef agecdfb cb gacedb cgb gcbdf bdgfac gedfb gcdaf | gfceda bc bdfgaec bdgef\n"
                                        + "geabc gcebad cgbeaf begdcf gfabe fe bfgda gfe afce ebcfagd | febgac fcea fbcged baecgd\n"
                                        + "aedfb gbfe acbgde fe fcedgba aef fcadge bacdf fegdab ebdga | fcgdeba afdeb ef fae\n"
                                        + "gbcfd adfgcb gba dgcbafe agbfde ag cfag fecdbg gbdac edcab | fgcbd bgcad agb bdefga\n"
                                        + "agfde efgcba gaefc bdcfeg cfe bafc bgcedaf egbac agcedb cf | gebacf ecgafb gfceab cefag\n"
                                        + "febdc begcd ecgbdf dbacfe bdfagc adgec gbd gb afbdceg gefb | dbg gacbfd egcdfb gfebcd\n"
                                        + "decfbg dagfb afcgbd gdcfb cbaedf caefgdb da gdca gafeb abd | adbgcef dgafb abd cfbgeda\n"
                                        + "begcfd bgeaf dcfaeb cadfeg gcdb cadbgfe cg ecfbd cfbge cgf | cg gfc gdcb gcf\n"
                                        + "ecagdf dfcebg fgbea bg cbeadgf egb ebcafg edfab gbca fegac | bafeg aebfd gcab fbecdag\n"
                                        + "dgacf abfecg fcgadeb cgd gdbace cgbfa egfda dbfc cdbfga dc | cfbd gabdce dbgcaf egfda\n"
                                        + "afedcgb afge bfead gf gdfbe fgabed dgcbe dfceab dgf fabgdc | feag cdabef bgdacf gfea\n"
                                        + "adebgc de bfgacde beafdg egabc egcabf deg cead cbdeg dcgfb | ed eabdgf eadc dbafge\n"
                                        + "fcdbgae gbacf ebgdfc bcadeg bdgea fagdbe gce aecgb acde ce | cge ecg cabeg efdcgba\n"
                                        + "aefgcb fedagcb cefbdg ef dcbeg bacdge fegd edbfc fabdc cef | ecbgfd gdef bacdeg bcdeg\n"
                                        + "geadc gecfad gfcadb eg agdcf bgafce gdef acdbe gea dabegcf | ecfbag gebcaf baecd dfeg\n"
                                        + "cdeba bd aegcd bfadec ecfab bafd agbfce bfdcge bdc cgebdfa | abcfe agdce db adecg\n"
                                        + "fbadg faedgc bgdac bcegd bcadge agc gdefcb cbae gdfbace ca | defgcb ceagbd bdgca afedcg\n"
                                        + "acgfd cbdage agcbd dfa edcfg fa dbcfeag fabc dbaefg gdbcfa | af gedabfc af ebgdfac\n"
                                        + "dafgbc dfegc dc cgfae cgeafd cdf cagefb ceda bfged fdegcba | aecd cade deac fbged\n"
                                        + "cbegda dgfeb gefcb aefbgc fdgceb agdfe dgb bd fdbc bdgfaec | dcgbeaf db gfbed efdag\n"
                                        + "aefbd cdbeagf gd fdgc fgcdab afbcg bdg egfabc bfgad aebgcd | gdfc gadcfeb cfgd fgacb\n"
                                        + "eb gcbaed aeb fgdecba begd eagdc cadfb eabdc cfgade gacfeb | egdca dcbea bdfcgea be";

}
