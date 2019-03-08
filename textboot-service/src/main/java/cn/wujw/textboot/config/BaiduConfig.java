package cn.wujw.textboot.config;

import com.baidu.aip.ocr.AipOcr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Desc:
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-03-02
 */
@Configuration
public class BaiduConfig {
    @Value("${baidu.ocr.app_id}")
    private String appId;
    @Value("${baidu.ocr.api_key}")
    private String apiKey;
    @Value("${baidu.ocr.secret_key}")
    private String secretKey;

    @Bean
    public AipOcr aipOcr(){
        AipOcr aipOcr = new AipOcr(appId,apiKey,secretKey);
        return  aipOcr;
    }
}
