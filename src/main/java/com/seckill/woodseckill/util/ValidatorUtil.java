package com.seckill.woodseckill.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {

    private static final Pattern mobile_pattern = Pattern.compile("\\d{10}");

    public static boolean isMobile(String src) {
        if (StringUtils.isEmpty(src)) {
            return false;
        }
        Matcher matcher = mobile_pattern.matcher(src);
        return matcher.matches();
    }

//    public static void main(String[] args) {
//        System.out.println(isMobile("9496685990"));
//    }
}
