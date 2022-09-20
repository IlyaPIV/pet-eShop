package pet.eshop.admin.categories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.eshop.common.entity.Category;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository repo;

    public List<Category> listAll() {
//       return (List<Category>) repo.findAll();
        List<Category> rootCategories = repo.findRootCategories();
        return listHierarchicalCategories(rootCategories);
    }

    /*
    * добавляет в список поочерёдно категории верхнего уровня, при этом вызывая заполнение вложенных в него категорий
     */
    private List<Category> listHierarchicalCategories(List<Category> rootCategories){
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCat: rootCategories) {
            hierarchicalCategories.add(Category.copyFull(rootCat));

            Set<Category> children = rootCat.getChildren();
            for (Category child: children) {
                hierarchicalCategories.add(Category.copyFull(child, "-- "));

                listSubHierarchicalCategories(hierarchicalCategories, child, 1);
            }
        }

        return hierarchicalCategories;
    }

    /*
    * рекурсивное заполнение категорий в глубину иерархии
     */
    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category parent, int subLevel){
        int curLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();
        for (Category subCategory : children) {
            String prefix = "--".repeat(Math.max(0, curLevel)) +
                    " ";
            hierarchicalCategories.add(Category.copyFull(subCategory, prefix));
            listSubHierarchicalCategories(hierarchicalCategories, subCategory, curLevel);
        }
    }

    public List<Category> listCategoriesUsedInForm(){
        List<Category> categoriesUsedInForm = new ArrayList<>();

        Iterable<Category> categoriesInDB = repo.findAll();

        for (Category category : categoriesInDB) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(Category.copyIdAndName(category.getId(), category.getName()));

                Set<Category> children = category.getChildren();
                for (Category subCategory : children) {
                    categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(),"-- " + subCategory.getName()));
                    listSubCategoriesUsedInForm(subCategory, 1, categoriesUsedInForm);
                }
            }
        }

        return categoriesUsedInForm;
    }

    private void listSubCategoriesUsedInForm(Category parent, int subLevel, List<Category> categoriesUsedInForm){
        int curLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();
        for (Category subCategory : children) {
            String prefix = "--".repeat(Math.max(0, curLevel)) +
                    " ";
            categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), prefix + subCategory.getName()));
            listSubCategoriesUsedInForm(subCategory, curLevel, categoriesUsedInForm);
        }
    }

    public Category save(Category category) {
        return repo.save(category);
    }

    public Category get(Integer id) throws CategoryNotFoundException {
        try {
            return repo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }
    }

    public String checkUnique(Integer id, String name, String alias){
        boolean isCreatingNew = (id == null || id == 0);
        Category categoryByName = repo.findByName(name);
        if (isCreatingNew) {
            if (categoryByName != null) {
                return "DuplicateName";
            } else {
                Category categoryByAlias = repo.findByAlias(alias);
                if (categoryByAlias != null) {
                    return "DuplicateAlias";
                }
            }
        } else {
            if (categoryByName != null && categoryByName.getId() != id) {
                return "DuplicateName";
            }
            Category categoryByAlias = repo.findByAlias(alias);
            if (categoryByAlias != null && categoryByAlias.getId() != id) {
                return "DuplicateAlias";
            }
        }

        return "OK";
    }

}
