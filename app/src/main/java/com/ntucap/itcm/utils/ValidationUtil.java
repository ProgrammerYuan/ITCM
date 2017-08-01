package com.ntucap.itcm.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ProgrammerYuan on 07/04/17.
 */

public class ValidationUtil {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    public static <K,V> V validateHashmapGet(Map<K, V> map, K key, V defaultValue){
        return map.containsKey(key) ? map.get(key) : defaultValue;
    }

}
