package com.augustin.gabriel.goldenraspberryawardsapi.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtils {

    private StringUtils() {}

    public static List<String> splitFromString(String str) {
        List<String> result = new ArrayList<>();

        String[] byAnds = str.split(" and ");
        for (String byAnd : byAnds) {
            String[] byCommas = byAnd.trim().split(",");
            result.addAll(Arrays.asList(byCommas));
        }

        return result;
    }

    public static Boolean toBoolean(String str) {
        str = str.trim();

        if (org.apache.commons.lang3.StringUtils.isBlank(str))
            return false;

        return switch (str.toLowerCase()) {
            case "true", "yes" -> true;
            default -> false;
        };
    }

}
