package com.imooc.api.config;

import com.imooc.api.interceptors.AdminTokenInterceptor;
import com.imooc.api.interceptors.PassportInterceptor;
import com.imooc.api.interceptors.UserActiveInterceptor;
import com.imooc.api.interceptors.UserTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public PassportInterceptor passportInterceptor(){
        return new PassportInterceptor();
    }

    @Bean
    public UserTokenInterceptor UserTokenInterceptor(){
        return new UserTokenInterceptor();
    }

    @Bean
    public AdminTokenInterceptor AdminTokenInterceptor(){
        return new AdminTokenInterceptor();
    }

    @Bean
    public UserActiveInterceptor UserActiveInterceptor(){
        return new UserActiveInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(passportInterceptor())
                .addPathPatterns("/passport/getSMSCode");

        registry.addInterceptor(UserTokenInterceptor())
                .addPathPatterns("/user/getAccountInfo")
                .addPathPatterns("/user/updateUserInfo")
                .addPathPatterns("/fs/uploadFace");

//        registry.addInterceptor(UserActiveInterceptor())
//                .addPathPatterns("/user/getAccountInfo");

        registry.addInterceptor(AdminTokenInterceptor())
                .addPathPatterns("/adminMng/adminIsExist")
                .addPathPatterns("/adminMng/addNewAdmin")
                .addPathPatterns("/adminMng/getAdminList")
                .addPathPatterns("/fs/uploadToGridFS")
                .addPathPatterns("/fs/readInGridFS");
    }
}
