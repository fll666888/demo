package com.example.demo.utils;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@Component
public class MinioUtil {

    private static String endpoint;

    private static Integer port;

    private static String accessKey;

    private static String secretKey;

    private static String bucketName;

    @Value("${minio.endpoint}")
    public void setEndpoint(String endpoint) {
        MinioUtil.endpoint = endpoint;
    }

    @Value("${minio.port}")
    public void setPort(Integer port) {
        MinioUtil.port = port;
    }

    @Value("${minio.accessKey}")
    public void setAccessKey(String accessKey) {
        MinioUtil.accessKey = accessKey;
    }

    @Value("${minio.secretKey}")
    public void setSecretKey(String secretKey) {
        MinioUtil.secretKey = secretKey;
    }

    @Value("${minio.bucketName}")
    public void setBucketName(String bucketName) {
        MinioUtil.bucketName = bucketName;
    }

    private static MinioClient minioClient;

    public MinioClient getInstance() throws IOException, InvalidResponseException, RegionConflictException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException {
        if (minioClient == null) {
            minioClient = MinioClient.builder().endpoint(endpoint, port, false).credentials(accessKey, secretKey).build();
        }
        createBucket();
        return minioClient;
    }

    /**
     * 初始化Bucket
     *
     * @throws Exception 异常
     */
    public static void createBucket() throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException, RegionConflictException {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     *
     * @Description 获取minio所有的桶
     * @return java.util.List<io.minio.messages.Bucket>
     **/
    public List<Bucket> getAllBucket() throws Exception {
        // 获取minio中所以的bucket
        List<Bucket> buckets = getInstance().listBuckets();
        for (Bucket bucket : buckets) {
            log.info("bucket 名称:  {}      bucket 创建时间: {}", bucket.name(), bucket.creationDate());
        }
        return buckets;
    }

    /**
     *
     * @Description  将图片上传到minio服务器
     * @param inputStream: 输入流
     * @param objectName: 对象名称
     * @return void
     **/
    public void uploadToMinio(InputStream inputStream, String objectName) {
        try {
            long size = inputStream.available();
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, size, -1)
                    .build();
            // 上传到minio
            getInstance().putObject(putObjectArgs);
            inputStream.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public void uploadToMinio_preview(InputStream inputStream, String contentType, String objectName) {
        try {
            long size = inputStream.available();
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(inputStream, size, -1)
                    .contentType(contentType)//默认是application/octet-stream ： 二进制流数据（如常见的文件下载）
                    .build();
            // 上传到minio
            getInstance().putObject(putObjectArgs);
            inputStream.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     *
     * @Description 根据指定的objectName获取下载链接，需要bucket设置可下载的策略
     * @param objectName: 对象的名称
     * @return java.lang.String
     **/
    public String getUrlByObjectName(String objectName) {
        String objectUrl = null;
        try {
            objectUrl = getInstance().getObjectUrl(bucketName, objectName);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return objectUrl;
    }

    /**
     *
     * @Description 根据objectName从minio中下载文件到指定的目录
     * @param objectName: objectName
     * @param fileName: 文件名称
     * @param dir: 文件目录
     * @return void
     **/
    public void downloadImageFromMinioToFile(String objectName, String fileName, String dir) throws Exception {
        GetObjectArgs objectArgs = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build();
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        InputStream inputStream = getInstance().getObject(objectArgs);
        FileOutputStream outputStream =  new FileOutputStream(new File(dir, fileName.substring(fileName.lastIndexOf("/")+1)));
        int length;
        byte[] buffer = new byte[1024];
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.close();
        inputStream.close();
    }

}
