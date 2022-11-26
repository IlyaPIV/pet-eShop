package pet.eshop.shoppingcart;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pet.eshop.addressBook.AddressService;
import pet.eshop.common.entity.Address;
import pet.eshop.common.entity.CartItem;
import pet.eshop.common.entity.Customer;
import pet.eshop.common.entity.ShippingRate;
import pet.eshop.customer.CustomerService;
import pet.eshop.shipping.ShippingRateService;
import pet.eshop.util.Utility;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService cartService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ShippingRateService shipService;


    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request){

        Customer customer = getAuthenticatedCustomer(request);
        List<CartItem> cartItemList = cartService.cartItemList(customer);
        float estimatedTotal = 0;
        for (CartItem item:
             cartItemList) {
            estimatedTotal+= item.getSubtotal();
        }

        boolean usePrimaryAddressAsDefault = false;
        ShippingRate shippingRate = null;
        Address defaultAddress = addressService.getDefaultAddress(customer);
        if (defaultAddress != null) {
            shippingRate = shipService.getShippingRateForAddress(defaultAddress);
        } else {
            shippingRate = shipService.getShippingRateForCustomer(customer);
            usePrimaryAddressAsDefault = true;
        }

        model.addAttribute("shippingSupported", shippingRate != null);
        model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
        model.addAttribute("cartItems", cartItemList);
        model.addAttribute("estimatedTotal", estimatedTotal);

        return "cart/shopping_cart";
    }

    public Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String customerEmail = Utility.getEmailOfAuthenticatedCustomer(request);

        return customerService.getCustomerByEmail(customerEmail);
    }


}
