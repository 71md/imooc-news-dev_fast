package com.imooc.user;

import com.rule.MyRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;


@MapperScan(basePackages = "com.imooc.user.mapper")
@ComponentScan(basePackages = {"com.imooc","org.n3r.idworker"})
//由于springboot自动配置了mongodb,项目启动会自动实例化mongodb实例，
// 故下面注解就是禁用此自动配置
@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@EnableEurekaClient     // 开启eureka client，注册到server中
@EnableCircuitBreaker   // 开启hystrix的熔断机制
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
