package com.bilalalp.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {

    public static final String DD_MM_YYYY = "MM/dd/yyyy";

    private DateUtil() {
        //Util Class
    }

    public static Date toDate(final String dateStr, final String pattern) {

        try {
            return new SimpleDateFormat(pattern).parse(dateStr);
        } catch (final Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}