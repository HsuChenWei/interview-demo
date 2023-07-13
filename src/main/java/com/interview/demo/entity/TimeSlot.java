package com.interview.demo.entity;

import java.time.LocalTime;

public class TimeSlot {
    private LocalTime startTime;
    private LocalTime endTime;

    public TimeSlot(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public boolean overlaps(TimeSlot other) {
        return this.startTime.isBefore(other.endTime) && this.endTime.isAfter(other.startTime);
    }

    @Override
    public String toString() {
        return startTime + " - " + endTime;
    }
}
