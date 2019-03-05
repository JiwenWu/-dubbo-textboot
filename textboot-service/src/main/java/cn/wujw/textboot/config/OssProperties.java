package cn.wujw.textboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Desc:
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-22
 */
@Component
@PropertySource(value = "classpath:conf/oss.properties")
public class OssProperties {

    @Value("${aliyun.oss.endpoint}")
    private String endPoint;

    @Value("${aliyun.oss.bucket}")
    private String bucket;

    @Value("${aliyun.oss.access.keyId}")
    private String keyId;

    @Value("${aliyun.oss.access.keySecret}")
    private String keySecret;

    @Value("${aliyun.oss.upload.type}")
    private String type;

    @Value("${aliyun.oss.region}")
    private String region;

    @Value("${aliyun.oss.sts_api_versin}")
    private String version;

    @Value("${aliyun.oss.token.expire}")
    private int expire;

    @Value("${aliyun.oss.content.min}")
    private int min;

    @Value("${aliyun.oss.content.max}")
    private int max;

    @Value("${aliyun.oss.tempPath}")
    private String tempPath;
    @Value("${aliyun.oss.domain}")
    private String domain;

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getKeySecret() {
        return keySecret;
    }

    public void setKeySecret(String keySecret) {
        this.keySecret = keySecret;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getTempPath() {
        return tempPath;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    public String getDomain() {
        return domain;
    }

    public OssProperties setDomain(String domain) {
        this.domain = domain;
        return this;
    }
}
