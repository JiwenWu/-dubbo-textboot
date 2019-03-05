package cn.wujw.textboot.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Desc: 正则缓存
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-20
 */
public class RegexUtil {
    private RegexUtil (){}


    private static final String NUMBER = "[^0-9]";

    private final static LoadingCache<String, Pattern> LOAD_CACHE =
            CacheBuilder.newBuilder()
                    .maximumSize(30)
                    .build(new CacheLoader<String, Pattern>() {
                        @Override
                        public Pattern load(String pattern) {
                            return Pattern.compile(pattern);
                        }
                    });

    public static Boolean isMatch(String pattern, String value){
        try {
            return LOAD_CACHE.get(pattern).matcher(value).matches();
        } catch (ExecutionException e) {
            return false;
        }
    }

    public static String removeNum(String pattern,String cellName) throws ExecutionException {
        Matcher m = LOAD_CACHE.get(pattern).matcher(cellName);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }

    public static Integer removeLetter(String cellName){
        try {
            Matcher m = LOAD_CACHE.get(NUMBER).matcher(cellName);
            String num =  m.replaceAll("").trim();
            return Integer.valueOf(num);
        } catch (ExecutionException e) {
            return 0;
        }
    }
}
