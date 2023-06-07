package org.example.dto;

import lombok.Data;
import org.example.enums.Event;

import java.util.Date;

@Data
public class EventBE {
    Date date;
    String name;
    String path;
    Event event;

}
