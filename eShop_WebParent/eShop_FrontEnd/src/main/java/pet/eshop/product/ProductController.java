package pet.eshop.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pet.eshop.category.CategoryService;
import pet.eshop.common.entity.Category;
import pet.eshop.common.entity.Product;
import pet.eshop.common.exception.CategoryNotFoundException;
import pet.eshop.common.exception.ProductNotFoundException;

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
        try {
            Category category = categoryService.getCategory(catAlias);

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

            return "product/products_by_category";
        } catch (CategoryNotFoundException ex){
            return "error/404";
        }
    }

    @GetMapping("/p/{product_alias}")
    public String viewProductDetail(@PathVariable("product_alias") String alias, Model model) {
        try {
            Product product = productService.getProduct(alias);
            List<Category> listCategoryParents = categoryService.getCategoryParents(product.getCategory());

            model.addAttribute("pageTitle", "eShop: " + product.getShortName());
            model.addAttribute("listCategoryParents", listCategoryParents);
            model.addAttribute("product", product);

            return "product/product_details";
        } catch (ProductNotFoundException ex) {
            return "error/404";
        }
    }

    @GetMapping("/search")
    public String search(@Param("keyword") String keyword,
                         Model model){
        return searchByPage(keyword, 1, model);
    }

    @GetMapping("/search/page/{pageNum}")
    public String searchByPage(@Param("keyword") String keyword,
                         @PathVariable("pageNum") int pageNum,
                         Model model) {

        Page<Product> pageResult = productService.search(keyword, pageNum);
        List<Product> listResult = pageResult.getContent();

        long startCount = (long) (pageNum - 1) * ProductService.SEARCH_RESULTS_PER_PAGE + 1;
        long endCount = startCount + ProductService.SEARCH_RESULTS_PER_PAGE - 1;
        if (endCount > pageResult.getTotalElements()) {
            endCount = pageResult.getTotalElements();
        }


        model.addAttribute("keyword", keyword);
        model.addAttribute("listResult", listResult);
        model.addAttribute("pageTitle", keyword + " - Search Result:");
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", pageResult.getTotalElements());

        return "product/search_result";
    }
}
