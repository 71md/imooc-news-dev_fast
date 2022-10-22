package com.imooc.user.mapper;

import com.imooc.my.mapper.MyMapper;
import com.imooc.pojo.AppUser;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

@Repository
public interface AppUserMapper extends MyMapper<AppUser> {
}