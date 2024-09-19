package terapps.factoryplanner.filestorage.services

import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.http.Method
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import terapps.factoryplanner.filestorage.dto.BucketEnum
import java.util.concurrent.TimeUnit

@Service
class FileStorageService {
    @Autowired
    @Qualifier("assets")
    private lateinit var assetsClient: MinioClient

/*    @Autowired
    @Qualifier("resources")
    private lateinit var resourcesClient: MinioClient*/

    private fun getClient(bucket: BucketEnum): MinioClient = when (bucket) {
        BucketEnum.Assets -> assetsClient
        BucketEnum.Resources -> TODO("access appears to be broken, dont need for now")
    }

    fun getSignedUrl(bucket: BucketEnum, bucketObject: String): String = getClient(bucket).getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucket.bucketName)
                    .`object`(bucketObject)
                    .expiry(2, TimeUnit.DAYS)
                    .build()
    )
}
