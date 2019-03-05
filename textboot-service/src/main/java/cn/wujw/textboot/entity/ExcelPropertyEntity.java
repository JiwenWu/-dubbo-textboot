package cn.wujw.textboot.entity;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Desc:
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-20
 */
public class ExcelPropertyEntity implements Serializable {
    /**
     * excelModel的字段field
     */
    private Field fieldEntity;
    /**
     * excle列名称（表头）
     */
    private String columnName;
    /**
     * 默认单元格的值
     */
    private String templateCellValue;

    /**
     *  是否必填
     */
    private Boolean required;

    /**
     * 日期格式
     */
    private String dateFormat;

    /**
     * 正则表达式校验
     */
    private String regex;

    /**
     * 正则不通过的提示信息
     */
    private String regexMessage;

    /**
     * BigDecimal精度 默认:2
     */
    private Integer scale;

    /**
     * BigDecimal 舍入规则 默认:2
     */
    private Integer roundingMode;

    public Field getFieldEntity() {
        return fieldEntity;
    }

    public void setFieldEntity(Field fieldEntity) {
        this.fieldEntity = fieldEntity;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTemplateCellValue() {
        return templateCellValue;
    }

    public void setTemplateCellValue(String templateCellValue) {
        this.templateCellValue = templateCellValue;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getRegexMessage() {
        return regexMessage;
    }

    public void setRegexMessage(String regexMessage) {
        this.regexMessage = regexMessage;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public Integer getRoundingMode() {
        return roundingMode;
    }

    public void setRoundingMode(Integer roundingMode) {
        this.roundingMode = roundingMode;
    }
}
