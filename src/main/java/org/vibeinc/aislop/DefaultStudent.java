package org.vibeinc.aislop;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

public final class DefaultStudent implements Student {
    private final String name;
    private final Schedule schedule;
    private final Set<Subject> registeredSubjects = new LinkedHashSet<>();
    private final Map<Subject, List<Integer>> grades = new LinkedHashMap<>();

    public DefaultStudent(String name, Schedule schedule) {
        this.name = ValidationUtils.requireText(name);
        this.schedule = Objects.requireNonNull(schedule);
    }

    @Override public String getName() { return name; }

    @Override
    public void registerSubject(Subject subject) {
        Objects.requireNonNull(subject, "Предмет не задано");
        if (!registeredSubjects.add(subject)) {
            throw new IllegalArgumentException("Студент уже зареєстрований на цей предмет");
        }
    }

    @Override
    public void addGrade(Subject subject, int grade) {
        if (!registeredSubjects.contains(subject)) {
            throw new NoSuchElementException("Студент не зареєстрований на предмет");
        }
        if (grade < 0 || grade > 100) {
            throw new IllegalArgumentException("Оцінка має бути від 0 до 100");
        }
        grades.computeIfAbsent(subject, ignored -> new ArrayList<>()).add(grade);
    }

    @Override
    public Map<Subject, List<Integer>> getGrades() {
        Map<Subject, List<Integer>> copy = new LinkedHashMap<>();
        grades.forEach((subject, values) -> copy.put(subject, List.copyOf(values)));
        return Collections.unmodifiableMap(copy);
    }

    @Override
    public Schedule getSchedule() {
        return new Schedule() {
            @Override public Map<LocalDateTime, Subject> getClasses() {
                Map<LocalDateTime, Subject> result = new TreeMap<>();
                schedule.getClasses().forEach((time, subject) -> {
                    if (registeredSubjects.contains(subject)) result.put(time, subject);
                });
                return Collections.unmodifiableMap(result);
            }
            @Override public void addClass(Subject subject, LocalDateTime time) {
                throw new UnsupportedOperationException("Змінюйте загальний розклад");
            }
            @Override public void rescheduleClass(LocalDateTime from, LocalDateTime to) {
                throw new UnsupportedOperationException("Змінюйте загальний розклад");
            }
        };
    }
}
