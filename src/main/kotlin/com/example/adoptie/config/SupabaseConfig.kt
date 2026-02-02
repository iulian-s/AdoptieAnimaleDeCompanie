package com.example.adoptie.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

@Configuration
class SupabaseConfig {
    @Value("\${app.upload.supabaseUrl}")
    private lateinit var endpoint: String

    @Value("\${app.upload.supabaseAccessKey}")
    private lateinit var accessKey: String

    @Value("\${app.upload.supabaseSecretKey}")
    private lateinit var secretKey: String

    @Value("\${app.upload.region}")
    private lateinit var region: String

    @Bean
    fun s3Client(): S3Client {
        val credentials = AwsBasicCredentials.create(accessKey, secretKey)
        return S3Client.builder()
            .endpointOverride(URI.create(endpoint))
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .forcePathStyle(true)
            .build()
    }

}