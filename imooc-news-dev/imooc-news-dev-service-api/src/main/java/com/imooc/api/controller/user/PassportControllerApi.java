package com.imooc.api.controller.user;

import com.imooc.api.config.Swagger2;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.RegistLoginBO;
import io.lettuce.core.dynamic.annotation.Value;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.cookie.SM;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value="用户注册登录",tags = {"用户注册登录的controller"})
@RequestMapping("/passport")
public interface PassportControllerApi {

    /**
     * 获得短信验证码
     * @return
     */
    @ApiOperation(value = "获得短信验证码",notes = "获得短信验证码",httpMethod = "GET")
    @GetMapping("/getSMSCode")
    public GraceJSONResult getSMSCode(@RequestParam String mobile, HttpServletRequest request);

    @ApiOperation(value = "一键注册登录接口",notes = "一键注册登录接口",httpMethod = "POST")
    @PostMapping("/doLogin")
    public GraceJSONResult doLogin(@RequestBody @Value RegistLoginBO registLoginBO,
//                                   BindingResult result,
                                   HttpServletRequest request,
                                   HttpServletResponse response);

    @ApiOperation(value = "用户退出登录",notes = "用户退出登录",httpMethod = "POST")
    @PostMapping("/logout")
    public GraceJSONResult logout(@RequestParam String userId,
                                   HttpServletRequest request,
                                   HttpServletResponse response);
}
