package org.example.utils;

import com.ibm.icu.text.Bidi;
import org.example.dto.EventBE;
import org.example.enums.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TextUtils {
    private static final String SPACE = " ";
    private static final String BIRTHDAY = "יום הולדת";
    private static final String WEDDINGDAY = "יום נישואין";
    private static final int MAX_CHAR_IN_LINE = 20;
    private static final String TO = "ל";
    private static final int MAX_LINES_IN_EVENT = 2;

    public static String setDirectionRtl(String text) {
        Bidi bidi = new Bidi(text, Bidi.RTL);
        return bidi.writeReordered(Bidi.DO_MIRRORING);
    }

    public static String createEventText(EventBE eventBE, int years) {
        String fullString = eventBE.getEvent().getName() + SPACE + years + SPACE + TO + eventBE.getName() + SPACE;
        List<String> lines = new ArrayList<>();
        while (!fullString.isEmpty()) {
            int to = fullString.lastIndexOf(" ", MAX_CHAR_IN_LINE);
            if (to == -1) {
                to = Math.min(fullString.length(), MAX_CHAR_IN_LINE);
            }
            lines.add(fullString.substring(0, to));
            fullString = (fullString.length() >= (to + 1)
                    && !Character.isWhitespace(fullString.charAt(0)))
                    ?  fullString.substring(to + 1) : fullString.substring(to);
        }
        return lines.stream()
                .limit(MAX_LINES_IN_EVENT)
                .map(line -> new Bidi(line, Bidi.RTL).writeReordered(Bidi.DO_MIRRORING))
                .collect(Collectors.joining("\n"));

    }
}
