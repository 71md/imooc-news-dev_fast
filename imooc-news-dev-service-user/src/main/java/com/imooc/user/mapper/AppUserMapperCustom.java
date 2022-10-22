package com.imooc.user.mapper;

import com.imooc.pojo.vo.PublisherVO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AppUserMapperCustom {

    public List<PublisherVO> getUserList(Map<String, Object> map);

}