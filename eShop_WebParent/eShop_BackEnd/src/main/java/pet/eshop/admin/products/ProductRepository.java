package pet.eshop.admin.products;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pet.eshop.admin.paging.SearchRepository;
import pet.eshop.common.entity.product.Product;

public interface ProductRepository extends SearchRepository<Product, Integer> {
    public Product findByName(String name);

    @Modifying
    @Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id = ?1")
    void updateEnabledStatus(Integer id, boolean enabled);

    Long countById(Integer id);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1% "
                + "OR p.shortDescription LIKE %?1% "
                + "OR p.fullDescription LIKE %?1% "
                + "OR p.brand.name LIKE %?1% "
                + "OR p.category.name LIKE %?1%")
    public Page<Product> findAll(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = ?1 "
            + "OR p.category.allParentsIDs LIKE %?2%")
    public Page<Product> findAllInCategory(Integer categoryId, String categoryIdMatch, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.category.id = ?1"
            + " OR p.category.allParentsIDs LIKE %?2%) AND "
            + "(p.name LIKE %?3% "
            + "OR p.shortDescription LIKE %?3% "
            + "OR p.fullDescription LIKE %?3% "
            + "OR p.brand.name LIKE %?3% "
            + "OR p.category.name LIKE %?3%)")
    public Page<Product> searchInCategory(Integer categoryId, String categoryIdMatch, String keyword,
                                          Pageable pageable);

    @Query("select p FROM Product p WHERE p.name LIKE %?1%")
    public Page<Product> searchProductByName(String keyword, Pageable pageable);
}
