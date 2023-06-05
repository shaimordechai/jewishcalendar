package org.example.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DayDto {
    Date date;
    String dayInWeek;
    String dayInWeekHeb;
    String dayInMonth;
    String dayInMonthHeb;
    String sunrise;
    String sunset;
}
