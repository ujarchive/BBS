package com.easynetworks.lotteFactoring.Utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CodeGenerator {

    public static String codeGenerator(){
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int length = 15;

        // 중복되지 않는 문자열 생성을 위한 Set 사용
        Set<Character> uniqueCharacters = new HashSet<>();
        StringBuilder stringBuilder = new StringBuilder();

        // 무작위 문자열 생성
        Random random = new Random();
        while (stringBuilder.length() < length) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            // 중복된 문자가 없는 경우에만 추가
            if (uniqueCharacters.add(randomChar)) {
                stringBuilder.append(randomChar);
            }
        }

        return stringBuilder.toString();
    }
}
