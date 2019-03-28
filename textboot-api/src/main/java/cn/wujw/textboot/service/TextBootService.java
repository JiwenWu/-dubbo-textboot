package cn.wujw.textboot.service;

import cn.wujw.textboot.factory.ExcelRuleBuilder;
import cn.wujw.textboot.enums.DataLocation;
import cn.wujw.textboot.model.ResultBody;

import java.util.List;

/**
 * Desc:
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-21
 */
public interface TextBootService {

    /**
     * 获取excel数据
     * @param startIndex 起始行
     * @param fileUrl 文件url路径
     * @param dataLocation  数据传递方式
     * @return
     */
    ResultBody urlToExcelData(int startIndex,String fileUrl, DataLocation dataLocation);

    /**
     * 获取excel数据
     * @param startIndex 起始行
     * @param fileUrl 文件路径
     * @param clazz 校验规则配置类
     * @param dataLocation  数据传递方式
     * @return
     */
    ResultBody urlToValidateExcelData(int startIndex, String fileUrl, ExcelRuleBuilder clazz, DataLocation dataLocation);

    /**
     * 导出excle
     * @param headData 表头数据
     * @param listData 组装好的行列数据
     * @return
     */
    ResultBody exportExcel(String[] headData,List<List<String>> listData);

    /**
     * 导出excle
     * @param listData 组装好的行列数据
     * @return
     */
    ResultBody exportExcel(List<List<String>> listData);

    /**
     * 导出文件byte[]
     * @param listData 组装好的行列数据
     * @return
     */
    byte[] exportStreamExcel(List<List<String>> listData);

    /**
     * 导出文件byte[]
     * @param headData
     * @param listData
     * @return
     */
    byte[] exportStreamExcel(String[] headData,List<List<String>> listData);

    /**
     * 获取excel数据
     * @param startIndex 起始行
     * @param fileBytes 文件byte数组
     * @param dataLocation  数据传递方式
     * @return
     */
    ResultBody streamToExcelData(int startIndex,byte[] fileBytes, DataLocation dataLocation);

    /**
     * 获取excel数据
     * @param startIndex 起始行
     * @param fileBytes 文件byte数组
     * @param clazz 校验规则配置类
     * @param dataLocation  数据传递方式
     * @return
     */
    ResultBody streamToValidateExcelData(int startIndex, byte[] fileBytes, ExcelRuleBuilder clazz, DataLocation dataLocation);

    /**
     * 解析图片中的文字内容
     * @param url
     * @return
     */
    ResultBody ocrImage(String url);
}
