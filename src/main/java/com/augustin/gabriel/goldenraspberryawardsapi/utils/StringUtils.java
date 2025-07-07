package com.augustin.gabriel.goldenraspberryawardsapi.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtils {

    private StringUtils() {}

    public static List<String> splitFromString(String string) {
        List<String> result = new ArrayList<>();

        String[] byAnds = string.split(" and ");
        for (String byAnd : byAnds) {
            String[] byCommas = byAnd.trim().split(",");
            result.addAll(Arrays.asList(byCommas));
        }

        return result;
    }

}
