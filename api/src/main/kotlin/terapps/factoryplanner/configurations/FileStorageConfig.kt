package terapps.factoryplanner.configurations

import com.fasterxml.jackson.databind.ObjectMapper
import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FileStorageConfig {
    @Bean
    fun minioClient(): MinioClient = MinioClient.builder() // TODO config
                .endpoint("http://192.168.1.61:9000")
                .credentials("cdQLoTUuUpAvlvJWyPue", "EYukFNKxnwk9TGMHvz2xyiFfVoJHqVYoEmHdfLk0")
                .build().also {
                    val bucket = BucketExistsArgs.builder().bucket("assets").build()

                    if (!it.bucketExists(bucket)) {
                        it.makeBucket(MakeBucketArgs.builder().bucket("assets").build())
                    }
            };
}