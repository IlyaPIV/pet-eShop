package pet.eshop.admin.categories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pet.eshop.common.entity.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE CONCAT(c.id, ' ', c.name, ' ', c.alias) LIKE %?1%")
    public List<Category> findAll(String keyword);

    /**
     * остался для тестов
     */
    @Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
    public List<Category> findRootCategories();

    @Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
    public List<Category> findRootCategories(Sort sort);

    @Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
    public Page<Category> findRootCategories(Pageable pageable);

    public Category findByName(String name);

    public Category findByAlias(String alias);

    @Modifying
    @Query("UPDATE Category c SET c.enabled = ?2 WHERE c.id = ?1")
    public void updateEnabledStatus(Integer id, boolean enabled);

    public Long countById(Integer id);
}
