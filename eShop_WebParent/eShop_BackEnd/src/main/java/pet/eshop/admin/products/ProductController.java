package pet.eshop.admin.products;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import pet.eshop.admin.categories.CategoryService;
import pet.eshop.admin.security.EShopUserDetails;
import pet.eshop.admin.util.FileUploadUtil;
import pet.eshop.common.entity.Brand;
import pet.eshop.common.entity.Category;
import pet.eshop.common.entity.Product;
import pet.eshop.common.entity.ProductImage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;

   // @GetMapping("/products")
    public String listAll(Model model){

        List<Product> productList = productService.listAll();

        model.addAttribute("listProducts", productList);

        return "products/products";
    }

    @GetMapping("/products")
    public String listFirstPage(Model model){
        return listByPage(1, model, "name", "asc", null, 0);
    }

    @GetMapping("/products/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                             @Param("sortField") String sortField, @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword,
                             @Param("categoryId") Integer categoryId) {

        Page<Product> page = productService.listByPage(pageNum, sortField, sortDir, keyword, categoryId);
        List<Product> productList = page.getContent();
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();

        long startCount = (long) (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
        long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        if (categoryId != null) model.addAttribute("categoryId", categoryId);

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listProducts", productList);
        model.addAttribute("moduleURL", "/products");
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

        if (loggedUser.hasRole("Salesperson")) {
            productService.saveProductPrice(product);
            redirectAttributes.addFlashAttribute("message", "The Product price has been updated successfully!");
        } else {
            setProductDetails(detailNames, detailValues, detailIDs, product);
            setMainImageName(mainImageMultipart, product);
            setExistingExtraImageNames(imageIDs, imageNames, product);
            setNewExtraImageNames(extraImageMultipart, product);

            Product savedProd = productService.save(product);
            saveUploadedImages(mainImageMultipart, extraImageMultipart, savedProd);
            deleteRemovedOnFormExtraImages(product);

            redirectAttributes.addFlashAttribute("message", "The Product has been saved successfully!");
        }

        return "redirect:/products";
    }

    private void deleteRemovedOnFormExtraImages(Product product) {
        String extraImageDir = "../product-images/" + product.getId() + "/extras";
        Path dirPath = Paths.get(extraImageDir);

        try {
            Files.list(dirPath).forEach(file -> {
                String filename = file.toFile().getName();
                if (!product.containsImageName(filename)) {
                    try {
                        Files.delete(file);
                        LOGGER.info("Deleted extra image: " + filename);
                    } catch (IOException e){
                        LOGGER.error("Could not delete extra image: " + filename);
                    }
                }
            });
        } catch (IOException ex) {
            LOGGER.error("Could not list directory: " + dirPath);
        }
    }

    private void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {
        if (imageIDs == null || imageIDs.length == 0) return;

        Set<ProductImage> images = new HashSet<>();

        for (int count = 0; count <imageIDs.length; count++){
            Integer id = Integer.parseInt(imageIDs[count]);
            String name = imageNames[count];
            images.add(new ProductImage(id, name, product));
        }

        product.setImages(images);
    }

    private void setProductDetails(String[] detailNames,
                                   String[] detailValues,
                                   String[] detailIDs, Product product) {
        if (detailNames == null || detailNames.length == 0) return;

        for (int count = 0; count < detailNames.length; count++) {
            String name = detailNames[count];
            String value = detailValues[count];
            int id = Integer.parseInt(detailIDs[count]);

            if (id !=0 ){
                product.addDetail(id, name, value);
            } else {
                if (!name.isEmpty() && !value.isEmpty()) {
                    product.addDetail(name, value);
                }
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
        //FileUploadUtil.cleanDir(uploadDir);
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

    private void setNewExtraImageNames(MultipartFile[] extraImageMultipart, Product product){
        if (extraImageMultipart.length > 0) {
            for(MultipartFile multipartFile: extraImageMultipart) {
                if (!multipartFile.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

                    if (!product.containsImageName(fileName)) {
                        product.addExtraImage(fileName);
                    }

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

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try {
            Product product = productService.get(id);

            List<Brand> brandsList = brandService.findAll();
            Integer numberOfExistingExtraImages = product.getImages().size();

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
