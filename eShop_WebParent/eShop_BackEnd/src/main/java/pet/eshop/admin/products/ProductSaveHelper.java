package pet.eshop.admin.products;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pet.eshop.admin.util.AmazonS3Util;
import pet.eshop.admin.util.FileUploadUtil;
import pet.eshop.common.entity.product.Product;
import pet.eshop.common.entity.product.ProductImage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductSaveHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaveHelper.class);
    static void deleteRemovedOnFormExtraImages(Product product) {
        //String extraImageDir = "../product-images/" + product.getId() + "/extras";
        String extraImageDir = "product-images/" + product.getId() + "/extras";
        List<String> listObjectKeys = AmazonS3Util.listFolder(extraImageDir);


        listObjectKeys.forEach(objectKey -> {
            int lastIndexOfSlash = objectKey.lastIndexOf("/");
            String fileName = objectKey.substring(lastIndexOfSlash + 1);

            if (!product.containsImageName(fileName)) {
                AmazonS3Util.deleteFile(objectKey);
                LOGGER.debug("Deleted extra image: " + fileName);
            }
        });

    }

    static void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {
        if (imageIDs == null || imageIDs.length == 0) return;

        Set<ProductImage> images = new HashSet<>();

        for (int count = 0; count <imageIDs.length; count++){
            Integer id = Integer.parseInt(imageIDs[count]);
            String name = imageNames[count];
            images.add(new ProductImage(id, name, product));
        }

        product.setImages(images);
    }

    static void setProductDetails(String[] detailNames,
                                   String[] detailValues,
                                   String[] detailIDs, Product product) {
        if (detailNames == null || detailNames.length == 0) return;

        for (int count = 0; count < detailNames.length; count++) {
            String name = detailNames[count];
            String value = detailValues[count];
            int id = Integer.parseInt(detailIDs[count]);

            if (id !=0 ){
                product.addDetail(id, name, value);
            } else {
                if (!name.isEmpty() && !value.isEmpty()) {
                    product.addDetail(name, value);
                }
            }
        }
    }

    static void saveUploadedImages(MultipartFile mainImageMultipart,
                                    MultipartFile[] extraImageMultipart,
                                    Product savedProd) throws IOException {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
//            String uploadDir = "../product-images/" + savedProd.getId();
//
//            FileUploadUtil.cleanDir(uploadDir);
//            FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);

            String uploadDir = "product-images/" + savedProd.getId();
            List<String> listObjectKey = AmazonS3Util.listFolder(uploadDir + "/");
            listObjectKey.forEach(objKey -> {
                if (objKey.contains("/extras/")) {
                    AmazonS3Util.deleteFile(objKey);
                }
            });
            AmazonS3Util.uploadFile(uploadDir, fileName, mainImageMultipart.getInputStream());
        }

        //String uploadDir = "../product-images/" + savedProd.getId() + "/extras";
        String uploadDir = "product-images/" + savedProd.getId() + "/extras";
        //FileUploadUtil.cleanDir(uploadDir);
        for (MultipartFile multipartFile : extraImageMultipart) {
            if (multipartFile.isEmpty()) continue;
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            // FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
        }
    }

    static void setMainImageName(MultipartFile mainImageMultipart, Product product){
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
            product.setMainImage(fileName);
        }
    }

    static void setNewExtraImageNames(MultipartFile[] extraImageMultipart, Product product){
        for (MultipartFile multipartFile : extraImageMultipart) {
            if (!multipartFile.isEmpty()) {
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

                if (!product.containsImageName(fileName)) {
                    product.addExtraImage(fileName);
                }

            }
        }
    }

}
