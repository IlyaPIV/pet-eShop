package pet.eshop.admin.products;

import org.springframework.data.repository.PagingAndSortingRepository;
import pet.eshop.common.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
}
