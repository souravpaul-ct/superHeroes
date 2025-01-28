package com.example.superheroes.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration
public class SqsClientConfig {

    private final SqsConfig sqsConfig;

    public SqsClientConfig(SqsConfig sqsConfig) {
        this.sqsConfig = sqsConfig;
    }

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .endpointOverride(URI.create(sqsConfig.getEndpoint()))
                .region(Region.of(sqsConfig.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(sqsConfig.getAccessKey(), sqsConfig.getSecretKey())
                ))
                .build();
    }
}
