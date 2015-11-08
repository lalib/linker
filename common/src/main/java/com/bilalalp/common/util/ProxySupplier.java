package com.bilalalp.common.util;

import com.bilalalp.common.dto.ProxyDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class ProxySupplier {

    private static final List<ProxyDto> PROXY_LIST = new ArrayList<>();

    static {
        PROXY_LIST.add(new ProxyDto("31.210.10.34", "8080"));
        PROXY_LIST.add(new ProxyDto("91.106.42.22", "80"));
        PROXY_LIST.add(new ProxyDto("37.221.210.97", "3128"));
    }

    public static ProxyDto getRandomProxy() {
        final int index = new Random().nextInt(PROXY_LIST.size());
        return PROXY_LIST.get(index);
    }

    public static void applyRandomProxy() {
        final ProxyDto randomProxy = getRandomProxy();
        System.setProperty("http.proxyHost", randomProxy.getHost());
        System.setProperty("http.proxyPort", randomProxy.getPort());
        System.setProperty("https.proxyHost", randomProxy.getHost());
        System.setProperty("https.proxyPort", randomProxy.getPort());
    }
}