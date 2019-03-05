package cn.wujw.textboot.factory;

import cn.wujw.textboot.model.ColumnField;

import java.io.Serializable;
import java.util.List;

/**
 * Desc:
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-27
 */
public class ExcelRuleBuilder implements Serializable {

    private List<ColumnField> columnFieldList;

    public List<ColumnField> getColumnFieldList() {
        return columnFieldList;
    }

    public void setColumnFieldList(List<ColumnField> columnFieldList) {
        this.columnFieldList = columnFieldList;
    }
}
