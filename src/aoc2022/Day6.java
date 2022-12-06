package aoc2022;

import java.util.stream.IntStream;

public class Day6 {
    public static void main(String[] args) {
        a(TEST_INPUT);
        a(INPUT);
        b(TEST_INPUT);
        b(INPUT);
        b2(INPUT);
    }

    private static void b(String input) {
        for (int i = 14; i <= input.length(); i++) {
            if (input.subSequence(i - 14, i).chars().distinct().count() == 14) {
                System.out.println(i);
                return;
            }
        }
    }

    private static void b2(String input) {
        System.out.println(IntStream.range(14, input.length())
                                    .filter(i -> input.subSequence(i - 14, i).chars().distinct().count() == 14)
                                    .findFirst().getAsInt());
    }

    private static void a(String input) {
        for (int i = 4; i <= input.length(); i++) {
            if (input.subSequence(i - 4, i).chars().distinct().count() == 4) {
                System.out.println(i);
                return;
            }
        }
    }

    private static final String TEST_INPUT = "mjqjpqmgbljsphdztnvjfqwrcgsmlb";

    private static final String INPUT =
            "mnlnvlljqqccznnjtjljbllrtllwwpmmhjjbbzppnndmmsppdqqwvvstvssgmsggmlmttnvvfbbdsssnzzbssjrsjjpmpvmmcjjwsssndsslwsswtwnwrrslshhvzzsppffmpfmmfvfpfpsssqpqzpqqcjcjnjcnnzbzjzpzbpbnbwbcctvvhgvgsvvpwwvjjvqjjjdqqrmrmqmsqszqsqpsqslsddhbhcbhbchcvvjvjcjnccdbcdcrddldblbffhvffpvpzpvvmvfmmwhwqhqvhqhmmpdmmlbmbgmbbrqqpmqqcvcmvvcncllptltvtdtbbqzzcggjgsjjvjsvvgmgffqhqqgpptspsffvdvbbhqhzzllvvjbvvbpppggfpgptgtvvzdvdzdgzgccmmphpmhppldlnlpnnhghhrrgwrwssnllmpllbvbvqvtvhtvvmnvvpgvgfvggtztpthhcfhfqfhhnhtnhhljhjppqjjffgjggrwrjjhphzhtztggwswnwzzvbzzmmtrtqtjqttwlwmmmmnddmnddwvvcllgrgfgzznwnsswjjhwwspsbbvzzqvzvbvcvmmtltnlnfnfnwwsvwwpswppjhjdhhmbbblfbfwffwvvjgghwhzhjzzrttwhwjhjchjhggdrgdgmmsjsfstftvtmtctggcwgcgzccgzgsgrgmmjhhqzzrmrttgtgfgcffvsfvvslsvvpqvvnjnrrwdwcwcnnhllwpwdpwdpdqqtwtftdftddppncpnccllqqrffpssgvsvtvmvssrbrhbbzggtssdsvddqfdfjfhjhdjhhncnddfpdfdmmrddncnvcnvccgvvhzvzwztwzwtzwtwqttrlrvrddztzrzcccgmmqgqjgqjgjqqspqqpjppbggchcqcpqqgbgdbbspbbrbhrhzhqhrqhhhtbhhvshspsvsggjdjwjwvjvdvwddjggmrrbnrrztthlllhlclbclbbhpbhphjhccdwccdbbjrbjjmrjrhhnlnjllltwltlmmlqlnqntqnntsnsqqvtvwvgwvwnncgcdctddnttfjfqfttrhrjhjqqcnnsmnmgmqgmgbblcctntrntnccnvvmpmjjvfjfrrbpbttsbttvnttmnnjdnndnzntnrnwrwcctllvhvqhhddmzztppphghphzzglzlnnfccrfffvvhllpspwssstwstsvttcrtccfssbccdjdqdfqddrbrqbbtllmmsfmfcmmzwzpznpnttjgtgbbdtdvdwwpmphpprsrjrbrqbqwwljlslrlrhhpchpcprcrtcrcfcfssndsspddcjjjfmfqfggmssnhsnhnpncnfnmffdrdjdhjjrgjrjgjqgjqqlmlljffbcfcrrrzwwftwtrrpgpprqqmrmhmwhwmmcrrhqrqwqppwjwggpdpgpvgvzvttqlljhhbvbhblhhcsssvmsmppcvpvrvzvbvtbtssplpgptgtnthhvwhvwwfvvfwwmtwmwfmfgmgnmnllgsgmsgswwhqhhhzqhhfwwnttmfmrrfnnpbpssvbsshqhqvhqqbmbpmbmqqjtjqjvjtjjhtttpzphpqqwqfqttqqhfhbfhfwwcpcpssdvvzhzwwqddjzdjdldlggvnvlnlbljlqjjmcczbccznnlnslswlwplpttvrvllfwflftllhclldhlhddbvvpvzpvpmmrccvgvdvqqjcqqwvvnjjlbbjwjrjhhlzhlhttljlcjjsnsgngrnntzzbsbmbsbrbdrdppjrjlrrjljqlqhqqnqsgdvhpgdhmnslqtjclmcfzrmgmlfnjbzznfgfprvwprwdbcgfcclmspgnzpbshwjbqvhzhrhswjzbfvnmcjtfvqbwmjpvfvctpmwsspdbtvfhfdfzjdpqnvslgmdvrnflzwzcnzmvzsvznwhpwtjwnqdgrrttmmdwzbbnwtllpbffrgtpjjjwltqrcbqcttdwnfjpmhdsbbpqmstjqchgjvfrmrbgqrlstnbdnzzzbzbsmsnnsssswmqhcbswtjhmcgnwmcclhzjqjzqcpbzgdzjgqzpqbmvvhtcznfrhdndswfvfhtfpdpszpjqrlwfdscvcngftwqmfttjtjrlbgcwvcjwsstqmcblmjzsgtgrqnqqvhzhvsphjmbcpfcznlcqldcvhlsvggbjngmhspwwqhlwstslvwmmbwqdmrgdvvnlstmjllhzscrhzjtmnsjfbndnlmzqbzgdgbcqchnbvwsftjtznnbsnvsgzpdzdqznjsslrlfnccdhwsljhczggvmgqswjltmrqqmwtbzmtdzhpjcvmwsscsdzpfnwlcrrdgzqqdmgwdlzvvvjcqsgpcwvrdnrstpcmgfjnjffbfmgzjthhllzrlsjtnqfppltbrlnqnjvqlvtpqvsbfgmmlcdzhgmzzqjwtqtzmpwwddbqrqnfzzpsjglsjddsslwwlrttzfzplmwsswlnvrvwwcgddjwcmvsjjbfgcfjmthfbpmcwjptchhnsmzttjqnwzdljffghhqdcwzwgbvfsmwqdbtblphdgcmbhprtbccjbzqrpvjdbnsmlwfntvjgptnshzmddwbhgwsnfrjbpqqwlsfdpnmmnnwhdmhzvjcmddbdnjzfzvffbgdqgwbggprcrbzwhvtzzgbhhcscrlmfgztfswjbsnwsmdfwlntwjzvlwhvlrfzszllmflmrsrcfnncvszvgdmmnvgrqnjhljcnrrhpdhffwmrsqfnbcpfdmmgppwjbjrwdfmpcrbznrjnbmssszhnlbpgmlczhhcdtgjqcbqrvzcbpgrftfhzdqthhspwnqqswntlpcmmqtcszngpggqvfjnmnprhdfjsngwrncjcmqdmjhpdlfnshpdlnlfpcnprwjgdvwwbvhvsbrfjtqsqjnvcpfdsrnfwmrrbtcvcqzflhdlbpcthzthdjzsrvwgbhjvhbtrngthfrszlvrbtnscsqlblcwlngslspcrhqzzdlzcdbhqdhthlpmdrntbhnqtwtzwpndbgphpsllbvgqjtmszdvjpgttzcmbwgrgdwmsbfgvgbbcmsnhvmnsbcsthsdwdqtghpdclfbglbdjgnnnwhmzzvnhbmgfbmvqwvwqhdswgtzslspmbmznnwdmjbzbddhzchtdzdzgwtlmlpmwrqvghpfwhvfjrtvmjwgjjnwdwnpdcqjdmcctjfcrdgpvczvnhlrbfmqgnrhmdwsrmmpqhvwgqgbqccpznpjfldwpntnvzgdfzljmtqwvfnrdsjsqgbvzjsczwwjggqtrpvwgqggwwhqggtgqfjmzsmjvdhdwqggbgnftpqqqlsfpflwrdpjwnhfdpchcgntjshgtwnwrnpsvwmplvqcltbgrcpflpgzbqfclghfnwjchnbgjnplgldmphdplvjrnrtzcmlftprsnmrjmnffpqjlvqlztbwprjwprrmmgzhjgdnhbfdrwjtvsvnbqhtfhbqgdrvcwlwfdbbcthgvttpvrwrqmpmrmvgpjzwlpvbqcvgccpgfddjbwhrvgqmjqzwgghllrtrblcpbttmcrgjsftlqhjfvqnbhmhbhngwnfqtgdttstzvmstrqcpfjrdgtsdbqqccqvbhpwhnpmpqgntfqszndjrmfhlqjqjbqvjtlfmrnnzzrtqlzzhjfqmmsmzvzrcplmfjpcmpfmpzbsbrmbnbnjqwjcfwnnwwrwzvvrsvhvrwnhlmwqjdztqthcwnwrlbjdflfsbplbwfmzqqnpwvzjbcfdgztpwttlrvhlfzzsfltpqwcpnzlsqgvwnqfvgclrfvssfcfmfvvjsndrhqdbrqfggfhjbvdvvmgpglqzwgjdmqtscjpfhgsbshghtmftrrhznttpzrzcsmrrvzdjmwtmbcbpqbsdmqzqdzrncwzmptltvdphsltfrhbbrdzbnbsqdhfvrgvmbgfvwblsjvfphlpzfvsllwnqjmbhngzzslcdmdzfgrgscbzggrzmbmwlzbnpzcvsbsfgdpnwzljsf";
}
