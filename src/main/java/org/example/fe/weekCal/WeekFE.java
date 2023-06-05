package org.example.fe.weekCal;

import com.itextpdf.layout.element.Cell;
import lombok.Data;
import org.example.fe.weekCal.DayFE;

import java.util.List;

@Data
public class WeekFE {

    Cell month;
    Cell montHeb;
    List<DayFE> days;
}
