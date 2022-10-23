package pet.eshop.admin.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pet.eshop.admin.brands.BrandService;
import pet.eshop.admin.categories.CategoryService;
import pet.eshop.admin.paging.PagingAndSortingHelper;
import pet.eshop.admin.paging.PagingAndSortingParam;
import pet.eshop.admin.security.EShopUserDetails;
import pet.eshop.admin.util.FileUploadUtil;
import pet.eshop.common.entity.Brand;
import pet.eshop.common.entity.Category;
import pet.eshop.common.entity.Product;
import pet.eshop.common.exception.ProductNotFoundException;

import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String listFirstPage(){
        return "redirect:/products/page/1?sortField=name&sortDir=asc&categoryId=0";
    }

    @GetMapping("/products/page/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "listProducts", moduleURL = "/products")PagingAndSortingHelper helper,
                             @PathVariable(name = "pageNum") int pageNum, Model model,
                             @Param("categoryId") Integer categoryId) {

        productService.listByPage(pageNum, helper, categoryId);

        List<Category> listCategories = categoryService.listCategoriesUsedInForm();

        if (categoryId != null) model.addAttribute("categoryId", categoryId);
        model.addAttribute("listCategories", listCategories);

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
        model.addAttribute("numberOfExistingExtraImages", 0);

        return "products/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product,
                              @RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,
                              @RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultipart,
                              @RequestParam(name = "detailIDs", required = false) String[] detailIDs,
                              @RequestParam(name = "detailNames", required = false) String[] detailNames,
                              @RequestParam(name = "detailValues", required = false) String[] detailValues,
                              @RequestParam(name = "imageIDs", required = false) String[] imageIDs,
                              @RequestParam(name = "imageNames", required = false) String[] imageNames,
                              @AuthenticationPrincipal EShopUserDetails loggedUser,
                              RedirectAttributes redirectAttributes) throws IOException {

        if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
            if (loggedUser.hasRole("Salesperson")){
                productService.saveProductPrice(product);
                redirectAttributes.addFlashAttribute("message", "The Product price has been updated successfully!");
                return "redirect:/products";
            }
        }

        ProductSaveHelper.setProductDetails(detailNames, detailValues, detailIDs, product);
        ProductSaveHelper.setMainImageName(mainImageMultipart, product);
        ProductSaveHelper.setExistingExtraImageNames(imageIDs, imageNames, product);
        ProductSaveHelper.setNewExtraImageNames(extraImageMultipart, product);

        Product savedProd = productService.save(product);
        ProductSaveHelper.saveUploadedImages(mainImageMultipart, extraImageMultipart, savedProd);
        ProductSaveHelper.deleteRemovedOnFormExtraImages(product);

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

            String extrasDir = "../product-images/" + id + "/extras";
            String mainDir = "../product-images/" + id;
            FileUploadUtil.removeDir(extrasDir);
            FileUploadUtil.removeDir(mainDir);

            redirectAttributes.addFlashAttribute("message",
                    "The Product ID=" + id + " was deleted successfully!");
        } catch (ProductNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id, Model model,
                              RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal EShopUserDetails loggedUser){

        try {
            Product product = productService.get(id);

            List<Brand> brandsList = brandService.findAll();
            Integer numberOfExistingExtraImages = product.getImages().size();

            boolean readonlyForSalesperson = false;
            if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
                if (loggedUser.hasRole("Salesperson")){
                    readonlyForSalesperson = true;
                }
            }

            model.addAttribute("readonlyForSalesperson", readonlyForSalesperson);
            model.addAttribute("product", product);
            model.addAttribute("pageTitle", "Edit Product (ID: " + id + " )");
            model.addAttribute("listBrands", brandsList);
            model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

            return "products/product_form";

        } catch (ProductNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/products";
        }
    }

    @GetMapping("/products/detail/{id}")
    public String viewProduct(@PathVariable("id") Integer id, Model model,
                              RedirectAttributes redirectAttributes){
        try {
            Product product = productService.get(id);

            model.addAttribute("product", product);

            return "products/product_detail_modal";

        } catch (ProductNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/products";
        }
    }
}
