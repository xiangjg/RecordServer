package com.jone.record.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class RedisDao {
    @Autowired
    private StringRedisTemplate template;
    public void setKey(String key,String value){
        ValueOperations<String,String> ops = template.opsForValue();
        ops.set(key,value);
    }
    public String getValue(String key){
        ValueOperations<String,String> ops = template.opsForValue();
        String value = "";
        if(template.hasKey(key)){
            value = ops.get(key);
        }
        return value;
    }
    public void setKey(String key,String value,long timeout, TimeUnit unit){
        ValueOperations<String,String> ops = template.opsForValue();
        ops.set(key,value,timeout,unit);
    }
    public void resetTime(String key,Integer time){
        template.expire(key,time,TimeUnit.HOURS);
    }
    public void sendMessage(String channel,String message){
        synchronized (this){
            template.convertAndSend(channel,message);
        }
    }
}
