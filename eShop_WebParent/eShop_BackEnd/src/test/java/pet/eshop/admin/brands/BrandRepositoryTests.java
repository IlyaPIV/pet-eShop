package pet.eshop.admin.brands;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import pet.eshop.admin.categories.CategoryRepository;
import pet.eshop.common.entity.Brand;
import pet.eshop.common.entity.Category;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@Rollback(value = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BrandRepositoryTests {

    @Autowired
    private BrandRepository repoBrands;

    @Autowired
    private CategoryRepository repoCategories;

    @Test
    public void testCreateBrandWithSingleCategory(){
        Brand newBrand = new Brand("Test Brand");
        Set<Category> categorySet = new HashSet<>();
        categorySet.add(repoCategories.findById(3).get());
        newBrand.setCategories(categorySet);

        Brand saved = repoBrands.save(newBrand);

        assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateBrandWithSomeCategories(){
        Brand newBrand = new Brand("Samsung");
        newBrand.addCategory(repoCategories.findById(4).get());
        newBrand.addCategory(repoCategories.findById(7).get());
        newBrand.addCategory(repoCategories.findById(29).get());
        newBrand.addCategory(repoCategories.findById(24).get());

        Brand saved = repoBrands.save(newBrand);

        assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindAll(){
        List<Brand> allBrands = (List<Brand>) repoBrands.findAll();

        for (Brand brand:
             allBrands) {
            System.out.println(brand);
        }

        assertThat(allBrands.size()).isGreaterThan(0);
    }

    @Test
    public void testGetById(){
        Brand brand = repoBrands.findById(3).get();
        System.out.println(brand);
        assertThat(brand).isNotNull();
    }

    @Test
    public void testUpdateName(){
        Brand brand = repoBrands.findById(5).get();
        String nameBefore = brand.getName();
        brand.setName("Epic Brand");
        Brand savedBrand = repoBrands.save(brand);
        String nameAfter = savedBrand.getName();

        assertThat(nameBefore).isNotEqualTo(nameAfter);
    }

    @Test
    public void testDelete(){
        Brand brand = repoBrands.findById(5).get();
        repoBrands.delete(brand);
    }


}
