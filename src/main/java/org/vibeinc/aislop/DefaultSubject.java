package org.vibeinc.aislop;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

public final class DefaultSubject implements Subject {
    private final String name;
    private final String teacher;
    private final Schedule schedule;

    public DefaultSubject(String name, String teacher, Schedule schedule) {
        this.name = ValidationUtils.requireText(name);
        this.teacher = ValidationUtils.requireText(teacher);
        this.schedule = Objects.requireNonNull(schedule);
    }

    @Override public String getName() { return name; }
    @Override public String getTeacher() { return teacher; }

    @Override
    public List<LocalDateTime> getClassTimes() {
        return schedule.getClasses().entrySet().stream()
                .filter(entry -> entry.getValue() == this)
                .map(Entry::getKey).sorted().toList();
    }
}
