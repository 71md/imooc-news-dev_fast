package com.imooc.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import redis.clients.jedis.Jedis;
import tk.mybatis.spring.annotation.MapperScan;


@MapperScan(basePackages = "com.imooc.user.mapper")
@ComponentScan(basePackages = {"com.imooc","org.n3r.idworker"})
//由于springboot自动配置了mongodb,项目启动会自动实例化mongodb实例，
// 故下面注解就是禁用此自动配置
@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
