package com.example.demo.Utils;

public class LengthUtils {

    // 문자열을 오른쪽 패딩하여 지정된 길이로 변환
    public static String rightPading(String input, int length){
        if (input == null) {
            return null;
        }
        if (input.length() >= length) {
            return input.substring(0, length);
        }
        StringBuilder paddedString = new StringBuilder();
        paddedString.append(input);
        while (paddedString.length() < length) {
            paddedString.append(" ");
        }
        return paddedString.toString();
    }

    public static String leftPading(String input, int length) {
        if (input == null) {
            return null;
        }
        if (input.length() >= length) {
            return input.substring(0, length);
        }
        StringBuilder paddedString = new StringBuilder();
        paddedString.append(input);
        while (paddedString.length() < length) {
            paddedString.insert(0, " ");
        }
        return paddedString.toString();
    }

    public static String leftPadingNumber(int number, int length) {
        String numberString = String.valueOf(number);
        return leftPading(numberString, length);
    }

}
