package pet.eshop.admin.products;

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
}
