package org.example.fe;

import com.itextpdf.layout.element.Cell;
import lombok.Data;

@Data
public class DayFE {
    Cell dayInWeek;
    Cell dayInWeekHeb;
    Cell dayInMonth;
    Cell dayInMonthHeb;
    Cell sunrise;
    Cell sunset;
    Cell other;
}
