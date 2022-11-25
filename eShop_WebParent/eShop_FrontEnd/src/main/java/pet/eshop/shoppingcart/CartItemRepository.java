package pet.eshop.shoppingcart;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pet.eshop.common.entity.CartItem;
import pet.eshop.common.entity.Customer;
import pet.eshop.common.entity.product.Product;

import java.util.List;

public interface CartItemRepository extends CrudRepository<CartItem, Integer> {

    public List<CartItem> findByCustomer(Customer customer);

    public CartItem findByCustomerAndProduct(Customer customer, Product product);

    @Query("UPDATE CartItem c SET c.quantity = ?1 WHERE c.customer = ?2 and c.product = ?3")
    @Modifying
    public void updateQuantity(Integer quantity, Customer customer, Product product);

    @Modifying
    public void deleteByCustomerAndProduct(Customer customer, Product product);

    @Query("DELETE FROM CartItem c WHERE c.customer.id = ?1 AND c.product.id = ?2")
    @Modifying
    public void deleteByCustomerAndProduct(Integer customerId, Integer productId);
}
