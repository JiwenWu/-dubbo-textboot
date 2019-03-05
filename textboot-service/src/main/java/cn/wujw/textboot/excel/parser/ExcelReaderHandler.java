package cn.wujw.textboot.excel.parser;

import cn.wujw.textboot.common.RegexUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Desc:
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-22
 */
public class ExcelReaderHandler extends ExcelReaderAbstract {
    /**
     * 提取列名称的正则表达式
     */
    private static final String DISTILL_COLUMN_REG = "^([A-Z]+)";
    private int curRow;

    /**
     * 读取excel的每一行记录。map的key是列号(A、B、C...), value是单元格的值。如果单元格是空，则没有值。
     */
    private List<Map<String, String>> dataList = new ArrayList<>();

    @Override
    public void optRows(int curRow, Map<String, String> rowValueMap) {
        this.curRow = curRow;
        Map<String, String> dataMap = new HashMap<>();
        for (Map.Entry<String, String> entry : rowValueMap.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            dataMap.put(removeNum(k), v);
        }
        dataList.add(dataMap);
    }

    /**
     * 删除单元格名称中的数字，只保留列号。
     * @param cellName 单元格名称。如:A1
     * @return 列号。如：A
     */
    private String removeNum(String cellName) {
        try {
            return RegexUtil.removeNum(DISTILL_COLUMN_REG,cellName);
        } catch (ExecutionException e) {
            return "";
        }
    }

    /**
     * 获取所有行的数据
     * @return
     */
    public List<Map<String, String>> getDataList() {
        return dataList;
    }

    /**
     * 获取指定行的数据
     * @param rowNum
     * @return
     */
    public Map<String, String> getRowData(int rowNum) {
        return rowNum <= curRow - 1 ? dataList.get(rowNum) : null;
    }

    /**
     * 获取行数
     * @return
     */
    public int getCurRow(){
        return this.curRow;
    }

}
