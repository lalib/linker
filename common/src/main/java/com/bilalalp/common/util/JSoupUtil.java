package com.bilalalp.common.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public final class JSoupUtil {

    private static final Integer TIMEOUT = 20000;
    private static final Integer FAULT_COUNT = 100;
    private static final int MILLIS = 1000;

    private JSoupUtil() {
        // Util Class
    }

    public static Element getBody(final String patentLink) {

        int faultCount = 0;
        while (true) {

            try {
                return Jsoup.connect(patentLink).timeout(TIMEOUT).get().body();
            } catch (Exception e) {
                faultCount++;
                sleep();
            }

            if (faultCount == FAULT_COUNT) {
                break;
            }
        }

        return null;
    }

    public static void sleep() {

        try {
            Thread.sleep(MILLIS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
