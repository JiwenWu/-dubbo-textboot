package cn.wujw.textboot.service;

import cn.wujw.textboot.common.AliyunOssUtils;
import cn.wujw.textboot.common.FileUtils;
import cn.wujw.textboot.common.IOUtils;
import cn.wujw.textboot.common.RedisCacheManager;
import cn.wujw.textboot.common.StringUtil;
import cn.wujw.textboot.enums.FileSuffix;
import cn.wujw.textboot.factory.ExcelRuleBuilder;
import cn.wujw.textboot.enums.DataLocation;
import cn.wujw.textboot.model.ResultBody;
import cn.wujw.textboot.support.ExcelExportConver;
import cn.wujw.textboot.support.ExcelImportConver;
import com.alibaba.fastjson.JSON;
import com.baidu.aip.ocr.AipOcr;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * Desc:
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-21
 */
@Service
public class TextBootServiceImpl implements TextBootService {
    private static Logger logger = LoggerFactory.getLogger(TextBootServiceImpl.class);

    @Autowired
    private RedisCacheManager redisCacheManager;

    @Autowired
    private AliyunOssUtils aliyunOssUtils;

    @Autowired
    private AipOcr aipOcr;

    @Override
    public ResultBody urlToExcelData(int startIndex, String fileUrl, DataLocation dataLocation) {
        ExcelImportConver excelImportConver = new ExcelImportConver(startIndex,fileUrl);
        return getResultBody(dataLocation, excelImportConver,false);
    }

    @Override
    public ResultBody urlToValidateExcelData(int startIndex, String fileUrl, ExcelRuleBuilder builder, DataLocation dataLocation) {
        ExcelImportConver excelImportConver = new ExcelImportConver(startIndex,fileUrl,builder);
        return getResultBody(dataLocation, excelImportConver,true);
    }

    @Override
    public ResultBody exportExcel(String[] headData, List<List<String>> listData) {
        ResultBody resultBody = new ResultBody();

        ExcelExportConver excelExportConver = new ExcelExportConver(listData,headData);
        InputStream inputStream = excelExportConver.commonExport();

        String url = aliyunOssUtils.uploadFileStream(inputStream, FileSuffix.XLSX);
        if (!StringUtils.isBlank(url)){
            resultBody.success(url);
        }
        return resultBody;
    }

    @Override
    public ResultBody exportExcel(List<List<String>> listData) {
        ResultBody resultBody = new ResultBody();

        ExcelExportConver excelExportConver = new ExcelExportConver(listData);
        InputStream inputStream = excelExportConver.commonExport();

        String url = aliyunOssUtils.uploadFileStream(inputStream, FileSuffix.XLSX);
        if (!StringUtil.isBlank(url)){
            resultBody.success(url);
        }
        return resultBody;
    }

    @Override
    public byte[] exportStreamExcel(List<List<String>> listData) {
        ExcelExportConver excelExportConver = new ExcelExportConver(listData);
        InputStream inputStream = excelExportConver.commonExport();
        return IOUtils.inputStream2ByteArray(inputStream);
    }

    @Override
    public byte[] exportStreamExcel(String[] headData, List<List<String>> listData) {
        ExcelExportConver excelExportConver = new ExcelExportConver(listData,headData);
        InputStream inputStream = excelExportConver.commonExport();
        return IOUtils.inputStream2ByteArray(inputStream);
    }

    @Override
    public ResultBody streamToExcelData(int startIndex, byte[] fileBytes, DataLocation dataLocation) {
        ExcelImportConver excelImportConver = new ExcelImportConver(startIndex,new ByteArrayInputStream(fileBytes));
        return getResultBody(dataLocation, excelImportConver,false);
    }

    @Override
    public ResultBody streamToValidateExcelData(int startIndex, byte[] fileBytes, ExcelRuleBuilder builder, DataLocation dataLocation) {
        ExcelImportConver excelImportConver = new ExcelImportConver(startIndex,new ByteArrayInputStream(fileBytes),builder);
        return getResultBody(dataLocation, excelImportConver,true);
    }

    /**
     * 获取返回数据
     * @param dataLocation 数据返回方式
     * @param excelImportConver 转化对象
     * @param validate 是否有校验builder
     * @return
     */
    private ResultBody getResultBody(DataLocation dataLocation, ExcelImportConver excelImportConver,boolean validate) {
        ResultBody resultBody = new ResultBody();
        try {
            excelImportConver.converData();
            if (CollectionUtils.isNotEmpty(excelImportConver.getErrorEntityList())){
                return resultBody.failed(excelImportConver.getErrorEntityList());
            }
            switch (dataLocation){
                case SYNC:
                    return resultBody.success(validate ? excelImportConver.getBuildDataList() : excelImportConver.getDataList());
                case REDIS:
                    String redisKey = UUID.randomUUID().toString().replace("-","");
                    redisCacheManager.set(redisKey,validate ? excelImportConver.getBuildDataList() : excelImportConver.getDataList(),3600);
                    logger.info(">>>>>>>>> 存入redis,key{}",redisKey);
                    return resultBody.success(redisKey);
                default:
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return resultBody.error(e.getMessage());
        }
        return resultBody;
    }

    @Override
    public ResultBody ocrImage(String url){
        ResultBody resultBody = new ResultBody();
        String uuid = UUID.randomUUID().toString().replace("-","");
        aliyunOssUtils.downloadFile(url,uuid);
        try {
            byte[] bytes = FileUtils.readFileByBytes(uuid);
            HashMap<String, String> options = new HashMap<>(16);
            options.put("language_type", "CHN_ENG");
            options.put("detect_direction", "true");
            options.put("detect_language", "true");
            options.put("probability", "true");

            JSONObject jsonObject = aipOcr.accurateGeneral(bytes, options);
            JSONArray wordsResult = jsonObject.getJSONArray("words_result");
            Iterator<Object> iterator = wordsResult.iterator();
            StringBuilder stringBuilder = new StringBuilder();
            while (iterator.hasNext()){
                Object next = iterator.next();
                HashMap hashMap = JSON.parseObject(next.toString(), HashMap.class);
                stringBuilder.append(hashMap.get("words"));
            }
            resultBody.success(Arrays.asList(stringBuilder.toString()));
            return resultBody;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultBody;
    }
}
