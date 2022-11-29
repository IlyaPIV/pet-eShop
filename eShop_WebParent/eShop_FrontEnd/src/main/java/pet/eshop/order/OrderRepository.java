package pet.eshop.order;

import org.springframework.data.jpa.repository.JpaRepository;
import pet.eshop.common.entity.order.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
