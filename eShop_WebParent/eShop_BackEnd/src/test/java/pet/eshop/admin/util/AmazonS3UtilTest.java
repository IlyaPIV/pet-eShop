package pet.eshop.admin.util;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AmazonS3UtilTest {

    @Test
    public void testListFolder(){
        String folderName = "product-images/18";
        List<String> listKeys = AmazonS3Util.listFolder(folderName);
        listKeys.forEach(System.out::println);
    }

    @Test
    public void testUploadFile(){
        String folderName = "test-upload";
        String fileName = "GB project.jpg";
        String filePath = "C:\\Users\\peter\\OneDrive\\Pictures\\" + fileName;

        try {
            InputStream inputStream = new FileInputStream(filePath);
            AmazonS3Util.uploadFile(folderName, fileName, inputStream);

            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testDeleteFile(){
        String fileName= "test-upload/GB project.jpg";
        AmazonS3Util.deleteFile(fileName);
    }

    @Test
    public void testRemoveFolder(){
        String folderName= "test-upload";
        AmazonS3Util.removeFolder(folderName);
    }

}