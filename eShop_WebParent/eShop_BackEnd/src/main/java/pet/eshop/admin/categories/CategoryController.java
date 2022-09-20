package pet.eshop.admin.categories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pet.eshop.admin.util.FileUploadUtil;
import pet.eshop.common.entity.Category;

import java.io.IOException;
import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping("/categories")
    public String listFirstPage(@Param("sortDir") String sortDir, Model model){

        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }

        List<Category> listCategories = service.listAll(sortDir);

        model.addAttribute("listCategories", listCategories);
        model.addAttribute("keyword", null);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "categories/categories";
    }

    @GetMapping("categories/new")
    public String newCategory(Model model){
        List<Category> listCategories = service.listCategoriesUsedInForm();

        model.addAttribute("category", new Category());
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("pageTitle", "Create New Category");

        return "categories/category_form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(Category category,
                               @RequestParam("fileImage")MultipartFile multipartFile,
                                RedirectAttributes redirectAttributes) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(fileName);

            Category savedCategory = service.save(category);
            String uploadDir = "../category-images/" + savedCategory.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            service.save(category);
        }

        redirectAttributes.addFlashAttribute("message", "The category has been saved successfully.");
        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes){

        try {
            Category cat = service.get(id);
            model.addAttribute("category", cat);
            model.addAttribute("pageTitle", "Edit category (ID: " + id + ")");

            List<Category> listCategories = service.listCategoriesUsedInForm();
            model.addAttribute("listCategories", listCategories);

            return "categories/category_form";

        } catch (CategoryNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/categories";
        }

    }
}
