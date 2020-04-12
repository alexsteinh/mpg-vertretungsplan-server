package de.alexsteinh.data;

import java.time.LocalDate;
import java.util.Objects;

public class Message {
    private final LocalDate date;
    private final String text;

    public Message(LocalDate date, String text) {
        this.date = date;
        this.text = text;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "date=" + date +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(date, message.date) &&
                Objects.equals(text, message.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, text);
    }
}
