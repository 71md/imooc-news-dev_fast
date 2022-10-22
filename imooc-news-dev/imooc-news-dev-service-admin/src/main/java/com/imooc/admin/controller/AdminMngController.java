package com.imooc.admin.controller;

import com.imooc.admin.service.AdminUserService;
import com.imooc.api.BaseController;
import com.imooc.api.controller.admin.AdminMngControllerApi;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RestController;

;import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
public class AdminMngController extends BaseController implements AdminMngControllerApi {

    final static Logger logger = LoggerFactory.getLogger(AdminMngController.class);

    @Autowired
    private RedisOperator redis;

    @Autowired
    private AdminUserService adminUserService;

    @Override
    public GraceJSONResult adminLogin(AdminLoginBO adminLoginBO,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        // 0. TODO 验证BO中的用户名和密码你为空

        // 1. 查询admin用户的信息
        AdminUser admin = adminUserService.queryAdminByUsername(adminLoginBO.getUsername());
        // 2. 判断admin不为空，如果为空则登陆失败
        if (admin == null) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
        // 3. 判断密码是否匹配
        boolean isPwdMatch = BCrypt.checkpw(adminLoginBO.getPassword(), admin.getPassword());
        if (isPwdMatch) {
            doLoginSettings(admin,request,response);
            return GraceJSONResult.ok();
        } else {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
    }

    /**
     * 用于admin用户登录过后的基本信息设置
     * @param admin
     * @param request
     * @param response
     */
    private void doLoginSettings(AdminUser admin,
                                        HttpServletRequest request,
                                        HttpServletResponse response){
        //保存token放入到redis中
        String token = UUID.randomUUID().toString();
        redis.set(REDIS_ADMIN_TOKEN + ":" + admin.getId(),token);

        // 保存admin登录基本token信息到cookie中
        setCookie(request,response,"atoken",token,COOKIE_MONTH);
        setCookie(request,response,"aid",admin.getId(),COOKIE_MONTH);
        setCookie(request,response,"aname",admin.getAdminName(),COOKIE_MONTH);
    }

    @Override
    public GraceJSONResult adminIsExist(String username) {
        checkAdminExist(username);
        return GraceJSONResult.ok();
    }

    private void checkAdminExist(String username){
        AdminUser admin = adminUserService.queryAdminByUsername(username);

        if(admin != null){
            GraceException.display(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
        }
    }

    @Override
    public GraceJSONResult addNewAdmin(NewAdminBO newAdminBO, HttpServletRequest request, HttpServletResponse response) {

        //0. TODO 验证BO中的用户名和密码不为空

        //1. base64不为空，则代表人脸入库，否则需要用户输入密码和确认密码
        if(StringUtils.isBlank(newAdminBO.getImg64())){
            if(StringUtils.isBlank(newAdminBO.getPassword()) ||
                StringUtils.isBlank(newAdminBO.getConfirmPassword())){
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
            }
        }

        //2. 密码不为空，则必须判断两次输入一致
        if(StringUtils.isNotBlank(newAdminBO.getPassword())){
            if(!newAdminBO.getPassword().equalsIgnoreCase(newAdminBO.getConfirmPassword())){
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
            }
        }

        //3. 校验用户名唯一
        checkAdminExist(newAdminBO.getUsername());

        // 调用service,存入admin信息
        adminUserService.createAdminUser(newAdminBO);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getAdminList(Integer page, Integer pageSize) {

        if(page == null){
            page = COMMON_START_PAGE;
        }
        if(pageSize == null){
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult result = adminUserService.queryAdminList(page,pageSize);

        return GraceJSONResult.ok(result);
    }

    @Override
    public GraceJSONResult adminLogout(String adminId, HttpServletRequest request, HttpServletResponse response) {

        //1. 从redis中删除admin的会话token
        redis.del(REDIS_ADMIN_TOKEN + ":" + adminId);

        //从cookle中删除县官信息
        deleteCoolie(request,response,"atoken");
        deleteCoolie(request,response,"aid");
        deleteCoolie(request,response,"aname");
        return GraceJSONResult.ok();
    }
}
