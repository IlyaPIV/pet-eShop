package pet.eshop.category;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pet.eshop.common.entity.Category;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

    @Query("SELECT c from Category c WHERE c.enabled = true ORDER BY c.name ASC")
    public List<Category> findAllEnabled();
}
