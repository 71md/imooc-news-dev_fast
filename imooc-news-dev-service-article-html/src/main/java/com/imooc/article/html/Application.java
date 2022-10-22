package com.imooc.article.html;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = "com.imooc.article.mapper")
@ComponentScan(basePackages = {"com.imooc","org.n3r.idworker"})
//由于springboot自动配置了mongodb,项目启动会自动实例化mongodb实例，
// 故下面注解就是禁用此自动配置
//@SpringBootApplication //(exclude = MongoAutoConfiguration.class)
@SpringBootApplication(exclude=DataSourceAutoConfiguration.class) //(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
