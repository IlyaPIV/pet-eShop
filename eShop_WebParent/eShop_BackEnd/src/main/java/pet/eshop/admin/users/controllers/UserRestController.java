package pet.eshop.admin.users.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pet.eshop.admin.users.UserService;

@RestController
public class UserRestController {

    @Autowired
    private UserService service;

    @PostMapping("/users/check_email")
    //public String checkDuplicateEmail(@RequestParam("id") Integer id, @RequestParam("email") String email){
    public String checkDuplicateEmail(Integer id, String email){    //@RequestParam - опционально если имена совпадают
        return service.isEmailUnique(id, email) ? "OK" : "Duplicate";
    }
}
