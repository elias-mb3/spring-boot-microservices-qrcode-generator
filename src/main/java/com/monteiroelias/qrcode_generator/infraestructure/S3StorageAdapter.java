package com.monteiroelias.qrcode_generator.infraestructure;

import com.monteiroelias.qrcode_generator.exception.StorageException;
import com.monteiroelias.qrcode_generator.ports.StoragePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.net.URL;

@Component 
public class S3StorageAdapter implements StoragePort {

    private static final Logger logger = LoggerFactory.getLogger(S3StorageAdapter.class);

    private final S3Client s3Client;
    private final String bucketName;

    public S3StorageAdapter(S3Client s3Client, @Value("${aws.s3.bucket.name}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public String uploadFile(byte[] fileData, String fileName, String contentType) {
        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromBytes(fileData));
            logger.info("Arquivo '{}' enviado com sucesso para o bucket S3: {}", fileName, bucketName);

            GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            URL fileUrl = s3Client.utilities().getUrl(getUrlRequest);
            return fileUrl.toExternalForm();

        } catch (SdkException e) {
            logger.error("Falha ao enviar o arquivo '{}' para o S3.", fileName, e);
            throw new StorageException("Erro ao fazer upload do arquivo para o S3", e);
        }
    }
}