package pet.eshop.addressBook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pet.eshop.common.entity.Address;
import pet.eshop.common.entity.Country;
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

    @GetMapping("/address_book/new")
    public String newAddress(Model model){
        List<Country> countryList = customerService.listAllCountries();

        model.addAttribute("listCountries", countryList);
        model.addAttribute("address", new Address());
        model.addAttribute("pageTitle", "Add New Address");

        return "address_book/address_form";
    }

    @PostMapping("/address_book/save")
    public String saveAddress(Address address, HttpServletRequest request, RedirectAttributes ra){
        Customer customer = getAuthenticatedCustomer(request);

        address.setCustomer(customer);
        addressService.save(address);

        ra.addFlashAttribute("message", "The address has been saved successfully.");

        return "redirect:/address_book";
    }

    @GetMapping("/address_book/edit/{id}")
    public String editAddress(@PathVariable("id") Integer addressId, Model model,
                              HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);
        List<Country> countryList = customerService.listAllCountries();

        Address address = addressService.get(addressId, customer.getId());

        model.addAttribute("address", address);
        model.addAttribute("listCountries", countryList);
        model.addAttribute("pageTitle", "Edit Address (ID: " + addressId + ")");

        return "address_book/address_form";
    }

    @GetMapping("/address_book/delete/{id}")
    public String deleteAddress(@PathVariable("id") Integer addressId, RedirectAttributes ra,
                                HttpServletRequest request){
        Customer customer = getAuthenticatedCustomer(request);
        addressService.delete(addressId, customer.getId());

        ra.addFlashAttribute("message", "The address ID: " + addressId + " has been deleted");

        return "redirect:/address_book";
    }
}
