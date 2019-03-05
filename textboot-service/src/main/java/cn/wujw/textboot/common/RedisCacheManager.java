package cn.wujw.textboot.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Desc: Redis缓存操作
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-25
 */
@Component
public class RedisCacheManager {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key,Object value){
        redisTemplate.opsForValue().set(key,value);
    }

    public void set(String key,Object value,long time){
        redisTemplate.opsForValue().set(key,value,time,TimeUnit.SECONDS);
    }

    public boolean expire(String key,long time,TimeUnit timeUnit){
        return redisTemplate.expire(key,time,timeUnit);
    }
}
