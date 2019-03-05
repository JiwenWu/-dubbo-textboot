package cn.wujw.textboot.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;

/**
 * Desc: excel列名转数字，A-1 ,B-2 ,.... ,AA-27
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-21
 */
public class Letter2Number {
    private final static LoadingCache<String, Integer> LOADING_CACHE =
            CacheBuilder.newBuilder()
                    .maximumSize(100)
                    .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String letter) throws Exception {
                        return letterToNumber(letter);
                    }
    });

    private Letter2Number() {
    }

    private static Integer letterToNumber(String letter){
        int result = 0;
        letter = letter.toUpperCase();
        for (int i = 0; i < letter.length(); i++) {
            char c = letter.charAt(i);
            int num = c - 'A' + 1;
            result = result * 26 + num;
        }
        return result;
    }

    public static Integer getNumber(String letter){
        try {
          return LOADING_CACHE.get(letter);
        } catch (ExecutionException e) {
            return 0;
        }
    }
}
