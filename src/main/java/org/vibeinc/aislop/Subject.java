package org.vibeinc.aislop;

import java.time.LocalDateTime;
import java.util.List;

public interface Subject {
    String getName();

    List<LocalDateTime> getClassTimes();

    String getTeacher();
}
