package org.example.fe;

import com.itextpdf.layout.element.Table;
import lombok.Data;

import java.util.List;

@Data
public class WeekCalFE {
    Table title;
    List<Table> weeks;
}
