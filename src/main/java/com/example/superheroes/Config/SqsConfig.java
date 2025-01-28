package com.example.superheroes.Config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class SqsConfig {

    @Value("${sqs.queue.name}")
    private String queueName;

    @Value("${sqs.queue.url}")
    private String queueUrl;

    @Value("${spring.cloud.aws.sqs.region}")
    private String region;

    @Value("${spring.cloud.aws.sqs.endpoint}")
    private String endpoint;

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;
}
