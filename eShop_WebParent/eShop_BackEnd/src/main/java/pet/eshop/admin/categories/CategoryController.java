package pet.eshop.admin.categories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String listFirstPage(Model model){

//      return listWithSort(model, "id", "asc", null);

        List<Category> listCategories = service.listAll();
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("keyword", null);
        model.addAttribute("sortDir", null);
        model.addAttribute("sortField", null);

        return "categories/categories";
    }

    //@GetMapping("/categories/page/{pageNum}")
    public String listWithSort(Model model,
                               String sortField, String sortDir, String keyword) { //@Param("имя_парам")

        List<Category> listCategories = null;
        if (keyword == null) listCategories = service.listAll();
                else listCategories = service.listCategories(sortField, sortDir, keyword);
        System.out.println(listCategories);

        model.addAttribute("listCategories", listCategories);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sortField", sortField);

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

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        category.setImage(fileName);

        Category savedCategory = service.save(category);
        String uploadDir = "../category-images/" + savedCategory.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        redirectAttributes.addFlashAttribute("message", "The category has been saved successfully.");
        return "redirect:/categories";
    }

}
