package org.vibeinc.aislop;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultSubjectTest {
    @Test
    void constructorStoresNameAndTeacher() {
        DefaultSchedule schedule = new DefaultSchedule();

        DefaultSubject subject = new DefaultSubject("Mathematics", "Ada Lovelace", schedule);

        assertEquals("Mathematics", subject.getName());
        assertEquals("Ada Lovelace", subject.getTeacher());
    }

    @Test
    void constructorRejectsInvalidTextAndNullSchedule() {
        DefaultSchedule schedule = new DefaultSchedule();

        assertThrows(IllegalArgumentException.class, () -> new DefaultSubject(" ", "Teacher", schedule));
        assertThrows(IllegalArgumentException.class, () -> new DefaultSubject("Subject", "\t", schedule));
        assertThrows(NullPointerException.class, () -> new DefaultSubject("Subject", "Teacher", null));
    }

    @Test
    void getClassTimesReturnsThisSubjectsTimesInChronologicalOrder() {
        DefaultSchedule schedule = new DefaultSchedule();
        DefaultSubject subject = new DefaultSubject("Mathematics", "Ada Lovelace", schedule);
        DefaultSubject otherSubject = new DefaultSubject("Physics", "Isaac Newton", schedule);
        LocalDateTime firstTime = LocalDateTime.of(2026, 9, 29, 9, 0);
        LocalDateTime secondTime = firstTime.plusDays(1);
        schedule.addClass(subject, secondTime);
        schedule.addClass(otherSubject, firstTime);
        schedule.addClass(subject, firstTime.plusDays(2));

        List<LocalDateTime> classTimes = subject.getClassTimes();

        assertEquals(List.of(secondTime, firstTime.plusDays(2)), classTimes);
    }

    @Test
    void getClassTimesReturnsEmptyListWhenSubjectHasNoClasses() {
        DefaultSchedule schedule = new DefaultSchedule();
        DefaultSubject subject = new DefaultSubject("Mathematics", "Ada Lovelace", schedule);

        assertEquals(List.of(), subject.getClassTimes());
    }
}
