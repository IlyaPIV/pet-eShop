package pet.eshop.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.eshop.common.entity.CartItem;
import pet.eshop.common.entity.Customer;
import pet.eshop.common.entity.Product;

import java.util.List;

@Service
public class ShoppingCartService {
    @Autowired
    private CartItemRepository cartRepo;

    public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException {
        Integer updatedQuantity = quantity;
        Product product = new Product(productId);
        CartItem cartItem = cartRepo.findByCustomerAndProduct(customer, product);

        if (cartItem != null) {
            updatedQuantity = cartItem.getQuantity() + quantity;
            if (updatedQuantity > 5) {
                throw new ShoppingCartException("Could not add more " + quantity + " item(s)"
                + " because there's already " + cartItem.getQuantity() + " item(s) "
                + "in your shopping cart. Maximum allowed quantity is 5.");
            }
        } else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCustomer(customer);
        }
        cartItem.setQuantity(updatedQuantity);
        cartRepo.save(cartItem);

        return updatedQuantity;
    }

    public List<CartItem> cartItemList(Customer customer){
        return cartRepo.findByCustomer(customer);
    }
}
