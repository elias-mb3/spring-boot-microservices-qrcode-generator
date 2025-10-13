package com.monteiroelias.qrcode_generator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    /**
     * Define o S3Client como um bean gerenciado pelo Spring.
     * O SDK buscará as credenciais automaticamente em locais padrão
     * (variáveis de ambiente, ~/.aws/credentials, etc.).
     */
    @Bean
    public S3Client s3Client(@Value("${aws.s3.region}") String awsRegion) {
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .build();
    }
}