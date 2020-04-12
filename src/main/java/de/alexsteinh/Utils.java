package de.alexsteinh;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.WeekFields;

public class Utils {
    public static String downloadString(String url) throws Exception {
        var uri = new URI(url);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(uri).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)).body();
    }

    public static LocalDate toLocalDate(String date) {
        String[] data = date.split("\\.");
        if (data.length == 2 || (data.length == 3 && data[2].isBlank())) {
            return fromDateWithYear(date + (!date.endsWith(".") ? "." : "") + getYear(Integer.parseInt(data[1])));
        } else {
            return fromDateWithYear(date);
        }
    }

    public static int getYear(int month) {
        var now = LocalDate.now();
        if (now.getMonth().equals(Month.JANUARY) && Month.of(month).equals(Month.DECEMBER)) {
            return now.getYear() - 1;
        } else {
            return now.getYear();
        }
    }

    public static int getCurrentWeek() {
        var now = LocalDate.now();
        return now.get(WeekFields.ISO.weekOfWeekBasedYear());
    }

    private static LocalDate fromDateWithYear(String date) {
        String[] data = date.split("\\.");
        return LocalDate.of(
                Integer.parseInt(data[2]),
                Integer.parseInt(data[1]),
                Integer.parseInt(data[0])
        );
    }
}
