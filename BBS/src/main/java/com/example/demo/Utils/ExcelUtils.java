package com.easynetworks.lotteFactoring.Utils;

import com.easynetworks.lotteFactoring.Common.ExcelColumn;
import com.easynetworks.lotteFactoring.DTO.UserHistoryDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ExcelUtils {

    public byte[] convertWorkbookToByteArray(Workbook workbook) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    private static void closeWorkBook(Workbook workbook) {
        try {
            if(workbook != null) {
                workbook.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void makingExcel(String name, List<UserHistoryDto> data, HttpServletResponse response)
            throws IOException, ParseException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1"); //시트 이름

        sheet.setDefaultColumnWidth(15); // 디폴트 너비 설정
        Cell cell = null;
        Row row = null;
        List<String> excelHeaderList = getHeaderName(getClass(data));

        // 컬럼 헤더 스타일 지정
        CellStyle HeaderStyle = workbook.createCellStyle();
        HeaderStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        HeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HeaderStyle.setAlignment(HorizontalAlignment.CENTER); // 가로 정렬
        HeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 세로 정렬
        HeaderStyle.setBorderLeft(BorderStyle.THIN);
        HeaderStyle.setBorderRight(BorderStyle.THIN);
        HeaderStyle.setBorderTop(BorderStyle.THIN);
        HeaderStyle.setBorderBottom(BorderStyle.THIN);
        Font HeaderStyleFont = workbook.createFont();
        HeaderStyleFont.setFontName("맑은 고딕");
        HeaderStyleFont.setColor(IndexedColors.GREY_80_PERCENT.getIndex());
        HeaderStyleFont.setFontHeightInPoints((short) 14); //폰트 크기 14
        HeaderStyle.setFont(HeaderStyleFont);

        // 바디 테두리 설정
        CellStyle BodyCellStyle = workbook.createCellStyle();
        BodyCellStyle.setAlignment(HorizontalAlignment.CENTER);
        BodyCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        BodyCellStyle.setBorderLeft(BorderStyle.THIN);
        BodyCellStyle.setBorderRight(BorderStyle.THIN);
        BodyCellStyle.setBorderTop(BorderStyle.THIN);
        BodyCellStyle.setBorderBottom(BorderStyle.THIN);
        Font BodyCellStyleFont = workbook.createFont();
        BodyCellStyleFont.setFontHeightInPoints((short) 12); //폰트 크기 12
        BodyCellStyle.setFont(BodyCellStyleFont);

        row = sheet.createRow(0);
        for (int i = 0; i < excelHeaderList.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue(excelHeaderList.get(i));
            cell.setCellStyle(HeaderStyle);
        }
        cell = row.createCell(excelHeaderList.size());
        cell.setCellValue("접속시간");
        cell.setCellStyle(HeaderStyle);

        int rowCount = 0;
        for(UserHistoryDto dto : data){
            row = sheet.createRow(rowCount++);

            cell = row.createCell(0);
            cell.setCellValue(rowCount);
            cell.setCellStyle(BodyCellStyle);

            cell = row.createCell(1);
            cell.setCellValue(name);
            cell.setCellStyle(BodyCellStyle);

            cell = row.createCell(2);
            cell.setCellValue(dto.getIp());
            cell.setCellStyle(BodyCellStyle);

            cell = row.createCell(3);
            cell.setCellValue(dto.getLocale());
            cell.setCellStyle(BodyCellStyle);

            cell = row.createCell(4);
            cell.setCellValue(dto.getDevice());
            cell.setCellStyle(BodyCellStyle);

            cell = row.createCell(5);
            cell.setCellValue(dto.getCreatedDate());
            cell.setCellStyle(BodyCellStyle);
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");        try {
            String encodedName = URLEncoder.encode("easy-"+ LocalDateTime.now().getDayOfYear()+LocalDateTime.now().getDayOfMonth(), "UTF-8");
            String headerValue = "attachment;filename=" + encodedName + ".xlsx";
            response.setHeader("Content-Disposition", headerValue);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            workbook.write(response.getOutputStream());
        } catch (IOException e) {

        } finally {
            closeWorkBook(workbook);
        }
    }

    private static Class<?> getClass(List<?> data) {
        // List가 비어있지 않다면 List가 가지고 있는 모든 DTO는 같은 필드를 가지고 있으므로,
        // 맨 마지막 DTO만 빼서 클래스 정보를 반환한다.
        if(!CollectionUtils.isEmpty(data)) {
            return data.get(data.size()-1).getClass();
        } else {
            throw new IllegalStateException("조회된 리스트가 비어 있습니다. 확인 후 다시 진행해주시기 바랍니다.");
        }
    }

    private static List<String> getHeaderName(Class<?> type) {
        // 스트림으로 엑셀 헤더 이름들을 리스트로 반환
        // 1. 매개변수로 전달된 클래스의 필드들을 배열로 받아, 스트림을 생성
        // 2. @ExcelColumn 애너테이션이 붙은 필드만 수집
        // 3. @ExcelColumn 애너테이션이 붙은 필드에서 애너테이션의 값을 매핑
        // 4. LinkedList로 반환
        List<String> excelHeaderNameList =  Arrays.stream(type.getDeclaredFields())
                .filter(s -> s.isAnnotationPresent(ExcelColumn.class))
                .map(s -> s.getAnnotation(ExcelColumn.class).headerName())
                .collect(Collectors.toCollection(LinkedList::new));

        // 헤더의 이름을 담은 List가 비어있을 경우, 헤더 이름이 지정되지 않은 것이므로, 예외를 발생시킨다.
        if(CollectionUtils.isEmpty(excelHeaderNameList)) {
            throw new IllegalStateException("헤더 이름이 없습니다.");
        }
        return excelHeaderNameList;
    }

}
