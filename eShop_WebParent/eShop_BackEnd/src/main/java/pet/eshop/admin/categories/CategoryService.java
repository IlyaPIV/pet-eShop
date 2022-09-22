package pet.eshop.admin.categories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pet.eshop.common.entity.Category;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class CategoryService {

    public static final int ROOT_CATEGORIES_PER_PAGE = 4;

    @Autowired
    private CategoryRepository repo;

    public List<Category> listAll(String sortDir) {
        Sort sort = Sort.by("name");

        if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else if (sortDir.equals("desc")){
            sort = sort.descending();
        }

        List<Category> rootCategories = repo.findRootCategories(sort);

        return listHierarchicalCategories(rootCategories, sortDir);
    }

    public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNum, String sortDir,
                                     String keyword) {
        Sort sort = Sort.by("name");

        if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else if (sortDir.equals("desc")){
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(pageNum -1, ROOT_CATEGORIES_PER_PAGE, sort);
        Page<Category> pageCategories = null;
        if(keyword!=null && !keyword.isEmpty()) {
            pageCategories = repo.search(keyword, pageable);
        } else {
            pageCategories = repo.findRootCategories(pageable);
        }
        pageInfo.setTotalElements(pageCategories.getTotalElements());
        pageInfo.setTotalPages(pageCategories.getTotalPages());

        if (keyword!=null && !keyword.isEmpty()) {
            List<Category> searchResult = pageCategories.getContent();
            for (Category cat: searchResult) {
                cat.setHasChildren(cat.getChildren().size()>0);
            }

            return searchResult;
        } else {
            return listHierarchicalCategories(pageCategories.getContent(), sortDir);
        }
    }

    /*
    * добавляет в список поочерёдно категории верхнего уровня, при этом вызывая заполнение вложенных в него категорий
     */
    private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir){
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category rootCat: rootCategories) {
            hierarchicalCategories.add(Category.copyFull(rootCat));

            Set<Category> children = sortSubCategories(rootCat.getChildren(), sortDir);
            for (Category child: children) {
                hierarchicalCategories.add(Category.copyFull(child, "-- "));

                listSubHierarchicalCategories(hierarchicalCategories, child, 1, sortDir);
            }
        }

        return hierarchicalCategories;
    }

    /*
    * рекурсивное заполнение категорий в глубину иерархии
     */
    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories,
                                               Category parent, int subLevel, String sortDir){
        int curLevel = subLevel + 1;
        Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);
        for (Category subCategory : children) {
            String prefix = "--".repeat(Math.max(0, curLevel)) +
                    " ";
            hierarchicalCategories.add(Category.copyFull(subCategory, prefix));
            listSubHierarchicalCategories(hierarchicalCategories, subCategory, curLevel, sortDir);
        }
    }

    public List<Category> listCategoriesUsedInForm(){
        List<Category> categoriesUsedInForm = new ArrayList<>();

//        Iterable<Category> categoriesInDB = repo.findAll();
        Iterable<Category> categoriesInDB = repo.findRootCategories(Sort.by("name").ascending());

        for (Category category : categoriesInDB) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(Category.copyIdAndName(category.getId(), category.getName()));

                Set<Category> children = sortSubCategories(category.getChildren());
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
        Set<Category> children = sortSubCategories(parent.getChildren());
        for (Category subCategory : children) {
            String prefix = "--".repeat(Math.max(0, curLevel)) +
                    " ";
            categoriesUsedInForm.add(Category.copyIdAndName(subCategory.getId(), prefix + subCategory.getName()));
            listSubCategoriesUsedInForm(subCategory, curLevel, categoriesUsedInForm);
        }
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children){
        return sortSubCategories(children, "asc");
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir){
        SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>(){
            @Override
            public int compare(Category cat1, Category cat2) {
                if (sortDir.equals("asc")) {
                    return cat1.getName().compareTo(cat2.getName());
                } else {
                    return cat2.getName().compareTo(cat1.getName());
                }
            }
        });

        sortedChildren.addAll(children);

        return sortedChildren;
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

    public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
        repo.updateEnabledStatus(id, enabled);
    }

    public void delete(Integer id) throws CategoryNotFoundException{
        Long countById = repo.countById(id);
        if (countById == null || countById == 0) {
            throw new CategoryNotFoundException("Could not find any Category with ID = " + id);
        }
        repo.deleteById(id);
    }
}
