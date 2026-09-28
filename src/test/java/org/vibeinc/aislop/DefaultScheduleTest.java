package org.vibeinc.aislop;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultScheduleTest {
    @Test
    void addClassStoresClassByStartTime() {
        DefaultSchedule schedule = new DefaultSchedule();
        Subject subject = new DefaultSubject("Mathematics", "Ada Lovelace", schedule);
        LocalDateTime startTime = LocalDateTime.of(2026, 9, 28, 9, 0);

        schedule.addClass(subject, startTime);

        assertEquals(Map.of(startTime, subject), schedule.getClasses());
    }

    @Test
    void addClassRejectsNullSubject() {
        DefaultSchedule schedule = new DefaultSchedule();
        LocalDateTime startTime = LocalDateTime.of(2026, 9, 28, 9, 0);

        assertThrows(NullPointerException.class, () -> schedule.addClass(null, startTime));
    }

    @Test
    void addClassRejectsNullStartTime() {
        DefaultSchedule schedule = new DefaultSchedule();
        Subject subject = new DefaultSubject("Mathematics", "Ada Lovelace", schedule);

        assertThrows(NullPointerException.class, () -> schedule.addClass(subject, null));
    }

    @Test
    void addClassRejectsOccupiedStartTime() {
        DefaultSchedule schedule = new DefaultSchedule();
        Subject firstSubject = new DefaultSubject("Mathematics", "Ada Lovelace", schedule);
        Subject secondSubject = new DefaultSubject("Physics", "Isaac Newton", schedule);
        LocalDateTime startTime = LocalDateTime.of(2026, 9, 28, 9, 0);
        schedule.addClass(firstSubject, startTime);

        assertThrows(IllegalArgumentException.class, () -> schedule.addClass(secondSubject, startTime));
    }

    @Test
    void getClassesReturnsUnmodifiableSnapshot() {
        DefaultSchedule schedule = new DefaultSchedule();
        Subject subject = new DefaultSubject("Mathematics", "Ada Lovelace", schedule);
        LocalDateTime startTime = LocalDateTime.of(2026, 9, 28, 9, 0);
        schedule.addClass(subject, startTime);
        Map<LocalDateTime, Subject> classes = schedule.getClasses();

        assertThrows(UnsupportedOperationException.class, () -> classes.clear());

        schedule.rescheduleClass(startTime, startTime.plusHours(1));
        assertTrue(classes.containsKey(startTime));
    }

    @Test
    void rescheduleClassMovesClassToNewStartTime() {
        DefaultSchedule schedule = new DefaultSchedule();
        Subject subject = new DefaultSubject("Mathematics", "Ada Lovelace", schedule);
        LocalDateTime currentStartTime = LocalDateTime.of(2026, 9, 28, 9, 0);
        LocalDateTime newStartTime = currentStartTime.plusHours(1);
        schedule.addClass(subject, currentStartTime);

        schedule.rescheduleClass(currentStartTime, newStartTime);

        assertEquals(Map.of(newStartTime, subject), schedule.getClasses());
    }

    @Test
    void rescheduleClassRejectsMissingClass() {
        DefaultSchedule schedule = new DefaultSchedule();
        LocalDateTime currentStartTime = LocalDateTime.of(2026, 9, 28, 9, 0);

        assertThrows(
                java.util.NoSuchElementException.class,
                () -> schedule.rescheduleClass(currentStartTime, currentStartTime.plusHours(1))
        );
    }

    @Test
    void rescheduleClassRejectsOccupiedNewStartTime() {
        DefaultSchedule schedule = new DefaultSchedule();
        Subject firstSubject = new DefaultSubject("Mathematics", "Ada Lovelace", schedule);
        Subject secondSubject = new DefaultSubject("Physics", "Isaac Newton", schedule);
        LocalDateTime firstStartTime = LocalDateTime.of(2026, 9, 28, 9, 0);
        LocalDateTime secondStartTime = firstStartTime.plusHours(1);
        schedule.addClass(firstSubject, firstStartTime);
        schedule.addClass(secondSubject, secondStartTime);

        assertThrows(
                IllegalArgumentException.class,
                () -> schedule.rescheduleClass(firstStartTime, secondStartTime)
        );
    }

    @Test
    void rescheduleClassKeepsClassWhenStartTimeIsUnchanged() {
        DefaultSchedule schedule = new DefaultSchedule();
        Subject subject = new DefaultSubject("Mathematics", "Ada Lovelace", schedule);
        LocalDateTime startTime = LocalDateTime.of(2026, 9, 28, 9, 0);
        schedule.addClass(subject, startTime);

        schedule.rescheduleClass(startTime, startTime);

        assertEquals(Map.of(startTime, subject), schedule.getClasses());
    }
}
