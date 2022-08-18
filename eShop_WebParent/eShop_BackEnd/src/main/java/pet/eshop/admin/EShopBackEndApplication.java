package pet.eshop.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"pet.eshop.common.entity", "pet.eshop.admin.users"})
public class EShopBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(EShopBackEndApplication.class, args);
    }

}
