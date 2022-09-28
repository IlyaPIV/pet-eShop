package pet.eshop.admin.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pet.eshop.admin.brands.BrandService;
import pet.eshop.admin.util.FileUploadUtil;
import pet.eshop.common.entity.Brand;
import pet.eshop.common.entity.Product;

import java.io.IOException;
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
    public String saveProduct(Product product,
                              @RequestParam("fileImage") MultipartFile mainImageMultipart,
                              @RequestParam("extraImage") MultipartFile[] extraImageMultipart,
                              @RequestParam(name = "detailNames", required = false) String[] detailNames,
                              @RequestParam(name = "detailValues", required = false) String[] detailValues,
                              RedirectAttributes redirectAttributes) throws IOException {

        setProductDetails(detailNames, detailValues, product);
        setMainImageName(mainImageMultipart, product);
        setExtraImageNames(extraImageMultipart, product);


        Product savedProd = productService.save(product);
        saveUploadedImages(mainImageMultipart, extraImageMultipart, savedProd);

        redirectAttributes.addFlashAttribute("message", "The Product has been saved successfully!");

        return "redirect:/products";
    }

    private void setProductDetails(String[] detailNames, String[] detailValues, Product product) {
        if (detailNames == null || detailNames.length == 0) return;

        for (int count = 0; count < detailNames.length; count++) {
            String name = detailNames[count];
            String value = detailValues[count];

            if (!name.isEmpty() && !value.isEmpty()) {
                product.addDetail(name, value);
            }
        }
    }

    private void saveUploadedImages(MultipartFile mainImageMultipart,
                                    MultipartFile[] extraImageMultipart,
                                    Product savedProd) throws IOException {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
            String uploadDir = "../product-images/" + savedProd.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
        }

        String uploadDir = "../product-images/" + savedProd.getId() + "/extras";
        FileUploadUtil.cleanDir(uploadDir);
        if (extraImageMultipart.length > 0) {
            for (MultipartFile multipartFile : extraImageMultipart) {
                if (multipartFile.isEmpty()) continue;
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            }
        }
    }

    private void setMainImageName(MultipartFile mainImageMultipart, Product product){
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
            product.setMainImage(fileName);
        }
    }

    private void setExtraImageNames(MultipartFile[] extraImageMultipart, Product product){
        if (extraImageMultipart.length > 0) {
            for(MultipartFile multipartFile: extraImageMultipart) {
                if (!multipartFile.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    product.addExtraImage(fileName);
                }
            }
        }
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
}
