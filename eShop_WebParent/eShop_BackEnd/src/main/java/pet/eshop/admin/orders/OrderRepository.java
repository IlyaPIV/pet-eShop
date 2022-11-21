package pet.eshop.admin.orders;

import org.springframework.data.repository.PagingAndSortingRepository;
import pet.eshop.common.entity.Order;

public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {

}
