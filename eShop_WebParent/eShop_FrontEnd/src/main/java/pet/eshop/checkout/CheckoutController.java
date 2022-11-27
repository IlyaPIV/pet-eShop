package pet.eshop.checkout;

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
import pet.eshop.shoppingcart.ShoppingCartService;
import pet.eshop.util.Utility;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CheckoutController {
    @Autowired private CheckoutService checkoutService;
    @Autowired private CustomerService customerService;
    @Autowired private AddressService addressService;
    @Autowired private ShippingRateService shipService;
    @Autowired private ShoppingCartService cartService;

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpServletRequest request){
        Customer customer = getAuthenticatedCustomer(request);
                ShippingRate shippingRate = null;
        Address defaultAddress = addressService.getDefaultAddress(customer);
        if (defaultAddress != null) {
            model.addAttribute("shippingAddress", defaultAddress.toString());
            shippingRate = shipService.getShippingRateForAddress(defaultAddress);
        } else {
            model.addAttribute("shippingAddress", customer.getAddress());
            shippingRate = shipService.getShippingRateForCustomer(customer);
        }
        if (shippingRate == null) {
            return "redirect:/cart";
        }
        List<CartItem> cartItems = cartService.cartItemList(customer);
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems, shippingRate);
        model.addAttribute("checkoutInfo", checkoutInfo);
        model.addAttribute("cartItems", cartItems);

        return "checkout/checkout";
    }

    public Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String customerEmail = Utility.getEmailOfAuthenticatedCustomer(request);

        return customerService.getCustomerByEmail(customerEmail);
    }
}
