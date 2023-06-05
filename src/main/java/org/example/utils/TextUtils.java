package org.example.utils;

import com.ibm.icu.text.Bidi;

public class TextUtils {
    public static String setDirectionRtl(String text) {
        Bidi bidi = new Bidi(text, Bidi.RTL);
        return bidi.writeReordered(Bidi.DO_MIRRORING);
    }
}
