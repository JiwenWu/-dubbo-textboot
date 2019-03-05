package cn.wujw.textboot.support;

import cn.wujw.textboot.excel.export.ExcelExport;

import java.io.InputStream;
import java.util.List;

/**
 * Desc:
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-28
 */
public class ExcelExportConver {

    private List<List<String>> dataList;

    private String[] headData;

    public ExcelExportConver(List<List<String>> dataList, String[] headData) {
        this.dataList = dataList;
        this.headData = headData;
    }

    public ExcelExportConver(List<List<String>> dataList){
        this(dataList,null);
    }

    public InputStream commonExport(){
       return ExcelExport.commonExportExcel(headData,dataList);
    }
}
