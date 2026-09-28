package org.vibeinc.aislop;

import java.time.LocalDateTime;
import java.util.Map;

public interface Schedule {
    void addClass(Subject subject, LocalDateTime startTime);

    Map<LocalDateTime, Subject> getClasses();

    void rescheduleClass(LocalDateTime currentStartTime, LocalDateTime newStartTime);
}
