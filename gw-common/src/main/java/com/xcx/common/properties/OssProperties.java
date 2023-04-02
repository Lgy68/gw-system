package com.xcx.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.sql.DataSourceDefinition;

/**
 * 读取配置文件
 */
@Data
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssProperties {

    private String accessKey;
    private String secret;
    private String bucketName;
    private String url; //域名
    private String endpoint;

}
