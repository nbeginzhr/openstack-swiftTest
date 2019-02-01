package com.haoyu.swift.utils;

import java.security.SecureRandom;
import java.util.UUID;

public class StringUtils {
    // 纯数字验证码
    private static final char[] MUMBERLIST = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    // 数字和字符混合验证码，去掉不容易识别的1和l，0和O
    private static final char[] STRLIST = {'w', 'B', 'T', '7', 'D', 'S', '2', 'c', '3', 'Y', '9', 'R', 'f', 'L', 'M', 'z', 'p', 't', 'k', 'G', 'Q', 'y', 'J', 'u', 'q', 's', 'V', 'P', 'i', 'b', '5', 'a', 'd', 'H', '6', 'E', 'h', 'W', 'x', 'm', 'U', 'Z', 'A', 'N', 'e', 'r', '8', 'n', 'v', 'X', '4', 'j', 'g', 'F', 'C', 'K'};
    // 随机数生成器
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 获取长度4位的随机字符字符串
     *
     * @return String 随机字符字符串
     */
    public static String random() {
        return random(4);
    }


    /**
     * 获取长度6位的随机数字字符串
     *
     * @return String 随机数字字符串
     */
    public static String randomNum() {
        return randomNum(6);
    }

    /**
     * 获取随机字符串
     *
     * @param length 长度
     * @return String 随机字符字符串
     */
    public static String random(final int length) {
        StringBuilder randomCode = new StringBuilder();
        if (length > 0) {
            for (int i = 0; i != length; ++i) {
                int rand = RANDOM.nextInt(STRLIST.length);
                randomCode.append(STRLIST[rand]);
            }
        }
        return randomCode.toString();
    }

    /**
     * 获取随机数字字符串
     *
     * @param length 长度
     * @return String 随机数字字符串
     */
    public static String randomNum(int length) {
        StringBuilder randomCode = new StringBuilder();
        if (length > 0) {
            for (int i = 0; i != length; ++i) {
                int rand = RANDOM.nextInt(MUMBERLIST.length);
                randomCode.append(MUMBERLIST[rand]);
            }
        }
        return randomCode.toString();
    }

    public static String UUID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }
}