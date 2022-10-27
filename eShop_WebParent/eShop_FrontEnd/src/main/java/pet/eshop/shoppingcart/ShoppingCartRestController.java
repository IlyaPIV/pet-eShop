package pet.eshop.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pet.eshop.common.entity.Customer;
import pet.eshop.common.exception.CustomerNotFoundException;
import pet.eshop.customer.CustomerService;
import pet.eshop.util.Utility;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ShoppingCartRestController {
    @Autowired
    private ShoppingCartService cartService;
    @Autowired
    private CustomerService customerService;

    @PostMapping("/cart/add/{productId}/{quantity}")
    public String addProduct(@PathVariable("productId") Integer productId,
                             @PathVariable("quantity") Integer quantity,
                             HttpServletRequest request){
        try {
            Customer customer = getAuthenticatedCustomer(request);
            Integer updatedQuantity = cartService.addProduct(productId, quantity, customer);
            return updatedQuantity + " item(s) of this product were added to your shopping cart.";
        } catch (CustomerNotFoundException e) {
            return "You must login to add this product to cart.";
        } catch (ShoppingCartException ex){
            return ex.getMessage();
        }
    }

    public Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String customerEmail = Utility.getEmailOfAuthenticatedCustomer(request);
        if (customerEmail == null) {
            throw new CustomerNotFoundException("No authenticated customer");
        }
        return customerService.getCustomerByEmail(customerEmail);
    }

    @PostMapping("/cart/update/{productId}/{quantity}")
    public String updateQuantity(@PathVariable("productId") Integer productId,
                                @PathVariable("quantity") Integer quantity,
                                HttpServletRequest request){
        try {
            Customer customer = getAuthenticatedCustomer(request);
            float subtotal = cartService.updateQuantity(productId, quantity, customer);
            return String.valueOf(subtotal);
        } catch (CustomerNotFoundException e) {
            return "You must login to change quantity of product in your cart.";
        }
    }

    @DeleteMapping("/cart/remove/{productId}")
    public String removeProduct(@PathVariable("productId") Integer productId, HttpServletRequest request){
        try {
            Customer customer = getAuthenticatedCustomer(request);
            cartService.removeProduct(productId, customer);
            return "The product was successfully removed from shopping cart.";
        } catch (CustomerNotFoundException e) {
            return "You must login to delete product from your cart.";
        }
    }
}
