package com.imooc.user.service.impl;

import com.imooc.enums.Sex;
import com.imooc.enums.UserStatus;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.pojo.vo.PublisherVO;
import com.imooc.user.mapper.AppUserMapper;
import com.imooc.user.mapper.AppUserMapperCustom;
import com.imooc.user.service.UserService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.DesensitizationUtil;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    public AppUserMapper appUserMapper;

    @Autowired
    public AppUserMapperCustom appUserMapperCustom;

    @Autowired
    public Sid sid;

    @Autowired
    public RedisOperator redis;
    public static final String REDIS_USER_INFO = "redis_user_info";

    private static final String USER_FACE0 = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";
    private static final String USER_FACE1 = "http://122.152.205.72:88/group1/M00/00/05/CpoxxF6ZUySASMbOAABBAXhjY0Y649.png";
    private static final String USER_FACE2 = "http://122.152.205.72:88/group1/M00/00/05/CpoxxF6ZUx6ANoEMAABTntpyjOo395.png";


    @Override
    public AppUser queryMobileIsExist(String mobile) {

        Example userExample = new Example(AppUser.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("mobile",mobile);
        AppUser user = appUserMapper.selectOneByExample(userExample);
        return user;
    }

    @Transactional
    @Override
    public AppUser createUser(String mobile) {

        String userId = sid.nextShort();
        /**
         * ???????????????????????????????????????
         * ?????????????????????????????????????????????????????????
         * ??????????????????id???????????????????????????????????????????????????
         */
        AppUser user = new AppUser();

        user.setId(userId);
        user.setMobile(mobile);
        //DesensitizationUtil:??????
        user.setNickname("?????????" + DesensitizationUtil.commonDisplay(mobile));
        user.setFace(USER_FACE0);

        user.setBirthday(DateUtil.stringToDate("1900-.1-.1"));
        user.setSex(Sex.secret.type);
        user.setActiveStatus(UserStatus.INACTIVE.type);

        user.setTotalIncome(0);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        appUserMapper.insert(user);
        return user;
    }

    @Override
    public AppUser getUser(String userId) {

        return appUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public void updateUserIfo(UpdateUserInfoBO updateUserInfoBO) {

        String userId = updateUserInfoBO.getId();
        //??????????????????????????????redis?????????????????????????????????
        redis.del(REDIS_USER_INFO + ":" + userId);

        AppUser userInfo = new AppUser();
        BeanUtils.copyProperties(updateUserInfoBO,userInfo);

        userInfo.setUpdatedTime(new Date());
        userInfo.setActiveStatus(userInfo.getActiveStatus());

        int result = appUserMapper.updateByPrimaryKeySelective(userInfo);
        if(result != 1){
            GraceException.display(ResponseStatusEnum.USER_UPDATE_ERROR);
        }

        //??????????????????????????????????????????redis???
        AppUser user = getUser(userId);
        redis.set(REDIS_USER_INFO + ":" + userId, JsonUtils.objectToJson(user));

        //??????????????????
        try {
            Thread.sleep(100);
            redis.del(REDIS_USER_INFO + ":" + userId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<PublisherVO> getUserList(List<String> userIdList) {

        Map<String, Object> map = new HashMap<>();
        map.put("userIdList", userIdList);
        List<PublisherVO> publisherList = appUserMapperCustom.getUserList(map);

        return publisherList;
    }
}
