package de.alexsteinh.data;

import java.util.Objects;

public class Grade {
    private final String name;
    private final int code;

    public Grade(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "name='" + name + '\'' +
                ", code=" + code +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade grade = (Grade) o;
        return code == grade.code &&
                Objects.equals(name, grade.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }
}
