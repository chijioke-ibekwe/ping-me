package com.project.pingme.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "amazon-properties")
@Data
public class AmazonS3Properties {

    private Bucket bucketName;

    @Data
    public static class Bucket {

        private String profilePicture;
    }
}
