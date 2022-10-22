package com.imooc.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.imooc.utils.extend.AliyunResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SMSUtils<SendSmsRequest> {

    @Autowired
    public AliyunResource aliyunResource;

    public void sendSMS(String mobile,String code){

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",
                                                aliyunResource.getAccessKeyID(),
                                                aliyunResource.getAccessKeySecret());
        /** use STS Token
         DefaultProfile profile = DefaultProfile.getProfile(
         "<your-region-id>",           // The region ID
         "<your-access-key-id>",       // The AccessKey ID of the RAM account
         "<your-access-key-secret>",   // The AccessKey Secret of the RAM account
         "<your-sts-token>");          // STS Token
         **/

        IAcsClient client = new DefaultAcsClient(profile);

        //SendSmsRequest request = new SendSmsRequest();

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-05");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId","cn-hangzhou");

        request.putQueryParameter("PhoneNumbers",mobile);
        request.putQueryParameter("SignName","风间影月");
        request.putQueryParameter("TemplateCode","SMS_183761535");
        request.putQueryParameter("TemplateParam","{\"code\":\'"+ code +"\"}");

        try {
            //SendSmsResponse response =
            CommonResponse response = client.getCommonResponse(request);
            //System.out.println(new Gson().toJson(response));
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
//            System.out.println("ErrCode:" + e.getErrCode());
//            System.out.println("ErrMsg:" + e.getErrMsg());
//            System.out.println("RequestId:" + e.getRequestId());
        }

    }

    }
