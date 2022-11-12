package pet.eshop.addressBook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pet.eshop.common.entity.Address;
import pet.eshop.common.entity.Customer;
import pet.eshop.customer.CustomerService;
import pet.eshop.util.Utility;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AddressController {

    @Autowired
    private AddressService addressService;
    @Autowired
    private CustomerService customerService;

    @GetMapping("/address_book")
    public String showAddressBook(Model model, HttpServletRequest request){
        Customer customer = getAuthenticatedCustomer(request);
        List<Address> addressList = addressService.addressList(customer);

        boolean usePrimaryAddressAsDefault = true;
        for (Address address:
             addressList) {
            if (address.isDefaultForShipping()) {
                usePrimaryAddressAsDefault = false;
                break;
            }
        }

        model.addAttribute("listAddresses", addressList);
        model.addAttribute("customer", customer);
        model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);

        return "address_book/addresses";
    }

    public Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String customerEmail = Utility.getEmailOfAuthenticatedCustomer(request);

        return customerService.getCustomerByEmail(customerEmail);
    }
}
