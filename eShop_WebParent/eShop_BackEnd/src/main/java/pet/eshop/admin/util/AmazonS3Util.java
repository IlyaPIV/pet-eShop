package pet.eshop.admin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AmazonS3Util {
    private static final String BUCKET_NAME;
    private static final Logger LOGGER = LoggerFactory.getLogger(AmazonS3Util.class);

    static {
        BUCKET_NAME = System.getenv("AWS_BUCKET_NAME");
    }

    public static List<String> listFolder(String folderName){
        S3Client client = S3Client.builder().build();
        List<S3Object> contents = getS3ObjectList(folderName, client);
        List<String> listKeys = new ArrayList<>();

        for (S3Object object : contents) {
//            System.out.println("Key: " + object.key());
//            System.out.println("Owner: " + object.owner());
            listKeys.add(object.key());
        }

        client.close();

        return listKeys;
    }

    private static List<S3Object> getS3ObjectList(String folderName, S3Client client) {
        ListObjectsRequest listRequest = ListObjectsRequest.builder()
                                                            .bucket(BUCKET_NAME)
                                                                    .prefix(folderName)
                                                            .build();

        ListObjectsResponse response = client.listObjects(listRequest);
        List<S3Object> contents = response.contents();
        return contents;
    }

    public static void uploadFile(String folderName, String fileName, InputStream inputStream){
        S3Client client = S3Client.builder().build();

        PutObjectRequest request = PutObjectRequest.builder()
                                                        .bucket(BUCKET_NAME)
                                                        .key(folderName + "/" + fileName)
                                                        .acl("public-read")
                                                                .build();
        try (inputStream) {
            int contentLength = inputStream.available();
            client.putObject(request, RequestBody.fromInputStream(inputStream, contentLength));
        } catch (IOException ex){
            LOGGER.error("Could not upload file to Amazon S3", ex);
        }

        client.close();
    }

    public static void deleteFile(String fileName){
        S3Client client = S3Client.builder().build();

        DeleteObjectRequest request = DeleteObjectRequest.builder()
                                                            .bucket(BUCKET_NAME)
                                                            .key(fileName)
                                                            .build();

        client.deleteObject(request);

        client.close();
    }

    public static void removeFolder(String folderName){
        S3Client client = S3Client.builder().build();
        List<S3Object> contents = getS3ObjectList(folderName, client);

        for (S3Object obj: contents) {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(obj.key())
                    .build();

            client.deleteObject(request);
        }

        client.close();
    }
}
