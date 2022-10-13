package pet.eshop.admin.customers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pet.eshop.common.entity.Customer;

import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService service;

    @GetMapping("/customers")
    public String listFirstPage(Model model){
        return listByPage(1, model, "firstName", "asc", null);
    }

    @GetMapping("/customers/page/{pageNum}")
    public String listByPage(@PathVariable("pageNum") Integer pageNum, Model model,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword){

        Page<Customer> customersPage = service.getPage(pageNum, sortField, sortDir, keyword);
        List<Customer> customerList = customersPage.getContent();

        long startCount = (long) (pageNum - 1) * CustomerService.CUSTOMERS_PER_PAGE + 1;
        long endCount = startCount + CustomerService.CUSTOMERS_PER_PAGE - 1;
        if (endCount > customersPage.getTotalElements()) {
            endCount = customersPage.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", customersPage.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", customersPage.getTotalElements());
        model.addAttribute("listCustomers", customerList);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("moduleURL", "/customers");

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
}
