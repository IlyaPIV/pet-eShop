package pet.eshop.admin.customers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pet.eshop.admin.paging.PagingAndSortingHelper;
import pet.eshop.admin.paging.PagingAndSortingParam;
import pet.eshop.admin.settings.country.CountryRepository;
import pet.eshop.common.entity.Country;
import pet.eshop.common.entity.Customer;
import pet.eshop.common.exception.CustomerNotFoundException;

import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService service;
    @Autowired
    private CountryRepository countryRepo;

    @GetMapping("/customers")
    public String listFirstPage(){
        return "redirect:/customers/page/1?sortField=firstName&sortDir=asc";
    }

    @GetMapping("/customers/page/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "listCustomers", moduleURL = "/customers") PagingAndSortingHelper helper,
                             @PathVariable("pageNum") Integer pageNum){

       service.listByPage(pageNum, helper);

        return "customers/customers";
    }

    @GetMapping("/customers/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable(name = "id") Integer id,
                                          @PathVariable(name = "status") boolean enabled,
                                          RedirectAttributes redirectAttributes) {
        service.updateEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The customer with ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/customers";
    }

    @GetMapping("/customers/detail/{id}")
    public String viewCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes attributes){
        try{
            Customer customer = service.get(id);

            model.addAttribute("customer", customer);

            return "customers/customer_detail_modal";
        } catch (CustomerNotFoundException ex){
            attributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/customers";
        }
    }

    @GetMapping("/customers/edit/{id}")
    public String editCustomer(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes attributes){
        try {
            Customer customer = service.get(id);
            List<Country> allCountries = countryRepo.findAllByOrderByNameAsc();

            model.addAttribute("customer", customer);
            model.addAttribute("listCountries", allCountries);
            model.addAttribute("pageTitle", "Edit Customer (ID = " + id + ")");

            return "customers/customer_form";
        } catch (CustomerNotFoundException ex){
            attributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/customers";
        }
    }

    @PostMapping("/customers/save")
    public String saveCustomer(Customer customer, RedirectAttributes attributes){
        service.save(customer);
        attributes.addFlashAttribute("message", "Customer was updated successfully");
        return "redirect:/customers";
    }

    @GetMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable(name = "id") Integer id,
                                 RedirectAttributes attributes){
        try {
            service.delete(id);
            attributes.addFlashAttribute("message", "The Customer ID=" + id +
                    " was deleted successfully.");
        } catch (CustomerNotFoundException ex){
            attributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/customers";
    }
}
