package dev.strongtino.soteria.util;

import java.util.Collection;

public class StringUtil {

    public static String convertToString(Collection<String> collection, boolean boldValues) {
        StringBuilder builder = new StringBuilder();
        String wrap = boldValues ? "**" : "";

        collection.forEach(element -> builder
                .append(wrap)
                .append(element)
                .append(wrap)
                .append(", "));

        int stringLength = builder.toString().length();

        return builder.substring(0, stringLength - (stringLength > 2 ? 2 : 0));
    }
}
