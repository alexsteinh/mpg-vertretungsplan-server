package de.alexsteinh.parser;

import de.alexsteinh.Utils;
import de.alexsteinh.data.Grade;
import de.alexsteinh.data.Message;
import de.alexsteinh.data.Replacement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TableParser {
    private final static String FRAME_URL =  "http://mpg-vertretungsplan.de/w/%02d/w000%02d.htm";

    protected final Document doc;

    public TableParser(Grade grade, int week) {
        Document doc1;
        try {
            var response = Utils.downloadString(String.format(FRAME_URL, week, grade.getCode()));
            doc1 = Jsoup.parse(processHtml(response.lines()));
        } catch (Exception e) {
            doc1 = null;
        }

        doc = doc1;
    }

    private String processHtml(Stream<String> lines) {
        final StringBuilder html = new StringBuilder();
        Pattern groupPattern = Pattern.compile("<b>\\d{1,2}\\.\\d{1,2}\\. .+</b>");
        Pattern datePattern = Pattern.compile("\\d{1,2}\\.\\d{1,2}");
        String date = null;

        for (Iterator<String> it = lines.iterator(); it.hasNext(); ) {
            String line = it.next();
            Matcher groupMatcher = groupPattern.matcher(line);
            if (groupMatcher.find()) {
                String group = groupMatcher.group();
                if (date != null) {
                    html.append("</div>");
                }

                html.append(line);
                Matcher dateMatcher = datePattern.matcher(group);
                dateMatcher.find();
                date = dateMatcher.group();
                html.append("<div date=\"").append(date).append("\">");
            } else {
                html.append(line);
            }
        }

        return html.toString();
    }

    private Elements getDateDivs() {
        return doc.select("div[date]");
    }

    public List<Replacement> getReplacements() {
        if (doc == null) {
            return Collections.emptyList();
        }

        return doc.select("tr[class~=list (?:odd|even)]").stream()
                .map(this::replacementFromHtml)
                .collect(Collectors.toUnmodifiableList());
    }

    private Replacement replacementFromHtml(Element element) {
        Elements data = element.children();
        return new Replacement(
                Utils.toLocalDate(data.get(0).text()),
                data.get(2).text(),
                data.get(3).text(),
                data.get(4).text(),
                data.get(5).text(),
                data.get(6).text()
        );
    }

    public List<Message> getMessages() {
        if (doc == null) {
            return Collections.emptyList();
        }

        List<Message> messages = new ArrayList<>();

        for (Element div : getDateDivs()) {
            LocalDate date = Utils.toLocalDate(div.attr("date"));
            try {
                String text = div.select("tbody > tr > th:matches(Nachrichten zum Tag)")
                        .first().parent().parent().select("tr:gt(0)").text();
                messages.add(new Message(date, text));
            } catch (Exception e) {
                // Es gab keine Nachricht zum Tag
            }
        }

        return Collections.unmodifiableList(messages);
    }
}
