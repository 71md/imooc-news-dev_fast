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

    /**
     * 上传单文件
     * @param userId
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadFace")
    public GraceJSONResult uploadFace(@RequestParam String userId,
                                      MultipartFile file) throws Exception;

    /**
     * 上传多个文件
     * @param userId
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadSomeFiles")
    public GraceJSONResult uploadSomeFiles(@RequestParam String userId,
                                           MultipartFile[] files) throws Exception;


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

    /**
     * 从gridfs中读取图片内容，并且返回base64
     * @param faceId
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping("/readFace64InGridFS")
    public GraceJSONResult readFace64InGridFS(String faceId,
                                              HttpServletRequest request,
                                              HttpServletResponse response)
            throws Exception;
}
