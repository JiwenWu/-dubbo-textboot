package cn.wujw.textboot.service;

import cn.wujw.textboot.common.AliyunOssUtils;
import cn.wujw.textboot.common.FileUtils;
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
    public String ping() {
        return "ping success";
    }

    @Override
    public ResultBody urlToExcelData(int startIndex, String fileUrl, DataLocation dataLocation) {
        ResultBody resultBody = new ResultBody();

        ExcelImportConver excelImportConver = new ExcelImportConver(startIndex,fileUrl);
        try {
            excelImportConver.converData();
            switch (dataLocation){
                case SYNC:
                    resultBody.success(excelImportConver.getDataList());
                    break;
                case REDIS:
                    String redisKey = UUID.randomUUID().toString().replace("-","");
                    resultBody.success(redisKey);
                    redisCacheManager.set(redisKey,excelImportConver.getDataList(),3600);
                    logger.info(">>>>>>>>> 存入redis,key{}",redisKey);
                    break;
                default:
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            resultBody.error(e.getMessage());
        }

        return resultBody;
    }

    @Override
    public ResultBody urlToValidateExcelData(int startIndex, String fileUrl, ExcelRuleBuilder builder, DataLocation dataLocation) {
        ResultBody resultBody = new ResultBody();
        ExcelImportConver excelImportConver = new ExcelImportConver(startIndex,fileUrl,builder);
        try {
            excelImportConver.converData();
            if (CollectionUtils.isNotEmpty(excelImportConver.getErrorEntityList())){
                resultBody.failed(excelImportConver.getErrorEntityList());
                return resultBody;
            }
            switch (dataLocation){
                case SYNC:
                    resultBody.success(excelImportConver.getBuildDataList());
                    break;
                case REDIS:
                    String redisKey = UUID.randomUUID().toString().replace("-","");
                    resultBody.success(redisKey);
                    redisCacheManager.set(redisKey,excelImportConver.getBuildDataList(),3600);
                    logger.info(">>>>>>>>> 存入redis,key{}",redisKey);
                    break;
                default:
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            resultBody.error(e.getMessage());
        }
        return resultBody;
    }

    @Override
    public ResultBody exportExcel(String[] headData, List<List<String>> listData) {
        ResultBody resultBody = new ResultBody();

        ExcelExportConver excelExportConver = new ExcelExportConver(listData,headData);
        InputStream inputStream = excelExportConver.commonExport();

        String url = aliyunOssUtils.uploadFileStream(inputStream, FileSuffix.XLSX);
        if (!StringUtil.isBlank(url)){
            List<String> list = new ArrayList<>();
            list.add(url);
            resultBody.success(list);
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
            List<String> list = new ArrayList<>();
            list.add(url);
            resultBody.success(list);
        }
        return resultBody;
    }

    public ResultBody ocrImage(String url){
        ResultBody resultBody = new ResultBody();
        String uuid = UUID.randomUUID().toString().replace("-","");
        aliyunOssUtils.downloadFile(url,uuid);
        try {
            byte[] bytes = FileUtils.readFileByBytes(uuid);
            HashMap<String, String> options = new HashMap<>();
            options.put("language_type", "CHN_ENG");
            options.put("detect_direction", "true");
            options.put("detect_language", "true");
            options.put("probability", "true");
            System.out.println(aipOcr.toString());

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
