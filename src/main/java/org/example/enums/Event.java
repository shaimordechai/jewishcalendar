package org.example.enums;

import java.util.Arrays;

public enum Event {
    BIRTHDAY("יום הולדת"),
    WEDDINGDAY("יום נישואין");
    private String name;
    Event(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Event fromName(String name) {
        return Arrays.stream(values())
                .filter(r -> r.name.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
