package org.vibeinc.aislop;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultStudentTest {
    @Test
    void constructorStoresNameAndRejectsInvalidArguments() {
        DefaultSchedule schedule = new DefaultSchedule();

        DefaultStudent student = new DefaultStudent("Grace Hopper", schedule);

        assertEquals("Grace Hopper", student.getName());
        assertThrows(IllegalArgumentException.class, () -> new DefaultStudent(" ", schedule));
        assertThrows(NullPointerException.class, () -> new DefaultStudent("Grace Hopper", null));
    }

    @Test
    void registerSubjectAddsSubjectAndRejectsDuplicates() {
        DefaultSchedule schedule = new DefaultSchedule();
        DefaultStudent student = new DefaultStudent("Grace Hopper", schedule);
        Subject subject = new DefaultSubject("Mathematics", "Ada Lovelace", schedule);

        student.registerSubject(subject);

        assertThrows(IllegalArgumentException.class, () -> student.registerSubject(subject));
        assertThrows(NullPointerException.class, () -> student.registerSubject(null));
    }

    @Test
    void addGradeStoresGradesForRegisteredSubject() {
        DefaultSchedule schedule = new DefaultSchedule();
        DefaultStudent student = new DefaultStudent("Grace Hopper", schedule);
        Subject subject = new DefaultSubject("Mathematics", "Ada Lovelace", schedule);
        student.registerSubject(subject);

        student.addGrade(subject, 0);
        student.addGrade(subject, 100);

        assertEquals(Map.of(subject, List.of(0, 100)), student.getGrades());
    }

    @Test
    void addGradeRejectsUnregisteredSubjectAndOutOfRangeGrade() {
        DefaultSchedule schedule = new DefaultSchedule();
        DefaultStudent student = new DefaultStudent("Grace Hopper", schedule);
        Subject subject = new DefaultSubject("Mathematics", "Ada Lovelace", schedule);

        assertThrows(java.util.NoSuchElementException.class, () -> student.addGrade(subject, 80));
        student.registerSubject(subject);
        assertThrows(IllegalArgumentException.class, () -> student.addGrade(subject, -1));
        assertThrows(IllegalArgumentException.class, () -> student.addGrade(subject, 101));
    }

    @Test
    void getGradesReturnsUnmodifiableDeepCopy() {
        DefaultSchedule schedule = new DefaultSchedule();
        DefaultStudent student = new DefaultStudent("Grace Hopper", schedule);
        Subject subject = new DefaultSubject("Mathematics", "Ada Lovelace", schedule);
        student.registerSubject(subject);
        student.addGrade(subject, 95);
        Map<Subject, List<Integer>> grades = student.getGrades();

        assertThrows(UnsupportedOperationException.class, () -> grades.clear());
        assertThrows(UnsupportedOperationException.class, () -> grades.get(subject).add(100));
        student.addGrade(subject, 100);
        assertEquals(List.of(95), grades.get(subject));
        assertEquals(List.of(95, 100), student.getGrades().get(subject));
    }

    @Test
    void getScheduleContainsOnlyRegisteredSubjectsInChronologicalOrder() {
        DefaultSchedule schedule = new DefaultSchedule();
        DefaultStudent student = new DefaultStudent("Grace Hopper", schedule);
        DefaultSubject registeredSubject = new DefaultSubject("Mathematics", "Ada Lovelace", schedule);
        DefaultSubject unregisteredSubject = new DefaultSubject("Physics", "Isaac Newton", schedule);
        LocalDateTime laterTime = LocalDateTime.of(2026, 9, 30, 12, 0);
        LocalDateTime earlierTime = laterTime.minusHours(2);
        schedule.addClass(registeredSubject, laterTime);
        schedule.addClass(unregisteredSubject, earlierTime);
        student.registerSubject(registeredSubject);

        Schedule studentSchedule = student.getSchedule();

        assertEquals(Map.of(laterTime, registeredSubject), studentSchedule.getClasses());
    }

    @Test
    void studentScheduleCannotBeChanged() {
        DefaultSchedule schedule = new DefaultSchedule();
        DefaultStudent student = new DefaultStudent("Grace Hopper", schedule);
        Subject subject = new DefaultSubject("Mathematics", "Ada Lovelace", schedule);
        LocalDateTime startTime = LocalDateTime.of(2026, 9, 30, 9, 0);
        schedule.addClass(subject, startTime);
        student.registerSubject(subject);

        Schedule studentSchedule = student.getSchedule();

        assertThrows(UnsupportedOperationException.class, () -> studentSchedule.addClass(subject, startTime.plusHours(1)));
        assertThrows(
                UnsupportedOperationException.class,
                () -> studentSchedule.rescheduleClass(startTime, startTime.plusHours(1))
        );
        assertThrows(UnsupportedOperationException.class, () -> studentSchedule.getClasses().clear());
    }
}
