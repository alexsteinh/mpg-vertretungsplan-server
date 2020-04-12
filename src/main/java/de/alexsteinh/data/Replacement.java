package de.alexsteinh.data;

import java.time.LocalDate;
import java.util.Objects;

public class Replacement {
    private final LocalDate date;
    private final String period, newSubject, room, oldSubject, text;

    public Replacement(LocalDate date, String period, String newSubject, String room,
                        String oldSubject, String text) {
        this.date = date;
        this.period = period;
        this.newSubject = newSubject;
        this.room = room;
        this.oldSubject = oldSubject;
        this.text = text;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getPeriod() {
        return period;
    }

    public String getNewSubject() {
        return newSubject;
    }

    public String getRoom() {
        return room;
    }

    public String getOldSubject() {
        return oldSubject;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Replacement{" +
                "date=" + date +
                ", period='" + period + '\'' +
                ", newSubject='" + newSubject + '\'' +
                ", room='" + room + '\'' +
                ", oldSubject='" + oldSubject + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Replacement that = (Replacement) o;
        return Objects.equals(date, that.date) &&
                Objects.equals(period, that.period) &&
                Objects.equals(newSubject, that.newSubject) &&
                Objects.equals(room, that.room) &&
                Objects.equals(oldSubject, that.oldSubject) &&
                Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, period, newSubject, room, oldSubject, text);
    }
}
