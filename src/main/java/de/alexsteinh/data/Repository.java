package de.alexsteinh.data;

import de.alexsteinh.parser.GradeParser;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Repository {
    private final Map<Grade, Table> tables;
    private final Map<String, Grade> grades;
    private final Thread updateThread;
    private final static long UPDATE_FREQ = 1000L * 60L * 5L;
    private OnUpdateCompletedListener listener = null;

    public Repository() {
        tables = new LinkedHashMap<>();
        grades = new LinkedHashMap<>();
        updateThread = new Thread(() -> {
            update();

            try {
                Thread.sleep(UPDATE_FREQ);
            } catch (InterruptedException e) {
                throw new RuntimeException("Can't update anymore...");
            }
        });

        populateMaps();
    }

    public void start() {
        updateThread.start();
    }

    public void update() {
        getTables().forEach(Table::update);
        if (listener != null) {
            listener.onUpdateCompleted();
        }
    }

    private void populateMaps() {
        List<Grade> grades = new GradeParser().getGrades();
        grades.forEach(grade -> {
            this.grades.put(grade.getName(), grade);
            tables.put(grade, new Table(grade));
        });
    }

    public Table getTable(Grade grade) {
        return tables.get(grade);
    }

    public Grade getGrade(String name) {
        return grades.get(name);
    }

    public Collection<Grade> getGrades() {
        return grades.values();
    }

    public Collection<Table> getTables() {
        return tables.values();
    }

    public void setOnUpdateCompletedListener(OnUpdateCompletedListener listener) {
        this.listener = listener;
    }

    @FunctionalInterface
    public interface OnUpdateCompletedListener {
        void onUpdateCompleted();
    }
}
