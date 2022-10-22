package com.imooc.files;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;


//由于springboot自动配置了mongodb,项目启动会自动实例化mongodb实例，
// 故下面注解就是禁用此自动配置
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})  //(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"com.imooc","org.n3r.idworker"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
