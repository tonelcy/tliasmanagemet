package com.lcy.controller;

import com.lcy.pojo.Result;
import com.lcy.utils.AliyunOSSOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {
    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;
    /**
     * 本地磁盘存储方案
     */
/*    private static final String UPLOAD_DIR = "D:/images/";
    @PostMapping("upload")
    public Result upload(String username , Integer age, MultipartFile file) throws Exception{



        log.info("上传文件：{}, {}, {}", username, age, file);
        if (!file.isEmpty()) {
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString().replace("-", "") + extName;
            // 拼接完整的文件路径
            File targetFile = new File(UPLOAD_DIR + uniqueFileName);

            // 如果目标目录不存在，则创建它
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            // 保存文件
            file.transferTo(targetFile);
        }
        return Result.success();*/

    /**
     * OSS存储文件
     */
    @PostMapping("upload")
    public Result upload(MultipartFile file) throws Exception{
        log.info("上传文件：{}", file);
        if (!file.isEmpty()) {
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString().replace("-", "") + extName;
            // 将文件交给OSS处理,上传文件
            String url = aliyunOSSOperator.upload(file.getBytes(), uniqueFileName);
            log.info("上传OSS成功,url：{}", url);
            return Result.success(url);
        }
        return Result.error("上传失败");
    }


}
