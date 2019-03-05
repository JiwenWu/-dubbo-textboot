package cn.wujw.textboot.test;

import cn.wujw.textboot.common.AliyunOssUtils;
import cn.wujw.textboot.common.RedisCacheManager;
import cn.wujw.textboot.enums.FileSuffix;
import cn.wujw.textboot.support.ExcelImportConver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * Desc:
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-22
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/spring/application.xml"})
public class BasicJunitTest {

    @Autowired(required = false)
    private RedisCacheManager redisCacheManager;
    @Autowired
    private AliyunOssUtils aliyunOssUtils;

    @Test
    public void test() {
        try {
            InputStream inputStream = new FileInputStream("/Users/wujw/Desktop/demo.xlsx");

            String s = aliyunOssUtils.uploadFileStream(inputStream, FileSuffix.XLSX);

            System.out.println(s);
//            String fileName = UUID.randomUUID().toString().replace("-","");
//
//            aliyunOssUtils.downloadFile(s,fileName);
//            ExcelImportConver excelImportConver = new ExcelImportConver(0,s);
//            System.out.println(excelImportConver);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
