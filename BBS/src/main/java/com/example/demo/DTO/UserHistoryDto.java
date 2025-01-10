package com.example.demo.DTO;

import com.example.demo.Common.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserHistoryDto {
    @ExcelColumn(headerName = "접속자")
    private String name;

    @ExcelColumn(headerName = "접속ip")
    private String ip;

    @ExcelColumn(headerName = "접속국가")
    private String locale;

    @ExcelColumn(headerName = "접속기기")
    private String device;

    @ExcelColumn(headerName = "액션")
    private String action;

    @ExcelColumn(headerName = "접속일시")
    private String createdDate;
}
