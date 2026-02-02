package com.example.adoptie.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest

@Service
class ImagineService(private val s3Client: S3Client) {
    @Value("\${app.upload.bucket}")
    private lateinit var bucketName: String

    fun saveImage(file: MultipartFile): String {
        val filename = "${System.currentTimeMillis()}_${file.originalFilename}"
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(filename)
            .contentType(file.contentType)
            .build()
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.bytes))
        return "/storage/v1/object/public/$bucketName/$filename"
    }

    fun deleteImage(imageUrl: String?) {
        if (imageUrl == null || imageUrl.isBlank()) return

        try {
            val filename = imageUrl.substringAfterLast("/")

            val deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build()

            s3Client.deleteObject(deleteObjectRequest)
            println("Imagine ștearsă cu succes din Supabase: $filename")
        } catch (e: Exception) {
            println("Eroare la ștergerea imaginii din Supabase: \${e.message}")
        }
    }

    fun deleteImages(imageUrls: List<String>) {
        imageUrls.forEach { deleteImage(it) }
    }
}