package com.imooc.user.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.api.controller.user.UserControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.pojo.vo.AppUserVO;
import com.imooc.pojo.vo.UserAccountInfoVO;
import com.imooc.user.service.UserService;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import io.lettuce.core.dynamic.annotation.Value;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

;import java.util.Map;

@RestController
public class UserController extends BaseController implements UserControllerApi {

    final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Override
    public GraceJSONResult getUserInfo(String userId) {
        // 0. 判断参数不能为空
        if(StringUtils.isBlank(userId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }

        // 1. 根据userId 查询用户的信息
        AppUser user = getUser(userId);

        // 2. 返回用户信息
        AppUserVO userVO = new AppUserVO();
        BeanUtils.copyProperties(user,userVO);

        return GraceJSONResult.ok(userVO);
    }

    public AppUser getUser(String userId){
        //查询判断redis中是否包含用户信息，如果包含，则查查询后直接返回，就不去查询数据库了
        String userJson = redis.get(REDIS_USER_INFO + ":" + userId);
        AppUser user = null;
        if(StringUtils.isNotBlank(userJson)){
            user = JsonUtils.jsonToPojo(userJson,AppUser.class);
        }else{
            // TODO 本方法后续共用，并且扩展
            user = userService.getUser(userId);

            //由于用户信息不常变动，对于一些千万几倍的网站来说，这类信息不会直接去长训数据库
            //那么完全可以依靠redis，直接把查询后的数据存到redis中
            redis.set(REDIS_USER_INFO + ":" + userId, JsonUtils.objectToJson(user));
        }

        return user;
    }

    @Override
    public GraceJSONResult getAccountInfo(String userId) {

        //判断参数不能为空
        if(StringUtils.isBlank(userId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }

        // 1.根据用户id查询用户的信息
        //AppUser user = userService.getUser(userId);
        AppUser user = getUser(userId);

        // 2. 返回用户信息
        UserAccountInfoVO accountInfoVO = new UserAccountInfoVO();
        BeanUtils.copyProperties(user,accountInfoVO);

        return GraceJSONResult.ok(accountInfoVO);

    }

    @Override
    public GraceJSONResult updateUserInfo(UpdateUserInfoBO updateUserInfoBO){
//            , BindingResult result) {
//
//        // 0. 校验BO
//        if(result.hasErrors()){
//            Map<String, String> map = getError(result);
//            return GraceJSONResult.errorMap(map);
//
//        }

        // 1. 执行更新操作
        userService.updateUserIfo(updateUserInfoBO);
        return GraceJSONResult.ok();

    }

}
