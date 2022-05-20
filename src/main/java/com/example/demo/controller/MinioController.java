package com.example.demo.controller;

import com.example.demo.utils.MinioUtil;
import com.example.demo.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/minio")
@Api(value = "minio", tags = "minio管理")
public class MinioController {

    @ApiOperation(value = "测试minio上传", notes = "测试minio上传")
    @PostMapping("/minioUpload")
    public R minioUpload(MultipartFile file) {
        try {
            if (null == file || file.isEmpty()) {
                return R.failed("上传图片不能为空！");
            }

            String fileName = file.getOriginalFilename();
            if (null == fileName || fileName.isEmpty()) {
                return R.failed("上传图片名称不能为空！");
            }

            String newName = System.currentTimeMillis()
                    + (fileName.lastIndexOf(".") > 0 ? fileName.substring(fileName.lastIndexOf(".")).toLowerCase() : ".data");

            //原图片尺寸不变，质量压缩
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream()).scale(1).outputQuality(0.1).toOutputStream(baos);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

            MinioUtil minioUtil = new MinioUtil();
            //minioUtil.uploadToMinio_preview(file.getInputStream(), file.getContentType(), newName);//原图上传
            minioUtil.uploadToMinio_preview(bais, file.getContentType(), newName);//压缩后上传
            String url = minioUtil.getUrlByObjectName(newName);//文件访问路径
            return R.ok(url, "上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("上传失败！");
        }
    }

}
