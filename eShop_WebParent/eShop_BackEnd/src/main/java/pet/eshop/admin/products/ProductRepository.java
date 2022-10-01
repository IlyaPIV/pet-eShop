package pet.eshop.admin.products;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import pet.eshop.common.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
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
}
