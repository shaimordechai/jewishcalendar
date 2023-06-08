package org.example.dto;

import lombok.Data;

import java.util.List;

@Data
public class WeekBE {
    String month;
    String monthHeb;
    String year;
    String yearHeb;
    List<DayBE> days;
    List<String> additionalData;
}
