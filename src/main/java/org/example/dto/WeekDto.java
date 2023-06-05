package org.example.dto;

import lombok.Data;

import java.util.List;

@Data
public class WeekDto {
    private String month;
    private String monthHeb;
    List<DayDto>dayDtos;

}
