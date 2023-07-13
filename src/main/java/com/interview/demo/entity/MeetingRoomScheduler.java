package com.interview.demo.entity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MeetingRoomScheduler {
    private List<TimeSlot> bookedTimeSlots;

    public MeetingRoomScheduler() {
        // 初始化已预订的时间段
        bookedTimeSlots = new ArrayList<>();
        // 假设已经有部分时间被预订
        bookedTimeSlots.add(new TimeSlot(LocalTime.of(10, 0), LocalTime.of(11, 0)));
        bookedTimeSlots.add(new TimeSlot(LocalTime.of(13, 0), LocalTime.of(14, 0)));
        bookedTimeSlots.add(new TimeSlot(LocalTime.of(16, 0), LocalTime.of(17, 0)));
    }

    public List<TimeSlot> getAvailableTimeSlots(LocalTime startTime, LocalTime endTime) {
        List<TimeSlot> availableTimeSlots = new ArrayList<>();

        // 检查时间范围内的可用时间
        LocalTime currentTime = startTime;
        while (currentTime.isBefore(endTime)) {
            LocalTime nextTime = currentTime.plusHours(1); // 为一小时一个区间
            TimeSlot timeSlot = new TimeSlot(currentTime, nextTime);

            // 检查是否与已预订的时间段重叠
            boolean isBooked = false;
            for (TimeSlot bookedSlot : bookedTimeSlots) {
                if (timeSlot.overlaps(bookedSlot)) {
                    isBooked = true;
                    break;
                }
            }

            // 如果未被预订，则加入可用时间列表
            if (!isBooked) {
                availableTimeSlots.add(timeSlot);
            }

            currentTime = nextTime;
        }

        return availableTimeSlots;
    }

    public static void main(String[] args) {
        MeetingRoomScheduler scheduler = new MeetingRoomScheduler();
        List<TimeSlot> availableSlots = scheduler.getAvailableTimeSlots(
                LocalTime.of(9, 0), LocalTime.of(18, 0));

        System.out.println("Available Time Slots:");
        for (TimeSlot slot : availableSlots) {
            System.out.println(slot);
        }
    }

    public List<TimeSlot> getBookedTimeSlots() {
        return bookedTimeSlots;
    }
}