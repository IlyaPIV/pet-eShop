package pet.eshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pet.eshop.category.CategoryService;
import pet.eshop.common.entity.Category;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public String viewHomePage(Model model){
        List<Category> categoryList = categoryService.listNoChildrenCategories();
        model.addAttribute("categoryList", categoryList);

        return "index";
    }
}
