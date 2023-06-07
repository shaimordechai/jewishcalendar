package org.example.utils;

import com.ibm.icu.text.Bidi;
import org.example.dto.EventBE;
import org.example.enums.Event;

public class TextUtils {
    private static final String SPACE = " ";
    private static final String BIRTHDAY = "יום הולדת";
    private static final String WEDDINGDAY = "יום נישואין";
    private static final String TO = "ל";
    public static String setDirectionRtl(String text) {
        Bidi bidi = new Bidi(text, Bidi.RTL);
        return bidi.writeReordered(Bidi.DO_MIRRORING);
    }

    public static String createEventText(EventBE eventBE, int years){
        if(eventBE.getEvent() == Event.BIRTHDAY){
            Bidi bidi = new Bidi(Event.BIRTHDAY.getName() + SPACE + years + SPACE + TO + eventBE.getName(), Bidi.RTL);
            return bidi.writeReordered(Bidi.DO_MIRRORING);
        }
        if(eventBE.getEvent() == Event.WEDDINGDAY){
            Bidi bidi = new Bidi(Event.WEDDINGDAY.getName() + SPACE + years + SPACE + TO + eventBE.getName(), Bidi.RTL);
            return bidi.writeReordered(Bidi.DO_MIRRORING);
        }
        return null;
    }
}
