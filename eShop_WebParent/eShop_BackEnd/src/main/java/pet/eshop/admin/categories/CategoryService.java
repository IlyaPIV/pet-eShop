package pet.eshop.admin.categories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pet.eshop.common.entity.Category;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository repo;

    public List<Category> listAll() {
        return (List<Category>) repo.findAll();
    }

    public List<Category> listCategories(String sortField, String sortDir, String keyword){
//        Sort sort = Sort.by(sortField);
//        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        return (List<Category>) repo.findAll(keyword);
    }

    public List<Category> listCategoriesUsedInForm(){
        List<Category> categoriesUsedInForm = new ArrayList<>();

        Iterable<Category> categoriesInDB = repo.findAll();

        for (Category category : categoriesInDB) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(new Category(category.getName()));

                Set<Category> children = category.getChildren();
                for (Category subCategory : children) {
                    categoriesUsedInForm.add(new Category("--" + subCategory.getName()));
                    listChildrenCat(subCategory, 1, categoriesUsedInForm);
                }
            }
        }

        return categoriesUsedInForm;
    }

    private void listChildrenCat(Category parent, int subLevel, List<Category> categoriesUsedInForm){
        int curLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();
        for (Category subCategory : children) {
            StringBuilder prefix = new StringBuilder();
            for (int i = 0; i < curLevel; i++) {
                prefix.append("--");
            }
            categoriesUsedInForm.add(new Category(prefix + subCategory.getName()));
            listChildrenCat(subCategory, curLevel, categoriesUsedInForm);
        }
    }


}
