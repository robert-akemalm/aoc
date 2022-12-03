package aoc2020;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class Day21 {
    public static void main(String[] args) {
        a(TEST_INPUT);
        a(INPUT);
        b(TEST_INPUT);
        b(INPUT);
    }


    private static void b(String input) {
    }

    private static void a(String input) {
        Food[] allFood = input.lines().map(Food::new).toArray(Food[]::new);
        Map<String, Set<String>> allergeneToPotentialIngredient = new HashMap<>();
        Map<String, Integer> ingredientToCount = new HashMap<>();
        for (Food food : allFood) {
            for (String ingredient : food.ingredients) {
                Integer count = ingredientToCount.getOrDefault(ingredient, 0);
                ingredientToCount.put(ingredient, count + 1);
            }
        }
        Map<String, String> determined = new HashMap<>();
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Food food : allFood) {
                for (String allergene : food.allergenes) {
                    Set<String> potential = allergeneToPotentialIngredient.get(allergene);
                    if (potential == null) {
                        allergeneToPotentialIngredient.put(allergene, new HashSet<>(food.ingredients));
                        changed = true;
                    } else {
                        if (potential.size() == 1) {
                            changed = null == determined.put(potential.iterator().next(), allergene);
                        }
                        changed |= potential.removeAll(determined.keySet());
                        changed |= food.ingredients.removeAll(determined.keySet());
                        changed |= potential.retainAll(new HashSet<>(food.ingredients));
                        if (potential.size() == 1) {
                            changed = null == determined.put(potential.iterator().next(), allergene);
                        }
                    }
                }
            }
        }

        System.out.println(ingredientToCount.entrySet().stream().filter(e -> !determined.containsKey(e.getKey())).mapToInt(
                Entry::getValue).sum());
        System.out.println(determined.entrySet().stream().sorted(Entry.comparingByValue()).map(Entry::getKey).collect(Collectors.joining(",")));
    }

    static class Food {
        final Set<String> ingredients;
        final Set<String> allergenes;

        Food(String line) {
            this.ingredients = new HashSet<>(Arrays.asList(line.split(" \\(contains ")[0].split(" ")));
            String[] allergenes = line.contains("(contains") ? line.split(" \\(contains ")[1].split(", ") : new String[0];
            if (allergenes.length > 0) {
                allergenes[allergenes.length - 1] = allergenes[allergenes.length - 1].substring(0, allergenes[allergenes.length - 1].length() - 1);
            }
            this.allergenes = new HashSet<>(Arrays.asList(allergenes));
        }
    }

    private static final String TEST_INPUT = "mxmxvkd kfcds sqjhc nhms (contains dairy, fish)\n"
                                             + "trh fvjkl sbzzf mxmxvkd (contains dairy)\n"
                                             + "sqjhc fvjkl (contains soy)\n"
                                             + "sqjhc mxmxvkd sbzzf (contains fish)";

    private static final String INPUT = "vjfx pfjrcc rrfg rdjcltd dgjvd rbgkxdq jcdjp qprp dptzgrt bfhd rlpnhg lgshl bqmhk gpqqcz mgpvqx rsq vztnmp klnm gzjgx nhjrzzj rpj lfz gbt dzrv zzlplm zglhrr ncrzbs jpcxzz frclq dbggg btffs cn qbmq xtnrljt zvtzs mdcbj bzfgm tctcqr kgcz qkkl jjfjd fbrjszh psbbsc blghf hvcj mqnccr dlrd kmzhd skhgbh zrpqf kbcbz zsbzcr vqzbq bscrs zbpv fcgtsq qjnlx hdvv qjcdvg btnkx qnxxkx dgc gkvdv zzcncd sfmrhg xvzfl qvzvcqt jjjz qhshq ptfq mbxbh ghjqj gkhxcb qlztc vkxgv jfvsx mtmm sssdn cqtfgb jqgf xxcgh tcqvk dtb mvgm ltd zqjm mtcqt (contains peanuts, shellfish)\n"
                                        + "zbxcf fvfct sssdn xqgzsc qvzvcqt hnpmf pfjrcc zzcncd npbpnmk zqjm klnm xstjn ddhmx zbpv gtgc xkd vqzbq ggd qkzp bqmhk mvgm bzfgm mdcbj vdxb nglsdslv dnb bdvzqg mtc gpqqcz skhgbh cljsqk hknpc zsbzcr prbgj btffs vgxzbdh zzfc rpj qbmq kgcz ncjhsb jpcxzz xzmxx ctgnj jjjz hcvqb hhxlz skvqn gbt fcdnv btnkx dvp dmblp rdrc lmd szhc gkhxcb jcdjp vrmcq htztq mhqbbs cn lpqq vqv rhlr dgpsh dtb dlrd rbgkxdq dzrv lfz brntsf zchzcg vztnmp tcqvk vkxgv tvch (contains peanuts, fish, sesame)\n"
                                        + "nhjrzzj tnrqn gbt txhccj hvktlrg fcgtsq dptzgrt fsst zqjm bqmhk ghjqj zpqgfm fcdnv hc dgjvd mdcbj vdxb dlrd cn gkhxcb kbcbz dtb kjnqq bspfmh szhc cqtfgb vqjcm qkzp psbbsc bdvzqg lfz dmblp mplcb qbmq zpnjz zmgkj ldj blghf fgnv rpj xdpzg tpkntx frhhb ndmc tvch ncrzbs mqnccr lkm mvgm lfn qpv gvgpv lpqq rzx bdll gqzrn dgc rbgkxdq xhttnt dzrv lvsclh zvtzs rhlr dlrz kvpk vjfx (contains nuts, wheat)\n"
                                        + "qhshq sfmrhg bscrs gqzrn lfz qbmq gbt mtcqt qprp zvtzs mtmm bqmhk qnxxkx rzx lmd cqtfgb qjcdvg hknpc xxcgh klnm rdrc tpkntx zvtx dpxxzz ssczkgc pfxk mnmgh ndmc ldj dtb sqtntgp thgdm qczkv dnfp gtgc vgxzbdh vqzbq zqjm vftnx lkm lpqq rdjcltd xqgzsc mbxbh nhjrzzj vqv zbxcf rbhd skvqn zpqgfm qtxs nsfq qdspjz dgc rsq jjfjd hcvqb vjfx vrmcq zlbpk gkhxcb hhxlz fsst lvsclh gpqqcz vdxb kmzhd tnrqn skhgbh ncjhsb zglhrr (contains sesame, peanuts, shellfish)\n"
                                        + "brntsf gzjgx dbggg fbrjszh qbmq pfxk sssdn qjnlx jfmn vqzbq xxcgh pfjrcc jjzkg dtb ssczkgc fcgtsq jqgf zvtzs zpnjz sngmfjv tctcqr fsbrq mdcbj psbbsc mhqbbs gbt btnkx qhshq vdxb ddhmx xdpzg hhxlz mttf zsbzcr dgjvd rpj sqtntgp mnmgh mtcqt kjnqq dpxxzz zqjm qjcdvg szln mgpvqx fcdnv qdspjz rbgkxdq vqjcm zbpv jqbxmh bdll nhjrzzj ncjhsb zglhrr tnrqn vkxgv zgcthq nglsdslv zzlplm kgcz qnxxkx gcvl kbqtt rcmjb szhc hdvv cljsqk nsfq dlrd cqtfgb vqv (contains eggs, sesame)\n"
                                        + "zqjm frhhb kbcbz fgnv zlbpk szln jnpcpvkp xdpzg qlztc ssczkgc hvktlrg jjfjd vztnmp pfxk vftnx dlrd nhjrzzj lvsclh xqgzsc qnxxkx zmgkj bspfmh rdjcltd hthvsl lmd hnpmf zchzcg gtgc qjnlx rzng zzcncd tcqvk dlrz zrpqf fsbrq skvqn zgcthq vgxzbdh zsbzcr zpnjz njg mtcqt bdvzqg mgpvqx vqzbq dvzldn qkkl vdxb dtb npbpnmk xzmxx ctgnj gkhxcb btffs rrfg mnmgh skhgbh rzx kgcz bqmhk rpj kbqtt xvzfl fkxmgt tnrqn ncrzbs dzrv nsfq mhqbbs (contains fish, shellfish)\n"
                                        + "mpdp ctgnj rpj hknpc mhqbbs qnxxkx gtgc szhc qpv ldj gpqqcz mplcb blghf sfmrhg hvcj bqmhk nhjrzzj ggd vqzbq kvpk dgc qdspjz jjzkg frclq dtb mnmgh lgshl lvsclh gbt dmblp skvqn qczkv kbqtt rcmjb jpcxzz zglhrr cljsqk dgpsh tcqvk dlrd qhshq hnpmf dzrv lfn jfvsx ddhmx xqgzsc txhccj zqjm zvtx htztq rrfg mvgm xstjn psbbsc tctcqr mtc rbgkxdq kbcbz fgnv zzlplm hhxlz zsbzcr qbmq thgdm lkm klnm ncjhsb btnkx dlrz hvktlrg dvp hthvsl gkhxcb jcdjp lpqq xkd mgpvqx nglsdslv brntsf dnfp mttf hdvv (contains sesame, nuts, shellfish)\n"
                                        + "nhjrzzj xstjn ncjhsb xtnrljt vqzbq nglsdslv cn mvgm qdmnxt fsst zzlplm bspfmh rpj ncrzbs zsbzcr mnmgh rcmjb fkxmgt ldj fcdnv tcqvk nsfq hnpmf jqgf zmgkj vdxb dmblp vqv zqjm rhlr txhccj mtmm zrpqf pfjrcc hdvv fbrjszh dgjvd hknpc jpcxzz tnrqn gvgpv bqmhk qhshq dtb jqbxmh jfvsx sssdn dvp zbxcf xxcgh fvfct bdvzqg zbpv dgc (contains shellfish)\n"
                                        + "vqv jqbxmh rdjcltd gqzrn dgpsh jnpcpvkp dmblp xvzfl mdcbj prbgj mvgm gkvdv tvch qnxxkx zqjm gzjgx rsq mtcqt nglsdslv vdxb dvzldn npbpnmk zvtzs qvzvcqt zchzcg rzng kbqtt ldj vjfx hnpmf hcvqb vqzbq frclq frhhb rpj nhjrzzj bdll gcvl jcdjp kgcz bfhd dtb cztdp mgpvqx ggd njg sqtntgp gbt cljsqk (contains dairy, fish, eggs)\n"
                                        + "lgshl hvcj zrpqf brntsf gbt dpxxzz xstjn qkzp zqjm mpdp vqv mbxbh jjjz dptzgrt ncrzbs vqjcm dtb mtcqt bzfgm vdxb hc qlztc ptfq npbpnmk vgxzbdh mhqbbs bscrs tvch bqmhk qnxxkx skhgbh rsq sqtntgp ndmc tnscnd dlrd zpqgfm rcmjb pfxk rlpnhg hnpmf kgcz mplcb rpj nhjrzzj dvp bfhd klnm xxcgh fcdnv hthvsl mnmgh (contains peanuts)\n"
                                        + "zzlplm lkm qtxs dgpsh dlrd mttf zbpv vqzbq bqmhk jjfjd szln bspfmh xdpzg dptzgrt qkkl lmd gkvdv rcmjb tvch ncrzbs gbt zlblpb rpj kvpk gvgpv vqv cqtfgb jqgf mnmgh xvzfl dtb zsbzcr dzrv qprp sfmrhg mplcb tnscnd hvktlrg zvtx zbxcf qjcdvg bfhd fsst txhccj ssczkgc mtcqt dvzldn ghjqj xkd rzng kjnqq njg ncjhsb fgnv btnkx zpnjz xhttnt zqjm zpqgfm jfvsx sqtntgp mtc vdxb gpqqcz (contains eggs, shellfish)\n"
                                        + "hnpmf vftnx zqjm ptfq gzjgx hthvsl sssdn qjcdvg mtcqt dgpsh vgxzbdh qczkv mvgm szln gkhxcb mbxbh mplcb dnfp sqtntgp vdxb vqzbq bfhd ltd jjjz pfxk dgc btnkx xhttnt rbgkxdq vrlfm mttf hhxlz zvtx ncjhsb thgdm zzfc jcdjp hvcj kvpk mdcbj dpxxzz xzmxx vrmcq qprp bspfmh rrfg lpqq skhgbh kjnqq zzcncd dlrz zchzcg tcqvk rzx pfjrcc qhshq psbbsc gqzrn tctcqr rpj gbt bdll htztq dtb bzfgm jfvsx ncrzbs nhjrzzj lfn blghf qkkl xqgzsc (contains shellfish, dairy, sesame)\n"
                                        + "dvzldn dgpsh sssdn xvzfl npbpnmk hsghzn xkd qdmnxt vqzbq mqnccr vftnx rdrc zmgkj sngmfjv bdvzqg klnm fgnv jqbxmh zrpqf lkm dgc bscrs lvsclh gbt hknpc jfmn rhlr zzfc lpqq ssczkgc ncrzbs vgxzbdh mhqbbs fsbrq dmblp qpv ghjqj rpj rrfg hc bqmhk zglhrr pfxk rbgkxdq zzcncd mtc zpnjz kbqtt zzlplm jnpcpvkp qkkl gkvdv nhjrzzj fcdnv dtb vrlfm gcvl psbbsc lmd zqjm zvtzs ltd bzfgm nsfq qhshq (contains eggs)\n"
                                        + "hknpc gbt pfjrcc jjzkg mplcb vrmcq vqjcm ndmc psbbsc hcvqb xstjn tvch zlblpb nsfq fsst ctgnj qkkl bqmhk txhccj mqnccr xzmxx xtnrljt tpkntx xqgzsc zzlplm klnm rrfg zsbzcr qczkv sfmrhg nhjrzzj dnb jjjz xkd gvgpv qbmq vdxb fkxmgt cqtfgb hnpmf mbxbh mttf lkm zqjm mhqbbs rhlr zbxcf ptfq ghjqj zmgkj zbpv ltd kbqtt blghf vqzbq gzjgx zvtzs tnscnd lpqq tctcqr gcvl btnkx jqgf hdvv dtb qpv ldj hc pfxk dmblp vgxzbdh zvtx tnrqn (contains fish)\n"
                                        + "qkkl vztnmp zzcncd gkhxcb vqzbq zqjm dpxxzz tpkntx prbgj hhxlz kvpk rdjcltd zgcthq dtb nhjrzzj ndmc dmblp mtc gvgpv htztq bscrs zvtzs hthvsl hvktlrg btffs sbcjfl dbggg dptzgrt qtxs ldj zpqgfm vrmcq gbt jqgf szhc klnm rpj vqv bzfgm mvgm ltd fgnv tcqvk zlblpb bdll thgdm zchzcg kjnqq dnfp blghf dvp jqbxmh jcdjp ssczkgc fsbrq bqmhk kmzhd zbxcf dlrd rcmjb gqzrn zbpv xdpzg dnb rbhd xstjn jnpcpvkp ctgnj skvqn qpv qvzvcqt rsq tnscnd jjzkg hvcj lkm rlpnhg qdmnxt dgjvd (contains eggs, shellfish, nuts)\n"
                                        + "bzfgm lfz jqgf zbpv ctgnj bqmhk ptfq rpj fvfct zpnjz zglhrr gkvdv zqjm dtb cn hknpc qdspjz qjcdvg dbggg rlpnhg ldj vqzbq btffs xkd rsq gtgc zchzcg mgpvqx hthvsl hsghzn rzx sbcjfl pfjrcc jqbxmh fsst mtc gpqqcz vdxb szln vrlfm gbt sqtntgp dmblp mqnccr xxcgh ltd (contains fish, dairy)\n"
                                        + "xxcgh dptzgrt frclq lfz qpv cztdp dvp szhc gbt skhgbh gzjgx kvpk mtcqt ctgnj vkxgv xdpzg qjcdvg mtc hvktlrg mqnccr zpnjz vqv bdvzqg psbbsc hc qbmq dtb hdvv gvgpv cn zbpv fgnv ddhmx vdxb qprp hhxlz vqjcm mnmgh qkkl lpqq ssczkgc tcqvk hthvsl qlztc rdrc zqjm jfmn gkvdv zvtx prbgj nhjrzzj zzlplm vztnmp zlblpb dbggg sqtntgp tctcqr rpj mpdp qtxs pfjrcc jnpcpvkp tnrqn cljsqk fkxmgt vrmcq dnb rzx rcmjb bzfgm vqzbq (contains fish)\n"
                                        + "dnfp jqbxmh cljsqk rsq dpxxzz jfvsx hknpc brntsf jcdjp gbt tpkntx zgcthq ssczkgc gcvl dmblp mbxbh zchzcg jjfjd txhccj btnkx lmd dnb zmgkj vqzbq ptfq dzrv qtxs xzmxx hthvsl vdxb bdll frhhb rpj xkd dgc kbqtt btffs npbpnmk ggd zglhrr bqmhk ncrzbs thgdm kbcbz sngmfjv fcgtsq dlrz jjjz jnpcpvkp dtb sbcjfl fkxmgt fvfct mvgm hvktlrg hc dvp hsghzn nhjrzzj zzlplm (contains dairy, eggs)\n"
                                        + "zvtx qlztc rlpnhg zpqgfm cljsqk hthvsl xqgzsc npbpnmk zlbpk gkvdv vqzbq jnpcpvkp zpnjz zglhrr hknpc bscrs skvqn nsfq lpqq dnfp lmd tctcqr xkd fbrjszh bdvzqg frclq fkxmgt njg psbbsc tnscnd gqzrn qhshq rcmjb dmblp kbqtt qpv bqmhk dvzldn ncrzbs zzfc mdcbj dgpsh dtb gtgc vdxb jfmn rbgkxdq sbcjfl qkzp dgjvd fsst gkhxcb txhccj mtmm zchzcg ndmc zqjm rdrc hnpmf blghf rzng lfn hvktlrg dgc xzmxx zlblpb ptfq frhhb xstjn dlrd zzlplm ghjqj rrfg gbt cn vgxzbdh zsbzcr skhgbh rsq hcvqb mttf vftnx nhjrzzj ssczkgc szln btnkx (contains sesame, fish)\n"
                                        + "zchzcg rpj mnmgh kvpk qprp gbt zqjm brntsf jfvsx dgc dnfp zzcncd ctgnj sfmrhg qnxxkx ddhmx vqzbq fvfct qvzvcqt blghf lmd ncrzbs gzjgx tvch jqbxmh dlrd ncjhsb ndmc jqgf hhxlz mhqbbs bscrs gkvdv hdvv vqv zlblpb ltd mgpvqx kbcbz zvtzs dgjvd nhjrzzj dtb hvktlrg vkxgv rdrc jfmn ptfq bqmhk rsq qjnlx zmgkj qkkl tnscnd qjcdvg qczkv psbbsc szln zglhrr dvp dlrz jcdjp zsbzcr (contains dairy, eggs)\n"
                                        + "vftnx mqnccr zvtzs nhjrzzj tpkntx tctcqr lfz hdvv gqzrn xzmxx hvcj blghf bfhd fgnv mtc txhccj zrpqf hnpmf zgcthq bqmhk rzx zglhrr zqjm sssdn jqbxmh dvzldn hsghzn szhc tnrqn rpj lkm jjfjd mbxbh htztq vqv lgshl vdxb qjcdvg gpqqcz mtcqt dgpsh brntsf hthvsl rhlr mgpvqx ptfq qhshq rlpnhg fkxmgt xtnrljt prbgj fcgtsq rzng vkxgv gvgpv vqzbq jqgf vjfx mnmgh xkd ldj gzjgx gbt lpqq qnxxkx ssczkgc vztnmp gcvl (contains peanuts, shellfish, dairy)\n"
                                        + "sbcjfl psbbsc fkxmgt bzfgm xqgzsc mtcqt vztnmp tvch dpxxzz xxcgh dgpsh jcdjp zsbzcr xhttnt gtgc dlrd rpj skhgbh cqtfgb gbt fvfct btffs hknpc ddhmx mhqbbs zlbpk zzcncd tctcqr gpqqcz brntsf bfhd dtb pfxk dmblp vdxb zglhrr kvpk rhlr zbpv qdmnxt mbxbh fsst ndmc hc vkxgv nhjrzzj lkm qhshq xstjn xvzfl bdll vqzbq hvktlrg lgshl ggd frclq jjjz tcqvk zvtx bqmhk cztdp hdvv mgpvqx ncjhsb fcgtsq bscrs qjnlx fsbrq thgdm (contains eggs)\n"
                                        + "zglhrr ltd xtnrljt gzjgx sqtntgp mgpvqx jqbxmh gbt bspfmh dtb hc ptfq zzcncd dgc kbqtt dbggg dnfp xdpzg tvch zgcthq tctcqr sngmfjv hdvv vqjcm pfxk ghjqj qnxxkx zlbpk rzx dlrz lkm mplcb vqv hhxlz qjnlx rpj skhgbh jfmn vdxb ddhmx qhshq vqzbq zzlplm zqjm mhqbbs frclq fvfct zchzcg xzmxx xstjn dzrv prbgj mttf mqnccr ndmc nhjrzzj (contains eggs, sesame)\n"
                                        + "vftnx gbt zbpv xtnrljt dzrv cn xzmxx rdjcltd zqjm jjjz sfmrhg lgshl sngmfjv bscrs hc hknpc zlbpk vztnmp npbpnmk btffs mtmm zrpqf fbrjszh bdvzqg fsbrq vqzbq mttf rhlr ldj skhgbh gcvl dgc ddhmx gqzrn lpqq cztdp qtxs mtcqt qdspjz hsghzn rpj dtb fcgtsq xstjn bqmhk jjzkg qbmq rzng zsbzcr vdxb mdcbj jfmn dmblp (contains sesame, shellfish, fish)\n"
                                        + "gpqqcz hc dgc nglsdslv zqjm dvzldn rbhd ssczkgc qdmnxt zzlplm zzfc skhgbh kbqtt szhc sssdn xtnrljt dtb zglhrr gvgpv fsbrq dlrz hknpc hsghzn fvfct ptfq qjcdvg hvcj vqzbq mttf psbbsc mtmm jjjz rlpnhg ndmc lfz vqjcm xqgzsc gzjgx xkd cqtfgb zchzcg zbxcf xvzfl gkhxcb hvktlrg pfjrcc qjnlx vdxb tnrqn cljsqk fkxmgt bdll cn qvzvcqt sfmrhg dpxxzz nhjrzzj bfhd mpdp qdspjz vztnmp mplcb kvpk sbcjfl jfvsx rpj pfxk dlrd bqmhk frhhb lpqq dmblp bdvzqg ltd npbpnmk qlztc jpcxzz qkkl bzfgm (contains eggs, wheat)\n"
                                        + "mqnccr zzcncd mpdp kgcz mdcbj sbcjfl hcvqb mtc qvzvcqt gbt vgxzbdh vqv szhc zsbzcr jjfjd vrmcq gtgc rpj xkd dvp xdpzg btnkx bqmhk qpv hthvsl tnrqn zchzcg ncrzbs fbrjszh zzlplm sqtntgp xzmxx dnb gvgpv zlblpb nhjrzzj jfmn rdjcltd kbcbz mttf vdxb gzjgx frclq hnpmf zpqgfm fcdnv qkkl psbbsc bspfmh pfxk zgcthq dvzldn ssczkgc lvsclh mtcqt vkxgv njg jjjz bscrs vqzbq vftnx ltd rrfg qlztc vqjcm ncjhsb fvfct npbpnmk hknpc gpqqcz sfmrhg xvzfl bfhd sssdn mbxbh dmblp xxcgh zqjm nsfq zzfc brntsf (contains wheat, sesame)\n"
                                        + "qdmnxt lpqq kgcz kvpk zzfc zlbpk ndmc gqzrn rpj rhlr jqgf rsq tcqvk lkm xdpzg xqgzsc fgnv vrmcq vrlfm zqjm qjcdvg hcvqb ptfq vjfx ldj fsbrq frhhb gvgpv dtb dnb nhjrzzj gbt njg mtmm bqmhk gkvdv qnxxkx dbggg txhccj vdxb zrpqf hvcj zvtzs (contains peanuts)\n"
                                        + "lfn kvpk ndmc vrlfm vftnx vqzbq dzrv gvgpv zgcthq gcvl lvsclh zbxcf njg bzfgm jfvsx vgxzbdh htztq zbpv rdjcltd ltd qlztc rpj qprp bspfmh npbpnmk jfmn hvcj bscrs rzx vdxb cljsqk jpcxzz zzlplm kjnqq zvtzs zqjm mtcqt ghjqj vztnmp lpqq nglsdslv nhjrzzj dnfp pfxk hsghzn mpdp dvp rbhd hthvsl tnrqn zsbzcr cqtfgb mgpvqx sbcjfl xxcgh lkm prbgj jcdjp jqgf jqbxmh xdpzg dtb rcmjb mplcb mtmm fsbrq dvzldn vqjcm hhxlz bqmhk blghf fkxmgt jjjz dlrz frhhb (contains sesame)\n"
                                        + "mhqbbs bqmhk xqgzsc dgjvd fbrjszh hc ghjqj skhgbh hthvsl bzfgm zrpqf kvpk ddhmx tcqvk nsfq rpj jfvsx fcdnv rrfg kjnqq btnkx rdrc ltd hdvv nhjrzzj lmd qvzvcqt jqgf ncrzbs psbbsc jjfjd mvgm vdxb fcgtsq jcdjp bfhd tnscnd bscrs gbt xzmxx zqjm vqzbq qjnlx hvktlrg dgc ssczkgc dzrv klnm bspfmh rhlr (contains peanuts, shellfish)\n"
                                        + "bqmhk hcvqb vgxzbdh mgpvqx mttf dmblp dzrv cn gcvl zmgkj jjzkg klnm zgcthq zqjm rbgkxdq vqjcm gpqqcz fbrjszh zzfc rzng qvzvcqt mvgm cqtfgb lpqq qtxs fcgtsq zglhrr frhhb zsbzcr tnscnd vqv gqzrn btnkx ldj vrmcq szln tcqvk dlrz ptfq rbhd rpj gkvdv rdrc szhc mbxbh jpcxzz xxcgh bdll gkhxcb jnpcpvkp gbt mplcb prbgj rrfg frclq lkm gvgpv hnpmf rzx sssdn ggd zpqgfm mtc hsghzn rcmjb dptzgrt vftnx dvp dgc kjnqq vdxb qkzp jfmn lvsclh qpv fvfct tctcqr pfjrcc zbpv zpnjz nhjrzzj bfhd fcdnv qkkl jqgf zlbpk vjfx mtmm hvcj vqzbq (contains sesame)\n"
                                        + "ddhmx klnm hsghzn vkxgv hc vqzbq psbbsc vjfx mbxbh vftnx prbgj jqgf mqnccr njg hcvqb lmd ggd zmgkj qdspjz dlrd vdxb zzfc qjnlx xvzfl nsfq qvzvcqt mtc gbt brntsf xhttnt xzmxx szln ghjqj dlrz ldj rzx tpkntx zsbzcr bfhd gtgc tcqvk qhshq zzcncd sqtntgp dtb jnpcpvkp htztq gqzrn vgxzbdh bdll mgpvqx rdjcltd jjzkg rbgkxdq hvcj zqjm gcvl cljsqk mhqbbs rbhd lpqq qjcdvg npbpnmk rpj xkd kmzhd zlbpk fgnv zlblpb fcdnv zbxcf rcmjb lfz skvqn nhjrzzj rsq gkvdv sssdn ndmc xtnrljt pfjrcc fvfct gzjgx qkzp dnfp (contains wheat, eggs, sesame)\n"
                                        + "qnxxkx gzjgx fsbrq vqv klnm txhccj xvzfl rdrc dzrv hvktlrg gcvl jfvsx qjcdvg jqbxmh hcvqb xqgzsc fsst mbxbh gkvdv hvcj tpkntx rhlr dvzldn rpj gqzrn dgc blghf zvtzs jpcxzz rbgkxdq ldj ctgnj lfn mgpvqx bfhd cqtfgb mhqbbs vftnx tnscnd qtxs tvch rcmjb lfz lkm fcgtsq lgshl fvfct nhjrzzj gtgc sqtntgp frclq lvsclh xhttnt cljsqk jjfjd sssdn zpqgfm lmd sbcjfl qdmnxt xdpzg xtnrljt dlrd ptfq zsbzcr vrmcq sfmrhg qkzp dnb xkd qjnlx gbt qvzvcqt mttf dtb njg frhhb zbxcf mqnccr bqmhk vdxb vqzbq (contains nuts, dairy)\n"
                                        + "skhgbh fsbrq jjfjd fkxmgt nhjrzzj blghf ncjhsb dvp vqzbq vrlfm zqjm bscrs dtb zzfc kjnqq xkd hnpmf jcdjp vztnmp szhc zlbpk bdvzqg rbhd dnfp gbt vftnx qlztc sqtntgp qkkl tnrqn zbpv jfmn hvcj npbpnmk gqzrn vdxb cn szln dlrz psbbsc jqbxmh kgcz vqv qpv bqmhk vqjcm mtcqt bdll (contains wheat)\n"
                                        + "lmd ldj psbbsc mtc qtxs nglsdslv qdmnxt lkm vdxb dzrv sfmrhg kmzhd xstjn hsghzn bzfgm dgc bqmhk vftnx mtcqt zqjm dtb nhjrzzj bdvzqg cn fbrjszh tnscnd hnpmf bdll lpqq rpj xkd gpqqcz zbpv xdpzg npbpnmk tvch hdvv szln pfjrcc jfvsx jcdjp zzfc vgxzbdh sbcjfl hvcj cztdp qprp sqtntgp vrmcq ddhmx btffs qhshq rdjcltd xzmxx zsbzcr jqbxmh xqgzsc rlpnhg mgpvqx sssdn fsst tctcqr hc jqgf qkzp rdrc qjnlx dgjvd vqzbq kjnqq zvtx dnfp gkvdv (contains wheat)\n"
                                        + "ssczkgc dgc cljsqk dptzgrt vkxgv hvcj vqjcm xqgzsc bqmhk hdvv zpqgfm fkxmgt zmgkj fcgtsq dtb mdcbj zbpv vrlfm psbbsc dmblp ghjqj zqjm qbmq sngmfjv klnm qtxs kmzhd mtc gvgpv rrfg btnkx vztnmp hcvqb fvfct lgshl ncrzbs zsbzcr sbcjfl vdxb hsghzn gkhxcb qhshq rsq xvzfl fgnv kbqtt rpj jcdjp zlbpk mnmgh gbt rcmjb frhhb vqv njg bdvzqg ncjhsb ggd vqzbq zzfc (contains eggs, sesame, shellfish)\n"
                                        + "bzfgm zchzcg cljsqk mbxbh szhc hc vdxb vqzbq zzcncd hthvsl dgc qkkl prbgj xhttnt qjnlx zmgkj btffs gvgpv rzng dvp kmzhd dpxxzz mtc cztdp gkhxcb zglhrr jnpcpvkp skhgbh zvtx hvcj ghjqj bfhd gqzrn klnm fkxmgt hnpmf ctgnj thgdm zqjm gbt fcgtsq sssdn fgnv fvfct xxcgh rpj txhccj zlblpb bqmhk rdjcltd lmd nsfq rzx xstjn vkxgv tnrqn xtnrljt mpdp kbcbz ndmc nhjrzzj gzjgx qjcdvg jjzkg sfmrhg kbqtt hhxlz lgshl mttf gcvl gpqqcz xzmxx dbggg lvsclh hvktlrg vgxzbdh (contains peanuts, sesame, fish)\n"
                                        + "jqbxmh fbrjszh zbxcf qtxs qhshq hhxlz qdmnxt mplcb dvp hsghzn rpj cljsqk btnkx npbpnmk dmblp bdll ctgnj dgc zqjm mdcbj zzcncd cn frhhb lfz xxcgh vdxb htztq dtb vftnx vqzbq nhjrzzj gzjgx gkvdv rsq bspfmh rzng xstjn mbxbh gkhxcb ssczkgc qkkl bqmhk ggd (contains wheat, fish)\n"
                                        + "gvgpv fbrjszh zvtzs vdxb tctcqr kmzhd nhjrzzj tvch cn dgjvd vftnx vrmcq rbhd jfmn rsq gkhxcb mqnccr bdll zrpqf bscrs zbxcf cqtfgb xvzfl hhxlz zchzcg ssczkgc sqtntgp lvsclh fsbrq vkxgv xkd zbpv jfvsx dpxxzz kvpk ncrzbs mtmm zqjm vrlfm dvp xxcgh bzfgm mdcbj gbt lfn xtnrljt njg qjcdvg gtgc ptfq vztnmp qkzp jqbxmh tnrqn rpj ggd vgxzbdh sfmrhg bspfmh gkvdv zmgkj ltd dtb fgnv rlpnhg ctgnj fcgtsq rrfg qpv ghjqj lkm npbpnmk lgshl jjzkg bqmhk bdvzqg mplcb dbggg rzx jjfjd hcvqb mpdp qczkv (contains fish)\n"
                                        + "cljsqk qjnlx cqtfgb dnfp jfmn gkvdv vkxgv dgjvd kmzhd cztdp lmd gpqqcz gbt qdmnxt sqtntgp bzfgm vrlfm mgpvqx nhjrzzj xvzfl kbcbz ddhmx dlrz mbxbh dtb vqjcm dpxxzz vdxb xdpzg hcvqb xtnrljt skvqn qkkl frclq kgcz tcqvk zlbpk fsst ndmc npbpnmk mttf qprp zglhrr zqjm vqzbq vgxzbdh dzrv tctcqr mplcb rlpnhg dnb btnkx hvcj txhccj jpcxzz ldj frhhb mpdp hvktlrg zlblpb sssdn bfhd rdjcltd rhlr dvzldn jcdjp rpj qnxxkx (contains wheat)\n"
                                        + "tnscnd dgpsh xzmxx vqjcm vdxb sfmrhg fvfct bfhd vqzbq jjzkg nhjrzzj ctgnj hvktlrg ndmc zvtzs bqmhk dbggg jqbxmh gbt nglsdslv fsst jjfjd gvgpv nsfq jfmn cqtfgb qhshq sssdn fbrjszh kbcbz sbcjfl szln vqv rpj lmd zgcthq qkzp lfz lgshl npbpnmk tctcqr dgjvd tcqvk tpkntx ncrzbs xxcgh mtcqt jnpcpvkp frhhb thgdm rhlr cljsqk qlztc fsbrq fkxmgt lvsclh zchzcg xvzfl zqjm qtxs jpcxzz btnkx tvch qpv gkvdv gqzrn dpxxzz cztdp mgpvqx hknpc mplcb (contains eggs, sesame)\n"
                                        + "klnm zbxcf tnscnd dtb dgc fsst tcqvk gpqqcz btffs zmgkj mhqbbs vqzbq rsq dmblp gkvdv hc dvp gvgpv mbxbh zzfc gbt vrmcq rdrc blghf zglhrr qprp mttf nhjrzzj zqjm vgxzbdh qpv bqmhk fcgtsq mpdp tvch qkzp vftnx ggd mgpvqx qjnlx mtmm szhc npbpnmk fvfct gtgc gkhxcb frhhb lmd bspfmh jpcxzz jfmn ltd vdxb rdjcltd (contains peanuts, sesame, dairy)\n"
                                        + "zgcthq mtcqt gkhxcb gbt lfz hvktlrg vqv vjfx qprp lpqq vqjcm bfhd rrfg sqtntgp kbqtt vrmcq txhccj kvpk dgc dgpsh qnxxkx sfmrhg ldj nhjrzzj gvgpv skhgbh gqzrn dtb npbpnmk szln dnb ncrzbs sngmfjv vrlfm rpj mgpvqx dvzldn bspfmh zbxcf hknpc dlrd bqmhk vdxb fkxmgt jnpcpvkp dptzgrt mttf klnm qjnlx vqzbq htztq qjcdvg tpkntx rbhd (contains wheat, sesame)\n"
                                        + "rbgkxdq szhc ddhmx mhqbbs gzjgx cztdp hdvv kvpk npbpnmk zbxcf ncjhsb gbt vqjcm vgxzbdh ssczkgc qtxs pfxk rpj prbgj jjzkg vrlfm dlrz dvp gtgc bscrs hknpc xhttnt gkhxcb lfz dnfp skhgbh dbggg rsq zvtzs mdcbj dlrd dtb zglhrr zqjm lmd lvsclh vqzbq zzlplm cn mnmgh dpxxzz psbbsc xqgzsc cqtfgb xzmxx zlblpb mbxbh klnm xdpzg mttf fgnv zlbpk qprp dgpsh xkd jfmn frclq rhlr dgjvd vdxb brntsf bqmhk cljsqk zgcthq kbcbz hnpmf fcgtsq vztnmp zzfc (contains nuts)\n"
                                        + "qbmq dnb rdrc mttf dbggg tctcqr szln hnpmf xhttnt vgxzbdh lgshl xvzfl tnscnd ggd lpqq hvktlrg rpj rbgkxdq mtc bqmhk kgcz dlrd ndmc zchzcg qprp ncrzbs xstjn zsbzcr txhccj rhlr bscrs dgjvd qdspjz dnfp dgc hhxlz xqgzsc hdvv vqv rdjcltd fcdnv hc dtb rcmjb cqtfgb sngmfjv mbxbh kbqtt gbt vdxb gpqqcz bzfgm rbhd zgcthq hknpc mtmm kmzhd gkvdv cztdp zlbpk zmgkj lfz frclq hcvqb vqzbq njg sssdn qkzp jcdjp fgnv skhgbh bdll vrmcq hthvsl kvpk klnm gzjgx cljsqk ldj fsst ghjqj qkkl nhjrzzj qjnlx fcgtsq (contains sesame, peanuts)\n"
                                        + "hthvsl bqmhk zsbzcr tpkntx ggd vqjcm mtcqt ctgnj jpcxzz xxcgh vztnmp dvp gbt ssczkgc vftnx xtnrljt ldj dnfp ghjqj dlrz zmgkj qpv zqjm qtxs fkxmgt zlblpb qjnlx hvktlrg zvtzs lvsclh xkd npbpnmk txhccj hknpc frclq qvzvcqt cqtfgb ltd bspfmh brntsf cljsqk jjzkg jfmn mpdp dtb btffs rsq hc qjcdvg zzcncd mttf qdspjz dmblp tvch vqzbq hsghzn lpqq rzng jfvsx rlpnhg rdrc jjfjd rbgkxdq hhxlz dnb nhjrzzj fsst kbqtt xdpzg rpj (contains dairy)\n"
                                        + "sbcjfl dgjvd jnpcpvkp dgc zlbpk mplcb rdjcltd lvsclh vqzbq tnscnd vztnmp tpkntx qtxs ggd gkhxcb lmd bdll mhqbbs zrpqf gtgc cn kmzhd qprp rcmjb dlrz qjcdvg ncjhsb pfxk ldj qvzvcqt qdmnxt mdcbj rzng dvzldn rpj blghf zvtzs vqjcm bqmhk xhttnt gkvdv jqgf ltd zchzcg xstjn xzmxx zqjm xtnrljt qkkl fsst mvgm rdrc dmblp hsghzn ctgnj mgpvqx qhshq lgshl dnfp tcqvk rsq lkm tctcqr gbt mtcqt hthvsl lfn nhjrzzj dtb zvtx vftnx (contains peanuts, dairy, nuts)";
}
