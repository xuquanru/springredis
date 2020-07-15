package cn.xqrcloud.springredis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringredisApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(SpringredisApplication.class, args);
        TestRedis redis = ctx.getBean(TestRedis.class);
        redis.testRedis2();
    }

}
