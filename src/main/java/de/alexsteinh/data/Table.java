package de.alexsteinh.data;

import de.alexsteinh.parser.TableParser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class Table {
    private final ReentrantReadWriteLock lock;
    private final Lock rLock, wLock;
    private final Grade grade;
    private final List<Replacement> replacements;
    private final List<Message> messages;

    public Table(Grade grade) {
        this.grade = grade;
        replacements = new ArrayList<>();
        messages = new ArrayList<>();
        lock = new ReentrantReadWriteLock();
        rLock = lock.readLock();
        wLock = lock.writeLock();
    }

    public void update() {
        try {
            wLock.lock();
            //int week = Utils.getCurrentWeek();
            int week = 12;
            replacements.clear();
            messages.clear();

            for (int i = 0; i < 2; i++) {
                var parser = new TableParser(grade, week + i);
                replacements.addAll(parser.getReplacements());
                messages.addAll(parser.getMessages());
            }
        } finally {
            wLock.unlock();
        }
    }

    public Grade getGrade() {
        return grade;
    }

    public Map<LocalDate, List<Replacement>> getReplacements() {
        try {
            rLock.lock();
            return replacements.stream().collect(Collectors.groupingBy(Replacement::getDate));
        } finally {
            rLock.unlock();
        }
    }

    public Map<LocalDate, List<Message>> getMessages() {
        try {
            rLock.lock();
            return messages.stream().collect(Collectors.groupingBy(Message::getDate));
        } finally {
            rLock.unlock();
        }
    }
}
