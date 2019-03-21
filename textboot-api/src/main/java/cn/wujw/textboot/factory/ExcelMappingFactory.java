package cn.wujw.textboot.factory;

import cn.wujw.textboot.annotation.ImportField;
import cn.wujw.textboot.model.BasicModel;
import cn.wujw.textboot.model.ColumnField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Desc:
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-27
 */
public enum  ExcelMappingFactory {
    /**
     * 枚举单利模式
     */
    INSTANCE;

    public ExcelRuleBuilder builder(Class<? extends BasicModel> clazz){

      ExcelRuleBuilder ruleBuilder = new ExcelRuleBuilder();

        List<ColumnField> list = new ArrayList<>();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ImportField annotation = field.getAnnotation(ImportField.class);
            if (annotation != null && annotation.column().trim().length() > 0) {
                ColumnField columnField = new ColumnField();
                columnField.setRequired(annotation.required());
                columnField.setColumn(annotation.column());
                columnField.setScale(annotation.scale());
                columnField.setFiled(field.getName());
                columnField.setFiledType(field.getType());
                columnField.setRegex(annotation.regex());
                columnField.setRegexMessage(annotation.regexMessage());
                columnField.setRoundingMode(annotation.roundingMode());
                columnField.setDateFormat(annotation.dataFormat());
                list.add(columnField);
            }
        }
        ruleBuilder.setColumnFieldList(list);

        return ruleBuilder;
    }
}
