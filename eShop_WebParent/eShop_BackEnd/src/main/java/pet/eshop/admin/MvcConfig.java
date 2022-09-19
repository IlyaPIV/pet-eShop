package pet.eshop.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String UserPhotosDirName = "user-photos";
        Path userPhotosDir = Paths.get(UserPhotosDirName);

        String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/" + UserPhotosDirName + "/**")
                .addResourceLocations("file:/" + userPhotosPath + "/");

        String categoryImagesDirName = "../category-images";
        Path categoryImagesDir = Paths.get(categoryImagesDirName);

        String categoryImagesPath = categoryImagesDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/category-images/**")
                .addResourceLocations("file:/" + categoryImagesPath + "/");
    }
}
