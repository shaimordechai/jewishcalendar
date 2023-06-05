package org.example.fe.weekCal;

import com.itextpdf.layout.element.Table;
import lombok.Data;

import java.util.List;

@Data
public class CalFE {
    Table title;
    List<Table> weeks;
}
