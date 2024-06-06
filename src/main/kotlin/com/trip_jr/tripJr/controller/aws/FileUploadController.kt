package com.trip_jr.tripJr.controller.aws

import com.trip_jr.tripJr.service.aws.S3Service
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.services.s3.S3Client
import java.nio.file.Files
import java.nio.file.Paths


@RestController
@RequestMapping("/api/photos")
class FileUploadController(private val s3Service: S3Service) {

    init {
        s3Service.createBucket("photos")
    }

    @PostMapping("/upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile): ResponseEntity<Map<String, String>> {
        val tempFilePath = Paths.get(System.getProperty("java.io.tmpdir"), file.originalFilename)
        Files.write(tempFilePath, file.bytes)

        val urls = s3Service.resizeAndUploadImage("photos", tempFilePath.toFile())
        return ResponseEntity.ok(urls)
    }


    @GetMapping("/presigned-url")
    fun getPresignedUrl(@RequestParam("fileName") fileName: String): ResponseEntity<String> {
        val presignedUrl = s3Service.generatePresignedUrl("photos", fileName)

        return ResponseEntity.ok(presignedUrl)
    }


}