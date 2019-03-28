package cn.wujw.textboot.common;

import cn.wujw.textboot.config.OssProperties;
import cn.wujw.textboot.enums.FileSuffix;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;

/**
 * Desc: 阿里云Oss操作工具
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-22
 */
@Configuration
public class AliyunOssUtils {

    @Autowired
    private OssProperties ossProperties;

    private static final String OSS_FORMAT_HOST = "https://%s.%s";

    /**
     * oss 文件下载
     * @param fileUrl
     * @param fileName
     */
    public void downloadFile(String fileUrl,String fileName){
        OSSClient ossClient = new OSSClient(ossProperties.getEndPoint(),ossProperties.getKeyId(),ossProperties.getKeySecret());
        String ossHttpsHost = String.format(OSS_FORMAT_HOST + "/", ossProperties.getBucket(), ossProperties.getEndPoint());
        String fileKey = fileUrl.replace(ossHttpsHost, "");
        ossClient.getObject(new GetObjectRequest(ossProperties.getBucket(),fileKey), new File(fileName));
        ossClient.shutdown();
    }

    public String uploadFileStream(InputStream inputStream, FileSuffix fileSuffix) {
        if (inputStream == null) {
            return null;
        }
        String url = "";
        String filename = getUUID() + "." + fileSuffix.getSuffix();
        String folder = filename.substring(0, 8);
        String key = String.format("export/%s/%s", folder, filename);

        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("image/jpeg");
            OSSClient ossClient = new OSSClient(ossProperties.getEndPoint(),ossProperties.getKeyId(),ossProperties.getKeySecret());
            ossClient.putObject(ossProperties.getBucket(), key, inputStream, objectMetadata);
            // 关闭client
            ossClient.shutdown();
            return getUploadFileUrl(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    private  String getUploadFileUrl(String key) {
        if (key == null) {
            return "";
        }
        if (key.startsWith("http")) {
            return key;
        }else {
           return ossProperties.getDomain() + key;
        }
    }

    /**
     * 生成唯一的UUID
     *
     * @return
     */
    private  String getUUID() {
        return getUniqueNO() + getRandom(4);
    }

    /**
     * 根据当前时间生成唯一值
     *
     * @return
     */
    private String getUniqueNO() {
        java.util.Date curDate = new java.util.Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
        return formatter.format(curDate);
    }

    /**
     * 获取随机数
     *
     * @param length
     *            随机数位数
     * @return
     */
    private String getRandom(int length) {
        StringBuilder strval = new StringBuilder();
        if (length < 4) {
            length = 4;
        }
        for (int nIndex = 0; nIndex < length; nIndex++) {
            strval.append((int) (Math.random() * 10.0D));
        }
        return strval.toString();
    }
}
