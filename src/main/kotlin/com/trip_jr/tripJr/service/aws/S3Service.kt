package com.trip_jr.tripJr.service.aws

import net.coobird.thumbnailator.Thumbnails
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectResponse
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.io.File
import java.io.IOException
import java.time.Duration
import java.util.*


@Service
class S3Service(
    private val s3Client: S3Client, private val s3Presigner: S3Presigner
) {

    fun createBucket(bucketName: String) {
        val createBucketReq = CreateBucketRequest.builder()
            .bucket(bucketName)
            .build()

        s3Client.createBucket(createBucketReq)
    }

    fun uploadFile(bucketName: String, file: File, key: String): PutObjectResponse {
        val putObjectReq = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()

        return s3Client.putObject(putObjectReq, file.toPath())
    }

    fun generatePresignedUrl(bucketName: String, key: String): String {
        val presignedReq = PutObjectPresignRequest.builder()
            .putObjectRequest { builder -> builder.bucket(bucketName).key(key) }
            .signatureDuration(Duration.ofMinutes(10)).build()

        val presignedPutObjectReq = s3Presigner.presignPutObject(presignedReq)
        return presignedPutObjectReq.url().toString()
    }


    fun resizeAndUploadImage(bucketName: String, originalFile: File): Map<String, String> {
        val keyBase = UUID.randomUUID().toString()
        val sizes = mapOf(
            "full" to 2000,
            "gallery" to 500,
            "thumbnail" to 100,
        )

        val urls = mutableMapOf<String, String>()

        sizes.forEach { (sizeName, width) ->
            val resizeFile = File("${originalFile.parent}/$keyBase-$sizeName.jpg")
            try {
                Thumbnails.of(originalFile)
                    .size(width, width)
                    .toFile(resizeFile)

                val s3Key = "$keyBase-$sizeName.jpg"
                uploadFile(bucketName, resizeFile, s3Key)
                urls[sizeName] = "http://localhost:4566/$bucketName/$s3Key"
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return urls
    }
}