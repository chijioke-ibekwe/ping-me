package com.project.pingme.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AwsCloudConfig {

    @Bean
    @Primary
    public AWSCredentialsProvider defaultAwsCredentialsProvider(){
        return new DefaultAWSCredentialsProviderChain();
    }
}
