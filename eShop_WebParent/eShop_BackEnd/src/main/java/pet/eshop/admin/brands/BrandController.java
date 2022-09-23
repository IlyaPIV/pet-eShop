package pet.eshop.admin.brands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BrandController {

    @Autowired
    private BrandService service;

    @GetMapping("/brands")
    public String listAll(Model model){

        model.addAttribute("listBrands", service.findAll());

        return "brands/brands";
    }
}
