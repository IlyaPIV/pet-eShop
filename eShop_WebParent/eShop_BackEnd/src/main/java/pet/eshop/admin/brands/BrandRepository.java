package pet.eshop.admin.brands;

import org.springframework.data.repository.PagingAndSortingRepository;
import pet.eshop.common.entity.Brand;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {

}
