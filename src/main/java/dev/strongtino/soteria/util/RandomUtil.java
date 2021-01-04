package dev.strongtino.soteria.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by StrongTino on 28.12.2020.
 */

public class RandomUtil {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");

    public static String getCurrentDate() {
        return simpleDateFormat.format(new Date());
    }
}
