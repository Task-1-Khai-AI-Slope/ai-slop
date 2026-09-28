package org.vibeinc.aislop;

final class ValidationUtils {
    static String requireText(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Текст не може бути порожнім");
        }
        return value.strip();
    }
}
