package com.imooc.exception;

import com.imooc.grace.result.ResponseStatusEnum;


/**
 * 自定义异常
 * 目的：统一处理异常信息
 *      便于解耦，server与controller错误的解耦，不会被server返回的类型而限制
 */
public class MyCustomException extends RuntimeException{

    public ResponseStatusEnum getResponseStatusEnum() {
        return responseStatusEnum;
    }

    public void setResponseStatusEnum(ResponseStatusEnum responseStatusEnum) {
        this.responseStatusEnum = responseStatusEnum;
    }

    private ResponseStatusEnum responseStatusEnum;

    public MyCustomException(ResponseStatusEnum responseStatusEnum){
        super("异常状态码为:" + responseStatusEnum.status() +
                ";具体异常信息为:" + responseStatusEnum.msg());
        this.responseStatusEnum = responseStatusEnum;
    }



}
