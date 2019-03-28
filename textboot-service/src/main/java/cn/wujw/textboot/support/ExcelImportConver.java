package cn.wujw.textboot.support;

import cn.wujw.textboot.common.*;
import cn.wujw.textboot.config.OssProperties;
import cn.wujw.textboot.excel.parser.ExcelReaderHandler;
import cn.wujw.textboot.exception.TextBootException;
import cn.wujw.textboot.model.ColumnField;
import cn.wujw.textboot.model.ErrorEntity;
import cn.wujw.textboot.factory.ExcelRuleBuilder;
import java.math.BigDecimal;
import org.apache.commons.collections4.CollectionUtils;


import java.io.InputStream;
import java.util.*;

/**
 * Desc:
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-22
 */
public class ExcelImportConver {

    private int startIndex;

    private String fileUrl;

    private InputStream inputStream;

    private ExcelRuleBuilder builder;

    private List<Map<Integer,String>> dataList = null;

    private List<Map<String,Object>> buildDataList = null;

    private List<ErrorEntity> errorEntityList = null;

    private ExcelImportConver(int startIndex, String fileUrl, ExcelRuleBuilder builder,InputStream inputStream){
        this.startIndex = startIndex;
        this.fileUrl = fileUrl;
        this.builder = builder;
        this.inputStream = inputStream;
    }

    public ExcelImportConver(int startIndex,String fileUrl){
        this(startIndex,fileUrl,null,null);
    }

    public ExcelImportConver(String fileUrl){
        this(0,fileUrl,null,null);
    }

    public ExcelImportConver(String fileUrl, ExcelRuleBuilder builder){
        this(0,fileUrl,builder,null);
    }
    public ExcelImportConver(int startIndex,String fileUrl, ExcelRuleBuilder builder){
        this(startIndex,fileUrl,builder,null);
    }

    public ExcelImportConver(int startIndex,InputStream inputStream){
        this(startIndex,null,null,inputStream);
    }

    public ExcelImportConver(InputStream inputStream){
        this(0,null,null,inputStream);
    }

    public ExcelImportConver(InputStream inputStream, ExcelRuleBuilder builder){
        this(0,null,builder,inputStream);
    }

    public ExcelImportConver(int startIndex, InputStream inputStream, ExcelRuleBuilder builder){
        this(startIndex,null,builder,inputStream);
    }


    public List<Map<Integer, String>> getDataList() {
        return dataList;
    }

    public List<ErrorEntity> getErrorEntityList() {
        return errorEntityList;
    }

    public List<Map<String, Object>> getBuildDataList() {
        return buildDataList;
    }

    private Map<String,String> headData;

    /**
     * 初始化完成数据的读取和空格数据的组装，保证数据的方正
     * @throws Exception
     */
    private void initExcle() throws Exception {

        ExcelReaderHandler handler = new ExcelReaderHandler();
        if (fileUrl != null) {
            String fileName = "";
            OssProperties ossProperties = SpringBeanFactoryUtils.getBean(OssProperties.class);
            AliyunOssUtils aliyunOssUtils = SpringBeanFactoryUtils.getBean(AliyunOssUtils.class);
            try {
                fileName = ossProperties.getTempPath() + UUID.randomUUID().toString().replace("-", "");
                aliyunOssUtils.downloadFile(fileUrl, fileName);
            } catch (Exception e) {
                e.printStackTrace();
                throw new TextBootException("文件下载失败");
            }
            handler.excelReader(fileName);
        }else if (inputStream != null){
            handler.excelReader(inputStream);
        }else {
            throw new TextBootException("数据源错误");
        }

        List<Map<String, String>> excelData = handler.getDataList();

        // 拿到表头数据
        headData = excelData.get(startIndex);

        Map<Integer,String> newMap;
        // 根据其转化的表头来重新组装其他的数据
        dataList = new ArrayList<>(excelData.size());
        for (Map<String, String> map : excelData) {
            newMap = new HashMap<>();
            for (String key : headData.keySet()) {
                if (map.get(key) != null){
                    newMap.put(Letter2Number.getNumber(key),map.get(key));
                }else {
                    newMap.put(Letter2Number.getNumber(key),"");
                }
            }
            dataList.add(newMap);
        }
        excelData.clear();
        excelData = null;
    }

