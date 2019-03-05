package cn.wujw.textboot.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Desc: 日期转换缓存
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-20
 */
public class DateFormatUtil {
    private DateFormatUtil () {}

    private final static LoadingCache<String, SimpleDateFormat> LOADING_CACHE =
            CacheBuilder.newBuilder()
                    .maximumSize(5)
                    .build(new CacheLoader<String, SimpleDateFormat>() {
                        @Override
                        public SimpleDateFormat load(String s) throws Exception {
                            return new SimpleDateFormat(s);
                        }
                    });

    public static Date parse(String pattern,String value) throws ExecutionException, ParseException {
        return LOADING_CACHE.get(pattern).parse(value);
    }

    public static String format(String pattern,Date value) throws ExecutionException {
        return LOADING_CACHE.get(pattern).format(value);
    }
}
