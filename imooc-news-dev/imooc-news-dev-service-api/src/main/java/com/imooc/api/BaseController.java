package com.imooc.api;

import com.imooc.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BaseController {

    @Autowired
    public RedisOperator redis;

    public static final String MOBILE_SMSCODE = "mobile:smscode";
    public static final String REDIS_USER_TOKEN = "redis_user_token";
    public static final String REDIS_ADMIN_TOKEN = "redis_admin_token";
    public static final String REDIS_USER_INFO = "redis_user_info";


    @Value("${website.domain-name}")
    public String DOMAIN_NAME;
    public static final Integer COOKIE_MONTH = 30 * 24 * 60 * 60;
    public static final Integer COOKIE_DELETE = 0;

    public static final Integer COMMON_START_PAGE = 1;
    public static final Integer COMMON_PAGE_SIZE = 10;

    public void setCookie(HttpServletRequest request,
                          HttpServletResponse response,
                          String cookieName,
                          String cookieValue,
                          Integer maxAge) {
        try {
            cookieValue = URLEncoder.encode(cookieValue, "utf-8");
            setCookieValue(request, response, cookieName, cookieValue, maxAge);
//            Cookie cookie = new Cookie(cookieName, cookieValue);
//            cookie.setMaxAge(maxAge);
//            cookie.setDomain("imoocnews.com");
//            cookie.setPath("/");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public void setCookieValue(HttpServletRequest request,
                               HttpServletResponse response,
                               String cookieName,
                               String cookieValue,
                               Integer maxAge) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(maxAge);
//        cookie.setDomain("imoocnews.com");
//        cookie.setDomain("192.168.137.1");

        cookie.setDomain(DOMAIN_NAME);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 验证BO中的错误信息
     * @param result
     */
    public Map<String,String> getError(BindingResult result){
        Map<String,String> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for(FieldError error:errorList){
            //发生验证错误的时候所对应的某个属性
            String field = error.getField();
            //验证的错误消息
            String msg = error.getDefaultMessage();
            map.put(field,msg);
        }
        return map;
    }

    public void deleteCoolie(HttpServletRequest request,
                             HttpServletResponse response,String cookieName){
        try {
            String daleteValue = URLEncoder.encode("","utf-8");
            setCookieValue(request,response,cookieName,"daleteValue",COOKIE_DELETE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
