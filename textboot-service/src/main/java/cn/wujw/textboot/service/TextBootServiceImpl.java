package cn.wujw.textboot.service;

import cn.wujw.textboot.common.AliyunOssUtils;
import cn.wujw.textboot.common.RedisCacheManager;
import cn.wujw.textboot.common.StringUtil;
import cn.wujw.textboot.enums.FileSuffix;
import cn.wujw.textboot.factory.ExcelRuleBuilder;
import cn.wujw.textboot.enums.DataLocation;
import cn.wujw.textboot.model.ResultBody;
import cn.wujw.textboot.support.ExcelExportConver;
import cn.wujw.textboot.support.ExcelImportConver;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
}
