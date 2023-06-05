package org.example.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DayBE {
    Date date;
    String year;
    String yearHeb;
    String month;
    String monthHeb;
    String dayInWeek;
    String dayInWeekHeb;
    String dayInMonth;
    String dayInMonthHeb;
    String sunrise;
    String sunset;
    String parsha;
    boolean isShabath;
    boolean isYomTov;
    String roshChodesh;
    String yomTovName;
    String candleLighting;
    String tzaisSabath;
    String tzaisYomTov;

}
