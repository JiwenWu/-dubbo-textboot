package cn.wujw.textboot.model;

import java.io.Serializable;

/**
 * Desc:
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-21
 */
public class ErrorEntity implements Serializable {
    /**
     * sheet索引
     */
    private Integer sheetIndex;
    /**
     * row 行索引
     */
    private Integer rowIndex;
    /**
     * cell 列索引
     */
    private Integer cellIndex;
    /**
     * cell 值
     */
    private String cellValue;
    /**
     * column 列名
     */
    private String columnName;
    /**
     * 错误信息
     */
    private String errorMessage;

    public Integer getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(Integer sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public Integer getCellIndex() {
        return cellIndex;
    }

    public void setCellIndex(Integer cellIndex) {
        this.cellIndex = cellIndex;
    }

    public String getCellValue() {
        return cellValue;
    }

    public void setCellValue(String cellValue) {
        this.cellValue = cellValue;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ErrorEntity{" +
                "sheetIndex=" + sheetIndex +
                ", rowIndex=" + rowIndex +
                ", cellIndex=" + cellIndex +
                ", cellValue='" + cellValue + '\'' +
                ", columnName='" + columnName + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
