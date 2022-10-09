package pet.eshop.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pet.eshop.common.entity.Country;
import pet.eshop.common.entity.Customer;

import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService service;

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        List<Country> listAllCountries = service.listAllCountries();
        model.addAttribute("pageTitle", "Customer registration");
        model.addAttribute("listCountries", listAllCountries);
        model.addAttribute("customer", new Customer());

        return "register/register_form";
    }
}
