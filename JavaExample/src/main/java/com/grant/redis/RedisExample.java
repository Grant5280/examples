package com.grant.redis;

import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Created by Grant on 2017/12/14/014.
 */
public class RedisExample {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.40.14",7008,3000);
        Set<String> keys = jedis.keys("*");
        for (String key: keys) {
            System.out.println(key);
        }
    }
}
