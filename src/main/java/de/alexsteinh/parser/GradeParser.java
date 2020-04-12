package de.alexsteinh.parser;

import de.alexsteinh.Utils;
import de.alexsteinh.data.Grade;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GradeParser {
    private final static String NAVBAR_URL = "http://mpg-vertretungsplan.de/frames/navbar.htm";

    protected final Document doc;

    public GradeParser() {
        Document doc1;
        try {
            doc1 = Jsoup.parse(Utils.downloadString(NAVBAR_URL));
        } catch (Exception e) {
            doc1 = null;
        }

        doc = doc1;
    }

    public List<Grade> getGrades() {
        if (doc == null) {
            return Collections.emptyList();
        }

        var js = doc.select("script").get(1).html().lines();
        var grades = js.filter(line -> line.matches(".*var classes = \\[.+")).findFirst();
        return grades.isPresent() ? extractGrades(grades.get()) : Collections.emptyList();
    }

    private List<Grade> extractGrades(String js) {
        var start = js.indexOf("[") + 1;
        var end = js.indexOf("]");
        var list = js.substring(start, end);
        var grades = list.replaceAll("\"", "").replaceAll("Klasse ", "").replaceAll("JG", "1").split(",");
        return IntStream.range(0, grades.length)
                .mapToObj(i -> new Grade(grades[i], i + 1))
                .collect(Collectors.toUnmodifiableList());
    }
}
