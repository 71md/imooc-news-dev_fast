package com.imooc.api.controller.user;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.RegistLoginBO;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import io.lettuce.core.dynamic.annotation.Value;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(value="用户信息相关Controller",tags = {"用户信息相关Controller"})
@RequestMapping("/user")
public interface UserControllerApi {

    @ApiOperation(value = "获得用户基本信息", notes = "获得用户基本信息", httpMethod = "POST")
    @PostMapping("/getUserInfo")
    public GraceJSONResult getUserInfo(@RequestParam String userId);

    /**
     * 获得短信验证码
     */
    @ApiOperation(value = "获得用户账户信息",notes = "获得用户账户信息",httpMethod = "POST")
    @PostMapping("/getAccountInfo")
    public GraceJSONResult getAccountInfo(@RequestParam String userId);

    @ApiOperation(value = "修改/完善用户信息", notes = "修改/完善用户信息", httpMethod = "POST")
    @PostMapping("/updateUserInfo")
    public GraceJSONResult updateUserInfo(
            @RequestBody @Valid UpdateUserInfoBO updateUserInfoBO);
//            BindingResult result);

}
