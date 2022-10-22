package com.imooc.api.controller.files;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.NewAdminBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value="文件上传Controller",tags = {"文件上传Controller"})
@RequestMapping("/fs")
public interface FileUploaderControllerApi {

    @ApiOperation(value = "上传用户头像",notes = "上传用户头像",httpMethod = "POST")
    @PostMapping("/uploadFace")
    public GraceJSONResult uploadFace(@RequestParam String userId,
                                      MultipartFile file) throws Exception;

    /**
     * 文件上传到mongodb的gridds中
     */
    @PostMapping("/uploadToGridFS")
    public GraceJSONResult uploadToGridFs(@RequestBody NewAdminBO newAdminBO)
            throws Exception;

    /**
     * 从gridfs中获得读取图片内容
     * @param faceId
     * @return
     * @throws Exception
     */
    @GetMapping("/readInGridFS")
    public void readInGridFS(String faceId,
                                        HttpServletRequest request,
                                        HttpServletResponse response)
            throws Exception;
}
