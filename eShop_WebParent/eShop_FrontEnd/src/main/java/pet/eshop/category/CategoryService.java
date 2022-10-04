package pet.eshop.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pet.eshop.common.entity.Category;
import pet.eshop.common.exception.CategoryNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repo;

    public List<Category> listNoChildrenCategories(){
        List<Category> listNoChildrenCategories = new ArrayList<>();
        List<Category> listAllEnabledCategories = repo.findAllEnabled();

        listAllEnabledCategories.forEach(category -> {
            Set<Category> children = category.getChildren();
            if (children == null || children.size() == 0) {
                listNoChildrenCategories.add(category);
            }
        });

        return listNoChildrenCategories;
    }

    public Category getCategory(String alias) throws CategoryNotFoundException {
        Category category = repo.findByAliasEnabled(alias);

        if (category == null) {
            throw new CategoryNotFoundException("Could not find any enabled Category with alias = " + alias);
        }
        return repo.findByAliasEnabled(alias);
    }

    public List<Category> getCategoryParents(Category child) {
        List<Category> listParents = new ArrayList<>();

        Category parent = child.getParent();
        while (parent != null) {
            listParents.add(0, parent);
            parent = parent.getParent();
        }
        listParents.add(child);

        return listParents;
    }
}
