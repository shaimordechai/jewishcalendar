package org.example.dto;

import lombok.Data;

import java.util.List;

@Data
public class WeekCalBE {
    String title;
    List<WeekBE> weeks;
}
