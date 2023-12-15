package main.tasks;

import java.util.Locale;

public enum Status {
    NEW,
    IN_PROGRESS,
    DONE;

    public static Status statusFromString(String statusString) {
        switch (statusString.toUpperCase()) {
            case "NEW":
                return NEW;
            case "DONE":
                return DONE;
            case "IN_PROGRESS":
                return IN_PROGRESS;
            default:
                throw new NullPointerException("Не найден статус:" + statusString);
        }
    }
}
