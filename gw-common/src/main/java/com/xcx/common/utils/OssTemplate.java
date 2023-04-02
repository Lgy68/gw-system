package com.xcx.common.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.xcx.common.properties.OssProperties;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 阿里云的oss存储
 */
@Component
public class OssTemplate {

    private OssProperties properties;

    public String updaload(String filename, InputStream is) {
        //3.拼写路径
        filename = new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                + "/" + UUID.randomUUID().toString() + filename.substring(filename.lastIndexOf("."));
        // 创建OSSClient实例。
        String endpoint = properties.getEndpoint();
        String accessKeyId = properties.getAccessKey();
        String accessKeySecret = properties.getSecret();
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            String bucketName = properties.getBucketName();
            ossClient.putObject(bucketName, filename, is);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return properties.getUrl() + filename;
    }
}
