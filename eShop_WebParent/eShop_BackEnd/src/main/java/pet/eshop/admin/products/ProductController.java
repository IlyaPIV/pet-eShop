package pet.eshop.admin.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String saveProduct(Product product) {

        System.out.println("Prod Name = "   + product.getName());
        System.out.println("Brand = "       + product.getBrand().getName());
        System.out.println("Cat = "         + product.getCategory().getName());

        return "redirect:/products";
    }
}
