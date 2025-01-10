package com.easynetworks.lotteFactoring.Utils;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaskingUtils {
    public enum MaskingType {
        Name,
        Mobile,
        Email,
        Address,
        BankName,
        AccountNumber,
        Year,
        Month,
        Birthday,
        CardNumber
    }


    public static String maskAs(String value, MaskingType maskingType ) {
        if (StringUtils.isEmpty(value) || value.trim().length() == 0) return value;

        String maskingValue = value;

        switch (maskingType) {
            case Name:
                if (value.length() == 2)
                    maskingValue = "*"+value.substring(1);
                else if (value.length() == 3)
                    maskingValue = value.substring(0,1)+"*"+value.substring(2);
                else if (value.length() >= 4)
                    maskingValue = value.substring(0,1)+"**"+value.substring(3);
                break;
            case Mobile:
                if (value.length() >= 8 && value.length() <= 10)
                    maskingValue = (value.length() == 8) ? value.substring(0,4)+"****" : value.substring(0,4)+"****"+value.substring(8);
                else if (value.length() >= 11)
                    maskingValue = value.substring(0,5)+"****"+value.substring(9);
                // what about length 1 to 7
                break;
            case Email:
                if (!value.substring(0).equals("@") && value.contains("@")){
                    String emailValue = value.split("@")[0];
                    if (emailValue.length() == 1 || emailValue.length() == 2)
                        maskingValue = (emailValue.length() == 1) ? value.replaceAll("(?<=.{0}).(?=.*@)", "*") : "*"+ emailValue.substring(1,2)+"@"+value.split("@")[1];
                    else if (emailValue.length() == 3 || emailValue.length() == 4)
                        maskingValue = (emailValue.length() == 3) ? value.replaceAll("(?<=.{1}).(?=.*@)", "*") : emailValue.substring(0,1)+ "**" + emailValue.substring(3)+"@"+ value.split("@")[1];
                    else if (emailValue.length() > 4)
                        maskingValue = emailValue.substring(0,1)+ "***" + emailValue.substring(4)+"@"+ value.split("@")[1];
                }
                break;
            case Address:
                StringBuilder address = new StringBuilder();
                for (int i =0; i < value.length(); i++) {
                    char item = value.charAt(i);
                    address.append(Character.isWhitespace(item) ? item : '*');
                }
                maskingValue = address.toString();
                break;
            case BankName:
                if(value.length() > 2) {
                    maskingValue =  "**" + value.substring(2);
                }else {
                    maskingValue = "**";
                }
                break;
            case AccountNumber:
                StringBuilder accountNumber = new StringBuilder();
                int length = maskingValue.length();
                if(length > 6) {
                    accountNumber.append(value.substring(0,6)); //what if a/c no length is less than 7
                }
                for (int i = 0; i <= length - 7; i++) {
                    accountNumber.append("*");
                }
                maskingValue = accountNumber.toString();
                break;
            case Year:
                StringBuilder year = new StringBuilder();
                for (int i =0; i < value.length(); i++) {
                    char item = value.charAt(i);
                    year.append(Character.isWhitespace(item) ? item : '*');
                }
                maskingValue = year.toString();
                break;
            case Month:
                StringBuilder month = new StringBuilder();
                for (int i =0; i < value.length(); i++) {
                    char item = value.charAt(i);
                    month.append(Character.isWhitespace(item) ? item : '*');
                }
                maskingValue = month.toString();
                break;
            case Birthday:
                StringBuilder birthday = new StringBuilder();
                maskingValue = value.replaceAll("[0-9]", "*");
                break;

            case CardNumber:
                StringBuilder cardNumber = new StringBuilder();
                maskingValue = value.substring(0, 4) + "********" + value.substring(12);
                break;
        }
        return maskingValue;
    }
}
