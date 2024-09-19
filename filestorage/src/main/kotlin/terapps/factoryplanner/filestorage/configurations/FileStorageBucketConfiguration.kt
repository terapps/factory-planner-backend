package terapps.factoryplanner.filestorage.configurations

import io.minio.BucketExistsArgs
import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import terapps.factoryplanner.filestorage.configurations.dto.BucketConfig
import terapps.factoryplanner.filestorage.configurations.dto.FileStorageConfig
import terapps.factoryplanner.filestorage.dto.BucketEnum

@Configuration
class FileStorageBucketConfiguration {
    @Autowired
    private lateinit var fileStorageConfig: FileStorageConfig

    @Bean
    @Qualifier("assets")
    fun minioClientAssets(): MinioClient = makeMinioClient(
            fileStorageConfig.url,
            BucketEnum.Assets.bucketName,
            fileStorageConfig.assets
    )

/*    @Bean
    @Qualifier("resources")
    fun minioClientResources(): MinioClient = makeMinioClient(
            fileStorageConfig.url,
            BucketEnum.Assets.bucketName,
            fileStorageConfig.resources
    )*/

    private fun makeMinioClient(url: String, bucketName: String, bucketConfig: BucketConfig): MinioClient = MinioClient.builder()
            .endpoint(url)
            .credentials(bucketConfig.accessKey, bucketConfig.secretKey)
            .build().also {
                val bucket = BucketExistsArgs.builder().bucket(bucketName).build()

                if (!it.bucketExists(bucket)) {
                    throw Error("Unknown bucket: ${bucketName}") // TODO type error
                }
            };
}