package org.example.fe.weekCal;

import com.itextpdf.layout.element.Cell;
import lombok.Data;

@Data
public class DayFE {
    Cell day;
    Cell dayTimes;
    Cell dayData;
    Cell dayEvents;
    Cell dayHeb;
}
