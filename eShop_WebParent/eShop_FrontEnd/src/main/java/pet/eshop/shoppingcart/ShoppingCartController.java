package pet.eshop.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pet.eshop.common.entity.CartItem;
import pet.eshop.common.entity.Customer;
import pet.eshop.common.exception.CustomerNotFoundException;
import pet.eshop.customer.CustomerService;
import pet.eshop.util.Utility;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService cartService;
    @Autowired
    private CustomerService customerService;


    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request){

        Customer customer = getAuthenticatedCustomer(request);
        List<CartItem> cartItemList = cartService.cartItemList(customer);
        float estimatedTotal = 0;
        for (CartItem item:
             cartItemList) {
            estimatedTotal+= item.getSubtotal();
        }
        model.addAttribute("cartItems", cartItemList);
        model.addAttribute("estimatedTotal", estimatedTotal);

        return "cart/shopping_cart";
    }

    public Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String customerEmail = Utility.getEmailOfAuthenticatedCustomer(request);

        return customerService.getCustomerByEmail(customerEmail);
    }


}
