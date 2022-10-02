package pet.eshop.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pet.eshop.category.CategoryService;
import pet.eshop.common.entity.Category;
import pet.eshop.common.entity.Product;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/c/{category_alias}")
    public String viewCategoryFirstPage(@PathVariable(name = "category_alias") String catAlias,
                                     Model model) {
        return viewCategoryByPage(catAlias, 1, model);
    }

    @GetMapping("/c/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage(@PathVariable(name = "category_alias") String catAlias,
                               @PathVariable(name = "pageNum") int pageNum,
                               Model model) {

        Category category = categoryService.getCategory(catAlias);
        if (category == null) {
            return "error/404";
        }
        List<Category> listCategoryParents = categoryService.getCategoryParents(category);
        Page<Product> productPage = productService.listByCategory(pageNum, category.getId());
        List<Product> productList = productPage.getContent();
        long startCount = (long) (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
        long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
        if (endCount > productPage.getTotalElements()) {
            endCount = productPage.getTotalElements();
        }

        model.addAttribute("pageTitle", "eShop: " + category.getName());
        model.addAttribute("listCategoryParents", listCategoryParents);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("listProducts", productList);
        model.addAttribute("category", category);

        return "products_by_category";
    }
}
