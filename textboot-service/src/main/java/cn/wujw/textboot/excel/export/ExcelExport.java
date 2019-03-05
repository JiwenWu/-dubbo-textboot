package cn.wujw.textboot.excel.export;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Desc:
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-28
 */
public class ExcelExport {
    /**
     * 通用的Excel导出工具
     *
     * @param headerNames 表头字段数组
     * @param dataList    组装好的数据 *与表头名称下标一致*
     *                    String内容为表头相对应的列内容 ，List<String>为一行数据
     * @return inputStream
     */
    public static InputStream commonExportExcel(String[] headerNames, List<List<String>> dataList) {
        double widthRiTio = 256.0D;
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 表头字体
        XSSFFont headFont = workbook.createFont();
        headFont.setFontName("宋体");
        headFont.setFontHeightInPoints((short) 12);
        headFont.setBold(true);
        // 红色字体
        XSSFFont redFont = workbook.createFont();
        redFont.setFontName("宋体");
        redFont.setColor(IndexedColors.RED.getIndex());
        headFont.setFontHeightInPoints((short) 12);
        headFont.setBold(true);
        // 通用字体
        XSSFFont commonFont = workbook.createFont();
        commonFont.setFontName("宋体");
        commonFont.setFontHeightInPoints((short) 11);
        headFont.setBold(false);
        // 表头样式
        XSSFCellStyle headCellStyle = workbook.createCellStyle();
        headCellStyle.setFont(headFont);
        headCellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        headCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headCellStyle.setBorderBottom(BorderStyle.THIN);
        headCellStyle.setBorderLeft(BorderStyle.THIN);
        headCellStyle.setBorderRight(BorderStyle.THIN);
        headCellStyle.setBorderTop(BorderStyle.THIN);
        //红色字体文本框样式
        XSSFCellStyle redHeadCellStyle = workbook.createCellStyle();
        redHeadCellStyle.setFont(redFont);
        redHeadCellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        redHeadCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        redHeadCellStyle.setBorderBottom(BorderStyle.THIN);
        redHeadCellStyle.setBorderLeft(BorderStyle.THIN);
        redHeadCellStyle.setBorderRight(BorderStyle.THIN);
        redHeadCellStyle.setBorderTop(BorderStyle.THIN);
        // 通用样式
        XSSFCellStyle commonCellStyle = workbook.createCellStyle();
        commonCellStyle.setFont(commonFont);
        // 创建sheet
        XSSFSheet sheet = workbook.createSheet();
        XSSFRow row;
        XSSFCell cell;
        // 是否有表头
        boolean hasHead = headerNames != null && headerNames.length > 0;
        if (hasHead) {
            for (int i = 0; i < headerNames.length; i++) {
                // 列宽尽量自适应
                int length = headerNames[i].length() > 4 ? headerNames[i].length() + 9 : 10;
                sheet.setColumnWidth(i, (int) (widthRiTio * length));
            }
            // 创建表头
            row = sheet.createRow(0);
            for (int i = 0; i < headerNames.length; i++) {
                cell = row.createCell(i, CellType.STRING);
                cell.setCellValue(headerNames[i]);
                if (headerNames[i].contains("必填")) {
                    cell.setCellStyle(redHeadCellStyle);
                } else {
                    cell.setCellStyle(headCellStyle);
                }
            }
        }

        // 起始位置
        int index = hasHead ? 1 : 0;
        // 写入数据
        for (int i = 0; i < dataList.size(); i++) {
            row = sheet.createRow(index++);
            for (int j = 0, k = dataList.get(i).size(); j < k; j++) {
                cell = row.createCell(j, CellType.STRING);
                if (dataList.get(i).get(j) != null) {
                    cell.setCellValue(dataList.get(i).get(j));
                } else {
                    cell.setCellValue(new XSSFRichTextString());
                }
                cell.setCellStyle(commonCellStyle);
            }
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
