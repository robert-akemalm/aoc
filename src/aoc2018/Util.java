package aoc2018;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

class Util {
    private Util() {
    }

    static String[] parseStrings(String input) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new StringReader(input))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lines.toArray(new String[0]);

    }

}
