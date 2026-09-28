package org.vibeinc.aislop;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.TreeMap;

public final class DefaultSchedule implements Schedule {
    private final Map<LocalDateTime, Subject> classes = new TreeMap<>();

    @Override
    public void addClass(Subject subject, LocalDateTime startTime) {
        Objects.requireNonNull(subject, "Предмет не задано");
        Objects.requireNonNull(startTime, "Час не задано");
        if (classes.containsKey(startTime)) {
            throw new IllegalArgumentException("На цей час уже заплановано заняття");
        }
        classes.put(startTime, subject);
    }

    @Override
    public Map<LocalDateTime, Subject> getClasses() {
        return Collections.unmodifiableMap(new TreeMap<>(classes));
    }

    @Override
    public void rescheduleClass(LocalDateTime currentStartTime, LocalDateTime newStartTime) {
        Objects.requireNonNull(currentStartTime, "Поточний час не задано");
        Objects.requireNonNull(newStartTime, "Новий час не задано");
        if (!classes.containsKey(currentStartTime)) {
            throw new NoSuchElementException("Заняття не знайдено");
        }
        if (currentStartTime.equals(newStartTime)) return;
        if (classes.containsKey(newStartTime)) {
            throw new IllegalArgumentException("На новий час уже заплановано заняття");
        }
        Subject subject = classes.remove(currentStartTime);
        classes.put(newStartTime, subject);
    }
}
