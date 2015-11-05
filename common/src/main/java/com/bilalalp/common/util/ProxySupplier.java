package com.bilalalp.common.util;

import com.bilalalp.common.dto.ProxyDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class ProxySupplier {

    private static final List<ProxyDto> PROXY_LIST = new ArrayList<>();

    static {
        PROXY_LIST.add(new ProxyDto("111.223.52.2", "3128"));
        PROXY_LIST.add(new ProxyDto("203.98.181.145", "80"));
        PROXY_LIST.add(new ProxyDto("218.200.66.199", "8080"));
    }

    public static ProxyDto getRandomProxy(){

        return null;
    }
}