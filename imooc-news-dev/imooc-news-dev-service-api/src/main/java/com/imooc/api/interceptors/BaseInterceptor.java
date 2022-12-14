package com.imooc.api.interceptors;

import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.utils.RedisOperator;
import io.swagger.annotations.ApiOperation;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class BaseInterceptor {

    @Autowired
    public RedisOperator redis;

    public static final String REDIS_ADMIN_TOKEN = "redis_admin_token";
    public static final String REDIS_USER_TOKEN = "redis_user_token";
    public static final String REDIS_USER_INFO = "redis_user_info";


    public boolean verifyUserIdToken(String id,String token,String redisKeyPrefix){

        if(StringUtils.isNotBlank(id)&&StringUtils.isNotBlank(token)){
            String redisToken = redis.get(redisKeyPrefix + ":" + id);
            if (StringUtils.isBlank(redisToken)) {
                GraceException.display(ResponseStatusEnum.UN_LOGIN);
                return false;
            }else{
                if(!redisToken.equals(token)){
                    GraceException.display(ResponseStatusEnum.TICKET_INVALID);
                    return false;
                }
            }
        }else{
            GraceException.display(ResponseStatusEnum.UN_LOGIN);
            return false;
        }
        return true;
    }

    // 从cookie中取值
    public String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(key)){
                String value = cookie.getValue();
                return value;
            }
        }
        return null;
    }

}
