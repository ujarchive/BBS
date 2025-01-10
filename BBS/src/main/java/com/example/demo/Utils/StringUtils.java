package com.easynetworks.lotteFactoring.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtils {
    public static List<String> splitStringToArrayList(String input, String delimiter) {
        if (input == null || input.isEmpty()) {
            return new ArrayList<>();
        }
        String[] items = input.split(delimiter);
        return new ArrayList<>(Arrays.asList(items));
    }

    public static String removeHyphen(String input){
        return input.replace("-","");
    }
}
