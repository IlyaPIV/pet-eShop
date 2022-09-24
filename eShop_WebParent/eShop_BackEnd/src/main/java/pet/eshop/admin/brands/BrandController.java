package pet.eshop.admin.brands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pet.eshop.common.entity.Brand;
import pet.eshop.common.entity.Category;

import java.util.List;

@Controller
public class BrandController {

    @Autowired
    private BrandService service;

    @GetMapping("/brands")
    public String listAll(Model model){

        model.addAttribute("listBrands", service.findAll());

        return "brands/brands";
    }

    @GetMapping("/brands/new")
    public String newBrand(Model model){

        List<Category> listCategories = service.listCategoriesUsedInForm();
        model.addAttribute("brand", new Brand());
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("pageTitle", "Create New Brand");

        return "brands/brand";
    }
}
