package org.example.dto;

import lombok.Data;

import java.util.List;

@Data
public class WeekBE {
    String month;
    String monthHeb;
    List<DayBE> days;
}