    private void buildData() throws TextBootException {
        // 列A,B
        Set<String> keySet = headData.keySet();

        // excel中的列
        List<Integer> excleHeadList = new ArrayList<>();
        for (String str : keySet) {
            excleHeadList.add(Letter2Number.getNumber(str));
        }
        // 连续的真实列
        List<Integer> realList = new ArrayList<>();
        int max = Collections.max(excleHeadList);
        for (int i = 1; i <= max; i++) {
            realList.add(i);
        }
        realList.removeAll(excleHeadList);
        if (realList.size() > 0) {
            // 表头出现空的列
            String errCol = StringUtil.join(realList, ",");
            throw new TextBootException(String.format("第[%s]行表头中第[%s]列为空", startIndex, errCol));
        }

        //表头中的值
        Collection<String> excelHeadCol = headData.values();

        // 所有构建的规则信息
        List<ColumnField> columnFieldList = builder.getColumnFieldList();

        //拿到所有必填的列
        List<String> requirdCol = new ArrayList<>();

        Map<Integer, ColumnField> columnMap = new HashMap<>();

        Map<Integer, String> dataHeadData = dataList.get(startIndex);

        for (ColumnField entity : columnFieldList) {
            if (entity.getRequired()) {
                requirdCol.add(entity.getColumn());
            }
            // 将build中的列信息重新建立一次索引，以符合excle的顺序,只拿取build中需要的列的值
            for (Map.Entry<Integer, String> head : dataHeadData.entrySet()) {
                if (entity.getColumn().equals(head.getValue())) {
                    columnMap.put(head.getKey(), entity);
                }
            }
        }
        requirdCol.removeAll(excelHeadCol);
        if (requirdCol.size() > 0) {
            throw new TextBootException(String.format("缺少必填的表头 [%s]", StringUtil.join(requirdCol, ",")));
        }
        buildDataList = new ArrayList<>();
        errorEntityList = new ArrayList<>();
        // 开始组装和校验数据
        int size = dataList.size();
        for (int i = startIndex + 1; i < size; i++) {
            Map<String, Object> map = new HashMap<>();
            for (Map.Entry<Integer, String> entry : dataList.get(i).entrySet()) {

                ColumnField field = columnMap.get(entry.getKey());
                boolean canPut = true;

                if (field != null) {
                    String value = entry.getValue();
                    ErrorEntity errorEntity ;
                    if (field.getRequired() && StringUtil.isEmpty(value)) {
                        // 必填项不能为空
                        errorEntity = new ErrorEntity();
                        errorEntity.setErrorMessage("不能为空");
                        errorEntity.setRowIndex(i);
                        errorEntity.setCellIndex(entry.getKey());
                        errorEntity.setColumnName(field.getColumn());
                        errorEntityList.add(errorEntity);
                        canPut = false;
                    } else if (StringUtil.isNotEmpty(value)){
                        // 有值，则进行下一步的校验
                        if (StringUtil.isNotEmpty(field.getRegex())){
                            if (!RegexUtil.isMatch(field.getRegex(),value)){
                                errorEntity = new ErrorEntity();
                                errorEntity.setErrorMessage(field.getRegexMessage());
                                errorEntity.setCellValue(value);
                                errorEntity.setRowIndex(i);
                                errorEntity.setCellIndex(entry.getKey());
                                errorEntity.setColumnName(field.getColumn());
                                errorEntityList.add(errorEntity);
                                canPut = false;
                            }
                        }
                    }
                    if (canPut) {
                        map.put(field.getFiled(), convertCellValue(field,value));
                    }
                }
            }
            buildDataList.add(map);
        }
        dataList.clear();
        dataList = null;
    }

    private Object convertCellValue(ColumnField field, String cellValue) {
        Object value = null;
        String filedClazz = field.getFiledType().getName();
        if (filedClazz.equals(Date.class.getName())) {
            try {
                value =  DateFormatUtil.parse(field.getDateFormat(), cellValue);
            } catch (Exception e) {
                return cellValue;
            }
        } else if (filedClazz.equals(Short.class.getName()) || filedClazz.equals(short.class.getName())) {
            value = Short.valueOf(cellValue);
        } else if (filedClazz.equals(Integer.class.getName()) || filedClazz.equals(int.class.getName())) {
            value = Integer.valueOf(cellValue);
        } else if (filedClazz.equals(Double.class.getName()) || filedClazz.equals(double.class.getName())) {
            value = Double.valueOf(cellValue);
        } else if (filedClazz.equals(Long.class.getName()) || filedClazz.equals(long.class.getName())) {
            value = Long.valueOf(cellValue);
        } else if (filedClazz.equals(Float.class.getName()) || filedClazz.equals(float.class.getName())) {
            value = Float.valueOf(cellValue);
        } else if (filedClazz.equals(BigDecimal.class.getName())) {
            if (field.getScale() == -1) {
                value = new BigDecimal(cellValue);
            } else {
                value = new BigDecimal(cellValue).setScale(field.getScale(), field.getRoundingMode());
            }
        } else {
            return cellValue;
        }
        return value;
    }

    public void converData() throws Exception {
        if(builder != null){
            if (CollectionUtils.isNotEmpty(builder.getColumnFieldList())){
                this.initExcle();
                this.buildData();
            }else {
                throw new TextBootException("无有效注解");
            }
        }else {
            this.initExcle();
        }
    }
}
