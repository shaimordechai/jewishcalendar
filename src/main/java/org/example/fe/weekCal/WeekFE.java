package org.example.fe.weekCal;

import com.itextpdf.layout.element.Cell;
import lombok.Data;

import java.util.List;

@Data
public class WeekFE {

    Cell month;
    Cell monthHeb;
    List<DayFE> days;
    Cell additionalData;
}
