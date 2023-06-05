package org.example.fe;

import com.itextpdf.layout.element.Cell;
import lombok.Data;

import java.util.List;

@Data
public class WeekFE {

    Cell month;
    Cell montHeb;
    List<DayFE> days;
}
