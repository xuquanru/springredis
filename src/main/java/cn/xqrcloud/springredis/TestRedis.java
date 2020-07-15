package cn.xqrcloud.springredis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌道阻且长，行则将至🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌
 * 🍁 Program: springredis
 * 🍁 Description
 * 🍁 Author: Stephen
 * 🍁 Create: 2020-07-14 23:48
 * 🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌行而不辍，未来可期🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌
 **/
@Component
public class TestRedis {
    //默认序列化，会加一些东西进去
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
            @Qualifier("getTemplate")
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    ObjectMapper objectMapper;


    public void testRedis(){
        redisTemplate.opsForValue().set("hello", "china");
        System.out.println(redisTemplate.opsForValue().get("hello"));
        stringRedisTemplate.opsForValue().set("gogo", "good");
        System.out.println(stringRedisTemplate.opsForValue().get("gogo"));

        stringRedisTemplate.opsForHash().put("sean", "age", "18");
        stringRedisTemplate.opsForHash().put("sean", "address", "address");
        stringRedisTemplate.opsForHash().put("sean", "name", "xuqr");
        System.err.println(stringRedisTemplate.opsForHash().get("sean", "age"));

    }

    public  void testRedis2(){
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        connection.set("girl".getBytes(), "ly".getBytes());
        System.err.println(new String(connection.get("girl".getBytes())));
    }

    public  void testRedis3(){
        Person person = new Person();
        person.setAge(18);
        person.setName("xuqr");
        Jackson2HashMapper jackson2HashMapper = new Jackson2HashMapper(objectMapper, false);
        redisTemplate.opsForHash().putAll("dog",jackson2HashMapper.toHash(person));
        Map dog = redisTemplate.opsForHash().entries("dog");
        Person personback = objectMapper.convertValue(dog, Person.class);
        System.err.println(personback);

    }

    public  void testRedis4(){
        Person person = new Person();
        person.setAge(19);
        person.setName("xuqr");
        //在总配文件已经改了
        //stringRedisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        Jackson2HashMapper jackson2HashMapper = new Jackson2HashMapper(objectMapper, false);
        stringRedisTemplate.opsForHash().putAll("dog1",jackson2HashMapper.toHash(person));
        Map dog = stringRedisTemplate.opsForHash().entries("dog1");
        Person personback = objectMapper.convertValue(dog, Person.class);
        System.err.println(personback);

    }
    public  void testRedis5(){


        RedisConnection connection = stringRedisTemplate.getConnectionFactory().getConnection();
        connection.subscribe(new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                byte[] body = message.getBody();
                System.err.println(new String(body));
            }
        }, "channel1".getBytes());

        while (true) {
            stringRedisTemplate.convertAndSend("channel1", "hello");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
