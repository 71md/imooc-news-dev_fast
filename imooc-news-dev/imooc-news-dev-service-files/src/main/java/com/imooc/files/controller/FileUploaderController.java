package com.imooc.files.controller;

import com.imooc.api.controller.files.FileUploaderControllerApi;
import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.exception.GraceException;
import com.imooc.files.FileResource;
import com.imooc.files.service.UploaderService;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.utils.FileUtils;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

;import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;

@RestController
public class FileUploaderController implements FileUploaderControllerApi {

    final static Logger logger = LoggerFactory.getLogger(FileUploaderController.class);

    @Autowired
    private UploaderService uploaderService;

    @Autowired
    private FileResource fileResource;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Override
    public GraceJSONResult uploadFace(String userId, MultipartFile file) throws Exception {

        String path = "";
        if(file != null){
            // 获得文件上传的名称
            String fileName = file.getOriginalFilename();

            // 判断文件名不能为空
            if(StringUtils.isNotBlank(fileName)){
                String[] fileNameArr = fileName.split("\\.");
                // 获得文件后缀
                String suffix = fileNameArr[fileNameArr.length - 1];
                // 判断后缀符合我们的预定义规范
                if (!suffix.equalsIgnoreCase("png") &&
                        !suffix.equalsIgnoreCase("jpg") &&
                        !suffix.equalsIgnoreCase("jpeg")){
                    return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);
                }

                // 执行上传
                path = uploaderService.uploadFdfs(file,suffix);

            }else{
                return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
            }
        }else{
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }
        String finalPath = "";
        if(StringUtils.isNotBlank(path)){
            finalPath = fileResource.getHost() + path;

        }else{
            return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        return GraceJSONResult.ok(finalPath);
    }

    @Override
    public GraceJSONResult uploadToGridFs(NewAdminBO newAdminBO) throws Exception {

        // 获得图片的base64
        String file64 = newAdminBO.getImg64();

        // 将字符串转化为byte[]
        byte[] bytes = new BASE64Decoder().decodeBuffer(file64.trim());
        // 将byte[]转换为输入流
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        // 上传到gridfs中
        ObjectId fileId = gridFSBucket.uploadFromStream(newAdminBO.getUsername() + ".png",byteArrayInputStream);
        // 获得文件在gridid中的主见id
        String fileIdStr = fileId.toString();

        return GraceJSONResult.ok(fileIdStr);
    }

    @Override
    public void readInGridFS(String faceId,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {

        // 0. 判断参数
        if(StringUtils.isBlank(faceId) || faceId.equalsIgnoreCase("null0")){
            GraceException.display(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }

        // 1. 从gridfs中读取
        File adminFace = readGridFSByFaceId(faceId);

        // 2. 把人脸图片输出到浏览器
        FileUtils.downloadFileByStream(response,adminFace);
    }

    private File readGridFSByFaceId(String faceId) throws Exception{
        GridFSFindIterable gridFSFiles = gridFSBucket.find(Filters.eq("_id", new ObjectId(faceId)));
        GridFSFile gridFS = gridFSFiles.first();
        if(gridFS == null){
            GraceException.display(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        }

        String fileName = gridFS.getFilename();
        System.out.println(fileName);

        // 获取文件流，保存文件到本地或者服务器的临时目录
        File fileTemp = new File("/workspace/temp_face/");
        if (!fileTemp.exists()) {
            fileTemp.mkdirs();
        }

        File myFile = new File("/workspace/temp_face" + fileName);

        // 创建文件输出流
        FileOutputStream os = new FileOutputStream(myFile);
        // 下载到服务器或者本地
        gridFSBucket.downloadToStream(new ObjectId(faceId),os);

        return myFile;
    }
}
