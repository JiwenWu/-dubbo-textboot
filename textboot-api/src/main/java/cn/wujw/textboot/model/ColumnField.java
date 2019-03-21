package cn.wujw.textboot.model;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * Desc:
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-27
 */
public class ColumnField implements Serializable {
    /**
     * 字段名称
     */
    private String filed;
    /**
     * 字段属性
     */
    private Class<?> filedType;
    /**
     * 是否必填
     */
    private Boolean required = false;
    /**
     * 列名
     */
    private String column;
    /**
     * BigDecimal 精度
     */
    private int scale = 2;
    /**
     * 舍入规则
     */
    private int roundingMode  = BigDecimal.ROUND_HALF_EVEN;
    /**
     * 正则校验
     */
    private String regex;
    /**
     * 正则校验不通过的提示语
     */
    private String regexMessage = "正则校验不通过";

    /**
     * 日期格式 默认 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    private String dateFormat;

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getFiled() {
        return filed;
    }

    public ColumnField setFiled(String filed) {
        this.filed = filed;
        return this;
    }

    public Class<?> getFiledType() {
        return filedType;
    }

    public ColumnField setFiledType(Class<?> filedType) {
        this.filedType = filedType;
        return this;
    }

    public Boolean getRequired() {
        return required;
    }

    public ColumnField setRequired(Boolean required) {
        this.required = required;
        return this;
    }

    public String getColumn() {
        return column;
    }

    public ColumnField setColumn(String column) {
        this.column = column;
        return this;
    }

    public int getScale() {
        return scale;
    }

    public ColumnField setScale(int scale) {
        this.scale = scale;
        return this;
    }

    public int getRoundingMode() {
        return roundingMode;
    }

    public ColumnField setRoundingMode(int roundingMode) {
        this.roundingMode = roundingMode;
        return this;
    }

    public String getRegex() {
        return regex;
    }

    public ColumnField setRegex(String regex) {
        this.regex = regex;
        return this;
    }

    public String getRegexMessage() {
        return regexMessage;
    }

    public ColumnField setRegexMessage(String regexMessage) {
        this.regexMessage = regexMessage;
        return this;
    }
}
