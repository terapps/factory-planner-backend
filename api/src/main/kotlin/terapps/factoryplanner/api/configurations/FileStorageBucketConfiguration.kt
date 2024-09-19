package terapps.factoryplanner.api.configurations

import io.minio.BucketExistsArgs
import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import terapps.factoryplanner.api.configurations.dto.BucketConfig
import terapps.factoryplanner.api.configurations.dto.FileStorageConfig

@Configuration
class FileStorageBucketConfiguration {
    @Autowired
    private lateinit var fileStorageConfig: FileStorageConfig

    @Bean
    @Qualifier("assets")
    fun minioClientAssets(): MinioClient = makeMinioClient(
            fileStorageConfig.url,
            fileStorageConfig.assets
    )

    @Bean
    @Qualifier("resources")
    fun minioClientResources(): MinioClient = makeMinioClient(
            fileStorageConfig.url,
            fileStorageConfig.resources
    )

    private fun makeMinioClient(url: String, bucketConfig: BucketConfig): MinioClient = MinioClient.builder() // TODO config
            .endpoint(url)
            .credentials(fileStorageConfig.assets.accessKey, fileStorageConfig.assets.secretKey)
            .build().also {
                val bucket = BucketExistsArgs.builder().bucket(bucketConfig.bucket).build()

                if (!it.bucketExists(bucket)) {
                    throw Error("Unknown bucket: ${bucketConfig.bucket}")
                }
            };
}