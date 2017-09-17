package com.gotofinal.blog.benchmark.part3.test;

import org.apache.commons.lang3.RandomStringUtils;

public class Something {
    public String publicObjectField = RandomStringUtils.randomAlphabetic(20);
    private String privateObjectField = RandomStringUtils.randomAlphabetic(20);
}
