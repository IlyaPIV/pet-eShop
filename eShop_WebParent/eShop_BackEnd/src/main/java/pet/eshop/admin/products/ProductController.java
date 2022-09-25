package pet.eshop.admin.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pet.eshop.admin.brands.BrandService;
import pet.eshop.common.entity.Brand;
import pet.eshop.common.entity.Product;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    @GetMapping("/products")
    public String listAll(Model model){

        List<Product> productList = productService.listAll();

        model.addAttribute("listProducts", productList);

        return "products/products";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model){

        List<Brand> brandsList = brandService.findAll();
        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("product", product);
        model.addAttribute("listBrands", brandsList);
        model.addAttribute("pageTitle", "Create new Product");

        return "products/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product, RedirectAttributes redirectAttributes) {

        productService.save(product);

        redirectAttributes.addFlashAttribute("message", "The Product has been saved successfully!");

        return "redirect:/products";
    }

    @GetMapping("/products/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable(name = "id") Integer id,
                                              @PathVariable(name = "status") boolean enabled,
                                              RedirectAttributes redirectAttributes) {
        productService.updateProductEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The Product ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes){
        try {
            productService.delete(id);

            redirectAttributes.addFlashAttribute("message",
                    "The Product ID=" + id + " was deleted successfully!");
        } catch (ProductNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/products";
    }
}
