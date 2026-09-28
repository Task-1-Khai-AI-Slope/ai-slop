package org.vibeinc.aislop;

import java.util.List;
import java.util.Map;

public interface Student {
    String getName();

    void registerSubject(Subject subject);

    Schedule getSchedule();

    Map<Subject, List<Integer>> getGrades();

    void addGrade(Subject subject, int grade);
}
