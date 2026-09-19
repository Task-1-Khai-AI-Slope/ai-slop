package org.vibeinc.aislop;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public interface Student {
    String getName();

    void registerSubject(Subject subject);

    Schedule getSchedule();

    Map<Subject, List<Integer>> getGrades();
}
