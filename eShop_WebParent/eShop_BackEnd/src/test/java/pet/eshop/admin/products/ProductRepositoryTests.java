package pet.eshop.admin.products;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import pet.eshop.common.entity.Brand;
import pet.eshop.common.entity.Category;
import pet.eshop.common.entity.Product;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateProduct(){
        Brand brand = entityManager.find(Brand.class, 37);
        Category category = entityManager.find(Category.class, 5);

        Product product = new Product();
        product.setName("Acer Aspire Desktop");
        product.setAlias("acer_aspire_desktop");
        product.setShortDescription("Short info for Acer desktop");
        product.setFullDescription("This is a full description for Acer Aspire Desktop");

        product.setBrand(brand);
        product.setCategory(category);

        product.setCost(600);
        product.setPrice(780);
        product.setEnabled(true);
        product.setInStock(true);
        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        Product savedProd = repo.save(product);

        assertThat(savedProd).isNotNull();
        assertThat(savedProd.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllProducts(){
        Iterable<Product> iterableProducts = repo.findAll();

        iterableProducts.forEach(System.out::println);
    }

    @Test
    public void testGetProduct(){
        Integer id = 2;
        Product prod =  repo.findById(id).get();
        System.out.println(prod);
        assertThat(prod).isNotNull();
    }

    @Test
    public void testUpdateProduct(){
        Integer id = 1;
        Product prod = repo.findById(id).get();

        prod.setPrice(499);
        prod.setCost(345);

        repo.save(prod);

        Product updatedProd = entityManager.find(Product.class, id);
        assertThat(updatedProd.getPrice()).isGreaterThan(490);
    }

    @Test
    public void testDeleteProduct(){
        Integer id = 4;
        repo.deleteById(id);

        Optional<Product> prod = repo.findById(id);
        assertThat(prod.isEmpty());
    }

    @Test
    public void testSaveProductWithImages(){
        Integer productId = 1;
        Product product = repo.findById(productId).get();

        product.setMainImage("main image.jpg");
        product.addExtraImage("extra image 1.jpg");
        product.addExtraImage("extra_image_2.png");
        product.addExtraImage("extra-image3.png");

        Product savedProd = repo.save(product);

        assertThat(savedProd.getImages().size()).isEqualTo(3);
    }
}
