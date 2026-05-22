package com.eduplan.infrastructure.storage

import com.eduplan.application.port.output.FileStoragePort
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.InputStream
import jakarta.annotation.PostConstruct

@Component
class S3StorageImpl(
    private val s3Config: S3StorageConfig,
) : FileStoragePort {

    private lateinit var client: S3Client

    @PostConstruct
    fun init() {
        val creds = AwsBasicCredentials.create(s3Config.accessKey, s3Config.secretKey)
        client = S3Client.builder()
            .region(Region.of(s3Config.region))
            .credentialsProvider(StaticCredentialsProvider.create(creds))
            .build()
    }

    override fun store(path: String, content: InputStream, contentLength: Long, contentType: String): String {
        val put = PutObjectRequest.builder()
            .bucket(s3Config.bucket)
            .key(path)
            .contentType(contentType)
            .contentLength(contentLength)
            .build()

        client.putObject(put, RequestBody.fromInputStream(content, contentLength))
        return "s3://${s3Config.bucket}/$path"
    }

    override fun delete(path: String) {
        try {
            val key = path.removePrefix("s3://${s3Config.bucket}/")
            client.deleteObject { b -> b.bucket(s3Config.bucket).key(key) }
        } catch (_: Exception) {
        }
    }

    override fun getResource(path: String): Resource? {
        return try {
            val key = path.removePrefix("s3://${s3Config.bucket}/")
            val req = GetObjectRequest.builder().bucket(s3Config.bucket).key(key).build()
            val responseInputStream = client.getObject(req)
            InputStreamResource(responseInputStream)
        } catch (e: Exception) {
            null
        }
    }
}
