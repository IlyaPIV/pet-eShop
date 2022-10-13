package pet.eshop.admin.customers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {
    @Autowired
    private CustomerService service;

    @PostMapping("/customers/check_unique_email")
    public String checkDuplicateEmail(@Param("email") String email, @Param("id") Integer id){
        return service.isEmailUnique(email, id) ? "OK" : "Duplicated";
    }
}
