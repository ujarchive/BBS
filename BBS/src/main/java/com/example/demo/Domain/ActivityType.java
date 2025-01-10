package com.example.demo.Domain;

public enum ActivityType {

    LOGIN("LOGIN", "로그인"),
    LOGIN_FAILURE("LOGIN_FAILURE","로그인 실패"),
    LOGOUT("LOGOUT","로그아웃")
    ;

    ActivityType(String code, String message){
        this.code = code;
        this.message = message;
    }

    private final String code;
    private final String message;
}
