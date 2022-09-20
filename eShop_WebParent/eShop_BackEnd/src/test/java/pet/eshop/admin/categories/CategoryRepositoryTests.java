package pet.eshop.admin.categories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import pet.eshop.common.entity.Category;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository repo;

    @Test
    public void testCreateRootCategory(){
        Category category = new Category("Electronics");
        Category savedCategory = repo.save(category);

        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateSubCategory(){
        Category parent = new Category(1);
        Category subCategory = new Category("Computer Components", parent);
        Category savedCategory = repo.save(subCategory);

        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateListSubCategories(){
        Category parent = new Category(7);
        Category sub1 = new Category("Android", parent);
        Category sub2 = new Category("Apple", parent);
        Category sub3 = new Category("Other", parent);

        repo.saveAll(List.of(sub1, sub2, sub3));
    }

    @Test
    public void testGetCategory(){
        Category category = repo.findById(1).get();
        System.out.println(category.getName());

        Set<Category> children = category.getChildren();

        for (Category subCategory : children) {
            System.out.println(subCategory.getAlias());
        }

        assertThat(children.size()).isGreaterThan(0);
    }

    @Test
    public void testPrintHierarchicalCategories(){
        Iterable<Category> categories = repo.findAll();

        for (Category category : categories) {
            if (category.getParent() == null) {
                System.out.println(category.getName());

                Set<Category> children = category.getChildren();
                for (Category subCategory : children) {
                    System.out.println("--" + subCategory.getName());
                    printChildrenCat(subCategory, 1);
                }
            }
        }
    }

    private void printChildrenCat(Category parent, int subLevel){
        int curLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();
        for (Category subCategory : children) {
            for (int i = 0; i < curLevel; i++) {
                System.out.print("--");
            }
            System.out.println(subCategory.getName());
            printChildrenCat(subCategory, curLevel);
        }
    }

    @Test
    public void testListRootCategories() {
        List<Category> rootCategories = repo.findRootCategories();
        rootCategories.forEach(category -> System.out.println(category.getName()));
    }

    @Test
    public void testFindByName(){
        String name = "Computers";
        Category category = repo.findByName(name);

        assertThat(category).isNotNull();
        assertThat(category.getName()).isEqualTo(name);
    }

    @Test
    public void testFindByAlias(){
        String alias = "Drones";
        Category category = repo.findByAlias(alias);

        assertThat(category).isNotNull();
        assertThat(category.getAlias()).isEqualTo(alias);
    }

}
